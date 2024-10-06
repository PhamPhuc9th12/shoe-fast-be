package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.AccountCreateRequest;
import org.graduate.shoefastbe.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {
    AccountEntity getEntityFromRequest(AccountCreateRequest accountCreateRequest);
}
