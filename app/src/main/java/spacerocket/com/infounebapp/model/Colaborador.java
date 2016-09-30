package spacerocket.com.infounebapp.model;

/**
 * Created by blackwolf on 18/09/16.
 */
public class Colaborador {

    private String nome, tipo, foto, link;

    public Colaborador(){}

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
