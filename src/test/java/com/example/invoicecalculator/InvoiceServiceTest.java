package com.example.invoicecalculator;

import com.example.invoicecalculator.entities.Invoice;
import com.example.invoicecalculator.entities.Product;
import com.example.invoicecalculator.services.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SpringBootTest
public class InvoiceServiceTest {


    @Autowired
    InvoiceService invoiceService;

    //Ger all purchased products and their amount
    @Test
    public void test(){
        TreeMap<Product, Integer> invoice = invoiceService.getDummyTrans();

        //expected 2 light bulbs and 3 waters (MAYBE or MAYBE! ) on order 2
        for(Map.Entry<Product, Integer> entry: invoice.entrySet()){
            System.out.println("PRODUCT: "+entry.getKey() + "  --- AMOUNT: " + entry.getValue());
        }
    }

    @Test
    public void invoiceSumTest(){
        System.out.println("\n");
        List<Invoice> invoices = invoiceService.getInvoices(1);
        if(invoices.size() >= 1){
            System.out.println(invoices.size());
            invoices.forEach(System.out::println);
        }
        System.out.println("\n");
    }

    @Test
    public void testsss(){

        System.out.println("CONVERTED VAT>>>>>>>>>> "+invoiceService.convertVAT("18"));


    }

}