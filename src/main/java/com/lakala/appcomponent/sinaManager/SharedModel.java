package com.lakala.appcomponent.sinaManager;

import android.os.Parcel;
import android.os.Parcelable;

public class SharedModel implements Parcelable {

    private String text;
    private String imageUrl;

    public SharedModel() {
    }

    protected SharedModel(Parcel in) {
        text = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<SharedModel> CREATOR = new Creator<SharedModel>() {
        @Override
        public SharedModel createFromParcel(Parcel in) {
            return new SharedModel(in);
        }

        @Override
        public SharedModel[] newArray(int size) {
            return new SharedModel[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(imageUrl);
    }
}
