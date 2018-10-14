/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.demo0;

import es.my.model.entities.Mensaje;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import org.jpwh.env.TransactionManagerTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see http://jpwh.org/examples/jpwh2/jpwh-2e-examples-20151103/
 *
 * @author fran
 */
public class DemoJPA extends TransactionManagerTest {

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
     * Inserta, lee y actualiza un mensaje en base de datos.
     * 
     * @throws Exception
     */
    @Test
    public void demo1() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DemoPUnit");

        try
        {
            {
                //1.- Transaccion
                UserTransaction tx = _TM.getUserTransaction();

                //2.- Crea una sesion con la base de datos
                EntityManager em = emf.createEntityManager();

                //3.- Inicio de la transaccion
                tx.begin();

                //3.-
                Mensaje x = new Mensaje();
                x.setMensaje("Mi primer mensaje persistente!!");

                //4.- Le desimos a hibernate que debe persistir este objeto
                em.persist(x);

                //5.- Se cierra la transaccion. Hibernate sincroniza el contexto de persistencia con base de datos
                tx.commit();
                em.close();
            }
            {
                UserTransaction tx = _TM.getUserTransaction();
                EntityManager   em = emf.createEntityManager();

                tx.begin();

                List<Mensaje> result = em.createQuery("select m from Mensaje m").getResultList();

                Assert.assertEquals(result.size(), 1);
                Assert.assertEquals(result.get(0).getMensaje(), "Mi primer mensaje persistente!!");

                // Hibernate detecta un cambio en un objeto que esta en el contexto de persistencia.
                result.get(0).setMensaje("Mensaje modificado!!");

                // Hibernate comprueba modificaciones en las entidades y ejecuta los UPDATE's correspondientes
                tx.commit();

                em.close();
            }
        }
        finally
        {
            _TM.rollback();
            emf.close();
        }
    }
}
