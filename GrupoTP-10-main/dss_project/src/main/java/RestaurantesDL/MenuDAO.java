package RestaurantesDL;

import java.sql.*;
import java.util.*;

public class MenuDAO {

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
    }

    public static Map<String, Double> getPrecos() {
        Map<String, Double> precos = new HashMap<>();
        String sql = "SELECT nome, preco FROM menus";

        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                precos.put(rs.getString("nome"), rs.getDouble("preco"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao ler preços: " + e.getMessage());
        }
        return precos;
    }

    public static Map<String, List<String>> getReceitas() {
        Map<String, List<String>> receitas = new HashMap<>();
        String sql = "SELECT menu_nome, ingrediente_nome FROM menu_receitas";

        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                String menu = rs.getString("menu_nome");
                String ingrediente = rs.getString("ingrediente_nome");
                receitas.computeIfAbsent(menu, k -> new ArrayList<>()).add(ingrediente);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao ler receitas: " + e.getMessage());
        }
        return receitas;
    }
}