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
            System.out.println("Api key carregada: " + apiKey); // Me mostra minha senha que o Hunter me disponibilizou

        } catch (Exception e) {
            e.printStackTrace();
            apiKey = "";
        }

    }

    // Método de verificação de e-mail
    public boolean verificarEmail(String email) {
        // Primeiro, verifica se o e-mail já está no banco de dados
        if (BancoDados.validarEmail(email)) {
            System.out.println("E-mail encontrado no banco: " + email);
            return true; // E-mail já está validado e presente no banco
        }

        // Aqui, você pode usar a própria instância da classe para validar o e-mail
        // Não precisa criar uma nova instância de ServiceHunterApi
        boolean valido = consultarApiParaValidarEmail(email);

        // Salva o resultado da validação no banco de dados
        BancoDados.salvarResultadoValidacao("email", email, valido);

        return valido;
    }

    // Método que faz a requisição à API do Hunter para validar o e-mail
    private boolean consultarApiParaValidarEmail(String email) {
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
                // Faz o parsing da resposta JSON da API
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.body());

                // Verifica o status de validação
                String status = jsonNode.get("data").get("status").asText();
                String result = jsonNode.get("data").get("result").asText();

                // Se status for "valid" e result não for "risky", consideramos válido
                if ("valid".equalsIgnoreCase(status) && !"risky".equalsIgnoreCase(result)) {
                    return true;
                } else {
                    System.out.println("E-mail suspeito ou inválido: " + email);
                    return false;
                }
            } else {
                System.out.println("Erro na API Hunter: Código " + response.statusCode());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
