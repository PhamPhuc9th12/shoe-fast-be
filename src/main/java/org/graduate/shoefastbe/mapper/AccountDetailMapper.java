package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.AccountCreateRequest;
import org.graduate.shoefastbe.entity.AccountDetailEntity;
import org.mapstruct.Mapper;

@Mapper
public interface AccountDetailMapper {
    AccountDetailEntity getEntityFromRequest(AccountCreateRequest account);
}
