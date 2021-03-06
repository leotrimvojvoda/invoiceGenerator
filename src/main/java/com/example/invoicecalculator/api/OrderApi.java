package com.example.invoicecalculator.api;

import com.example.invoicecalculator.data.OrderRepository;
import com.example.invoicecalculator.entities.MakeOrder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderApi {

    OrderRepository orderRepository;

    @Autowired
    public OrderApi(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /*MAYBE REQUESTBODY SHOULD BE REMOVED */
    @PostMapping("/add")
    public MakeOrder addOrder(@RequestBody @Valid MakeOrder order){
        order.setTotal(0);
        order.setVat(0);
        order.setSubTotal(0);
        return  orderRepository.save(order);
    }

    @GetMapping("/get/{id}")
    public MakeOrder getOrder(@PathVariable int id)throws Exception{

        Optional<MakeOrder> order = orderRepository.findById(id);

        return order.orElseThrow(() -> new Exception("Order not found"));

    }

    @GetMapping("/getAll")
    public List<MakeOrder> getOrders(){
        return orderRepository.findAll();
    }

    @PostMapping("/delete/{id}")
    public MakeOrder delete(@PathVariable int id)throws Exception{

        Optional<MakeOrder> order = orderRepository.findById(id);

        orderRepository.delete(order.orElseThrow(() -> new Exception("Order not found")));

        return order.get();
    }


}