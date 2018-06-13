package com.daily.news.location;


import com.amap.api.location.AMapLocation;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.utils.L;
import com.zjrb.core.utils.UIUtils;


/**
 * Created by gaoyangzhen on 2018/4/26.
 * Initialization on Demand Holder的单例模式
 * 实现原理：java机制规定，内部类LocationManagerHolder只有在getInstance()方法第一次调用的时候才会被加载（实现了lazy），
 * 而且其加载过程是线程安全的（实现线程安全）。内部类加载的时候实例化一次instance。
 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证，没有一个同步的关键字
 */

public class LocationManager implements Location.Listener {

    public static String AMAPLOCATION = "amaplocation";
    public static String IPLOCATION = "iplocation";
    public static String FINAL_LOCATION = "final_location";

    private AMapLocation aMapLocation = null;//gps定位数据
    private String city = null;
    private DataLocation dataLocation;//ip定位数据
    private DataLocation final_location;//不区分gps或者ip定位，统一的数据，gps或者ip定位成功后都会更新这个值
    private Location mLocation;

    LocationCallback callback;

    boolean isUpdate = true;


    public static class LocationManagerHolder {
        private static LocationManager instance = new LocationManager();
    }

    private LocationManager() {

    }


    public static LocationManager getInstance() {

        return LocationManagerHolder.instance;
    }

    public void startLocation() {
        LocationGPS();
    }

    /**
     * 设置是否需要上传定位信息
     * @param isUpdate
     */
    public void isUpdate(boolean isUpdate)
    {
        this.isUpdate = isUpdate;
    }



    /**
     * 高德地图gps
     */
    public void LocationGPS() {
        mLocation = new Location(UIUtils.getApp(), LocationManager.this);
        mLocation.start();
    }


    /**
     * ip定位
     */
    public void locationIP() {
        new IPLocationTask(new LoadingCallBack<DataLocation>() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onError(String errMsg, int errCode) {
                L.e("location error===", errMsg);
                loc_fail(errMsg);
            }

            @Override
            public void onSuccess(DataLocation data) {
                L.e("city ip===", data.getLocation());
                setIPLocation(data);
                setLocation(data);
                ip_loc_Sucess(data);
                //应后台要求，每次定位成功都要上报
                if (null != data.getAddress() && isUpdate) {
                    new ReportAddressTask(null).exe(data.getAddress().getCountry(), data.getAddress().getProvince(), data.getAddress().getCity());
                }
            }

        }).setTag(this).exe();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == LOCATION_SUCCESS) {
            setaMapLocation(aMapLocation);
            gps_loc_Sucess(aMapLocation);
            DataLocation dataLocation = new DataLocation();
            dataLocation.setLocation(aMapLocation.getCity());
            DataLocation.Address address = new DataLocation.Address();
            address.setCity(aMapLocation.getCity());
            address.setProvince(aMapLocation.getProvince());
            address.setCountry(aMapLocation.getCountry());
            dataLocation.setAddress(address);

            if (null != dataLocation.getAddress() && isUpdate) {
                new ReportAddressTask(null).exe(dataLocation.getAddress().getCountry(), dataLocation.getAddress().getProvince(), dataLocation.getAddress().getCity());
            }
            setLocation(dataLocation);
        } else {
            // Gps定位失败，通过ip定位
            locationIP();
        }
        if (mLocation != null) { // 定位一次，就停止定位
            mLocation.stop().destroy();
        }
    }

    /**
     * 执行回调，先判空，这样调用端可以自由选择是否实现回调方法
     */
    private void gps_loc_Sucess(AMapLocation location) {
        if (callback != null)
            callback.gps_locationSucess(location);
    }

    private void ip_loc_Sucess(DataLocation location) {
        if (callback != null)
            callback.ip_locationSucess(location);
    }

    private void loc_fail(String error) {
        if (callback != null)
            callback.locationFail(error);
    }


    public AMapLocation getaMapLocation() {
        return SPHelper.get().getObject(AMAPLOCATION);
    }

    public void setaMapLocation(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
        SPHelper.get().put(AMAPLOCATION, aMapLocation).commit();
    }


    public DataLocation getIPLocation() {
        return SPHelper.get().getObject(IPLOCATION);
    }

    public void setIPLocation(DataLocation location) {
        this.dataLocation = location;
        SPHelper.get().put(IPLOCATION, location).commit();
    }

    public void setLocation(DataLocation location) {
        this.final_location = location;
        SPHelper.get().put(FINAL_LOCATION, location).commit();
    }

    /**
     * 浙江新闻中读取缓存数据可以调用这个method，缓存的是gps或ip定位中最新的数据
     *
     * @return
     */
    public DataLocation getLocation() {
        return SPHelper.get().getObject(FINAL_LOCATION);
    }

    public interface LocationCallback {
        void gps_locationSucess(AMapLocation location);

        void ip_locationSucess(DataLocation location);

        void locationFail(String error);
    }

    public void setLocationCallback(LocationCallback callback) {
        this.callback = callback;
    }


}
