/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.demo0;

import es.my.model.entities.Mensaje;
import java.util.List;
import javax.transaction.UserTransaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.resource.transaction.backend.jta.internal.JtaTransactionCoordinatorBuilderImpl;
import org.hibernate.service.ServiceRegistry;
import org.jpwh.env.TransactionManagerTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class DemoHibernate extends TransactionManagerTest {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/
    /**
     *
     */
    private void _unusedSimpleBoot() {
        SessionFactory sf = new MetadataSources(new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build()).buildMetadata().buildSessionFactory();
    }

    /**
     * Crea una instancia de una factoria de sesion.
     *
     * @return
     */
    private SessionFactory _getSessionFactory() {
        final StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();

        //Configura el servicio de registro
        ssrb.applySetting("hibernate.connection.datasource", "myDS")
                .applySetting("hibernate.format_sql",        "true")
                .applySetting("hibernate.use_sql_comments",  "true")
                .applySetting("hibernate.hbm2ddl.auto",      "create-drop");

        //JTA
        ssrb.applySetting(Environment.TRANSACTION_COORDINATOR_STRATEGY, JtaTransactionCoordinatorBuilderImpl.class);

        final ServiceRegistry sr = ssrb.build();

        final MetadataSources ms = new MetadataSources(sr);

        ms.addAnnotatedClass(es.my.model.entities.Mensaje.class);

        //Anhadir ficheros hbm.xml
        //ms.addFile(...);

        //Anhadir ficheros hbm.xml incluidos en un JAR
        //ms.addJar(...);

        final MetadataBuilder mb = ms.getMetadataBuilder();
        final Metadata        m  = mb.build();

        Assert.assertEquals(m.getEntityBindings().size(), 1);

        return m.buildSessionFactory();
    }

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
     * Inserta, lee y actualiza un mensaje en base de datos.
     *
     * @throws Exception
     */
    @Test
    public void demo1() throws Exception {
        SessionFactory sf = _getSessionFactory();

        try
        {
            {
                final UserTransaction tx = _TM.getUserTransaction();
                tx.begin();

                System.out.println("......................... BEGIN .........................");

                // Todas las llamadas a 'getCurrentSession' desde el mismo thread devuelven la misma sesion de hibernate.
                // La sesion esta asociada a la transaccion y se cierra automaticamente cuando se hace un commit o rollback de la Tx
                final Session s = sf.getCurrentSession();

                final Mensaje mensaje = new Mensaje();
                mensaje.setMensaje("Mensaje que se persiste desde Hibernate!!");

                s.persist(mensaje);

                // Cuando se ejecuta el commit hibernate cierra la sesion de forma automatica
                tx.commit();
            }
            {
                final UserTransaction tx = _TM.getUserTransaction();
                tx.begin();

                List<Mensaje> result = sf.getCurrentSession().createCriteria(Mensaje.class).list();

                Assert.assertEquals(result.size(), 1);
                Assert.assertEquals(result.get(0).getMensaje(), "Mensaje que se persiste desde Hibernate!!");

                // Hibernate detecta un cambio en un objeto que esta en el contexto de persistencia.
                result.get(0).setMensaje("Mensaje modificado!!");

                tx.commit();
            }
        }
        finally
        {
            _TM.rollback();
        }
    }
}
