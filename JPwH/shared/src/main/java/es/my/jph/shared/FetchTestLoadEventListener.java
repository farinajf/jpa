/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.jph.shared;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.service.ServiceRegistry;

/**
 * Se invoca despues de que una entidad es cargada.
 * 
 * @author fran
 */
public class FetchTestLoadEventListener implements PostLoadEventListener {

    private Map<Class, Integer> _loadCount = new HashMap<Class, Integer>();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    /**
     *
     * @param emf
     */
    public FetchTestLoadEventListener(EntityManagerFactory emf) {
        ServiceRegistry serviceRegistry = ((SessionFactoryImplementor) emf.unwrap(org.hibernate.SessionFactory.class)).getServiceRegistry();

        serviceRegistry.getService(EventListenerRegistry.class).appendListeners(EventType.POST_LOAD, this);
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    @Override
    public void onPostLoad(PostLoadEvent event) {
        Class entityType = event.getEntity().getClass();

        if (!_loadCount.containsKey(entityType)) _loadCount.put(entityType, 0);

        _loadCount.put(entityType, _loadCount.get(entityType) + 1);
    }

    public int getLoadCount(Class entityType) {
        return _loadCount.containsKey(entityType) ? _loadCount.get(entityType) : 0;
    }

    public void reset() {
        _loadCount.clear();
    }
}
