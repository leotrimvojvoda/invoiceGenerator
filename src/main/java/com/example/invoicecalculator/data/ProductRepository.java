package com.example.invoicecalculator.data;

import com.example.invoicecalculator.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
