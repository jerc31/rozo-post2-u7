package com.universidad.apiproductos.controller;

import com.universidad.apiproductos.model.Producto;
import com.universidad.apiproductos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoApiController {

    @Autowired
    private ProductoService servicio;

    // GET /api/productos → 200 OK + lista JSON
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(servicio.obtenerTodos());
    }

    // GET /api/productos/{id} → 200 OK + objeto JSON, o 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscar(@PathVariable Long id) {
        Producto producto = servicio.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        return ResponseEntity.ok(producto);
    }

    // POST /api/productos → 201 Created + objeto creado
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto nuevo = servicio.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // PUT /api/productos/{id} → 200 OK + actualizado, o 404
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @RequestBody Producto producto) {
        Producto existente = servicio.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        producto.setId(id);
        return ResponseEntity.ok(servicio.guardar(producto));
    }

    // DELETE /api/productos/{id} → 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
