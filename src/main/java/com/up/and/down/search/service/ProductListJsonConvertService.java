package com.up.and.down.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.up.and.down.product.entity.ProductInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductListJsonConvertService {

    private final ObjectMapper objectMapper;

    public String convertProductListToJson(Map<Long, ProductInformation> productList) throws JsonProcessingException {
        return objectMapper.writeValueAsString(productList);
    }

    public Map<Long, ProductInformation> convertJsonToProductList(String productInformationJson) throws JsonProcessingException {
        System.out.println(productInformationJson);
        return objectMapper.readValue(productInformationJson, new TypeReference<Map<Long, ProductInformation>>() {});
    }
}