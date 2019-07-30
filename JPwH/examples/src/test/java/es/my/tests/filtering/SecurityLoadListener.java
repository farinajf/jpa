/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.filtering;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.spi.LoadEvent;
import org.hibernate.event.spi.LoadEventListener;

/**
 *
 * @author fran
 */
public class SecurityLoadListener extends DefaultLoadEventListener {

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
    /**
     *
     * @param event
     * @param loadType
     * @throws HibernateException
     */
    @Override
    public void onLoad(final LoadEvent event, final LoadEventListener.LoadType loadType) throws HibernateException {
        System.out.println("SecurityLoadListener.onLoad()");

        final boolean authorized = MySecurity.isAuthorized(event.getEntityClassName(), event.getEntityId());

        if (authorized == false) throw new MySecurityException("No autorizado!!");

        super.onLoad(event, loadType);
    }

    /**
     *
     */
    static public class MySecurity {
        static boolean isAuthorized(final String entityName, final Serializable entityId) {return true;}
    }

    /**
     *
     */
    static public class MySecurityException extends RuntimeException {
        public MySecurityException(final String m) {super(m);}
    }
}
