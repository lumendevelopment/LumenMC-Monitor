package cloud.lumenvm;                        
import org.bukkit.plugin.java.JavaPlugin;     
import java.io.OutputStream;                  import java.net.HttpURLConnection;            import java.net.URL;
import java.time.LocalDateTime;               
public final class main extends JavaPlugin {
    private static final String WEBHOOK_URL = System.getenv("WEBHOOK_URL");                     private static final String WEBHOOK_AVATAR = System.getenv("WEBHOOK_AVATAR");               private static final String WEBHOOK_NAME = System.getenv("WEBHOOK_NAME");
    private static final String WEBHOOK_IMAGE = System.getenv("WEBHOOK_IMAGE");
    private static final String TZ = System.getenv("TZ");                                       private static final String SERVER_MEMORY = System.getenv("SERVER_MEMORY");                 private static final String SERVER_IP = System.getenv("SERVER_IP");                         private static final String SERVER_PORT = System.getenv("SERVER_PORT");
    private static final String P_SERVER_LOCATION = System.getenv("P_SERVER_LOCATION");
    private static final String P_SERVER_UUID = System.getenv("P_SERVER_UUID");

    @Override
    public void onEnable() {
        sendWebhook("Server Started", "The server has been started.", "8388736");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sendWebhook("Server Crashed", "The server has crashed or stopped unexpectedly.", "FF0000");
        }));                                      }

    @Override
    public void onDisable() {
        sendWebhook("Server Stopped", "The server has been stopped.", "8388736");
    }                                         private void sendWebhook(String title, String description, String color) {                      try {
        URL url = new URL(WEBHOOK_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        StringBuilder jsonPayload = new StringBuilder();                                            jsonPayload.append("{")
                .append("\"username\": \"").append(WEBHOOK_NAME != null ? WEBHOOK_NAME : "LumenMC").append("\",")
                .append("\"avatar_url\": \"").append(WEBHOOK_AVATAR != null ? WEBHOOK_AVATAR : "https://cdn.lumenvm.cloud/lumen-avatar.png").append("\",")
                .append("\"embeds\": [{")
                .append("\"title\": \"").append(title).append("\",")
                .append("\"description\": \"").append(description).append("\",")
                .append("\"color\": ").append(color).append(",")                                            .append("\"image\": {")
                .append("\"url\": \"").append(WEBHOOK_IMAGE != null ? WEBHOOK_IMAGE : "https://cdn.lumenvm.cloud/lumenmc-banner.png").append("\"")
                .append("},");                
        jsonPayload.append("\"fields\": [")                   .append("{\"name\": \"Time Zone\", \"value\": \"").append(TZ).append("\", \"inline\": true},")
                .append("{\"name\": \"Server Memory\", \"value\": \"").append(SERVER_MEMORY).append("MB\", \"inline\": true},")                           .append("{\"name\": \"Server IP\", \"value\": \"").append(SERVER_IP).append("\", \"inline\": true},")
                .append("{\"name\": \"Server Port\", \"value\": \"").append(SERVER_PORT).append("\", \"inline\": true},")
                .append("{\"name\": \"Server Location\", \"value\": \"").append(P_SERVER_LOCATION).append("\", \"inline\": true},")                       .append("{\"name\": \"Server UUID\", \"value\": \"```").append(P_SERVER_UUID).append("```\", \"inline\": true},")
                .append("{\"name\": \"Server Version\", \"value\": \"").append(getServer().getVersion()).append("\", \"inline\": true},")
                .append("{\"name\": \"Number of Plugins\", \"value\": \"").append(getServer().getPluginManager().getPlugins().length).append("\", \"inline\": true}")                                   .append("],");

        jsonPayload.append("\"footer\": {")
                .append("\"text\": \"LumenMC Monitor | ").append(LocalDateTime.now()).append("\"")                                                        .append("}")
                .append("}]")
                .append("}");
                                                      try (OutputStream os = connection.getOutputStream()) {                                          byte[] input = jsonPayload.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

}                                             }
