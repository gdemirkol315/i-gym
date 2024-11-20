package com.igym.controller;

import com.igym.dto.ProductDTO;
import com.igym.dto.EntryProductDTO;
import com.igym.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPERVISOR') or hasRole('MANAGER')")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping("/entry/list")
    public ResponseEntity<List<EntryProductDTO>> getEntryProducts() {
        List<EntryProductDTO> entryProducts = productService.getAllEntryProducts();
        return ResponseEntity.ok(entryProducts);
    }

    @PostMapping("/entry/create")
    @PreAuthorize("hasRole('SUPERVISOR') or hasRole('MANAGER')")
    public ResponseEntity<EntryProductDTO> createEntryProduct(@RequestBody EntryProductDTO entryProductDTO) {
        EntryProductDTO createdEntryProduct = productService.createEntryProduct(entryProductDTO);
        return ResponseEntity.ok(createdEntryProduct);
    }
}
