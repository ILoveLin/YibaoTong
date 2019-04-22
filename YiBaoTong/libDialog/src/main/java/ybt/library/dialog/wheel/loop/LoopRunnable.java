package ybt.library.dialog.wheel.loop;

import ybt.library.dialog.wheel.listener.LoopListener;
/**
 * package_name ybt.library.dialog.wheel.loop
 * Created by yang on 2016/8/29.
 */
public final class LoopRunnable implements Runnable {

    final LoopView loopView;

    LoopRunnable(LoopView loopview) {
        super();
        loopView = loopview;

    }

    public final void run() {
        LoopListener listener = loopView.loopListener;
        int i = LoopView.getSelectItem(loopView);
        listener.onItemSelect(i);
    }
}
