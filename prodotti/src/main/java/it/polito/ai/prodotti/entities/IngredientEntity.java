package it.polito.ai.prodotti.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ingredient")
public class IngredientEntity {
    @Id
    private String id;
    private String name;
    // @ManyToMany
    // private List<IngredientEntiity> ingredientEntity;

}