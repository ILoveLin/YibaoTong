package ybt.library.dialog.wheel.loop;

import java.util.Timer;
import java.util.TimerTask;
/**
 * package_name ybt.library.dialog.wheel.loop
 * Created by yang on 2016/8/29.
 */
public final class MTimer extends TimerTask {

    int a;
    int b;
    final int c;
    final Timer timer;
    final LoopView loopView;

    MTimer(LoopView loopview, int i, Timer timer) {
        super();
        loopView = loopview;
        c = i;
        this.timer = timer;
        a = 0x7fffffff;
        b = 0;
    }

    public final void run() {
        if (a == 0x7fffffff) {
            if (c < 0) {
                if ((float) (-c) > (loopView.l * (float) loopView.h) / 2.0F) {
                    a = (int) (-loopView.l * (float) loopView.h - (float) c);
                } else {
                    a = -c;
                }
            } else if ((float) c > (loopView.l * (float) loopView.h) / 2.0F) {
                a = (int) (loopView.l * (float) loopView.h - (float) c);
            } else {
                a = -c;
            }
        }
        b = (int) ((float) a * 0.1F);
        if (b == 0) {
            if (a < 0) {
                b = -1;
            } else {
                b = 1;
            }
        }
        if (Math.abs(a) <= 0) {
            timer.cancel();
            loopView.handler.sendEmptyMessage(3000);
            return;
        } else {
            LoopView loopview = loopView;
            loopview.totalScrollY = loopview.totalScrollY + b;
            loopView.handler.sendEmptyMessage(1000);
            a = a - b;
            return;
        }
    }
}
