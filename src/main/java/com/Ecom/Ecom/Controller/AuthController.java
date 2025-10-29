package com.Ecom.Ecom.Controller;

import com.Ecom.Ecom.Entity.BuyProduct;
import com.Ecom.Ecom.Entity.Products;
import com.Ecom.Ecom.Entity.User;
import com.Ecom.Ecom.Entity.productPhoto;
import com.Ecom.Ecom.Repo.BuyProductsRepo;
import com.Ecom.Ecom.dto.*;
import com.Ecom.Ecom.service.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired private UserService service;
    @Autowired private EmailService emailService;
    @Autowired private GenerateMail generateMail;
    @Autowired private AuthenticationManager auth;
    @Autowired private JwtService jwtService;
    @Autowired private ProductService productService;
    @Autowired private BuyProductsRepo buyProductsRepo;

    // Inject from application.properties or environment
    @Value("${razor.api.key}")
    private String razorpayKeyId;

    @Value("${razor.api.secret}")
    private String razorpayKeySecret;

    @GetMapping("/products")
    public ResponseEntity<List<productDto>> getProductsForUser() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<getproductwpDto> getProductInfo(@PathVariable int id){
        Products pp = productService.getProductByid(id);
        getproductwpDto dp=new getproductwpDto();
        dp.setPid(pp.getId());
        dp.setPrice(pp.getPrice());
        dp.setPname(pp.getName());
        dp.setSellername(pp.getSellername());
        dp.setStatus(pp.getStatus());
        dp.setStock(String.valueOf(pp.getStock()));
        dp.setDescription(pp.getDescription());
        List<String> photos = pp.getPhotos().stream()
                .map(productPhoto::getPhoto)
                .collect(Collectors.toList());
        dp.setImg(photos);
        return ResponseEntity.ok(dp);
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> user(@RequestBody RegisterDto u){
        User user = service.setUser(u);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest ll, HttpServletResponse httpServletResponse){
        Authentication aa = auth.authenticate(
                new UsernamePasswordAuthenticationToken(ll.getGmail(), ll.getPass())
        );
        if(aa.isAuthenticated()){
            User uu = service.getUser(ll.getGmail());
            String token=jwtService.generatetoken(uu);
            Cookie cc=new Cookie("token",token);
            cc.setHttpOnly(true);
            cc.setPath("/");
            cc.setMaxAge(24*60*60);
            httpServletResponse.addCookie(cc);
            Cookie roleCookie = new Cookie("role", uu.getRole().name());
            roleCookie.setHttpOnly(false);
            roleCookie.setPath("/");
            roleCookie.setMaxAge(24 * 60 * 60);
            httpServletResponse.addCookie(roleCookie);

            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/checkout/session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody Map<String, Object> req, Authentication authentication) {
        try {
            String email = authentication.getName();
            int productId = Integer.parseInt(req.get("productId").toString());
            int quantity = Integer.parseInt(req.get("quantity").toString());
            String address = req.get("address").toString();
            String mode = req.get("mode").toString();

            Products product = productService.getProductByid(productId);
            if (product == null) {
                return ResponseEntity.badRequest().body("Product not found");
            }
            if (quantity < 1) {
                return ResponseEntity.badRequest().body("Invalid quantity");
            }
            if (product.getStock() < quantity) {
                return ResponseEntity.badRequest().body("Insufficient stock");
            }

            int pricePerUnit = product.getPrice(); // in INR
            int totalAmountInRupees = pricePerUnit * quantity;
            int totalAmountInPaise = totalAmountInRupees * 100; // Razorpay expects smallest unit

            BuyProduct o1 = new BuyProduct();
            o1.setPid(productId);
            o1.setMode(mode);
            o1.setQuantity(quantity);
            o1.setPrice(totalAmountInRupees);
            o1.setAddress(address);
            o1.setStatus("Pending");
            o1.setPurchaseDate(LocalDateTime.now());
            o1.setDateApply(LocalDate.now());
            o1.setDeDate(o1.getDateApply().plusDays(5));
            String sellerMail = productService.GetSellerGmail(o1.getPid());
            o1.setSellerGmail(sellerMail);
            o1.setUserEmail(email);

            // Save initial record
            o1 = buyProductsRepo.save(o1);

            if (mode.equalsIgnoreCase("COD")) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "COD_ORDER_CONFIRMED");
                response.put("buyId", o1.getId());
                return ResponseEntity.ok(response);
            }

            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            o1.setStatus("Not_Paid");
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", totalAmountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_" + o1.getId());

            Order order = client.orders.create(orderRequest);

            // Optionally store Razorpay order id against your buy record for reconciliation
            // o1.setGatewayOrderId(order.get("id")); // add field if desired
            buyProductsRepo.save(o1);

            Map<String, Object> response = new HashMap<>();
            response.put("id", order.get("id"));
            response.put("amount", totalAmountInPaise);
            response.put("currency", "INR");
            response.put("key", razorpayKeyId); // return only key_id for frontend
            response.put("buyId", o1.getId());
            return ResponseEntity.ok(response);

        } catch (RazorpayException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Razorpay error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to create order: " + e.getMessage());
        }
    }

    @PostMapping("/payment/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> body, Authentication authentication) {
        try {
            String email = authentication.getName();

            // Inputs from frontend handler
            Integer buyId = Integer.parseInt(body.get("buyId").toString());
            String razorpayOrderId = body.get("razorpay_order_id").toString();
            String razorpayPaymentId = body.get("razorpay_payment_id").toString();
            String razorpaySignature = body.get("razorpay_signature").toString();

            Optional<BuyProduct> opt = buyProductsRepo.findById(buyId);
            if (opt.isEmpty()) {
                return ResponseEntity.badRequest().body("Order not found");
            }
            BuyProduct bp = opt.get();
            if (!Objects.equals(bp.getUserEmail(), email)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed");
            }
            if (!"Not_Paid".equalsIgnoreCase(bp.getStatus())) {
                return ResponseEntity.badRequest().body("Order already processed");
            }

            // Construct expected payload: order_id|payment_id
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            boolean status = Utils.verifyPaymentSignature(options, razorpayKeySecret);

            if (status) {
                // Mark as paid and reduce stock
                bp.setStatus("Paid");
                buyProductsRepo.save(bp);
                productService.decreaseStock(bp.getPid(), bp.getQuantity()); // implement this method safely

                Map<String, Object> res = new HashMap<>();
                res.put("status", "VERIFIED");
                return ResponseEntity.ok(res);
            } else {
                bp.setStatus("Payment_Failed");
                buyProductsRepo.delete(bp);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying payment");
        }

    }
    @GetMapping("/products/search")
    public ResponseEntity<List<productDto>> searching(@RequestParam String keyword){
        System.out.println("sujit swain");
        List<productDto> pro=productService.SearchProducts(keyword);
        return ResponseEntity.ok()
                .body(pro);

    }
    @PostMapping("/mail/newSus")
    public ResponseEntity<?>sendMail(@RequestParam String email){
        return emailService.SaveAndSendMail(email);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        String[] cookies = {"token", "role"};
        for (String name : cookies) {
            Cookie cookie = new Cookie(name, null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            if (name.equals("token")) {
                cookie.setHttpOnly(true);
            } else {
                cookie.setHttpOnly(false);
            }
            response.addCookie(cookie);
        }
        return ResponseEntity.ok("All cookies cleared");
    }


}
