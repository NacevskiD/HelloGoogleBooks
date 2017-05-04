package com.company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.URL;

/**
 * Created by admin on 5/3/17.
 */
public class GetBook {


    public static void searchBook(String searchQuery, BookGUI bookGUI) {

        new BookAPIWorker(searchQuery, bookGUI).execute();

    }

    private static class BookAPIWorker extends SwingWorker<Book, Void>{

        String query;
        BookGUI gui;

        public BookAPIWorker(String query, BookGUI bookGUI) {
            this.query = query;
            this.gui = bookGUI;
        }

        @Override
        protected Book doInBackground() throws Exception {

            String key = readKey();

            if (key == null) {
                System.out.println("Fix key error. Exiting program");
            }

            //Example URL for Minneapolis

            //http://api.wunderground.com/api/KEYGOESHERE/conditions/q/MN/Minneapolis.json

            String baseURL = "https://www.googleapis.com/books/v1/volumes?q=%s&key=%s";

            String url = String.format(baseURL, query, key);

            System.out.println(query);
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


            JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

            String title = volumeInfo.getString("title");
            String description = volumeInfo.getString("description");
            try {
                JSONArray authorList = volumeInfo.getJSONArray("authors");
            }catch (JSONException je) {
                System.out.println("No author(s) listed for this book.");
            }
            //todo figure out how to deal with authorList.

            // todo do you need ISBN?

            double googleRating = 0;
            try{
                googleRating = volumeInfo.getDouble("averageRating");
            }catch (JSONException je) {
                System.out.println("No rating provided for this book.");

            }

        Book firstBook = new Book(title, "author goes here", description, "isbn goes here", googleRating);


            // Example of looping over the array of results returned.
            for (int x = 0 ; x < items.length() ; x++) {
                JSONObject volumeInformation = items.getJSONObject(x).getJSONObject("volumeInfo");
                String thisTitle = volumeInformation.getString("title");
                String thisDescription = volumeInformation.getString("description");
                System.out.println(x + " " + thisTitle + " " + thisDescription);
            }


            return firstBook;
            //return title;

        }

        @Override
        public void done() {
            try {
                Book book = get();   // get() fetches whatever doInBackground returns.
                gui.firstResultIs(book);
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
