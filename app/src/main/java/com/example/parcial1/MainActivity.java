package com.example.parcial1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    Button btnreiniciar;
    int posicion = 0;
    HashMap<String, Double> tasas = new HashMap<>();
    HashMap<String, Integer> banderas = new HashMap<>();
    static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MoneyShift");
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

        Button btn0 = findViewById(R.id.btn0);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);
        Button btnpunto = findViewById(R.id.btnpunto);
        Button btnatras = findViewById(R.id.btnatras);


        banderas.put("USD", R.drawable.flag_usa);
        banderas.put("EUR", R.drawable.flag_ue);
        banderas.put("GBP", R.drawable.flag_ru);
        banderas.put("JPY", R.drawable.flag_jp);
        banderas.put("COP", R.drawable.flag_col);

        tasas.put("USD", 1.0);
        tasas.put("EUR", 0.85);
        tasas.put("GBP", 0.73);
        tasas.put("JPY", 110.0);
        tasas.put("PAB", 1.0);

        String[] arrmoneda = getResources().getStringArray(R.array.arrmoneda);
        ArrayAdapter<String> adaptadormonedas = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrmoneda);
        sporigen.setAdapter((adaptadormonedas));
        spdestino.setAdapter((adaptadormonedas));

        // Botones numéricos y punto
        btn0.setOnClickListener(v -> txtconvertir.append("0"));
        btn1.setOnClickListener(v -> txtconvertir.append("1"));
        btn2.setOnClickListener(v -> txtconvertir.append("2"));
        btn3.setOnClickListener(v -> txtconvertir.append("3"));
        btn4.setOnClickListener(v -> txtconvertir.append("4"));
        btn5.setOnClickListener(v -> txtconvertir.append("5"));
        btn6.setOnClickListener(v -> txtconvertir.append("6"));
        btn7.setOnClickListener(v -> txtconvertir.append("7"));
        btn8.setOnClickListener(v -> txtconvertir.append("8"));
        btn9.setOnClickListener(v -> txtconvertir.append("9"));
        btnpunto.setOnClickListener(v -> txtconvertir.append("."));

        // Botón de retroceso
        btnatras.setOnClickListener(v -> {
            String texto = txtconvertir.getText().toString();
            if (!texto.isEmpty()) {
                txtconvertir.setText(texto.substring(0, texto.length() - 1));
                txtconvertir.setSelection(txtconvertir.getText().length());
            }
        });

        // Botón de punto validaciones
        btnpunto.setOnClickListener(v -> {
            validacionPunto();
        });


        sporigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                posicion = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        sporigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                String monedaOrigen = sporigen.getSelectedItem().toString();
                ImageView imgOrigen = findViewById(R.id.imgOrigen);

                if (banderas.containsKey(monedaOrigen)) {
                    imgOrigen.setImageResource(banderas.get(monedaOrigen));
                } else {
                    imgOrigen.setImageResource(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spdestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                String monedaDestino = spdestino.getSelectedItem().toString();
                ImageView imgbandera = findViewById(R.id.imageView);

                if (banderas.containsKey(monedaDestino)) {
                    imgbandera.setImageResource(banderas.get(monedaDestino));
                } else {
                    imgbandera.setImageResource(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnconvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String origen = sporigen.getSelectedItem().toString();
                String destino = spdestino.getSelectedItem().toString();

                if (txtconvertir.getText().toString().isEmpty()) {
                    Snackbar.make(v, getString(R.string.e_campo_vacio), Snackbar.LENGTH_LONG).show();
                } else if (origen.equals(destino)) {
                    Snackbar.make(v, getString(R.string.e_monedas_iguales), Snackbar.LENGTH_LONG).show();
                } else if (origen.equals(getString(R.string.seleccione)) || destino.equals(getString(R.string.seleccione))) {
                    Snackbar.make(v, getString(R.string.e_debe_seleccionar_divisa), Snackbar.LENGTH_LONG).show();
                } else {
                    convert2 pasar = new convert2(origen, destino, Double.parseDouble(txtconvertir.getText().toString()), tasas);
                    txtresult.setText(String.format("%.2f", pasar.convertidorcompleto()));
                    cambiocolor(pasar);
                }

            }
        });



        obtenerTasasAPI();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_salir) {
            mostrarDialogoSalir();
            return true;
        } else if (id == R.id.action_reiniciar) {
            reiniciarCampos();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogoSalir() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.titulo_salir))
                .setMessage(getString(R.string.mensaje_salir))
                .setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    private void reiniciarCampos() {
        txtconvertir.setText("");
        txtresult.setText("");
        sporigen.setSelection(0);
        spdestino.setSelection(0);
        ImageView imgbandera = findViewById(R.id.imageView);
        imgbandera.setImageResource(0);
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
            Toast.makeText(MainActivity.this, R.string.e_advert_monto_alto, Toast.LENGTH_SHORT).show();
        }
    }

    private void validacionPunto(){
        String texto = txtconvertir.getText().toString();


        if (texto.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.e_comenzar_punto, Toast.LENGTH_LONG).show();
            return;
        }


        if (texto.contains(".")) {
            Toast.makeText(MainActivity.this, R.string.e_tiene_punto, Toast.LENGTH_LONG).show();
            return;
        }


        if (texto.endsWith(".")) {
            Toast.makeText(MainActivity.this, R.string.e_termina_punto, Toast.LENGTH_LONG).show();
            return;
        }


        txtconvertir.append(".");
    }
}