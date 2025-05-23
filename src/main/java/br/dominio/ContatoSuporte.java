package dominio;

public abstract class ContatoSuporte {
    protected String nome;
    protected String telefone;
    protected String email;

    public ContatoSuporte(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }


    public boolean isValido() {
        return telefone.replaceAll("[^0-9]", "").length() == 11;
    }
    
}
