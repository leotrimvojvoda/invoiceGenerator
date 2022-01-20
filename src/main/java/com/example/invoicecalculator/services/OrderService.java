package com.example.invoicecalculator.services;

import com.example.invoicecalculator.data.OrderRepository;
import com.example.invoicecalculator.data.ProductRepository;
import com.example.invoicecalculator.data.TransactionRepository;
import com.example.invoicecalculator.entities.MakeOrder;
import com.example.invoicecalculator.entities.Product;
import com.example.invoicecalculator.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/*This helper class will update the TOTAL, SUBTOTAL and VAT in one order when the product is purchased in an existing order*/
@Service
public class OrderService {

    OrderRepository orderRepository;
    ProductRepository productRepository;
    TransactionRepository transactionRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, TransactionRepository transactionRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction addTransactionAndUpdateOrder(@Valid Transaction transaction)throws Exception{
        Product product = productRepository.findById(transaction.getProductId()).orElseThrow(() -> new Exception("Product does not exist in the database"));

        double subTotal = product.getProductPrice()-product.getDiscount();
        double vat = subTotal*product.getVat();
        double total = subTotal+vat;

        MakeOrder order = new MakeOrder();

        if(transaction.getOrderId() != 0){
            MakeOrder tempOrder = orderRepository.getById(transaction.getOrderId());
            subTotal += tempOrder.getSubTotal();
            vat += tempOrder.getVat();
            total += tempOrder.getTotal();

            order.setId(transaction.getOrderId());
        }

        order.setSubTotal(subTotal);
        order.setVat(vat);
        order.setTotal(total);

        orderRepository.save(order);

        return transactionRepository.save(transaction);
    }

}
