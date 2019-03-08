/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.concurrency.versiontimestamp;

import es.my.model.Constants;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @Version
    private Date lastUpdated;

    @NotNull
    private String nombre;

    private BigDecimal precio;

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

    public Item(final String n) {
        this.nombre = n;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long       getId()          {return id;}
    public Date       getLastUpdated() {return lastUpdated;}
    public String     getNombre()      {return nombre;}
    public BigDecimal getPrecio()      {return precio;}

    public void setNombre(final String     x) {this.nombre = x;}
    public void setPrecio(final BigDecimal x) {this.precio = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", lastUpdated=" + lastUpdated + ", nombre=" + nombre + ", precio=" + precio + '}';
    }
}
