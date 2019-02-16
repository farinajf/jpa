/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.custom;

import es.my.model.Constants;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
@org.hibernate.annotations.Check(
        constraints = "FECHAINICIO < FECHAFIN"
)
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    private String nombre;

    @NotNull
    private java.util.Date fechaInicio;

    @NotNull
    private java.util.Date fechaFin;

    @OneToMany(mappedBy = "item")
    private Set<Bid> bids = new HashSet<Bid>();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Item() {}

    public Item(final String nombre, final Date fechaInicio, final Date fechaFin) {
        this.nombre      = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long     getId()          {return id;}
    public String   getNombre()      {return nombre;}
    public Date     getFechaInicio() {return fechaInicio;}
    public Date     getFechaFin()    {return fechaFin;}
    public Set<Bid> getBids()        {return bids;}

}
