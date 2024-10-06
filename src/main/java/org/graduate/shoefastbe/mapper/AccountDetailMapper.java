package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.AccountCreateRequest;
import org.graduate.shoefastbe.dto.account.AccountUpdateRequest;
import org.graduate.shoefastbe.entity.AccountDetailEntity;
import org.graduate.shoefastbe.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface AccountDetailMapper {
    AccountDetailEntity getEntityFromRequest(AccountCreateRequest account);
    void updateEntityByUpdateAccount(@MappingTarget AccountDetailEntity accountDetailEntity, AccountUpdateRequest updateAccountRequest);
}
