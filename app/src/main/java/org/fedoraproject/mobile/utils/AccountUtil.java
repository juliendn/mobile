package org.fedoraproject.mobile.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OnAccountsUpdateListener;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * An utility class to handle connections
 * Created by Julien De Nadai on 03/11/2014.
 */
public class AccountUtil {
    private final Context mContext;
    private final AccountManager mAccountManager;
    private final SharedPreferences mPref;
    private final OnAccountsUpdateListener mAccountListener;
    private OnAccountChangedListener mListener;
    private Account[] mAccounts;

    public AccountUtil(Context context) {
        mContext = context;
        mAccountManager = AccountManager.get(mContext);
        mAccounts = mAccountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);

        mAccountListener = new OnAccountsUpdateListener() {

            @Override
            public void onAccountsUpdated(Account[] accounts) {
                mAccounts = accounts;
                if (null != mListener) {
                    mListener.onAccountChanged();
                }
            }
        };
    }

    public void registerListener(OnAccountChangedListener listener) {
        mListener = listener;
        mAccountManager.addOnAccountsUpdatedListener(mAccountListener, null, false);
    }

    public void unregisterListener() {
        mAccountManager.removeOnAccountsUpdatedListener(mAccountListener);
    }

    public boolean hasAccount() {
        return mAccounts.length > 0;
    }

    @Nullable
    public Account getLastAccountUsed() {
        Account lastAccountUsed = null;
        String lastAccountUsedName = mPref.getString(Constants.PREF_LAST_ACCOUNT_USED, null);
        if (null != lastAccountUsedName) {
            for (Account account : mAccounts) {
                if (lastAccountUsedName.equals(account.name)) {
                    lastAccountUsed = account;
                    break;
                }
            }
        }
        return lastAccountUsed;
    }

    public void setLastAccountUsed(@NonNull Account account) {
        mPref.edit().putString(Constants.PREF_LAST_ACCOUNT_USED, account.name).apply();
    }

    //java.lang.SecurityException: caller uid 10113 is different than the authenticator's uid
//    @Nullable
//    public String getIcon(@NonNull Account account) {
//        return mAccountManager.getUserData(account, Constants.ACCOUNT_IMG_URL);
//    }

    @Nullable
    public Intent getAddAccountIntent() {
        AccountManagerFuture<Bundle> futur = mAccountManager.addAccount(Constants.ACCOUNT_TYPE, Constants.AUTH_TOKEN_TYPE, null, null, null, null, null);
        Bundle bundle = null;
        Intent intent = null;
        try {
            futur.getResult();
            intent = bundle.getParcelable(AccountManager.KEY_INTENT);
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        }
        return intent;
    }

    public void addAccount(Activity activity) {
        mAccountManager.addAccount(Constants.ACCOUNT_TYPE, Constants.AUTH_TOKEN_TYPE, null, null, activity, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bundle = future.getResult();
                    //TODO: parse bundle
                    if (null != mListener) {
                        mListener.onAccountChanged();
                    }
                } catch (OperationCanceledException e) {
                    LogUtil.d("fail to add account");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }


    public Account getAccount() {
        if (mAccounts.length > 0) {
            return mAccounts[0];
        } else {
            return null;
        }
    }

    public void setAccounts(Account[] accounts) {
        mAccounts = accounts;
    }


    public interface OnAccountChangedListener {
        public void onAccountChanged();
    }
}
