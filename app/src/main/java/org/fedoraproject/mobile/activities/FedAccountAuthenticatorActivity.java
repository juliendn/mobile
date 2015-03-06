package org.fedoraproject.mobile.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.fedoraproject.mobile.BuildConfig;
import org.fedoraproject.mobile.R;
import org.fedoraproject.mobile.utils.Constants;
import org.fedoraproject.mobile.utils.LogUtil;

/**
 * Activity that handle user credential inputs
 * <p/>
 * Created by Julien De Nadai on 03/11/2014.
 */
public class FedAccountAuthenticatorActivity extends AccountAuthenticatorCompatActivity {

    private WebView mView;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_fedoath);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = (ProgressBar) findViewById(R.id.account_progress);

        mView = (WebView) findViewById(R.id.account_webview);
        mView.setVerticalScrollBarEnabled(false);
        mView.setHorizontalScrollBarEnabled(false);
        mView.setWebViewClient(new InternalWebViewClient());
        mView.setWebChromeClient(new InternalWebChromeClient());
//        mView.getSettings().setJavaScriptEnabled(true);
        mView.setInitialScale(200);

        login();
    }

    public void login() {

        mView.loadUrl("https://id.fedoraproject.org/openid/?openid.ns.sreg=http://openid.net/extensions/sreg/1.1&openid.ax.if_available=ext0,ext1,ext2,ext3,ext4,ext5,ext6,ext7,ext8,ext9,ext10,ext11,ext12,ext13,ext14,ext15&openid.ax.type.ext14=http://axschema.org/pref/language&openid.ax.type.ext15=http://axschema.org/namePerson/last&openid.ax.type.ext12=http://axschema.org/namePerson/friendlye&openid.ax.type.ext13=http://axschema.org/contact/postalCode/homee&openid.ax.type.ext10=http://axschema.org/pref/timezone&openid.ax.type.ext11=http://axschema.org/namePerson/suffix&openid.ax.type.ext8=http://axschema.org/birthDate&openid.ax.type.ext9=http://axschema.org/contact/email&openid.ax.type.ext4=http://axschema.org/person/gender&openid.ax.type.ext5=http://axschema.org/namePerson/middle&openid.ax.type.ext6=http://axschema.org/media/image/default&openid.ax.type.ext7=http://axschema.org/namePerson/first&openid.ax.type.ext0=http://axschema.org/namePerson/prefix&openid.ax.type.ext1=http://axschema.org/contact/web/default&openid.ax.type.ext2=http://axschema.org/contact/country/home&openid.ax.type.ext3=http://axschema.org/namePerson&openid.ax.mode=fetch_request&openid.sreg.optional=nickname,email,fullname,dob,gender,postcode,country,language,timezone&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&openid.ns.ax=http://openid.net/srv/ax/1.0&openid.mode=checkid_setup&openid.identity=http://specs.openid.net/auth/2.0/identifier_select&openid.ns=http://specs.openid.net/auth/2.0&openid.return_to=http://mobile.fedoraproject.org&openid.realm=http://mobile.fedoraproject.org");
    }

    private class InternalWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url != null && url.startsWith("http://mobile.fedoraproject.org/?openid.assoc_handle")) {
                Uri uri = Uri.parse(url);
                String nickname = uri.getQueryParameter("openid.sreg.nickname");
                String email = uri.getQueryParameter("openid.sreg.email");
                String name = uri.getQueryParameter("openid.sreg.fullname");
                String timezone = uri.getQueryParameter("openid.sreg.timezone");
                String country = uri.getQueryParameter("openid.sreg.country");

                final Account account = new Account(nickname, Constants.ACCOUNT_TYPE);
                AccountManager.get(FedAccountAuthenticatorActivity.this).addAccountExplicitly(account, "", null);

                // Now we tell our caller, could be the Andreoid Account Manager or even our own application
                // that the process was successful

                final Intent intent = new Intent();
                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, nickname);
                intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
                intent.putExtra(AccountManager.KEY_AUTHTOKEN, Constants.ACCOUNT_TYPE);
//                intent.putExtra(AccountManager.KEY_USERDATA, )
                setAccountAuthenticatorResult(intent.getExtras());
                setResult(RESULT_OK, intent);
                finish();
                return true;
            } else if (url != null && url.startsWith("http://mobile.fedoraproject.org/")) {
                LogUtil.d("Error " + url);
                finish();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //mListener.onError(new LeanError(LeanError.Type.ServerError, " Could not connect to Facebook authorization server."));
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgress.setVisibility(View.GONE);
        }

    }

    private class InternalWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgress.setProgress(newProgress);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            LogUtil.d(consoleMessage.message());
            return BuildConfig.DEBUG;
        }
    }
}
