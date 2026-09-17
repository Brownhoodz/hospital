import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HospitalServer {

    static ArrayList<Patient> patients = new ArrayList<>();
    static int nextId = 1;

    static class Patient {
        int id;
        String name;
        int age;
        String gender;
        String phone;
        String department;
        String reason;

        Patient(int id, String name, int age, String gender,
                String phone, String department, String reason) {

            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
            this.department = department;
            this.reason = reason;
        }
    }

    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "8080")
        );

        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port), 0
        );

        server.createContext("/", HospitalServer::handleHome);
        server.createContext("/add", HospitalServer::handleAdd);
        server.createContext("/delete", HospitalServer::handleDelete);

        server.start();

        System.out.println("Hospital Management System running on port " + port);
    }

    // HOME PAGE
    static void handleHome(HttpExchange exchange) throws IOException {

        StringBuilder html = new StringBuilder();

        html.append("""
        <!DOCTYPE html>
        <html>
        <head>
            <title>Hospital Management System</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background: #f4f7fb;
                    margin: 0;
                    padding: 30px;
                }

                .container {
                    max-width: 1100px;
                    margin: auto;
                    background: white;
                    padding: 30px;
                    border-radius: 15px;
                    box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                }

                h1 {
                    text-align: center;
                    color: #1e3a5f;
                }

                h2 {
                    color: #1e3a5f;
                }

                form {
                    display: grid;
                    grid-template-columns: 1fr 1fr;
                    gap: 12px;
                    margin-bottom: 30px;
                }

                input, select {
                    padding: 12px;
                    border: 1px solid #ccc;
                    border-radius: 7px;
                }

                button {
                    padding: 12px;
                    background: #1e3a5f;
                    color: white;
                    border: none;
                    border-radius: 7px;
                    cursor: pointer;
                }

                button:hover {
                    background: #142942;
                }

                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-top: 15px;
                }

                th, td {
                    border: 1px solid #ddd;
                    padding: 10px;
                    text-align: left;
                }

                th {
                    background: #1e3a5f;
                    color: white;
                }

                .delete {
                    background: #c0392b;
                }

                .empty {
                    text-align: center;
                    padding: 20px;
                    color: #777;
                }
            </style>
        </head>

        <body>

        <div class="container">

            <h1>🏥 Hospital Management System</h1>

            <h2>Patient Admission</h2>

            <form method="POST" action="/add">

                <input type="text"
                       name="name"
                       placeholder="Patient Name"
                       required>

                <input type="number"
                       name="age"
                       placeholder="Age"
                       required>

                <select name="gender" required>
                    <option value="">Select Gender</option>
                    <option>Male</option>
                    <option>Female</option>
                    <option>Other</option>
                </select>

                <input type="text"
                       name="phone"
                       placeholder="Phone Number"
                       required>

                <input type="text"
                       name="department"
                       placeholder="Department"
                       required>

                <input type="text"
                       name="reason"
                       placeholder="Reason for Admission"
                       required>

                <button type="submit">
                    Add Patient
                </button>

            </form>

            <h2>Admitted Patients</h2>
        """);

        if (patients.isEmpty()) {

            html.append("""
                <div class="empty">
                    No patients admitted yet.
                </div>
            """);

        } else {

            html.append("""
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Age</th>
                        <th>Gender</th>
                        <th>Phone</th>
                        <th>Department</th>
                        <th>Reason</th>
                        <th>Action</th>
                    </tr>
            """);

            for (Patient p : patients) {

                html.append("<tr>");

                html.append("<td>").append(p.id).append("</td>");
                html.append("<td>").append(escape(p.name)).append("</td>");
                html.append("<td>").append(p.age).append("</td>");
                html.append("<td>").append(escape(p.gender)).append("</td>");
                html.append("<td>").append(escape(p.phone)).append("</td>");
                html.append("<td>").append(escape(p.department)).append("</td>");
                html.append("<td>").append(escape(p.reason)).append("</td>");

                html.append("""
                    <td>
                        <form method="POST" action="/delete"
                              style="display:block; margin:0;">
                """);

                html.append("<input type=\"hidden\" name=\"id\" value=\"")
                    .append(p.id)
                    .append("\">");

                html.append("""
                        <button class="delete" type="submit">
                            Delete
                        </button>
                        </form>
                    </td>
                """);

                html.append("</tr>");
            }

            html.append("</table>");
        }

        html.append("""
        </div>

        </body>
        </html>
        """);

        sendResponse(exchange, html.toString());
    }

    // ADD PATIENT
    static void handleAdd(HttpExchange exchange) throws IOException {

        String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        Map<String, String> data = parseForm(body);

        try {

            String name = data.getOrDefault("name", "");
            int age = Integer.parseInt(data.getOrDefault("age", "0"));
            String gender = data.getOrDefault("gender", "");
            String phone = data.getOrDefault("phone", "");
            String department = data.getOrDefault("department", "");
            String reason = data.getOrDefault("reason", "");

            Patient patient = new Patient(
                    nextId++,
                    name,
                    age,
                    gender,
                    phone,
                    department,
                    reason
            );

            patients.add(patient);

        } catch (Exception e) {
            System.out.println("Error adding patient: " + e.getMessage());
        }

        redirect(exchange, "/");
    }

    // DELETE PATIENT
    static void handleDelete(HttpExchange exchange) throws IOException {

        String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        Map<String, String> data = parseForm(body);

        try {

            int id = Integer.parseInt(data.getOrDefault("id", "0"));

            patients.removeIf(patient -> patient.id == id);

        } catch (Exception e) {
            System.out.println("Error deleting patient: " + e.getMessage());
        }

        redirect(exchange, "/");
    }

    // FORM DATA
    static Map<String, String> parseForm(String body) {

        Map<String, String> data = new HashMap<>();

        if (body == null || body.isEmpty()) {
            return data;
        }

        String[] pairs = body.split("&");

        for (String pair : pairs) {

            String[] parts = pair.split("=", 2);

            if (parts.length == 2) {

                String key = URLDecoder.decode(
                        parts[0],
                        StandardCharsets.UTF_8
                );

                String value = URLDecoder.decode(
                        parts[1],
                        StandardCharsets.UTF_8
                );

                data.put(key, value);
            }
        }

        return data;
    }

    // SEND HTML
    static void sendResponse(
            HttpExchange exchange,
            String response
    ) throws IOException {

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set("Content-Type", "text/html; charset=UTF-8");

        exchange.sendResponseHeaders(200, bytes.length);

        OutputStream output = exchange.getResponseBody();

        output.write(bytes);
        output.close();
    }

    // REDIRECT
    static void redirect(
            HttpExchange exchange,
            String location
    ) throws IOException {

        exchange.getResponseHeaders()
                .set("Location", location);

        exchange.sendResponseHeaders(303, -1);

        exchange.close();
    }

    // BASIC HTML SAFETY
    static String escape(String text) {

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
