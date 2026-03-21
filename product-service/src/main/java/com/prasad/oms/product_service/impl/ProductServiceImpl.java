package com.prasad.oms.product_service.impl;

import com.prasad.oms.product_service.dto.ProductDTO;
import com.prasad.oms.product_service.entity.Product;
import com.prasad.oms.product_service.mapper.ProductMapper;
import com.prasad.oms.product_service.repository.ProductRepository;
import com.prasad.oms.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = mapper.toEntity(productDTO);
        Product saved = repository.save(product);
        return mapper.toDTO(saved);
    }

    @Override
    public ProductDTO getProductById(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapper.toDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}
