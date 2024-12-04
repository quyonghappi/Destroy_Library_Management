package com.library.api;

import com.google.gson.*;
import com.library.config.APIConfig;
import com.library.dao.DocumentDao;
import com.library.models.Author;
import com.library.models.Category;
import com.library.models.Document;
import com.library.models.Publisher;
import okhttp3.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class GoogleBooksAPIClient {
    private static final int LIMIT = 100; // API call limit per minute
    private static final long TIME_WINDOW_MS = 60000; // Time window in ms
    private final Queue<Long> requestTimestamps = new LinkedList<>();

    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:%s&key=%s";

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
            if (!response.isSuccessful()) {
                System.err.println("Error: " + response.code() + " - " + response.message());
                return false;
            }

            if (response.body() == null) {
                System.err.println("Error: Response body is null");
                return false;
            }

            String jsonData = response.body().string();
            JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
            JsonArray items = jsonObject.getAsJsonArray("items");
            return items != null && !items.isEmpty();
        } catch (IOException e) {
            System.err.println("Request failed: " + e.getMessage());
            return false;
        } catch (JsonSyntaxException e) {
            System.err.println("JSON parsing error: " + e.getMessage());
            return false;
        }
    }


    // Get book data and insert it into the database
    public void getBookData(String isbn, int quantity) {
        try {
            if (!DocumentDao.searchByIsbn(isbn).isEmpty()) {
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
                        printApiResponse(jsonData);
                        parseBooks(jsonData, quantity, isbn);
                    } else {
                        System.out.printf("Error for ISBN %s: %s - %s\n", isbn, response.code(), response.message());
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printApiResponse(String jsonData) {
        System.out.println("api response:");
        System.out.println(jsonData);
        System.out.println("--------------------------");

    }

    private void parseBooks(String jsonData, int quantity, String givenIsbn) {
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
            System.out.println("No books found for the provided ISBN." + givenIsbn);
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
        return getStringOrDefault(accessInfo, "previewLink", "No preview available.");
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
//        String file = "src/main/java/com/library/api/programming_books.txt";
        GoogleBooksAPIClient newClient = new GoogleBooksAPIClient();
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String isbn;
//            int quantity = 10;
//            while ((isbn = br.readLine()) != null) {
//                if (!isbn.trim().isEmpty()) newClient.getBookData(isbn.trim(), quantity);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String isbn="9789390504640";
        int quantity=10;
        try {
            newClient.getBookData(isbn.trim(),quantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




