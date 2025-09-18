package View;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * Digital Tree Search (Árbol Digital de Búsqueda)
 * --------------------
 * Versión visual mejorada para ajustarse a la estética solicitada y con
 * funcionalidad para guardar/recuperar el árbol en un archivo de texto.
 *
 * - Fondo blanco predominante.
 * - Detalles azul claro alrededor del panel izquierdo.
 * - Botones oscuros (alto contraste) con texto blanco.
 * - Nodos del árbol ligeramente más grandes.
 * - Guardar / Guardar y Cerrar / Recuperar (cargar) desde archivo de texto.
 *
 * Formato de archivo usado para guardar: una letra por línea (orden de inserción).
 *
 * Compilar y ejecutar:
 *   javac BinaryTreeLetterTree.java
 *   java OTRA_CLASE_QUE_USE_EL_SINGLETON
 */
public class DigitalTreeSearch extends JFrame {

    // ------------------ Estructura del árbol ------------------
    static class Node {
        char value; // letra almacenada (A..Z)
        Node left, right;
        Node(char v) { value = v; }
    }

    private Node root = null;
    private final java.util.List<Character> insertionOrder = new ArrayList<>();

    // GUI components
    private final TreePanel treePanel = new TreePanel();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> letterList = new JList<>(listModel);
    private final JTextField inputField = new JTextField(20);
    private final JButton insertBtn = new JButton("Insertar");
    private final JButton deleteBtn = new JButton("Eliminar");
    private final JButton resetBtn = new JButton("Reiniciar");
    // nuevos botones
    private final JButton saveBtn = new JButton("Guardar");
    private final JButton saveCloseBtn = new JButton("Guardar y Salir");
    private final JButton loadBtn = new JButton("Cargar");
    private final JButton volverBtn = new JButton("Volver");

    // Animación
    private Timer animTimer;

    public DigitalTreeSearch() {
        super("Arboles Digitales de Búsqueda");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        // Aplicar fondo blanco general
        getContentPane().setBackground(Color.WHITE);

        // Panel de controles superior (blanco, con etiqueta azul)
        JPanel control = new JPanel(new GridBagLayout());
        control.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lbl = new JLabel("Clave");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        control.add(lbl, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        control.add(inputField, gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setOpaque(false);
        btns.add(insertBtn);
        btns.add(deleteBtn);
        btns.add(resetBtn);
        btns.add(saveBtn);
        btns.add(saveCloseBtn);
        btns.add(loadBtn);
        btns.add(volverBtn);
        control.add(btns, gbc);
        

        // Estilizar botones oscuros (alto contraste)
        Font btnFont = new Font("SansSerif", Font.BOLD, 13);
        JButton[] allBtns = {insertBtn, deleteBtn, resetBtn, saveBtn, saveCloseBtn, loadBtn, volverBtn};
        for (JButton b : allBtns) {
            b.setFont(btnFont);
            b.setBackground(new Color(40, 40, 40));
            b.setForeground(Color.WHITE);
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setPreferredSize(new Dimension(140, 32));
        }

        // Panel derecho: lista de letras (blanco)
        letterList.setVisibleRowCount(8);
        letterList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane listScroll = new JScrollPane(letterList);
        listScroll.setPreferredSize(new Dimension(180, 160));
        JPanel right = new JPanel(new BorderLayout(8,8));
        right.setBackground(Color.WHITE);
        JLabel rightTitle = new JLabel("Letras (orden de inserción):");
        rightTitle.setFont(new Font("Serif", Font.BOLD, 16));
        rightTitle.setForeground(new Color(20, 90, 180)); // azul detalle
        right.add(rightTitle, BorderLayout.NORTH);
        right.add(listScroll, BorderLayout.CENTER);

        // Panel izquierdo: contenedor azul claro con el panel del arbol dentro
        Color blueDetail = new Color(93, 154, 255);
        JPanel leftContainer = new JPanel(new BorderLayout());
        leftContainer.setBackground(blueDetail);
        leftContainer.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));

        JLabel leftTitle = new JLabel("Arbol Digital de Búsqueda");
        leftTitle.setHorizontalAlignment(SwingConstants.CENTER);
        leftTitle.setFont(new Font("Serif", Font.BOLD, 28));
        leftTitle.setForeground(Color.WHITE);
        leftTitle.setBorder(BorderFactory.createEmptyBorder(6,6,12,6));
        leftContainer.add(leftTitle, BorderLayout.NORTH);

        // panel interior blanco que contendrá el dibujo del árbol (con padding)
        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(Color.WHITE);
        inner.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        inner.add(treePanel, BorderLayout.CENTER);
        leftContainer.add(inner, BorderLayout.CENTER);

        // Split pane entre izquierda (decorada) y derecha (información)
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftContainer, right);
        split.setDividerLocation(800);
        split.setBorder(null);

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(control, BorderLayout.NORTH);
        getContentPane().add(split, BorderLayout.CENTER);

