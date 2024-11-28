//package com.library.api;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.library.config.APIConfig;
//import com.library.dao.DocumentDao;
//import com.library.models.Author;
//import com.library.models.Category;
//import com.library.models.Document;
//import com.library.models.Publisher;
//import okhttp3.*;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GoogleBooksAPIClient {
//    private static final int limit=100;
//    private static final long time=600; //one min to ms
//    private List<Long> requestTimestamp=new ArrayList<>();
//
//    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn%s&key=%s";
//
//    private final OkHttpClient client;
//    //parse json response into java object
//    private final Gson gson;
//    private DocumentDao documentDao;
//
//
//    public GoogleBooksAPIClient() {
//        this.client = new OkHttpClient();
//        this.gson = new Gson();
//        this.documentDao = new DocumentDao();
//    }
//
//    // check document
//    public boolean isBookAvailable(String term) {
//        String url = String.format(API_URL, term, APIConfig.GOOGLE_BOOKS_API_KEY);
//
//        // request
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                String jsonData = response.body().string();
//                JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
//
//                JsonArray items = jsonObject.getAsJsonArray("items");
//                return items != null && items.size() > 0;
//            } else {
//                System.out.println("Error: " + response.code() + " - " + response.message());
//                return false;
//            }
//        } catch (IOException e) {
//            System.err.println("Request failed: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//    //get book data, minh nen search bang ten sach vi viec collect isbn kho hon so voi viec collect ten sach
//    //to khong de static duoc vi requestTimestamp nonstatic
//    public void getBookData(String term, int quantity) throws Exception {
//
//        // Document already exists
//        if (DocumentDao.searchByIsbn(term) != null && !isBookAvailable(term)) {
//            return;
//        }
//
//        //solve call limit per minute, tai vi limit 100 calls/min
//        long currentTime = System.currentTimeMillis();
//        requestTimestamp.removeIf(timestamp -> currentTime - timestamp > 60000);
//
//        if (requestTimestamp.size() >= limit) {
//            long wait = time - (currentTime - requestTimestamp.get(0));
//            if (wait > 0) {
//                try {
//                    Thread.sleep(wait);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        String url = String.format(API_URL, term, APIConfig.GOOGLE_BOOKS_API_KEY);
////        HttpClient client = HttpClient.newHttpClient();
////        HttpRequest request = HttpRequest.newBuilder()
////                .uri(URI.create(API_URL))
////                .build();
//
//        //okhttp nhanh hon
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.err.println("Request failed: " + e.getMessage());
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String jsonData = response.body().string();
//                    printApiResponse(jsonData);
//                    parseBooks(jsonData, quantity);
//                } else {
//                    String errorBody = response.body().string(); //read the error body for more details
//                    System.out.println("Error: " + response.code() + " - " + response.message());
//                    System.out.println("Error Body: " + errorBody); //print the error message from api
//                }
//            }
//        });
//    }
//
//    private void printApiResponse(String jsonData) {
//        System.out.println("api response:");
//        System.out.println(jsonData);
//        System.out.println("--------------------------");
//
//    }
//
//
//
//    private void parseBooks(String jsonData, int quantity) {
//        //convert json string into a json object
//        //to interact with the data
//        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
//        JsonArray items = jsonObject.getAsJsonArray("items");
//
//        if (items != null) {
//            for (int i = 0; i < items.size(); i++) {
//                JsonObject book = items.get(i).getAsJsonObject();
//                // volumeInfo object contains book details like title, ISBN...
//                JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");
//
//                String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "N/A";
//                String subtitle = volumeInfo.has("subtitle") ? volumeInfo.get("subtitle").getAsString() : "";
//                String isbn = extractISBN(volumeInfo);
//                String authorName = volumeInfo.has("authors") ? volumeInfo.get("authors").getAsJsonArray().get(0).getAsString() : "Unknown";
//                String publisherName = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Unknown";
//                String categoryName = volumeInfo.has("categories") ? volumeInfo.get("categories").getAsJsonArray().get(0).getAsString() : "General";
//                int publicationYear = volumeInfo.has("publishedDate") ? Integer.parseInt(volumeInfo.get("publishedDate").getAsString().substring(0, 4)) : 0;
//                String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "N/A";
//
//                int pages = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").getAsInt() : 0;
//
//                String imageLink = "N/A";
//                if (volumeInfo.has("imageLinks")) {
//                    JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
//                    imageLink = imageLinks.has("thumbnail") ? imageLinks.get("thumbnail").getAsString() : "N/A";
//                }
//
//                //trong accessInfo co preview link
//                JsonObject accessInfo = book.has("accessInfo") ? book.getAsJsonObject("accessInfo") : null;
//                String checkViewability = accessInfo != null && accessInfo.has("viewability") ? accessInfo.get("viewability").getAsString() : "N/A";
//
//                String previewLink;
//                if (accessInfo == null || checkViewability.equals("NO_PAGES")) {
//                    previewLink = "There is no preview for this book.";
//                } else {
//                    previewLink = volumeInfo.has("previewLink") ? volumeInfo.get("previewLink").getAsString() : "N/A";
//                }
//
//                Document doc = new Document();
//                doc.setTitle(title + " " + subtitle);
//                doc.setISBN(isbn);
//                doc.setPublicationYear(publicationYear);
//                doc.setDescription(description);
//                doc.setPreviewLink(previewLink);
//                doc.setPage(pages);
//                doc.setQuantity(quantity);
//                doc.setImageLink(imageLink);  //set imageLink in document
////
////                if ("N/A".equals(title)) {
////                    printApiResponse(jsonData);
////                    doc.setTitle("N/A");
////                }
//                if (!"Unknown".equals(categoryName)) {
//                    Category category = new Category();
//                    category.setName(categoryName);
//                    doc.setCategory(category);
//                }
//                else {
//                    Category category = new Category();
//                    category.setName("N/A");
//                    doc.setCategory(category);
//                }
//
//                if (!"N/A".equals(authorName)) {
//                    Author author = new Author();
//                    author.setName(authorName);
//                    doc.setAuthor(author);
//                }
//                else {
//                    Author author = new Author();
//                    author.setName("N/A");
//                    doc.setAuthor(author);
//                }
//
//                if(!"N/A".equals(publisherName)) {
//                    Publisher pub = new Publisher();
//                    pub.setName(publisherName);
//                    doc.setPublisher(pub);
//                }
//                else {
//                    Publisher pub = new Publisher();
//                    pub.setName("N/A");
//                    doc.setPublisher(pub);
//                }
//
//                //insert into the database (fetching or creating the id for author, publisher, and category)
//                insertDocumentIntoDatabase(doc,authorName, publisherName, categoryName);
//            }
//        } else {
//            System.out.println("No books found for the term.");
//        }
//    }
//
//
//    private String extractISBN(JsonObject volumeInfo) {
//        //ISBN is stored under the industryIdentifiers field so we have this if condition to check
//        //try print response to see its structure
//        if (volumeInfo.has("industryIdentifiers")) {
//            JsonArray identifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
//            if (identifiers != null) {
//                for (int j = 0; j < identifiers.size(); j++) {
//                    JsonObject identifier = identifiers.get(j).getAsJsonObject();
//                    //extract the first isbn we found
//                    if (identifier.has("type") && "ISBN_13".equals(identifier.get("type").getAsString())) {
//                        return identifier.get("identifier").getAsString();
//                    }
//                }
//            }
//
//
//        }
//        return "N/A"; //if ISBN is not found
//    }
//
//    //use for extract first author and category, cuz idk how can we store multi
//    private String getFirstElement(JsonObject volumeInfo, String key) {
//        if (volumeInfo.has(key)) {
//            JsonArray array = volumeInfo.getAsJsonArray(key);
//            if (array.size() > 0) {
//                return array.get(0).getAsString();
//            }
//        }
//        return "N/A";
//    }
//
//    private void insertDocumentIntoDatabase(Document doc, String authorName, String publisherName, String categoryName) {
//        try {
//            //get or create the authorid
//            int authorId = documentDao.getOrCreateAuthorId(authorName);
//            doc.setAuthorId(authorId);
//
//            //get or create the publisherid
//            int publisherId = documentDao.getOrCreatePublisherId(publisherName);
//            doc.setPublisherId(publisherId);
//
//            //get or create the categoryid
//            int categoryId = documentDao.getOrCreateCategoryId(categoryName);
//            doc.setCategoryId(categoryId);
//
//            documentDao.add(doc);
//            System.out.println("Book inserted successfully");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        String file = "src/main/java/com/library/api/programming_books.txt";
//        GoogleBooksAPIClient newClient = new GoogleBooksAPIClient();
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String isbn;
//            int quantity = 10;
//            while ((isbn = br.readLine()) != null) {
//                if (!isbn.trim().isEmpty()) newClient.getBookData(isbn.trim(), quantity);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
package com.library.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.config.APIConfig;
import com.library.dao.DocumentDao;
import com.library.models.Author;
import com.library.models.Category;
import com.library.models.Document;
import com.library.models.Publisher;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class GoogleBooksAPIClient {
    private static final int LIMIT = 100; // API call limit per minute
    private static final long TIME_WINDOW_MS = 60000; // Time window in ms
    private final Queue<Long> requestTimestamps = new LinkedList<>();

    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn%s&key=%s";

    private final OkHttpClient client;
    private final Gson gson;
    private final DocumentDao documentDao;

    public GoogleBooksAPIClient() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
        this.documentDao = new DocumentDao();
    }

    // Ensure API rate limit is respected
    private void handleApiRateLimit() {
        long currentTime = System.currentTimeMillis();
        synchronized (requestTimestamps) {
            while (!requestTimestamps.isEmpty() && currentTime - requestTimestamps.peek() > TIME_WINDOW_MS) {
                requestTimestamps.poll();
            }
            if (requestTimestamps.size() >= LIMIT) {
                long waitTime = TIME_WINDOW_MS - (currentTime - requestTimestamps.peek());
                try {
                    System.out.printf("Rate limit reached, waiting %d ms...\n", waitTime);
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            requestTimestamps.add(currentTime);
        }
    }

    // Check if a book is available using its ISBN
    public boolean isBookAvailable(String isbn) {
        String url = String.format(API_URL, isbn, APIConfig.GOOGLE_BOOKS_API_KEY);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
                JsonArray items = jsonObject.getAsJsonArray("items");
                return items != null && items.size() > 0;
            } else {
                System.out.println("Error: " + response.code() + " - " + response.message());
                return false;
            }
        } catch (IOException e) {
            System.err.println("Request failed: " + e.getMessage());
            return false;
        }
    }

    // Get book data and insert it into the database
    public void getBookData(String isbn, int quantity) {
        try {
            if (DocumentDao.searchByIsbn(isbn) != null) {
                System.out.printf("Book with ISBN %s already exists in the database.\n", isbn);
                return;
            }

            handleApiRateLimit();

            String url = String.format(API_URL, isbn, APIConfig.GOOGLE_BOOKS_API_KEY);
            Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.err.printf("Request failed for ISBN %s: %s\n", isbn, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonData = response.body().string();
                        parseBooks(jsonData, quantity);
                    } else {
                        System.out.printf("Error for ISBN %s: %s - %s\n", isbn, response.code(), response.message());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseBooks(String jsonData, int quantity) {
        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
        JsonArray items = jsonObject.getAsJsonArray("items");

        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                JsonObject book = items.get(i).getAsJsonObject();
                JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");

                String title = getStringOrDefault(volumeInfo, "title", "N/A");
                String subtitle = getStringOrDefault(volumeInfo, "subtitle", "");
                String isbn = extractISBN(volumeInfo);
                String authorName = getFirstElement(volumeInfo, "authors");
                String publisherName = getStringOrDefault(volumeInfo, "publisher", "Unknown");
                String categoryName = getFirstElement(volumeInfo, "categories");
                int publicationYear = extractPublicationYear(volumeInfo);
                String description = getStringOrDefault(volumeInfo, "description", "N/A");
                int pages = getIntOrDefault(volumeInfo, "pageCount", 0);
                String imageLink = extractImageLink(volumeInfo);
                String previewLink = extractPreviewLink(book);

                Document doc = new Document();
                doc.setTitle(title + " " + subtitle);
                doc.setISBN(isbn);
                doc.setPublicationYear(publicationYear);
                doc.setDescription(description);
                doc.setPreviewLink(previewLink);
                doc.setPage(pages);
                doc.setQuantity(quantity);
                doc.setImageLink(imageLink);

                if (!"N/A".equals(categoryName)) {
                    Category category = new Category();
                    category.setName(categoryName);
                    doc.setCategory(category);
                }

                if (!"Unknown".equals(authorName)) {
                    Author author = new Author();
                    author.setName(authorName);
                    doc.setAuthor(author);
                }

                if (!"Unknown".equals(publisherName)) {
                    Publisher publisher = new Publisher();
                    publisher.setName(publisherName);
                    doc.setPublisher(publisher);
                }

                insertDocumentIntoDatabase(doc, authorName, publisherName, categoryName);
            }
        } else {
            System.out.println("No books found for the provided ISBN.");
        }
    }

    private String extractISBN(JsonObject volumeInfo) {
        if (volumeInfo.has("industryIdentifiers")) {
            JsonArray identifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
            for (JsonElement element : identifiers) {
                JsonObject identifier = element.getAsJsonObject();
                if ("ISBN_13".equals(getStringOrDefault(identifier, "type", ""))) {
                    return getStringOrDefault(identifier, "identifier", "N/A");
                }
            }
        }
        return "N/A";
    }

    private int extractPublicationYear(JsonObject volumeInfo) {
        if (volumeInfo.has("publishedDate")) {
            String date = volumeInfo.get("publishedDate").getAsString();
            if (date.length() >= 4) {
                return Integer.parseInt(date.substring(0, 4));
            }
        }
        return 0;
    }

    private String extractImageLink(JsonObject volumeInfo) {
        if (volumeInfo.has("imageLinks")) {
            JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
            return getStringOrDefault(imageLinks, "thumbnail", "N/A");
        }
        return "N/A";
    }

    private String extractPreviewLink(JsonObject book) {
        JsonObject accessInfo = book.getAsJsonObject("accessInfo");
        String viewability = getStringOrDefault(accessInfo, "viewability", "N/A");
        if ("NO_PAGES".equals(viewability)) {
            return "No preview available.";
        }
        return getStringOrDefault(accessInfo, "previewLink", "N/A");
    }

    private void insertDocumentIntoDatabase(Document doc, String authorName, String publisherName, String categoryName) {
        try {
            int authorId = documentDao.getOrCreateAuthorId(authorName);
            doc.setAuthorId(authorId);

            int publisherId = documentDao.getOrCreatePublisherId(publisherName);
            doc.setPublisherId(publisherId);

            int categoryId = documentDao.getOrCreateCategoryId(categoryName);
            doc.setCategoryId(categoryId);

            documentDao.add(doc);
            System.out.println("Book inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getStringOrDefault(JsonObject obj, String key, String defaultValue) {
        return obj.has(key) ? obj.get(key).getAsString() : defaultValue;
    }

    private int getIntOrDefault(JsonObject obj, String key, int defaultValue) {
        return obj.has(key) ? obj.get(key).getAsInt() : defaultValue;
    }

    private String getFirstElement(JsonObject obj, String key) {
        if (obj.has(key)) {
            JsonArray array = obj.getAsJsonArray(key);
            if (!array.isEmpty()) {
                return array.get(0).getAsString();
            }
        }
        return "Unknown";
    }

    public static void main(String[] args) {
        String file = "src/main/java/com/library/api/sci-fi_books.txt";
        GoogleBooksAPIClient newClient = new GoogleBooksAPIClient();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String isbn;
            int quantity = 10;
            while ((isbn = br.readLine()) != null) {
                if (!isbn.trim().isEmpty()) newClient.getBookData(isbn.trim(), quantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String isbn;
            int quantity = 10;
            int counter = 0;
            while ((isbn = br.readLine()) != null) {
                if (!isbn.trim().isEmpty()) {
                    System.out.printf("Processing ISBN %s (%d)\n", isbn.trim(), ++counter);
                    newClient.getBookData(isbn.trim(), quantity);
                }
            }
            System.out.println("Finished processing all ISBNs.");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String isbn="9780735636972";
//        int quantity=10;
//        try {
//            newClient.getBookData(isbn.trim(),quantity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    public static JsonArray getBooks(String query) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query ;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
                return jsonResponse.getAsJsonArray("items");  // Get the books array
            } else {
                System.err.println("Error fetching data: " + response.message());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}




