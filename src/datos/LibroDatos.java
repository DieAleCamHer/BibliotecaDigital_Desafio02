package datos;

import beans.LibroBeans;
import util.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDatos {

    // === NUEVO MÉTODO: Validación de título duplicado ===
    public boolean existeTitulo(String titulo, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM libro WHERE LOWER(titulo) = LOWER(?) AND id_libro != ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, titulo.trim());
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertar(LibroBeans libro) {
        String sql = "INSERT INTO libro (titulo, año_publicacion, id_autor, id_categoria) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setInt(2, libro.getAño_publicacion());
            ps.setInt(3, libro.getId_autor());
            ps.setInt(4, libro.getId_categoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(LibroBeans libro) {
        String sql = "UPDATE libro SET titulo=?, año_publicacion=?, id_autor=?, id_categoria=? WHERE id_libro=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setInt(2, libro.getAño_publicacion());
            ps.setInt(3, libro.getId_autor());
            ps.setInt(4, libro.getId_categoria());
            ps.setInt(5, libro.getId_libro());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id_libro) {
        String sql = "DELETE FROM libro WHERE id_libro = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_libro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<LibroBeans> listarLibros() {
        return listarConFiltros(0, 0);
    }

    public List<LibroBeans> listarConFiltros(int idAutor, int idCategoria) {
        List<LibroBeans> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT l.id_libro, l.titulo, l.año_publicacion, l.id_autor, l.id_categoria, " +
                        "a.nombre AS nombre_autor, c.nombre_categoria AS nombre_categoria " +
                        "FROM libro l " +
                        "INNER JOIN autor a ON l.id_autor = a.id_autor " +
                        "INNER JOIN categoria c ON l.id_categoria = c.id_categoria "
        );

        boolean tieneWhere = false;
        if (idAutor > 0) {
            sql.append("WHERE l.id_autor = ? ");
            tieneWhere = true;
        }
        if (idCategoria > 0) {
            sql.append(tieneWhere ? "AND " : "WHERE ");
            sql.append("l.id_categoria = ? ");
        }
        sql.append("ORDER BY l.id_libro");

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (idAutor > 0) ps.setInt(paramIndex++, idAutor);
            if (idCategoria > 0) ps.setInt(paramIndex, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LibroBeans l = new LibroBeans();
                    l.setId_libro(rs.getInt("id_libro"));
                    l.setTitulo(rs.getString("titulo"));
                    l.setAño_publicacion(rs.getInt("año_publicacion"));
                    l.setId_autor(rs.getInt("id_autor"));
                    l.setId_categoria(rs.getInt("id_categoria"));
                    l.setNombreAutor(rs.getString("nombre_autor"));
                    l.setNombreCategoria(rs.getString("nombre_categoria"));
                    lista.add(l);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public LibroBeans obtenerPorId(int id_libro) {
        LibroBeans libro = null;
        String sql = "SELECT l.id_libro, l.titulo, l.año_publicacion, l.id_autor, l.id_categoria, " +
                "a.nombre AS nombre_autor, c.nombre_categoria AS nombre_categoria " +
                "FROM libro l " +
                "INNER JOIN autor a ON l.id_autor = a.id_autor " +
                "INNER JOIN categoria c ON l.id_categoria = c.id_categoria " +
                "WHERE l.id_libro = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_libro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    libro = new LibroBeans();
                    libro.setId_libro(rs.getInt("id_libro"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAño_publicacion(rs.getInt("año_publicacion"));
                    libro.setId_autor(rs.getInt("id_autor"));
                    libro.setId_categoria(rs.getInt("id_categoria"));
                    libro.setNombreAutor(rs.getString("nombre_autor"));
                    libro.setNombreCategoria(rs.getString("nombre_categoria"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libro;
    }
}