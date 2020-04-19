package it.polito.ai.prodotti.services;

import it.polito.ai.prodotti.dtos.IngredientDTO;
import it.polito.ai.prodotti.dtos.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO getProduct(String id);

    List<ProductDTO> getProducts();

    void addProduct(ProductDTO productDTO);

    Long getAvailableProduct(String id);

    void removeProduct(String id);

    List<ProductDTO> getProductByIngredient(String ingredientID);

    List<IngredientDTO> getIngredients(String id);

    void produce(ProductDTO productDTO, long n);

    void sell(ProductDTO productDTO, long n);
}
