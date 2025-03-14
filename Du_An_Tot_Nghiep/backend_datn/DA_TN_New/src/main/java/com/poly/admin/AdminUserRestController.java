
package com.poly.admin;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.poly.dto.OrderDetailDTO1;
import com.poly.dto.OrderWithDetailOrderDTO;
import com.poly.dto.UserOrderCancelDTO;
import com.poly.model.Order;
import com.poly.repository.OrderDetailRepository;
import com.poly.repository.OrderRepository;
import com.poly.service.EmailUserService;
import com.poly.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.model.User;
import com.poly.repository.AddressRepository;

import com.poly.service.UserService;

@RestController
@RequestMapping("admin/api/users")
@CrossOrigin(origins = "*")
public class AdminUserRestController {
	@Autowired
    private UserService userService;

    @Autowired
    private EmailUserService emailUserService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
//            updatedUser.setUsername(userDetails.getUsername());
//            updatedUser.setPassword(userDetails.getPassword());
            updatedUser.setFullName(userDetails.getFullName());
            updatedUser.setGender(userDetails.isGender());
//            updatedUser.setEmail(userDetails.getEmail());
//            updatedUser.setPhone(userDetails.getPhone());
            updatedUser.setBirthday(userDetails.getBirthday());
            updatedUser.setStatus_user(userDetails.isStatus_user());

            // Thêm logic để cập nhật ảnh
            if (userDetails.getImage_user() != null) {
                updatedUser.setImage_user(userDetails.getImage_user());
            }

