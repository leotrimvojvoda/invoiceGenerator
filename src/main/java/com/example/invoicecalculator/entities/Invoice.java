package com.example.invoicecalculator.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private TreeMap<Product,Integer> items;
    private double subTotal;
    private double vat;
    private double total;

}
