package ybt.library.dialog.wheel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ybt.library.dialog.R;
import ybt.library.dialog.bean.CCityBean;
import ybt.library.dialog.bean.CCountysBean;
import ybt.library.dialog.bean.CProviceBean;
import ybt.library.dialog.wheel.listener.LoopListener;
import ybt.library.dialog.wheel.loop.LoopView;

/**
 * 实现3D滚轮弹窗效果，省，市,区三级联动
 * package_name ybt.library.dialog.wheel
 * Created by yang on 2017/3/21.
 */
public class RegionPickerDialog2 extends Dialog {
    private static final String TAG = RegionPickerDialog2.class.getSimpleName();
    protected Params params;

    public RegionPickerDialog2(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(RegionPickerDialog2.Params params) {
        this.params = params;
    }

    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopCity;
        private LoopView loopProvice;
        private LoopView loopQu;
        private int initSelection;
        private OnTitlebarClickListener listener;
        private List<CProviceBean> listProvice;
    }

    public static class Builder {
        private final Context context;
        private final RegionPickerDialog2.Params params;

        public Builder(Context context) {
            this.context = context;
            params = new RegionPickerDialog2.Params();
            params.listProvice = new ArrayList<CProviceBean>();
            try {
                InputStreamReader inputReader = new InputStreamReader(context.getAssets().open("sanjidiqu.json"));
                BufferedReader bufReader = new BufferedReader(inputReader);
                String line = "";
                StringBuffer result = new StringBuffer();
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                JsonArray jsonArray = new Gson().fromJson(result.toString(), JsonArray.class);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonElement jsonElement = jsonArray.get(i);
                    CProviceBean provice = new Gson().fromJson(jsonElement.toString(), CProviceBean.class);
                    params.listProvice.add(provice);
                }
            } catch (Exception e) {
                Log.e(TAG, "ERROR:INFO==" + e.getMessage());
                Log.e(TAG, e.getMessage(), e.getCause());
            }
        }

        private final String getCurrentProviceValue() {
            return params.loopProvice.getCurrentItemValue();
        }

        private final String getCurrentCityValue() {
            return params.loopCity.getCurrentItemValue();
        }
        private final String getCurrentQuValue() {
            String qu=params.loopQu.getCurrentItemValue();
            if(qu.isEmpty()){
                qu="";
            }
            return qu;
        }

        private final int getCurrentProviceIndex() {
            return params.loopProvice.getCurrentItem();
        }


//        public void setDefaultValue(String currentProvice, String currentCity) {
//            setCurrentProvice(currentProvice);
//            List<String> listCity=params.listProvice.get(currentProvice);
//            for (int i = 0; i <listCity.size() ; i++) {
//                String city=listCity.get(i);
//               if(currentCity.equals(city)){
//                   setCurrentCityIndex(i);
//                   break;
//               }
//            }
//        }

        public void setDefaultValue(int provicePosition, int cityPosition) {
            setCurrentProviceIndex(provicePosition);
            setCurrentCityIndex(cityPosition);
        }

        private final int getCurrentCityIndex() {
            return params.loopCity.getCurrentItem();
        }

        private final int getCurrentQuIndex() {
            return params.loopQu.getCurrentItem();
        }

        public void setCurrentProvice(String currentProvice) {
            for (int i = 0; i < params.listProvice.size(); i++) {
                String provice = params.listProvice.get(i).getProvince();
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
            params.loopProvice.setCurrentItem(position);
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
        public RegionPickerDialog2 create() {
            final RegionPickerDialog2 dialog = new RegionPickerDialog2(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel_picker_region, null);
            view.findViewById(R.id.loop_qu_fragment).setVisibility(View.VISIBLE);
            view.findViewById(R.id.dialog_titlebar_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            final LoopView loopProvice = (LoopView) view.findViewById(R.id.loop_provice);
            int positionProvice = loopProvice.getCurrentItem();
            List<String> provices = new ArrayList<String>();
            for (CProviceBean cprovice : params.listProvice) {
                provices.add(cprovice.getProvince());
            }
            loopProvice.setArrayList(provices);
            loopProvice.setNotLoop();
            final LoopView loopCity = (LoopView) view.findViewById(R.id.loop_city);
            int positionCity = loopCity.getCurrentItem();
            List<String> citys = new ArrayList<String>();
            for (CCityBean city : params.listProvice.get(positionProvice).getCitys()) {
                citys.add(city.getCity());
            }
            loopCity.setArrayList(citys);
            loopCity.setNotLoop();
            final LoopView loopQu = (LoopView) view.findViewById(R.id.loop_qu);
            List<String> qus = new ArrayList<String>();
            for (CCountysBean county : params.listProvice.get(positionProvice).getCitys().get(positionCity).getCountys()) {
                qus.add(county.getCounty());
            }
            loopQu.setArrayList(qus);
            loopQu.setNotLoop();
            if (params.listProvice.size() > 0) {
                loopProvice.setCurrentItem(params.initSelection);
                loopCity.setCurrentItem(params.initSelection);
                loopQu.setCurrentItem(params.initSelection);
            }
            loopProvice.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    List<String> citys = new ArrayList<String>();
                    int positionProvice = loopProvice.getCurrentItem();
                    for (CCityBean city : params.listProvice.get(positionProvice).getCitys()) {
                        citys.add(city.getCity());
                        List<String> qus = new ArrayList<String>();
                        if(city.getCountys().size()>0){
                            for (CCountysBean cc : city.getCountys()) {
                                qus.add(cc.getCounty());
                                loopQu.setArrayList(qus);
                            }
                        }else{
                            qus.add("");
                            loopQu.setArrayList(qus);
                        }
                    }
                    loopCity.setArrayList(citys);
                }
            });
            loopCity.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    List<String> qus = new ArrayList<String>();
                    int positionProvice = loopProvice.getCurrentItem();
                    int positionCity = loopCity.getCurrentItem();
                    if(params.listProvice.size()>0){
                        for (CCountysBean qu : params.listProvice.get(positionProvice).getCitys().get(positionCity).getCountys()) {
                            qus.add(qu.getCounty());
                        }
                        if(qus.size()>0){
                            loopQu.setArrayList(qus);
                        }else{
                            qus.add("");
                            loopQu.setArrayList(qus);
                        }
                    }
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
            params.loopProvice = loopProvice;
            params.loopQu = loopQu;
            dialog.setParams(params);
            view.findViewById(R.id.dialog_titlebar_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String p = getCurrentProviceValue();
                    String c = getCurrentCityValue();
                    String q = getCurrentQuValue();
                    int pIndex = getCurrentProviceIndex();
                    int cIndex = getCurrentCityIndex();
                    int qIndex=getCurrentQuIndex();
                    if (TextUtils.isEmpty(p)) {
                        return;
                    }
                    if (TextUtils.isEmpty(c)) {
                        return;
                    }
                    if (params.listener == null) {
                        return;
                    }
                    params.listener.onTitlebarOkClickListener(pIndex, cIndex,qIndex, p, c,q);
                    dialog.dismiss();
                }
            });
            return dialog;
        }
    }

    public interface OnTitlebarClickListener {
        void onTitlebarOkClickListener(int p, int c,int q, String provice, String city,String qu);
    }

}
