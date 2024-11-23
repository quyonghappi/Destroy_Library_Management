package com.library.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.library.config.APIConfig;
import com.library.dao.DocumentDao;
import com.library.models.Author;
import com.library.models.Category;
import com.library.models.Document;
import com.library.models.Publisher;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
//import java.awt.List;
import java.util.ArrayList;
import java.util.List;

public class GoogleBooksAPIClient {
    private static final int limit=100;
    private static final long time=600; //one min to ms
    private List<Long> requestTimestamp=new ArrayList<>();

    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn%s&key=%s";

    private final OkHttpClient client;
    //parse json response into java object
    private final Gson gson;
    private DocumentDao documentDao;

    public GoogleBooksAPIClient() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
        this.documentDao = new DocumentDao();
    }

    //get book data, minh nen search bang ten sach vi viec collect isbn kho hon so voi viec collect ten sach
    //to khong de static duoc vi requestTimestamp nonstatic
    public void getBookData(String term) throws Exception {
        //solve call limit per minute, tai vi limit 100 calls/min
        long currentTime = System.currentTimeMillis();
        requestTimestamp.removeIf(timestamp -> currentTime - timestamp > 60000);

        if (requestTimestamp.size() >= limit) {
            long wait = time - (currentTime - requestTimestamp.get(0));
            if (wait > 0) {
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

            String url = String.format(API_URL, term, APIConfig.GOOGLE_BOOKS_API_KEY);
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(API_URL))
//                .build();

            //okhttp nhanh hon
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    System.err.println("Request failed: " + e.getMessage());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonData = response.body().string();
                        printApiResponse(jsonData);
                        parseBooks(jsonData);
                    } else {
                        String errorBody = response.body().string(); //read the error body for more details
                        System.out.println("Error: " + response.code() + " - " + response.message());
                        System.out.println("Error Body: " + errorBody); //print the error message from api
                    }
                }

            });
        //HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    private void printApiResponse(String jsonData) {
        System.out.println("api response:");
        System.out.println(jsonData);
        System.out.println("--------------------------");

    }


    private void parseBooks(String jsonData) {
        //convert json string into a json object
        //to interact with the data
        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
        JsonArray items = jsonObject.getAsJsonArray("items");

        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                JsonObject book = items.get(i).getAsJsonObject();
                // volumeInfo object contains book details like title, ISBN...
                JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");

                String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "N/A";
                String subtitle = volumeInfo.has("subtitle") ? volumeInfo.get("subtitle").getAsString() : "";
                String isbn = extractISBN(volumeInfo);
                String authorName = volumeInfo.has("authors") ? volumeInfo.get("authors").getAsJsonArray().get(0).getAsString() : "Unknown";
                String publisherName = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Unknown";
                String categoryName = volumeInfo.has("categories") ? volumeInfo.get("categories").getAsJsonArray().get(0).getAsString() : "General";
//                String s = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString().substring(0, 4) + "";
                int publicationYear = volumeInfo.has("publishedDate") ? Integer.parseInt(volumeInfo.get("publishedDate").getAsString().substring(0, 4)) : 0;
                String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "N/A";

                int pages = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").getAsInt() : 0;

                String imageLink = "N/A";
                if (volumeInfo.has("imageLinks")) {
                    JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
                    imageLink = imageLinks.has("thumbnail") ? imageLinks.get("thumbnail").getAsString() : "N/A";
                }

                //trong accessInfo co preview link
                JsonObject accessInfo = book.has("accessInfo") ? book.getAsJsonObject("accessInfo") : null;
                String checkViewability = accessInfo != null && accessInfo.has("viewability") ? accessInfo.get("viewability").getAsString() : "N/A";

                String previewLink;
                if (accessInfo == null || checkViewability.equals("NO_PAGES")) {
                    previewLink = "There is no preview for this book.";
                } else {
                    previewLink = volumeInfo.has("previewLink") ? volumeInfo.get("previewLink").getAsString() : "N/A";
                }


                Document doc = new Document();
                doc.setTitle(title + " " + subtitle);
                doc.setISBN(isbn);
                doc.setPublicationYear(publicationYear);
                doc.setDescription(description);
                doc.setPreviewLink(previewLink);
                doc.setPage(pages);
                doc.setImageLink(imageLink);  //set imageLink in document
//
//                if ("N/A".equals(title)) {
//                    printApiResponse(jsonData);
//                    doc.setTitle("N/A");
//                }
                if (!"Unknown".equals(categoryName)) {
                    Category category = new Category();
                    category.setName(categoryName);
                    doc.setCategory(category);
                }
                else {
                    Category category = new Category();
                    category.setName("N/A");
                    doc.setCategory(category);
                }

                if (!"N/A".equals(authorName)) {
                    Author author = new Author();
                    author.setName(authorName);
                    doc.setAuthor(author);
                }
                else {
                    Author author = new Author();
                    author.setName("N/A");
                    doc.setAuthor(author);
                }

                if(!"N/A".equals(publisherName)) {
                    Publisher pub = new Publisher();
                    pub.setName(publisherName);
                    doc.setPublisher(pub);
                }
                else {
                    Publisher pub = new Publisher();
                    pub.setName("N/A");
                    doc.setPublisher(pub);
                }

                //insert into the database (fetching or creating the id for author, publisher, and category)
                insertDocumentIntoDatabase(doc,authorName, publisherName, categoryName);
            }
        } else {
            System.out.println("No books found for the term.");
        }
    }


    private String extractISBN(JsonObject volumeInfo) {
        //ISBN is stored under the industryIdentifiers field so we have this if condition to check
        //try print response to see its structure
        if (volumeInfo.has("industryIdentifiers")) {
            JsonArray identifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
            if (identifiers != null) {
                for (int j = 0; j < identifiers.size(); j++) {
                    JsonObject identifier = identifiers.get(j).getAsJsonObject();
                    //extract the first isbn we found
                    if (identifier.has("type") && "ISBN_13".equals(identifier.get("type").getAsString())) {
                        return identifier.get("identifier").getAsString();
                    }
                }
            }


        }
        return "N/A"; //if ISBN is not found
    }

    //use for extract first author and category, cuz idk how can we store multi
    private String getFirstElement(JsonObject volumeInfo, String key) {
        if (volumeInfo.has(key)) {
            JsonArray array = volumeInfo.getAsJsonArray(key);
            if (array.size() > 0) {
                return array.get(0).getAsString();
            }
        }
        return "N/A";
    }

    private void insertDocumentIntoDatabase(Document doc, String authorName, String publisherName, String categoryName) {
        try {
            //get or create the authorid
            int authorId = documentDao.getOrCreateAuthorId(authorName);
            doc.setAuthorId(authorId);

            //get or create the publisherid
            int publisherId = documentDao.getOrCreatePublisherId(publisherName);
            doc.setPublisherId(publisherId);

            //get or create the categoryid
            int categoryId = documentDao.getOrCreateCategoryId(categoryName);
            doc.setCategoryId(categoryId);

            documentDao.add(doc);
            System.out.println("Book inserted successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String file = "src/main/java/com/library/api/programming_books.txt";
        GoogleBooksAPIClient newClient = new GoogleBooksAPIClient();
        String isbn;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((isbn = br.readLine()) != null) {
                isbn = isbn.substring(2, isbn.length() - 3);
                if (!isbn.trim().isEmpty()) newClient.getBookData(isbn.trim());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            String isbn = "9780132350884";
//            newClient.getBookData(isbn.trim());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}

