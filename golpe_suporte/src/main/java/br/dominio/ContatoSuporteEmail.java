package br.dominio;

public class ContatoSuporteEmail extends ContatoSuporte {
    
    public ContatoSuporteEmail (String nome, String telefone, String email) {
        super(nome, null, email);
    }

    @Override
    public boolean isValido() {
        return getEmail() != null && getEmail().contains("@");
    }
    
}
