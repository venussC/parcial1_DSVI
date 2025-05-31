package com.example.parcial1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    EditText txtconvertir;
    TextView txtresult;
    Button btnconvertir;
    Spinner sporigen, spdestino;

    int posicion = 0;
    HashMap<String, Double> tasas = new HashMap<>();
    static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtconvertir = findViewById(R.id.txtconvertir);
        txtresult = findViewById(R.id.txtresult);
        btnconvertir = findViewById(R.id.btnconvertir);
        sporigen = findViewById(R.id.sporigen);
        spdestino = findViewById(R.id.spdestino);

        tasas.put("USD", 1.0);
        tasas.put("EUR", 0.85);
        tasas.put("GBP", 0.73);
        tasas.put("JPY", 110.0);
        tasas.put("PAB", 1.0);

        String[] arrmoneda = getResources().getStringArray(R.array.arrmoneda);
        ArrayAdapter<String> adaptadormonedas = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrmoneda);
        sporigen.setAdapter((adaptadormonedas));
        spdestino.setAdapter((adaptadormonedas));

        sporigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                posicion = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spdestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                posicion = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnconvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtconvertir.getText().toString().equals("")) {
                    Snackbar.make(v, getString(R.string.e_campo_vacio), Snackbar.LENGTH_LONG).show();

                }else if (sporigen.getSelectedItem().toString().equals(spdestino.getSelectedItem().toString())) {
                    Snackbar.make(v, getString(R.string.e_monedas_iguales), Snackbar.LENGTH_LONG).show();
                } else{
                    convert2 pasar = new convert2(sporigen.getSelectedItem().toString(), spdestino.getSelectedItem().toString(), Double.parseDouble(txtconvertir.getText().toString()), tasas);
                    txtresult.setText(String.format("%.2f", pasar.convertidorcompleto()));
                    cambiocolor(pasar);
                }

            }
        });


        obtenerTasasAPI();
    }

    private void obtenerTasasAPI() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(API_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    reader.close();
                    inputStream.close();
                    connection.disconnect();

                    String jsonResult = result.toString();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonResponse = new JSONObject(jsonResult);
                                JSONObject rates = jsonResponse.getJSONObject("rates");

                                tasas.clear();
                                tasas.put("USD", 1.0);

                                Iterator<String> keys = rates.keys();
                                while (keys.hasNext()) {
                                    String currency = keys.next();
                                    double rate = rates.getDouble(currency);
                                    tasas.put(currency, rate);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


    private void cambiocolor(convert2 pasar){
        double resultado = pasar.convertidorcompleto();
        if(resultado < 100){
            txtresult.setTextColor(Color.GREEN);
        } else if (resultado <= 1000) {
            txtresult.setTextColor(Color.BLUE);
        }else{
            txtresult.setTextColor(Color.RED);
        }
        if (resultado > 10000){
            Toast.makeText(MainActivity.this, getString(R.string.e_advert_monto_alto), Toast.LENGTH_SHORT).show();
        }
    }
}