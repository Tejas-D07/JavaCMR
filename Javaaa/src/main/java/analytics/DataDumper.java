package analytics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataDumper {
    public static void main(String[] args) throws IOException {
        String json = AnalyticsData.getJsonData();
        Files.write(Paths.get("docs/data.json"), json.getBytes());
        System.out.println("Data dumped to docs/data.json");
    }
}
