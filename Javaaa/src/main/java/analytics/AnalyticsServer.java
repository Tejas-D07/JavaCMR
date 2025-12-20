package analytics;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AnalyticsServer {

    private static final int PORT = 8000;
    // Assuming resources are at src/main/resources/web relative to working
    // directory or classpath
    // For simplicity in this dev environment, we'll try to read from
    // src/main/resources/web directly
    // if running from project root.
    private static final String WEB_ROOT = "src/main/resources/web";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // API Endpoint
        server.createContext("/api/analytics", new AnalyticsHandler());

        // Static File Server
        server.createContext("/", new StaticFileHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Analytics Server started on port " + PORT);
        System.out.println("Open http://localhost:" + PORT + " in your browser.");
    }

    static class AnalyticsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = AnalyticsData.getJsonData();
            // Add CORS headers for local dev if needed, or just standard JSON headers
            t.getResponseHeaders().add("Content-Type", "application/json");
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            t.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = t.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath();
            if (path.equals("/")) {
                path = "/index.html";
            }

            // Prevent directory traversal
            if (path.contains("..")) {
                String response = "403 Forbidden";
                t.sendResponseHeaders(403, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            Path filePath = Paths.get(WEB_ROOT, path);
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                String contentType = determineContentType(path);

                t.getResponseHeaders().add("Content-Type", contentType);
                t.sendResponseHeaders(200, Files.size(filePath));

                OutputStream os = t.getResponseBody();
                Files.copy(filePath, os);
                os.close();
            } else {
                String response = "404 Not Found: " + path;
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        private String determineContentType(String path) {
            if (path.endsWith(".html"))
                return "text/html";
            if (path.endsWith(".css"))
                return "text/css";
            if (path.endsWith(".js"))
                return "application/javascript";
            if (path.endsWith(".png"))
                return "image/png";
            if (path.endsWith(".jpg"))
                return "image/jpeg";
            return "text/plain";
        }
    }
}
