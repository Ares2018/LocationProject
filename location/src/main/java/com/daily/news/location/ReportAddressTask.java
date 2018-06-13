package com.daily.news.location;

import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;

/**
* 定位成功后上报位置
* @author zhengy
* create at 2018/5/24 上午9:27
**/
public class ReportAddressTask extends APIPostTask<Void> {

    public ReportAddressTask(LoadingCallBack<Void> callBack) {
        super(callBack);
    }

    @Override
    protected void onSetupParams(Object... params) {
            put("country", params[0]);
            put("province", params[1]);
            put("city", params[2]);


    }

    @Override
    protected String getApi() {
        return "/api/area/address_save";
    }

}
