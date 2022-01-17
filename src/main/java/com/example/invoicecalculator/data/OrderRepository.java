package com.example.invoicecalculator.data;

import com.example.invoicecalculator.entities.MakeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<MakeOrder,Integer> {
}
