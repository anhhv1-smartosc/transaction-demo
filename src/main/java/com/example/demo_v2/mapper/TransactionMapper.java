package com.example.demo_v2.mapper;


import com.example.demo_v2.dto.request.TransactionRequest;
import com.example.demo_v2.dto.response.TransactionResponse;
import com.example.demo_v2.enity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    //Transaction toTransaction(TransactionRequest request);

    //@Mapping(source = "transaction.amount", target = "amount")
    @Mapping(source = "transaction.senderAccount.id", target = "senderAccount")
    @Mapping(source = "transaction.receiverAccount.id", target = "receiverAccount")
    TransactionResponse toTransactionResponse(Transaction transaction);

}
