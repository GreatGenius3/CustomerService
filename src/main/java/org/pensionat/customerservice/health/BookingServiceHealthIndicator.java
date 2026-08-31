package org.pensionat.customerservice.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BookingServiceHealthIndicator {

    @Value("${inventory.service.url}")
    private String bookingServiceUrl;

    @GetMapping("/actuator/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        Map<String, Object> details = new HashMap<>();
        
        try {
            URL url = new URL(bookingServiceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);

            int responseCode = connection.getResponseCode();
            
            if (responseCode >= 200 && responseCode < 300) {
                health.put("status", "UP");
                details.put("service", "BookingService");
                details.put("url", bookingServiceUrl);
                details.put("bookingServiceStatus", "UP");
                health.put("details", details);
            } else {
                health.put("status", "DOWN");
                details.put("service", "BookingService");
                details.put("url", bookingServiceUrl);
                details.put("bookingServiceStatus", "DOWN");
                details.put("responseCode", responseCode);
                details.put("message", "BookingService svarar med felkod");
                health.put("details", details);
            }
        } catch (IOException e) {
            health.put("status", "DOWN");
            details.put("service", "BookingService");
            details.put("url", bookingServiceUrl);
            details.put("bookingServiceStatus", "DOWN");
            details.put("error", e.getMessage());
            details.put("message", "BookingService är inte tillgänglig");
            health.put("details", details);
        }
        
        return health;
    }
}
