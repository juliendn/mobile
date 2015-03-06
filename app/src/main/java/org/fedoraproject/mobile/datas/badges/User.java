package org.fedoraproject.mobile.datas.badges;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Julien De Nadai on 03/11/2014.
 */
public class User {

    public List<Badge> getBadges() {
        return assertions;
    }

    public String getName() {
        return user;
    }

    public float getRatio() {
        return percent_earned;
    }

    @Expose
    private float percent_earned;
    @Expose
    private List<Badge> assertions;
    @Expose
    private String user;
    @Expose
    private String avatar;
}
