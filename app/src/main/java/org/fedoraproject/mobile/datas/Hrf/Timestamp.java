package org.fedoraproject.mobile.datas.Hrf;


import com.google.gson.annotations.Expose;

/**
 * Newsfeed timestamp POJO
 * Created by Julien De Nadai on 01/11/2014.
 */
public class Timestamp {
    /*
      "timestamp": {
        "ago": "19 seconds ago",
        "fulldate": "Saturday, November 01, 2014",
        "epoch": "1414860522.0",
        "iso": "2014-11-01T17:48:42+01:00",
        "time": "17:48",
        "usadate": "11/01/2014"
      },
     */

    public String getAgo() {
        return ago;
    }

    public long getEpoch() {
        return epoch;
    }

    @Expose
    private String ago;
    @Expose
    private String fulldate;
    @Expose
    private long epoch;
    @Expose
    private String iso;
    @Expose
    private String time;
    @Expose
    private String usadate;
}
