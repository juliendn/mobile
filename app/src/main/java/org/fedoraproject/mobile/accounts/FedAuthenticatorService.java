package org.fedoraproject.mobile.accounts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Julien De Nadai on 03/11/2014.
 */
public class FedAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        FedAuthenticator authenticator = new FedAuthenticator(getBaseContext());
        return authenticator.getIBinder();
    }
}
