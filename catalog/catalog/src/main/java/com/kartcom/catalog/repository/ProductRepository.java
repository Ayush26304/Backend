package com.kartcom.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kartcom.catalog.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long > {

	List<Product> findByCategoryId(Long categoryId);
	@Query("SELECT p FROM Product p WHERE " +
		       "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
		List<Product> searchProduct(@Param("keyword") String keyword);

}
