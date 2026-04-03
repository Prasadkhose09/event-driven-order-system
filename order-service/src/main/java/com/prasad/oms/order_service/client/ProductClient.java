package com.prasad.oms.order_service.client;


import com.prasad.oms.order_service.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product.service.url:http://product-service:8082/products/}")
    private String productUrl;

    public ProductResponse getProductById(Long productId){
        return restTemplate.getForObject(productUrl + productId, ProductResponse.class);
    }

    @PutMapping("/products/increase-stock/{id}")
    public void increaseStock(@PathVariable Long id, @RequestParam int quantity) {

    }
}
