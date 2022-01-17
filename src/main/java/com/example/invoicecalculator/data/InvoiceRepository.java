package com.example.invoicecalculator.data;

import com.example.invoicecalculator.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface InvoiceRepository /* extends JpaRepository<Invoice,Integer> */{


    /*@Query(" SELECT new com.example.invoicecalculator.entities.Invoice(id,invoideName,quantity) " +
            " FROM Invoice a" +
            " WHERE a.deletedAt IS NULL AND a.id=?1")
    Optional<Invoice> findArticleById(Long id);*/
}
