/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetomany.embeddablejointable;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
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
    @Column(nullable = false)
    private String provincia;

    @OneToMany
    @JoinTable(
            name = "DIRECCION_ENVIO",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_ENVIO")
    )
    private Set<Envio> envios = new HashSet<>();

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

    public Direccion(final String c, final String p) {
        this.calle     = c;
        this.provincia = p;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String     getCalle()     {return calle;}
    public String     getProvincia() {return provincia;}
    public Set<Envio> getEnvios()    {return envios;}

    public void setCalle    (final String     x) {this.calle     = x;}
    public void setEnvios   (final Set<Envio> x) {this.envios    = x;}
    public void setProvincia(final String     x) {this.provincia = x;}

    @Override
    public String toString() {
        return "Direccion{" + "calle=" + calle + ", provincia=" + provincia + ", envios=" + envios + '}';
    }
}
