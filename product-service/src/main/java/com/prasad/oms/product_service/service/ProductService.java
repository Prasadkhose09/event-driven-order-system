package com.prasad.oms.product_service.service;

import com.prasad.oms.product_service.dto.ProductDTO;
import com.prasad.oms.product_service.entity.Product;
import com.prasad.oms.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    List<ProductDTO> getAllProducts();
}
