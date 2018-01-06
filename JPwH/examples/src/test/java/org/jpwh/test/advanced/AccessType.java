/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jpwh.test.advanced;

import org.jpwh.env.JPATest;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class AccessType extends JPATest {

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
    public void configurePersistenceUnit() throws Exception {configurePersistenceUnit("AdvancedPU");}

    @Test
    public void storeLoadAccessType() throws Exception {

    }
}
