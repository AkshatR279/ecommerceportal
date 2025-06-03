package in.akshatr.ecommerceportal.service.impl;

import in.akshatr.ecommerceportal.entity.OrderEntity;
import in.akshatr.ecommerceportal.entity.OrderItemEntity;
import in.akshatr.ecommerceportal.io.*;
import in.akshatr.ecommerceportal.repository.OrderEntityRepository;
import in.akshatr.ecommerceportal.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderEntityRepository orderEntityRepository;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        OrderEntity newOrder = convertToOrderEntity(request);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setStatus(newOrder.getPaymentMethod() == PaymentMethod.CASH ?
                PaymentDetails.PaymentStatus.COMPLETED : PaymentDetails.PaymentStatus.PENDING);
        newOrder.setPaymentDetails(paymentDetails);

        List<OrderItemEntity> orderItemEntityList = request.getCartItems().stream()
                .map(this::convertToItemEntity)
                .toList();
        newOrder.setItems(orderItemEntityList);

        newOrder = orderEntityRepository.save(newOrder);

        return convertToResponse(newOrder);
    }

    private OrderEntity convertToOrderEntity(OrderRequest request) {
        return OrderEntity.builder()
                .customerName(request.getCustomerName())
                .mobileNumber(request.getMobileNumber())
                .subtotal(request.getSubtotal())
                .tax(request.getTax())
                .grandTotal(request.getGrandTotal())
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
                .build();
    }

    private OrderItemEntity convertToItemEntity(OrderRequest.OrderItemRequest request) {
        return OrderItemEntity.builder()
                .itemId(request.getItemId())
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();
    }

    private OrderResponse convertToResponse(OrderEntity newOrder) {
        return OrderResponse.builder()
                .orderId(newOrder.getOrderId())
                .customerName(newOrder.getCustomerName())
                .mobileNumber(newOrder.getMobileNumber())
                .subtotal(newOrder.getSubtotal())
                .tax(newOrder.getTax())
                .grandTotal(newOrder.getGrandTotal())
                .paymentMethod(newOrder.getPaymentMethod())
                .createdAt(newOrder.getCreatedAt())
                .items(newOrder.getItems().stream().map(this::convertToItemResponse).toList())
                .paymentDetails(newOrder.getPaymentDetails())
                .build();
    }

    private OrderResponse.OrderItemResponse convertToItemResponse(OrderItemEntity request){
        return OrderResponse.OrderItemResponse.builder()
                .itemId(request.getItemId())
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();
    }

    @Override
    public void deleteOrder(String orderId) {
        OrderEntity existingOrder = orderEntityRepository.findByOrderId(orderId)
                .orElseThrow(()->new RuntimeException("Order not found: " + orderId));
        orderEntityRepository.delete(existingOrder);
    }

    @Override
    public List<OrderResponse> getLatestOrders() {
        return orderEntityRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public OrderResponse verifyPayment(PaymentVerificationRequest request) {
        OrderEntity existingOrder = orderEntityRepository.findByOrderId(request.getOrderId())
                .orElseThrow(()->new RuntimeException("Order not found."));

        if(!verifyRazorpaySignature(request.getRazorpayOrderId(),request.getRazorPaymentId(),request.getRazorSignature())){
            throw new RuntimeException("Payment verification failed.");
        }

        PaymentDetails paymentDetails = existingOrder.getPaymentDetails();
        paymentDetails.setRazorpayOrderId(request.getRazorpayOrderId());
        paymentDetails.setRazorpayPaymentId(request.getRazorPaymentId());
        paymentDetails.setRazorpaySignature(request.getRazorSignature());
        paymentDetails.setStatus(PaymentDetails.PaymentStatus.COMPLETED);

        existingOrder = orderEntityRepository.save(existingOrder);
        return convertToResponse(existingOrder);
    }

    @Override
    public Double sumSalesByDate(LocalDate date) {
        return orderEntityRepository.sumSalesByDate(date);
    }

    @Override
    public Long countByOrderDate(LocalDate date) {
        return orderEntityRepository.countByOrderDate(date);
    }

    @Override
    public List<OrderResponse> findRecentOrders() {
        return orderEntityRepository.findRecentOrders(PageRequest.of(0,5))
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private boolean verifyRazorpaySignature(String razorpayOrderId, String razorPaymentId, String razorSignature) {
        return true;
    }
}
