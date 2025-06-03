package in.akshatr.ecommerceportal.service;

import in.akshatr.ecommerceportal.io.UserRequest;
import in.akshatr.ecommerceportal.io.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    String getUserRole(String email);
    List<UserResponse> readUsers();
    void deleteUser(String id);
}
