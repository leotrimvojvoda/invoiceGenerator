package com.example.invoicecalculator.api;

import com.example.invoicecalculator.data.OrderRepository;
import com.example.invoicecalculator.data.ProductRepository;
import com.example.invoicecalculator.data.TransactionRepository;
import com.example.invoicecalculator.entities.Invoice;
import com.example.invoicecalculator.entities.MakeOrder;
import com.example.invoicecalculator.entities.Product;
import com.example.invoicecalculator.entities.Transaction;
import com.example.invoicecalculator.services.InvoiceService;
import com.example.invoicecalculator.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("transaction")
public class TransactionApi {

    InvoiceService invoiceService;
    TransactionRepository transactionRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;
    OrderService orderService;

    @Autowired
    public TransactionApi(InvoiceService invoiceService, TransactionRepository transactionRepository, ProductRepository productRepository, OrderRepository orderRepository, OrderService orderService) {
        this.invoiceService = invoiceService;
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping("/getInvoice/{orderId}")
    public List<Invoice> generateInvoices(@PathVariable int orderId){
        return invoiceService.getInvoices(orderId);
    }

    @GetMapping("/get/{transactionId}")
    public Transaction getTransaction(@PathVariable int transactionId){
        return transactionRepository.getById(transactionId);
    }

    @GetMapping("/getAll")
    public List<Transaction> getAll(){
        return transactionRepository.findAll();
    }

    @PostMapping("/buy")
    public Transaction makeTransaction(@Valid @RequestBody Transaction transaction)throws Exception{
       return orderService.addTransactionAndUpdateOrder(transaction);
    }

    @PostMapping("/addAll")
    public void addList(@RequestBody List<Transaction> transactions)throws  Exception{
        for(Transaction transaction: transactions){
            orderService.addTransactionAndUpdateOrder(transaction);
        }
    }

    @Transactional
    @PostMapping("delete/{transactionId}")
    public void delete(@PathVariable int transactionId)throws Exception{
        //return transactionRepository.deleteTransactionById(transactionId).orElseThrow(() -> new Exception("Transaction could not be deleted"));
        transactionRepository.deleteById(transactionId);
    }

    @PostMapping("/deleteAll")
    public void deleteAll(){
        transactionRepository.deleteAll();
    }




}
