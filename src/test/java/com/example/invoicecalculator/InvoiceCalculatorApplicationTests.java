package com.example.invoicecalculator;

import com.example.invoicecalculator.data.TransactionRepository;
import com.example.invoicecalculator.entities.MakeOrder;
import com.example.invoicecalculator.entities.Product;
import com.example.invoicecalculator.entities.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InvoiceCalculatorApplicationTests {

	@Autowired
	TransactionRepository transactionRepository;

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

}