package com.gtfconnect.utilities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GifUtil {



    //public static downloadGif(Context context)



    public void saveGifImage(Context context, byte[] bytes, String imgName ) {
        FileOutputStream fos = null;
        try {
                File file = new File(getGifFilePath());

                fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.flush();
                fos.close();

                if (file != null) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, file.getName());
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
                    values.put(MediaStore.Images.Media.DESCRIPTION, "");
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/gif");
                    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());

                    ContentResolver contentResolver = context.getContentResolver();
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Toast.makeText(context, "Image saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public byte[] getBytesFromFile(File file) throws IOException {
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File is too large!");
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        InputStream is = new FileInputStream(file);
        try {
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        return bytes;
    }




    public static String getGifFilePath(){
        String gifFilePath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/" + "connect_gif" + "_"
                + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                + ".gif";

        return gifFilePath;
    }
}
