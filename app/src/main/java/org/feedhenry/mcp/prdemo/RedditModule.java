package org.feedhenry.mcp.prdemo;

import android.util.Log;

import com.google.gson.Gson;

import org.aerogear.mobile.core.MobileCore;
import org.aerogear.mobile.core.ServiceModule;
import org.aerogear.mobile.core.configuration.ServiceConfiguration;
import org.aerogear.mobile.core.http.HttpServiceModule;

import java.util.ArrayList;
import java.util.List;

class RedditModule implements ServiceModule {

    private HttpServiceModule http;

    private final String url = "http://www.mocky.io/v2/5a5f74172e00006e260a8476";

    @Override
    public void bootstrap(MobileCore core, ServiceConfiguration configuration, Object... args) {
        Log.i("RedditModule", configuration.toString());
        this.http = (HttpServiceModule) core.getService("http");
    }

    public List<Story> getFrontPage() {
        Gson gson = new Gson();
        String response = http.get(url);
        List<Story> toReturn = new ArrayList<>();
        toReturn.add(gson.fromJson(response, Story.class));
        return toReturn;
    }

}
