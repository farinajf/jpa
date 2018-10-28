/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.converters;

import es.my.model.entities.advanced.ValorMonetario;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author fran
 */
@Converter(autoApply = true)
public class ValorMonetarioConverter implements AttributeConverter<ValorMonetario, String> {

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
     *
     * @param x
     * @return
     */
    @Override
    public String convertToDatabaseColumn(final ValorMonetario x) {
        System.out.println("ValorMonetarioConverter.convertToDatabaseColumn(" + x + ")");
        return x.toString();
    }

    /**
     *
     * @param x
     * @return
     */
    @Override
    public ValorMonetario convertToEntityAttribute(final String x) {
        System.out.println("ValorMonetarioConverter.convertToEntityAttribute(" + x + ")");
        return ValorMonetario.fromString(x);
    }
}
