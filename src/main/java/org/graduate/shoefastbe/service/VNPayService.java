package org.graduate.shoefastbe.service;
import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.payment.VNPayConfig;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class VNPayService {
//    private VNPayConfig VNPayConfig;
//
//public String createOrder(int total, String orderInfor, String urlReturn) {
//    String vnp_Version = "2.1.0";
//    String vnp_Command = "pay";
//    String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
//    String vnp_IpAddr = "127.0.0.1";
//    String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
//    String orderType = "order-type";
//
//    Map<String, String> vnp_Params = new HashMap<>();
//    vnp_Params.put("vnp_Version", vnp_Version);
//    vnp_Params.put("vnp_Command", vnp_Command);
//    vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//    vnp_Params.put("vnp_Amount", String.valueOf(total * 100)); // Tổng tiền theo VNPay yêu cầu nhân với 100
//    vnp_Params.put("vnp_CurrCode", "VND");
//
//    vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//    vnp_Params.put("vnp_OrderInfo", orderInfor);
//    vnp_Params.put("vnp_OrderType", orderType);
//
//    String locate = "vn";
//    vnp_Params.put("vnp_Locale", locate);
//
//    urlReturn += VNPayConfig.vnp_Returnurl;
//    vnp_Params.put("vnp_ReturnUrl", urlReturn);
//    vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//    // Thiết lập thời gian tạo và thời gian hết hạn cho giao dịch
//    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//    String vnp_CreateDate = formatter.format(cld.getTime());
//    vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//    // Cài đặt thời gian hết hạn là 15 phút (có thể điều chỉnh theo yêu cầu VNPay)
//    cld.add(Calendar.MINUTE, 15);
//    String vnp_ExpireDate = formatter.format(cld.getTime());
//    vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//    List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
//    Collections.sort(fieldNames);
//    StringBuilder hashData = new StringBuilder();
//    StringBuilder query = new StringBuilder();
//
//    for (String fieldName : fieldNames) {
//        String fieldValue = vnp_Params.get(fieldName);
//        if (fieldValue != null && fieldValue.length() > 0) {
//            try {
//                hashData.append(fieldName).append('=')
//                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()))
//                        .append('=')
//                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            query.append('&');
//            hashData.append('&');
//        }
//    }
//
//    // Loại bỏ ký tự & cuối cùng của query
//    if (query.length() > 0) query.setLength(query.length() - 1);
//    if (hashData.length() > 0) hashData.setLength(hashData.length() - 1);
//
//    String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
//    query.append("&vnp_SecureHash=").append(vnp_SecureHash);
//
//    String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query.toString();
//    return paymentUrl;
//}
//
//
//    public int orderReturn(HttpServletRequest request){
//        Map fields = new HashMap();
//        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
//            String fieldName = null;
//            String fieldValue = null;
//            try {
//                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
//                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                fields.put(fieldName, fieldValue);
//            }
//        }
//
//        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
//        if (fields.containsKey("vnp_SecureHashType")) {
//            fields.remove("vnp_SecureHashType");
//        }
//        if (fields.containsKey("vnp_SecureHash")) {
//            fields.remove("vnp_SecureHash");
//        }
//        String signValue = VNPayConfig.hashAllFields(fields);
//        if (signValue.equals(vnp_SecureHash)) {
//            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
//                return 1;
//            } else {
//                return 0;
//            }
//        } else {
//            return -1;
//        }
//    }

}