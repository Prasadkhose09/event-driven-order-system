package com.prasad.oms.product_service.repository;

import com.prasad.oms.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
