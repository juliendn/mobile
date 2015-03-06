package org.fedoraproject.mobile.webapis;

import org.fedoraproject.mobile.datas.badges.Badge;
import org.fedoraproject.mobile.datas.badges.Leaderboard;
import org.fedoraproject.mobile.datas.badges.User;
import org.fedoraproject.mobile.utils.Constants;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 *  Wrapper on https://badges.fedoraproject.org
 *
 * Created by Julien De Nadai on 02/11/2014.
 */
public interface Badges {
    @GET(Constants.BADGES_LEADERBOARD_URL)
    public void getLeaderboard(Callback<Leaderboard> _callback);

    @GET(Constants.BADGES_BY_ID_URL)
    public void getBadge(@Path("id") String id
            , Callback<Badge> _callback);

    @GET(Constants.BADGES_BY_USER_URL)
    public void getUser(@Path("user") String user
            , Callback<User> _callback);
}

