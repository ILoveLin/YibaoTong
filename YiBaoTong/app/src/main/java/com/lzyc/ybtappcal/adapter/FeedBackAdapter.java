package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/12/22.
 */
public class FeedBackAdapter extends BaseAdapter {

    private List<String> listPath;
    private Context mContext;
    private OnFbItemChildListener listener;
    private Bitmap bitmap = null;
    private List<String> listUrl;

    public FeedBackAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.listPath = list;
        listUrl = new ArrayList<>();
    }

    public List<String> getListPath() {
        return listPath;
    }

    public List<String> getListUrl() {
        return listUrl;
    }

    public void addUrl(String url) {
        listUrl.add(url);
        notifyDataSetChanged();
    }

    public void deleteUrl(int position) {
        listUrl.remove(position);
        notifyDataSetChanged();
    }

    public void setOnFbItemChildListener(OnFbItemChildListener listener) {
        this.listener = listener;
    }

    public void addItem(String path) {
        this.listPath.add(path);
        this.notifyDataSetChanged();
    }

    public void deleteItem(String path) {
        this.listPath.remove(path);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = 0;
        if (listPath == null) {
            listPath = new ArrayList<>();
        }
        count = listPath.size() + 1;
        return count;
    }

    @Override
    public Object getItem(int i) {
        String path = listPath.get(i);
        if (path != null) {
            return path;
        }
        return null;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        String path = "";
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_feedback_pic, null);
            holder.item_fb_iv_content = (ImageView) convertView.findViewById(R.id.item_fb_iv_content);
            holder.item_fb_iv_delete = (ImageView) convertView.findViewById(R.id.item_fb_iv_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == listPath.size()) {
            holder.item_fb_iv_delete.setVisibility(View.GONE);
            holder.item_fb_iv_content.setImageBitmap(BitmapFactory.decodeResource(
                    mContext.getResources(), R.mipmap.icon_feedback_add));
        } else {
            path = listPath.get(position);
            bitmap = BitmapUtil.getimage(path);
            holder.item_fb_iv_content.setImageBitmap(bitmap);
            holder.item_fb_iv_delete.setVisibility(View.VISIBLE);
        }
        final String finalPath = path;
        holder.item_fb_iv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && (position < listPath.size())) {
                    listener.onImageContentClick(position, finalPath);
                } else {
                    listener.onImageContentAddClick(position);
                }
            }
        });
        holder.item_fb_iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && (position < listPath.size())) {
                    listener.onImageDeleteClick(position, finalPath);
                } else {
                    listener.onImageContentAddClick(position);
                }
            }
        });
        if (position == 3) {
            holder.item_fb_iv_content.setVisibility(View.GONE);
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView item_fb_iv_content;
        ImageView item_fb_iv_delete;
    }

    public interface OnFbItemChildListener {
        void onImageContentClick(int position, String path);

        void onImageDeleteClick(int position, String path);

        void onImageContentAddClick(int position);
    }
}
