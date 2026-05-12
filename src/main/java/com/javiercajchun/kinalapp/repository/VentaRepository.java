package com.javiercajchun.kinalapp.repository;

import com.javiercajchun.kinalapp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
