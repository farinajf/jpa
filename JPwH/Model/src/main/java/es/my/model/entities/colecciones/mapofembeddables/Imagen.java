/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.colecciones.mapofembeddables;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author fran
 */
@Embeddable
public class Imagen {

    // Puede ser nulo si se establece una PK subrogada
    @Column(nullable = true)
    private String titulo;

    private int ancho;
    private int alto;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Imagen() {}

    public Imagen(final String t, final int x, final int y) {
        this.titulo = t;
        this.ancho  = x;
        this.alto   = y;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getTitulo() {return titulo;}
    public int    getAncho()  {return ancho;}
    public int    getAlto()   {return alto;}

    public void setTitulo(String x) {this.titulo = x;}
    public void setAncho (int    x) {this.ancho  = x;}
    public void setAlto  (int    x) {this.alto   = x;}

    @Override
    public int hashCode() {
        int hash = 3;

        hash = 59 * hash + Objects.hashCode(this.titulo);
        hash = 59 * hash + this.ancho;
        hash = 59 * hash + this.alto;

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) return false;

        if (getClass() != o.getClass()) return false;

        final Imagen other = (Imagen) o;
        if (this.ancho != other.ancho) return false;

        if (this.alto != other.alto) return false;

        if (!Objects.equals(this.titulo, other.titulo)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Imagen{" + "titulo=" + titulo + ", ancho=" + ancho + ", alto=" + alto + '}';
    }
}
