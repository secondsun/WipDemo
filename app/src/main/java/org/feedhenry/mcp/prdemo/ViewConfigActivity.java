package org.feedhenry.mcp.prdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aerogear.mobile.core.MobileCore;
import org.aerogear.mobile.core.configuration.ServiceConfiguration;

public class ViewConfigActivity extends CoreActivity {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner serviceSpinner = findViewById(R.id.serviceSpinner);
        TextView configTextView = findViewById(R.id.configText);

        //Load values from core
        String[] names  = new String[]{"keycloak","echo-service"};
        serviceSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, names));
        serviceSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Load values from core
                        ServiceConfiguration config = core.getConfig(names[position]);
                        configTextView.setText(gson.toJson(config));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        configTextView.setText(" ¯\\_(ツ)_/¯");
                    }
                }
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
