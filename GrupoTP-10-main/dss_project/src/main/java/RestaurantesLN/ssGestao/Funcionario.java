package RestaurantesLN.ssGestao;

import java.util.Objects;

public class Funcionario {
    private String id;
    private String nome;
    private String password;
    private String cargo;
    private String idRestaurante;

    public Funcionario(String id, String nome, String password, String cargo, String idRestaurante) {
        this.id = id;
        this.nome = nome;
        this.password = password;
        this.cargo = cargo;
        this.idRestaurante = idRestaurante;
    }

    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getPassword() { return password; }
    public String getCargo() { return cargo; }
    public String getIdRestaurante() { return idRestaurante; }

    public void setPassword(String password) { this.password = password; }

    public boolean validarLogin(String password) {
        return this.password.equals(password);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        return Objects.equals(id, that.id);
    }

    public String toString() {
        return "Funcionario{" + "nome='" + nome + '\'' + ", cargo='" + cargo + '\'' + '}';
    }
}