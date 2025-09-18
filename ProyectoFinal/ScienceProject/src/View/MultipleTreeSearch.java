package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


/*
 QuadTrieVisualizer.java

 Visualizador e implementación en Java Swing del árbol descrito por el usuario:
 - Cada letra (A-Z) se codifica en 5 bits según su posición en el alfabeto (A=1..Z=26).
 - Ese código de 5 bits se divide en grupos de 2 bits + 2 bits + 1 bit (por ejemplo: 10 00 0).
 - Nivel 1 (desde la raíz): 4 hijos posibles -> índices 0..3 para "00","01","10","11".
 - Nivel 2: 4 hijos posibles por nodo (mismo esquema).
 - Nivel 3 (final): sólo 2 hijos (0 o 1) para el último bit.

 Funcionalidades:
 - Insertar una palabra (inserta letra por letra; letras repetidas no se duplican).
 - Eliminar una letra.
 - Animación paso a paso del recorrido de inserción (resalta nodos mientras baja).
 - Visualización gráfica tipo árbol con nodos y aristas y letras en las hojas.
 - Botones para controlar inserciones, borrado y reset.

 Compilar y ejecutar:
 javac QuadTrieVisualizer.java
 java QuadTrieVisualizer

*/

public class MultipleTreeSearch extends JFrame {
    private TreePanel treePanel;
    private JTextField inputField;
    private JButton deleteBtn, insertAnimateBtn, resetBtn, saveBtn, saveExitBtn, loadBtn;
    private JSpinner speedSpinner;

    public MultipleTreeSearch() {
        super("Arbol por Residuos Multiples");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        treePanel = new TreePanel();
        add(treePanel, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout(FlowLayout.LEFT));

        inputField = new JTextField(20);
        controls.add(new JLabel("Clave:"));
        controls.add(inputField);

        deleteBtn = new JButton("Eliminar");
        insertAnimateBtn = new JButton("Insertar");
        resetBtn = new JButton("Limpiar");
        saveBtn = new JButton("Guardar");
        saveExitBtn = new JButton("Guardar y Salir");
        loadBtn = new JButton("Cargar");

        controls.add(insertAnimateBtn);
        controls.add(deleteBtn);
        controls.add(resetBtn);
        controls.add(saveBtn);
        controls.add(saveExitBtn);
        controls.add(loadBtn);

        speedSpinner = new JSpinner(new SpinnerNumberModel(200, 50, 2000, 50));

        add(controls, BorderLayout.NORTH);

                Font btnFont = new Font("SansSerif", Font.BOLD, 13);
        JButton[] allBtns = {insertAnimateBtn, deleteBtn, resetBtn, saveBtn, saveExitBtn, loadBtn};
        for (JButton b : allBtns) {
            b.setFont(btnFont);
            b.setBackground(new Color(40, 40, 40));
            b.setForeground(Color.WHITE);
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setPreferredSize(new Dimension(140, 32));
        }


        // Button actions

        deleteBtn.addActionListener(e -> {
            String text = inputField.getText().trim().toUpperCase(Locale.ROOT);
            if (text.isEmpty()) return;
            // delete each char
            for (char c : text.toCharArray()) {
                if (!Character.isLetter(c)) continue;
                treePanel.deleteLetter(c);
            }
            treePanel.repaint();
        });

        insertAnimateBtn.addActionListener(e -> {
            String text = inputField.getText().trim().toUpperCase(Locale.ROOT);
            if (text.isEmpty()) return;
            java.util.List<Character> letters = new ArrayList<>();
            for (char c : text.toCharArray()) if (Character.isLetter(c)) letters.add(c);
            new Thread(() -> {
                for (char c : letters) {
                    try {
                        treePanel.animateInsert(c, (int)((Integer) speedSpinner.getValue()));
                        // small pause between letters
                        Thread.sleep(150);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        });

        resetBtn.addActionListener(e -> {
            treePanel.reset();
        });

        saveBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar árbol (.mul)");
            fc.setSelectedFile(new File("tree.mul"));
            int res = fc.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".mul")) f = new File(f.getAbsolutePath() + ".mul");
                try {
                    treePanel.saveToFile(f);
                    JOptionPane.showMessageDialog(this, "Guardado en: " + f.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error guardando: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveExitBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar árbol (.mul)");
            fc.setSelectedFile(new File("tree.mul"));
            int res = fc.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".mul")) f = new File(f.getAbsolutePath() + ".mul");
                try {
                    treePanel.saveToFile(f);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error guardando: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.exit(0);
            }
        });

        loadBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Cargar árbol (.mul)");
            int res = fc.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                try {
                    treePanel.loadFromFile(f);
                    JOptionPane.showMessageDialog(this, "Árbol cargado desde: " + f.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error cargando: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MultipleTreeSearch v = new MultipleTreeSearch();
            v.setVisible(true);
        });
    }

