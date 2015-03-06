package org.fedoraproject.mobile.datas.Infra;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.google.gson.annotations.Expose;

import org.fedoraproject.mobile.R;

/**
 * Created by Julien De Nadai on 02/11/2014.
 */
public class Service {
    @Expose
    private String message;
    @Expose
    private String name;
    @Expose
    private Status status;
    @Expose
    private String url;

    public String getName() {
        return name;
    }

    public Spannable getMessage(Context context) {
        SpannableString spannedMessage = new SpannableString(message);
        spannedMessage.setSpan(new ForegroundColorSpan(getColor(context)), 0, spannedMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannedMessage;
    }

    public int getColor(Context context) {
        Resources res = context.getResources();
        switch (status) {
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
