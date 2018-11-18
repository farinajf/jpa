/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.asociaciones.onetomany;

import es.my.model.Constants;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * OJO: no se puede usar @MappedSuperclass cuando se usa en asociaciones.
 *
 * @author fran
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BillingDetails {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private User usuario;

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
    public Long   getId()      {return id;}
    public String getOwner()   {return owner;}
    public User   getUsuario() {return usuario;}

    public void setOwner  (final String x) {this.owner   = x;}
    public void setUsuario(final User   x) {this.usuario = x;}

    public void pagar(final int x) {}

    @Override
    public String toString() {
        return "BillingDetails{" + "id=" + id + ", owner=" + owner + ", usuario=" + usuario + '}';
    }
}
