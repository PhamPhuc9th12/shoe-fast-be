package org.graduate.shoefastbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.dto.voucher.VoucherDtoResponse;
import org.graduate.shoefastbe.service.voucher.VoucherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/voucher")
@CrossOrigin
@AllArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping("/by-code")
    @Operation(summary = "Lấy voucher theo code")
    VoucherDtoResponse getVoucherByCode(@RequestParam String code){
        return voucherService.getVoucherByCode(code);
    }
}
