package com.kartcom.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kartcom.catalog.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long > {

	List<Product> findByCategoryId(Long categoryId);

}
