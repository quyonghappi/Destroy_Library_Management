//package com.library.api;
//
//import org.json.JSONObject;
//import org.json.JSONArray;
//
//import java.io.*;
//import java.net.*;
//import java.util.*;
//public class GoogleBookAPI {
//    public static void main(String[] args) {
//        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=Crazy Town"; // Thay thế "Java" bằng từ khóa bạn muốn
//        StringBuilder response = new StringBuilder();
//
//        try {
//            URL url = new URL(apiUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.connect();
//
//            // Kiểm tra mã phản hồi
//            int responseCode = conn.getResponseCode();
//            if (responseCode == 200) {
//                // Đọc phản hồi
//                Scanner scanner = new Scanner(url.openStream());
//                while (scanner.hasNext()) {
//                    response.append(scanner.nextLine());
//                }
//                scanner.close();
//            } else {
//                System.out.println("Lỗi: " + responseCode);
//                return; // Thoát nếu có lỗi
//            }
//
//            JSONObject obj = new JSONObject(response.toString());
//            JSONArray items = obj.getJSONArray("items");
//
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter("isbn_list.txt"))){
//                for(int i = 0; i < items.length(); i++) {
//                    JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");
//                    JSONArray identifiers  = volumeInfo.optJSONArray("industryIdentifiers");
//                    if (identifiers != null) {
//                        for (int j = 0; j < identifiers.length(); j++) {
//                            JSONObject identifier = identifiers.getJSONObject(j);
//                            if ("ISBN_13".equals(identifier.getString("type"))) {
//                                String isbn = identifier.getString("identifier");
//                                writer.write(isbn);
//                                writer.newLine();
//                            }
//                        }
//                    }
//                }
//            }
//            System.out.println("Đã lưu danh sách ISBN vào file isbn_list.txt");
//        } catch (ProtocolException e) {
//            throw new RuntimeException(e);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
