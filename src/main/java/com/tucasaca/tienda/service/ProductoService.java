package com.tucasaca.tienda.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tucasaca.tienda.model.Producto;
import com.tucasaca.tienda.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto getProductoById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }
}