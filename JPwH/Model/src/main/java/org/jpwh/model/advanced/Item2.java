/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jpwh.model.advanced;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import es.my.model.Constants;

/**
 *
 */
@Entity
public class Item2 {
    private static final String _AUCTION = "AUCTION: ";

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    protected long id;

    @NotNull
    protected String description;

    protected BigDecimal initialPrice;

    protected String name;

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
    public Long       getId()           {return id;}
    public String     getDescription()  {return description;}
    public BigDecimal getInitialPrice() {return initialPrice;}
    public String     getName()         {return name;}

    public void setDescription(String x) {description = x;}
    public void setName       (String x) {name        = x.startsWith(_AUCTION) == false ? _AUCTION + x : x;}
}
