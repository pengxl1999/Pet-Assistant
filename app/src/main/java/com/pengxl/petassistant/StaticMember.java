package com.pengxl.petassistant;

import android.graphics.Bitmap;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polygon;

import java.util.ArrayList;
import java.util.LinkedList;

public class StaticMember {
    public static Polygon geoFence;
    public static LatLng petPosition;
    public static String account;
    public static String password;
    public static int step = 0;

    public static Bitmap convertViewToBitmap(View view)     //View转Bitmap
    {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
