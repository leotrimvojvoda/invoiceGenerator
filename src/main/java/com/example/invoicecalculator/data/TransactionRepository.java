package com.example.invoicecalculator.data;

import com.example.invoicecalculator.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
/*
* -Get all transactions that have an order X
* -Get all the products in that order
* -Count products by id
*
*
* */

    /*This query gets the number of distinct products purchased in one order
    * If in ode order were purchased 25 items,
    * 10 being of one type another 15 being of another type
    * this method will return the number 2 because 2 products were purchased multiple times.
    * */
    @Query(value = "SELECT COUNT(DISTINCT invoice_calculator.transaction.product_id) " +
            "FROM invoice_calculator.transaction " +
            "WHERE invoice_calculator.transaction.orderid = ?1",
            //countQuery = "SELECT COUNT(*) FROM TRANSACTION",
            nativeQuery = true)
    int getNumberOfDistinctProductsInOneOrder(int id);


    /*Get the list of products that were purchased in one order*/
    @Query(value = "SELECT DISTINCT invoice_calculator.transaction.product_id AS \"PRODUCTS FROM ORDER\" FROM invoice_calculator.transaction WHERE invoice_calculator.transaction.order_id = ?1",
            nativeQuery = true)
    List<Integer> getProductListInOneOrder(int orderId);

    /*Get the number of purchases of  a product that was ordered in one order*/
    @Query(value = "SELECT COUNT(product_id) FROM transaction t where t.product_id = ?1 AND t.order_id = ?2",
    nativeQuery = true)
    int getNumberOfPurchasedProductsInOneOrder(int product_id, int order_id);


    //Optional<Transaction> deleteTransactionById(int id);

    //void deleteAllByOrderId(int orderId);





}
