package com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "group_channel_gallery")
public class GroupChannelGalleryEntity {

    @PrimaryKey(autoGenerate = true)
    int id;

    private String ImageName;

    private int GC_ID;

    private int GC_MEMBER_ID;

    private String GalleryType;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] ImageData;




    private String ImageUrl;


    private String ImageThumbnailUrl;


    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }


    public String getImageThumbnailUrl() {
        return ImageThumbnailUrl;
    }

    public void setImageThumbnailUrl(String imageThumbnailUrl) {
        ImageThumbnailUrl = imageThumbnailUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public int getGC_ID() {
        return GC_ID;
    }

    public void setGC_ID(int GC_ID) {
        this.GC_ID = GC_ID;
    }

    public int getGC_MEMBER_ID() {
        return GC_MEMBER_ID;
    }

    public void setGC_MEMBER_ID(int GC_MEMBER_ID) {
        this.GC_MEMBER_ID = GC_MEMBER_ID;
    }

    public String getGalleryType() {
        return GalleryType;
    }

    public void setGalleryType(String galleryType) {
        GalleryType = galleryType;
    }

    public byte[] getImageData() {
        return ImageData;
    }

    public void setImageData(byte[] imageData) {
        ImageData = imageData;
    }
}
