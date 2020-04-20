package it.polito.ai.prodotti.repositories;

import it.polito.ai.prodotti.entities.IngredientEntity;
import it.polito.ai.prodotti.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Productrepository extends JpaRepository<ProductEntity, String> {

    //nuovo metodo getBy + nome campo  "IngredientEntities"
    //lo uso in ProductServiceIMpl nel moodo getProductByIngredient
    List<ProductEntity> getByIngredientEntitiesContaining(IngredientEntity i);
}
