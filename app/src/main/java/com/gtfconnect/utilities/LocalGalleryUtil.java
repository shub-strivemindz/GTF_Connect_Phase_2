package com.gtfconnect.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GroupChannelGalleryEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalGalleryUtil {


    private static GroupChannelGalleryEntity galleryEntity;


    public static GroupChannelGalleryEntity saveImage(String imageName,
                                                      String galleryType,
                                                      Drawable image,
                                                      String imageUrl,
                                                      String imageThumbnailUrl,
                                                      int gc_id){

        galleryEntity = new GroupChannelGalleryEntity();
        galleryEntity.setImageName(imageName);
        galleryEntity.setGalleryType(galleryType);
        galleryEntity.setImageData(getBytesFromImage(convertDrawableToBitmap(image)));
        galleryEntity.setImageUrl(imageUrl);
        galleryEntity.setImageThumbnailUrl(imageThumbnailUrl);
        galleryEntity.setGC_ID(gc_id);

        return galleryEntity;
    }


    // Convert Drawable to Bitmap in android
    private static Bitmap convertDrawableToBitmap(Drawable resource){
        Bitmap bitmap = Bitmap.createBitmap(resource.getIntrinsicWidth(),
                resource.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        resource.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        resource.draw(canvas);
        return bitmap;
    }





    // convert from bitmap to byte array

    public static byte[] getBytesFromImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }



    public static Bitmap getImageFromDB(byte[] blobImageData){
        return BitmapFactory.decodeByteArray(blobImageData,0,blobImageData.length);
    }



    private static File getGroupChannelImagePath()
    {
        File filePath;

        String fileName = "Profile"+new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())+".jpg";

        String downloadPath = "/" + "connect_files" + "/" +
                fileName;

        String fileDownloadPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + downloadPath;

        filePath = new File(fileDownloadPath);

        try{
            if (!filePath.getParentFile().exists()){
                filePath.getParentFile().mkdirs();
            }
        }
        catch (Exception e){
            Log.d("Exception while saving image",e.toString());
        }

        return filePath;
    }





    private static File getGroupChannelGifPath()
    {
        File filePath;

        String fileName = "Profile"+new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())+".gif";

        String downloadPath = "/" + "connect_files" + "/" +
                fileName;

        String fileDownloadPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + downloadPath;

        filePath = new File(fileDownloadPath);

        try{
            if (!filePath.getParentFile().exists()){
                filePath.getParentFile().mkdirs();
            }
        }
        catch (Exception e){
            Log.d("Exception while saving image",e.toString());
        }

        return filePath;
    }




    public static boolean saveImageToGallery(Context context, Drawable drawable){

        Bitmap bitmap = convertDrawableToBitmap(drawable);

        FileOutputStream outStream = null;

        File file = getGroupChannelImagePath();

        try {

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();


            Toast.makeText(context, "Image saved successfully!", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (Exception e){
            Log.d("Exception while saving image",e.toString());
            return false;
        }
    }




    public static File saveGifToGallery(Context context, GifDrawable gifDrawable){


        ByteBuffer byteBuffer = gifDrawable.getBuffer();
        File gifFile = getGroupChannelGifPath();

        FileOutputStream outStream = null;

        try{
            outStream = new FileOutputStream(gifFile);

            byte[] bytes = new byte[byteBuffer.capacity()];
            ((ByteBuffer) byteBuffer.duplicate().clear()).get(bytes);

            outStream.write(bytes, 0, bytes.length);
            outStream.close();
        }
        catch (Exception e){
            Log.d("Gif Exception",e.toString());
        }

        return gifFile;
    }
}
