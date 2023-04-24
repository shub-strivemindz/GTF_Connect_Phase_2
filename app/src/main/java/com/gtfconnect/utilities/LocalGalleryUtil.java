package com.gtfconnect.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GroupChannelGalleryEntity;

import java.io.ByteArrayOutputStream;

public class LocalGalleryUtil {


    private static GroupChannelGalleryEntity galleryEntity;


    public static GroupChannelGalleryEntity saveImage(String imageName,
                                                      String galleryType,
                                                      Drawable image,
                                                      String imageUrl,
                                                      String imageThumbnailUrl,
                                                      int gc_id,
                                                      int gc_member_id){

        galleryEntity = new GroupChannelGalleryEntity();
        galleryEntity.setImageName(imageName);
        galleryEntity.setGalleryType(galleryType);
        galleryEntity.setImageData(getBytesFromImage(convertDrawableToBitmap(image)));
        galleryEntity.setImageUrl(imageUrl);
        galleryEntity.setImageThumbnailUrl(imageThumbnailUrl);
        galleryEntity.setGC_ID(gc_id);
        galleryEntity.setGC_MEMBER_ID(gc_member_id);

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }



    public static Bitmap getImageFromDB(byte[] blobImageData){
        return BitmapFactory.decodeByteArray(blobImageData,0,blobImageData.length);
    }

}
