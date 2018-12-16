/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetoone.jointable;

import es.my.model.Constants;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class Transporte {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private Date fechaCreacion = new Date();

    @NotNull
    private EstadoEnvio estado = EstadoEnvio.TRANSITO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ITEM_TRANSPORT", //Required
            joinColumns = @JoinColumn(name = "TRANSPORT_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID", nullable = false, unique = true)
    )
    private Item item;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Transporte() {}

    public Transporte(final Item x) {
        this.item = x;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long getId()            {return id;}
    public Date getFechaCreacion() {return fechaCreacion;}
    public Item getItem()          {return item;}
    public EstadoEnvio getEstado() {return estado;}

    public void setEstado       (final EstadoEnvio x) {this.estado        = x;}
    public void setFechaCreacion(final Date        x) {this.fechaCreacion = x;}
    public void setItem         (final Item        x) {this.item          = x;}

    @Override
    public String toString() {
        return "Transporte{" + "id=" + id + ", fechaCreacion=" + fechaCreacion + ", estado=" + estado + ", item=" + item + '}';
    }
}
