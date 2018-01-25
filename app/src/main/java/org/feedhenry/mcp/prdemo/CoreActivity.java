package org.feedhenry.mcp.prdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.aerogear.mobile.core.MobileCore;

public abstract class CoreActivity extends AppCompatActivity {

    protected MobileCore core;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.core = MobileCore.getInstance(this);
    }

}
