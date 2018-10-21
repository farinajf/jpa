/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.jph.env;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.internal.util.StringHelper;

/**
 * Creates an EntityManagerFactory.
 *
 * Configuration of the persistence unot is taken from META-INF/persistence.xml
 */
public class JPASetup {

    protected final Map<String, String>  properties = new HashMap<>();
    protected final EntityManagerFactory entityManagerFactory;
    protected final String               persistenceUnitName;

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
     * @param databaseProduct
     * @param persistenceUnitName
     * @param hbmResources
     * @throws Exception
     */
    public JPASetup(DatabaseProduct databaseProduct, String persistenceUnitName, String... hbmResources) throws Exception {
        this.persistenceUnitName = persistenceUnitName;

        //No automatic scanning by Hibernate
        properties.put("hibernate.archive.autodetection", "none");
        properties.put("hibernate.hbmxml.files",          StringHelper.join(",", hbmResources != null ? hbmResources : new String[0]));
        properties.put("hibernate.format_sql",       "true");
        properties.put("hibernate.use_sql_comments", "true");
        properties.put("hibernate.dialect",          databaseProduct.hibernateDialect);

        entityManagerFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName(), properties);
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public EntityManagerFactory getEntityManagerFactory() {return entityManagerFactory;}
    public String               getPersistenceUnitName()  {return persistenceUnitName;}

    public EntityManager createEntityManager() {return getEntityManagerFactory().createEntityManager();}

    public void createSchema() {generateSchema("create");}
    public void dropSchema()   {generateSchema("drop");}

    /**
     *
     * @param action
     */
    public void generateSchema(String action) {
        Map<String,String> createSchemaProperties = new HashMap<>(properties);

        createSchemaProperties.put("javax.persistence.schema-generation.database.action", action);

        Persistence.generateSchema(getPersistenceUnitName(), createSchemaProperties);
    }
}
