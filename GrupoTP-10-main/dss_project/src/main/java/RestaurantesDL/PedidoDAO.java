package RestaurantesDL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import RestaurantesLN.ssVendas.Pedido;

public class PedidoDAO extends AbstractDAO<Pedido> {
    private static PedidoDAO instance = null;

    private PedidoDAO() {
        super("pedidos", "id_interno");
    }

    public static PedidoDAO getInstance() {
        if (PedidoDAO.instance == null) {
            PedidoDAO.instance = new PedidoDAO();
        }
        return PedidoDAO.instance;
    }

    public boolean containsValue(Object value) {
        if (value instanceof Pedido && value != null) {
            Pedido p = this.get(String.valueOf(((Pedido) value).getNumero()));
            return value.equals(p);
        }
        return false;
    }

    public Pedido put(String key, Pedido value) {
        boolean turnAutoCommitBackOn = false;
        try {
            turnAutoCommitBackOn = this.connection.getAutoCommit();
            this.connection.setAutoCommit(false);

            String sqlPedido = "INSERT INTO pedidos(numero, restaurante_id, estado, hora, notas, metodo_pagamento) VALUES (?, ?, ?, ?, ?, ?)";
            int idGerado = -1;

            try (PreparedStatement stm = this.connection.prepareStatement(sqlPedido, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                stm.setInt(1, value.getNumero());
                stm.setString(2, value.getIdRestaurante());
                stm.setString(3, value.getEstado());
                stm.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

                stm.setString(5, value.getNotas());

                if (value.getMetodoPagamento() != null) {
                    stm.setString(6, value.getMetodoPagamento().name());
                } else {
                    stm.setString(6, "DINHEIRO");
                }

                stm.executeUpdate();

                try (ResultSet rs = stm.getGeneratedKeys()) {
                    if (rs.next()) idGerado = rs.getInt(1);
                }
            }

            // Inserção dos itens (mantém-se igual, usando o idGerado)
            String sqlItens = "INSERT INTO itens_pedido(pedido_id, produto_nome, quantidade) VALUES (?, ?, ?)";
            try (PreparedStatement stmItens = this.connection.prepareStatement(sqlItens)) {
                for (Map.Entry<String, Integer> entry : value.getItens().entrySet()) {
                    stmItens.setInt(1, idGerado);
                    stmItens.setString(2, entry.getKey());
                    stmItens.setInt(3, entry.getValue());
                    stmItens.executeUpdate();
                }
            }

            this.connection.commit();
            return value;
        } catch (SQLException e) {
            try { if (this.connection != null) this.connection.rollback(); } catch (SQLException ex) { }
            throw new RuntimeException("Erro ao inserir pedido: " + e.getMessage(), e);
        } finally {
            try { if (turnAutoCommitBackOn) this.connection.setAutoCommit(true); }
            catch (SQLException e) { }
        }
    }

    public void atualizarEstado(int numPedido, String idRest, String novoEstado) {
        try (PreparedStatement st = this.connection.prepareStatement(
                "UPDATE pedidos SET estado = ? WHERE numero = ? AND restaurante_id = ?")) {
            st.setString(1, novoEstado);
            st.setInt(2, numPedido);
            st.setString(3, idRest);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pedido> getPedidosPorEstado(String idRestaurante, String estado) {
        List<Pedido> resultado = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE restaurante_id = ? AND estado = ?";

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, idRestaurante);
            st.setString(2, estado);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                resultado.add(this.decodeTuple(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao ir buscar pedidos por estado: " + e.getMessage());
        }
        return resultado;
    }

    protected Pedido decodeTuple(ResultSet result) throws SQLException {
        int numero = result.getInt("numero");
        String idRest = result.getString("restaurante_id");
        String estado = result.getString("estado");

        Pedido p = new Pedido(numero, idRest);
        p.setEstado(estado);

        return p;
    }
}