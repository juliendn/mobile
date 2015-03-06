package org.fedoraproject.mobile.views;

import android.accounts.Account;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.fedoraproject.mobile.BuildConfig;
import org.fedoraproject.mobile.R;

/**
 * An item in left drawer
 * <p/>
 * Created by Julien De Nadai on 01/11/2014.
 */
public class AccountView extends RelativeLayout {
    private final ImageView mImageView;
    private final TextView mNameTextView;

//    private final RecyclerView mList;
//    private final Adapter mAdapter;

    public AccountView(Context context) {
        this(context, null, 0);
    }

    public AccountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View root = LayoutInflater.from(context).inflate(R.layout.layout_drawer_header, this, true);
        mImageView = (ImageView) root.findViewById(R.id.account_current);
        mNameTextView = (TextView) root.findViewById(R.id.account_current_name);
//        mList = (RecyclerView) root.findViewById(R.id.account_list);


        TypedArray typedArray = context.obtainStyledAttributes(new int[]{android.support.v7.appcompat.R.attr.colorPrimary});
        int backgroundId = typedArray.getResourceId(0, 0);
        //first 0 is the index in the array, second is the   default value
        typedArray.recycle();

        root.setBackgroundResource(backgroundId);

//        mList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
//        mList.setItemAnimator(new DefaultItemAnimator());
//        mList.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                outRect.set(4, 4, 4, 4);
//            }
//        });
//
//        mAdapter = new Adapter();
//        mList.setAdapter(mAdapter);
    }

    public void setAccount(Account account, String url) {
        mNameTextView.setText(account.name);
        Picasso picasso = Picasso.with(getContext());
        if (BuildConfig.DEBUG) {
            picasso.setIndicatorsEnabled(BuildConfig.DEBUG);
            picasso.setLoggingEnabled(false);
        }

        picasso.load(url)
                .placeholder(R.drawable.ic_launcher)
//                    .transform(new RoundCropTransformation())
                .into(mImageView);
    }

//    private class Account {
//
//        public String mName;
//        public String mImg;
//
//        public Account(String name, String img) {
//            mName = name;
//            mImg = img;
//        }
//    }
//
//    private class AccountViewHolder extends RecyclerView.ViewHolder {
//
//        private ImageView mImg;
//
//        public AccountViewHolder(View itemView) {
//            super(itemView);
//            mImg = (ImageView) itemView;
//
//            mImg.setAdjustViewBounds(false);
//            mImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        }
//
//        public void apply(String url) {
//            Picasso picasso = Picasso.with(mImg.getContext());
//            picasso.setLoggingEnabled(BuildConfig.PICASSO_LOG);
//            picasso.setIndicatorsEnabled(BuildConfig.PICASSO_INDICATOR);
//            picasso.load(url)
//                    .placeholder(R.drawable.ic_launcher)
//                    .into(mImg);
//        }
//    }
//
//    private class Adapter extends RecyclerView.Adapter<AccountViewHolder> {
//
//        private final Account[] ACCOUNTS = {
//                new Account("Toto", "https://assets.mozilla.org/Brands-Logos/Firefox/logo-only/firefox_logo-only_RGB.png")
//                , new Account("Tata", "https://g.twimg.com/Twitter_logo_blue.png")
//                , new Account("Titi", "http://thevisualcommunicationguy.com/wp-content/uploads/2013/11/Starbucks-Logo-051711.gif")
//                , new Account("Tutu", "http://pngimg.com/upload/car_logo_PNG1667.png")
//                , new Account("Tyty", "http://wallpho.com/wp-content/uploads/8589130457239-xbox-logo-wallpaper-hd.jpg")
//                , new Account("Bob", "http://slidervilla.com/placid/files/2011/07/intel-logo.jpg")
//        };
//
//        public Adapter() {
//            super();
//        }
//
//        @Override
//        public AccountViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            return new AccountViewHolder(new ImageView(viewGroup.getContext()));
//        }
//
//        @Override
//        public void onBindViewHolder(AccountViewHolder accountViewHolder, int i) {
//            accountViewHolder.apply(ACCOUNTS[i].mImg);
//        }
//
//        @Override
//        public int getItemCount() {
//            return ACCOUNTS.length;
//        }
//    }

}
