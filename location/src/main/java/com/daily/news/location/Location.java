package com.daily.news.location;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

/**
 * 定位 - 封装
 *
 * @author a_liYa
 * @date 2017/7/27 18:20.
 */
public class Location {

    public AMapLocationClient mLocationClient;

    private Listener mListener;

    public Location(Context context, Listener listener) {
        mListener = listener;
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (mListener != null) {
                        mListener.onLocationChanged(aMapLocation);
                    }
//                    if (aMapLocation.getErrorCode() == 0) {
//                        Log.e("TAG", "定位成功 " + aMapLocation.getProvince() + aMapLocation.getCity
//                                () + aMapLocation.getDistrict());
//                    }
                }
            }
        });

    }

    public Location start() {
        mLocationClient.startLocation();
        return this;
    }

    public Location stop() {
        mLocationClient.stopLocation();
        return this;
    }

    public void destroy() {
        mLocationClient.onDestroy();
        mLocationClient = null;
        mListener = null;
    }

    public interface Listener {
        int LOCATION_SUCCESS = AMapLocation.LOCATION_SUCCESS;
        void onLocationChanged(AMapLocation aMapLocation);
    }
}
