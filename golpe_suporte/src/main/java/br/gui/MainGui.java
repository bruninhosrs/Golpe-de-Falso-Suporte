package br.gui;

import br.services.ServiceHunterApi;
import br.services.ServiceNumVerifyApi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainGui extends Application {

    private ServiceHunterApi serviceHunter = new ServiceHunterApi();
    private ServiceNumVerifyApi serviceNumVerify = new ServiceNumVerifyApi();

    @Override
    public void start(Stage primaryStage) {  // Aqui você sobrescreve o método start()

        // Criar os componentes da interface
        Label labelEmail = new Label("Digite o e-mail para validar:");
        TextField emailField = new TextField();
        Button validateEmailBtn = new Button("Validar E-mail");
        Label emailResult = new Label();

        Label labelTelefone = new Label("Digite o telefone para validar:");
        TextField telefoneField = new TextField();
        Button validateTelefoneBtn= new Button("Validar Telefone");
        Label telefoneResult = new Label();

        // Ação do botão para validar o e-mail
        validateEmailBtn.setOnAction(event -> {
            String email = emailField.getText();
            boolean isValidEmail = serviceHunter.verificarEmail(email);
            if (isValidEmail) {
                emailResult.setText("O e-mail '" + email + "' é VÁLIDO.");
            } else {
                emailResult.setText("O e-mail '" + email + "' é FALSO ou INVÁLIDO.");
            }
        });

        // Ação do botão para validar o telefone
        validateTelefoneBtn.setOnAction(event -> {
            String telefone = telefoneField.getText();
            boolean isValidTelefone = serviceNumVerify.verificarTelefone(telefone);
            if (isValidTelefone) {
                telefoneResult.setText("O telefone '" + telefone + "' é VÁLIDO.");
            } else {
                telefoneResult.setText("O telefone '" + telefone + "' é FALSO ou INVÁLIDO.");
            }
        });

        // Layout com VBox para organizar os elementos verticalmente
        VBox vbox = new VBox(10, labelEmail, emailField, validateEmailBtn, emailResult, labelTelefone, telefoneField, validateTelefoneBtn, telefoneResult);
        vbox.setStyle("-fx-padding: 20px; -fx-font-size: 14px;");

        // Criar a cena e adicionar ao estágio
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Validação de Suporte");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  // Isso lança a aplicação JavaFX
    }
}
