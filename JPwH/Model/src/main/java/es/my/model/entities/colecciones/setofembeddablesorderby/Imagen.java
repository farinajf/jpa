/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.colecciones.setofembeddablesorderby;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author fran
 */
@Embeddable
public class Imagen {

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String fichero;

    private int ancho;
    private int alto;

    // Permite incluir Item en el metodo equals() de manera que se pueda distinguir
    // dos imagenes que tiene los mismos atributos pero pertenecen a distintos Items.
    @org.hibernate.annotations.Parent
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
    public Imagen() {}

    public Imagen(final String t, final String f, final int x, final int y) {
        this.titulo  = t;
        this.fichero = f;
        this.ancho   = x;
        this.alto    = y;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Item   getItem()    {return item;}
    public String getFichero() {return fichero;}
    public String getTitulo()  {return titulo;}
    public int    getAncho()   {return ancho;}
    public int    getAlto()    {return alto;}

    public void setItem   (Item   x) {this.item    = x;}
    public void setTitulo (String x) {this.titulo  = x;}
    public void setFichero(String x) {this.fichero = x;}
    public void setAncho  (int    x) {this.ancho   = x;}
    public void setAlto   (int    x) {this.alto    = x;}

    @Override
    public int hashCode() {
        int result = titulo.hashCode();

        result = 31 * result + fichero.hashCode();
        result = 31 * result + ancho;
        result = 31 * result + alto;

        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Imagen x = (Imagen) o;

        if (!fichero.equals(x.getFichero())) return false;
        if (!titulo.equals(x.getTitulo()))   return false;
        if (ancho != x.getAncho())           return false;
        if (alto  != x.getAlto())            return false;

        return true;
    }

    @Override
    public String toString() {
        return "Imagen{" + "titulo=" + titulo + ", fichero=" + fichero + ", ancho=" + ancho + ", alto=" + alto + '}';
    }
}
