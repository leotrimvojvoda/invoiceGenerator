package com.example.invoicecalculator.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Transactional
public class Product implements Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "productName")
    @Size(min = 1, max = 50, message = "PRODUCT NAME TO LONG: Product name must be between 1 and 50 characters")
    private String productName;
    @Column(name = "productPrice")

    private double productPrice;
    @Column(name = "discount")
    private double discount;

    @Column(name = "vat")
    private double vat;

    public Product(int id) {
        this.id = id;

    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Product){
            Product product = (Product)o;
            if(product.getId() == id) return 0;
            //if(product.getProductPrice() == productPrice) return 0;
        }
        return 1;
    }
}
