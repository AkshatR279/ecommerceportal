package in.akshatr.ecommerceportal.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile file, HttpServletRequest request);
    boolean deleteFile(String imgUrl);
}
