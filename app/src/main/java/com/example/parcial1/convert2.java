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
    }

    public convert2(String origen, String destino, double cantidad, HashMap<String, Double> tasasactualizadas) {
        this.origen = origen;
        this.destino = destino;
        this.cantidad = cantidad;
        this.tasas = tasasactualizadas;
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





