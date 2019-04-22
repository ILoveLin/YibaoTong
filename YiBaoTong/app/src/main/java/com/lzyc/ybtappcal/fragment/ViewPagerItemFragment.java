package com.lzyc.ybtappcal.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.widget.RoundImageViewSize;
import com.squareup.picasso.Picasso;

/**
 * Created by wpy on 15/9/2.
 */
public class ViewPagerItemFragment extends Fragment {

    private static final String BUNDLE_KEY = "bundle_key";
    private String path;
    private static boolean isRound;

    public static ViewPagerItemFragment instantiateWithArgs(final Context context, final String path) {
        final ViewPagerItemFragment fragment = (ViewPagerItemFragment) instantiate(context, ViewPagerItemFragment.class.getName());
        final Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY, path);
        fragment.setArguments(args);
        return fragment;
    }

    public static ViewPagerItemFragment instantiateWithArgs(final Context context, final String path, boolean round) {
        final ViewPagerItemFragment fragment = (ViewPagerItemFragment) instantiate(context, ViewPagerItemFragment.class.getName());
        final Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY, path);
        fragment.setArguments(args);
        isRound = round;
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments();
    }

    private void initArguments() {
        final Bundle arguments = getArguments();
        if (arguments != null) {
            path = (String) arguments.getSerializable(BUNDLE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        if(isRound){
            view = inflater.inflate(R.layout.viewpager_item_fragment_round, container, false);
            initViewRound(view);
        } else {
            view = inflater.inflate(R.layout.viewpager_item_fragment, container, false);
            initView(view);
        }

        return view;
    }

    private void initView(final View view) {
        final ImageView backgroundImage = (ImageView) view.findViewById(R.id.item_fragment_image);
        backgroundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(getActivity()).load(path).resize(ScreenUtils.getScreenWidth(),
                DensityUtils.dp2px(150)).placeholder(R.mipmap.icon_hospital_photo_empity)
                .error(R.mipmap.icon_hospital_photo_empity)
                .into(backgroundImage);
    }

    private void initViewRound(final View view) {
        final RoundImageViewSize backgroundImage = (RoundImageViewSize) view.findViewById(R.id.item_fragment_image_round);

        backgroundImage.setBorderWidth(2)
                .setBorderColor(getResources().getColor(R.color.color_f3f3f3))
                .setImgType(RoundImageViewSize.TYPE_ROUND)
                .setLeftTopCornerRadius(5)
                .setRightTopCornerRadius(5)
                .setRightBottomCornerRadius(5)
                .setLeftBottomCornerRadius(5);

        Picasso.with(getActivity()).load(path).resize(ScreenUtils.getScreenWidth(),
                DensityUtils.dp2px(150)).placeholder(R.mipmap.icon_hospital_photo_empity)
                .error(R.mipmap.icon_hospital_photo_empity)
                .into(backgroundImage);
    }
}
