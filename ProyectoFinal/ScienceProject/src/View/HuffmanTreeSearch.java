package View;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * HuffmanGUI - Versión final con frecuencias dibujadas en las aristas (líneas),
 * estética blanco/azul, botones alineados, separación dinámica y soporte .huf.
 *
 * Cambio solicitado: las frecuencias ya NO aparecen dentro de los nodos.
 * Se muestran en la arista que une cada nodo con su padre (k/total).
 */
public class HuffmanTreeSearch extends JFrame {
    private JTextField inputField;
    private JButton insertBtn, deleteBtn, clearBtn;
    private JButton saveBtn, saveExitBtn, loadBtn, volverBtn;
    private TreePanel treePanel;

    // Conteos de caracteres (mantiene orden de primera aparición)
    private final Map<Character, Integer> counts = new LinkedHashMap<>();
    private int seqCounter = 0;

    private static HuffmanTreeSearch instance;

    public static HuffmanTreeSearch getInstance() {
        if (instance == null) {
            instance = new HuffmanTreeSearch();
        }
        return instance;
    }

    public HuffmanTreeSearch() {
        super("Árbol de Huffman - Visualizador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLayout(new BorderLayout());

        // Top: label + textfield + botones en la MISMA linea y con misma altura
        JPanel top = new JPanel();
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

        JLabel lbl = new JLabel("Clave: ");
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 13f));
        lbl.setForeground(new Color(10, 50, 120));
        top.add(lbl);
        top.add(Box.createRigidArea(new Dimension(6, 0)));

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        inputField.setPreferredSize(new Dimension(500, 30));
        top.add(inputField);
        top.add(Box.createRigidArea(new Dimension(10, 0)));

        // Botones con la misma altura que el textfield
        Dimension btnSize = new Dimension(110, 30);
        insertBtn = new JButton("Insertar");
        insertBtn.setPreferredSize(btnSize);
        insertBtn.setMaximumSize(btnSize);

        deleteBtn = new JButton("Eliminar");
        deleteBtn.setPreferredSize(btnSize);
        deleteBtn.setMaximumSize(btnSize);

        clearBtn = new JButton("Limpiar");
        clearBtn.setPreferredSize(btnSize);
        clearBtn.setMaximumSize(btnSize);

        saveBtn = new JButton("Guardar");
        saveBtn.setPreferredSize(btnSize);
        saveBtn.setMaximumSize(btnSize);

        saveExitBtn = new JButton("Guardar y Salir");
        saveExitBtn.setPreferredSize(btnSize);
        saveExitBtn.setMaximumSize(btnSize);

        loadBtn = new JButton("Recuperar");
        loadBtn.setPreferredSize(btnSize);
        loadBtn.setMaximumSize(btnSize);

        volverBtn = new JButton("Volver");
        volverBtn.setPreferredSize(btnSize);
        volverBtn.setMaximumSize(btnSize);

        // Estilo simple azul en los botones
        Color azul = new Color(30, 120, 220);
        for (JButton b : Arrays.asList(insertBtn, deleteBtn, clearBtn, saveBtn, saveExitBtn, loadBtn, volverBtn)) {
            b.setBackground(azul);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
        }

        top.add(insertBtn);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(deleteBtn);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(clearBtn);
        top.add(Box.createRigidArea(new Dimension(12,0)));
        top.add(saveBtn);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(saveExitBtn);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(loadBtn);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(volverBtn);

        add(top, BorderLayout.NORTH);

