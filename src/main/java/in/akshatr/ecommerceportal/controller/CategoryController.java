package in.akshatr.ecommerceportal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.akshatr.ecommerceportal.io.CategoryRequest;
import in.akshatr.ecommerceportal.io.CategoryResponse;
import in.akshatr.ecommerceportal.service.CategoryService;
import in.akshatr.ecommerceportal.service.FileUploadService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategory(@RequestPart("category") String categoryString, @RequestPart("file")MultipartFile file,  HttpServletRequest servRequest){
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryRequest request=null;
        try{
            request=objectMapper.readValue(categoryString, CategoryRequest.class);
            return categoryService.add(request, file, servRequest);
        }
        catch (JsonProcessingException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occurred while parsing JSON: "+ex.getMessage());
        }
        catch (IOException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error uploading file: "+ex.getMessage());
        }
    }

    @GetMapping("/categories")
    public List<CategoryResponse> fetchCategory(){
        return categoryService.read();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/categories/{categoryId}")
    public void remove(@PathVariable String categoryId){
        try{
            categoryService.delete(categoryId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }
}
