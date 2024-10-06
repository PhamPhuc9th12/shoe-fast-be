package org.graduate.shoefastbe.service.account;

import org.graduate.shoefastbe.base.error_success_handle.SuccessResponse;
import org.graduate.shoefastbe.dto.AccountCreateRequest;
import org.graduate.shoefastbe.dto.account.*;
import org.graduate.shoefastbe.entity.AccountDetailEntity;

import javax.mail.MessagingException;

public interface AccountService {
    SuccessResponse singUp(AccountCreateRequest account);
    TokenAndRole login(LoginRequest loginRequest);
    AccountResponse findByUsername(String username);
    SuccessResponse forgotPassword(ForgotPassRequest forgotPassRequest) throws MessagingException;
    AccountResponse getDetailById(Long id);
    AccountDetailEntity updateProfile(AccountUpdateRequest accountUpdateRequest);
}
