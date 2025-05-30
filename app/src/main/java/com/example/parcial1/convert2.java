package com.example.parcial1;

import java.util.HashMap;

public class convert2 {

        private HashMap<String, Double> tasas;

        private String origen, destino;

        private double cantidad;

        public convert2() {
        }


    public convert2(String origen, String destino, double cantidad) {
        this.origen = origen;
        this.destino = destino;
        this.cantidad = cantidad;


        tasas = new HashMap<>();
        tasas.put("USD", 1.0);
        tasas.put("EUR", 0.85);
        tasas.put("GBP", 0.73);
        tasas.put("JPY", 110.0);
        tasas.put("PAB", 1.0);
    }




    public convert2(String origen, double cantidad) {
        this.origen = origen;
        this.cantidad = cantidad;
    }

    public  double usdconvert(){


            return this.cantidad/tasas.get(this.origen);
        }

        public double convertidorcompleto(){

            double pasausd = usdconvert();
            double result = pasausd*tasas.get(this.destino);
           return result;


        }




}
