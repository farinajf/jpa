/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.filtering.cascade;

import es.my.model.Constants;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BillingDetails {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String propietario;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public BillingDetails() {}

    public BillingDetails(final String p) {
        this.propietario = p;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long   getId()          {return id;}
    public String getPropietario() {return propietario;}

    public void setPropietario(String x) {this.propietario = x;}

    @Override
    public String toString() {
        return "BillingDetails{" + "id=" + id + ", propietario=" + propietario + '}';
    }
}
