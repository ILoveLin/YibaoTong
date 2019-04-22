package ybt.library.dialog.wheel.loop;


import java.util.Timer;
import java.util.TimerTask;
/**
 * package_name ybt.library.dialog.wheel.loop
 * Created by yang on 2016/8/29.
 */
public final class MyTimerTask extends TimerTask {

    private float a;
    private float b;
    private final int c;
    private final Timer timer;
    private final LoopView loopView;

    public MyTimerTask(LoopView loopview, int i, Timer timer) {
        super();
        this.loopView = loopview;
        c = i;
        this.timer = timer;

        a = 2.147484E+09F;
        b = 0.0F;
    }

    public final void run() {
        if (a == 2.147484E+09F) {
            a = (float) (c - LoopView.getSelectItem(loopView)) * loopView.l * (float) loopView.h;
            if (c > LoopView.getSelectItem(loopView)) {
                b = -1000F;
            } else {
                b = 1000F;
            }
        }
        if (Math.abs(a) < 1.0F) {
            timer.cancel();
            loopView.handler.sendEmptyMessage(2000);
            return;
        }
        int j = (int) ((b * 10F) / 1000F);
        int i = j;
        if (Math.abs(a) < (float) Math.abs(j)) {
            i = (int) (-a);
        }
        LoopView loopview = loopView;
        loopview.totalScrollY = loopview.totalScrollY - i;
        float f = a;
        a = (float) i + f;
        loopView.handler.sendEmptyMessage(1000);
    }
}
