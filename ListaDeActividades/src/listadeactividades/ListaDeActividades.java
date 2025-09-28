/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package listadeactividades;

/**
 *
 * @author usuario
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ListaDeTareasApp - Aplicación simple de lista de tareas usando Java Swing.
 * Cumple los requisitos: añadir, marcar como completada (visualmente) y eliminar.
 *
 * Comentarios en el código explican la lógica y los manejadores de eventos.
 */
public class ListaDeActividades extends JFrame {

    // Modelo que almacena objetos Task
    private DefaultListModel<Task> modeloLista;
    private JList<Task> listaTareas;
    private JTextField campoTexto;
    private JButton btnAgregar, btnCompletar, btnEliminar;

    public ListaDeActividades() {
        initUI();      // Construcción de la interfaz
        initEvents();  // Registro de manejadores de eventos
    }

    // Construye la interfaz (componentes y layout)
    private void initUI() {
        setTitle("Lista de Tareas");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // Campo de texto en la parte superior para escribir nuevas tareas
        campoTexto = new JTextField();
        add(campoTexto, BorderLayout.NORTH);

        // Modelo y JList que mostrará objetos Task
        modeloLista = new DefaultListModel<>();
        listaTareas = new JList<>(modeloLista);
        listaTareas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Renderer personalizado para mostrar tachado cuando está completada
        listaTareas.setCellRenderer(new TaskCellRenderer());
        add(new JScrollPane(listaTareas), BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel();
        btnAgregar = new JButton("Añadir Tarea");
        btnCompletar = new JButton("Marcar como Completada");
        btnEliminar = new JButton("Eliminar Tarea");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnCompletar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Registra listeners y acciones
    private void initEvents() {
        // Añadir con botón
        btnAgregar.addActionListener(e -> agregarTarea());

        // Añadir con Enter en el JTextField (requisito)
        campoTexto.addActionListener(e -> agregarTarea());

        // Marcar/Desmarcar tarea seleccionada
        btnCompletar.addActionListener(e -> completarTarea());

        // Eliminar tarea seleccionada
        btnEliminar.addActionListener(e -> eliminarTarea());

        // Doble clic en la lista para marcar como completada (opcional)
        listaTareas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    completarTarea();
                }
            }
        });
    }

    // Agrega una tarea nueva (si el texto no está vacío)
    private void agregarTarea() {
        String texto = campoTexto.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe una tarea antes de añadir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modeloLista.addElement(new Task(texto));
        campoTexto.setText("");
        campoTexto.requestFocusInWindow();
    }

    // Alterna el estado completado de la tarea seleccionada
    private void completarTarea() {
        int index = listaTareas.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea para marcarla.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Task tarea = modeloLista.get(index);
        tarea.setCompleted(!tarea.isCompleted());
        // Re-asignar el mismo elemento para forzar que la vista se refresque
        modeloLista.set(index, tarea);
    }

    // Elimina la tarea seleccionada con confirmación
    private void eliminarTarea() {
        int index = listaTareas.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar la tarea seleccionada?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            modeloLista.remove(index);
        }
    }

    // Clase que representa una tarea (texto + estado)
    private static class Task {
        private String text;
        private boolean completed;

        public Task(String text) {
            this.text = text;
            this.completed = false;
        }
        public String getText() { return text; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean c) { completed = c; }

        @Override
        public String toString() { return text; } // usado por defecto, pero renderer lo sobreescribe
    }

    // Renderer personalizado: usa HTML para aplicar tachado cuando está completada
    private static class TaskCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (!(value instanceof Task)) return label;
            Task tarea = (Task) value;
            String textEsc = escapeHtml(tarea.getText());

            if (tarea.isCompleted()) {
                // HTML <s> aplica tachado de forma segura y portátil
                label.setText("<html><s>" + textEsc + "</s></html>");
                label.setForeground(Color.GRAY);
            } else {
                label.setText(textEsc);
                label.setForeground(Color.BLACK);
            }
            return label;
        }

        // Reemplaza caracteres especiales para evitar HTML injection
        private static String escapeHtml(String s) {
            if (s == null) return "";
            return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        }
    }

    // main para ejecutar la app
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListaDeActividades().setVisible(true));
    }
}

