package com.lzyc.ybtappcal.util;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ImagePiece implements Serializable
{
	private int index ; 
	private Bitmap bitmap ;
	
	public ImagePiece()
	{
	}

	public ImagePiece(int index, Bitmap bitmap)
	{
		this.index = index;
		this.bitmap = bitmap;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public Bitmap getBitmap()
	{
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
	}

	@Override
	public String toString()
	{
		return "ImagePiece [index=" + index + ", bitmap=" + bitmap + "]";
	}
	
}
