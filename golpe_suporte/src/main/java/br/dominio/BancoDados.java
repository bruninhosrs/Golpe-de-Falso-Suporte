package br.dominio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BancoDados {
    private static String url = "jdbc:mysql://localhost:3306/golpe_suporte";
    private static String usuario = "root";
    private static String senha = "admin";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, usuario, senha);
    }

    // Método de validação do telefone
    public static boolean validarTelefone(String telefone) {
        String query = "SELECT * FROM contatos_validos WHERE telefone = ?";
        try (Connection con = conectar();
                PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, telefone);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método de validação do e-mail
    public static boolean validarEmail(String email) {
        String query = "SELECT * FROM contatos_validos WHERE email = ?";
        try (Connection con = conectar();
                PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void salvarResultadoValidacao(String tipoContato, String contato, boolean valido) {

        String checkQuery = "SELECT * FROM resultados_validacao WHERE tipo_contato = ? AND contato = ?";

        try (Connection con = conectar();
                PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {

            checkStmt.setString(1, tipoContato);
            checkStmt.setString(2, contato);

            ResultSet rs = checkStmt.executeQuery();


            if (!rs.next()) {
                String insertQuery = "INSERT INTO resultados_validacao (tipo_contato, contato, valido) VALUES (?, ?, ?)";

                try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, tipoContato);
                    insertStmt.setString(2, contato);
                    insertStmt.setBoolean(3, valido);
                    insertStmt.executeUpdate();
                }
            } else {
                System.out.println("O " + tipoContato + " '" + contato + "' já foi validado e está no banco de dados.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
