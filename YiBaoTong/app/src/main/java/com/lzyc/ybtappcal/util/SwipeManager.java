package com.lzyc.ybtappcal.util;


import com.lzyc.ybtappcal.view.SwipeLayout;

/**
 * Created by yang on 2017/01/06.
 */
public class SwipeManager {
	private SwipeManager(){}
	private static SwipeManager mInstance = new SwipeManager();
	
	public static SwipeManager getInstance(){
		return mInstance;
	}
	
	private SwipeLayout currentLayout;//用来记录当前打开的SwipeLayout
	public void setSwipeLayout(SwipeLayout layout){
		this.currentLayout = layout;
	}
	
	public void clearCurrentLayout(){
		currentLayout = null;
	}
	
	public void closeCurrentLayout(){
		if(currentLayout!=null){
			currentLayout.close();
		}
	}
	
	public boolean isSwipe(SwipeLayout swipeLayout){
		if(currentLayout==null){
			//说明当前没有打开的layout
			return true;
		}else {
			//说明有打开的layout
			return currentLayout==swipeLayout;
		}
	}
}
