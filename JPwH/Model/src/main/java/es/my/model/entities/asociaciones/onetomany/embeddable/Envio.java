/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetomany.embeddable;

import es.my.model.Constants;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class Envio {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private java.util.Date fecha = new Date();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Envio() {}

    public Envio(final java.util.Date f) {
        this.fecha = f;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long getId()    {return id;}
    public Date getFecha() {return fecha;}

    public void setFecha(final java.util.Date x) {this.fecha = x;}

    @Override
    public String toString() {
        return "Envio{" + "id=" + id + ", fecha=" + fecha + '}';
    }
}
