package br.services;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceNumVerifyApi {
    private String apiKey;

    public ServiceNumVerifyApi() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            apiKey = prop.getProperty("numberverify.api.key");
            System.out.println("Api key carregada: " + apiKey); // Me mostra minha senha que o Hunter me disponibilizou

        } catch (Exception e) {
            e.printStackTrace();
            apiKey = "";
        }

    }

    public boolean verificarTelefone(String telefone) {
        try {
            String urlStr = "http://apilayer.net/api/validate?access_key=" + apiKey + "&number=" + telefone + "&country_code=BR&format=1";
            URI uri = URI.create(urlStr);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.body());

                return jsonNode.get("valid").asBoolean();

            } else {
                System.out.println("Erro na API NumVerify: Código " + response.statusCode());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
