package com.lzyc.ybtappcal.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.Util;

/**
 * 检查更新
 * Created by lxx on 2017/7/13.
 */

public class PopupWindowUpdate extends PopupWindow {

    Activity mContext;
    String[] descs = new String[]{};

    int forced, versionCode, minVersionCode;

    public PopupWindowUpdate(Activity context, int forced, int versionCode, int minVersionCode, String[] descs) {
        this.mContext = context;
        this.forced = forced;
        this.versionCode = versionCode;
        this.minVersionCode = minVersionCode;
        this.descs = descs;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.pop_version_update, null);

        TextView tvLater = (TextView) conentView.findViewById(R.id.tv_updata_later);
        TextView tvNow = (TextView) conentView.findViewById(R.id.tv_updata_now);
        ListView listContent = (ListView) conentView.findViewById(R.id.list_content);

        UpdataAdapter adapter = new UpdataAdapter();
        listContent.setAdapter(adapter);
        adapter.updata(descs);

        this.setContentView(conentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        if (forced == 1) {
            if (versionCode < minVersionCode) {
                tvLater.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                tvLater.setBackgroundResource(R.drawable.shape_bg_white_border_gray);
                tvLater.setEnabled(false);
                tvLater.setClickable(false);
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_VERSION_ISFORCED, true);
            } else {
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_VERSION_ISFORCED, false);
            }
        } else {
            SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_VERSION_ISFORCED, false);
        }

        tvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(null != onUpdateListener){
                   onUpdateListener.onUpdateNow();
               }
            }
        });
    }

    public void showPopupWindow(View parent, int gravity) {
        if (!this.isShowing()) {
            Util.setBackgroundAlpha(mContext, 0.5f);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        Util.setBackgroundAlpha(mContext, 1f);
    }

    private UpdateListener onUpdateListener;

    public void setOnUpdateListener(UpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    public interface UpdateListener{
        void onUpdateNow();
    }

    class UpdataAdapter extends BaseAdapter {

        String[] data = new String[]{};

        public void updata(String[] data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int i) {
            return data[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Drawable drawable = mContext.getResources().getDrawable(R.drawable.shape_circle_green);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(mContext);
            tv.setLayoutParams(lp);
            tv.setPadding(0, 0, 0, 55);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setTextSize(15);
            tv.setTextColor(mContext.getResources().getColor(R.color.color_555555));
            tv.setCompoundDrawablePadding(20);
            tv.setCompoundDrawables(drawable, null, null, null);

            tv.setText(data[i]);

            return tv;
        }

    }
}
