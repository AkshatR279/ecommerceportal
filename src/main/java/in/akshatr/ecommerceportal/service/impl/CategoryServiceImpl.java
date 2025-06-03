package in.akshatr.ecommerceportal.service.impl;

import in.akshatr.ecommerceportal.entity.CategoryEntity;
import in.akshatr.ecommerceportal.io.CategoryRequest;
import in.akshatr.ecommerceportal.io.CategoryResponse;
import in.akshatr.ecommerceportal.repository.CategoryRepository;
import in.akshatr.ecommerceportal.repository.ItemRepository;
import in.akshatr.ecommerceportal.service.CategoryService;
import in.akshatr.ecommerceportal.service.FileUploadService;
import in.akshatr.ecommerceportal.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;
    private final ItemRepository itemRepository;

    @Override
    public CategoryResponse add(CategoryRequest request, MultipartFile file, HttpServletRequest servRequest) throws IOException {
        //String imgUrl= fileUploadService.uploadFile(file, servRequest);
        String imgUrl = uploadFile(file);
        CategoryEntity newCategory = convertToEntity(request);
        newCategory.setImgUrl(imgUrl);
        newCategory = categoryRepository.save(newCategory);

        return convertToResponse(newCategory);
    }

    @Override
    public List<CategoryResponse> read() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public void delete(String categoryId) {
        CategoryEntity existingCategory = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: "+categoryId));

        boolean isFileDeleted= deleteFile(existingCategory.getImgUrl());
        if(isFileDeleted){
            categoryRepository.delete(existingCategory);
        }
        else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete image file.");
        }
    }

    private CategoryResponse convertToResponse(CategoryEntity newCategory) {
        Integer items = itemRepository.countByCategoryId(newCategory.getId());
        return CategoryResponse.builder()
                .categoryId(newCategory.getCategoryId())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgColor(newCategory.getBgColor())
                .imgUrl(newCategory.getImgUrl())
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .items(items)
                .build();
    }

    private CategoryEntity convertToEntity(CategoryRequest request){
        return CategoryEntity.builder()
                .categoryId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .bgColor(request.getBgColor())
                .build();
    }

    private String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path uploadPath=Paths.get("uploads").toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return "http://localhost:8080/api/v1.0/uploads/" + fileName;
    }

    private boolean deleteFile(String imgUrl)  {
        boolean fileDeleted = false;

        try{
            String fileName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
            Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
            String filePath = String.valueOf(uploadPath.resolve(fileName));
            Files.deleteIfExists(Path.of(filePath));

            fileDeleted=true;
        } catch (Exception _) {

        }

        return fileDeleted;
    }
}
