package in.akshatr.ecommerceportal.service.impl;

import in.akshatr.ecommerceportal.service.FileUploadService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.http.HttpRequest;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    @Override
    public String uploadFile(MultipartFile file, HttpServletRequest request) {
        String uploadedFileUrl="";

        try{
            if(!file.isEmpty()){
                String uploadDir="/uploads/";
                String uploadPath=request.getServletContext().getRealPath(uploadDir);

                if(!new File(uploadPath).exists()){
                    boolean dirCreated = new File(uploadPath).mkdir();
                }

                String fileExt = StringUtils.getFilenameExtension(file.getOriginalFilename());
                String fileName= "Upload_" + Long.toString(System.currentTimeMillis()) + "." + fileExt;
                String filePath = uploadPath + fileName;

                File destFile=new File(filePath);
                file.transferTo(destFile);

                uploadedFileUrl = fileName;
            }
        } catch (Exception ex)
        {
            throw new RuntimeException("Error uploading file: "+ex.getMessage());
        }

        return uploadedFileUrl;
    }

    @Override
    public boolean deleteFile(String imgUrl) {
        return true;
    }
}
