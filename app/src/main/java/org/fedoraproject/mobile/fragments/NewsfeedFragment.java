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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.fedoraproject.mobile.BuildConfig;
import org.fedoraproject.mobile.R;
import org.fedoraproject.mobile.datas.Hrf.HrfResult;
import org.fedoraproject.mobile.datas.Hrf.Newsfeed;
import org.fedoraproject.mobile.utils.Constants;
import org.fedoraproject.mobile.utils.ItemAnimator;
import org.fedoraproject.mobile.utils.LogUtil;
import org.fedoraproject.mobile.webapis.HrfCloud;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Fragment managing the newsfeed ui
 * <p/>
 * Created by Julien De Nadai on 01/11/2014.
 */
public class NewsfeedFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;
    private NewsfeedAdapter mAdapter;
    private long mTimestamp;
    private ProgressBar mProgress;
    private TextView mError;

    static public NewsfeedFragment newInstance() {
        return new NewsfeedFragment();
    }

    public NewsfeedFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_newsfeed, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.newsfeed_recyclerview);
        mSwipe = (SwipeRefreshLayout) root.findViewById(R.id.newsfeed_swipe);
        mProgress = (ProgressBar) root.findViewById(R.id.newsfeed_progress);
        mError = (TextView) root.findViewById(R.id.newsfeed_error);
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                outRect.set(4, 4, 4, 4);
//            }
//        });
        mRecyclerView.setItemAnimator(new ItemAnimator());
        mAdapter = new NewsfeedAdapter();
        mRecyclerView.setAdapter(mAdapter);

        download();
    }

    private void download() {
        mSwipe.setRefreshing(true);

        String strLocale = TimeZone.getDefault().getID();

        RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.NONE;
        if (BuildConfig.DEBUG) {
            logLevel = RestAdapter.LogLevel.BASIC;
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.HRF_CLOUD_FEDORAPROJECT_URL)
                .setLogLevel(logLevel)
                .build();
        HrfCloud service = restAdapter.create(HrfCloud.class);
        service.queryLatestNewsfeed(strLocale, 7200, "desc", new Callback<HrfResult>() {

            @Override
            public void success(HrfResult hrfResult, Response response) {
                if (isAdded()) {
                    mProgress.setVisibility(View.GONE);
                    List<Newsfeed> newsfeeds = hrfResult.getResults();
                    if (newsfeeds.size() > 0) {
                        LogUtil.d("Found " + newsfeeds.size() + " newsfeeds");
                        mTimestamp = newsfeeds.get(0).getEpoch();
                        mAdapter.addNewsFeeds(newsfeeds);
                    }
//                    mRecyclerView.smoothScrollToPosition(0);
                    mSwipe.setRefreshing(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (isAdded()) {
                    mProgress.setVisibility(View.GONE);
                    mError.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), R.string.error_fail_query_newsfeeds, Toast.LENGTH_LONG).show();
                    mSwipe.setRefreshing(false);
                }
            }
        });
    }

    private void refresh() {
        mSwipe.setRefreshing(true);

        String strLocale = TimeZone.getDefault().getID();

        RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.NONE;
        if (BuildConfig.DEBUG) {
            logLevel = RestAdapter.LogLevel.BASIC;
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.HRF_CLOUD_FEDORAPROJECT_URL)
                .setLogLevel(logLevel)
                .build();
        HrfCloud service = restAdapter.create(HrfCloud.class);
        service.queryNewNewsfeed(strLocale, mTimestamp + 1, "asc", new Callback<HrfResult>() {

            @Override
            public void success(HrfResult hrfResult, Response response) {
                if (!isDetached()) {
                    List<Newsfeed> newsfeeds = hrfResult.getResults();
                    if (newsfeeds.size() > 0) {
                        LogUtil.d("Found " + newsfeeds.size() + " newsfeeds");
                        mTimestamp = newsfeeds.get(0).getEpoch();
                        mAdapter.addNewsFeeds(newsfeeds);
                    }
                    mSwipe.setRefreshing(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isDetached()) {
                    Toast.makeText(getActivity(), R.string.error_fail_query_newsfeeds, Toast.LENGTH_LONG).show();
                    mSwipe.setRefreshing(false);
                }
            }
        });
    }

    private class NewsfeedViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImg;
        private final TextView mTitle;
        private final TextView mSubTitle;
        private final TextView mDate;

        public NewsfeedViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.newsfeed_img);
            mTitle = (TextView) itemView.findViewById(R.id.newsfeed_title);
            mSubTitle = (TextView) itemView.findViewById(R.id.newsfeed_subtitle);
            mDate = (TextView) itemView.findViewById(R.id.newsfeed_date);
        }

        public void apply(Newsfeed newsfeed) {
            Picasso picasso = Picasso.with(mImg.getContext());
            picasso.setLoggingEnabled(BuildConfig.PICASSO_LOG);
            picasso.setIndicatorsEnabled(BuildConfig.PICASSO_INDICATOR);
            picasso.load(newsfeed.getImg())
                    .placeholder(R.drawable.ic_launcher)
                    .into(mImg);

            mTitle.setText(newsfeed.getTitle());

            mSubTitle.setText(newsfeed.getSubtitle());

            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            Date date = new Date(newsfeed.getEpoch() * 1000);
            mDate.setText(dateFormat.format(date));
        }
    }

    private class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedViewHolder> {
        private final ArrayList<Newsfeed> mNewsFeeds;

        NewsfeedAdapter() {
            mNewsFeeds = new ArrayList<Newsfeed>();
        }

        public void addNewsFeeds(List<Newsfeed> newsfeeds) {
            for (Newsfeed newsfeed : newsfeeds) {
                mNewsFeeds.add(0, newsfeed);
                notifyItemInserted(0);
            }
        }

        @Override
        public NewsfeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View root = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_newsfeed_item, viewGroup, false);

            return new NewsfeedViewHolder(root);
        }

        @Override
        public void onBindViewHolder(NewsfeedViewHolder newsfeedViewHolder, int i) {
            newsfeedViewHolder.apply(mNewsFeeds.get(i));
        }

        @Override
        public int getItemCount() {
            return mNewsFeeds.size();
        }
    }
}
