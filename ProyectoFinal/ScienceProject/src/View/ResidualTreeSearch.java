package View;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

/**
 * BinaryPrefixTreeVisualizer
 *
 * Última modificación: guardar/recuperar cambia para usar extensión .res y el JFileChooser filtra
 * únicamente archivos con esa extensión.
 */
public class ResidualTreeSearch extends JFrame {
    private static ResidualTreeSearch instance = null;
    private static final int NODE_RADIUS = 22;
    private static final int LINK_W = 70;
    private static final int LINK_H = 36;
    private static final int H_SPACING = 100;
    private static final int V_SPACING = 110;

    // Margen general lateral
    private static final int MARGIN = 40;

    // Cabecera / offset para dibujar el árbol (evita solapamiento con título)
    private static final int HEADER_HEIGHT = 92;      // altura de la barra superior decorativa
    private static final int HEADER_TOP_GAP = 8;     // espacio superior antes de la cabecera
    private static final int EXTRA_TOP_SPACE = 40;   // espacio extra para separar el árbol del marco
    private static final int DRAW_OFFSET_Y = HEADER_TOP_GAP + HEADER_HEIGHT + EXTRA_TOP_SPACE; // donde comienza el área del árbol

    private static final Color ACCENT = new Color(79, 156, 255);
    private static final Color HEADER_BLUE = new Color(83, 143, 255);
    private static final Color HIGHLIGHT = new Color(255, 140, 0);

    private final Node root;
    private final TreePanel treePanel;

    private final JTextField inputField = new JTextField(20);
    private final JButton insertBtn = new JButton("Insertar");
    private final JButton deleteBtn = new JButton("Eliminar");
    private final JButton clearBtn = new JButton("Limpiar");
    private final JButton saveBtn = new JButton("Guardar");
    private final JButton saveExitBtn = new JButton("Guardar y Salir");
    private final JButton recoverBtn = new JButton("Recuperar");
    private final JButton volverBtn = new JButton("Volver");
    private final JLabel statusLabel = new JLabel("Ready");

    public static ResidualTreeSearch getInstance() {
        if (instance == null) {
            synchronized (DigitalTreeSearch.class) {
                if (instance == null) {
                    if (SwingUtilities.isEventDispatchThread()) {
                        instance = new ResidualTreeSearch();
                    } else {
                        try {
                            SwingUtilities.invokeAndWait(() -> instance = new ResidualTreeSearch());
                        } catch (Exception e) {
                            throw new RuntimeException("No se pudo crear la instancia del frame", e);
                        }
                    }
                }
            }
        }
        return instance;
    }

