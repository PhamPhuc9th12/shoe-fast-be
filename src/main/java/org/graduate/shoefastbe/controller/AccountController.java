package org.graduate.shoefastbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.SuccessResponse;
import org.graduate.shoefastbe.common.Common;
import org.graduate.shoefastbe.dto.AccountCreateRequest;
import org.graduate.shoefastbe.dto.account.*;
import org.graduate.shoefastbe.entity.AccountDetail;
import org.graduate.shoefastbe.service.account.AccountService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@CrossOrigin
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
    @Operation(summary = "Cập nhập thong tin")
    AccountDetail forgotPassword(@RequestBody AccountUpdateRequest accountUpdateRequest)  {
        return accountService.updateProfile(accountUpdateRequest);
    }

    //admin
    @GetMapping("/admin/count")
    @Operation(summary = "Số lượng tài khoản")
    Long countAccount(){
        return accountService.countAccount();
    }
    @GetMapping("/admin/total-page")
    Integer getTotalPage(){
        return accountService.getTotalPage();
    }
    @GetMapping("/admin/account/by-role")
    List<AccountResponse> getAccountByRoleName(@RequestParam("roleName") String roleName,
                                               @ParameterObject Pageable pageable){
        return accountService.findUserByRole(roleName, pageable);
    }
    @GetMapping("/admin/account/find-all")
    List<AccountResponse> getAllAccounts(@ParameterObject Pageable pageable){
        return accountService.findAllUser(pageable);
    }

}
