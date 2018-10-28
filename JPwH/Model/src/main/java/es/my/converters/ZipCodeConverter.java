/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.converters;

import es.my.model.entities.advanced.converter.ItalyZipCode;
import es.my.model.entities.advanced.converter.SpainZipCode;
import es.my.model.entities.advanced.converter.ZipCode;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author fran
 */
@Converter
public class ZipCodeConverter implements AttributeConverter<ZipCode, String> {

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
    public String convertToDatabaseColumn(final ZipCode x) {
        return x.getCodigo();
    }

    @Override
    public ZipCode convertToEntityAttribute(final String x) {
        return (x.length() < 5) ? new ItalyZipCode(x) : new SpainZipCode(x);
    }
}
