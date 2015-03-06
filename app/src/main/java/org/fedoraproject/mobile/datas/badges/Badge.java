package org.fedoraproject.mobile.datas.badges;

import com.google.gson.annotations.Expose;

/**
 * Created by Julien De Nadai on 03/11/2014.
 */
public class Badge {
    /*
    "first_awarded":1382755079.0,
    "first_awarded_person":"ralph",
    "last_awarded_person":"spaz",
    "name":"Baby Badger",
    "percent_earned":7.2010534112418725,
    "last_awarded":1414967700.0,
    "image":"https://badges.fedoraproject.org/pngs/baby-badger.png",
    "id":"baby-badger",
    "times_awarded":875,
    "description":"You have logged in to the Fedora Badges app"
    */

    @Expose
    private long first_awarded;
    @Expose
    private String first_awarded_person;
    @Expose
    private String last_awarded_person;
    @Expose
    private String name;
    @Expose
    private float percent_earned;
    @Expose
    private long last_awarded;
    @Expose
    private String image;
    @Expose
    private String id;
    @Expose
    private int times_awarded;
    @Expose
    private String description;

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
