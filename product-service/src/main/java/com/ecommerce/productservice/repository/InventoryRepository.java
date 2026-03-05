package com.ecommerce.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.productservice.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{

}
