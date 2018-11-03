/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.converters;

import es.my.model.entities.advanced.ValorMonetario;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.DynamicParameterizedType;

/**
 *
 * @author fran
 */
public class ValorMonetarioUserType implements CompositeUserType, DynamicParameterizedType {

    private java.util.Currency convertTo;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/
    /**
     * Rutina de conversion.
     *
     * @param x
     * @param monedaDB
     * @return
     */
    private ValorMonetario _convert(ValorMonetario x, java.util.Currency monedaDB) {
        return new ValorMonetario(x.getValor().multiply(new BigDecimal(2)), monedaDB);
    }

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
    public Class returnedClass() {return ValorMonetario.class;}

    @Override
    public boolean isMutable() {return false;}

    @Override
    public Object deepCopy(Object value) throws HibernateException {return value;}

    /**
     * Hibernate usa este metodo para almacenar un objeto en la cache de segundo nivel.
     * @param value
     * @param session
     * @return
     * @throws HibernateException
     */
    @Override
    public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {return value.toString();}

    /**
     * Hibernate lo usa cuando lee la representacion serializada de un objeto en la cache de segundo nivel.
     * @param cached
     * @param session
     * @param owner
     * @return
     * @throws HibernateException
     */
    @Override
    public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {return ValorMonetario.fromString((String) cached);}

    @Override
    public Object replace(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {return original;}

    /**
     * Se llama para leer el ResultSet cuando un ValorMonetario es extraido de la base de datos.
     *
     * @param rs
     * @param names
     * @param session
     * @param owner
     * @return
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        System.out.println("ValorMonetarioUserType.nullSafeGet(" + names + ")");

        final BigDecimal x = rs.getBigDecimal(names[0]);

        if (rs.wasNull() == true) return null;

        final java.util.Currency y = java.util.Currency.getInstance(rs.getString(names[1]));

        final ValorMonetario result = new ValorMonetario(x, y);

        System.out.println("result: " + result);

        return result;
    }

    /**
     * Se llama cuando el valor monetario debe ser almacenado en la base de datos.
     *
     * @param st
     * @param value
     * @param index
     * @param session
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        System.out.println("ValorMonetarioUserType.nullSafeSet(" + String.valueOf(value) + ")");

        if (value == null)
        {
            st.setNull(index,   StandardBasicTypes.BIG_DECIMAL.sqlType());
            st.setNull(index+1, StandardBasicTypes.CURRENCY.sqlType());
            return;
        }

        final ValorMonetario valor   = (ValorMonetario) value;
        final ValorMonetario dbValor = _convert(valor, convertTo);

        st.setBigDecimal(index,   dbValor.getValor());
        st.setString    (index+1, convertTo.getCurrencyCode());
    }

    @Override
    public String[] getPropertyNames() {return new String[]{"valor", "moneda"};}

    @Override
    public Type[] getPropertyTypes() {return new Type[]{StandardBasicTypes.BIG_DECIMAL, StandardBasicTypes.CURRENCY};}

    @Override
    public Object getPropertyValue(Object component, int property) throws HibernateException {
        final ValorMonetario x = (ValorMonetario) component;

        return (property == 0) ? x.getValor() : x.getMoneda();
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {throw new UnsupportedOperationException("ValorMonetario es inmutable!!");}

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {return x == y || !(x == null || y == null) && x.equals(y);}

    @Override
    public int hashCode(Object x) throws HibernateException {return x.hashCode();}

    @Override
    public void setParameterValues(Properties parameters) {
        System.out.println("ValorMonetarioUserType.setParameterValues()");

        final ParameterType parameterType = (ParameterType) parameters.get(PARAMETER_TYPE);
        final String[]      columnas      = parameterType.getColumns();
        final String        tabla         = parameterType.getTable();
        final Annotation[]  anotaciones   = parameterType.getAnnotationsMethod();

        final String convertToParameter = parameters.getProperty("convertTo");

        this.convertTo = java.util.Currency.getInstance(convertToParameter != null ? convertToParameter : "USD");
    }
}
