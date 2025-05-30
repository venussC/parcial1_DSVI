package com.example.parcial1;



import java.util.HashMap;

public class convert {
    //private HashMap<String, Double> tasas;
    //public void lastasas () {
        //tasas = new HashMap<>();
        //tasas.put("USD", 1.0);
       // tasas.put("EUR", 0.85);
       // tasas.put("GBP", 0.73);
      //  tasas.put("JPY", 110.0);
       // tasas.put("COP", 3900.0);
    //}

    private double usd=1.0;
    private double eur =0.85;
    private double gbp = 0.73;
    private double jpy = 110.0;

    private double cop = 3900.0;

    private double pasarausd;

    private String origen, destino;

    private double cantidad;

    public convert() {
    }

    public convert(double pasarausd, String origen, String destino, double cantidad) {
        this.pasarausd = pasarausd;
        this.origen = origen;
        this.destino = destino;
        this.cantidad = cantidad;
    }

    public  double usdconvert(){


        return  this.pasarausd=this.cantidad/usd;
    }


}
