package br.dominio;

import br.services.ServiceHunterApi;
import br.services.ServiceNumVerifyApi;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ServiceHunterApi serviceHunter = new ServiceHunterApi(); // Instancia o objeto da API (E-MAIL)
        ServiceNumVerifyApi serviceNumVerify = new ServiceNumVerifyApi(); // Instancia o objeto da API (TELEFONE)

        System.out.print("Digite o e-mail para validar: ");
        String email = scanner.nextLine();

        System.out.println("Validando e-mail pela API Hunter...");
        boolean emailValido = serviceHunter.verificarEmail(email); // # Não esquecer que o método de ServiceHunterApi recebe como parâmetro o email

        if (emailValido) {
            System.out.println("O e-mail '" + email + "' é VÁLIDO.");
        } else {
            System.out.println("O e-mail '" + email + "' é FALSO ou INVÁLIDO.");
        }

        System.out.println("=================================================");

        System.out.println("Digite o número de telefone (Exemplo: 11972420186):");
        String telefone = scanner.nextLine();

        System.out.println("Validando telefone pela API NumVerify...");
        boolean telefoneValido = serviceNumVerify.verificarTelefone(telefone);

        if (telefoneValido) {
            System.out.println("O telefone '" + telefone + "' é VÁLIDO.");
        } else {
            System.out.println("O telefone '" + telefone + "' é FALSO ou INVÁLIDO.");
        }

        // Salvar resultado no banco de dados
        BancoDados.salvarResultadoValidacao("email", email, emailValido);
        BancoDados.salvarResultadoValidacao("telefone", telefone, telefoneValido);

        scanner.close();
    }
}
