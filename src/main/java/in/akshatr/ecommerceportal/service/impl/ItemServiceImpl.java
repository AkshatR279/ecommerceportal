package in.akshatr.ecommerceportal.service.impl;

import in.akshatr.ecommerceportal.entity.CategoryEntity;
import in.akshatr.ecommerceportal.entity.ItemEntity;
import in.akshatr.ecommerceportal.io.ItemRequest;
import in.akshatr.ecommerceportal.io.ItemResponse;
import in.akshatr.ecommerceportal.repository.CategoryRepository;
import in.akshatr.ecommerceportal.repository.ItemRepository;
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
public class ItemServiceImpl implements ItemService {
    private final FileUploadService fileUploadService;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemResponse add(ItemRequest request, MultipartFile file, HttpServletRequest servRequest) throws IOException {
        //String imgUrl= fileUploadService.uploadFile(file, servRequest);
        String imgUrl = uploadFile(file);
        ItemEntity newItem=convertToEntity(request);
        CategoryEntity existingCategory = categoryRepository.findByCategoryId(request.getCategoryId())
                .orElseThrow(()->new RuntimeException("Category not found: "+request.getCategoryId()));
        newItem.setCategory(existingCategory);
        newItem.setImgUrl(imgUrl);
        newItem = itemRepository.save(newItem);
        
        return convertToResponse(newItem);
    }

    private ItemResponse convertToResponse(ItemEntity newItem) {
        return ItemResponse.builder()
                .itemId(newItem.getItemId())
                .name(newItem.getName())
                .description(newItem.getDescription())
                .price(newItem.getPrice())
                .imgUrl(newItem.getImgUrl())
                .categoryName(newItem.getCategory().getName())
                .categoryId(newItem.getCategory().getCategoryId())
                .createdAt(newItem.getCreatedAt())
                .updatedAt(newItem.getUpdatedAt())
                .build();
    }

    private ItemEntity convertToEntity(ItemRequest request) {
        return ItemEntity.builder()
                .itemId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
    }

    @Override
    public List<ItemResponse> fetchItems() {
        return itemRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public void deleteItem(String itemId) {
        ItemEntity existingItem = itemRepository.findByItemId(itemId)
                .orElseThrow(()->new RuntimeException("Item not found: "+itemId));
        //boolean isFileDeleted= fileUploadService.deleteFile(existingItem.getImgUrl());
        boolean isFileDeleted= deleteFile(existingItem.getImgUrl());
        if(isFileDeleted){
            itemRepository.delete(existingItem);
        }
        else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete image file.");
        }
    }

    private String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path uploadPath= Paths.get("uploads").toAbsolutePath().normalize();
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
