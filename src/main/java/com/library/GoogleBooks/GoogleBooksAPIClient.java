package com.library.GoogleBooks;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.library.config.APIConfig;

import static com.library.config.APIConfig.GOOGLE_BOOKS_API_KEY;

public class GoogleBooksAPIClient {

    //get book data
    public static JsonObject getBookData(String isbn) throws Exception {
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + GOOGLE_BOOKS_API_KEY;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Response API
        System.out.println("Response from API: " + response.body());

        // JSON
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        if (jsonObject.has("items")) {
            JsonArray items = jsonObject.getAsJsonArray("items");

            if (items.size() > 0) {
                JsonObject volumeInfo = items.get(0).getAsJsonObject().getAsJsonObject("volumeInfo");
                System.out.println("Volume Info: " + volumeInfo);
                return volumeInfo;
            } else {
                System.out.println("No items found for ISBN: " + isbn);
            }
        } else {
            System.out.println("No 'items' field found in API response for ISBN: " + isbn);
        }

        return null;
    }

    public static void main(String[] args) {
        try {
            String isbn = "9780967851462";
            JsonObject bookData = com.library.GoogleBooks.GoogleBooksAPIClient.getBookData(isbn);

            if (bookData != null) {
                String title = bookData.get("title").getAsString();
                String authors = bookData.has("authors") ? bookData.get("authors").getAsJsonArray().get(0).getAsString() : "Unknown";
                String publisher = bookData.has("publisher") ? bookData.get("publisher").getAsString() : "Unknown";
                String category = bookData.has("categories") ? bookData.get("categories").getAsJsonArray().get(0).getAsString() : "General";
                String publicationDate = bookData.has("publishedDate") ? bookData.get("publishedDate").getAsString() : "0";
                int publicationYear = 0;
                if (publicationDate.length() >= 4) {
                    publicationYear = Integer.parseInt(publicationDate.substring(0, 4));
                }
                // insert document from API
                AddDatabase.insertDocumentIntoDatabase(title, authors, publisher, isbn, category, publicationYear);
                System.out.println("Document inserted successfully.");
            } else {
                System.out.println("No document information found for ISBN: " + isbn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Add getBook for user dashboard!!!!!!
    public static JsonArray getBooks(String query) {
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query.replace(" ", "+") + "&key=" + GOOGLE_BOOKS_API_KEY;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("HTTP response code: " + responseCode);
            }

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            JsonObject response = JsonParser.parseString(inline.toString()).getAsJsonObject();
            return response.getAsJsonArray("items");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

