package View;

import javax.swing.*;
import javax.swing.Timer;
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
 ...
*/

public class MultipleTreeSearch extends JFrame {

    private JTextField inputField;
    private JButton insertBtn, deleteBtn, clearBtn;
    private JButton saveBtn, saveExitBtn, loadBtn, volverBtn;

    public MultipleTreeSearch() {
        setTitle("Arbol por Residuos Multiples");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        TreePanel treePanel = new TreePanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(treePanel), BorderLayout.CENTER);

        insertBtn.addActionListener(_ -> {
            String k = inputField.getText().trim().toUpperCase();
            if (k.isEmpty()) return;
            try {
                treePanel.insertString(k);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            inputField.setText("");
        });

        deleteBtn.addActionListener(_ -> {
            String k = inputField.getText().trim().toUpperCase();
            if (k.isEmpty()) return;
            treePanel.deleteString(k);
            inputField.setText("");
        });

        clearBtn.addActionListener(_ -> treePanel.clear());

        saveBtn.addActionListener(_ -> {
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
                    JOptionPane.showMessageDialog(this, "Error guardando: " + ex.getMessage(), "Error", JOptionPane.ERROR
                            );
                }
            }
        });

        saveExitBtn.addActionListener(_ -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar árbol (.mul)");
            fc.setSelectedFile(new File("tree.mul"));
            int res = fc.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".mul")) f = new File(f.getAbsolutePath() + ".mul");
                try {
                    treePanel.saveToFile(f);
                    System.exit(0);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error guardando: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadBtn.addActionListener(_ -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Cargar árbol (.mul)");
            int res = fc.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                try {
                    treePanel.loadFromFile(f);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error cargando: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        volverBtn.addActionListener(_ -> {
            this.dispose();
            PrincipalPage.getInstance().setVisible(true);
        });

        setVisible(true);
    }

    // -------------------- Tree Panel --------------------
    class TreePanel extends JPanel {
        private TrieNode root;
        private Map<TrieNode, Point> positions = new HashMap<>();
        // animation support: target positions, start positions and timer
        private Map<TrieNode, Point> targetPositions = new HashMap<>(); // animated targets
        private Map<TrieNode, Point> startPositions = new HashMap<>();  // animation start
        private long animStartTime = 0;
        private int animDuration = 300; // ms
        private Timer animTimer;
        private Set<TrieNode> highlight = new HashSet<>();


        TreePanel() {
            setPreferredSize(new Dimension(2000, 900));
            setBackground(Color.WHITE);
            
            root = new TrieNode(0);


            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point p = e.getPoint();
                    for (Map.Entry<TrieNode, Point> en : positions.entrySet()) {
                        Point np = en.getValue();
                        int dx = p.x - np.x;
                        int dy = p.y - np.y;
                        if (dx * dx + dy * dy <= 400) {
                            JOptionPane.showMessageDialog(TreePanel.this, en.getKey().toString());
                            break;
                        }
                    }
                }
            });
        }

        // Insert a whole string (A-Z) mapping to 5-bit codes
        public void insertString(String s) {
            for (char c : s.toCharArray()) insertChar(c);
            repaint();
        }

        public void deleteString(String s) {
            for (char c : s.toCharArray()) deleteChar(c);
            repaint();
        }

        public void clear() {
            root = new TrieNode(0);
            positions.clear();
            repaint();
        }

        public void saveToFile(File f) throws IOException {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
                oos.writeObject(root);
            }
        }

        public void loadFromFile(File f) throws IOException {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                root = (TrieNode) ois.readObject();
                repaint();
            } catch (ClassNotFoundException e) {
                throw new IOException(e);
            }
        }

        private void insertChar(char c) {
            int code = charToCode(c);
            TrieNode cur = root;
            // 2 bits, 2 bits, 1 bit
            int b1 = (code >> 3) & 0x3; // top 2 bits
            int b2 = (code >> 1) & 0x3; // next 2 bits
            int b3 = code & 0x1; // last bit
            cur = cur.ensureChild(b1);
            cur = cur.ensureChild(b2);
            cur = cur.ensureChild(b3);
            cur.letter = c;
        }

        private void deleteChar(char c) {
            int code = charToCode(c);
            Stack<TrieNode> stack = new Stack<>();
            TrieNode cur = root;
            stack.push(cur);
            int b1 = (code >> 3) & 0x3;
            int b2 = (code >> 1) & 0x3;
            int b3 = code & 0x1;
            cur = cur.getChild(b1);
            if (cur == null) return;
            stack.push(cur);
            cur = cur.getChild(b2);
            if (cur == null) return;
            stack.push(cur);
            cur = cur.getChild(b3);
            if (cur == null) return;
            cur.letter = '\0';
            // clean up empty nodes
            for (int i = 0; i < 3; i++) {
                TrieNode t = stack.pop();
                TrieNode parent = stack.peek();
                if (t.isEmpty()) {
                    // find index in parent
                    for (int j = 0; j < 4; j++) {
                        if (parent.getChild(j) == t) parent.removeChild(j);
                    }
                } else break;
            }
        }

        private int charToCode(char c) {
            if (c < 'A' || c > 'Z') return 0;
            return (c - 'A' + 1) & 0x1F;
        }

        // Layout algorithm: compute subtree leaf counts and assign x positions to avoid overlaps.
        private void computePositions() {
            // compute target positions into a temporary map (do not overwrite 'positions' directly)
            Map<TrieNode, Point> tempPositions = new HashMap<>();
            if (root == null) return;

            int w = getWidth();

            // vertical layout params
            final int yRoot = 40;
            final int verticalSpacing = 140;

            // first pass: compute number of leaf slots per subtree
            Map<TrieNode, Integer> leafCounts = new HashMap<>();
            computeLeafCounts(root, leafCounts);

            // second pass: assign x positions using an in-order like layout into tempPositions
            int margin = 40;
            int spacing = 80; // base horizontal spacing between leaf slots
            int[] nextX = new int[]{margin}; // mutable integer for current x slot (in pixels)

            assignPositions(root, 0, nextX, spacing, verticalSpacing, yRoot, leafCounts, tempPositions);

            // center the whole tree horizontally in tempPositions
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            for (Point p : tempPositions.values()) {
                if (p.x < minX) minX = p.x;
                if (p.x > maxX) maxX = p.x;
            }
            if (minX == Integer.MAX_VALUE) return; // no nodes

            final int treeMid = (minX + maxX) / 2;
            final int centerX = w / 2;
            int shift = centerX - treeMid;
            if (shift != 0) {
                // shift copy to avoid concurrent-modification on iteration
                Map<TrieNode, Point> shifted = new HashMap<>();
                for (Map.Entry<TrieNode, Point> e : tempPositions.entrySet()) {
                    Point old = e.getValue();
                    shifted.put(e.getKey(), new Point(old.x + shift, old.y));
                }
                tempPositions = shifted;
            }

            // if there is no current layout, just set positions immediately and center viewport
            if (positions.isEmpty()) {
                positions.putAll(tempPositions);

                // center viewport horizontally and give a bit of vertical offset so root is visible
                JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, TreePanel.this);
                if (viewport != null) {
                    Rectangle view = viewport.getViewRect();
                    int vx = Math.max(0, centerX - view.width / 2);
                    int vy = Math.max(0, yRoot - view.height / 8);
                    viewport.setViewPosition(new Point(vx, vy));
                }

                repaint();
                return;
            }

            // prepare animation: capture start positions for nodes in the new layout
            startPositions.clear();
            for (Map.Entry<TrieNode, Point> e : tempPositions.entrySet()) {
                TrieNode n = e.getKey();
                Point tp = e.getValue();
                Point sp = positions.get(n);
                if (sp == null) {
                    // new node appears without movement: start == target
                    sp = new Point(tp.x, tp.y);
                }
                startPositions.put(n, new Point(sp.x, sp.y));
            }

            // set target positions and mark animation start
            targetPositions = tempPositions;
            animStartTime = System.currentTimeMillis();

            // create timer if needed
            if (animTimer == null) {
                final int duration = (int) animDuration; // preserve as effectively final
                final int centerX_forTimer = centerX;
                final int yRoot_forTimer = yRoot;

                animTimer = new Timer(30, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        long now = System.currentTimeMillis();
                        float t = (now - animStartTime) / (float) duration;
                        if (t >= 1f) t = 1f;

                        // interpolate positions between startPositions and targetPositions
                        for (Map.Entry<TrieNode, Point> en : targetPositions.entrySet()) {
                            TrieNode n = en.getKey();
                            Point tp = en.getValue();
                            Point sp = startPositions.get(n);
                            if (sp == null) sp = new Point(tp.x, tp.y);
                            int ix = (int) (sp.x + (tp.x - sp.x) * t);
                            int iy = (int) (sp.y + (tp.y - sp.y) * t);
                            positions.put(n, new Point(ix, iy));
                        }

                        repaint();

                        if (t >= 1f) {
                            // finalize: keep only nodes that exist in targetPositions and set exact target coords
                            positions.keySet().retainAll(targetPositions.keySet());
                            positions.putAll(targetPositions);

                            animTimer.stop();

                            // center viewport after animation completes
                            JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, TreePanel.this);
                            if (viewport != null) {
                                Rectangle view = viewport.getViewRect();
                                int vx = Math.max(0, centerX_forTimer - view.width / 2);
                                int vy = Math.max(0, yRoot_forTimer - view.height / 8);
                                viewport.setViewPosition(new Point(vx, vy));
                            }
                        }
                    }
                });
            }

            animTimer.start();
        }


        // count leaf-slot equivalents: a node with no non-null child counts as 1
        private int computeLeafCounts(TrieNode node, Map<TrieNode, Integer> leafCounts) {
            if (node == null) return 0;
            int cnt = 0;
            boolean hasChild = false;
            for (int i = 0; i < 4; i++) {
                TrieNode c = node.getChild(i);
                if (c != null) {
                    hasChild = true;
                    cnt += computeLeafCounts(c, leafCounts);
                }
            }
            if (!hasChild) cnt = 1;
            leafCounts.put(node, cnt);
            return cnt;
        }

        // assign positions recursively: children first so parent can be centered above them
        private void assignPositions(TrieNode node, int depth, int[] nextX, int spacing, int vSpacing, int yRoot, Map<TrieNode, Integer> leafCounts, Map<TrieNode, Point> outPositions) {
            if (node == null) return;
            int startX = Integer.MAX_VALUE;
            int endX = Integer.MIN_VALUE;
            // process children left-to-right (0..3)
            for (int i = 0; i < 4; i++) {
                TrieNode c = node.getChild(i);
                if (c == null) continue;
                assignPositions(c, depth + 1, nextX, spacing, vSpacing, yRoot, leafCounts, outPositions);
                Point pc = outPositions.get(c);
                if (pc != null) {
                    if (pc.x < startX) startX = pc.x;
                    if (pc.x > endX) endX = pc.x;
                }
            }
            int x;
            if (startX == Integer.MAX_VALUE) {
                // leaf: place at next slot
                x = nextX[0];
                nextX[0] += spacing;
            } else {
                // internal: center above children
                x = (startX + endX) / 2;
            }
            int y = yRoot + depth * vSpacing;
            outPositions.put(node, new Point(x, y));
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

                // label on edge
                String label = edgeLabelFor(node.level, i);
                if (label != null) {
                    Font prev = g.getFont();
                    Font bold = prev.deriveFont(Font.BOLD, 14f);
                    g.setFont(bold);
                    FontMetrics fm = g.getFontMetrics();
                    int lx = (p.x + pc.x) / 2;
                    int ly = (p.y + pc.y) / 2;
                    g.setColor(new Color(10,80,160));
                    g.drawString(label, lx - fm.stringWidth(label) / 2, ly - 6);
                    g.setFont(prev);
                }

                // recurse
                drawEdges(g, child);
            }
        }

        private String edgeLabelFor(int level, int childIndex) {
            if (level == 0) {
                switch (childIndex) {
                    case 0: return "00";
                    case 1: return "01";
                    case 2: return "10";
                    case 3: return "11";
                }
            } else if (level == 1) {
                switch (childIndex) {
                    case 0: return "00";
                    case 1: return "01";
                    case 2: return "10";
                    case 3: return "11";
                }
            } else if (level == 2) {
                return (childIndex == 0) ? "0" : "1";
            }
            return null;
        }

        private void drawNodes(Graphics2D g) {
            // draw nodes in positions map
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
    static class TrieNode implements Serializable {
        int level;
        char letter = '\0';
        TrieNode[] children = new TrieNode[4];

        TrieNode(int level) {
            this.level = level;
            if (level < 2) children = new TrieNode[4];
            else children = new TrieNode[2];
        }

        TrieNode getChild(int idx) {
            if (idx < 0 || idx >= children.length) return null;
            return children[idx];
        }

        TrieNode ensureChild(int idx) {
            if (idx < 0) return null;
            if (idx >= children.length) return null;
            if (children[idx] == null) children[idx] = new TrieNode(level + 1);
            return children[idx];
        }

        int childrenCount() {
            return children.length;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Nivel: ").append(level).append("\n");
            if (letter != '\0') sb.append("Letra: ").append(letter).append("\n");
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) sb.append("Child[").append(i).append("]\n");
            }
            return sb.toString();
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

    private static MultipleTreeSearch instance;

    public static MultipleTreeSearch getInstance() {
        if (instance == null) {
            instance = new MultipleTreeSearch();
        }
        return instance;
    } 

}
