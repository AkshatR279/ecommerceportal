package in.akshatr.ecommerceportal.service;

import in.akshatr.ecommerceportal.io.ItemRequest;
import in.akshatr.ecommerceportal.io.ItemResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ItemService {
    ItemResponse add(ItemRequest request, MultipartFile file, HttpServletRequest servRequest) throws IOException;
    List<ItemResponse> fetchItems();
    void deleteItem(String itemId);
}
