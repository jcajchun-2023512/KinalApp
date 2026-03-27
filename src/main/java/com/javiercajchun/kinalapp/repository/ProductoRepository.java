package com.javiercajchun.kinalapp.repository;

import com.javiercajchun.kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
