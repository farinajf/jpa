/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.compositekey.mapsid;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 *
 * @author fran
 */
@Entity
@Table(name = "USUARIO")
public class Usuario {

    @EmbeddedId
    private UsuarioId id;

    @ManyToOne
    @MapsId("empresaId")
    private Empresa empresa;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    protected Usuario() {}

    public Usuario(final UsuarioId x) {
        this.id = x;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public UsuarioId getId()      {return id;}
    public Empresa   getEmpresa() {return empresa;}

    public void setEmpresa(final Empresa x) {this.empresa = x;}

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", empresa=" + empresa + '}';
    }
}
