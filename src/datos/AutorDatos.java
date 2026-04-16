package datos;

import beans.AutorBeans;
import beans.CategoriaBeans;
import util.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDatos {

    public List<AutorBeans> listarAutores() {
        List<AutorBeans> lista = new ArrayList<>();
        String sql = "SELECT id_autor, nombre, nacionalidad FROM autor ORDER BY nombre";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AutorBeans a = new AutorBeans();
                a.setId_autor(rs.getInt("id_autor"));
                a.setNombre(rs.getString("nombre"));
                a.setNacionalidad(rs.getString("nacionalidad"));
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<CategoriaBeans> listarCategorias() {
        List<CategoriaBeans> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre_categoria FROM categoria ORDER BY nombre_categoria";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CategoriaBeans c = new CategoriaBeans();
                c.setId_categoria(rs.getInt("id_categoria"));
                c.setNombre_categoria(rs.getString("nombre_categoria"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}