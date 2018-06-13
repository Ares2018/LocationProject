package com.daily.news;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.daily.news.location.ReportAddressTask;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.permission.AbsPermSingleCallBack;
import com.zjrb.core.common.permission.Permission;
import com.zjrb.core.common.permission.PermissionManager;
import com.zjrb.core.utils.L;

import java.util.List;

public class MainActivity extends BaseActivity implements LocationManager.LocationCallback{

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = LocationManager.getInstance();
        locationManager.setLocationCallback(this);

        PermissionManager.get().request(this, new AbsPermSingleCallBack() {
            @Override
            public void onGranted(boolean isAlreadyDef) {
                locationManager.startLocation();
            }

            @Override
            public void onDenied(List<String> neverAskPerms) {
                locationManager.locationIP();
            }
        }, Permission.LOCATION_COARSE);


    }


    @Override
    public void gps_locationSucess(AMapLocation location) {

    }

    @Override
    public void ip_locationSucess(DataLocation location) {

    }

    @Override
    public void locationFail(String error) {

    }
}
