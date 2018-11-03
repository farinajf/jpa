@org.hibernate.annotations.TypeDefs({
    @org.hibernate.annotations.TypeDef(
            name = "valor_monetario_usd",
            typeClass = ValorMonetarioUserType.class,
            parameters = {@Parameter(name = "convertTo", value = "USD")}
    ),
    @org.hibernate.annotations.TypeDef(
            name = "valor_monetario_eur",
            typeClass = ValorMonetarioUserType.class,
            parameters = {@Parameter(name = "convertTo", value = "EUR")}
    )
})

package es.my.converters;

import org.hibernate.annotations.Parameter;
