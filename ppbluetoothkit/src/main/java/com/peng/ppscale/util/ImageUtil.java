package com.peng.ppscale.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public class ImageUtil {


    public static boolean isChinese(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < 0x4E00 || c > 0x9FFF) {
                return false;
            }
        }
        return true;
    }

    /**
     * 把文字画到图片上，并居中
     *
     * @param userName
     * @return
     */
    public static Bitmap drawTextToPic(String userName) {
        int sp = 30;
        if (isChinese(userName)) {
            sp = 30;
            if (userName.length() > 3) {
                userName = userName.substring(0, 3);
            }
        } else {
            sp = 30;
            if (userName.length() > 4) {
                userName = userName.substring(0, 4);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(104, 32, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
//            canvas.drawColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setTextSize(sp);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        Rect rect = new Rect(0, 0, 104, 32);
        if (isChinese(userName)) {
            int baseline = (int) ((rect.bottom - rect.top) / 2
                    + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
            canvas.drawText(userName, rect.width() / 2.0f, baseline, paint);
        } else {
            int baseline = (int) ((rect.bottom - rect.top) / 2
                    + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
//            canvas.drawText(userName, rect.width() / 2, baseline - 3, paint);
            canvas.drawText(userName, rect.width() / 2.0f, baseline - 3f, paint);
        }


//            String labelFilename = "file:///android_asset/20220608102501.jpg";
//
//            String actualFilename = labelFilename.split("file:///android_asset/")[1];
//            InputStream labelsInput = null;
//
//            try {
//                labelsInput = ProtocalTorreDeviceHelper.getInstance().getContext().getAssets().open(actualFilename);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Bitmap bitmap = BitmapFactory.decodeStream(labelsInput);

        return bitmap;
    }


    /**
     * 把图像转成二进制字符串
     *
     * @param
     * @return
     */
    public static String bitmapToBinary(String userName) {

        //1、把文字画到图片上，并居中
        Bitmap bitmap = drawTextToPic(userName);

//        Bitmap bitmap1 = compressBitmap1(bitmap, 104, 32);
        // 2.灰度化
        Bitmap grayImage = convertGreyImg(bitmap);
        // 3.二值化
        Bitmap binaryImage = zeroAndOne(grayImage);

        // 4.输出到txt文本
        return convertTo2B(binaryImage);

        /*保存图片
        File newFile = new File("d:\\test5.jpg");
        ImageIO.write(binaryImage, "jpg", newFile);*/

//        String hex = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000000011110000000000001111000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000001111000000001111111100000000111111110000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000011111000000011110001110000001111000111000000000000000000000000000000000\n" +
//                "00000000000000000000000000000001111011000000111000000111000011100000011100000000000000000000000000000000\n" +
//                "00000000000000000000000000000001100011000000110000000011000011000000001100000000000000000000000000000000\n" +
//                "00000000000000000000000000000011000011000000110000000011000011000000001100000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000000000000011000000000000001100000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000000000000111000000000000011100000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000000000000110000000000000011000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000000000001110000000000000111000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000000000011100000000000001110000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000000000111000000000000011100000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000000001110000000000000111000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000000011100000000000001110000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000000111000000000000011100000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000001110000000000000111000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000011100000000000001110000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000111100000000000011110000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000111111111111100011111111111110000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000011000000111111111111000011111111111100000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

//        return hex.replace("\n", "");

    }


    /**
     * 将彩⾊图转换为灰度图
     *
     * @param img 位图
     * @return 返回转换好的位图
     */
    public static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的⾼
        int[] pixels = new int[width * height]; //通过位图的⼤⼩创建像素点数组
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }


    /**
     * 图⽚进⾏⼆值化⿊⽩
     */
    static Bitmap zeroAndOne(Bitmap bm) {
        int width = bm.getWidth();//原图像宽度
        int height = bm.getHeight();//原图像⾼度
        int color;//⽤来存储某个像素点的颜⾊值
        int r, g, b, a;//红，绿，蓝，透明度
        //创建空⽩图像，宽度等于原图宽度，⾼度等于原图⾼度，⽤ARGB_8888渲染，这个不⽤了解，这样写就⾏了
        Bitmap bmp = Bitmap.createBitmap(width, height
                , Bitmap.Config.ARGB_8888);
        int[] oldPx = new int[width * height];//⽤来存储原图每个像素点的颜⾊信息
        int[] newPx = new int[width * height];//⽤来处理处理之后的每个像素点的颜⾊信息
        /**
         * 第⼀个参数oldPix[]:⽤来接收（存储）bm这个图像中像素点颜⾊信息的数组
         * 第⼆个参数offset:oldPix[]数组中第⼀个接收颜⾊信息的下标值
         * 第三个参数width:在⾏之间跳过像素的条⽬数，必须⼤于等于图像每⾏的像素数
         * 第四个参数x:从图像bm中读取的第⼀个像素的横坐标
         * 第五个参数y:从图像bm中读取的第⼀个像素的纵坐标
         * 第六个参数width:每⾏需要读取的像素个数
         * 第七个参数height:需要读取的⾏总数
         */
        bm.getPixels(oldPx, 0, width, 0, 0, width, height);//获取原图中的像素信息
        for (int i = 0; i < width * height; i++) {//循环处理图像中每个像素点的颜⾊值
            color = oldPx[i];//取得某个点的像素值
            r = Color.red(color);//取得此像素点的r(红⾊)分量
            g = Color.green(color);//取得此像素点的g(绿⾊)分量
            b = Color.blue(color);//取得此像素点的b(蓝⾊分量)
            a = Color.alpha(color);//取得此像素点的a通道值
            //此公式将r,g,b运算获得灰度值，经验公式不需要理解
            int gray = (int) ((float) r * 0.3 + (float) g * 0.59 + (float) b * 0.11);
            //下⾯前两个if⽤来做溢出处理，防⽌灰度公式得到到灰度超出范围（0-255）
            if (gray > 255) {
                gray = 255;
            }
            if (gray < 0) {
                gray = 0;
            }
            if (gray != 0) {//如果某像素的灰度值不是0(⿊⾊)就将其置为255（⽩⾊）
                gray = 255;
            }
            newPx[i] = Color.argb(a, gray, gray, gray);//将处理后的透明度（没变），r,g,b分量重新合成颜⾊值并将其存储在数组中
        }
        /**
         * 第⼀个参数newPix[]:需要赋给新图像的颜⾊数组//The colors to write the bitmap
         * 第⼆个参数offset:newPix[]数组中第⼀个需要设置给图像颜⾊的下标值//The index of the first color to read from pixels[]
         * 第三个参数width:在⾏之间跳过像素的条⽬数//The number of colors in pixels[] to skip between rows.
         * Normally this value will be the same as the width of the bitmap,but it can be larger(or negative).
         * 第四个参数x:从图像bm中读取的第⼀个像素的横坐标//The x coordinate of the first pixels to write to in the bitmap.
         * 第五个参数y:从图像bm中读取的第⼀个像素的纵坐标//The y coordinate of the first pixels to write to in the bitmap.
         * 第六个参数width:每⾏需要读取的像素个数The number of colors to copy from pixels[] per row.
         * 第七个参数height:需要读取的⾏总数//The number of rows to write to the bitmap.
         */
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);//将处理后的像素信息赋给新图
        return bmp;//返回处理后的图像
    }

    /**
     * 输出 0,1 TXT文本
     */
    public static String convertTo2B(Bitmap bufferedImage) {
        try {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                for (int i = 0; i < bufferedImage.getWidth(); i++) {
                    int color = bufferedImage.getPixel(i, j);
                    int red = (color & 0x00ff0000) >> 16;  //取高两位
                    int green = (color & 0x0000ff00) >> 8; //取中两位
                    int blue = color & 0x000000ff; //取低两位

                    //来判断该像素是否为白色
                    if (color < -8388608) {
                        builder.append("1");
                    } else {
                        builder.append("0");
                    }
                }
                builder.append("\n");
            }
            Log.v("syncUserInfo liyp_", builder.toString());
            return builder.toString().replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 输出 0,1 TXT文本
     */
    public static void writeToTxt(Bitmap bufferedImage, String toSaveFilePath) {
        File file = new File(toSaveFilePath);
        try {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                StringBuilder builder1 = new StringBuilder();
                for (int i = 0; i < bufferedImage.getWidth(); i++) {
                    int color = bufferedImage.getPixel(i, j);
                    int red = (color & 0x00ff0000) >> 16;  //取高两位
                    int green = (color & 0x0000ff00) >> 8; //取中两位
                    int blue = color & 0x000000ff; //取低两位

                    //来判断该像素是否为白色
                    if (color < -8388608) {
                        builder.append("0");
                        builder1.append("0");
                    } else {
                        builder.append("1");
                        builder1.append("1");
                    }
                }
                Log.d("syncUserInfo" + j, builder1.toString());
                builder.append("\r\n");
            }
            Writer writer = new OutputStreamWriter(new FileOutputStream(file, true), Charset.defaultCharset());
            writer.write(builder.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片压缩: 按尺寸压缩
     *
     * @param beforeBitmap 要压缩的图片
     * @param newWidth     压缩后的宽度
     * @param newHeight    压缩后的高度
     * @return 压缩后的图片
     */
    static public Bitmap compressBitmap1(Bitmap beforeBitmap, double newWidth, double newHeight) {
        // 图片原有的宽度和高度
        float beforeWidth = beforeBitmap.getWidth();
        float beforeHeight = beforeBitmap.getHeight();

        // 计算宽高缩放率
        float scaleWidth = 0;
        float scaleHeight = 0;
        if (beforeWidth > beforeHeight) {
            scaleWidth = ((float) newWidth) / beforeWidth;
            scaleHeight = ((float) newHeight) / beforeHeight;
        } else {
            scaleWidth = ((float) newWidth) / beforeHeight;
            scaleHeight = ((float) newHeight) / beforeWidth;
        }

        // 矩阵对象
        Matrix matrix = new Matrix();
        // 缩放图片动作 缩放比例
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建一个新的Bitmap 从原始图像剪切图像
        Bitmap afterBitmap = Bitmap.createBitmap(beforeBitmap, 0, 0,
                (int) beforeWidth, (int) beforeHeight, matrix, true);
        return afterBitmap;
    }


}
