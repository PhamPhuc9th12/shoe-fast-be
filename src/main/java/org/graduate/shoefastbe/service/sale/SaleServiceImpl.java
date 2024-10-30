package org.graduate.shoefastbe.service.sale;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.dto.sale.SaleResponse;
import org.graduate.shoefastbe.entity.SalesEntity;
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

        Page<SalesEntity> entities = salesRepository.findAll(pageable);
        return entities.map(saleMapper::getResponseBy);
    }
}
