package org.graduate.shoefastbe.service.sale;

import org.graduate.shoefastbe.dto.sale.SaleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaleService {

    Page<SaleResponse> getAllSale(Pageable pageable);
}