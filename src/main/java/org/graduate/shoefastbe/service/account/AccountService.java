package org.graduate.shoefastbe.service.account;

import org.graduate.shoefastbe.base.error_success_handle.SuccessResponse;
import org.graduate.shoefastbe.dto.AccountCreateRequest;

public interface AccountService {
    SuccessResponse singUp(AccountCreateRequest account);
}
