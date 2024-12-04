package com.example.webshopmenswear.config.VNPay;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VNPAYConfig {

    // URL thanh toán VNPay (sandbox)
    public static final String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    // URL callback sau thanh toán
    public static final String VNP_RETURN_URL = "/vnpay-payment-return";
    // Mã TmnCode (nhận qua email từ VNPay sau khi đăng ký)
    public static final String VNP_TMN_CODE = "OZH4SRSA"; // Thay YOUR_TMN_CODE bằng mã thực tế
    // Chuỗi bí mật để mã hóa
    public static final String VNP_HASH_SECRET = "69L8EJ5ML8KRNF8MWVBE0K0F2B1QOWB6";
    // URL API giao dịch (sandbox)
    public static final String VNP_API_URL = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    /**
     * Hàm hash toàn bộ các trường (fields) để tạo chữ ký.
     *
     * @param fields Bản đồ chứa các trường dữ liệu.
     * @return Chuỗi hash được mã hóa HmacSHA512.
     */
    public static String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        // Sắp xếp các tên trường theo thứ tự ABC
        Collections.sort(fieldNames);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = fields.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                sb.append(fieldName).append("=").append(fieldValue);
                if (i < fieldNames.size() - 1) {
                    sb.append("&");
                }
            }
        }

        return hmacSHA512(VNP_HASH_SECRET, sb.toString());
    }

    /**
     * Hàm mã hóa dữ liệu bằng thuật toán HmacSHA512.
     *
     * @param key  Chuỗi bí mật (secret key).
     * @param data Dữ liệu cần mã hóa.
     * @return Chuỗi mã hóa HmacSHA512.
     */
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException("Key or data is null");
            }

            Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);

            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);

            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }

            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * Hàm lấy địa chỉ IP của request.
     *
     * @param request Đối tượng HttpServletRequest.
     * @return Địa chỉ IP.
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    /**
     * Hàm tạo chuỗi số ngẫu nhiên có độ dài `len`.
     *
     * @param len Độ dài chuỗi số cần tạo.
     * @return Chuỗi số ngẫu nhiên.
     */
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        String chars = "0123456789";

        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