    private ResidualTreeSearch() {
        super("Arboles por Residuos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        root = new Node(); root.isLink = true;
        treePanel = new TreePanel();
        JScrollPane scroll = new JScrollPane(treePanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel control = new JPanel();
        control.add(new JLabel("Clave"));
        control.add(inputField);
        control.add(insertBtn);
        control.add(deleteBtn);
        control.add(clearBtn);
        control.add(saveBtn);
        control.add(saveExitBtn);
        control.add(recoverBtn);
        control.add(volverBtn);

        add(scroll, BorderLayout.CENTER);
        add(control, BorderLayout.NORTH);
        add(statusLabel, BorderLayout.SOUTH);

        insertBtn.addActionListener(e -> doInsert(false));
        deleteBtn.addActionListener(e -> doDeleteUsingInput());
        clearBtn.addActionListener(e -> doClear());
        saveBtn.addActionListener(e -> doSave(false));
        saveExitBtn.addActionListener(e -> doSave(true));
        recoverBtn.addActionListener(e -> doRecover());
        volverBtn.addActionListener(e -> doReturn());

        // Estilizar botones oscuros (alto contraste)
        Font btnFont = new Font("SansSerif", Font.BOLD, 13);
        JButton[] allBtns = {insertBtn, deleteBtn, clearBtn, saveBtn, saveExitBtn, recoverBtn, volverBtn};
        for (JButton b : allBtns) {
            b.setFont(btnFont);
            b.setBackground(new Color(40, 40, 40));
            b.setForeground(Color.WHITE);
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setPreferredSize(new Dimension(140, 32));
        }
    }

    // ------------------------ Clear ------------------------
    private void doClear() {
        root.left = null; root.right = null; root.isLink = true; root.letter = 0;
        treePanel.clearHighlights(); treePanel.recomputeAndRepaint();
        statusLabel.setText("Arbol limpiado");
    }

    // ------------------------ Insert / Animation ------------------------
    private void doInsert(boolean animated) {
        String text = inputField.getText();
        if (text == null || text.isEmpty()) { statusLabel.setText("Nada para insertar"); return; }
        ArrayList<Character> letters = new ArrayList<>();
        for (char c : text.toUpperCase().toCharArray()) if (c >= 'A' && c <= 'Z') letters.add(c);
        if (letters.isEmpty()) { statusLabel.setText("No claves validas (A-Z)"); return; }

        if (!animated) {
            for (char c : letters) insertLetterImmediate(c);
            treePanel.recomputeAndRepaint();
            statusLabel.setText(letters.size() + " Claves insertadas");
            return;
        }

        setControlsEnabled(false);
        Timer seq = new Timer(700, null);
        final int[] idx = {0};
        seq.addActionListener(new ActionListener() {
            Timer stepTimer = null;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idx[0] >= letters.size()) {
                    seq.stop(); if (stepTimer != null) stepTimer.stop();
                    treePanel.clearHighlights(); treePanel.recomputeAndRepaint();
                    setControlsEnabled(true);
                    statusLabel.setText("Insercion animada finalizada");
                    return;
                }
                char c = letters.get(idx[0]++);
                statusLabel.setText("Insertando: " + c);
                treePanel.recomputeAndRepaint();
                List<PathStep> path = treePanel.buildPathForAnimation(root, c);
                if (stepTimer != null) stepTimer.stop();
                final int[] step = {0};
                stepTimer = new Timer(300, ev -> {
                    if (step[0] < path.size()) {
                        PathStep ps = path.get(step[0]);
                        if (ps.target != null) treePanel.setHighlightNode(ps.target);
                        else treePanel.setHighlightStep(ps);
                        treePanel.recomputeAndRepaint();
                        step[0]++;
                    } else {
                        insertLetterImmediate(c);
                        treePanel.clearHighlights(); treePanel.recomputeAndRepaint();
                        stepTimer.stop();
                    }
                });
                stepTimer.setInitialDelay(0); stepTimer.start();
            }
        });
        seq.setInitialDelay(0); seq.start();
    }

    private void setControlsEnabled(boolean en) {
        insertBtn.setEnabled(en); deleteBtn.setEnabled(en); clearBtn.setEnabled(en); saveBtn.setEnabled(en); saveExitBtn.setEnabled(en); recoverBtn.setEnabled(en); inputField.setEnabled(en);
    }

    private void insertLetterImmediate(char letter) {
        int[] bits = codeBits(letter);
        insertAt(root, letter, bits, 0);
    }

    private void insertAt(Node current, char letter, int[] bits, int pos) {
        if (pos >= bits.length) return;
        int bit = bits[pos];
        Node child = (bit == 0) ? current.left : current.right;
        if (child == null) {
            Node info = new Node(); info.isLink = false; info.letter = letter;
            if (bit == 0) current.left = info; else current.right = info;
            return;
        }
        if (!child.isLink) {
            if (child.letter == letter) return; // duplicate
            char existing = child.letter;
            child.isLink = true; child.letter = 0; child.left = null; child.right = null;
            insertAt(child, existing, codeBits(existing), pos + 1);
            insertAt(child, letter, bits, pos + 1);
            return;
        }
        insertAt(child, letter, bits, pos + 1);
    }

