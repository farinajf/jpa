/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.jph.shared.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author fran
 */
public class CalendarUtil {

    public static final Calendar HOY            = new GregorianCalendar();
    public static final Calendar MANHANA        = new GregorianCalendar();
    public static final Calendar PASADO_MANHANA = new GregorianCalendar();

    static {
        HOY.set(Calendar.HOUR_OF_DAY, 23);
        HOY.set(Calendar.MINUTE,      59);

        MANHANA.roll       (Calendar.DAY_OF_YEAR, true);
        PASADO_MANHANA.roll(Calendar.DAY_OF_YEAR, true);
        PASADO_MANHANA.roll(Calendar.DAY_OF_YEAR, true);
    }

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

}
