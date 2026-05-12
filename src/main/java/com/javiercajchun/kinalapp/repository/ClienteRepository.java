package com.javiercajchun.kinalapp.repository;

import com.javiercajchun.kinalapp.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ClienteRepository extends JpaRepository<Cliente,String> {

}