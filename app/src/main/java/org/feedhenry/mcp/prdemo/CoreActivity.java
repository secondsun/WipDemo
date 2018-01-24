package org.feedhenry.mcp.prdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.aerogear.mobile.core.MobileCore;
import org.aerogear.mobile.core.ServiceModuleRegistry;
import org.aerogear.mobile.keycloak_service_module.KeyCloakService;
import org.feedhenry.mcp.prdemo.service.EchoServiceModule;

public abstract class CoreActivity extends AppCompatActivity {


    static {
        ServiceModuleRegistry.getInstance().registerServiceModule("keycloak", KeyCloakService.class, "http");
        ServiceModuleRegistry.getInstance().registerServiceModule("echo-service", EchoServiceModule.class, "keycloak", "http");
    }

    protected MobileCore core;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.core = new MobileCore.Builder(this).build();

    }
}
