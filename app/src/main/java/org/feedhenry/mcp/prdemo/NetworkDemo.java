package org.feedhenry.mcp.prdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
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

    private GoogleSignInClient mGoogleSignInClient;
    private EchoServiceModule echoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_demo);
        bindViews();

        keycloak = KeyCloakService.getInstance(this);
        echoService = new EchoServiceModule(this);
    }

    private void bindViews() {
        keycloakClear = findViewById(R.id.keycloak_clear);
        keycloakLogin = findViewById(R.id.keycloak_login);
        insecureEcho = findViewById(R.id.echo_insecure);
        secureEcho = findViewById(R.id.echo_secure);

        output = findViewById(R.id.output);

        keycloakLogin.setOnClickListener((view)->{
            startExchange();
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

    private void startExchange() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(new Scope(Scopes.PROFILE), new Scope(Scopes.PLUS_ME), new Scope(Scopes.EMAIL))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        return;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                exchangeTokens(account);
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void exchangeTokens(GoogleSignInAccount account) {

        String token = account.getIdToken();
        final HttpResponse response = keycloak.exchangeToken(token);
        response.onComplete(() -> {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(response.stringBody()).getAsJsonObject();
            response.waitForCompletionAndClose();
            keycloak.setAccessToken(json.get("access_token").getAsString());
            new AppExecutors().mainThread().execute(() -> {
                output.setText(gson.toJson(json).toString());
            });

        });

    }

}
