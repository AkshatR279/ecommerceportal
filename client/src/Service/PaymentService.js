import axios from "axios";

export const createRazorpayOrder = async (paymentData) => {
    return await axios.post("http://localhost:8080/api/v1.0/payments/create-order", paymentData, { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
}

export const verifyPayment = async (paymentData) => {
    return await axios.post("http://localhost:8080/api/v1.0/payments/verify", paymentData, { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
}