package com.lzyc.ybtappcal.widget.sort.pinyin;

import java.util.ArrayList;

import com.lzyc.ybtappcal.widget.sort.pinyin.HanziToPinyin3.Token;

public class PinYin
{
	// 汉字返回拼音，字母原样返回，都转换为小写
	public static String getPinYin(String input)
	{
		ArrayList<Token> tokens = HanziToPinyin3.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0)
		{
			for (Token token : tokens)
			{
				if (Token.PINYIN == token.type)
				{
					sb.append(token.target);
				} else
				{
					sb.append(token.source);
				}
			}
		}
		return sb.toString().toLowerCase();
	}
}
