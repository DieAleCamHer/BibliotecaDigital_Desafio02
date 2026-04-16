package vista;

import beans.AutorBeans;
import beans.CategoriaBeans;
import beans.LibroBeans;
import datos.AutorDatos;
import datos.LibroDatos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.List;

public class frmBiblioteca extends JFrame {
    private JTextField txtTitulo, txtAno;
    private JComboBox<AutorBeans> cmbAutor, cmbFiltroAutor;
    private JComboBox<CategoriaBeans> cmbCategoria, cmbFiltroCategoria;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLimpiar, btnFiltrar, btnTodos;
    private JTable tblLibros;
    private DefaultTableModel model;
    private int idLibroSeleccionado = -1;

    private final LibroDatos libroDatos = new LibroDatos();
    private final AutorDatos autorDatos = new AutorDatos();

    public frmBiblioteca() {
        setTitle("📚 Sistema de Biblioteca Digital - UDB");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // === CAMPOS ===
        JLabel lblTitulo = new JLabel("Título del libro:");
        lblTitulo.setBounds(20, 20, 120, 25);
        add(lblTitulo);
        txtTitulo = new JTextField();
        txtTitulo.setBounds(150, 20, 280, 25);
        add(txtTitulo);

        JLabel lblAno = new JLabel("Año de publicación:");
        lblAno.setBounds(20, 60, 140, 25);
        add(lblAno);
        txtAno = new JTextField();
        txtAno.setBounds(150, 60, 80, 25);
        add(txtAno);

        txtAno.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });

        JLabel lblAutor = new JLabel("Autor:");
        lblAutor.setBounds(20, 100, 120, 25);
        add(lblAutor);
        cmbAutor = new JComboBox<>();
        cmbAutor.setBounds(150, 100, 280, 25);
        add(cmbAutor);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(20, 140, 120, 25);
        add(lblCategoria);
        cmbCategoria = new JComboBox<>();
        cmbCategoria.setBounds(150, 140, 280, 25);
        add(cmbCategoria);

        // === BOTONES ===
        btnGuardar = new JButton("💾 Guardar");
        btnGuardar.setBounds(20, 190, 110, 35);
        add(btnGuardar);

        btnEditar = new JButton("✏️ Editar");
        btnEditar.setBounds(140, 190, 110, 35);
        add(btnEditar);

        btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setBounds(260, 190, 110, 35);
        add(btnEliminar);

        btnLimpiar = new JButton("🧹 Limpiar");
        btnLimpiar.setBounds(380, 190, 110, 35);
        add(btnLimpiar);

        // === FILTROS ===
        JLabel lblFiltro = new JLabel("🔎 Filtrar por:");
        lblFiltro.setBounds(520, 20, 100, 25);
        add(lblFiltro);

        cmbFiltroAutor = new JComboBox<>();
        cmbFiltroAutor.setBounds(620, 20, 220, 25);
        add(cmbFiltroAutor);

        cmbFiltroCategoria = new JComboBox<>();
        cmbFiltroCategoria.setBounds(620, 60, 220, 25);
        add(cmbFiltroCategoria);

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBounds(860, 20, 100, 30);
        add(btnFiltrar);

        btnTodos = new JButton("Mostrar Todos");
        btnTodos.setBounds(860, 60, 140, 30);
        add(btnTodos);

        // === TABLA ===
        JLabel lblTabla = new JLabel("Libros registrados:");
        lblTabla.setBounds(20, 250, 200, 25);
        add(lblTabla);

        model = new DefaultTableModel(new Object[0][0],
                new String[]{"ID", "Título", "Año", "Autor", "Categoría"});
        tblLibros = new JTable(model);
        JScrollPane scroll = new JScrollPane(tblLibros);
        scroll.setBounds(20, 280, 1050, 350);
        add(scroll);

        cargarCombos();
        cargarTabla();

        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnFiltrar.addActionListener(e -> filtrar());
        btnTodos.addActionListener(e -> { cargarTabla(); idLibroSeleccionado = -1; });

        tblLibros.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editar();
            }
        });
    }

    private void cargarCombos() {
        // Combo para ingresar libro
        DefaultComboBoxModel<AutorBeans> modelA = new DefaultComboBoxModel<>();
        autorDatos.listarAutores().forEach(modelA::addElement);
        cmbAutor.setModel(modelA);

        DefaultComboBoxModel<CategoriaBeans> modelC = new DefaultComboBoxModel<>();
        autorDatos.listarCategorias().forEach(modelC::addElement);
        cmbCategoria.setModel(modelC);

        // === COMBO FILTRO AUTOR (CORREGIDO) ===
        DefaultComboBoxModel<AutorBeans> modelFA = new DefaultComboBoxModel<>();
        modelFA.addElement(new AutorBeans(0, "Todos los autores", ""));
        autorDatos.listarAutores().forEach(modelFA::addElement);   // ← AQUÍ ESTABA EL ERROR
        cmbFiltroAutor.setModel(modelFA);

        // Combo filtro categoría
        DefaultComboBoxModel<CategoriaBeans> modelFC = new DefaultComboBoxModel<>();
        modelFC.addElement(new CategoriaBeans(0, "Todas las categorías"));
        autorDatos.listarCategorias().forEach(modelFC::addElement);
        cmbFiltroCategoria.setModel(modelFC);
    }

    private void cargarTabla() {
        List<LibroBeans> lista = libroDatos.listarLibros();
        model.setRowCount(0);
        for (LibroBeans l : lista) {
            model.addRow(new Object[]{
                    l.getId_libro(), l.getTitulo(), l.getAño_publicacion(),
                    l.getNombreAutor(), l.getNombreCategoria()
            });
        }
    }

    private void guardar() {
        String titulo = txtTitulo.getText().trim();
        String anoStr = txtAno.getText().trim();

        if (titulo.isEmpty() || anoStr.isEmpty() ||
                cmbAutor.getSelectedItem() == null || cmbCategoria.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (titulo.length() > 200) {
            JOptionPane.showMessageDialog(this, "⚠️ El título no puede tener más de 200 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int ano;
        try {
            ano = Integer.parseInt(anoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ El año debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int añoActual = Calendar.getInstance().get(Calendar.YEAR);
        if (ano < 1000 || ano > añoActual + 5) {
            JOptionPane.showMessageDialog(this, "❌ El año debe estar entre 1000 y " + (añoActual + 5), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // === VALIDACIÓN DE TÍTULO DUPLICADO ===
        int idExcluir = (idLibroSeleccionado > 0) ? idLibroSeleccionado : 0;
        if (libroDatos.existeTitulo(titulo, idExcluir)) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Ya existe un libro con el título \"" + titulo + "\"\nNo se permite duplicados.",
                    "Título duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LibroBeans libro = new LibroBeans();
            libro.setTitulo(titulo);
            libro.setAño_publicacion(ano);

            AutorBeans aut = (AutorBeans) cmbAutor.getSelectedItem();
            CategoriaBeans cat = (CategoriaBeans) cmbCategoria.getSelectedItem();
            libro.setId_autor(aut.getId_autor());
            libro.setId_categoria(cat.getId_categoria());

            boolean exito;
            if (idLibroSeleccionado > 0) {
                libro.setId_libro(idLibroSeleccionado);
                exito = libroDatos.actualizar(libro);
            } else {
                exito = libroDatos.insertar(libro);
            }

            if (exito) {
                JOptionPane.showMessageDialog(this, "✅ Operación realizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al guardar/actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        int fila = tblLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "❌ Seleccione un libro de la tabla para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tblLibros.getValueAt(fila, 0);
        idLibroSeleccionado = id;

        LibroBeans libro = libroDatos.obtenerPorId(id);
        if (libro != null) {
            txtTitulo.setText(libro.getTitulo());
            txtAno.setText(String.valueOf(libro.getAño_publicacion()));
            seleccionarCombo(cmbAutor, libro.getId_autor());
            seleccionarCombo(cmbCategoria, libro.getId_categoria());
        }
    }

    private void seleccionarCombo(JComboBox<?> combo, int idBuscado) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            if (item instanceof AutorBeans && ((AutorBeans) item).getId_autor() == idBuscado) {
                combo.setSelectedIndex(i);
                return;
            }
            if (item instanceof CategoriaBeans && ((CategoriaBeans) item).getId_categoria() == idBuscado) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void eliminar() {
        int fila = tblLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "❌ Seleccione un libro de la tabla para eliminar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tblLibros.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este libro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (libroDatos.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "✅ Libro eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo eliminar el libro", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtAno.setText("");
        if (cmbAutor.getItemCount() > 0) cmbAutor.setSelectedIndex(0);
        if (cmbCategoria.getItemCount() > 0) cmbCategoria.setSelectedIndex(0);
        idLibroSeleccionado = -1;
    }

    private void filtrar() {
        AutorBeans autF = (AutorBeans) cmbFiltroAutor.getSelectedItem();
        CategoriaBeans catF = (CategoriaBeans) cmbFiltroCategoria.getSelectedItem();

        int idA = (autF != null && autF.getId_autor() > 0) ? autF.getId_autor() : 0;
        int idC = (catF != null && catF.getId_categoria() > 0) ? catF.getId_categoria() : 0;

        List<LibroBeans> lista = libroDatos.listarConFiltros(idA, idC);
        model.setRowCount(0);
        for (LibroBeans l : lista) {
            model.addRow(new Object[]{
                    l.getId_libro(), l.getTitulo(), l.getAño_publicacion(),
                    l.getNombreAutor(), l.getNombreCategoria()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new frmBiblioteca().setVisible(true));
    }
}