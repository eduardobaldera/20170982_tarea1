package edu.pucmm.eict;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import java.io.IOException;
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
                System.out.println("URL valido." + "\u2713"+ " Parsing Website...\n");
            } else {
                System.out.println("URL invalido" + "\u26A0" + " Por favor inserte un URL valido (i.e.: https://google.com)\n");
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
    }

    private static boolean urlValido (String url) {
        String[] schemes = {"http", "https"};
        if (url.startsWith(schemes[0]) || url.startsWith(schemes[1])) {
            return true;
        }
        return false;
    }
}