    // -------------------- Tree and drawing panel --------------------
    static class TreePanel extends JPanel {
        private TrieNode root;
        private Map<TrieNode, Point> positions = new HashMap<>();
        private Set<TrieNode> highlight = new HashSet<>();
        private TrieNode animCurrent = null;

        // ---------------- File save/load ----------------
        // Guardar en archivo .mul (texto plano): primera línea header, luego una letra por línea
        public void saveToFile(File f) throws IOException {
            java.util.List<Character> letters = collectLetters();
            try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                pw.println("QuadTrieMul v1");
                for (char c : letters) pw.println(c);
            }
        }

        // Cargar desde archivo .mul
        public void loadFromFile(File f) throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String header = br.readLine(); // ignorar header (si existe)
                reset();
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    char c = line.charAt(0);
                    if (Character.isLetter(c)) insertLetterImmediate(Character.toUpperCase(c));
                }
            }
        }

        // Recolectar letras existentes (orden: recorrido en profundidad)
        private java.util.List<Character> collectLetters() {
            java.util.List<Character> out = new ArrayList<>();
            collectRec(root, out);
            return out;
        }

        private void collectRec(TrieNode node, java.util.List<Character> out) {
            if (node == null) return;
            if (node.letter != '\0') out.add(node.letter);
            int max = node.childrenCount();
            for (int i = 0; i < max; i++) collectRec(node.getChild(i), out);
        }


        public TreePanel() {
            setBackground(Color.WHITE);
            root = new TrieNode(0);
        }

        public void reset() {
            root = new TrieNode(0);
            positions.clear();
            highlight.clear();
            repaint();
        }

        // Insert letter without animation
        public void insertLetterImmediate(char letter) {
            java.util.List<Integer> path = codeToPath(letter);
            TrieNode current = root;
            for (int i = 0; i < path.size(); i++) {
                int idx = path.get(i);
                current = current.getOrCreateChild(idx, i);
            }
            // final node, set letter if not present
            if (current.letter == '\0') current.letter = letter;
            // recompute positions
            computePositions();
        }

        // Delete letter (and prune empty nodes)
        public void deleteLetter(char letter) {
            java.util.List<Integer> path = codeToPath(letter);
            deleteHelper(root, path, 0);
            computePositions();
        }

        private boolean deleteHelper(TrieNode node, java.util.List<Integer> path, int depth) {
            if (depth == path.size()) {
                // reached leaf candidate
                if (node.letter != '\0') node.letter = '\0';
                // return true if this node has no letter and no children
                return node.isEmpty();
            }
            int idx = path.get(depth);
            TrieNode child = node.getChild(idx);
            if (child == null) return false;
            boolean shouldRemove = deleteHelper(child, path, depth + 1);
            if (shouldRemove) {
                node.removeChild(idx);
            }
            return node.isEmpty();
        }

        // Animate insertion of a single letter: highlights nodes while traversing
        public void animateInsert(char letter, int delayMs) throws InterruptedException {
            java.util.List<Integer> path = codeToPath(letter);
            java.util.List<TrieNode> visited = new ArrayList<>();
            TrieNode current = root;
            visited.add(current);
            for (int i = 0; i < path.size(); i++) {
                int idx = path.get(i);
                TrieNode next = current.getChild(idx);
                if (next == null) next = current.getOrCreateChild(idx, i);
                current = next;
                visited.add(current);
            }
            // animate visited nodes
            for (TrieNode n : visited) {
                highlight.clear();
                highlight.add(n);
                computePositions();
                repaint();
                Thread.sleep(delayMs);
            }
            // set letter at final node
            TrieNode finalNode = visited.get(visited.size() - 1);
            if (finalNode.letter == '\0') finalNode.letter = letter;
            highlight.clear();
            computePositions();
            repaint();
        }

        // Compute integer path from letter: returns list of indices for each level
        // Level 1: 2-bit group -> index 0..3
        // Level 2: 2-bit group -> index 0..3
        // Level 3: single bit -> index 0..1
        private java.util.List<Integer> codeToPath(char letter) {
            int pos = letter - 'A' + 1; // 1..26
            if (pos < 1 || pos > 26) pos = 0;
            String bits = String.format("%5s", Integer.toBinaryString(pos)).replace(' ', '0');
            // split into groups of 2,2,1
            String g1 = bits.substring(0, 2);
            String g2 = bits.substring(2, 4);
            String g3 = bits.substring(4, 5);
            java.util.List<Integer> path = new ArrayList<>();
            path.add(bitsToIndex2(g1));
            path.add(bitsToIndex2(g2));
            path.add(bitsToIndex1(g3));
            return path;
        }

        private int bitsToIndex2(String s) {
            // "00"->0, "01"->1, "10"->2, "11"->3
            if (s.equals("00")) return 0;
            if (s.equals("01")) return 1;
            if (s.equals("10")) return 2;
            return 3;
        }

        private int bitsToIndex1(String s) {
            return s.equals("1") ? 1 : 0;
        }

        // Layout algorithm (simple, fixed tiers and local offsets)
        private void computePositions() {
            positions.clear();
            int w = getWidth();
            int h = getHeight();
            int yRoot = 40;
            // más separación vertical para evitar que los nodos queden juntos
            int y1 = 160;
            int y2 = 360;
            int y3 = 540;

            // Root
            positions.put(root, new Point(w / 2, yRoot));
            // Level1: cuatro posiciones igualmente distribuidas, pero más separadas
            for (int i = 0; i < 4; i++) {
                TrieNode n1 = root.getChild(i);
                int x = (i + 1) * w / 6; // usar 6 para dejar márgenes y más separación
                if (n1 != null) positions.put(n1, new Point(x, y1));
            }
            // Level2: for each existing level1 child, position its up to 4 children
            for (int i = 0; i < 4; i++) {
                TrieNode n1 = root.getChild(i);
                if (n1 == null) continue;
                Point p1 = positions.get(n1);
                if (p1 == null) continue;
                // spread children horizontally around parent (más separación)
                for (int j = 0; j < 4; j++) {
                    TrieNode n2 = n1.getChild(j);
                    if (n2 == null) continue;
                    int baseX = p1.x - 180; // separamos más a la izquierda para cubrir 4 hijos
                    int x = baseX + j * 120; // 4 children más espaciados
                    positions.put(n2, new Point(x, y2));
                }
            }
            // Level3: for each level2 child, up to 2 children (más separación)
            for (int i = 0; i < 4; i++) {
                TrieNode n1 = root.getChild(i);
                if (n1 == null) continue;
                for (int j = 0; j < 4; j++) {
                    TrieNode n2 = n1.getChild(j);
                    if (n2 == null) continue;
                    Point p2 = positions.get(n2);
                    if (p2 == null) continue;
                    for (int k = 0; k < 2; k++) {
                        TrieNode n3 = n2.getChild(k);
                        if (n3 == null) continue;
                        int x = p2.x - 30 + k * 60; // dos hijos separados
                        positions.put(n3, new Point(x, y3));
                    }
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            computePositions();

            // draw edges and nodes recursively
            drawEdges(g, root);
            drawNodes(g);

            // legend
            g.setColor(new Color(10,80,160));
            g.drawString("Nodo resaltado: azul. Letras en azul.", 10, getHeight() - 10);
        }

        private void drawEdges(Graphics2D g, TrieNode node) {
            Point p = positions.get(node);
            if (p == null) return;
            // draw children edges based on node level
            int level = node.level;
            int maxChildren = node.childrenCount();
            for (int i = 0; i < maxChildren; i++) {
                TrieNode child = node.getChild(i);
                if (child == null) continue;
                Point pc = positions.get(child);
                if (pc == null) continue;
                // line
                g.setStroke(new BasicStroke(2f));
                g.setColor(new Color(200,200,200));
                g.drawLine(p.x, p.y + 18, pc.x, pc.y - 18);

                // small label for edge (bits)
                String label = edgeLabelFor(level, i);
                int lx = (p.x + pc.x) / 2;
                int ly = (p.y + pc.y) / 2 - 14; // situar el texto por encima de la línea
                Font prev = g.getFont();
                Font bold = prev.deriveFont(Font.BOLD, 14f);
                g.setFont(bold);
                FontMetrics fm = g.getFontMetrics();
                int sw = fm.stringWidth(label);
                g.setColor(new Color(10,80,160));
                g.drawString(label, lx - sw/2, ly);
                g.setFont(prev);

                // recurse
                drawEdges(g, child);
            }
        }

        private String edgeLabelFor(int level, int childIndex) {
            if (level == 0 || level == 1) {
                switch (childIndex) {
                    case 0: return "00";
                    case 1: return "01";
                    case 2: return "10";
                    default: return "11";
                }
            } else {
                // level 2 -> last single bit
                return childIndex == 0 ? "0" : "1";
            }
        }

        private void drawNodes(Graphics2D g) {
            // draw root
            for (Map.Entry<TrieNode, Point> e : positions.entrySet()) {
                TrieNode n = e.getKey();
                Point p = e.getValue();
                drawOneNode(g, n, p.x, p.y);
            }
        }

        private void drawOneNode(Graphics2D g, TrieNode n, int x, int y) {
            int r = 20;
            boolean isHighlighted = highlight.contains(n);
            // fill
            if (isHighlighted) {
                g.setColor(new Color(200,230,255));
                g.fillOval(x - r, y - r, r * 2, r * 2);
            }
            // circle border
            g.setColor(new Color(10,80,160));
            g.setStroke(new BasicStroke(2f));
            g.drawOval(x - r, y - r, r * 2, r * 2);

            // letter if present
            if (n.letter != '\0') {
                g.setColor(new Color(10,80,160));
                FontMetrics fm = g.getFontMetrics();
                String s = Character.toString(n.letter);
                int sw = fm.stringWidth(s);
                int sh = fm.getAscent();
                g.drawString(s, x - sw / 2, y + sh / 2 - 3);
            }
        }

    }

    // -------------------- Trie Node --------------------
    static class TrieNode {
        int level; // 0=root, 1=level1, 2=level2, 3=level3(final)
        // children: if level < 2 -> size 4; if level ==2 -> size 2
        TrieNode[] children;
        char letter = '\0';

        TrieNode(int level) {
            this.level = level;
            if (level < 2) children = new TrieNode[4];
            else children = new TrieNode[2];
        }

        int childrenCount() { return children.length; }

        TrieNode getChild(int idx) {
            if (idx < 0 || idx >= children.length) return null;
            return children[idx];
        }

        TrieNode getOrCreateChild(int idx, int depth) {
            if (idx < 0) return null;
            if (idx >= children.length) {
                // defensive: if requested an index outside (shouldn't happen), expand safely
                return null;
            }
            if (children[idx] == null) {
                children[idx] = new TrieNode(Math.min(level + 1, 3));
            }
            return children[idx];
        }

        void removeChild(int idx) {
            if (idx < 0 || idx >= children.length) return;
            children[idx] = null;
        }

        boolean isEmpty() {
            if (letter != '\0') return false;
            for (TrieNode c : children) if (c != null) return false;
            return true;
        }
    }
}
