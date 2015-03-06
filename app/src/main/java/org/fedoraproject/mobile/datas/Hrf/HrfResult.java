package org.fedoraproject.mobile.datas.Hrf;

import com.google.gson.annotations.Expose;

import java.util.List;


/**
 * Hrf result POJO
 * Created by Julien De Nadai on 01/11/2014.
 */
public class HrfResult {
    @Expose
    private List<Newsfeed> results;

    public List<Newsfeed> getResults() {
        return results;
    }
}
