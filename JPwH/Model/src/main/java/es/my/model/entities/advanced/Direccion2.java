/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Embeddable
public class Direccion2 {

    @NotNull
    @Column(nullable = false)
    private String calle;

    @NotNull
    @AttributeOverrides(
            @AttributeOverride(
                    name = "nombre",
                    column = @Column(name = "PROVINCIA", nullable = false)
            )
    )
    private Provincia2 provincia;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String     getCalle()     {return calle;}
    public Provincia2 getProvincia() {return provincia;}

    public void setCalle    (final String     x) {this.calle     = x;}
    public void setProvincia(final Provincia2 x) {this.provincia = x;}

    @Override
    public String toString() {
        return "Direccion2{" + "calle=" + calle + ", provincia=" + provincia + '}';
    }
}
