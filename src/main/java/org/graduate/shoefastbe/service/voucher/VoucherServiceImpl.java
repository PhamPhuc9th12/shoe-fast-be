package org.graduate.shoefastbe.service.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.dto.voucher.VoucherDtoResponse;
import org.graduate.shoefastbe.entity.VoucherEntity;
import org.graduate.shoefastbe.mapper.VoucherMapper;
import org.graduate.shoefastbe.repository.VoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Getter
@Setter
@Service
@Transactional(readOnly = true)
public class VoucherServiceImpl implements VoucherService{
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    @Override
    public VoucherDtoResponse getVoucherByCode(String code) {
        VoucherEntity voucherEntity = voucherRepository.findVoucherByCode(code);
        if(Objects.nonNull(voucherEntity)){
            if(voucherEntity.getExpireDate().isBefore(LocalDate.now())){
                throw new RuntimeException(CodeAndMessage.ERR6);
            }
            if(!voucherEntity.getIsActive()){
                throw new RuntimeException(CodeAndMessage.ERR7);
            }
            if(voucherEntity.getCount() == 0){
                throw new RuntimeException(CodeAndMessage.ERR8);
            }
            return voucherMapper.getResponseByEntity(voucherEntity);
        }else{
            throw new RuntimeException(CodeAndMessage.ERR3);
        }
    }
}
