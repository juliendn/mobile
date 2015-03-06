package org.fedoraproject.mobile.webapis;

import org.fedoraproject.mobile.datas.Hrf.HrfResult;
import org.fedoraproject.mobile.utils.Constants;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Wrapper on Hrf.cloud.fedoraproject.org
 * <p/>
 * Created by Julien De Nadai on 01/11/2014.
 */
public interface HrfCloud {

    @GET(Constants.HRF_ALL_URL)
    public void queryLatestNewsfeed(@Query(Constants.HRF_TIMEZONE) String timezone
            , @Query(Constants.HRF_DELTA) int delta
            , @Query(Constants.HRF_ORDER) String order
            , Callback<HrfResult> _callback);

    @GET(Constants.HRF_ALL_URL)
    public void queryNewNewsfeed(@Query(Constants.HRF_TIMEZONE) String timezone
            , @Query(Constants.HRF_START) long start
            , @Query(Constants.HRF_ORDER) String order
            , Callback<HrfResult> _callback);
}
