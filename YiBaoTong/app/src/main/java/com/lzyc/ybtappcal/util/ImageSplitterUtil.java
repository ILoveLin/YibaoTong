package com.lzyc.ybtappcal.util;

import android.app.Activity;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class ImageSplitterUtil {
    private Activity activity;
    private int halfHeight;


    /**
     * @param bitmap
     * @return List<ImagePiece>
     */
    public static List<ImagePiece> splitImage(Bitmap bitmap, Activity activity, int halfHeight, int screenHeight) {
        List<ImagePiece> imagePieces = new ArrayList<ImagePiece>();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        ImagePiece imagePiece1 = new ImagePiece();
        imagePiece1.setBitmap(Bitmap.createBitmap(bitmap, 0, halfHeight, width, width));

        ImagePiece imagePiece2 = new ImagePiece();
        imagePiece2.setBitmap(Bitmap.createBitmap(bitmap, halfHeight, screenHeight, width, width));

        imagePieces.add(imagePiece1);
        imagePieces.add(imagePiece2);

        return imagePieces;


    }


	/*	public static Bitmap createBitmap (Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter)
        从原始位图剪切图像，这是一种高级的方式。可以用Matrix(矩阵)来实现旋转等高级方式截图
		参数说明：
		　　Bitmap source：要从中截图的原始位图
		　　int x:起始x坐标
		　　int y：起始y坐标
		int width：要截的图的宽度
		int height：要截的图的宽度
		Bitmap.Config  config：一个枚举类型的配置，可以定义截到的新位图的质量
		返回值：返回一个剪切好的Bitmap*/


/*

		for (int i = 0; i < piece; i++)
		{
			for (int j = 0; j < piece; j++)
			{

				ImagePiece imagePiece = new ImagePiece();
				imagePiece.setIndex(j + i * piece);

				int x = j * pieceWidth;
				int y = i * pieceWidth;

				imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y,
						pieceWidth, pieceWidth));
				imagePieces.add(imagePiece);
			}
		}
*/

}
