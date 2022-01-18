package com.example.invoicecalculator.services;

import com.example.invoicecalculator.data.ProductRepository;
import com.example.invoicecalculator.data.TransactionRepository;
import com.example.invoicecalculator.entities.Invoice;
import com.example.invoicecalculator.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

@Component
public class InvoiceService {

    private TreeMap<Product, Integer> transactions;

    //Create temporary invoices
    private Invoice tempInvoice;

    //Add invoices below 500$ to this list or single products above > 500$
    private final List<Invoice> invoices;

    //Hold items until one invoice is complete
    private TreeMap<Product, Integer> tempItems;

    //Hold pack of products with over 50 purchases of one product
    private NavigableMap<Product, Integer> fifties;

    private double subTotal;
    private double total;
    private double invoiceVat;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ProductRepository productRepository;

    public InvoiceService() {
        this.transactions = new TreeMap<>();
        this.tempInvoice = new Invoice();
        this.invoices = new ArrayList<>();
        this.tempItems = new TreeMap<>();
        this.fifties = new ConcurrentSkipListMap<>();
        this.subTotal = 0;
        this.invoiceVat = 0;
        this.total = 0;
    }

    public TreeMap<Product, Integer> getDummyTransactions(){

        Product p1 = new Product(1,"Milk",250,0,0.18);
        Product p2 = new Product(2,"Water",250,0,0.18);
        Product p3 = new Product(3,"Snickers",250,0,0.18);
        Product p4 = new Product(4,"Coke",250,0,0.18);
        Product p5 = new Product(5,"Tea",250,0,0.18);
        Product p6 = new Product(6,"Light Bulb",250,0,0.18);
        Product p7 = new Product(7,"TV",600,0,0);
        Product p8 = new Product(8,"Boiler",750,0.25,0.18);
        Product p9 = new Product(9, "A",10,0,0.1);
        Product p10 = new Product(10, "B",10,0,0.1);
        Product p11 = new Product(11, "C",10,0,0.1);
        Product p12 = new Product(12, "D",10,0,0.1);
        Product p13 = new Product(13, "E",10,0,0.1);
        Product p14 = new Product(14,"F",1,0.25,0.18);
        Product p15 = new Product(15, "G",1,0,0.18);
        Product p16 = new Product(16, "H61",2,0,0.18);
        Product p17 = new Product(17, "I62",2,0,0.18);
        Product p18 = new Product(18, "J63",2,0,0.18);
        Product p19 = new Product(19, "K",1,0,0.18);

        TreeMap<Product, Integer> list = new TreeMap<>();

        /*list.put(p1,1);
        list.put(p2,1);
        list.put(p3,1);
        list.put(p4,1);
        list.put(p5,1);
        list.put(p6,1);
        list.put(p7,1);
        list.put(p8,1);
        list.put(p9,10);
        list.put(p10,10);
        list.put(p11,10);
        list.put(p12,10);
        list.put(p13,10);*/
        list.put(p14,1);
        list.put(p15,33);
        list.put(p16,61);
        list.put(p17,62);
        list.put(p18,150);
       list.put(p19,1);

        return list;
    }

    public List<Invoice> getInvoices(int orderId){

        transactions =  getDummyTransactions();//getTransactions(orderId);

        Product product = new Product();
        int amount = 0;
        double discount = 0;
        double price = 0;
        double vat = 0;

        for(Map.Entry<Product, Integer> entry: transactions.entrySet()) {

            product = entry.getKey();
            amount = entry.getValue();

            discount = product.getDiscount();
            price = product.getProductPrice() - discount;
            vat = product.getVat(); //convertVAT(String.valueOf(product.getVat()));

            if (price > 500) {
                invoices.add(getOver500Invoice(product, price, discount, vat, amount));
                tempInvoice = new Invoice();
            }
            else if(amount > 50){

                int leftOver = addToFifties(product,amount);

                //In case there is leftover from the amount add it to the invoice
                //Example: Amount = 63 -> 13 will be the leftover
              if(leftOver > 0 ){
                  if ((total+((price*leftOver) + (price*leftOver) * vat)) < 500) {
                      //addProductToInvoice(product, leftOver, price, vat);
                  }else {
                      invoices.add(getCompleteInvoice());
                      clear();
                      //addProductToInvoice(product,leftOver,price,vat);
                  }
              }
            }else if ((total+((price*amount) + (price*amount) * vat)) < 500) {

                addProductToInvoice(product, amount, price, vat);

                if(!fifties.isEmpty()){

                    System.out.println("FIFTIES" );
                    for (Map.Entry<Product, Integer> tff : fifties.entrySet()){
                        System.out.println(tff.getKey().getProductName()+" - "+tff.getValue());
                    }

                    for(Map.Entry<Product, Integer> tf : fifties.entrySet()){

                        addFiftyToInvoice(tf.getKey(), tf.getValue());

                    }
                }
            }else {
                invoices.add(getCompleteInvoice());
                clear();
                addProductToInvoice(product,amount,price,vat);
            }
        }

        //If there are some products left then they will be added in a new invoice
        if(total > 0 || !fifties.isEmpty()){
            if(total > 0){
                //clear();
                //In case there is anything left in total that surpasses 500 it will be added to the invoice and surpass 500
                //Removing the below line will dismiss the last products that did not make it to the threshold
                addProductToInvoice(product,amount,price,vat);
                invoices.add(getCompleteInvoice());
            }else{
                while (!fifties.isEmpty()){
                        for(Map.Entry<Product, Integer> tf : fifties.entrySet()){
                            addFiftyToInvoice(tf.getKey(), tf.getValue());
                        }
                }
            }
        }

        System.out.println("FIFTIES IN THE END" );
        if(fifties.isEmpty()){
            System.out.println("- NO PRODUCTS LEFT IN FIFITES - ");
        }else{
            for (Map.Entry<Product, Integer> tff : fifties.entrySet()){
                System.out.println(tff.getKey().getProductName()+" - "+tff.getValue());
            }
        }

        return invoices;
    }

