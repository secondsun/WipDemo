package org.feedhenry.mcp.prdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.aerogear.mobile.core.executor.AppExecutors;
import org.aerogear.mobile.core.http.HttpResponse;
import org.aerogear.mobile.keycloak_service_module.KeyCloakService;
import org.feedhenry.mcp.prdemo.service.EchoServiceModule;

public class NetworkDemo extends CoreActivity {
    private static final int RC_SIGN_IN = 0x123;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Button keycloakLogin;
    Button keycloakClear;
    Button secureEcho;
    Button insecureEcho;
    TextView output;

    KeyCloakService keycloak;

    private EchoServiceModule echoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_demo);
        bindViews();

        keycloak = KeyCloakService.create();
        echoService = new EchoServiceModule(this);
    }

    private void bindViews() {
        keycloakClear = findViewById(R.id.keycloak_clear);
        keycloakLogin = findViewById(R.id.keycloak_login);
        insecureEcho = findViewById(R.id.echo_insecure);
        secureEcho = findViewById(R.id.echo_secure);

        output = findViewById(R.id.output);

        keycloakLogin.setOnClickListener((view)->{
        });

        keycloakClear.setOnClickListener((view)->{
            keycloak.setAccessToken(null);
        });

        insecureEcho.setOnClickListener((view)->{
            echoService.echoInsecure(output);
        });


        secureEcho.setOnClickListener((view)->{
            echoService.echoSecure(output);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.

        }
    }

}