        // Listeners
        insertBtn.addActionListener(e -> insertTextAnimated(inputField.getText()));
        deleteBtn.addActionListener(e -> deleteSelected());
        resetBtn.addActionListener(e -> resetAll());
        saveBtn.addActionListener(e -> onSave(false));
        saveCloseBtn.addActionListener(e -> onSave(true));
        loadBtn.addActionListener(e -> onLoad());
        volverBtn.addActionListener(e -> onReturn());

        updateListModel();
    }

    // ------------------ Algoritmos básicos ------------------
    private static String letterTo5Bits(char ch) {
        ch = Character.toUpperCase(ch);
        if (ch < 'A' || ch > 'Z') return null;
        int pos = (ch - 'A') + 1; // A -> 1
        String s = Integer.toBinaryString(pos);
        while (s.length() < 5) s = "0" + s;
        if (s.length() > 5) s = s.substring(s.length()-5);
        return s;
    }

    private boolean existsInTree(char ch) {
        return insertionOrder.contains(Character.toUpperCase(ch));
    }

    private boolean insertDirect(char ch) {
        ch = Character.toUpperCase(ch);
        if (ch < 'A' || ch > 'Z') return false;
        if (existsInTree(ch)) return false;
        String bits = letterTo5Bits(ch);
        if (root == null) {
            root = new Node(ch);
            insertionOrder.add(ch);
            return true;
        }
        Node cur = root;
        for (int i = 0; i < bits.length(); ++i) {
            char b = bits.charAt(i);
            if (b == '0') {
                if (cur.left == null) { cur.left = new Node(ch); insertionOrder.add(ch); return true; }
                else cur = cur.left;
            } else {
                if (cur.right == null) { cur.right = new Node(ch); insertionOrder.add(ch); return true; }
                else cur = cur.right;
            }
        }
        return false;
    }

    private void deleteChar(char ch) {
        ch = Character.toUpperCase(ch);
        if (!insertionOrder.remove((Character) ch)) return;
        Node old = root;
        root = null;
        java.util.List<Character> copy = new ArrayList<>(insertionOrder);
        insertionOrder.clear();
        for (char c : copy) insertDirect(c);
    }

    // ------------------ Guardar / Cargar ------------------
    private void onSave(boolean exitAfter) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar árbol");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivo de árbol (.dig)", "dig"));
        int res = fc.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        if (!f.getName().toLowerCase().endsWith(".dig")) f = new File(f.getParentFile(), f.getName() + ".dig");
        try {
            saveToFile(f);
            JOptionPane.showMessageDialog(this, "Guardado correctamente en: " + f.getAbsolutePath());
            if (exitAfter) {
                // cerrar de forma segura
                disposeInstance();
                System.exit(0);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void onLoad() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Recuperar árbol");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivo de árbol (.dig)", "dig"));
        int res = fc.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        try {
            loadFromFile(f);
            updateListModel();
            JOptionPane.showMessageDialog(this, "Árbol recuperado correctamente desde: " + f.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage());
        }
    }

    private void onReturn() {
        this.setVisible(false);
        PrincipalPage.getInstance().setVisible(true);
    }

    private void saveToFile(File f) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            for (char c : insertionOrder) {
                w.write(c);
                w.newLine();
            }
        }
    }

    private void loadFromFile(File f) throws IOException {
        java.util.List<Character> letters = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                char ch = line.charAt(0);
                if (Character.isLetter(ch)) letters.add(Character.toUpperCase(ch));
            }
        }
        // Reconstruir el árbol con las letras leídas (se asume que el archivo preserva orden de inserción)
        root = null;
        insertionOrder.clear();
        for (char c : letters) insertDirect(c);
    }

    // ------------------ Animación ------------------
    private static class PathStep {
        Node node;
        boolean isInsertion;
        Node insertionParent;
        boolean insertRight;
        PathStep(Node node) { this.node = node; }
    }

    private java.util.List<PathStep> planInsertionPath(char ch) {
        java.util.List<PathStep> steps = new ArrayList<>();
        ch = Character.toUpperCase(ch);
        if (root == null) {
            PathStep st = new PathStep(null);
            st.isInsertion = true;
            st.insertionParent = null;
            steps.add(st);
            return steps;
        }
        Node cur = root;
        steps.add(new PathStep(cur));
        String bits = letterTo5Bits(ch);
        for (int i = 0; i < bits.length(); ++i) {
            char b = bits.charAt(i);
            if (b == '0') {
                if (cur.left == null) {
                    PathStep st = new PathStep(null);
                    st.isInsertion = true;
                    st.insertionParent = cur;
                    st.insertRight = false;
                    steps.add(st);
                    return steps;
                } else {
                    cur = cur.left;
                    steps.add(new PathStep(cur));
                }
            } else {
                if (cur.right == null) {
                    PathStep st = new PathStep(null);
                    st.isInsertion = true;
                    st.insertionParent = cur;
                    st.insertRight = true;
                    steps.add(st);
                    return steps;
                } else {
                    cur = cur.right;
                    steps.add(new PathStep(cur));
                }
            }
        }
        return steps;
    }

    private void insertTextAnimated(String text) {
        if (text == null || text.isEmpty()) return;
        final java.util.List<Character> letters = new ArrayList<>();
        for (char c : text.toCharArray()) {
            if (!Character.isLetter(c)) continue;
            char up = Character.toUpperCase(c);
            if (!existsInTree(up)) letters.add(up);
        }
        if (letters.isEmpty()) { JOptionPane.showMessageDialog(this, "No hay letras nuevas para insertar (o estaban duplicadas)."); return; }

        new Thread(() -> {
            for (char c : letters) {
                final Object lock = new Object();
                final boolean[] finished = {false};
                SwingUtilities.invokeLater(() -> {
                    java.util.List<PathStep> steps = planInsertionPath(c);
                    animateStepsForLetter(c, steps, () -> {
                        synchronized (lock) { finished[0] = true; lock.notify(); }
                        SwingUtilities.invokeLater(this::updateListModel);
                    });
                });
                synchronized (lock) {
                    while (!finished[0]) {
                        try { lock.wait(); } catch (InterruptedException ignored) {}
                    }
                }
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void animateStepsForLetter(char ch, java.util.List<PathStep> steps, Runnable onFinish) {
        final int delay = 450; // ms por paso
        final int[] idx = {0};
        treePanel.clearHighlights();
        if (animTimer != null && animTimer.isRunning()) animTimer.stop();

        animTimer = new Timer(delay, null);
        animTimer.addActionListener(e -> {
            treePanel.clearHighlights();
            if (idx[0] < steps.size()) {
                PathStep p = steps.get(idx[0]);
                if (p.node != null) treePanel.highlightNode(p.node);
                if (p.isInsertion && p.insertionParent != null) {
                    treePanel.setInsertionHint(p.insertionParent, p.insertRight);
                }
                treePanel.setStatus("Insertando: " + ch + " — paso " + (idx[0]+1) + "/" + steps.size());
                treePanel.repaint();
                idx[0]++;
            } else {
                animTimer.stop();
                treePanel.clearHighlights();
                treePanel.clearInsertionHint();
                boolean inserted = false;
                if (root == null) {
                    root = new Node(ch);
                    inserted = true;
                } else {
                    Node cur = root;
                    String bits = letterTo5Bits(ch);
                    for (int i = 0; i < bits.length(); ++i) {
                        char b = bits.charAt(i);
                        if (b == '0') {
                            if (cur.left == null) { cur.left = new Node(ch); inserted = true; break; }
                            else cur = cur.left;
                        } else {
                            if (cur.right == null) { cur.right = new Node(ch); inserted = true; break; }
                            else cur = cur.right;
                        }
                    }
                }
                if (inserted) insertionOrder.add(ch);
                treePanel.setStatus(inserted ? "Inserción completada: " + ch : "No se pudo insertar: " + ch);
                treePanel.repaint();
                if (onFinish != null) onFinish.run();
            }
        });
        animTimer.setInitialDelay(0);
        animTimer.start();
    }

    // ------------------ GUI: panel que dibuja el árbol ------------------
    class TreePanel extends JPanel {
        private final Set<Node> highlights = new HashSet<>();
        private Node insertionParent = null;
        private boolean insertionRight = false;
        private String status = "";

        TreePanel() {
            setBackground(Color.WHITE);
        }

        void highlightNode(Node n) { if (n != null) highlights.add(n); }
        void clearHighlights() { highlights.clear(); }
        void setInsertionHint(Node parent, boolean right) { insertionParent = parent; insertionRight = right; }
        void clearInsertionHint() { insertionParent = null; }
        void setStatus(String s) { status = s; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (root == null) {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("Árbol vacío. Inserte letras (A-Z).", 20, 20);
                g2.drawString(status, 20, 35);
                return;
            }

            Map<Node, Point> pos = new HashMap<>();
            int margin = 10;
            computePositions(root, margin, getWidth()-margin, margin + 20, 0, pos);

            // dibujar aristas
            g2.setStroke(new BasicStroke(2));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            for (Map.Entry<Node, Point> e : pos.entrySet()) {
                Node n = e.getKey();
                Point p = e.getValue();
                if (n.left != null && pos.containsKey(n.left)) {
                    Point c = pos.get(n.left);
                    g2.setColor(new Color(200,200,200));
                    g2.drawLine(p.x, p.y, c.x, c.y);
                    // dibujar etiqueta "0" en la arista (punto medio)
                    int mx = (p.x + c.x) / 2;
                    int my = (p.y + c.y) / 2;
                    String lbl = "0";
                    FontMetrics fmLbl = g2.getFontMetrics();
                    int lw = fmLbl.stringWidth(lbl);
                    int lh = fmLbl.getAscent();
                    // fondo claro para la etiqueta
                    g2.setColor(new Color(255,255,255,220));
                    g2.fillRect(mx - lw/2 - 4, my - lh + 2 - 4, lw + 8, lh + 6);
                    g2.setColor(new Color(120,120,120));
                    g2.drawString(lbl, mx - lw/2, my + 2);
                }
                if (n.right != null && pos.containsKey(n.right)) {
                    Point c = pos.get(n.right);
                    g2.setColor(new Color(200,200,200));
                    g2.drawLine(p.x, p.y, c.x, c.y);
                    // dibujar etiqueta "1" en la arista (punto medio)
                    int mx = (p.x + c.x) / 2;
                    int my = (p.y + c.y) / 2;
                    String lbl = "1";
                    FontMetrics fmLbl = g2.getFontMetrics();
                    int lw = fmLbl.stringWidth(lbl);
                    int lh = fmLbl.getAscent();
                    g2.setColor(new Color(255,255,255,220));
                    g2.fillRect(mx - lw/2 - 4, my - lh + 2 - 4, lw + 8, lh + 6);
                    g2.setColor(new Color(120,120,120));
                    g2.drawString(lbl, mx - lw/2, my + 2);
                }
            }

            // dibujar nodos (más grandes)
            int nodeR = 32; // aumentado ligeramente
            Color accentBlue = new Color(20, 90, 180);
            for (Map.Entry<Node, Point> e : pos.entrySet()) {
                Node n = e.getKey();
                Point p = e.getValue();
                boolean h = highlights.contains(n);

                if (insertionParent == n) {
                    int hx = insertionRight ? p.x + 120/2 : p.x - 120/2;
                    int hy = p.y + 90;
                    g2.setColor(new Color(220, 240, 255));
                    g2.fillOval(hx - nodeR/2, hy - nodeR/2, nodeR, nodeR);
                    g2.setColor(new Color(160,190,240));
                    g2.drawOval(hx - nodeR/2, hy - nodeR/2, nodeR, nodeR);
                }

                if (h) {
                    g2.setColor(new Color(200, 235, 255));
                    g2.fillOval(p.x - nodeR/2, p.y - nodeR/2, nodeR, nodeR);
                    g2.setColor(accentBlue.darker());
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(p.x - nodeR/2, p.y - nodeR/2, nodeR, nodeR);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(p.x - nodeR/2, p.y - nodeR/2, nodeR, nodeR);
                    g2.setColor(accentBlue);
                    g2.drawOval(p.x - nodeR/2, p.y - nodeR/2, nodeR, nodeR);
                }

                // letra
                g2.setColor(Color.BLACK);
                FontMetrics fm = g2.getFontMetrics(new Font("SansSerif", Font.BOLD, 16));
                String s = String.valueOf(n.value);
                int sw = fm.stringWidth(s);
                int sh = fm.getAscent();
                g2.setFont(new Font("SansSerif", Font.BOLD, 16));
                g2.drawString(s, p.x - sw/2, p.y + sh/2 - 2);
            }

            // estado
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(status, 10, getHeight()-10);
        }

        private void computePositions(Node node, int x1, int x2, int y, int depth, Map<Node, Point> pos) {
            if (node == null) return;
            int x = (x1 + x2) / 2;
            pos.put(node, new Point(x, y));
            int levelGap = 100;
            if (node.left != null) computePositions(node.left, x1, x, y + levelGap, depth + 1, pos);
            if (node.right != null) computePositions(node.right, x, x2, y + levelGap, depth + 1, pos);
        }

    }

    // ------------------ Acciones de botones ------------------
    private void updateListModel() {
        listModel.clear();
        for (char c : insertionOrder) listModel.addElement(String.valueOf(c));
        treePanel.repaint();
    }

    private void deleteSelected() {
        String sel = letterList.getSelectedValue();
        if (sel == null) { JOptionPane.showMessageDialog(this, "Seleccione una letra de la lista para eliminar."); return; }
        char ch = sel.charAt(0);
        int confirm = JOptionPane.showConfirmDialog(this, "Eliminar letra " + ch + " y reconstruir el árbol?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        deleteChar(ch);
        updateListModel();
    }

    private void resetAll() {
        int c = JOptionPane.showConfirmDialog(this, "Reiniciar y vaciar todo?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        root = null;
        insertionOrder.clear();
        updateListModel();
    }

    // ------------------ Singleton access ------------------
    // Reemplazamos el método main por un patrón Singleton para poder obtener
    // la instancia desde otras clases.
    private static volatile DigitalTreeSearch instance = null;

    /**
     * Devuelve la instancia única del frame. Si aún no existe, la crea
     * garantizando que la construcción ocurra en el Event Dispatch Thread
     * (EDT), que es obligatorio para componentes Swing.
     */
    public static DigitalTreeSearch getInstance() {
        if (instance == null) {
            synchronized (DigitalTreeSearch.class) {
                if (instance == null) {
                    if (SwingUtilities.isEventDispatchThread()) {
                        instance = new DigitalTreeSearch();
                    } else {
                        try {
                            SwingUtilities.invokeAndWait(() -> instance = new DigitalTreeSearch());
                        } catch (Exception e) {
                            throw new RuntimeException("No se pudo crear la instancia del frame", e);
                        }
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Muestra la ventana de forma segura en el EDT.
     */
    public void showWindow() {
        SwingUtilities.invokeLater(() -> {
            if (!this.isVisible()) this.setVisible(true);
            this.toFront();
        });
    }

    /**
     * Cierra y limpia la instancia del singleton (para permitir recrearla si se desea).
     */
    public static void disposeInstance() {
        if (instance == null) return;
        SwingUtilities.invokeLater(() -> {
            try {
                instance.dispose();
            } finally {
                instance = null;
            }
        });
    }

}
