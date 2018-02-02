package org.feedhenry.mcp.prdemo.service;

import android.content.Context;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.aerogear.mobile.core.MobileCore;
import org.aerogear.mobile.core.ServiceModule;
import org.aerogear.mobile.core.executor.AppExecutors;
import org.aerogear.mobile.core.http.HttpRequest;
import org.aerogear.mobile.core.http.HttpResponse;
import org.aerogear.mobile.core.http.HttpServiceModule;
import org.aerogear.mobile.core.http.OkHttpServiceModule;
import org.aerogear.mobile.keycloak_service_module.KeyCloakService;

public class EchoServiceModule implements ServiceModule {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private KeyCloakService keycloak;
    private HttpServiceModule http;
    private String url;

    public EchoServiceModule(Context context) {
//        this.keycloak = KeyCloakService.getInstance(context);
//        this.http = OkHttpServiceModule.getInstance(context);
//        this.url = MobileCore.getInstance(context).getConfig("echo-service").getUri();
    }

    public void echoInsecure(TextView output) {
        HttpRequest request = http.newRequest();
        request.get(url + "/echo");

        HttpResponse response = request.execute();
        response.onComplete(() -> {
            String responseText = response.stringBody();
            response.waitForCompletionAndClose();
            new AppExecutors().mainThread().execute(() -> {
                output.setText(prettyPrint(responseText));
            });
        });

    }

    public void echoSecure(TextView output) {
        HttpRequest request = http.newRequest();
        keycloak.addBearerToken(request);
        request.get(url + "/secure");

        HttpResponse response = request.execute();
        response.onComplete(() -> {
            String responseText = response.stringBody();
            int status = response.getStatus();
            response.waitForCompletionAndClose();
            new AppExecutors().mainThread().execute(() -> {
                if (status == 200) {
                    output.setText(prettyPrint(responseText));
                } else {
                    output.setText("Unauthorized");
                }
            });
        });

    }

    private String prettyPrint(String text) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(text).getAsJsonObject();
        return gson.toJson(json).toString();
    }

}
