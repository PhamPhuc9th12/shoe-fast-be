package org.graduate.shoefastbe.service.voucher;

import org.graduate.shoefastbe.dto.voucher.VoucherDtoResponse;

public interface VoucherService {

    VoucherDtoResponse getVoucherByCode(String code);
}
