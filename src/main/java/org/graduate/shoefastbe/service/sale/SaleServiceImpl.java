package org.graduate.shoefastbe.service.sale;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.dto.sale.SaleResponse;
import org.graduate.shoefastbe.entity.Sales;
import org.graduate.shoefastbe.mapper.SaleMapper;
import org.graduate.shoefastbe.repository.SalesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaleServiceImpl implements SaleService{
    private final SalesRepository salesRepository;
    private final SaleMapper saleMapper;
    @Override
    public Page<SaleResponse> getAllSale(Pageable pageable) {

        Page<Sales> entities = salesRepository.findAll(pageable);
        return entities.map(saleMapper::getResponseBy);
    }

    @Override
    public SaleResponse create(SaleResponse saleResponse) {
        Sales sales = saleMapper.getEntityBy(saleResponse);
        salesRepository.save(sales);
        return saleMapper.getResponseBy(sales);
    }

    @Override
    public SaleResponse getDetailSale(Long id) {
        Sales sales = salesRepository.findById(id).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        return saleMapper.getResponseBy(sales);
    }

}