    // ------------------------ Delete / Compress ------------------------
    private void doDeleteUsingInput() {
        String s = inputField.getText(); if (s == null || s.isEmpty()) { statusLabel.setText("Escribe la clave a eliminar en el mismo campo"); return; }
        char c = Character.toUpperCase(s.charAt(0)); if (c < 'A' || c > 'Z') { statusLabel.setText("Clave Invalida"); return; }
        ParentRef found = findLetter(root, null, c); if (found == null) { statusLabel.setText("Clave no encontrada"); return; }
        if (found.parent == null) { statusLabel.setText("Cannot delete at root"); return; }
        if (found.isLeft) found.parent.left = null; else found.parent.right = null;
        Node compressed = compressNode(root);
        if (compressed == null) { root.left = null; root.right = null; root.isLink = true; }
        else if (!compressed.isLink) {
            char sole = compressed.letter; int first = codeBits(sole)[0]; root.left = root.right = null; Node info = new Node(); info.isLink = false; info.letter = sole; if (first == 0) root.left = info; else root.right = info; root.isLink = true;
        } else { root.left = compressed.left; root.right = compressed.right; root.isLink = true; }
        treePanel.recomputeAndRepaint(); statusLabel.setText("Eliminada " + c);
    }

    private Node compressNode(Node node) {
        if (node == null) return null;
        int count = countInfoNodes(node);
        if (count == 0) return null;
        if (count == 1) { char sole = findSoleLetter(node); Node info = new Node(); info.isLink = false; info.letter = sole; return info; }
        Node res = new Node(); res.isLink = true; res.left = compressNode(node.left); res.right = compressNode(node.right); return res;
    }

    private int countInfoNodes(Node node) { if (node == null) return 0; if (!node.isLink) return 1; return countInfoNodes(node.left) + countInfoNodes(node.right); }
    private char findSoleLetter(Node node) { if (node == null) return 0; if (!node.isLink) return node.letter; char l = findSoleLetter(node.left); if (l != 0) return l; return findSoleLetter(node.right); }

    private ParentRef findLetter(Node node, Node parent, char letter) {
        if (node == null) return null;
        if (!node.isLink && node.letter == letter) return new ParentRef(parent, node, parent != null && parent.left == node);
        ParentRef l = findLetter(node.left, node, letter); if (l != null) return l; return findLetter(node.right, node, letter);
    }