            return ResponseEntity.ok(userService.save(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/status/{id}")
    public ResponseEntity<User> updateUserStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> statusUpdate) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setStatus_user(statusUpdate.get("status_user")); // Cập nhật trạng thái
            userService.save(updatedUser);

            // Thông tin cửa hàng
            String customerName = updatedUser.getFullName();  // Tên người dùng
            String shopName = "Shop Maou";  // Tên cửa hàng
            String shopAddress = "10 Đường số 3, khu dân cư Metro, Ninh Kiều, Cần Thơ 902070, Vietnam";  // Địa chỉ cửa hàng
            String shopContact = "19001393";  // Thông tin liên hệ

            // Nếu trạng thái là vô hiệu hóa, gửi email thông báo
            if (!updatedUser.isStatus_user()) {
                String subject = "Thông báo tài khoản bị vô hiệu hóa";
                String content = "Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ với quản trị viên để biết thêm chi tiết.";
                emailUserService.sendUserNotificationEmail1(updatedUser.getEmail(), subject, customerName, shopName,
                        shopAddress, shopContact, content);
            }
            // Nếu trạng thái là kích hoạt, gửi email thông báo
            else {
                String subject = "Thông báo tài khoản đã được kích hoạt";
                String content = "Tài khoản của bạn đã được kích hoạt. Bạn có thể đăng nhập và sử dụng dịch vụ. Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.";
                emailUserService.sendUserNotificationEmail1(updatedUser.getEmail(), subject, customerName, shopName,
                        shopAddress, shopContact, content);
            }

            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    //Minh
    @GetMapping("/users/cancelled-orders")
    public List<UserOrderCancelDTO> findUsersWithCancelledOrders() {
        try {
            return orderService.getUsersWithCancelledOrders();
        }catch (Exception e){
         e.printStackTrace();
         return  null;
        }

    }

    @GetMapping("/users/{userId}/cancelled-orders")
    public ResponseEntity<?> getUsersWithCancelledOrders(@PathVariable Integer userId) {
//        List
//        try {
//          List<Order> orders = orderRepository.findByUserIdAndStateId(userId);
//
//        if (orders.isEmpty()) {
//            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy đơn hàng
//        }
//
//        return ResponseEntity.ok(orders);
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
        try {
            List<Order> orders = orderRepository.findByUserIdAndStateId(userId);

            if (orders.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            List<OrderWithDetailOrderDTO> orderDTOs = orders.stream().map(order -> {
                OrderWithDetailOrderDTO dto = new OrderWithDetailOrderDTO();
                dto.setOrderId(order.getId());
                dto.setDeliveryFee(order.getDelivery_fee());
                dto.setTotalCash(order.getTotal_cash());
                dto.setNote(order.getNote());
                dto.setUpdateddDate(order.getUpdated_date());
                dto.setState(order.getState().getName_status_order());

                List<OrderDetailDTO1> detailDTOs = order.getOrderDetails().stream().map(detail -> {
                    OrderDetailDTO1 detailDTO = new OrderDetailDTO1();
                    detailDTO.setId(detail.getId());
                    detailDTO.setQuantity(detail.getQuantity());
                    detailDTO.setPrice(detail.getPrice());
                    detailDTO.setFullAddress(detail.getFull_address());
                    detailDTO.setNameColor(detail.getName_color());
                    detailDTO.setNameSize(detail.getName_size());
                    detailDTO.setDiscountFS(detail.getDiscount_FS());
                    detailDTO.setNameFS(detail.getName_FS());
                    detailDTO.setNameProduct(detail.getVariantProd().getProduct().getName_prod());
                    return detailDTO;
                }).toList();

                dto.setOrderDetails(detailDTOs);
                return dto;
            }).toList();

            return ResponseEntity.ok(orderDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }

    }





    @PostMapping("/warning")
    public ResponseEntity<?> warning(@RequestBody Map<String, Object> reqMap) {
        Integer userId = (Integer) reqMap.get("idUser");  // Lấy userId từ request body
        Integer stateId = 6;  // StateId cho đơn hàng đã hủy

        // Tìm người dùng theo userId
        Optional<User> user = userService.findById(userId);

        if (user.isPresent()) {
            // Fetch user details
            String customerName = user.get().getFullName();
            String shopName = "Shop Maou";  // Tên cửa hàng
            String shopAddress = "10 Đường số 3, khu dân cư Metro, Ninh Kiều, Cần Thơ 902070, Vietnam ";  // Địa chỉ cửa hàng
            String shopContact = "19001393";  // Thông tin liên hệ của cửa hàng

            // Fetch các đơn hàng đã hủy của người dùng
            List<Order> canceledOrders = orderRepository.findByUserIdAndStateId(userId);

            if (!canceledOrders.isEmpty()) {
                // Chuyển đổi đơn hàng và chi tiết thành DTO nếu cần
                List<OrderWithDetailOrderDTO> canceledOrdersDTO = canceledOrders.stream().map(order -> {
                    OrderWithDetailOrderDTO dto = new OrderWithDetailOrderDTO();
                    dto.setOrderId(order.getId());
                    dto.setDeliveryFee(order.getDelivery_fee());
                    dto.setTotalCash(order.getTotal_cash());
                    dto.setNote(order.getNote());
                    dto.setUpdateddDate(order.getUpdated_date());
                    dto.setState(order.getState().getName_status_order());

                    List<OrderDetailDTO1> detailDTOs = order.getOrderDetails().stream().map(detail -> {
                        OrderDetailDTO1 detailDTO = new OrderDetailDTO1();
                        detailDTO.setId(detail.getId());
                        detailDTO.setQuantity(detail.getQuantity());
                        detailDTO.setPrice(detail.getPrice());
                        detailDTO.setFullAddress(detail.getFull_address());
                        detailDTO.setNameColor(detail.getName_color());
                        detailDTO.setNameSize(detail.getName_size());
                        detailDTO.setDiscountFS(detail.getDiscount_FS());
                        detailDTO.setNameFS(detail.getName_FS());
                        detailDTO.setNameProduct(detail.getVariantProd().getProduct().getName_prod());
                        return detailDTO;
                    }).toList();

                    dto.setOrderDetails(detailDTOs);
                    return dto;
                }).collect(Collectors.toList());

                // Chuyển các thông tin cần thiết vào email để gửi cho người dùng
                StringBuilder emailContent = new StringBuilder();

                emailContent.append("<h3>Cảnh báo về các đơn hàng đã hủy:</h3>");
                canceledOrdersDTO.forEach(orderDTO -> {
                    emailContent.append("<p>Mã đơn hàng: ").append(orderDTO.getOrderId())
                            .append("<br>Tổng tiền: ").append(NumberFormat.getNumberInstance(new Locale("vi","VN")).format(orderDTO.getTotalCash())).append("đ")
                            .append("<br>Phí vận chuyển: ").append(NumberFormat.getNumberInstance(new Locale("vi","VN")).format(orderDTO.getDeliveryFee())).append("đ")
                            .append("<br>Lý do hủy: ").append(orderDTO.getNote())
                            .append("</p>");
                    orderDTO.getOrderDetails().forEach(detail -> {
                        emailContent.append("<p>- Sản phẩm: ").append(detail.getNameProduct())
                                .append("<br>Size: ").append(detail.getNameSize())
                                .append("<br>Màu: ")
                                .append("<span style='display:inline-block;width:20px;height:20px;background-color: ")
                                .append(detail.getNameColor()).append(";'></span>")
                                .append("<br>Giá: ").append(NumberFormat.getNumberInstance(new Locale("vi","Vn")).format(detail.getPrice())).append("đ")
                                .append("<br>Số lượng: ").append(detail.getQuantity())
                                .append("</p>");
                    });
                });

                // Gửi email cảnh báo cho người dùng
                emailUserService.sendUserNotificationEmail(

                        user.get().getEmail(),
                        "Thông báo các đơn hàng đã hủy",
                        customerName,
                        shopName,
                        shopAddress,
                        shopContact,
                        emailContent.toString()


                );

                return ResponseEntity.ok("Cảnh báo đã được gửi qua email!");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Không có đơn hàng nào bị hủy.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại.");
        }
    }

}



