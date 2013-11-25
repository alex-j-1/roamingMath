package dajohnson89;

import java.io.IOException;
import java.net.URL;

/**
 * Main point of entry for the roaming-math challenge
 */
public class App 
{
    public static void main(String[] args) {
        try {
            URL url = new URL("http://www.java2s.com/hi");
            System.out.println("URL is " + url.toString());
            System.out.println("authority is " + url.getAuthority());
            System.out.println("path is " + url.getPath());
            System.out.println("default port is " + url.getDefaultPort());
            System.out.println("query is " + url.getQuery());
            System.out.println("ref is " + url.getRef());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
