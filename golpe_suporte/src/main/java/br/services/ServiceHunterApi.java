package br.services;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class ServiceHunterApi {

    private String apiKey;

    public ServiceHunterApi() {
         try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            apiKey = prop.getProperty("hunter.api.key");
            System.out.println("Api key carregada: " +apiKey); // Me mostra minha senha que o Hunter me disponibilizou 
            
        } catch (Exception e) {
            e.printStackTrace();
            apiKey = "";
        }

    }

    public boolean verificarEmail(String email) {
        try {
            // Cria a URI
            String urlStr = "https://api.hunter.io/v2/email-verifier?email=" + email + "&api_key=" + apiKey;
            URI uri = URI.create(urlStr);

            // Cria o cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Cria a requisição GET
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            // Envia a requisição e recebe a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String body = response.body();

            if (statusCode == 200) {
                // Aqui você deve processar o body JSON para analisar validade
                System.out.println("Resposta da API: " + body);
                return true; // Retorne conforme o resultado do parse
            } else if (statusCode == 401) {
                System.out.println("Erro 401: Chave API inválida ou não autorizada.");
                return false;
            } else {
                System.out.println("Erro na API Hunter: Código " + statusCode);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
