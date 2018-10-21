/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.jph.shared;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author fran
 */
public class ImprovedH2Dialect extends H2Dialect {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public ImprovedH2Dialect() {
        super();
        // Demonstration of function customization in SQL dialect
        registerFunction("lpad", new StandardSQLFunction("lpad", StandardBasicTypes.STRING ) );
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
}
