package org.fedoraproject.mobile.webapis;

import org.fedoraproject.mobile.datas.Infra.InfraStatus;
import org.fedoraproject.mobile.utils.Constants;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Julien De Nadai on 02/11/2014.
 */
public interface Status {
    @GET(Constants.STATUS_STATUSES_URL)
    public void queryInfraStatuses(Callback<InfraStatus> _callback);
}
