package com.example.secure.poc.main.mapper;

import com.example.secure.poc.main.mapper.dto.AccountsDTO;
import com.example.secure.poc.main.model.Accounts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountsToAccountsDTOMapper {
    AccountsDTO convert(Accounts a);
    Accounts convert(AccountsDTO a);
}
