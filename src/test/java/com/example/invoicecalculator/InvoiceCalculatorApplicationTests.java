package com.example.invoicecalculator;

import com.example.invoicecalculator.api.TransactionApi;
import com.example.invoicecalculator.data.TransactionRepository;
import com.example.invoicecalculator.entities.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
class InvoiceCalculatorApplicationTests {

	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	TransactionApi transactionApi;

	@Test
	void contextLoads() {

		//System.out.println(transactionRepository.findAll());
		Transaction transaction = new Transaction();
		transaction.setOrderId(1);
		transaction.setProductId(8);

		for(int i = 0; i <= 188; i++){
			transactionRepository.save(transaction);
		}
	}

	@Test
	public void getTransactions(){
		transactionRepository.findAll().forEach(System.out::println);
	}

	@Test
	public void getBoughtProducts(){
		//System.out.println("PURCHASED PRODUCTS: "+transactionRepository.getAllProducts());;
		//transactionRepository.getAllProducts().forEach(System.out::println);
		System.out.println(transactionRepository.getNumberOfDistinctProductsInOneOrder(2));
	}

	@Test
	public void getDistinct(){
		transactionRepository.getProductListInOneOrder(1).forEach(System.out::println);
		System.out.println(transactionRepository.getNumberOfPurchasedProductsInOneOrder(1,1));
	}

	@Test
	public void getNumOfPurchaedProducts(){
		System.out.println(transactionRepository.getNumberOfPurchasedProductsInOneOrder(1,1));
	}


	@Test
	public void makeTransaction()throws Exception{
		Transaction transaction = new Transaction();
		transaction.setOrderId(1);
		transaction.setProductId(7);

		transactionApi.makeTransaction(transaction);

	}

	@Test
	void deleteTransaction()throws Exception{
		transactionApi.delete(112);
	}



}