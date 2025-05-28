package br.dominio;

public class ContatoSuporteTelefone extends ContatoSuporte {

    public ContatoSuporteTelefone(String nome, String telefone, String email) {
        super(nome, telefone, null);
    }

    @Override
    public boolean isValido() {
        return getTelefone() != null && getTelefone().length() == 10;
    }

}
