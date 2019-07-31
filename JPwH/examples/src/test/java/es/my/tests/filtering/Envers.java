/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.filtering;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.filtering.envers.Item;
import es.my.model.entities.filtering.envers.Usuario;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.testng.annotations.Test;

import static es.my.jph.env.TransactionManagerTest._TM;

/**
 *
 * @author fran
 */
public class Envers extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myFilteringEnversPUnit");}

    /**
     * Tables:
     *   - Bid
     *   - Item
     *   - Item_AUD
     *   - REVINFO
     *   - USUARIOS
     *   - USUARIOS_AUD
     *
     * @throws Throwable
     */
    @Test
    public void test() throws Throwable {
        final UserTransaction tx = _TM.getUserTransaction();
        final Date            TIMESTAMP_CREATE;
        final Date            TIMESTAMP_UPDATE;
        final Date            TIMESTAMP_DELETE;
        final Long            ITEM_ID;
        final Long            USER_ID;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario u = new Usuario("U-00");
                em.persist(u);

                final Item i = new Item("I-00", u);
                em.persist(i);

                Constants.print("Create instances!!");
                // INSERT INTO USUARIOS     (nombre, id)                            VALUES (?,?)
                // INSERT INTO ITEM         (nombre, VENDEDOR_ID, id)               VALUES (?,?,?)
                // INSERT INTO REVINFO      (REV, REVTSTMP)                         VALUES (null,?)
                // INSERT INTO USUARIOS_AUD (REVTYPE, nombre, id, REV)              VALUES (?,?,?,?)
                // INSERT INTO Item_AUD     (REVTYPE, nombre, VENDEDOR_ID, id, REV) VALUES (?,?,?,?,?)
                tx.commit();
                em.close();

                USER_ID = u.getId();
                ITEM_ID = i.getId();
            }
            TIMESTAMP_CREATE = new Date();

            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item i = em.find(Item.class, ITEM_ID);

                i.setNombre("I-XX");
                i.getVendedor().setNombre("U-XX");

                Constants.print("Update instances!!");
                // UPDATE Item     SET nombre=?, VENDEDOR_ID=? WHERE id=?
                // UPDATE USUARIOS SET nombre=?                WHERE id=?
                //
                // INSERT INTO REVINFO      (REV, REVTSTMP)                         VALUES (null,?)
                // INSERT INTO USUARIOS_AUD (REVTYPE, nombre, id, REV)              VALUES (?,?,?,?)
                // INSERT INTO Item_AUD     (REVTYPE, nombre, VENDEDOR_ID, id, REV) VALUES (?,?,?,?,?)
                tx.commit();
                em.close();
            }
            TIMESTAMP_UPDATE = new Date();

            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item i = em.find(Item.class, ITEM_ID);

                em.remove(i);

                Constants.print("Delete instances!!");
                // DELETE Item WHERE id=?
                //
                // INSERT INTO REVINFO  (REV, REVTSTMP)                         VALUES (null,?)
                // INSERT INTO Item_AUD (REVTYPE, nombre, VENDEDOR_ID, id, REV) VALUES (?,?,?,?,?)
                tx.commit();
                em.close();
            }
            TIMESTAMP_DELETE = new Date();

            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final AuditReader auditReader = AuditReaderFactory.get(em);

                // SELECT max(this_.REV) FROM REVINFO this_ WHERE this_.REVTSTMP<=?
                final Number revCreate = auditReader.getRevisionNumberForDate(TIMESTAMP_CREATE);
                // SELECT max(this_.REV) FROM REVINFO this_ WHERE this_.REVTSTMP<=?
                final Number revUpdate = auditReader.getRevisionNumberForDate(TIMESTAMP_UPDATE);
                // SELECT max(this_.REV) FROM REVINFO this_ WHERE this_.REVTSTMP<=?
                final Number revDelete = auditReader.getRevisionNumberForDate(TIMESTAMP_DELETE);

                System.out.println("Create: " + revCreate.toString() + " " + TIMESTAMP_CREATE);
                System.out.println("Update: " + revUpdate.toString() + " " + TIMESTAMP_UPDATE);
                System.out.println("Delete: " + revDelete.toString() + " " + TIMESTAMP_DELETE);

                final List<Number> itemRevisions = auditReader.getRevisions(Item.class, ITEM_ID);
                for (Number n : itemRevisions)
                {
                    // SELECT this_.REVTSTMP FROM REVINFO this_ WHERE this_.REV=?
                    final Date fechaRevision = auditReader.getRevisionDate(n);
                    System.out.println("Fecha de revision (" + n.toString() + "): " + fechaRevision.getTime());
                }

                Constants.print("USUARIOS");
                final List<Number> userRevisions = auditReader.getRevisions(Usuario.class, USER_ID);
                for (Number n : userRevisions) System.out.println("Usuario - numero de revision: " + n.toString());

                // AuditReader.forRevisionsOfEntity()
                em.clear();
                {
                    Constants.print(" 1 ");
                    // Obtiene todas las revisiones de una entidad
                    final AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(Item.class, false, true);

                    final List<Object[]> result = query.getResultList();

                    // Object[]
                    //    - Instancia de la entidad
                    //    - Revision, incluyendo el numero y el timestamp
                    //    - Tipo de revision
                    for (Object[] tupla : result)
                    {
                        final Item                  i = (Item)                  tupla[0];
                        final DefaultRevisionEntity r = (DefaultRevisionEntity) tupla[1];
                        final RevisionType          t = (RevisionType)          tupla[2];

                        System.out.println("Revision " + r.getId() + " es de tipo " + t.name() + ":" + i.toString());
                    }
                }

                // AuditReader.find()
                em.clear();
                {
                    Constants.print(" 2 ");

                    final Item i1 = auditReader.find(Item.class, ITEM_ID, revCreate);
                    System.out.println("Item (" + revCreate +"): " + i1);

                    final Item i2 = auditReader.find(Item.class, ITEM_ID, revUpdate);
                    System.out.println("Item (" + revUpdate +"): " + i2);

                    final Item i3 = auditReader.find(Item.class, ITEM_ID, revDelete);
                    System.out.println("Item (" + revDelete +"): " + i3);

                    final Usuario u = auditReader.find(Usuario.class, USER_ID, revDelete);
                    System.out.println("Usuario (" + revDelete +"): " + u);
                }

                // Arbitrary queries
                em.clear();
                {
                    Constants.print(" 3 ");

                    final AuditQuery q = auditReader.createQuery().forEntitiesAtRevision(Item.class, revUpdate);

                    q.add(AuditEntity.property("nombre").like("I-", MatchMode.START));
                    q.add(AuditEntity.relatedId("vendedor").eq(USER_ID));

                    q.addOrder(AuditEntity.property("nombre").desc());

                    q.setFirstResult(0);
                    q.setMaxResults(10);

                    final Item i = (Item) q.getResultList().get(0);

                    System.out.println("Item: " + i);
                }

                // Projections
                em.clear();
                {
                    Constants.print(" 4 ");

                    final AuditQuery q = auditReader.createQuery().forEntitiesAtRevision(Item.class, revUpdate);

                    q.addProjection(AuditEntity.property("nombre"));

                    final String s = (String) q.getSingleResult();

                    System.out.println("Nombre: " + s);
                }

                // Roll back an entity to an older version
                em.clear();
                {
                    Constants.print(" 5 ");

                    final Usuario u1 = em.find(Usuario.class, USER_ID);
                    System.out.println("u1: " + u1);

                    final Usuario u = auditReader.find(Usuario.class, USER_ID, revCreate);

                    System.out.println("Usuario: " + u);

                    // Envers lo registra como una nueva version de esa instancia de Usuario
                    em.unwrap(Session.class).replicate(u, ReplicationMode.OVERWRITE);
                    em.flush();
                    em.clear();

                    final Usuario u2 = em.find(Usuario.class, USER_ID);
                    System.out.println("u2: " + u2);
                }

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
