package org.fedoraproject.mobile.datas.badges;

import android.content.Context;

import com.google.gson.annotations.Expose;

import org.fedoraproject.mobile.R;

/**
 * POJO for leaderboard
 * Created by Julien De Nadai on 02/11/2014.
 */
public class BadgeOwner {

    public String getName() {
        return nickname;
    }

    public String getBadges(Context context) {
        return context.getString(R.string.leaderboard_badges, badges);
    }

    public String getRank() {
        return "#" + rank;
    }

    @Expose
    private String nickname;
    @Expose
    private int badges;
    @Expose
    private int rank;
}
