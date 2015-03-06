package org.fedoraproject.mobile.datas.Hrf;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Newsfeed POJO
 * Created by Julien De Nadai on 01/11/2014.
 */
public class Newsfeed implements Serializable {

    /* Newsfeed sample
    {
      "usernames": [],
      "subtitle": "Repo initialized:  f21-build (arm)",
      "title": "buildsys.repo.init",
      "timestamp": {
        "ago": "19 seconds ago",
        "fulldate": "Saturday, November 01, 2014",
        "epoch": "1414860522.0",
        "iso": "2014-11-01T17:48:42+01:00",
        "time": "17:48",
        "usadate": "11/01/2014"
      },
      "secondary_icon": "https://fedoraproject.org/w/uploads/2/20/Artwork_DesignService_koji-icon-48.png",
      "repr": "buildsys.repo.init -- Repo initialized:  f21-build (arm) http://arm.koji.fedoraproject.org/koji/taginfo?tagID=146",
      "objects": [
        "arm/repos/f21-build"
      ],
      "link": "http://arm.koji.fedoraproject.org/koji/taginfo?tagID=146",
      "packages": [],
      "icon": "https://fedoraproject.org/w/uploads/2/20/Artwork_DesignService_koji-icon-48.png"
    }
     */

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAgo() {
        return timestamp.getAgo();
    }

    public String getImg() {
        return icon;
    }

    public long getEpoch() {
        return timestamp.getEpoch();
    }

    @Expose
    private List<String> usernames;
    @Expose
    private String subtitle;
    @Expose
    private String title;
    @Expose
    private Timestamp timestamp;
    @Expose
    private String secondary_icon;
    @Expose
    private String repr;
    @Expose
    private List<String> objects;
    @Expose
    private String link;
    @Expose
    private List<String> packages;
    @Expose
    private String icon;
}