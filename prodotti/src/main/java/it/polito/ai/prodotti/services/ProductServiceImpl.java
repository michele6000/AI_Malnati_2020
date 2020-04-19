package it.polito.ai.prodotti.services;

import it.polito.ai.prodotti.dtos.IngredientDTO;
import it.polito.ai.prodotti.dtos.ProductDTO;
import it.polito.ai.prodotti.entities.IngredientEntity;
import it.polito.ai.prodotti.entities.ProductEntity;
import it.polito.ai.prodotti.repositories.IngredientRepository;
import it.polito.ai.prodotti.repositories.Productrepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    Productrepository productrepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    IngredientRepository ingredientRepository;

    @Override
    public ProductDTO getProduct(String id) {
     /*   try {
            ProductEntity p = productrepository.getOne(id);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        }*/
        ProductEntity p = productrepository.getOne(id);
        if (p == null)
            return null;
        else return modelMapper.map(p, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getProducts() {
        //rimappo entity in DTO con ModelMApper
        return productrepository.findAll().stream().map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<IngredientDTO> getIngredients(String id) {
        ProductEntity p = productrepository.getOne(id);
        if (p == null) return null;

        return p.getIngredientEntities().stream().map(i -> modelMapper.map(i, IngredientDTO.class)).collect(Collectors.toList());

    }

    @Override
    public List<ProductDTO> getProductByIngredient(String ingredientID) {
        IngredientEntity ingredientEntity = ingredientRepository.getOne(ingredientID);
        if (ingredientEntity == null) return null;
        return productrepository.getByIngredientEntitiesContaining(ingredientEntity)
                .stream().map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public void addProduct(ProductDTO productDTO) {

    }

    @Override
    public Long getAvailableProduct(String id) {
        return null;
    }

    @Override
    public void removeProduct(String id) {

    }


    @Override
    public void produce(ProductDTO productDTO, long n) {

    }

    @Override
    public void sell(ProductDTO productDTO, long n) {

    }
}
