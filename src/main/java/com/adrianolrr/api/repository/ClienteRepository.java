package com.adrianolrr.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adrianolrr.api.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> { }
