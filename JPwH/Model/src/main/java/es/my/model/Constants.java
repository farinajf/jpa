/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.my.model;

/**
 *
 * @author fran
 */
public class Constants {
    public static final String ID_GENERATOR        = "ID_GENERATOR";
    public static final String ID_GENERATOR_POOLED = "ID_GENERATOR_POOLED";
    public static final long   TIME_OFFSET_MS      = 3600000L;

    public static void print(final String x) {
        System.out.println("*-------------------------------------------------*");
        System.out.println("*- " + x + "  -*");
        System.out.println("*-------------------------------------------------*");
    }
}
