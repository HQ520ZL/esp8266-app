package com.league55.esp8266.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.league55.esp8266.R;
import com.league55.esp8266.models.LEDColor;
import com.league55.esp8266.models.LEDOperation;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RadioGroup operationsRadioGroup;
    private RadioGroup colorRadioGroup;
    private Button submitButton;


    private LEDOperation operation;
    private LEDColor color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operationsRadioGroup = (RadioGroup) findViewById(R.id.operationsRadioGroup);
        colorRadioGroup = (RadioGroup) findViewById(R.id.colorRadioGroup);
        initRadioGroups();

        submitButton = (Button) findViewById(R.id.buttonSendRequest);
        submitButton.setOnClickListener(submitOnClickListener);

    }

    private void initRadioGroups() {
        if(operationsRadioGroup.getCheckedRadioButtonId() == -1) {
            operationsRadioGroup.check(R.id.radioUP);
        }
        if(colorRadioGroup.getCheckedRadioButtonId() == -1) {
            colorRadioGroup.check(R.id.buttonBLUE);
        }
    }

    private View.OnClickListener submitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            refreshParams();
            doPost();
        }

        private void refreshParams() {
            color = getLEDColor(colorRadioGroup.getCheckedRadioButtonId());
            operation = getLEDOperation(operationsRadioGroup.getCheckedRadioButtonId());
        }

        private LEDOperation getLEDOperation(int id) {
            switch (id) {
                case R.id.radioDOWN:
                    return LEDOperation.DOWN;
                case R.id.radioUP:
                    return LEDOperation.UP;
                default: throw new IllegalArgumentException("Wrong Radio Button Id, no such operation");
            }
        }

        private LEDColor getLEDColor(int checkedRadioButtonId) {
            switch (checkedRadioButtonId) {
                case R.id.buttonBLUE:
                    return LEDColor.BLUE;
                case R.id.buttonGREEN:
                    return LEDColor.GREEN;
                case R.id.buttonRED:
                    return LEDColor.RED;
                default: throw new IllegalArgumentException("Wrong Radio Button Id, no such color");
            }
        }
    };

    private void doPost() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.4.1/LED";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Success!", Toast.LENGTH_SHORT);
                        toast.show();                     }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Error occurred!", Toast.LENGTH_SHORT);
                toast.show();

                Toast toast2 = Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_SHORT);
                toast2.show();}
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("OPERATION", operation.toString());
                params.put("LED_COLOR", color.getColorCode()+ "");
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
