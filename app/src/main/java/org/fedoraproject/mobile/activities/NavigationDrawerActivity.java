/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fedoraproject.mobile.activities;

import android.accounts.Account;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.fedoraproject.mobile.BuildConfig;
import org.fedoraproject.mobile.R;
import org.fedoraproject.mobile.fragments.BadgesLeaderboardFragment;
import org.fedoraproject.mobile.fragments.InfraStatusFragment;
import org.fedoraproject.mobile.fragments.MyAccountFragment;
import org.fedoraproject.mobile.fragments.NewsfeedFragment;
import org.fedoraproject.mobile.fragments.UserBadgesFragment;
import org.fedoraproject.mobile.utils.AccountUtil;
import org.fedoraproject.mobile.utils.Constants;
import org.fedoraproject.mobile.utils.LogUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 * <p/>
 * Created by Julien De Nadai on 01/11/2014.
 */
public class NavigationDrawerActivity extends ActionBarActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private AccountUtil mAccountUtil;
    private ImageView mPortrait;
    private TextView mName;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        mAccountUtil = new AccountUtil(this);
        mAccountUtil.registerListener(new AccountUtil.OnAccountChangedListener() {
            @Override
            public void onAccountChanged() {
                refreshAccount();
            }
        });

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mPager = (ViewPager) findViewById(R.id.content_frame);


        findViewById(R.id.drawer_newsfeed).setOnClickListener(this);
        findViewById(R.id.drawer_infrastructure_status).setOnClickListener(this);
        findViewById(R.id.drawer_badges).setOnClickListener(this);
//        findViewById(R.id.drawer_fedora_people).setOnClickListener(this);
//        findViewById(R.id.drawer_packages_updates).setOnClickListener(this);
//        findViewById(R.id.drawer_preferences).setOnClickListener(this);

        mPortrait = (ImageView) findViewById(R.id.drawer_my_account);
        mPortrait.setOnClickListener(this);

        mName = (TextView) findViewById(R.id.drawer_my_account_name);
        mName.setOnClickListener(this);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Account
        refreshAccount();

        if (savedInstanceState == null) {
            selectItem(Constants.FRAGMENT_NEWSFEED);
        }
    }

    private void refreshAccount() {
        boolean found = false;

        final Picasso picasso = Picasso.with(this);
        picasso.setLoggingEnabled(BuildConfig.PICASSO_LOG);
        picasso.setIndicatorsEnabled(BuildConfig.PICASSO_INDICATOR);

        if (mAccountUtil.hasAccount()) {
            final Account account = mAccountUtil.getAccount();
            if (null != account) {
                final String imgUrl = getLibravatarImage("julien.denadai@gmail.com");

                LogUtil.d("account found: " + account.name + " icon: " + imgUrl);

                mName.setText(account.name);

                picasso.load(imgUrl)
                        .placeholder(R.drawable.ic_launcher)
//                    .transform(new RoundCropTransformation())
                        .into(mPortrait);
                found = true;
            }
        }
        if (!found) {
            mName.setText(R.string.account_default_name);

            picasso.load(R.drawable.ic_launcher)
//                    .transform(new RoundCropTransformation())
                    .into(mPortrait);
        }
    }

    @Nullable
    private String getLibravatarImage(@NonNull String email) {
        StringBuilder hexString = null;
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(email.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            hexString = new StringBuilder("https://seccdn.libravatar.org/avatar/");
            for (byte aMessageDigest : messageDigest) {
                String hash = Integer.toHexString(0xFF & aMessageDigest);
                while (hash.length() < 2)
                    hash = "0" + hash;
                hexString.append(hash);
            }
            hexString.append("?s=");
            hexString.append(mPortrait.getMeasuredWidth());
            hexString.append("&d=http://cdn.libravatar.org/avatar/40f8d096a3777232204cb3f796c577b7?s=80&d=retro");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null != hexString ? hexString.toString() : null;
    }

    @Override
    protected void onDestroy() {
        mAccountUtil.unregisterListener();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(Gravity.LEFT);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectItem(int position) {

        // update the main content by replacing fragments
        switch (position) {
            case Constants.FRAGMENT_MY_ACCOUNT:
                mPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return MyAccountFragment.newInstance();
                    }

                    @Override
                    public int getCount() {
                        return 1;
                    }

                    @Override
                    public int getItemPosition(Object object) {
                        return POSITION_NONE;
                    }
                });
                setTitle(R.string.drawer_my_account);
                break;
            case Constants.FRAGMENT_NEWSFEED:
                mPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return NewsfeedFragment.newInstance();
                    }

                    @Override
                    public int getCount() {
                        return 1;
                    }

                    @Override
                    public int getItemPosition(Object object) {
                        return POSITION_NONE;
                    }
                });
                setTitle(R.string.drawer_newsfeed);
                break;
            case Constants.FRAGMENT_INFRA_STATUS:
                mPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return InfraStatusFragment.newInstance();
                    }

                    @Override
                    public int getCount() {
                        return 1;
                    }

                    @Override
                    public int getItemPosition(Object object) {
                        return POSITION_NONE;
                    }
                });
                setTitle(R.string.drawer_infrastructure_status);
                break;
            case Constants.FRAGMENT_BADGES:
                mPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        int index = mAccountUtil.hasAccount() ? position : position + 1;
                        switch (index) {
                            case 0:
                                return UserBadgesFragment.newInstance(mAccountUtil.getAccount().name);
                            default:
                                return BadgesLeaderboardFragment.newInstance();
                        }
                    }

                    @Override
                    public int getCount() {
                        return mAccountUtil.hasAccount() ? 2 : 1;
                    }

                    @Override
                    public int getItemPosition(Object object) {
                        return POSITION_NONE;
                    }
                });
                setTitle(R.string.drawer_badges);
                break;
            case Constants.FRAGMENT_PREFERENCES:
            default:
                mPager.setAdapter(null);
                Toast.makeText(this, R.string.error_not_implemented_yet, Toast.LENGTH_SHORT).show();
                return;
        }
        mPager.setCurrentItem(0);
        mPager.getAdapter().notifyDataSetChanged();

        // update selected item title, then close the drawer
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_my_account:
            case R.id.drawer_my_account_name:
                if (!mAccountUtil.hasAccount()) {
                    mAccountUtil.addAccount(this);
                } else {
                    selectItem(Constants.FRAGMENT_MY_ACCOUNT);
                }
                break;
            case R.id.drawer_newsfeed:
                selectItem(Constants.FRAGMENT_NEWSFEED);
                break;
            case R.id.drawer_infrastructure_status:
                selectItem(Constants.FRAGMENT_INFRA_STATUS);
                break;
            case R.id.drawer_badges:
                selectItem(Constants.FRAGMENT_BADGES);
                break;
//            case R.id.drawer_fedora_people:
//                selectItem(Constants.FRAGMENT_FEDORA_PEOPLE);
//                break;
//            case R.id.drawer_packages_updates:
//                selectItem(Constants.FRAGMENT_PACKAGES_UPDATES);
//                break;
//            case R.id.drawer_preferences:
//                selectItem(Constants.FRAGMENT_PREFERENCES);
//                break;
            default:
                LogUtil.wtf("Unreacheable code");
                break;
        }
    }
}