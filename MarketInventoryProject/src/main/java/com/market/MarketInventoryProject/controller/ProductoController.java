package com.market.MarketInventoryProject.controller;

import com.market.MarketInventoryProject.model.Producto;
import com.market.MarketInventoryProject.service.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return service.guardar(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return ResponseEntity.ok(service.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/paginado")
    public ResponseEntity<?> getAllProductosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nombre,asc") String[] sort
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
            Page<Producto> productosPage = service.obtenerTodosPaginados(pageable);
            return ResponseEntity.ok(productosPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Dirección de orden inválida. Use 'asc' o 'desc'.");
        }
    }

    private List<Sort.Order> parseSort(String[] sortParams) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String param : sortParams) {
            String[] parts = param.split(",");
            if (parts.length == 2) {
                String field = parts[0].trim();
                String directionStr = parts[1].trim().toUpperCase();
                Sort.Direction direction = Sort.Direction.fromString(directionStr);
                orders.add(new Sort.Order(direction, field));
            } else {
                throw new IllegalArgumentException("Parámetro de orden inválido: " + param);
            }
        }
        return orders;
    }
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscar(@RequestParam String term) {
        List<Producto> resultados = service.buscarPorIdONombre(term.trim());
        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultados);
    }
}
