package com.lzyc.ybtappcal.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.top.TopWebviewActivity;
import com.lzyc.ybtappcal.bean.TopBanner;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by yang on 17/5/14.
 */
public class ViewPagerItemBiyaoFragment extends Fragment {

    private static final String BUNDLE_KEY = "bundle_key";
    private TopBanner.DataBean.ImagesBean imagesBean;

    public static ViewPagerItemBiyaoFragment instantiateWithArgs(final Context context, final TopBanner.DataBean.ImagesBean imagesBean) {
        final ViewPagerItemBiyaoFragment fragment = (ViewPagerItemBiyaoFragment) instantiate(context, ViewPagerItemBiyaoFragment.class.getName());
        final Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY, imagesBean);
        fragment.setArguments(args);
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
            imagesBean = (TopBanner.DataBean.ImagesBean) arguments.getSerializable(BUNDLE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.viewpager_item_fragment, container, false);
        initViews(view);
        return view;
    }

    private void initViews(final View view) {
        final ImageView imageview = (ImageView) view.findViewById(R.id.item_fragment_image);
        String path=imagesBean.getImg_url();
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(getActivity()).load(path).placeholder(R.mipmap.empty_biyao_banner)
                        .resize(ScreenUtils.getScreenWidth(), DensityUtils.dp2px(105))
                        .error(R.mipmap.empty_biyao_banner)
                        .into(imageview);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(imagesBean.getWeb_url())){
                    Intent intent=new Intent(getActivity(), TopWebviewActivity.class);
                    intent.putExtra(Contants.KEY_STR_URL,imagesBean.getWeb_url());
                    startActivity(intent);
                }
            }
        });
    }
}
