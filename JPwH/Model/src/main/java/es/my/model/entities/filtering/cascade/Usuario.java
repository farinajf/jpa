/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.filtering.cascade;

import es.my.model.Constants;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
@Table(name = "USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Set<BillingDetails> billingDetails = new HashSet<BillingDetails>();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Usuario() {}

    public Usuario(final String n) {
        this.nombre = n;
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
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", billingDetails=" + billingDetails + '}';
    }
}
