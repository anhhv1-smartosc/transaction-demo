package com.example.demo_v2.mapper;

import com.example.demo_v2.dto.response.AccountResponse;
import com.example.demo_v2.enity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    //@Mapping(source = "createdAt", target = "created_at"))

    AccountResponse toAccountResponse(Account account);
}
