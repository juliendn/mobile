package org.fedoraproject.mobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.fedoraproject.mobile.BuildConfig;
import org.fedoraproject.mobile.R;
import org.fedoraproject.mobile.datas.badges.BadgeOwner;
import org.fedoraproject.mobile.datas.badges.Leaderboard;
import org.fedoraproject.mobile.utils.Constants;
import org.fedoraproject.mobile.utils.LogUtil;
import org.fedoraproject.mobile.webapis.Badges;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Fragment to show badges leaderboard
 * <p/>
 * Created by Julien De Nadai on 03/11/2014.
 */
public class BadgesLeaderboardFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;
    private LeaderboardAdapter mAdapter;
    private ProgressBar mProgress;
    private TextView mError;

    public static Fragment newInstance() {
        return new BadgesLeaderboardFragment();
    }

    public BadgesLeaderboardFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_badges_leaderboard, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.leaderboard_recyclerview);
        mSwipe = (SwipeRefreshLayout) root.findViewById(R.id.leaderboard_swipe);
        mProgress = (ProgressBar) root.findViewById(R.id.leaderboard_progress);
        mError = (TextView) root.findViewById(R.id.leaderboard_error);
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                outRect.set(4, 4, 4, 4);
//            }
//        });
        mAdapter = new LeaderboardAdapter();
        mRecyclerView.setAdapter(mAdapter);

        refresh();
    }

    private void refresh() {
        mSwipe.setRefreshing(true);

        final RestAdapter.LogLevel logLevel = BuildConfig.DEBUG ? RestAdapter.LogLevel.BASIC : RestAdapter.LogLevel.NONE;

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BADGES_FEDORAPROJECT_URL)
                .setLogLevel(logLevel)
                .build();
        Badges service = restAdapter.create(Badges.class);
        service.getLeaderboard(new Callback<Leaderboard>() {
            @Override
            public void success(Leaderboard leaderboard, Response response) {
                if (isAdded()) {
                    mProgress.setVisibility(View.GONE);
                    final List<BadgeOwner> badgeOwners = leaderboard.getBadgeOwners();
                    LogUtil.d("Found " + badgeOwners.size() + " badgeOwners");
                    mAdapter.swapBadgeOwners(badgeOwners);
                    mSwipe.setRefreshing(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (isAdded()) {
                    mProgress.setVisibility(View.GONE);
                    mError.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), R.string.error_fail_query_leaderboard, Toast.LENGTH_LONG).show();
                    mSwipe.setRefreshing(false);
                }
            }
        });
    }

    private class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        //        private final ImageView mImg;
        private final TextView mName;
        private final TextView mBadges;
        private final TextView mRank;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);
//            mImg = (ImageView) itemView.findViewById(R.id.leaderboard_item_img);
            mName = (TextView) itemView.findViewById(R.id.leaderboard_item_name);
            mBadges = (TextView) itemView.findViewById(R.id.leaderboard_item_badges);
            mRank = (TextView) itemView.findViewById(R.id.leaderboard_item_rank);
        }

        public void apply(BadgeOwner badgeOwner) {
//            Picasso picasso = Picasso.with(mImg.getContext());
//            picasso.setLoggingEnabled(BuildConfig.PICASSO_LOG);
//            picasso.setIndicatorsEnabled(BuildConfig.PICASSO_INDICATOR);
//            picasso.setIndicatorsEnabled(false);
//            picasso.load("https://seccdn.libravatar.org/avatar/ced44b7024a4437dadab0d8ea1de245f3a8e1c633525ce29a5c1ad0fbbf05282?s=100&d=https%3A%2F%2Fbadges.fedoraproject.org%2Fstatic%2Fimg%2Fbadger_avatar.png")
//                    .placeholder(R.drawable.ic_launcher)
////                    .transform(new SquareCropTransformation())
//                    .into(mImg);

            mName.setText(badgeOwner.getName());
            mBadges.setText(badgeOwner.getBadges(mBadges.getContext()));
            mRank.setText(badgeOwner.getRank());
        }
    }

    private class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardViewHolder> {
        private final ArrayList<BadgeOwner> mBadgeOwners;

        LeaderboardAdapter() {
            mBadgeOwners = new ArrayList<BadgeOwner>();
        }

        public void swapBadgeOwners(@Nullable List<BadgeOwner> badgeOwners) {
            if (null != badgeOwners) {
                mBadgeOwners.clear();
                mBadgeOwners.addAll(badgeOwners);
                notifyDataSetChanged();
            }
        }

        @Override
        public LeaderboardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            final View root = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_badges_leaderboard, viewGroup, false);

            return new LeaderboardViewHolder(root);
        }

        @Override
        public void onBindViewHolder(LeaderboardViewHolder newsfeedViewHolder, int i) {
            newsfeedViewHolder.apply(mBadgeOwners.get(i));
        }

        @Override
        public int getItemCount() {
            return mBadgeOwners.size();
        }
    }
}
