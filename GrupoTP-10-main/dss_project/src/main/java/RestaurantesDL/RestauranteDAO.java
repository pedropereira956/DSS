package RestaurantesDL;

import java.sql.*;

import RestaurantesLN.ssGestao.Restaurante;

public class RestauranteDAO extends AbstractDAO<Restaurante> {
    private static RestauranteDAO instance = null;

    private RestauranteDAO() {
        super("restaurantes", "id");
    }

    public static RestauranteDAO getInstance() {
        if (RestauranteDAO.instance == null) {
            RestauranteDAO.instance = new RestauranteDAO();
        }
        return RestauranteDAO.instance;
    }

    public boolean containsValue(Object value) {
        if (value instanceof Restaurante && value != null) {
            Restaurante r = this.get(((Restaurante) value).getId());
            return value.equals(r);
        }
        return false;
    }

    public void atualizarQuantidadeStock(String restauranteId, String itemNome, int quantidadeDiferenca) {
        String sql = "UPDATE stock SET quantidade = quantidade + ? WHERE restaurante_id = ? AND item_nome = ?";

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, quantidadeDiferenca);
            st.setString(2, restauranteId);
            st.setString(3, itemNome);

            int rowsUpdated = st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar stock na base de dados", e);
        }
    }

    public Restaurante put(String key, Restaurante value) {
        boolean turnAutoCommitBackOn = false;
        try {
            turnAutoCommitBackOn = this.connection.getAutoCommit();
            this.connection.setAutoCommit(false);

            Restaurante res = this.remove(key);

            try (PreparedStatement statement = this.connection.prepareStatement(
                    "INSERT INTO restaurantes(id, nome, localizacao, capacidadeStock) VALUES (?, ?, ?, ?)")) {

                statement.setString(1, value.getId());
                statement.setString(2, value.getNome());
                statement.setString(3, value.getLocalizacao());
                statement.setInt(4, value.getCapacidadeStock());

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

    protected Restaurante decodeTuple(ResultSet result) throws SQLException {
        String id = result.getString("id");
        String nome = result.getString("nome");
        String localizacao = result.getString("localizacao");
        int capacidade = result.getInt("capacidadeStock");

        return new Restaurante(id, nome, localizacao, capacidade);
    }

    public Restaurante get(Object key) {
        Restaurante r = super.get(key);

        if (r != null) {
            try (Connection conn = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
                 PreparedStatement st = conn.prepareStatement("SELECT item_nome, quantidade FROM stock WHERE restaurante_id = ?")) {

                st.setString(1, (String) key);
                ResultSet rs = st.executeQuery();

                while (rs.next()) {
                    r.adicionarStock(rs.getString("item_nome"), rs.getInt("quantidade"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return r;
    }
}