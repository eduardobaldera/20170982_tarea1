package edu.pucmm.eict;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Inserte un url valido. (Debe incluir http or https): ");
        Scanner scan = new Scanner(System.in);
        String url, rawDoc = "";
        Connection connection;
        Document parsedDoc = null;

        do {
            url = scan.nextLine();
            if (urlValido(url)) {
                System.out.println("URL valido. Parseando sitio web...\n");
            } else {
                System.out.println("URL invalido. Por favor inserte un URL valido (i.e.: https://google.com)\n");
            }
        } while (!urlValido(url));
        scan.close();

        try {
            connection = Jsoup.connect(url);
            parsedDoc = connection.get();
            rawDoc = connection.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // A) Indicar la cantidad de lineas del recurso retornado.
        int lineas = rawDoc.split("\n").length;
        mostrar('A', "Cantidad de lineas del recurso retornado: ", lineas);

        // B)  Indicar la cantidad de párrafos (p) que contiene el documento HTML.
        Elements pTags = parsedDoc.getElementsByTag("p");
        mostrar('B', "tags <p> encontrado: ", pTags.size());

        // C) Indicar la cantidad de imágenes (img) dentro de los párrafos que contiene el archivo HTML.
        int imgCounter = 0;
        for (Element tag: pTags) {
            imgCounter += tag.getElementsByTag("img").size();
        }
        mostrar('C', "tags <img> dentro de tags <p>: ", imgCounter);

        // D) indicar la cantidad de formularios (form) que contiene el HTML por
        //categorizando por el método implementado POST o GET.
        mostrar('D', "<form> tags en GET: ", getForm(parsedDoc, "GET"));
        mostrar('D', "<form> tags en POST: ", getForm(parsedDoc, "POST"));

        // E) Para cada formulario mostrar los campos del tipo input y su respectivo tipo que contiene en el documento HTML.
        //Section E
        System.out.println("=========== OPERACION E ===========");
        Elements formTags = parsedDoc.getElementsByTag("form");
        ArrayList<Elements> inputs = new ArrayList<>();
        for (Element form : formTags) {
            inputs.add(form.getElementsByTag("input"));
        }
        System.out.println("<input> tags en cada form: ");
        mostrarInputs(inputs);

        //
        System.out.println("=========== OPERACION F ===========");
        request(parsedDoc, url);

    }

    private static boolean urlValido (String url) {
        String[] schemes = {"http", "https"};
        if (url.startsWith(schemes[0]) || url.startsWith(schemes[1])) {
            return true;
        }
        return false;
    }

    private static void mostrar(char letra, String titulo, int cantidad) {
        System.out.println("=========== OPERACION " + letra + " ===========");
        System.out.println(titulo + cantidad);
        System.out.println("_________________________________");
        return;
    }

    private static int getForm(Document documento, String metodo){
        Elements forms = documento.select("form");
        int cantidadForms = 0;
        for(Element form: forms) {
            if(form.attr("method").equalsIgnoreCase(metodo))
                cantidadForms++;
        }
        return cantidadForms;
    }

    private static void mostrarInputs(ArrayList<Elements> inputs) {
        int cant = 0;
        for (Elements items : inputs) {
            System.out.println("Form #" + cant + ": ");
            for (Element item : items) {
                String type = item.attr("type");
                System.out.println("Tipo: " + type);
            }
            System.out.println("=================================\n");
            cant++;
        }
        return;
    }

    private static void request(Document documento, String url){
        Elements forms = documento.select("form");

        for(Element form : forms) {
            if(form.attr("method").equalsIgnoreCase("post")){
                String urlToPost = formatoURL(form, url);
                System.out.println("URL usado para el post " + urlToPost);
                try{
                    Document respuesta = Jsoup.connect(urlToPost)
                            .data("asignatura", "practica1")
                            .header("matricula", "20170982")
                            .post();

                    System.out.println(respuesta);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static String formatoURL(Element form, String url) {
        String formattedURL = url.substring(0, url.substring(8).indexOf("/") + 8) + form.attr("action");

        return form.attr("action").contains("http")
                ? form.attr("action")
                : formattedURL;
    }



}