package br.services;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.dominio.BancoDados;

public class ServiceNumVerifyApi {

    private String apiKey;

    public ServiceNumVerifyApi() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            apiKey = prop.getProperty("numberverify.api.key");
            System.out.println("Api key carregada: " + apiKey);

        } catch (Exception e) {
            e.printStackTrace();
            apiKey = "";
        }

    }

    public boolean verificarTelefone(String telefone) {

        if (BancoDados.validarTelefone(telefone)) {
            System.out.println("Telefone encontrado no banco: " + telefone);
            return true;
        }

        String resultado = consultarApiParaValidarTelefone(telefone);

        BancoDados.salvarResultadoValidacao("telefone", telefone, resultado);

        return "valid".equalsIgnoreCase(resultado);
    }

    public String consultarApiParaValidarTelefone(String telefone) {
        try {
            String urlStr = "http://apilayer.net/api/validate?access_key=" + apiKey + "&number=" + telefone
                    + "&country_code=BR&format=1";
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

                if (jsonNode.get("valid").asBoolean()) {
                    return "valid";
                } else {
                    return "invalid";
                }

            } else {
                System.out.println("Erro na API NumVerify: Código " + response.statusCode());
                return "invalid";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "invalid";
        }
    }

}
