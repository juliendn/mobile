package org.fedoraproject.mobile.utils;

/**
 * Utils class to maintains constants between classes
 *
 * Created by Julien De Nadai on 01/11/2014.
 */
public class Constants {
    public static final String TAG = "FedoraMobile";

    // Fragments
    public final static int FRAGMENT_MY_ACCOUNT = 0;
    public final static int FRAGMENT_FEDORA_PEOPLE = 1;
    public final static int FRAGMENT_PACKAGES_UPDATES = 2;
    public final static int FRAGMENT_NEWSFEED = 3;
    public final static int FRAGMENT_INFRA_STATUS = 4;
    public final static int FRAGMENT_PREFERENCES = 5;
    public final static int FRAGMENT_BADGES = 6;

    // webapi
    public static final String USER_AGENT = "User-Agent: FedoraMobile/1.0";

    public static final String APPS_FEDORAPROJECT_URL = "https://apps.fedoraproject.org";
    public static final String DATAGREPPER_URL = "/datagrepper/raw/?grouped=true";
    public static final String DATAGREPPER_COUNT_URL = "/datagrepper/messagecount";

    public static final String HRF_CLOUD_FEDORAPROJECT_URL = "http://hrf.cloud.fedoraproject.org";
    public static final String HRF_ALL_URL = "/all";
    public static final String HRF_TIMEZONE = "timezone";
    public static final String HRF_DELTA = "delta";
    public static final String HRF_ORDER = "order";
    public static final String HRF_START = "start";

    public static final String STATUS_FEDORAPROJECT_URL = "http://status.fedoraproject.org";
    public static final String STATUS_STATUSES_URL = "/statuses.json";

    public static final String BADGES_FEDORAPROJECT_URL = "https://badges.fedoraproject.org";
    public static final String BADGES_BY_ID_URL = "/badge/{id}/json";
    public static final String BADGES_BY_USER_URL = "/user/{user}/json";
    public static final String BADGES_LEADERBOARD_URL = "/leaderboard/json";

    public static final String ID_FEDORAPROJECT_URL = "https://id.fedoraproject.org";
    public static final String ID_GET_AUTHENTICATE = "/authenticate";
    public static final String ID_POST_AUTHENTICATE = "/openid/"
            + "?openid.ns.sreg=http://openid.net/extensions/sreg/1.1"
            +"&openid.ax.if_available=ext0,ext1,ext2,ext3,ext4,ext5,ext6,ext7,ext8,ext9,ext10,ext11,ext12,ext13,ext14,ext15"
            +"&openid.realm=https://mobile.fedoraproject.org"
            +"&openid.ax.type.ext14=http://axschema.org/pref/language"
            +"&openid.ax.type.ext15=http://axschema.org/namePerson/last"
            +"&openid.ax.type.ext12=http://axschema.org/namePerson/friendlye"
            +"&openid.ax.type.ext13=http://axschema.org/contact/postalCode/homee"
            +"&openid.ax.type.ext10=http://axschema.org/pref/timezone"
            +"&openid.ax.type.ext11=http://axschema.org/namePerson/suffix"
            +"&openid.ax.type.ext8=http://axschema.org/birthDate"
            +"&openid.ax.type.ext9=http://axschema.org/contact/email"
            +"&openid.ax.type.ext4=http://axschema.org/person/gender"
            +"&openid.ax.type.ext5=http://axschema.org/namePerson/middle"
            +"&openid.ax.type.ext6=http://axschema.org/media/image/default"
            +"&openid.ax.type.ext7=http://axschema.org/namePerson/first"
            +"&openid.ax.type.ext0=http://axschema.org/namePerson/prefix"
            +"&openid.ax.type.ext1=http://axschema.org/contact/web/default"
            +"&openid.ax.type.ext2=http://axschema.org/contact/country/home"
            +"&openid.ax.type.ext3=http://axschema.org/namePerson"
            +"&openid.ax.mode=fetch_request"
            +"&openid.sreg.optional=nickname,email,fullname,dob,gender,postcode,country,language,timezone"
            +"&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select"
            +"&openid.ns.ax=http://openid.net/srv/ax/1.0"
            +"&openid.mode=checkid_setup"
            +"&openid.identity=http://specs.openid.net/auth/2.0/identifier_select"
            +"&openid.ns=http://specs.openid.net/auth/2.0";

    // Accounts
    public static final String ACCOUNT_TYPE = "org.fedoraproject.mobile.auth";
    public static final String ACCOUNT_IMG_URL = "account_img_url";
    public static final String AUTH_TOKEN_TYPE = "Full access";

    // Extras
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String ARG_USER = "user";

    // Preferences
    public static final String PREF_LAST_ACCOUNT_USED = "last_account_used";
}
