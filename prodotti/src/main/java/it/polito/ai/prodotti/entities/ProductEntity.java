package it.polito.ai.prodotti.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "product") // se specifico questo crea questa tabella con questo nome
//altrimenti product_entity
// con questo nelle proprietà spring.jpa.generate-ddl=true    crea in automatico
//con questo  spring.jpa.show-sql=true   fa vedere codice SQL che esegue
public class ProductEntity {
    @Id
    private String id;
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "product_ingredient", joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))

    private List<IngredientEntity> ingredientEntities;

}
