package analytics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnalyticsData {

    public static class CustomerStats {
        public int id;
        public String name;
        public String email;
        public double totalSpend;
        public int visitCount;
        public String lastVisit;

        public CustomerStats(int id, String name, String email, double totalSpend, int visitCount, String lastVisit) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.totalSpend = totalSpend;
            this.visitCount = visitCount;
            this.lastVisit = lastVisit;
        }
    }

    public static List<CustomerStats> getMockData() {
        List<CustomerStats> customers = new ArrayList<>();
        String[] firstNames = {
                "Aarav", "Vivaan", "Aditya", "Vihaan", "Arjun", "Sai", "Reyansh", "Ayan", "Krishna", "Ishaan",
                "Diya", "Saanvi", "Ananya", "Aadhya", "Pari", "Anika", "Navya", "Angel", "Myra", "Riya"
        };
        String[] lastNames = {
                "Sharma", "Verma", "Gupta", "Malhotra", "Bhatia", "Saxena", "Reddy", "Nair", "Patel", "Singh",
                "Chopra", "Desai", "Joshi", "Mehta", "Jain", "Agarwal", "Chatterjee", "Das", "Rao", "More"
        };
        Random rand = new Random();

        for (int i = 0; i < 50; i++) {
            int id = 1001 + i;
            String fName = firstNames[rand.nextInt(firstNames.length)];
            String lName = lastNames[rand.nextInt(lastNames.length)];
            String name = fName + " " + lName;

            String email = fName.toLowerCase() + "." + lName.toLowerCase() + "@example.com";
            double totalSpend = 500 + (9500 * rand.nextDouble()); // Random spend between 500 and 10000
            int visitCount = 1 + rand.nextInt(100);
            LocalDate lastVisitDate = LocalDate.now().minusDays(rand.nextInt(60));

            customers.add(new CustomerStats(
                    id,
                    name,
                    email,
                    Math.round(totalSpend * 100.0) / 100.0,
                    visitCount,
                    lastVisitDate.toString()));
        }
        return customers;
    }

    public static String getJsonData() {
        List<CustomerStats> data = getMockData();
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < data.size(); i++) {
            CustomerStats c = data.get(i);
            json.append(String.format(
                    "{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"totalSpend\":%.2f,\"visitCount\":%d,\"lastVisit\":\"%s\"}",
                    c.id, c.name, c.email, c.totalSpend, c.visitCount, c.lastVisit));
            if (i < data.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}
