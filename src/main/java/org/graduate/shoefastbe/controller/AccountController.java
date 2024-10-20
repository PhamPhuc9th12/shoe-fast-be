package org.graduate.shoefastbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.SuccessResponse;
import org.graduate.shoefastbe.common.Common;
import org.graduate.shoefastbe.dto.AccountCreateRequest;
import org.graduate.shoefastbe.dto.account.*;
import org.graduate.shoefastbe.entity.AccountDetailEntity;
import org.graduate.shoefastbe.service.account.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@CrossOrigin()
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    @Operation(summary = "Đăng ký tài khoản")
    SuccessResponse singUp(@RequestBody @Valid AccountCreateRequest accountCreateRequest){
        return accountService.singUp(accountCreateRequest);
    }
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập")
    TokenAndRole logIn(@RequestBody @Valid LoginRequest loginRequest){
        return accountService.login(loginRequest);
    }
    @GetMapping("/detail")
    @Operation(summary = "Lấy thông tin theo username")
    AccountResponse getAccountResponse(@RequestHeader(Common.AUTHORIZATION) String accessToken){
         return accountService.findByUsername(accessToken);
    }
    @GetMapping("/detail/id")
    @Operation(summary = "Lấy thông tin chi tiết theo id")
    AccountResponse getAccountResponse(@RequestParam Long id){
        return accountService.getDetailById(id);
    }
    @PostMapping("/forgot-password")
    @Operation(summary = "Quên mật khẩu")
    SuccessResponse forgotPassword(@RequestBody ForgotPassRequest forgotPassRequest) throws MessagingException {
        return accountService.forgotPassword(forgotPassRequest);
    }
    @PutMapping("/update-profile")
    @Operation(summary = "Cập nhập mật khẩu")
    AccountDetailEntity forgotPassword(@RequestBody AccountUpdateRequest accountUpdateRequest)  {
        return accountService.updateProfile(accountUpdateRequest);
    }

    //admin
    @GetMapping("/admin/count")
    @Operation(summary = "Số lượng tài khoản")
    Long countAccount(){
        return accountService.countAccount();
    }


}