        // Panel del árbol con fondo blanco y borde azul
        treePanel = new TreePanel();
        treePanel.setBackground(Color.WHITE);
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(new Color(230, 244, 255)); // marco azul claro
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        centerWrapper.add(treePanel, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        // Listeners
        insertBtn.addActionListener(_ -> {
            String text = inputField.getText();
            if (text == null || text.isEmpty()) return;
            addTextCounts(text);
            rebuildAndDraw();
            inputField.setText("");
        });

        deleteBtn.addActionListener(_ -> {
            String text = inputField.getText();
            if (text == null || text.isEmpty()) return;
            removeCharacters(text);
            rebuildAndDraw();
            inputField.setText("");
        });

        clearBtn.addActionListener(_ -> {
            counts.clear();
            seqCounter = 0;
            treePanel.setRoot(null, 0);
            treePanel.repaint();
        });

        saveBtn.addActionListener(_ -> {
            doSave(false);
        });

        saveExitBtn.addActionListener(_ -> {
            doSave(true);
        });

        loadBtn.addActionListener(_ -> {
            doLoad();
        });

        volverBtn.addActionListener(_ -> {
            this.setVisible(false);;
            PrincipalPage.getInstance().setVisible(true);
        });

        setSize(1200, 720);
        setLocationRelativeTo(null);
    }

    private void addTextCounts(String text) {
        for (char c : text.toCharArray()) {
            counts.putIfAbsent(c, 0);
            counts.put(c, counts.get(c) + 1);
        }
    }

    private void removeCharacters(String text) {
        for (char c : text.toCharArray()) {
            counts.remove(c);
        }
    }

    private void rebuildAndDraw() {
        if (counts.isEmpty()) {
            treePanel.setRoot(null, 0);
            treePanel.repaint();
            return;
        }

        int total = counts.values().stream().mapToInt(Integer::intValue).sum();

        // Crear nodos hoja con seq según orden de primera aparición
        seqCounter = 0;
        PriorityQueue<Node> pq = new PriorityQueue<>(new NodeComparator());
        for (Map.Entry<Character,Integer> e : counts.entrySet()) {
            Node n = new Node(e.getKey(), e.getValue(), seqCounter++);
            pq.add(n);
        }

        // Si solo hay uno, creamos un padre para que se vea bien el dibujo
        if (pq.size() == 1) {
            Node only = pq.poll();
            Node root = new Node(null, only.count, seqCounter++);
            root.left = only;
            pq.add(root);
        }

        while (pq.size() > 1) {
            Node a = pq.poll();
            Node b = pq.poll();
            Node parent = new Node(null, a.count + b.count, seqCounter++);
            parent.left = a;
            parent.right = b;
            pq.add(parent);
        }

        Node root = pq.peek();
        treePanel.setRoot(root, total);
        treePanel.repaint();
    }

