/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.embeddable;

import es.my.model.Constants;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author fran
 */
@Entity
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "El atributo nombre es obligatorio"
    )
    private String nombre;

    private Dimensiones dimensiones;
    private Peso        peso;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Item() {}

    public Item(final String x, final Dimensiones y, final Peso z) {
        this.nombre      = x;
        this.dimensiones = y;
        this.peso        = z;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long        getId()          {return id;}
    public Dimensiones getDimensiones() {return dimensiones;}
    public String      getNombre()      {return nombre;}
    public Peso        getPeso()        {return peso;}

    public void setNombre     (String      x) {this.nombre      = x;}
    public void setDimensiones(Dimensiones x) {this.dimensiones = x;}
    public void setPeso       (Peso        x) {this.peso        = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", nombre=" + nombre + ", dimensiones=" + dimensiones + ", peso=" + peso + '}';
    }
}
