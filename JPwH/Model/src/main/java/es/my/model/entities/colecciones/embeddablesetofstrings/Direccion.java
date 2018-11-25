/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.colecciones.embeddablesetofstrings;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Embeddable
public class Direccion {

    @NotNull
    @Column(nullable = false)
    private String calle;

    @NotNull
    @Column(nullable = false, length = 5)
    private String codigoPostal;

    @NotNull
    @Column(nullable = false)
    private String ciudad;

    @ElementCollection
    @Column(name = "CONTACTO", nullable = false)
    @CollectionTable(
            name = "TABLA_CONTACTOS",
            joinColumns = @JoinColumn(name = "ID_USUARIO")
    )
    private Set<String> contactos = new HashSet<>();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Direccion() {}

    public Direccion(final String calle, final String cp, final String ciudad) {
        this.calle        = calle;
        this.codigoPostal = cp;
        this.ciudad       = ciudad;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String      getCalle()        {return calle;}
    public String      getCodigoPostal() {return codigoPostal;}
    public String      getCiudad()       {return ciudad;}
    public Set<String> getContactos()    {return contactos;}

    public void setCalle       (final String      x) {this.calle        = x;}
    public void setCodigoPostal(final String      x) {this.codigoPostal = x;}
    public void setCiudad      (final String      x) {this.ciudad       = x;}
    public void setContactos   (final Set<String> x) {this.contactos    = x;}

    @Override
    public String toString() {
        return "Direccion{" + "calle=" + calle + ", codigoPostal=" + codigoPostal + ", ciudad=" + ciudad + ", contactos=" + contactos + '}';
    }
}
