package in.akshatr.ecommerceportal.service;

import in.akshatr.ecommerceportal.io.CategoryRequest;
import in.akshatr.ecommerceportal.io.CategoryResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    CategoryResponse add(CategoryRequest request, MultipartFile file, HttpServletRequest servRequest) throws IOException;
    List<CategoryResponse> read();
    void delete(String categoryId);
}
