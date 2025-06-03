package in.akshatr.ecommerceportal.controller;

import com.razorpay.RazorpayException;
import in.akshatr.ecommerceportal.io.OrderResponse;
import in.akshatr.ecommerceportal.io.PaymentRequest;
import in.akshatr.ecommerceportal.io.PaymentVerificationRequest;
import in.akshatr.ecommerceportal.io.RazorpayOrderResponse;
import in.akshatr.ecommerceportal.service.OrderService;
import in.akshatr.ecommerceportal.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final RazorpayService razorpayService;
    private final OrderService orderService;

    @PostMapping("/create-order")
    @ResponseStatus(HttpStatus.CREATED)
    public RazorpayOrderResponse createRazorpayOrder(@RequestBody PaymentRequest request) throws RazorpayException{
        return razorpayService.createOrder(request.getAmount(), request.getCurrency());
    }

    @PostMapping("verify")
    public OrderResponse verifyPayment(@RequestBody PaymentVerificationRequest request){
        return orderService.verifyPayment(request);
    }
}
