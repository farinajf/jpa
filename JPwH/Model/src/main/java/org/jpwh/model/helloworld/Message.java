/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jpwh.model.helloworld;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author fran
 */
@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long   id;
    private String text;
    
    public String getText() {return text;}
    
    public void setText(final String x) {this.text = x;}
}
