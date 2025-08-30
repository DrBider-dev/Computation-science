package View;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class ArrayVisualizerDemo extends JFrame {
    // Colores y medidas
    private static final Color BG = new Color(22, 22, 22); // fondo general (oscuro)
    private static final Color MINT = new Color(123, 220, 191); // mint claro para resaltar
    private static final Color LABEL_WHITE = Color.WHITE;
    private static final int CELL_WIDTH = 100;
    private static final int CELL_HEIGHT = 70;

    // Modelo
    private Integer[] array; // null = vacío

    // Componentes UI
    private JPanel cellsContainer; // contenedor horizontal de celdas
    private JScrollPane scrollPane;

    private JTextField sizeField;
    private JButton createBtn;

    private JTextField insertValueField;
    private JTextField insertIndexField;
    private JButton insertBtn;

    private JTextField searchValueField;
    private JButton searchBtn;

    private JTextField modifyIndexField;
    private JTextField modifyValueField;
    private JButton modifyBtn;

    private JTextField deleteIndexField;
    private JButton deleteBtn;

    public ArrayVisualizerDemo() {
        super("Búsqueda Lineal - Visualizador");
        initUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 500);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        getContentPane().setBackground(BG);
        getContentPane().setLayout(new BorderLayout(10, 10));

        // PANEL superior con controles
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topPanel.setBackground(BG);

        sizeField = new JTextField("8", 4);
        createBtn = new JButton("Crear arreglo");
        createBtn.addActionListener(e -> onCreateArray());

        topPanel.add(new JLabel("Tamaño:") {{ setForeground(LABEL_WHITE); setFont(getFont().deriveFont(14f)); }});
        topPanel.add(sizeField);
        topPanel.add(createBtn);

        topPanel.add(new JSeparator(SwingConstants.VERTICAL) {{ setPreferredSize(new Dimension(12,1)); }});

        insertValueField = new JTextField(6);
        insertIndexField = new JTextField(4);
        insertBtn = new JButton("Insertar");
        insertBtn.addActionListener(e -> onInsert());

        topPanel.add(new JLabel("Valor:") {{ setForeground(LABEL_WHITE); }});
        topPanel.add(insertValueField);
        topPanel.add(new JLabel("Índice (opcional):") {{ setForeground(LABEL_WHITE); }});
        topPanel.add(insertIndexField);
        topPanel.add(insertBtn);

        topPanel.add(new JSeparator(SwingConstants.VERTICAL) {{ setPreferredSize(new Dimension(12,1)); }});

        searchValueField = new JTextField(6);
        searchBtn = new JButton("Buscar");
        searchBtn.addActionListener(e -> onSearch());

        topPanel.add(new JLabel("Buscar valor:") {{ setForeground(LABEL_WHITE); }});
        topPanel.add(searchValueField);
        topPanel.add(searchBtn);

        topPanel.add(new JSeparator(SwingConstants.VERTICAL) {{ setPreferredSize(new Dimension(12,1)); }});

        modifyIndexField = new JTextField(4);
        modifyValueField = new JTextField(6);
        modifyBtn = new JButton("Modificar");
        modifyBtn.addActionListener(e -> onModify());

        topPanel.add(new JLabel("Índice:") {{ setForeground(LABEL_WHITE); }});
        topPanel.add(modifyIndexField);
        topPanel.add(new JLabel("Nuevo valor:") {{ setForeground(LABEL_WHITE); }});
        topPanel.add(modifyValueField);
        topPanel.add(modifyBtn);

        topPanel.add(new JSeparator(SwingConstants.VERTICAL) {{ setPreferredSize(new Dimension(12,1)); }});

        deleteIndexField = new JTextField(4);
        deleteBtn = new JButton("Eliminar");
        deleteBtn.addActionListener(e -> onDelete());

        topPanel.add(new JLabel("Índice eliminar:") {{ setForeground(LABEL_WHITE); }});
        topPanel.add(deleteIndexField);
        topPanel.add(deleteBtn);

        // Contenedor de celdas (horizontal) dentro de un scroll
        cellsContainer = new JPanel();
        cellsContainer.setBackground(BG);
        cellsContainer.setLayout(new BoxLayout(cellsContainer, BoxLayout.X_AXIS));
        scrollPane = new JScrollPane(cellsContainer, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        scrollPane.getViewport().setBackground(BG);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Creación inicial
        onCreateArray();
    }

    /** Crea el arreglo con el tamaño pedido */
    private void onCreateArray() {
        try {
            int size = Math.max(1, Integer.parseInt(sizeField.getText().trim()));
            array = new Integer[size];
            refreshCellsUI();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tamaño inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Inserta: si se indica índice lo coloca ahí; si no, lo pone en la primera celda libre */
    private void onInsert() {
        try {
            int value = Integer.parseInt(insertValueField.getText().trim());
            String idxText = insertIndexField.getText().trim();
            if (!idxText.isEmpty()) {
                int idx = Integer.parseInt(idxText);
                if (idx < 1 || idx > array.length) {
                    JOptionPane.showMessageDialog(this, "Índice fuera de rango (1.."+array.length+")", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                array[idx-1] = value;
            } else {
                // buscar primer null
                int free = -1;
                for (int i=0;i<array.length;i++){
                    if (array[i]==null) { free = i; break; }
                }
                if (free == -1) {
                    JOptionPane.showMessageDialog(this, "No hay espacio libre (usa un índice)", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                array[free] = value;
            }
            refreshCellsUI();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor o índice inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Busca el valor y resalta el/los encontrados */
    private void onSearch() {
        try {
            int value = Integer.parseInt(searchValueField.getText().trim());
            clearHighlights();
            boolean found = false;
            for (int i=0;i<array.length;i++){
                if (array[i] != null && array[i] == value) {
                    highlightCell(i);
                    scrollCellToVisible(i);
                    found = true;
                    // si quieres solo el primero, haces "break;" acá
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Valor no encontrado", "Buscar", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor de búsqueda inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Modifica el valor en el índice dado */
    private void onModify() {
        try {
            int idx = Integer.parseInt(modifyIndexField.getText().trim());
            int val = Integer.parseInt(modifyValueField.getText().trim());
            if (idx < 1 || idx > array.length) {
                JOptionPane.showMessageDialog(this, "Índice fuera de rango", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            array[idx-1] = val;
            refreshCellsUI();
            highlightCell(idx-1);
            scrollCellToVisible(idx-1);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Índice o valor inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Elimina (deja null) el índice */
    private void onDelete() {
        try {
            int idx = Integer.parseInt(deleteIndexField.getText().trim());
            if (idx < 1 || idx > array.length) {
                JOptionPane.showMessageDialog(this, "Índice fuera de rango", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            array[idx-1] = null;
            refreshCellsUI();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Índice inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Refresca la UI reconstruyendo las celdas a partir del array (llamar después de cambios) */
    private void refreshCellsUI() {
        cellsContainer.removeAll();
        for (int i=0;i<array.length;i++) {
            CellPanel cell = new CellPanel(i + 1, array[i]);
            cellsContainer.add(cell);
            cellsContainer.add(Box.createRigidArea(new Dimension(8, 0))); // separación
        }
        cellsContainer.revalidate();
        cellsContainer.repaint();
    }

    /** Remueve todos los highlights (vuelve transparente las celdas) */
    private void clearHighlights() {
        for (Component c : cellsContainer.getComponents()) {
            if (c instanceof CellPanel) {
                ((CellPanel)c).setHighlighted(false);
            }
        }
    }

    /** Resalta una celda y hace un pequeño parpadeo */
    private void highlightCell(int index) {
        Component comp = getCellComponent(index);
        if (!(comp instanceof CellPanel)) return;
        CellPanel cell = (CellPanel) comp;
        // parpadeo simple: 2 cambios
        final int[] state = {0};
        Timer t = new Timer(220, null);
        t.addActionListener(evt -> {
            if (state[0] == 0) {
                cell.setHighlighted(true);
            } else if (state[0] == 1) {
                cell.setHighlighted(false);
            } else if (state[0] == 2) {
                cell.setHighlighted(true);
            } else {
                t.stop();
            }
            state[0]++;
        });
        t.setInitialDelay(0);
        t.start();
    }

    /** Lleva la celda al centro de la vista del scroll */
    private void scrollCellToVisible(int index) {
        Component comp = getCellComponent(index);
        if (comp != null) {
            Rectangle r = comp.getBounds();
            // desplazar un poco para centrar
            JViewport v = scrollPane.getViewport();
            Rectangle viewRect = v.getViewRect();
            r.x = Math.max(0, r.x - (viewRect.width - r.width)/2);
            v.scrollRectToVisible(r);
        }
    }

    /** Devuelve el componente CellPanel correspondiente al índice (0-based) */
    private Component getCellComponent(int index) {
        // record: entre cada celda hay un rigid area (espacio), entonces el índice real de componente = index*2
        int compIndex = index * 2;
        if (compIndex >= 0 && compIndex < cellsContainer.getComponentCount()) {
            return cellsContainer.getComponent(compIndex);
        }
        return null;
    }

    // Panel que representa una celda del arreglo
    private class CellPanel extends JPanel {
        private JLabel posLabel;
        private JLabel valLabel;

        public CellPanel(int position, Integer value) {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
            setMaximumSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
            setBackground(new Color(0,0,0,0)); // inicialmente transparente
            setOpaque(false); // mantener transparencia
            setBorder(new LineBorder(Color.WHITE, 1, true));

            posLabel = new JLabel(String.valueOf(position), SwingConstants.CENTER);
            posLabel.setForeground(LABEL_WHITE);
            posLabel.setFont(getFont().deriveFont(Font.PLAIN, 12f));
            posLabel.setOpaque(false);
            posLabel.setBorder(BorderFactory.createEmptyBorder(4,4,0,4));

            valLabel = new JLabel(value == null ? "" : String.valueOf(value), SwingConstants.CENTER);
            valLabel.setForeground(LABEL_WHITE);
            valLabel.setFont(getFont().deriveFont(Font.BOLD, 18f));
            valLabel.setOpaque(false);
            valLabel.setBorder(BorderFactory.createEmptyBorder(0,4,6,4));

            add(posLabel, BorderLayout.NORTH);
            add(valLabel, BorderLayout.CENTER);
        }

        public void setHighlighted(boolean highlight) {
            if (highlight) {
                setOpaque(true);
                setBackground(MINT);
                posLabel.setForeground(Color.black);
                valLabel.setForeground(Color.black);
            } else {
                setOpaque(false);
                setBackground(new Color(0,0,0,0));
                posLabel.setForeground(LABEL_WHITE);
                valLabel.setForeground(LABEL_WHITE);
            }
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArrayVisualizerDemo demo = new ArrayVisualizerDemo();
            demo.setVisible(true);
        });
    }
}
