package com.library.controller.admin.books;

import com.library.dao.DocumentDao;
import com.library.models.Document;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.List;


public class SearchBookInfo {

    private static DocumentDao documentDao = DocumentDao.getInstance();

    static void handleSearch(ListView<Document> bookDetailContainer, String searchText, String selectedCriteria) {
        if (searchText != null && !searchText.isEmpty()) {
            switch (selectedCriteria) {
                case "ISBN":
                    SearchBookByIsbn(bookDetailContainer, searchText);
                    break;
                case "Title":
                    SearchBookByTitle(bookDetailContainer, searchText);
                    break;
                case "Author":
                    SearchBookByAuthor(bookDetailContainer, searchText);
                    break;
                case "Category":
                    SearchBookByCategory(bookDetailContainer, searchText);
                    break;
            }
        } else {
            loadBookList(bookDetailContainer);
        }
    }

    private static void loadBookList(ListView<Document> bookDetailContainer) {
        Task<List<Document>> loadTask = new Task<>() {
            @Override
            protected List<Document> call() {
                return documentDao.getAll();
            }
        };

        loadTask.setOnSucceeded(event -> {
            SearchBookInfo.refreshListView(bookDetailContainer, loadTask.getValue());
        });
        loadTask.setOnFailed(event -> {
            System.out.println("fail to load book info" + loadTask.getException());
        });
        new Thread(loadTask).start();
    }

    static void refreshListView(ListView<Document> bookDetailContainer, List<Document> documentList) {
        bookDetailContainer.setCellFactory(param->
        {
            BookInfoCell cell = new BookInfoCell();
            cell.setListView(bookDetailContainer);
            return cell;
        });
        bookDetailContainer.getItems().setAll(documentList);
    }


    public static void SearchBookByIsbn(ListView<Document> bookDetailContainer, String IsbnValue) {

        Task<List<Document>> searchTask = new Task<>() {

            @Override
            protected List<Document> call() {
                return DocumentDao.searchByIsbn(IsbnValue);
            }
        };

        searchTask.setOnSucceeded(event -> {
            List<Document> searchResults = searchTask.getValue();
            if (searchResults.isEmpty()) {
                bookDetailContainer.getItems().clear();
                bookDetailContainer.setVisible(false);
            } else {
                bookDetailContainer.setVisible(true);
            }
            refreshListView(bookDetailContainer, searchResults);
        });

        searchTask.setOnFailed(event -> {
            System.out.println("fail to load book info" + searchTask.getException());
        });
        new Thread(searchTask).start();

    }

    public static void SearchBookByTitle(ListView<Document> bookDetailContainer, String TitleValue) {

        Task<List<Document>> searchTask = new Task<>() {

            @Override
            protected List<Document> call() {
                return DocumentDao.searchByTitle(TitleValue);
            }
        };

        searchTask.setOnSucceeded(event -> {
            List<Document> searchResults = searchTask.getValue();
            if (searchResults.isEmpty()) {
                bookDetailContainer.getItems().clear();
                bookDetailContainer.setVisible(false);
            } else {
                bookDetailContainer.setVisible(true);
            }
            refreshListView(bookDetailContainer, searchResults);
        });

        searchTask.setOnFailed(event -> {
            System.out.println("fail to load book info" + searchTask.getException());
        });
        new Thread(searchTask).start();

    }

    public static void SearchBookByAuthor(ListView<Document> bookDetailContainer, String authorValue) {

        Task<List<Document>> searchTask = new Task<>() {

            @Override
            protected List<Document> call() throws SQLException {
                return DocumentDao.searchByAuthor(authorValue);
            }
        };

        searchTask.setOnSucceeded(event -> {
            List<Document> searchResults = searchTask.getValue();
            if (searchResults.isEmpty()) {
                bookDetailContainer.getItems().clear();
                bookDetailContainer.setVisible(false);
            } else {
                bookDetailContainer.setVisible(true);
            }
            refreshListView(bookDetailContainer, searchResults);
        });

        searchTask.setOnFailed(event -> {
            System.out.println("fail to load book info" + searchTask.getException());
        });
        new Thread(searchTask).start();

    }

    public static void SearchBookByCategory(ListView<Document> bookDetailContainer, String authorValue) {

        Task<List<Document>> searchTask = new Task<>() {

            @Override
            protected List<Document> call() throws SQLException {
                return DocumentDao.searchByCategory(authorValue);
            }
        };

        searchTask.setOnSucceeded(event -> {
            List<Document> searchResults = searchTask.getValue();
            if (searchResults.isEmpty()) {
                bookDetailContainer.getItems().clear();
                bookDetailContainer.setVisible(false);
            } else {
                bookDetailContainer.setVisible(true);
            }
            refreshListView(bookDetailContainer, searchResults);
        });

        searchTask.setOnFailed(event -> {
            System.out.println("fail to load book info" + searchTask.getException());
        });
        new Thread(searchTask).start();

    }
}
