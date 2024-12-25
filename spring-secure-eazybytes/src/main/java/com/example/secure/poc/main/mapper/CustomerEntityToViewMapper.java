package com.example.secure.poc.main.mapper;

import com.example.secure.poc.main.entity.CustomerEntity;
import com.example.secure.poc.main.model.CustomerView;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEntityToViewMapper {
    CustomerEntity mapToEntity(CustomerView c);
}
