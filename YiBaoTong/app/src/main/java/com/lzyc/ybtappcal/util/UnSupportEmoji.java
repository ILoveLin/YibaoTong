package com.lzyc.ybtappcal.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.ToastUtil;

/**
 * Created by lovelin on 2016/10/27.
 */
public class UnSupportEmoji implements TextWatcher {

    private EditText et;
    private Context mContext;

    public UnSupportEmoji(EditText et, Context mContext) {
        this.mContext = mContext;
        this.et = et;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // i("s:" + s);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // e("s:" + s + "  start:" + start + " before:" + before + " count:" + count);
        //输入的类容
        CharSequence input = s.subSequence(start, start + count);
        //e("输入信息:" + input);
        // 退格
        if (count == 0) return;

        //如果 输入的类容包含有Emoji
        if (isEmojiCharacter(input)) {
            show("不支持输入Emoji表情符号");
            //那么就去掉
            et.setText(removeEmoji(s));
        }

        //如果输入的字符超过最大限制,超出的部分 砍掉~
//            if (s.length() > 140) {
//                show("超过输入的最大限制");
//                et.setText(s.subSequence(0, start));
//            }
        //最后光标移动到最后 TODO 这里可能会有更好的解决方案
        et.setSelection(et.getText().toString().length());

    }

    @Override
    public void afterTextChanged(Editable s) {
        // d("s:" + s);
    }

    /**
     * 去除字符串中的Emoji表情
     *
     * @param source
     * @return
     */
    private String removeEmoji(CharSequence source) {
        String result = "";
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (isEmojiCharacter(c)) {
                continue;
            }
            result += c;
        }
        return result;
    }


    /**
     * 判断一个字符串中是否包含有Emoji表情
     *
     * @param input
     * @return true 有Emoji
     */
    private boolean isEmojiCharacter(CharSequence input) {
        for (int i = 0; i < input.length(); i++) {
            if (isEmojiCharacter(input.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是Emoji 表情,抄的那哥们的代码
     *
     * @param codePoint
     * @return true 是Emoji表情
     */
    public static boolean isEmojiCharacter(char codePoint) {
        // Emoji 范围
        boolean isScopeOf = (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF) && (codePoint != 0x263a))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));

        return !isScopeOf;
    }

    private void show(String msg) {
        ToastUtil.showToastCenter(mContext,msg);

    }

    void e(String msg) {
        LogUtil.e(this.getClass().getSimpleName(), msg);
    }

    void i(String msg) {
        LogUtil.i(this.getClass().getSimpleName(), msg);

    }

    void d(String msg) {
        LogUtil.d(this.getClass().getSimpleName(), msg);

    }

}
