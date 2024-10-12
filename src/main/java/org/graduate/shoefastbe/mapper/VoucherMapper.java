package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.voucher.VoucherDtoResponse;
import org.graduate.shoefastbe.entity.VoucherEntity;
import org.mapstruct.Mapper;

@Mapper
public interface VoucherMapper {
    VoucherDtoResponse getResponseByEntity(VoucherEntity voucher);
}
