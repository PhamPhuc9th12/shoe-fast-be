package org.graduate.shoefastbe.service.sale;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.dto.sale.SaleResponse;
import org.graduate.shoefastbe.entity.Product;
import org.graduate.shoefastbe.entity.Sales;
import org.graduate.shoefastbe.mapper.SaleMapper;
import org.graduate.shoefastbe.repository.ProductRepository;
import org.graduate.shoefastbe.repository.SalesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SaleServiceImpl implements SaleService{
    private final SalesRepository salesRepository;
    private final SaleMapper saleMapper;
    private final ProductRepository productRepository;
    @Override
    public Page<SaleResponse> getAllSale(Pageable pageable) {

        Page<Sales> entities = salesRepository.findAll(pageable);
        return entities.map(saleMapper::getResponseBy);
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public SaleResponse update(SaleResponse saleResponse) {
        Sales sales = salesRepository.findById(saleResponse.getId()).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        saleMapper.update(sales,saleResponse);
        List<Product> products = productRepository.findAllBySaleId(sales.getId());
        if(Boolean.FALSE.equals(sales.getIsActive())){
            for(Product product : products){
                product.setSaleId(1L);
                productRepository.save(product);
            }
        }
        return saleMapper.getResponseBy(sales);
    }

}