    // ---------- Guardar / Cargar (.huf) ----------
    private void doSave(boolean exitAfterSave) {
        if (counts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay datos para guardar.", "Atención", JOptionPane.INFORMATION_MESSAGE);
            if (exitAfterSave) System.exit(0);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar archivo .huf");
        chooser.setFileFilter(new FileNameExtensionFilter("Huffman files (*.huf)", "huf"));
        int userSel = chooser.showSaveDialog(this);
        if (userSel == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            // Asegurar extensión .huf
            if (!f.getName().toLowerCase().endsWith(".huf")) {
                f = new File(f.getParentFile(), f.getName() + ".huf");
            }
            try {
                saveToFile(f);
                JOptionPane.showMessageDialog(this, "Guardado correctamente en:\n" + f.getAbsolutePath(), "Guardado", JOptionPane.INFORMATION_MESSAGE);
                if (exitAfterSave) System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (exitAfterSave) {
                // el usuario canceló; preguntamos si desea salir de todas formas
                int r = JOptionPane.showConfirmDialog(this, "No se guardó. ¿Deseas salir de todas formas?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) System.exit(0);
            }
        }
    }

    private void doLoad() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Recuperar archivo .huf");
        chooser.setFileFilter(new FileNameExtensionFilter("Huffman files (*.huf)", "huf"));
        int userSel = chooser.showOpenDialog(this);
        if (userSel == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                loadFromFile(f);
                rebuildAndDraw();
                JOptionPane.showMessageDialog(this, "Archivo cargado correctamente:\n" + f.getAbsolutePath(), "Recuperado", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Guarda el mapa 'counts' en formato texto plano, una línea por entrada:
     *   codepoint,count
     * (ej: 77,1)
     */
    private void saveToFile(File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            bw.write("# Huffman .huf v1");
            bw.newLine();
            // escribimos en orden de inserción (LinkedHashMap)
            for (Map.Entry<Character, Integer> e : counts.entrySet()) {
                int cp = e.getKey();
                int cnt = e.getValue();
                bw.write(cp + "," + cnt);
                bw.newLine();
            }
            bw.flush();
        }
    }

    /**
     * Carga el archivo y reconstruye counts en el orden de las líneas (ignorando líneas que comiencen con #)
     */
    private void loadFromFile(File file) throws IOException {
        Map<Character, Integer> newCounts = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("#")) continue;
                // formato: codepoint,count
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;
                try {
                    int cp = Integer.parseInt(parts[0]);
                    int cnt = Integer.parseInt(parts[1]);
                    char ch = (char) cp;
                    newCounts.put(ch, cnt);
                } catch (NumberFormatException ex) {
                    // línea inválida, la saltamos
                    System.err.println("Línea inválida en .huf: " + line);
                }
            }
        }
        // reemplazamos counts por newCounts (respetando orden de archivo)
        counts.clear();
        counts.putAll(newCounts);
    }

    // ---------- Estructura del árbol y dibujo (modificado para etiquetas en aristas) ----------
    static class Node {
        Character ch; // null si nodo interno
        int count;    // número de apariciones (entero)
        int seq;      // secuencia para desempate
        Node left, right;
        int x, y;     // posición para dibujar

        Node(Character ch, int count, int seq) {
            this.ch = ch;
            this.count = count;
            this.seq = seq;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }
    }

    static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node a, Node b) {
            if (a.count != b.count) return Integer.compare(a.count, b.count);
            return Integer.compare(a.seq, b.seq);
        }
    }

    static class TreePanel extends JPanel {
        private Node root;
        private int totalCount = 0;
        private final int levelGap = 90;
        public TreePanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(1200, 800));
        }
        public void setRoot(Node r, int total) {
            this.root = r;
            this.totalCount = total;
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color azul = new Color(30, 120, 220);
            Color lineColor = new Color(150, 150, 150);
            g.setStroke(new BasicStroke(2f));

            if (root == null) {
                g.setColor(Color.DARK_GRAY);
                g.drawString("Árbol vacío", 20, 20);
                return;
            }

            int leaves = countLeaves(root);
            int depth = getDepth(root);
            int gap = computeLeafGap(leaves);
            int width = Math.max(getWidth(), leaves * gap + 200);
            int height = Math.max(getHeight(), (depth + 2) * levelGap + 120);
            setPreferredSize(new Dimension(width, height));
            revalidate();

            AtomicInt currentX = new AtomicInt(60);
            assignPositions(root, 0, currentX, gap);

            g.setColor(lineColor);
            g.setStroke(new BasicStroke(2f));
            drawEdges(g, root);

            drawNodes(g, root, azul);

            g.setColor(Color.DARK_GRAY);
            g.drawString("Total = " + totalCount + " (frecuencias como k/" + totalCount + ")", 10, height - 10);
        }

        private int computeLeafGap(int leaves) {
            if (leaves <= 0) return 80;
            int gap = 1000 / leaves;
            gap = Math.max(60, Math.min(140, gap));
            if (leaves <= 4) gap = Math.max(gap, 140);
            return gap;
        }

        // Dibujar aristas y etiquetas de frecuencia en la arista (cercana al hijo)
        private void drawEdges(Graphics2D g, Node node) {
            if (node == null) return;
            if (node.left != null) {
                g.drawLine(node.x, node.y, node.left.x, node.left.y);
                drawFrequencyLabel(g, node, node.left);
                drawEdges(g, node.left);
            }
            if (node.right != null) {
                g.drawLine(node.x, node.y, node.right.x, node.right.y);
                drawFrequencyLabel(g, node, node.right);
                drawEdges(g, node.right);
            }
        }

        // Dibuja la etiqueta de frecuencia (por ejemplo "1/10") cerca de la arista entre parent y child.
        private void drawFrequencyLabel(Graphics2D g, Node parent, Node child) {
            if (totalCount <= 0) return;
            String text;
            if (child.count == totalCount) text = "1";
            else text = child.count + "/" + totalCount;

            // punto medio
            double mx = (parent.x + child.x) / 2.0;
            double my = (parent.y + child.y) / 2.0;

            // desplazamiento perpendicular para que la etiqueta no quede sobre la línea
            double dx = child.x - parent.x;
            double dy = child.y - parent.y;
            double len = Math.sqrt(dx*dx + dy*dy);
            double ux = 0, uy = 0;
            if (len > 0) {
                ux = -dy / len; // unit perpendicular
                uy = dx / len;
            }
            int offset = 12; // píxeles de separación de la línea
            int tx = (int) Math.round(mx + ux * offset);
            int ty = (int) Math.round(my + uy * offset);

            // preparar fuente
            Font f = g.getFont().deriveFont(Font.BOLD, 12f);
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            int tw = fm.stringWidth(text);
            int th = fm.getAscent();

            // dibujar fondo blanco para legibilidad
            g.setColor(Color.WHITE);
            g.fillRect(tx - tw/2 - 4, ty - th + 2, tw + 8, th + 4);

            // dibujar texto
            g.setColor(Color.DARK_GRAY);
            g.drawString(text, tx - tw/2, ty);
        }

        private void drawNodes(Graphics2D g, Node node, Color azul) {
            if (node == null) return;
            int r = 24;
            int w = r * 2;
            int h = r * 2;

            g.setColor(Color.WHITE);
            g.fillOval(node.x - r, node.y - r, w, h);

            g.setColor(azul);
            g.setStroke(new BasicStroke(3f));
            g.drawOval(node.x - r, node.y - r, w, h);

            // Texto: SOLO la letra en hojas. Los internos NO muestran texto (frecuencia en la arista).
            g.setColor(Color.BLACK);
            if (node.isLeaf()) {
                String text = String.valueOf(node.ch);
                Font f = g.getFont().deriveFont(Font.BOLD, 12f);
                g.setFont(f);
                FontMetrics fm = g.getFontMetrics();
                int tw = fm.stringWidth(text);
                int th = fm.getAscent();
                g.drawString(text, node.x - tw/2, node.y + th/2 - 3);
            }

            drawNodes(g, node.left, azul);
            drawNodes(g, node.right, azul);
        }

        private void assignPositions(Node node, int depth, AtomicInt curX, int gap) {
            if (node == null) return;
            node.y = 50 + depth * levelGap;
            if (node.isLeaf()) {
                node.x = curX.getAndAdd(gap);
            } else {
                assignPositions(node.left, depth + 1, curX, gap);
                assignPositions(node.right, depth + 1, curX, gap);
                if (node.left != null && node.right != null) {
                    node.x = (node.left.x + node.right.x) / 2;
                } else if (node.left != null) {
                    node.x = node.left.x + gap/2;
                } else if (node.right != null) {
                    node.x = node.right.x - gap/2;
                }
            }
        }

        private int countLeaves(Node node) {
            if (node == null) return 0;
            if (node.isLeaf()) return 1;
            return countLeaves(node.left) + countLeaves(node.right);
        }

        private int getDepth(Node node) {
            if (node == null) return 0;
            return 1 + Math.max(getDepth(node.left), getDepth(node.right));
        }

        static class AtomicInt {
            int v;
            AtomicInt(int start) { v = start; }
            int getAndAdd(int d) { int old = v; v += d; return old; }
        }
    }
}
