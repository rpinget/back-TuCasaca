package com.tucasaca.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tucasaca.tienda.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}