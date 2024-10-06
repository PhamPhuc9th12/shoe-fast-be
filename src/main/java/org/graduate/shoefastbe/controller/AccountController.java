package org.graduate.shoefastbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.SuccessResponse;
import org.graduate.shoefastbe.common.Common;
import org.graduate.shoefastbe.dto.AccountCreateRequest;
import org.graduate.shoefastbe.dto.account.AccountResponse;
import org.graduate.shoefastbe.dto.account.LoginRequest;
import org.graduate.shoefastbe.dto.account.TokenAndRole;
import org.graduate.shoefastbe.service.account.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
