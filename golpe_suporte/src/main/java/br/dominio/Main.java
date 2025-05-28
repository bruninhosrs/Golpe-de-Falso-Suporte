package br.dominio;

import br.services.ServiceHunterApi;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        ServiceHunterApi serviceHunter = new ServiceHunterApi(); // Instancia o objeto
        
        // Entrada e validação de e-mail via API Hunter
        System.out.print("Digite o e-mail para validar: ");
        String email = scanner.nextLine();
        
        System.out.println("Validando e-mail pela API Hunter...");
        boolean emailValido = serviceHunter.verificarEmail(email); // # Não esquecer que o método de ServiceHunterApi recebe como parâmetro o email
        if (emailValido) {
            System.out.println("O e-mail '" + email + "' é VÁLIDO.");
        } else {
            System.out.println("O e-mail '" + email + "' é FALSO ou INVÁLIDO.");
        }

        scanner.close();
    }
}
