package com.kartcom.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kartcom.catalog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
