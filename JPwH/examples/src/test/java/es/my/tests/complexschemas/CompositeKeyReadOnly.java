/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.complexschemas;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.complexschemas.compositekey.readonly.Empresa;
import es.my.model.entities.complexschemas.compositekey.readonly.Usuario;
import es.my.model.entities.complexschemas.compositekey.readonly.UsuarioId;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class CompositeKeyReadOnly extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myCompositeKeyReadOnlyPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            empresaId;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Empresa e = new Empresa("e-1");
                em.persist(e);

                final UsuarioId id = new UsuarioId("x-1", e.getId());
                final Usuario   u  = new Usuario(id);

                em.persist(u);

                tx.commit();
                em.close();

                empresaId = u.getId().getEmpresaId();

                Constants.print(u);
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final UsuarioId id = new UsuarioId("x-1", empresaId);
                final Usuario   u  = em.find(Usuario.class, id);

                tx.commit();
                em.close();

                Constants.print(u);
            }
        }
        finally {_TM.rollback();}
    }
}
