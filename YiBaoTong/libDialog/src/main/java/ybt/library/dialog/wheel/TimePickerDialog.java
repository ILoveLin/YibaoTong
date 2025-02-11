package ybt.library.dialog.wheel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ybt.library.dialog.R;
import ybt.library.dialog.wheel.loop.LoopView;

import java.util.Arrays;
import java.util.List;
/**
 * 实现3D滚轮弹窗效果，时分秒
 * package_name ybt.library.dialog.wheel.wheel
 * Created by yang on 2016/8/30.
 */
public class TimePickerDialog extends Dialog{

    public interface OnTimeSelectedListener {
        void onTimeSelected(int[] times);
    }


    private Params params;

    public TimePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(TimePickerDialog.Params params) {
        this.params = params;
    }


    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopHour, loopMin;
        private OnTimeSelectedListener callback;
    }

    public static class Builder {
        private final Context context;
        private final TimePickerDialog.Params params;

        public Builder(Context context) {
            this.context = context;
            params = new TimePickerDialog.Params();
        }

        /**
         * 获取当前选择的时间
         *
         * @return int[]数组形式返回。例[12,30]
         */
        private final int[] getCurrDateValues() {
            int currHour = Integer.parseInt(params.loopHour.getCurrentItemValue());
            int currMin = Integer.parseInt(params.loopMin.getCurrentItemValue());
            return new int[]{currHour, currMin};
        }

        @SuppressLint("Override")
        public TimePickerDialog create() {
            final TimePickerDialog dialog = new TimePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel_picker_time, null);

            final LoopView loopHour = (LoopView) view.findViewById(R.id.loop_hour);
            loopHour.setArrayList(d(0, 23));
            loopHour.setCurrentItem(12);

            final LoopView loopMin = (LoopView) view.findViewById(R.id.loop_min);
            loopMin.setArrayList(d(0, 59));
            loopMin.setCurrentItem(30);

            view.findViewById(R.id.dialog_titlebar_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onTimeSelected(getCurrDateValues());
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

            params.loopHour = loopHour;
            params.loopMin = loopMin;
            dialog.setParams(params);

            return dialog;
        }


        public Builder setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
            params.callback = onTimeSelectedListener;
            return this;
        }


        /**
         * 将数字传化为集合，并且补充0
         *
         * @param startNum 数字起点
         * @param count    数字个数
         * @return
         */
        private static List<String> d(int startNum, int count) {
            String[] values = new String[count];
            for (int i = startNum; i < startNum + count; i++) {
                String tempValue = (i < 10 ? "0" : "") + i;
                values[i - startNum] = tempValue;
            }
            return Arrays.asList(values);
        }
    }
}
