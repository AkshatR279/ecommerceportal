package in.akshatr.ecommerceportal.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerificationRequest {
    private String razorpayOrderId;
    private String razorPaymentId;
    private String razorSignature;
    private String orderId;
}
