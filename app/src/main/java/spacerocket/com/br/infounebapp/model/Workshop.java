package spacerocket.com.br.infounebapp.model;

/**
 * Created by Diôgo on 02/09/16.
 */
public class Workshop {

    private String titulo, palestrante, horario, dia, data, foto;

    //Introducing the dummy constructor
    public Workshop() {
    }
   /* public Workshop(String titulo, String palestrante, String horario, String dia,String data, int thumbnail) {
        this.titulo = titulo;
        this.palestrante = palestrante;
        this.horario = horario;
        this.dia = dia;
        this.foto = thumbnail;
        this.data = data;
    }*/

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPalestrante() {
        return palestrante;
    }

    public void setPalestrante(String palestrante) {
        this.palestrante = palestrante;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
