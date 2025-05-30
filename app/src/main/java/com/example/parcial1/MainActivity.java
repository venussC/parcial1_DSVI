package com.example.parcial1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText txtconvertir;

    TextView txtresult;
    Button btnconvertir;

    Spinner sporigen, spdestino;

    int posicion=0;

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

        txtconvertir=findViewById(R.id.txtconvertir);

        txtresult=findViewById(R.id.txtresult);
        btnconvertir=findViewById(R.id.btnconvertir);
        sporigen=findViewById(R.id.sporigen);
        spdestino=findViewById(R.id.spdestino);

        tasas.put("USD", 1.0);
        tasas.put("EUR", 0.85);
        tasas.put("GBP", 0.73);
        tasas.put("JPY", 110.0);
        tasas.put("PAB", 1.0);

        String[] arrmoneda =getResources().getStringArray(R.array.arrmoneda);
        ArrayAdapter<String> adaptadormonedas= new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrmoneda);
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
convert2 pasar = new convert2(sporigen.getSelectedItem().toString(),spdestino.getSelectedItem().toString(),Double.parseDouble(txtconvertir.getText().toString()));
txtresult.setText(String.valueOf(pasar.convertidorcompleto()));


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});





    }
}