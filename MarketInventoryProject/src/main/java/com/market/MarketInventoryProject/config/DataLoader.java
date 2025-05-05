package com.market.MarketInventoryProject.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.MarketInventoryProject.model.Producto;
import com.market.MarketInventoryProject.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository repository;

    public DataLoader(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("productos.json");

            if (inputStream != null) {
                List<Producto> productos = objectMapper.readValue(inputStream, new TypeReference<List<Producto>>() {});
                repository.saveAll(productos);
                System.out.println("Se cargaron " + productos.size() + " productos desde productos.json");
            } else {
                System.out.println("No se encontró el archivo productos.json");
            }
        } else {
            System.out.println("La base de datos ya contiene productos.");
        }
    }
}
