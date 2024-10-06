package org.graduate.shoefastbe.service.account;

import org.graduate.shoefastbe.base.error_success_handle.SuccessResponse;
import org.graduate.shoefastbe.dto.AccountCreateRequest;
import org.graduate.shoefastbe.dto.account.LoginRequest;
import org.graduate.shoefastbe.dto.account.TokenAndRole;

public interface AccountService {
    SuccessResponse singUp(AccountCreateRequest account);
    TokenAndRole login(LoginRequest loginRequest);
}
