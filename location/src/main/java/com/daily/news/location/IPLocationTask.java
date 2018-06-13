package com.daily.news.location;

import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;

/**
 * ip location task
 *
 * @author a_liYa
 * @date 2017/9/4 10:40.
 */
public class IPLocationTask extends APIGetTask<DataLocation> {

    public IPLocationTask(LoadingCallBack<DataLocation> mCallBack) {
        super(mCallBack);
    }

    @Override
    protected void onSetupParams(Object... params) {
    }

    @Override
    protected String getApi() {
        return "/api/area/location";
    }

}
