package org.graduate.shoefastbe.service.attribute;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.dto.attribute.AttributeDtoResponse;
import org.graduate.shoefastbe.entity.AttributeEntity;
import org.graduate.shoefastbe.mapper.AttributeMapper;
import org.graduate.shoefastbe.repository.AttributeRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AttributeServiceImpl implements AttributeService {
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

    @Override
    public AttributeDtoResponse getAttributeByProductId(Long productId, Long size) {
        AttributeEntity attribute = attributeRepository.findByProductIdAndSize(productId,size);
        if(Objects.isNull(attribute)) throw new RuntimeException(CodeAndMessage.ERR3);
        return attributeMapper.getResponseFromEntity(attribute);
    }
}
