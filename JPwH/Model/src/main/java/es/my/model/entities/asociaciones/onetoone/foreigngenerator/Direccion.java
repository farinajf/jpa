/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetoone.foreigngenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

/**
 * OneToOne: Foreign Primary Key
 * 
 * @author fran
 */
@Entity
public class Direccion {

    @Id
    @GeneratedValue(generator = "addressKeyGenerator")
    @org.hibernate.annotations.GenericGenerator(
            name = "addressKeyGenerator",
            strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "usuario")
    )
    private Long id;

    @NotNull                  // Ignorado en la generacion del DDL
    private String calle;

    @NotNull
    private String zipCode;

    @NotNull
    private String provincia;

    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn
    private Usuario usuario;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    protected Direccion() {}

    private Direccion(final Usuario x) {
        this.usuario = x;
    }

    public Direccion(final Usuario usuario, final String calle, final String zipCode, final String provincia) {
        this.usuario   = usuario;
        this.calle     = calle;
        this.zipCode   = zipCode;
        this.provincia = provincia;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long    getId()        {return id;}
    public String  getCalle()     {return calle;}
    public String  getProvincia() {return provincia;}
    public Usuario getUsuario()   {return usuario;}
    public String  getZipCode()   {return zipCode;}

    public void setCalle    (final String x) {this.calle     = x;}
    public void setProvincia(final String x) {this.provincia = x;}
    public void setZipCode  (final String x) {this.zipCode   = x;}

    @Override
    public String toString() {
        return "Direccion{" + "id=" + id + ", calle=" + calle + ", zipCode=" + zipCode + ", provincia=" + provincia + ", usuario=" + usuario + '}';
    }
}
