package br.services;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

import br.dominio.BancoDados;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceHunterApi {

    private String apiKey;

    public ServiceHunterApi() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            apiKey = prop.getProperty("hunter.api.key");
            System.out.println("Api key carregada: " + apiKey);

        } catch (Exception e) {
            e.printStackTrace();
            apiKey = "";
        }

    }

    // Método de verificação de e-mail se já está no Banco de Dados
    public boolean verificarEmail(String email) {

        if (BancoDados.validarEmail(email)) {
            System.out.println("E-mail encontrado no banco: " + email);
            return true;
        }

        String resultado = consultarApiParaValidarEmail(email);

        BancoDados.salvarResultadoValidacao("email", email, resultado);

        return "valid".equalsIgnoreCase(resultado);
    }

    private String consultarApiParaValidarEmail(String email) {
        try {
            String urlStr = "https://api.hunter.io/v2/email-verifier?email=" + email + "&api_key=" + apiKey;
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

                // Verifica o status de validação
                String status = jsonNode.get("data").get("status").asText();
                String result = jsonNode.get("data").get("result").asText();

                // Se status for "valid" e result não for "risky", consideramos válido
                if ("valid".equalsIgnoreCase(status) && !"risky".equalsIgnoreCase(result)) {
                    return "valid";
                } else if ("risky".equalsIgnoreCase(result)) {
                    return "risky";
                } else {
                    return "invalid";
                }
            } else {
                System.out.println("Erro na API Hunter: Código " + response.statusCode());
                return "invalid";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "invalid";
        }
    }
}
