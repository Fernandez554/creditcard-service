package com.nttbank.microservices.creditcardservice.mapper;

import com.nttbank.microservices.creditcardservice.dto.CreditCardDTO;
import com.nttbank.microservices.creditcardservice.model.CreditCard;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CreditCardMapper {

  CreditCardMapper INSTANCE = Mappers.getMapper(CreditCardMapper.class);

  CreditCard creditCardDTOToCreditCard(CreditCardDTO creditCardDTO);
}
