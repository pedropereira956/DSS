package RestaurantesDL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import RestaurantesLN.ssGestao.Funcionario;

public class FuncionarioDAO extends AbstractDAO<Funcionario> {
    private static FuncionarioDAO instance = null;

    private FuncionarioDAO() {
        super("funcionarios", "id");
    }

    public static FuncionarioDAO getInstance() {
        if (FuncionarioDAO.instance == null) {
            FuncionarioDAO.instance = new FuncionarioDAO();
        }
        return FuncionarioDAO.instance;
    }

    public boolean containsValue(Object value) {
        if (value instanceof Funcionario && value != null) {
            Funcionario f = this.get(((Funcionario) value).getId());
            return value.equals(f);
        }
        return false;
    }

    public Funcionario put(String key, Funcionario value) {
        boolean turnAutoCommitBackOn = false;
        try {
            turnAutoCommitBackOn = this.connection.getAutoCommit();
            this.connection.setAutoCommit(false);

            Funcionario res = this.remove(key);

            try (PreparedStatement statement = this.connection.prepareStatement(
                    "INSERT INTO funcionarios(id, nome, password, cargo, restaurante_id) VALUES (?, ?, ?, ?, ?)")) {

                statement.setString(1, value.getId());
                statement.setString(2, value.getNome());
                statement.setString(3, value.getPassword());
                statement.setString(4, value.getCargo());
                statement.setString(5, value.getIdRestaurante());

                statement.executeUpdate();
            }

            this.connection.commit();
            return res;
        } catch (SQLException e) {
            try { this.connection.rollback(); } catch (SQLException ex) { }
            throw new RuntimeException(e);
        } finally {
            try { if (turnAutoCommitBackOn) this.connection.setAutoCommit(true); }
            catch (SQLException e) { throw new RuntimeException(e); }
        }
    }

    public String getCargo(String idFuncionario) {
        String sql = "SELECT cargo FROM funcionarios WHERE id = ?";
        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, idFuncionario);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cargo");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao ir buscar o cargo do funcionário", e);
        }
        return "";
    }

    @Override
    protected Funcionario decodeTuple(ResultSet result) throws SQLException {
        String id = result.getString("id");
        String nome = result.getString("nome");
        String password = result.getString("password");
        String cargo = result.getString("cargo");
        String idRestaurante = result.getString("restaurante_id");

        return new Funcionario(id, nome, password, cargo, idRestaurante);
    }
}