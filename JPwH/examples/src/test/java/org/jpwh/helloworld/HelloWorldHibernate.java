/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jpwh.helloworld;

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
import org.jpwh.model.helloworld.Message;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class HelloWorldHibernate extends TransactionManagerTest {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/
    /**
     *
     */
    protected void _unusedSimpleBoot() {
        SessionFactory sessionFactory = new MetadataSources(new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build()).buildMetadata().buildSessionFactory();
    }

    /**
     *
     * @return
     */
    protected SessionFactory _createSessionFactory() {
        //This builder helps you create the immutable service registry with chained method calls
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();

        //Configure the services registry by applying settings
        serviceRegistryBuilder.applySetting("hibernate.connection.datasource", "myDS")
                .applySetting("hibernate.format_sql", "true")
                .applySetting("hibernate.use_sql_comments", "true")
                .applySetting("hibernate.hbm2ddl.auto", "create-drop");

        //Enable JTA
        serviceRegistryBuilder.applySetting(Environment.TRANSACTION_COORDINATOR_STRATEGY, JtaTransactionCoordinatorBuilderImpl.class);

        ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();

        //You can only enter this configuration stage with an existing service registry
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);

        //Add your persistent classes to the (mapping) metadata sources
        metadataSources.addAnnotatedClass(org.jpwh.model.helloworld.Message.class);

        //Add hbm.xml mapping files
        //metadataSources.addFile(...);

        //Read all hbm.xml mapping files from a JAR
        //metadataSources.addJar(...)

        MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();

        Metadata metadata = metadataBuilder.build();

        Assert.assertEquals(metadata.getEntityBindings().size(), 1);

        return metadata.buildSessionFactory();
    }

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    @Test
    public void storeLoadMessage() throws Exception {
        SessionFactory sessionFactory = _createSessionFactory();

        try
        {
            {
                // Get access to the standard transaction API UserTransaction and begin a transaction on this thread of execution
                UserTransaction tx = _TM.getUserTransaction();
                tx.begin();

                //Whenever you call getCurrentSession() in the same thread you get the same org.hibernate.Session.
                //It's bound automatically to the ongoing transaction and is closed for you automatically when that transaction commits or rolls back.
                Session session = sessionFactory.getCurrentSession();

                Message message = new Message();
                message.setText("Hello World!!");

                //The native Hibernate API is very similar to the standard Jave Persistence API and most methods have the same name
                session.persist(message);

                //Hibernate synchronizes the session with the database and closes the "current" session on commit of the bound transaction automatically.
                tx.commit();
            }
            {
                UserTransaction tx = _TM.getUserTransaction();
                tx.begin();

                //A hibernate criteria query is a type-safe programmatic way to express queries
                //SELECT * FROM MESSAGE
                List<Message> messages = sessionFactory.getCurrentSession().createCriteria(Message.class).list();

                Assert.assertEquals(messages.size(), 1);
                Assert.assertEquals(messages.get(0).getText(), "Hello World!!");

                tx.commit();
            }
        }
        finally
        {
            _TM.rollback();
        }
    }
}
