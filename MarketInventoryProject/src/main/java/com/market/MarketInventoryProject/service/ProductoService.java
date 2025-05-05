package com.market.MarketInventoryProject.service;

import com.market.MarketInventoryProject.model.Producto;
import com.market.MarketInventoryProject.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Producto guardar(Producto producto) {
        return repository.save(producto);
    }

    public Producto actualizar(Long id, Producto nuevoProducto) {
        return repository.findById(id).map(producto -> {
            producto.setNombre(nuevoProducto.getNombre());
            producto.setDescripcion(nuevoProducto.getDescripcion());
            producto.setPrecio(nuevoProducto.getPrecio());
            producto.setStock(nuevoProducto.getStock());
            return repository.save(producto);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }


    public Page<Producto> obtenerTodosPaginados(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public List<Producto> buscarPorIdONombre(String term) {
        // 1) Intentar ID
        try {
            Long id = Long.parseLong(term);
            Optional<Producto> found = repository.findById(id);
            if (found.isPresent()) {
                return List.of(found.get());
            }
        } catch (NumberFormatException e) {
            // no es un número → seguimos a buscar por nombre
        }

        // 2) Buscar por nombre (contiene, ignore case)
        return repository.findByNombreContainingIgnoreCase(term);
    }
}