    /*
     * This method will return a list of transactions
     * associated  with the number of items sold per order
     * */
    public TreeMap<Product,Integer> getTransactions(int orderId){

        TreeMap<Product,Integer> map = new TreeMap<>();
        int numProducts;
        Product product;

        List<Integer> purchasedProducts = transactionRepository.getProductListInOneOrder(orderId);

        for(int i : purchasedProducts){
            product = productRepository.getById(i);
            numProducts = transactionRepository.getNumberOfPurchasedProductsInOneOrder(i,orderId);
            map.put(product,numProducts);
        }
        return map;
    }

    /*Add product in the invoice list and add its value to the invoice*/
    public void addProductToInvoice(Product product, int amount, double price, double vat){
        subTotal += price * amount;
        invoiceVat += (price*amount) * vat;
        total = subTotal+invoiceVat;
        tempItems.put(product, amount);
    }

    /*Save invoice with list of products and subtotal, vat and total of the invoice*/
    public Invoice getCompleteInvoice(){
        tempInvoice.setVat(invoiceVat);
        tempInvoice.setSubTotal(subTotal);
        tempInvoice.setTotal(total);
        tempInvoice.setItems(tempItems);

        return tempInvoice;
    }

    /*Save an invoice in case it is more than 500$*/
    public Invoice getOver500Invoice(Product product, double price, double discount, double vat, int amount){

        double subTotal = price * amount;
        double invoiceVat =  subTotal * vat;
        double total = subTotal + invoiceVat;

        TreeMap<Product, Integer> temp = new TreeMap<>();
        temp.put(product,amount);

        tempInvoice.setItems(temp);
        tempInvoice.setVat(invoiceVat);
        tempInvoice.setSubTotal(subTotal);
        tempInvoice.setTotal(total);

        return tempInvoice;
    }

    public void addFiftyToInvoice(Product product, int fiftyAmount){

        double thisFiftyTotal = (product.getProductPrice() * 50)
                + ((product.getProductPrice() * 50) * product.getVat());

        if((total + thisFiftyTotal ) < 500) {
            System.out.println(">>>> ADDED 50  < 500! " + product.getProductName());

            addProductToInvoice(product, 50, (product.getProductPrice() - product.getDiscount()), product.getVat());
            fifties.remove(product);
            fifties.put(product, fiftyAmount - 1);
            if (fiftyAmount < 1)
                fifties.remove(product);

        } else {
            invoices.add(getCompleteInvoice());
            clear();
            addProductToInvoice(product, 50, (product.getProductPrice() - product.getDiscount()), product.getVat());
            fifties.remove(product);
            fifties.put(product, fiftyAmount - 1);
            if (fiftyAmount < 1)
                fifties.remove(product);
        }

    }

    public int addToFifties(Product product, int amount){
        int tempAmount = amount;
        int leftOver = tempAmount % 50;
        tempAmount = tempAmount / 50;
        fifties.put(product,tempAmount);
        return leftOver;
    }

    public void clear(){
        subTotal = 0;
        invoiceVat = 0;
        total = 0;
        tempItems = new TreeMap<>();
        tempInvoice = new Invoice();
    }

    /*Convert vat 18 => 0.18*/
    public Double convertVAT(String vat){

        switch (vat.length()){
            case 1:
                vat = "0.0" + vat;
                break;
            case 2:
                vat = "0." + vat;
                break;
            case 3:
                vat = vat.charAt(0)+"."+vat.substring(1,vat.length());
                break;
            default:
                vat = "0";
                System.out.println("INVALID VAT VALUE > 999");
        }
        return Double.parseDouble(vat);
    }

}