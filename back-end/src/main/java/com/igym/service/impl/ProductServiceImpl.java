package com.igym.service.impl;

import com.igym.dto.ProductDTO;
import com.igym.dto.EntryProductDTO;
import com.igym.entity.Product;
import com.igym.entity.EntryProduct;
import com.igym.entity.Inventory;
import com.igym.repository.ProductRepository;
import com.igym.repository.EntryProductRepository;
import com.igym.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final EntryProductRepository entryProductRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, EntryProductRepository entryProductRepository) {
        this.productRepository = productRepository;
        this.entryProductRepository = entryProductRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice().doubleValue());
        
        // Create and set inventory
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantityInStock(productDTO.getQuantity());
        product.setInventory(inventory);

        Product savedProduct = productRepository.save(product);
        return mapToProductDTO(savedProduct);
    }

    @Override
    public List<EntryProductDTO> getAllEntryProducts() {
        return entryProductRepository.findAll().stream()
                .map(this::mapToEntryProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EntryProductDTO createEntryProduct(EntryProductDTO entryProductDTO) {
        EntryProduct entryProduct = new EntryProduct();
        entryProduct.setName(entryProductDTO.getName());
        entryProduct.setDuration(entryProductDTO.getDuration());
        entryProduct.setMaxEntries(entryProductDTO.getMaxEntries());
        entryProduct.setPrice(entryProductDTO.getPrice().doubleValue());
        entryProduct.setEntryType(EntryProduct.EntryType.valueOf(entryProductDTO.getEntryType()));

        EntryProduct savedEntryProduct = entryProductRepository.save(entryProduct);
        return mapToEntryProductDTO(savedEntryProduct);
    }

    private ProductDTO mapToProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setPrice(BigDecimal.valueOf(product.getPrice()));
        dto.setQuantity(product.getInventory() != null ? product.getInventory().getQuantityInStock() : 0);
        return dto;
    }

    private EntryProductDTO mapToEntryProductDTO(EntryProduct entryProduct) {
        EntryProductDTO dto = new EntryProductDTO();
        dto.setId(entryProduct.getId());
        dto.setName(entryProduct.getName());
        dto.setDuration(entryProduct.getDuration());
        dto.setMaxEntries(entryProduct.getMaxEntries());
        dto.setPrice(BigDecimal.valueOf(entryProduct.getPrice()));
        dto.setEntryType(entryProduct.getEntryType().name());
        return dto;
    }
}
