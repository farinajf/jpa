/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.compositekey.readonly;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @JoinColumn(
            name = "EMPRESAID",
            insertable = false,
            updatable = false
    )
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

    public Usuario(final String n, final Empresa e) {
        if (e.getId() == null) throw new IllegalStateException("Empresa no tiene Id: " + e);

        this.id      = new UsuarioId(n, e.getId());
        this.empresa = e;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public UsuarioId getId()      {return id;}
    public Empresa   getEmpresa() {return empresa;}

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", empresa=" + empresa + '}';
    }
}
