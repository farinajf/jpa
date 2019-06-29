/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.fetching.fetchloadgraph;

import es.my.model.Constants;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@NamedEntityGraphs({
    @NamedEntityGraph(
            name = "BidBidderItem",
            attributeNodes = {
                @NamedAttributeNode(value = "bidder"),
                @NamedAttributeNode(value = "item")
            }
    ),
    @NamedEntityGraph(
            name = "BidBidderItemSellerBids",
            attributeNodes = {
                @NamedAttributeNode(value = "bidder"),
                @NamedAttributeNode(
                        value = "item",
                        subgraph = "ItemSellerBids"
                )
            },
            subgraphs = {
                @NamedSubgraph(
                        name = "ItemSellerBids",
                        attributeNodes = {
                            @NamedAttributeNode("vendedor"),
                            @NamedAttributeNode("bids")
                        }
                )
            }
    )
})
@Entity
public class Bid {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario bidder;

    @NotNull
    private BigDecimal cantidad;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Bid() {}

    public Bid(final BigDecimal c) {
        this.cantidad = c;
    }

    public Bid(final Item i, final Usuario u, final BigDecimal c) {
        this.item     = i;
        this.cantidad = c;
        this.bidder   = u;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long       getId()       {return id;}
    public Item       getItem()     {return item;}
    public BigDecimal getCantidad() {return cantidad;}
    public Usuario    getBidder()   {return bidder;}

    public void setItem    (final Item       x) {this.item     = x;}
    public void setCantidad(final BigDecimal x) {this.cantidad = x;}
    public void setBidder  (final Usuario    x) {this.bidder   = x;}

    @Override
    public String toString() {
        return "Bid{" + "id=" + id + ", item=" + item + ", bidder=" + bidder + ", cantidad=" + cantidad + '}';
    }
}
