/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.asociaciones.onetomany;

import es.my.model.Constants;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    @OneToMany(mappedBy = "usuario")
    private Set<BillingDetails> billingDetails = new HashSet<>();

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
public User() {}

    public User(final String x) {
        this.nombre = x;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long                getId()             {return id;}
    public String              getNombre()         {return nombre;}
    public Set<BillingDetails> getBillingDetails() {return billingDetails;}

    public void setNombre        (final String              x) {this.nombre         = x;}
    public void setBillingDetails(final Set<BillingDetails> x) {this.billingDetails = x;}

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", nombre=" + nombre + ", billingDetails=" + billingDetails + '}';
    }
}
