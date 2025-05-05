package com.market.MarketInventoryProject.repository;

import com.market.MarketInventoryProject.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Busca todas las coincidencias de nombre (insensible a mayúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
