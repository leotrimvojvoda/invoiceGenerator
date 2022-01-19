package com.example.invoicecalculator.api;

import com.example.invoicecalculator.data.OrderRepository;
import com.example.invoicecalculator.data.ProductRepository;
import com.example.invoicecalculator.data.TransactionRepository;
import com.example.invoicecalculator.entities.Invoice;
import com.example.invoicecalculator.entities.MakeOrder;
import com.example.invoicecalculator.entities.Product;
import com.example.invoicecalculator.entities.Transaction;
import com.example.invoicecalculator.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("transaction")
public class TransactionApi {

    InvoiceService invoiceService;
    TransactionRepository transactionRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;

    public TransactionApi(InvoiceService invoiceService, TransactionRepository transactionRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.invoiceService = invoiceService;
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/getInvoice/{orderId}")
    public List<Invoice> generateInvoices(@PathVariable int orderId){
       return invoiceService.getInvoices(orderId);
    }

    @PostMapping("/buy")
    public Transaction makeTransaction(@Valid @RequestBody Transaction transaction)throws Exception{

        Product product = productRepository.findById(transaction.getProductId()).orElseThrow(() -> new Exception("Product does not exist in the database"));

       if(transaction.getOrderId() != 0){
           double subTotal = product.getProductPrice()-product.getDiscount();
           double vat = subTotal*product.getVat();
           double total = subTotal+vat;

           MakeOrder order = new MakeOrder();
           order.setId(1);
           order.setSubTotal(22.2);
           order.setVat(22.1);
           order.setTotal(22.3);

           orderRepository.save(order);
       }

        return transactionRepository.save(transaction);
    }

}
