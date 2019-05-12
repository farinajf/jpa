@org.hibernate.annotations.FetchProfiles({
    /*
        Cada profile tiene un nombre
    */
    @FetchProfile(name = Item.PROFILE_JOIN_SELLER, fetchOverrides = @FetchProfile.FetchOverride(
        entity = Item.class,
        association = "vendedor",
        mode = FetchMode.JOIN
    )),

    @FetchProfile(name = Item.PROFILE_JOIN_BIDS, fetchOverrides = @FetchProfile.FetchOverride(
        entity = Item.class,
        association = "bids",
        mode = FetchMode.JOIN
    ))
})

package es.my.model.entities.fetching.profile;

import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchMode;
