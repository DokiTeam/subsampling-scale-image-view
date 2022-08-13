package com.davemorrissey.labs.subscaleview;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Wraps the scale, center and orientation of a displayed image for easy restoration on screen rotate.
 */
@SuppressWarnings("WeakerAccess")
public class ImageViewState implements Parcelable {

    private final float scale;

    private final float centerX;

    private final float centerY;

    private final int orientation;

    public ImageViewState(float scale, @NonNull PointF center, int orientation) {
        this.scale = scale;
        this.centerX = center.x;
        this.centerY = center.y;
        this.orientation = orientation;
    }

    public ImageViewState(Parcel in) {
        scale = in.readFloat();
        centerX = in.readFloat();
        centerY = in.readFloat();
        orientation = in.readInt();
    }

    public static final Creator<ImageViewState> CREATOR = new Creator<ImageViewState>() {
        @Override
        public ImageViewState createFromParcel(Parcel in) {
            return new ImageViewState(in);
        }

        @Override
        public ImageViewState[] newArray(int size) {
            return new ImageViewState[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(scale);
        dest.writeFloat(centerX);
        dest.writeFloat(centerY);
        dest.writeInt(orientation);
    }

    public float getScale() {
        return scale;
    }

    @NonNull
    public PointF getCenter() {
        return new PointF(centerX, centerY);
    }

    public int getOrientation() {
        return orientation;
    }

}
