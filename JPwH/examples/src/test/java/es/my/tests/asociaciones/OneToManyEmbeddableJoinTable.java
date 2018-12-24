/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.entities.asociaciones.onetomany.embeddablejointable.Direccion;
import es.my.model.entities.asociaciones.onetomany.embeddablejointable.Envio;
import es.my.model.entities.asociaciones.onetomany.embeddablejointable.Usuario;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class OneToManyEmbeddableJoinTable extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myOneToManyEmbeddableJoinTablePUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario   u = new Usuario  ("Juan");
                final Direccion d = new Direccion("Calle 24", "Madrid");

                u.setDireccion(d);
                em.persist(u);

                final Envio e1 = new Envio(new Date());

                d.getEnvios().add(e1);
                em.persist(e1);

                final Envio e2 = new Envio(new Date(2016, 12, 1));
                d.getEnvios().add(e2);
                em.persist(e2);

                System.out.println("------------------- INSERT COMMIT -----------------------");
                //1.- INSERT INTO USUARIO (calle, provincia, nombre, id) VALUES ...
                //2.- INSERT INTO ENVIO (fecha, id) VALUES ...
                //3.- INSERT INTO ENVIO (fecha, id) VALUES ...
                //4.- INSERT DIRECCION_ENVIO (ID_USUARIO, ID_ENVIO) VALUES ...
                //5.- INSERT DIRECCION_ENVIO (ID_USUARIO, ID_ENVIO) VALUES ...
                tx.commit();
                em.close();

                id = u.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                System.out.println("------------------- SELECT -----------------------");
                //6.- SELECT id, calle, provincia, nombre FROM USUARIO WHERE id=?
                final Usuario u = em.find(Usuario.class, id);

                System.out.println("------------------- PRINT -----------------------");
                //7.- SELECT DIRECCION_ENVIO.ID_USUARIO, DIRECCION_ENVIO.ID_ENVIO, Envio.id, Envio.fecha FROM DIRECCION_ENVIO
                //           INNER JOIN Envio ON DIRECCION_ENVIO.ID_ENVIO=Envio.id
                //    WHERE DIRECCION_ENVIO.ID_USUARIO=?
                System.out.println(u);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
