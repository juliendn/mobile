package org.fedoraproject.mobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
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
import org.fedoraproject.mobile.datas.Infra.InfraStatus;
import org.fedoraproject.mobile.datas.Infra.Service;
import org.fedoraproject.mobile.utils.Constants;
import org.fedoraproject.mobile.webapis.Status;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Fragment managing the newsfeed ui
 * <p/>
 * Created by Julien De Nadai on 01/11/2014.
 */
public class InfraStatusFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;
    private InfraStatusAdapter mAdapter;
    private TextView mError;
    private ProgressBar mProgress;
    private TextView mSummary;
    private CardView mHeader;

    static public InfraStatusFragment newInstance() {
        return new InfraStatusFragment();
    }

    public InfraStatusFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_infra_status, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.infra_status_recyclerview);
        mSwipe = (SwipeRefreshLayout) root.findViewById(R.id.infra_status_swipe);
        mSummary = (TextView) root.findViewById(R.id.infra_status_summary);
        mHeader = (CardView) root.findViewById(R.id.infra_status_header);
        mProgress = (ProgressBar) root.findViewById(R.id.infra_status_progress);
        mError = (TextView) root.findViewById(R.id.infra_status_error);
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
        mAdapter = new InfraStatusAdapter();
        mRecyclerView.setAdapter(mAdapter);

        refresh();
    }

    private void refresh() {
        if (!isDetached()) {
            mSwipe.setRefreshing(true);

            RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.NONE;
            if (BuildConfig.DEBUG) {
                logLevel = RestAdapter.LogLevel.BASIC;
            }

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.STATUS_FEDORAPROJECT_URL)
                    .setLogLevel(logLevel)
                    .build();
            Status service = restAdapter.create(Status.class);
            service.queryInfraStatuses(new Callback<InfraStatus>() {

                @Override
                public void success(InfraStatus infraStatus, Response response) {
                    if (isAdded()) {
                        mProgress.setVisibility(View.GONE);
                        List<Service> services = infraStatus.getServices();
                        mSummary.setText(infraStatus.getStatus());
                        mSummary.setBackgroundColor(infraStatus.getColor(getActivity()));
                        mHeader.setVisibility(View.VISIBLE);

                        mAdapter.swapServices(services);
                        mSwipe.setRefreshing(false);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (isAdded()) {
                        mProgress.setVisibility(View.GONE);
                        mError.setVisibility(View.VISIBLE);
//                        Toast.makeText(getActivity(), R.string.error_fail_query_infra_status, Toast.LENGTH_LONG).show();
                        mSwipe.setRefreshing(false);
                    }
                }
            });
        }
    }

    private class InfraStatusHolder extends RecyclerView.ViewHolder {

        private final View mStatus;
        private final TextView mTitle;
        private final TextView mSubTitle;

        public InfraStatusHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.infra_status_item_title);
            mSubTitle = (TextView) itemView.findViewById(R.id.infra_status_item_subtitle);
            mStatus = itemView.findViewById(R.id.infra_status_item_status);
        }

        public void apply(Service service) {
            // TODO: fill the view
            mTitle.setText(service.getName());
            mSubTitle.setText(service.getMessage(mSubTitle.getContext()));
            mStatus.setBackgroundColor(service.getColor(mStatus.getContext()));
        }
    }

    private class InfraStatusAdapter extends RecyclerView.Adapter<InfraStatusHolder> {
        private final ArrayList<Service> mServices;
        private static final int HEADER = 0;
        private static final int OTHER = 1;

        InfraStatusAdapter() {
            mServices = new ArrayList<Service>();
        }

        public void swapServices(List<Service> services) {
            if (null != services) {
                mServices.clear();
                mServices.addAll(services);
                notifyDataSetChanged();
            }
        }

        @Override
        public InfraStatusHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            View root;
            switch (position) {
                case HEADER:
                    root = LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.layout_infra_status_header, viewGroup, false);
                    break;
                default:
                    root = LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.layout_infra_status_item, viewGroup, false);
                    break;
            }

            return new InfraStatusHolder(root);
        }

        @Override
        public void onBindViewHolder(InfraStatusHolder newsfeedViewHolder, int position) {
            switch (position) {
                case HEADER:
                    break;
                default:
                    newsfeedViewHolder.apply(mServices.get(position - 1));
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? HEADER : OTHER;
        }

        @Override
        public int getItemCount() {
            return mServices.size() + 1;
        }
    }
}
