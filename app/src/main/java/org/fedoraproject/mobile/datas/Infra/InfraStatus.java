package org.fedoraproject.mobile.datas.Infra;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.annotations.Expose;

import org.fedoraproject.mobile.R;

import java.util.List;

/**
 * Created by Julien De Nadai on 02/11/2014.
 */
public class InfraStatus {

    @Expose
    private String global_info;
    @Expose
    private Status global_status;
    @Expose
    private String global_verbose_status;
    @Expose
    private ServiceMap services;

    public List<Service> getServices() {
        return services.getServiceList();
    }

    public String getStatus() {
        return global_verbose_status;
    }


    public int getColor(Context context) {
        Resources res = context.getResources();
        switch (global_status) {
            case good:
                return res.getColor(R.color.status_good);
            case minor:
            case scheduled:
                return res.getColor(R.color.status_minor);
            case major:
            default:
                return res.getColor(R.color.status_major);
        }
    }

    private enum Status {
        good, minor, major, scheduled
    }
}
