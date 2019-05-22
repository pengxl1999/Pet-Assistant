package com.pengxl.petassistant;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolygonOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.pengxl.petassistant.StaticMember.convertViewToBitmap;
import static com.pengxl.petassistant.StaticMember.geoFence;

public class LocationActivity extends AppCompatActivity {

    private MapView mapView;
    private AMap aMap;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationClientOption;
    private Thread showPetPositionThread;
    private LatLng petPosition, userPosition;
    private Marker petPositionMarker;
    private Handler handler;
    private boolean isPetFirstLoc = true, isUserFirstLoc = true;
    private ImageButton back;
    private Bitmap petIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mapView = (MapView) findViewById(R.id.location_map);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mLocationClient.stopLocation();//停止定位
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
        //销毁定位客户端之后，若要重新开启定位请重新New一个AMapLocationClient对象。
    }

    private void init() {
        back = (ImageButton) findViewById(R.id.location_back);
        handler = new Handler();

        View view = View.inflate(LocationActivity.this, R.layout.marker_dog, null);
        petIcon = convertViewToBitmap(view);

        setUserLocation();
        setPetLocation();
        if(StaticMember.geoFence != null) {
            setFenceUI();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.onDestroy();
                mLocationClient.stopLocation();//停止定位
                finish();
            }
        });
    }

    private void setUserLocation() {
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        aMap.setMyLocationStyle(myLocationStyle);
        mLocationClient = new AMapLocationClient(LocationActivity.this);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation != null) {
                    if(aMapLocation.getErrorCode() == 0) {
                        userPosition = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        //Log.i("pengxl1999", "latitude:" + userPosition.latitude + " longitude:" + userPosition.longitude);
                        if(isPetFirstLoc && isUserFirstLoc) {
                            isUserFirstLoc = false;
                            aMap.animateCamera(CameraUpdateFactory.newLatLng(userPosition));
                            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                        }
                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        aMapLocation.getLatitude();//获取纬度
                        aMapLocation.getLongitude();//获取经度
                        aMapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                        Date date = new Date(aMapLocation.getTime());
                        df.format(date);//定位时间
                    }
                    else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        mLocationClientOption = new AMapLocationClientOption();
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClientOption.setInterval(2000);
        mLocationClient.setLocationOption(mLocationClientOption);
        mLocationClient.startLocation();
    }

    private void setPetLocation() {
        if(StaticMember.petPosition == null) {
            Log.e("PetPositionError", "No message from server, check network connection");
            Toast.makeText(LocationActivity.this, "无法获取宠物位置，请检查手机与项圈网络连接", Toast.LENGTH_LONG).show();
            return;
        }
        showPetPositionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!showPetPositionThread.isInterrupted()) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(petPositionMarker != null) {
                                    petPositionMarker.remove();
                                }
                                petPosition = StaticMember.petPosition;
                                petPositionMarker = aMap.addMarker(new MarkerOptions().draggable(false).icon(BitmapDescriptorFactory.fromBitmap(petIcon)));    //设置宠物定位图标样式
                                petPositionMarker.setPosition(petPosition);
                                petPositionMarker.setClickable(false);
                                if(isPetFirstLoc) {
                                    aMap.animateCamera(CameraUpdateFactory.newLatLng(petPosition));
                                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                                    isPetFirstLoc = false;
                                }
                            }
                        });
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        showPetPositionThread.start();
    }

    private void setFenceUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<LatLng> geoFencePoints = geoFence.getPoints();
                PolygonOptions polygonOptions = new PolygonOptions();
                polygonOptions.addAll(geoFencePoints);
                polygonOptions.strokeWidth(5) // 多边形的边框
                        .strokeColor(Color.argb(50, 0, 0, 0)) // 边框颜色
                        .fillColor(Color.argb(50, 255, 255, 255));   // 多边形的填充色
                aMap.addPolygon(polygonOptions);
            }
        });
    }
}