    // ------------------------ Save / Recover (con .res filter) ------------------------
    private void doSave(boolean exitAfter) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar árbol (archivo .res)");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Res files (*.res)", "res");
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);

        int r = fc.showSaveDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        if (!f.getName().toLowerCase().endsWith(".res")) {
            f = new File(f.getParentFile(), f.getName() + ".res");
        }
        if (f.exists()) {
            int ans = JOptionPane.showConfirmDialog(this, "El archivo ya existe. ¿Sobrescribir?", "Confirmar sobrescritura", JOptionPane.YES_NO_OPTION);
            if (ans != JOptionPane.YES_OPTION) return;
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
            List<Character> letters = collectLetters(root);
            for (char c : letters) { bw.write(c); bw.newLine(); }
            statusLabel.setText("Guardado " + letters.size() + " claves en " + f.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            return;
        }
        if (exitAfter) System.exit(0);
    }

    private void doRecover() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Recuperar árbol (archivo .res)");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Res files (*.res)", "res");
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);

        int r = fc.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        if (f == null) return;
        // optionally ensure extension
        if (!f.getName().toLowerCase().endsWith(".res")) {
            JOptionPane.showMessageDialog(this, "Seleccione un archivo con extensión .res");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            List<Character> letters = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toUpperCase(); if (line.isEmpty()) continue;
                if (line.length() == 1 && line.charAt(0) >= 'A' && line.charAt(0) <= 'Z') { letters.add(line.charAt(0)); }
                else { for (char c : line.toCharArray()) if (c >= 'A' && c <= 'Z') letters.add(c); }
            }
            root.left = null; root.right = null; root.isLink = true; root.letter = 0;
            for (char c : letters) insertLetterImmediate(c);
            treePanel.recomputeAndRepaint();
            statusLabel.setText("Recuperado " + letters.size() + " letras desde " + f.getName());
        } catch (IOException ex) { JOptionPane.showMessageDialog(this, "Error al recuperar: " + ex.getMessage()); }
    }

    private List<Character> collectLetters(Node node) { List<Character> list = new ArrayList<>(); collectLettersRec(node, list); return list; }
    private void collectLettersRec(Node node, List<Character> list) { if (node == null) return; if (!node.isLink) { list.add(node.letter); return; } collectLettersRec(node.left, list); collectLettersRec(node.right, list); }

    private int[] codeBits(char letter) { int pos = (letter - 'A') + 1; int[] bits = new int[5]; for (int i = 4; i >= 0; i--) { bits[i] = pos & 1; pos >>= 1; } return bits; }

    // ------------------------ Return to previous window ------------------------
    private void doReturn() {
        this.dispose();
        PrincipalPage.getInstance().setVisible(true);
    }

    // ------------------------ Visualization ------------------------
    class TreePanel extends JPanel {
        private final Map<Node, Rectangle> nodeBounds = new HashMap<>();
        private Node highlightNode = null;
        private PathStep highlightStep = null;
        private int contentWidth = 1000, contentHeight = 600;

        public TreePanel() { setBackground(Color.WHITE); setPreferredSize(new Dimension(contentWidth, contentHeight)); }

        public void setHighlightNode(Node n) { highlightNode = n; highlightStep = null; }
        public void setHighlightStep(PathStep ps) { highlightStep = ps; highlightNode = null; }
        public void clearHighlights() { highlightNode = null; highlightStep = null; }

        public List<PathStep> buildPathForAnimation(Node root, char letter) {
            List<PathStep> path = new ArrayList<>(); if (root == null) return path; int[] bits = codeBits(letter);
            Node cur = root; path.add(new PathStep(null, -1, cur));
            for (int pos = 0; pos < bits.length; pos++) {
                int b = bits[pos]; Node child = (b == 0) ? cur.left : cur.right; path.add(new PathStep(cur, b, child)); if (child == null) break; if (!child.isLink) break; cur = child; }
            return path;
        }

        public void recomputeAndRepaint() { computePositions(); revalidate(); repaint(); }

        private void computePositions() {
            nodeBounds.clear(); NextX nx = new NextX(); assignPositions(root, 0, nx);
            int maxX = 0, maxY = 0; for (Node n : nodeBounds.keySet()) { Rectangle r = nodeBounds.get(n); maxX = Math.max(maxX, r.x + r.width + MARGIN); maxY = Math.max(maxY, r.y + r.height + MARGIN); }
            contentWidth = Math.max(maxX + 100, 600); contentHeight = Math.max(maxY + 60, 400); setPreferredSize(new Dimension(contentWidth, contentHeight)); }

        private void assignPositions(Node node, int depth, NextX nx) {
            if (node == null) return; int y = DRAW_OFFSET_Y + depth * V_SPACING; // <-- usar DRAW_OFFSET_Y para evitar cabecera
            if (!node.isLink) { int x = MARGIN + nx.next * H_SPACING; nx.next++; node.x = x; node.y = y; nodeBounds.put(node, new Rectangle(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2)); return; }
            if (node.left != null) assignPositions(node.left, depth + 1, nx);
            if (node.right != null) assignPositions(node.right, depth + 1, nx);
            int x; if (node.left != null && node.right != null) x = (node.left.x + node.right.x) / 2; else if (node.left != null) x = node.left.x; else if (node.right != null) x = node.right.x; else { x = MARGIN + nx.next * H_SPACING; nx.next++; }
            node.x = x; node.y = y; nodeBounds.put(node, new Rectangle(x - LINK_W/2, y - LINK_H/2, LINK_W, LINK_H)); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw decorative header using HEADER constants
            int w = getWidth();
            g2.setColor(HEADER_BLUE);
            g2.fillRect(0, HEADER_TOP_GAP, w, HEADER_HEIGHT);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
            FontMetrics fm = g2.getFontMetrics();
            String title = "Arbol por Residuos";
            int tx = 20;
            int ty = HEADER_TOP_GAP + (HEADER_HEIGHT + fm.getAscent()) / 2 - 6;
            g2.drawString(title, tx, ty);

            // blue frame decoration located below the header and offset further down
            int frameX = 8;
            int frameY = DRAW_OFFSET_Y - 24; // more separation so nodes don't touch the inner border
            int frameW = Math.max(200, getWidth() - 32);
            int frameH = Math.max(200, getHeight() - frameY - 24);
            g2.setColor(ACCENT);
            Stroke oldS = g2.getStroke();
            g2.setStroke(new BasicStroke(10f));
            g2.drawRoundRect(frameX, frameY, frameW, frameH, 12, 12);
            g2.setStroke(oldS);

            // draw edges and nodes
            g2.setColor(Color.GRAY); g2.setStroke(new BasicStroke(2f)); drawEdges(g2, root);
            drawNodes(g2, root);

            // hypothetical marker for a highlight step
            if (highlightStep != null && highlightStep.target == null && highlightStep.parent != null) {
                Rectangle pr = nodeBounds.get(highlightStep.parent);
                if (pr != null) {
                    int px = pr.x + pr.width/2; int py = pr.y + pr.height;
                    int childX = px + (highlightStep.bit == 0 ? -H_SPACING/2 : H_SPACING/2);
                    int childY = py + V_SPACING - (MARGIN/2);
                    Stroke old = g2.getStroke();
                    float[] dash = {6f, 6f};
                    g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
                    g2.setColor(HIGHLIGHT);
                    g2.drawOval(childX - NODE_RADIUS, childY - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
                    g2.setStroke(old);
                }
            }

            g2.dispose();
        }

        private void drawEdges(Graphics2D g2, Node node) {
            if (node == null) return;
            Rectangle r = nodeBounds.get(node);
            if (node.left != null) {
                Rectangle rc = nodeBounds.get(node.left);
                if (r != null && rc != null) {
                    int x1 = r.x + r.width/2, y1 = r.y + r.height;
                    int x2 = rc.x + rc.width/2, y2 = rc.y;
                    g2.drawLine(x1, y1, x2, y2); drawBitLabel(g2, x1, y1, x2, y2, "0");
                }
                drawEdges(g2, node.left);
            }
            if (node.right != null) {
                Rectangle rc = nodeBounds.get(node.right);
                if (r != null && rc != null) {
                    int x1 = r.x + r.width/2, y1 = r.y + r.height;
                    int x2 = rc.x + rc.width/2, y2 = rc.y;
                    g2.drawLine(x1, y1, x2, y2); drawBitLabel(g2, x1, y1, x2, y2, "1");
                }
                drawEdges(g2, node.right);
            }
        }

        private void drawBitLabel(Graphics2D g2, int x1, int y1, int x2, int y2, String bit) {
            int mx = (x1 + x2) / 2 - 6; int my = (y1 + y2) / 2 - 6;
            g2.setColor(ACCENT); g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12)); g2.drawString(bit, mx, my); g2.setColor(Color.GRAY);
        }

        private void drawNodes(Graphics2D g2, Node node) {
            if (node == null) return;
            Rectangle r = nodeBounds.get(node);
            if (r == null) return;
            boolean isHighlight = (node == highlightNode);
            if (!node.isLink) {
                g2.setColor(Color.WHITE); g2.fillOval(r.x, r.y, r.width, r.height);
                g2.setColor(isHighlight ? HIGHLIGHT : ACCENT); g2.setStroke(new BasicStroke(2f)); g2.drawOval(r.x, r.y, r.width, r.height);
                g2.setColor(Color.BLACK); g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14)); String s = String.valueOf(node.letter);
                FontMetrics fm = g2.getFontMetrics(); int sw = fm.stringWidth(s); int sh = fm.getAscent(); g2.drawString(s, r.x + (r.width - sw) / 2, r.y + (r.height + sh) / 2 - 4);
            } else {
                g2.setColor(Color.WHITE); g2.fillRect(r.x, r.y, r.width, r.height);
                g2.setColor(isHighlight ? HIGHLIGHT : ACCENT); g2.setStroke(new BasicStroke(2f)); g2.drawRect(r.x, r.y, r.width, r.height);
            }
            drawNodes(g2, node.left); drawNodes(g2, node.right);
        }
    }

    // ------------------------ Helpers & Types ------------------------
    static class Node { boolean isLink = true; char letter = 0; Node left = null, right = null; int x, y; }
    static class ParentRef { Node parent; Node node; boolean isLeft; ParentRef(Node p, Node n, boolean l) { parent = p; node = n; isLeft = l; } }
    static class NextX { int next = 0; }
    static class PathStep { Node parent; int bit; Node target; PathStep(Node p, int b, Node t) { parent = p; bit = b; target = t; } }

}
