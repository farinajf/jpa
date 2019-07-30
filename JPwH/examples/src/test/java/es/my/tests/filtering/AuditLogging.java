/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.filtering;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.filtering.interceptor.AuditLogRecord;
import es.my.model.entities.filtering.interceptor.Item;
import es.my.model.entities.filtering.interceptor.Usuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class AuditLogging extends JPATest {

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
    @Override
    public void configurePU() throws Exception {super.configurePU("myFilteringInterceptorPUnit");}

    /**
     *
     * @throws Throwable
     */
    @Test
    public void test() throws Throwable {
        final UserTransaction tx = _TM.getUserTransaction();

        try
        {
            final Long USER_ID;

            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario u = new Usuario("U-1");
                em.persist(u);

                tx.commit();
                em.close();

                USER_ID = u.getId();
            }

            final EntityManagerFactory emf = _JPA.getEntityManagerFactory();

            final Map<String, String> p = new HashMap<>();
            p.put(org.hibernate.jpa.AvailableSettings.SESSION_INTERCEPTOR, AuditLogInterceptor.class.getName());

            final EntityManager em = emf.createEntityManager(p);
            final Session       s  = em.unwrap(Session.class);

            final AuditLogInterceptor interceptor = (AuditLogInterceptor) ((SessionImplementor) s).getInterceptor();

            interceptor.setCurrentSession(s);
            interceptor.setCurrentUserId (USER_ID);

            // TX-1
            tx.begin();

            em.joinTransaction();

            final Item i = new Item("I1");

            Constants.print("TX-1 persist(Item)");
            em.persist(i);

            //AuditLogInterceptor.onSave()
            //AuditLogInterceptor.postFlush()

            tx.commit();
            em.clear();

            // TX-2
            tx.begin();

            em.joinTransaction();

            Constants.print("TX-2 load(AuditLogRecord)");

            final List<AuditLogRecord> logs = em.createQuery("SELECT a FROM AuditLogRecord a", AuditLogRecord.class).getResultList();

            for (AuditLogRecord a : logs) System.out.println(a.toString());

            //AuditLogInterceptor.postFlush()
            tx.commit();
            em.clear();

            // TX-3
            tx.begin();

            em.joinTransaction();

            Constants.print("TX-3 update(Item)");

            final Item i2 = em.find(Item.class, i.getId());

            i2.setNombre("IXX");

            //AuditLogInterceptor.onFlushDirty()
            //AuditLogInterceptor.postFlush()

            tx.commit();
            em.clear();

            // TX-4
            tx.begin();

            em.joinTransaction();

            Constants.print("TX-4 load(AuditLogRecord)");

            List<AuditLogRecord> logs2 = em.createQuery("SELECT a FROM AuditLogRecord a", AuditLogRecord.class).getResultList();

            for (AuditLogRecord a : logs2) System.out.println(a.toString());

            //AuditLogInterceptor.postFlush()
            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
