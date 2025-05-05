package com.market.MarketInventoryProject.controller;

import com.market.MarketInventoryProject.model.Producto;
import com.market.MarketInventoryProject.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/web/productos")
public class ProductoWebController {

    private static final Logger log = LoggerFactory.getLogger(ProductoWebController.class);
    private final ProductoService productoService;

    public ProductoWebController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Método auxiliar para parsear y construir Sort según los parámetros recibidos
    private Sort parseSortParams(String[] sortParams) {
        List<Sort.Order> orders = new ArrayList<>();
        log.debug("Parseando sortParams: {}", Arrays.toString(sortParams));

        if (sortParams != null) {
            for (String param : sortParams) {
                if (param != null && !param.trim().isEmpty()) {
                    String[] parts = param.split(",");
                    if (parts.length == 2) {
                        try {
                            String field = parts[0].trim();
                            String directionStr = parts[1].trim();
                            Sort.Direction direction = Sort.Direction.fromString(directionStr);
                            orders.add(new Sort.Order(direction, field));
                            log.debug("Añadida orden: {} {}", direction, field);
                        } catch (IllegalArgumentException e) {
                            log.warn("Parámetro de ordenación ignorado - dirección inválida: {}", param);
                        }
                    } else {
                        log.warn("Parámetro de ordenación ignorado - formato inválido (se esperaba 'campo,direccion'): {}", param);
                    }
                }
            }
        }

        if (orders.isEmpty()) {
            log.debug("No se proporcionó ordenación válida o ninguna, usando default: nombre,asc");
            orders.add(new Sort.Order(Sort.Direction.ASC, "nombre"));
        }

        Sort result = Sort.by(orders);
        log.debug("Ordenación final parseada: {}", result);
        return result;
    }

    @GetMapping
    public String listarProductos(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(required = false) String[] sort,
                                  @RequestParam(required = false) String term) {
        // Si hay término de búsqueda, devolvemos lista sin paginación
        if (term != null && !term.isBlank()) {
            List<Producto> resultados = productoService.buscarPorIdONombre(term.trim());
            model.addAttribute("resultados", resultados);
            model.addAttribute("searchTerm", term.trim());
            return "list-productos";
        }

        if (size <= 0) {
            size = 5;
        }

        // 1. Obtención de datos con paginación y orden
        Sort sortOrder = parseSortParams(sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Producto> productosPage = productoService.obtenerTodosPaginados(pageable);

        // 2. Cálculo de límites de paginación
        int maxPagesToShow = 5;
        int currentPage    = productosPage.getNumber();
        int totalPages     = productosPage.getTotalPages();

        int half = maxPagesToShow / 2;
        int startPage = Math.max(0, currentPage - half);
        int endPage   = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);
        startPage     = Math.max(0, endPage - maxPagesToShow + 1);

        // 3. Inyección al modelo
        model.addAttribute("productosPage", productosPage);
        model.addAttribute("sortParams", sort != null ? Arrays.asList(sort) : List.of("nombre,asc"));
        model.addAttribute("currentSort", sortOrder.toString());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", currentPage);

        return "list-productos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "Nuevo Producto");
        return "form-producto";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        Optional<Producto> productoOptional = productoService.obtenerPorId(id);
        if (productoOptional.isPresent()) {
            model.addAttribute("producto", productoOptional.get());
            model.addAttribute("titulo", "Editar Producto");
            return "form-producto";
        } else {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/web/productos";
        }
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  RedirectAttributes redirectAttributes) {
        boolean isNew = producto.getId() == null;
        try {
            if (isNew) {
                productoService.guardar(producto);
                redirectAttributes.addFlashAttribute("success", "Producto creado exitosamente.");
            } else {
                productoService.actualizar(producto.getId(), producto);
                redirectAttributes.addFlashAttribute("success", "Producto actualizado exitosamente.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el producto: " + e.getMessage());
        }
        return "redirect:/web/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto. Es posible que no exista.");
        }
        return "redirect:/web/productos";
    }
}

