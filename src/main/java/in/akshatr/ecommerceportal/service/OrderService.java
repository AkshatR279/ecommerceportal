package in.akshatr.ecommerceportal.service;

import in.akshatr.ecommerceportal.io.OrderRequest;
import in.akshatr.ecommerceportal.io.OrderResponse;
import in.akshatr.ecommerceportal.io.PaymentVerificationRequest;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    void deleteOrder(String orderId);
    List<OrderResponse> getLatestOrders();
    OrderResponse verifyPayment(PaymentVerificationRequest request);
    Double sumSalesByDate(LocalDate date);
    Long countByOrderDate(LocalDate date);
    List<OrderResponse> findRecentOrders();
}
