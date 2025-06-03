package in.akshatr.ecommerceportal.service;

import com.razorpay.RazorpayException;
import in.akshatr.ecommerceportal.io.RazorpayOrderResponse;

public interface RazorpayService {
    RazorpayOrderResponse createOrder(Double amount, String currency) throws RazorpayException;
}
