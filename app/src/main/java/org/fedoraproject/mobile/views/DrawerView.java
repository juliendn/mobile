package org.fedoraproject.mobile.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.fedoraproject.mobile.R;

/**
 * An item in left drawer
 *
 * Created by Julien De Nadai on 01/11/2014.
 */
public class DrawerView extends RelativeLayout {

    public DrawerView(Context context) {
        this(context, null, 0);
    }

    public DrawerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View root = LayoutInflater.from(context).inflate(R.layout.layout_drawer_item, this, true);
        ImageView imageView = (ImageView) root.findViewById(R.id.drawer_item_icon);
        TextView textView = (TextView) root.findViewById(R.id.drawer_item_title);

        TypedArray typedArray = context.obtainStyledAttributes(new int[] { android.R.attr.selectableItemBackground });
        int backgroundId = typedArray.getResourceId(0, 0);
        //first 0 is the index in the array, second is the   default value
        typedArray.recycle();

        // get custom attrs
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawerView);

        int iconId = typedArray.getResourceId(R.styleable.DrawerView_drawer_icon, R.drawable.ic_launcher);
        int titleId = typedArray.getResourceId(R.styleable.DrawerView_drawer_title, R.string.app_name);

        typedArray.recycle();

        setBackgroundResource( backgroundId );

        imageView.setImageResource(iconId);
        textView.setText(titleId);

        setClickable(true);
    }

}
