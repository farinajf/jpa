/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.custom;

import es.my.model.Constants;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author fran
 */
@Entity
@Table(
        name = "USUARIOS",
        uniqueConstraints =
                @UniqueConstraint(
                        name = "UNQ_NOMBRE_EMAIL",
                        columnNames = {"NOMBRE", "EMAIL"}
                ),
        indexes = {
            @Index(
                    name = "IND_NOMBRE",
                    columnList = "NOMBRE"
            ),
            @Index(
                    name = "IND_NOMBRE_EMAIL",
                    columnList = "NOMBRE, EMAIL"
            )
        }
)
public class Usuario {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            columnDefinition = "EMAIL_ADDRESS(255)"
    )
    private String email;

    @Column(columnDefinition =
            "VARCHAR(15) NOT NULL UNIQUE CHECK(NOT SUBSTRING(LOWER(nombre), 0, 5) = 'admin')"
            )
    private String nombre;

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

    public Usuario(final String nombre, final String email) {
        this.nombre = nombre;
        this.email  = email;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long   getId()     {return id;}
    public String getEmail()  {return email;}
    public String getNombre() {return nombre;}

    public void setEmail (final String x) {this.email  = x;}
    public void setNombre(final String x) {this.nombre = x;}
}
