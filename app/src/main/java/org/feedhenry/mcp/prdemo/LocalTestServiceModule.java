package org.feedhenry.mcp.prdemo;

import android.util.Log;

import org.aerogear.mobile.core.MobileCore;
import org.aerogear.mobile.core.ServiceModule;
import org.aerogear.mobile.core.configuration.ServiceConfiguration;

class LocalTestServiceModule implements ServiceModule {
    @Override
    public void bootstrap(MobileCore core, ServiceConfiguration configuration, Object... args) {
        Log.i("LocalService", configuration.toString());
    }
}
