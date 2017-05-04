package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.URL;

/**
 * Created by admin on 5/3/17.
 */
public class GetBook {


    public static void getTitleByISBN(String isbn, BookGUI bookGUI) {

        new BookAPIWorker(isbn, bookGUI).execute();

    }

    private static class BookAPIWorker extends SwingWorker<String, Void>{

        String isbn;
        BookGUI gui;

        public BookAPIWorker(String isbn, BookGUI bookGUI) {
            this.isbn = isbn;
            this.gui = bookGUI;
        }

        @Override
        protected String doInBackground() throws Exception {

            String key = readKey();

            if (key == null) {
                System.out.println("Fix key error. Exiting program");
            }

            //Example URL for Minneapolis

            //http://api.wunderground.com/api/KEYGOESHERE/conditions/q/MN/Minneapolis.json

            String baseURL = "https://www.googleapis.com/books/v1/volumes?q=%s&key=%s";

            String url = String.format(baseURL, isbn, key);

            System.out.println(isbn);
            System.out.println(url);

            InputStream stream = new URL(url).openConnection().getInputStream();

            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            // Read stream into String. Use StringBuilder to put multiple lines together.
            // Read lines in a loop until the end of the stream.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            //and turn the StringBuilder into a String.
            String responseString = builder.toString();

            System.out.println(responseString);

            // JSON processing here

            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray items = jsonObject.getJSONArray("items");

            for (int x = 0 ; x < items.length() ; x++) {
                JSONObject volumeInfo = items.getJSONObject(x).getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                String des = volumeInfo.getString("description");
                System.out.println(title + " " + des);
            }

            JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

            String title = volumeInfo.getString("title");
            String des = volumeInfo.getString("description");

            System.out.println(title);
//            String description = items.getJSONObject(0).getJSONObject("volumeInfo").getString("description");
//            System.out.println(description);
//
            return title;

        }

        @Override
        public void done() {
            try {
                String title = get();   // get() fetches whatever doInBackground returns.
                gui.titleFetched(title);
            } catch (Exception e) {
                System.out.println(e);
            }
        }


    }


    private static String readKey() {

        try {
            BufferedReader reader = new BufferedReader(new FileReader("key.txt"));
            String key = reader.readLine();
            if (key == null) {
                System.out.println("Key not found in file. Paste your Weather Underground key as the first line of key.txt");
                return null;
            }

            return key;

        } catch (IOException ioe) {
            System.out.println("Key file not found. Please provide a file called key.txt in the root directory of the project");
            System.exit(-1);
            return null;
        }
    }

}
