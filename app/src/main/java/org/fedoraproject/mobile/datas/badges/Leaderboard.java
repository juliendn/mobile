package org.fedoraproject.mobile.datas.badges;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Leaderboard POJO
 * Created by Julien De Nadai on 02/11/2014.
 */
public class Leaderboard {
    @Expose
    private List<BadgeOwner> leaderboard;

    public List<BadgeOwner> getBadgeOwners()
    {
        return leaderboard;
    }
}
