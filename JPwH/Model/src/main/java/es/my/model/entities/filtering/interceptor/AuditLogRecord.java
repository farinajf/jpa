/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.filtering.interceptor;

import es.my.model.Constants;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class AuditLogRecord {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String mensaje;

    @NotNull
    private Long entityId;

    @NotNull
    private Class entityClass;

    @NotNull
    private Long userId;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion = new Date();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public AuditLogRecord() {}

    public AuditLogRecord(final String m, final Auditable e, final Long u) {
        this.mensaje     = m;
        this.entityId    = e.getId();
        this.entityClass = e.getClass();
        this.userId      = u;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long   getId()            {return id;}
    public String getMensaje()       {return mensaje;}
    public Long   getEntityId()      {return entityId;}
    public Class  getEntityClass()   {return entityClass;}
    public Long   getUserId()        {return userId;}
    public Date   getFechaCreacion() {return fechaCreacion;}

    public void setMensaje      (final String x) {this.mensaje       = x;}
    public void setEntityId     (final Long   x) {this.entityId      = x;}
    public void setEntityClass  (final Class  x) {this.entityClass   = x;}
    public void setUserId       (final Long   x) {this.userId        = x;}
    public void setFechaCreacion(final Date   x) {this.fechaCreacion = x;}

    @Override
    public String toString() {
        return "AuditLogRecord{" + "id=" + id + ", mensaje=" + mensaje + ", entityId=" + entityId + ", entityClass=" + entityClass + ", userId=" + userId + ", fechaCreacion=" + fechaCreacion + '}';
    }
}
