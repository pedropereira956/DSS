package RestaurantesDL;

import RestaurantesLN.ssProducao.Tarefa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProducaoDAO {
    private Connection connection;

    public ProducaoDAO(Connection conn) { this.connection = conn; }

    public void criarTarefa(int pedidoId, String item, int qtd, String posto, int prioridade) throws SQLException {
        String sql = "INSERT INTO producao_tarefas (pedido_id, item_nome, quantidade, posto_trabalho, prioridade) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, pedidoId);
            st.setString(2, item);
            st.setInt(3, qtd);
            st.setString(4, posto);
            st.setInt(5, prioridade);
            st.executeUpdate();
        }
    }

    public List<Tarefa> getTarefasPorPosto(String posto) throws SQLException {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = (posto.equals("TODOS"))
                ? "SELECT * FROM producao_tarefas WHERE estado = 'PENDENTE' ORDER BY pedido_id ASC, prioridade ASC"
                : "SELECT * FROM producao_tarefas WHERE posto_trabalho = ? AND estado = 'PENDENTE' ORDER BY pedido_id ASC, prioridade ASC";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            if (!posto.equals("TODOS")) st.setString(1, posto);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                tarefas.add(new Tarefa(
                        rs.getInt("id_tarefa"),
                        rs.getInt("pedido_id"),
                        rs.getString("item_nome"),
                        rs.getInt("quantidade"),
                        rs.getInt("prioridade")
                ));
            }
        }
        return tarefas;
    }

    public void atualizarEstadoTarefa(int idTarefa, String novoEstado) throws SQLException {
        String sql = "UPDATE producao_tarefas SET estado = ? WHERE id_tarefa = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, novoEstado);
            st.setInt(2, idTarefa);

            int rowsUpdated = st.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException("Erro: Tarefa com ID " + idTarefa + " não encontrada.");
            }
        }
    }

    public boolean temTarefasPendentes(int idPedido) throws SQLException {
        String sql = "SELECT COUNT(*) FROM producao_tarefas WHERE pedido_id = ? AND estado = 'PENDENTE'";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, idPedido);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public int getPedidoIdDeTarefa(int idTarefa) throws SQLException {
        String sql = "SELECT pedido_id FROM producao_tarefas WHERE id_tarefa = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, idTarefa);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt("pedido_id");
        }
        return -1;
    }
}