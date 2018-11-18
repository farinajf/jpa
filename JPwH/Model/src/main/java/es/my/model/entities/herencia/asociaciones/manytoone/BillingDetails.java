/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.asociaciones.manytoone;

import es.my.model.Constants;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

/**
 * OJO: no se puede usar @MappedSuperclass cuando se usa en asociaciones.
 *
 * @author fran
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BillingDetails {

    private Long id;

    @NotNull
    private String owner;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    protected BillingDetails() {}

    protected BillingDetails(final String owner) {
        this.owner = owner;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    public Long   getId()    {return id;}
    public String getOwner() {return owner;}

    public void setId   (final Long   x) {this.id    = x;}
    public void setOwner(final String x) {this.owner = x;}

    public void pagar(final int x) {}

    @Override
    public String toString() {
        return "BillingDetails{" + "id=" + id + ", owner=" + owner + '}';
    }
}
