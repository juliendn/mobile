package org.fedoraproject.mobile.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.fedoraproject.mobile.BuildConfig;
import org.fedoraproject.mobile.R;
import org.fedoraproject.mobile.datas.badges.Badge;
import org.fedoraproject.mobile.datas.badges.User;
import org.fedoraproject.mobile.utils.Constants;
import org.fedoraproject.mobile.webapis.Badges;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Fragment to show badges leaderboard
 * Created by Julien De Nadai on 03/11/2014.
 */
public class UserBadgesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;
    private BadgesAdapter mAdapter;
    private ProgressBar mProgress;
    private TextView mError;
    private TextView mEarned;
    private CardView mHeader;

    public static Fragment newInstance(String user) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARG_USER, user);

        Fragment fragment = new UserBadgesFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public UserBadgesFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_badges_user, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.badges_recyclerview);
        mSwipe = (SwipeRefreshLayout) root.findViewById(R.id.badges_swipe);
        mProgress = (ProgressBar) root.findViewById(R.id.badges_progress);
        mError = (TextView) root.findViewById(R.id.badges_error);
        mEarned = (TextView) root.findViewById(R.id.badges_earned);
        mHeader = (CardView) root.findViewById(R.id.badges_header);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mSwipe.setColorSchemeResources(R.color.fedora_freedom
                , R.color.fedora_friends
                , R.color.fedora_features
                , R.color.fedora_first);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(4, 4, 4, 4);
            }
        });
        mAdapter = new BadgesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        refresh();
    }

    private void refresh() {
        mSwipe.setRefreshing(true);

        RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.NONE;
        if (BuildConfig.DEBUG) {
            logLevel = RestAdapter.LogLevel.BASIC;
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BADGES_FEDORAPROJECT_URL)
                .setLogLevel(logLevel)
                .build();
        Badges service = restAdapter.create(Badges.class);
        service.getUser(getUser(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                if (isAdded()) {
                    mProgress.setVisibility(View.GONE);
                    List<Badge> badges = user.getBadges();
                    mEarned.setText(String.format(getString(R.string.badges_earned), user.getName(), badges.size(), user.getRatio()));
                    mHeader.setVisibility(View.VISIBLE);
                    mAdapter.swapBadges(badges);
                    mSwipe.setRefreshing(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (isAdded()) {
                    mProgress.setVisibility(View.GONE);
                    mError.setText(String.format(getString(R.string.error_fail_query_badges), getUser()));
                    mError.setVisibility(View.VISIBLE);

//                    Toast.makeText(getActivity(), R.string.error_fail_query_leaderboard, Toast.LENGTH_LONG).show();
                    mSwipe.setRefreshing(false);
                }
            }
        });
    }

    private String getUser() {
        return getArguments().getString(Constants.ARG_USER);
    }

    private class BadgeViewHolder extends RecyclerView.ViewHolder {
        //        private final TextView mName;
        private final ImageView mBadges;

        public BadgeViewHolder(View itemView) {
            super(itemView);
//            mName = (TextView) itemView.findViewById(R.id.badge_item_name);
            mBadges = (ImageView) itemView.findViewById(R.id.badge_item_img);
        }

        public void apply(Badge badge) {
            Picasso picasso = Picasso.with(mBadges.getContext());
            picasso.setLoggingEnabled(BuildConfig.PICASSO_LOG);
            picasso.setIndicatorsEnabled(BuildConfig.PICASSO_INDICATOR);
            picasso.load(badge.getImage())
                    .placeholder(R.drawable.ic_launcher)
//                    .transform(new SquareCropTransformation())
                    .into(mBadges);

//            mName.setText(badge.getName());
        }
    }

    private class BadgesAdapter extends RecyclerView.Adapter<BadgeViewHolder> {
        private final ArrayList<Badge> mBadges;

        BadgesAdapter() {
            mBadges = new ArrayList<Badge>();
        }

        public void swapBadges(@Nullable List<Badge> badges) {
            if (null != badges) {
                mBadges.clear();
                mBadges.addAll(badges);
                notifyDataSetChanged();
            }
        }

        @Override
        public BadgeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View root = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_badges_grid, viewGroup, false);

            return new BadgeViewHolder(root);
        }

        @Override
        public void onBindViewHolder(BadgeViewHolder newsfeedViewHolder, int i) {
            newsfeedViewHolder.apply(mBadges.get(i));
        }

        @Override
        public int getItemCount() {
            return mBadges.size();
        }
    }
}
