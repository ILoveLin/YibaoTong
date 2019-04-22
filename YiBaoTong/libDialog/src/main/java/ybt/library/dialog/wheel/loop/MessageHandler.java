package ybt.library.dialog.wheel.loop;

import android.os.Handler;
import android.os.Message;
/**
 * package_name ybt.library.dialog.wheel.loop
 * Created by yang on 2016/8/29.
 */
public final class MessageHandler extends Handler {

    final LoopView a;

    MessageHandler(LoopView loopview) {
        super();
        a = loopview;
    }

    public final void handleMessage(Message paramMessage) {
        if (paramMessage.what == 1000)
            this.a.invalidate();
        while (true) {
            if (paramMessage.what == 2000)
                LoopView.b(a);
            else if (paramMessage.what == 3000)
                this.a.c();
            super.handleMessage(paramMessage);
            return;
        }
    }

}
