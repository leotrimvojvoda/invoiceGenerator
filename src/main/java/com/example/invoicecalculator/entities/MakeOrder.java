package com.example.invoicecalculator.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class MakeOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name="subTotal")
    String subTotal;
    @Column(name="total")
    String total;

    public MakeOrder(int id) {
        this.id = id;
    }
}
