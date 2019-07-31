@org.hibernate.annotations.FilterDefs({
    @org.hibernate.annotations.FilterDef(
            name = "limitByUserRank",
            parameters = {
                @org.hibernate.annotations.ParamDef(
                        name = "rangoUsuarioActual",
                        type = "int"
                )
            }
    ),
    @org.hibernate.annotations.FilterDef(
            name = "limitByUserRankDefault",
            defaultCondition = ":currentUserRank >= (SELECT u.RANGO FROM Usuarios u WHERE u.ID = VENDEDOR_ID)",
            parameters = {
                @org.hibernate.annotations.ParamDef(
                        name = "currentUserRank",
                        type = "int"
                )
            }
    )
})
package es.my.model.entities.filtering.dynamic;
