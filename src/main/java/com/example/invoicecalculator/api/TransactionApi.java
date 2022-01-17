package com.example.invoicecalculator.api;

import com.example.invoicecalculator.entities.Invoice;
import com.example.invoicecalculator.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("transaction")
public class TransactionApi {

    InvoiceService invoiceService;

    @Autowired
    public TransactionApi(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/getInvoice/{orderId}")
    public List<Invoice> generateInvoices(@PathVariable int orderId){

       return invoiceService.getInvoices(orderId);
    }

}
