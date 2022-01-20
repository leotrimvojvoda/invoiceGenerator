package com.example.invoicecalculator.api;


import com.example.invoicecalculator.data.ProductRepository;
import com.example.invoicecalculator.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductApi {

    ProductRepository productRepository;

    @Autowired
    public ProductApi(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody @Valid Product product){
        return  productRepository.save(product);
    }

    @PostMapping("/addAll")
    public List<Product> addAll(@RequestBody List<Product> products){
        return productRepository.saveAll(products);
    }

    @GetMapping("/get/{id}")
    public Product getProduct(@PathVariable int id)throws Exception{
        Optional<Product> product = productRepository.findById(id);

        return product.orElseThrow(() -> new Exception("Product with "+id+" not found"));
    }

    @GetMapping("/getAll")
    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/delete/{id}")
    public Product delete(@PathVariable int id)throws Exception{

        Optional<Product> product = productRepository.findById(id);

        productRepository.delete(product.orElseThrow(()-> new Exception("")));

        return product.get();

    }




}
