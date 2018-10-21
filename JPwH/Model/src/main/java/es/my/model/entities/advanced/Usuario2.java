/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced;

import es.my.model.Constants;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author fran
 */
@Entity
@Table(name = "USUARIOS")
public class Usuario2 {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    private Direccion2 direccion;

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
    public Long       getId()        {return id;}
    public Direccion2 getDireccion() {return direccion;}

    public void setDireccion(final Direccion2 x) {this.direccion = x;}

    @Override
    public String toString() {
        return "Usuario2{" + "id=" + id + ", direccion=" + direccion + '}';
    }
}
