package com.example.invoicecalculator.api;

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
    OrderApi orderApi;

    @Autowired
    public TransactionApi(InvoiceService invoiceService, TransactionRepository transactionRepository, ProductRepository productRepository, OrderApi orderApi) {
        this.invoiceService = invoiceService;
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.orderApi = orderApi;
    }

    @GetMapping("/getInvoice/{orderId}")
    public List<Invoice> generateInvoices(@PathVariable int orderId){
       return invoiceService.getInvoices(orderId);
    }

    @PostMapping("/buy")
    public Transaction makeTransaction(@Valid @RequestBody Transaction transaction)throws Exception{

        transactionRepository.save(transaction);

        Product product = productRepository.findById(transaction.getProductId()).orElseThrow(() -> new Exception("Product does not exist in the database"));

        double subTotal = product.getProductPrice()-product.getDiscount();
        double vat = subTotal*product.getVat();
        double total = subTotal+vat;

        MakeOrder order = new MakeOrder();
        order.setId(transaction.getOrderId());
        order.setSubTotal(subTotal);
        order.setVat(vat);
        order.setTotal(total);

        orderApi.addOrder(order);

        return transaction;
    }

}
