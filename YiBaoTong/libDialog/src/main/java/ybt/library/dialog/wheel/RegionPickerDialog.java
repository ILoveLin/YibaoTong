package ybt.library.dialog.wheel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ybt.library.dialog.R;
import ybt.library.dialog.wheel.listener.LoopListener;
import ybt.library.dialog.wheel.loop.LoopView;

/**
 * 实现3D滚轮弹窗效果，省，市两级联动
 * package_name ybt.library.dialog.wheel.wheel
 * Created by yang on 2016/8/30.
 */
public class RegionPickerDialog extends Dialog {

    protected Params params;
    public RegionPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(RegionPickerDialog.Params params) {
        this.params = params;
    }

    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopCity;
        private LoopView loopArea;
        private int initSelection;
        private OnTitlebarClickListener listener;
        private Map<String, ArrayList<String>> dataList;
        private List<String> proviceList;
    }

    public static class Builder {
        private final Context context;
        private final RegionPickerDialog.Params params;

        public Builder(Context context) {
            this.context = context;
            params = new RegionPickerDialog.Params();
            try {
                String fileName =context.getResources().getString(R.string.citydatajson);
                InputStreamReader inputReader = new InputStreamReader(context.getAssets().open(fileName));
                BufferedReader bufReader = new BufferedReader(inputReader);
                String line = "";
                StringBuffer result = new StringBuffer();
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                params.dataList = new Gson().fromJson(result.toString(), new TypeToken<Map<String, List<String>>>() {
                }.getType());
                params.proviceList = new ArrayList<String>();
                Iterator<String> keys = params.dataList.keySet().iterator();
                while (keys.hasNext()) {
                    String provice = keys.next();
                    params.proviceList.add(provice);
                }
            } catch (Exception e) {
                params.dataList = new HashMap<String, ArrayList<String>>();
            }
        }

        private final String getCurrentProviceValue() {
            return params.loopCity.getCurrentItemValue();
        }

        private final String getCurrentCityValue() {
            return params.loopArea.getCurrentItemValue();
        }

        private final int getCurrentProviceIndex() {
            return params.loopCity.getCurrentItem();
        }


        public void setDefaultValue(String currentProvice, String currentCity) {
            setCurrentProvice(currentProvice);
            List<String> listCity=params.dataList.get(currentProvice);
            for (int i = 0; i <listCity.size() ; i++) {
                String city=listCity.get(i);
               if(currentCity.equals(city)||(currentCity+"市").equals(city)){
                   setCurrentCityIndex(i);
                   break;
               }
            }
        }

        public void setDefaultValue(int provicePosition, int cityPosition) {
            setCurrentProviceIndex(provicePosition);
            setCurrentCityIndex(cityPosition);
        }

        private final int getCurrentCityIndex() {
            return params.loopArea.getCurrentItem();
        }

        public void setCurrentProvice(String currentProvice) {
            for (int i = 0; i < params.proviceList.size(); i++) {
                String provice=params.proviceList.get(i);
                if(currentProvice.contains("市")){
                    currentProvice= currentProvice.substring(0,currentProvice.length());
                }
                if (provice.equals(currentProvice)) {
                    setCurrentProviceIndex(i);
                    break;
                }
            }

        }

        public void setCurrentProviceIndex(int position) {
            params.loopCity.setCurrentItem(position);
        }

        private void setCurrentCityIndex(int position) {
            params.loopArea.setCurrentItem(position);
        }


        public Builder setSelection(int initSelection) {
            params.initSelection = initSelection;
            return this;
        }

        public Builder setOnRegionSelectedListener(OnTitlebarClickListener onRegionSelectedListener) {
            params.listener = onRegionSelectedListener;
            return this;
        }

        @SuppressLint("Override")
        public RegionPickerDialog create() {
            final RegionPickerDialog dialog = new RegionPickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel_picker_region, null);
            view.findViewById(R.id.dialog_titlebar_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            final LoopView loopCity = (LoopView) view.findViewById(R.id.loop_provice);
            loopCity.setArrayList(new ArrayList(params.dataList.keySet()));
            loopCity.setNotLoop();
            if (params.dataList.size() > 0) {
                loopCity.setCurrentItem(params.initSelection);
            }
            final LoopView loopArea = (LoopView) view.findViewById(R.id.loop_city);
            String selectedCity = loopCity.getCurrentItemValue();
            loopArea.setArrayList(params.dataList.get(selectedCity));
            loopArea.setNotLoop();
            loopCity.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    String selectedCity = loopCity.getCurrentItemValue();
                    loopArea.setArrayList(params.dataList.get(selectedCity));
                }
            });
            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.BOTTOM);
            win.setWindowAnimations(R.style.Animation_Bottom_Rising);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(params.canCancel);
            dialog.setCancelable(params.canCancel);
            params.loopCity = loopCity;
            params.loopArea = loopArea;
            dialog.setParams(params);
            view.findViewById(R.id.dialog_titlebar_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String p=getCurrentProviceValue();
                    String c=getCurrentCityValue();
                    int pIndex = getCurrentProviceIndex();
                    int cIndex = getCurrentCityIndex();
                    if(TextUtils.isEmpty(p)){
                        return;
                    }
                    if(TextUtils.isEmpty(c)){
                        return;
                    }
                    if(params.listener==null){
                        return;
                    }
                    params.listener.onTitlebarOkClickListener(pIndex, cIndex,p,c);
                    dialog.dismiss();
                }
            });
            return dialog;
        }
    }

    public interface OnTitlebarClickListener {
        void onTitlebarOkClickListener(int p, int c, String provice, String city);
    }

}
