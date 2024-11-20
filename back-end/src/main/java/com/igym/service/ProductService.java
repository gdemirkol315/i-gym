package com.igym.service;

import com.igym.dto.ProductDTO;
import com.igym.dto.EntryProductDTO;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO createProduct(ProductDTO productDTO);
    List<EntryProductDTO> getAllEntryProducts();
    EntryProductDTO createEntryProduct(EntryProductDTO entryProductDTO);
}
