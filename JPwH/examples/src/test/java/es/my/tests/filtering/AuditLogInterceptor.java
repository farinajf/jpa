/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.filtering;

import es.my.model.entities.filtering.interceptor.AuditLogRecord;
import es.my.model.entities.filtering.interceptor.Auditable;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;

/**
 *
 * @author fran
 */
public class AuditLogInterceptor extends EmptyInterceptor {

    private Session        _session;
    private Long           _userId;
    private Set<Auditable> _inserts = new HashSet<>();
    private Set<Auditable> _updates = new HashSet<>();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public AuditLogInterceptor() {
        System.out.println("AuditLogInterceptor()");
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public void setCurrentSession(final Session x) {this._session = x;}
    public void setCurrentUserId (final Long    x) {this._userId  = x;}

    /**
     * Este metodo se invoca cuando una entidad se hace persistente.
     *
     * @param entity
     * @param id
     * @param state
     * @param propertyNames
     * @param types
     * @return
     * @throws CallbackException
     */
    @Override
    public boolean onSave(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) throws CallbackException {
        System.out.println("AuditLogInterceptor.onSave()");

        if (entity instanceof Auditable) _inserts.add((Auditable) entity);

        //No se ha modificado el estado
        return false;
    }

    /**
     * Este metodo se llama cuando se detecta una entidad 'dirty' durante un flush del contexto de persistencia.
     *
     * @param entity
     * @param id
     * @param currentState
     * @param previousState
     * @param propertyNames
     * @param types
     * @return
     * @throws CallbackException
     */
    @Override
    public boolean onFlushDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) throws CallbackException {
        System.out.println("AuditLogInterceptor.onFlushDirty()");

        if (entity instanceof Auditable) _updates.add((Auditable) entity);

        return false;
    }

    /**
     * Este metodo se llama despues de que se complete el flush del contexto de
     * persistencia.
     *
     * @param iterator
     * @throws CallbackException
     */
    @Override
    public void postFlush(final Iterator iterator) throws CallbackException {
        System.out.println("AuditLogInterceptor.postFlush()");

        final Session s = _session.sessionWithOptions().transactionContext().connection().openSession();

        try
        {
            for (Auditable a : _inserts) s.persist(new AuditLogRecord("insert", a, _userId));
            for (Auditable a : _updates) s.persist(new AuditLogRecord("update", a, _userId));

            // Se cierra esta sesion independientemente de la Session original.
            s.flush();
        }
        finally
        {
            s.close();
            _inserts.clear();
            _updates.clear();
        }
    }
}
