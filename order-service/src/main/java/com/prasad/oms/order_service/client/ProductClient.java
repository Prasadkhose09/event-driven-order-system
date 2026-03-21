package com.prasad.oms.order_service.client;


import com.prasad.oms.order_service.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String PRODUCT_URL = "http://localhost:8082/products/";

    public ProductResponse getProductById(Long productId){
        return restTemplate.getForObject(PRODUCT_URL + productId, ProductResponse.class);
    }
}
