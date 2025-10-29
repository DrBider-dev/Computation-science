package View;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ExternalSearchBlocks.java
 * 
 * Programa en Java Swing que implementa la estructura por bloques descrita
 * - Crear: crea la estructura con n (tamaño total de claves esperadas)
 * - Insertar: inserta claves una a una, manteniendo la estructura ordenada
 * - Buscar: realiza la busqueda externa (comparando el ultimo elemento de cada bloque)
 * - Eliminar: elimina una clave y reordena/blokea
 * - Limpiar: borra todo
 * 
 * Diseño visual: fondo blanco, botones azules, bloques con borde azul.
 *
 * Compilar: javac ExternalSearchBlocks.java
 * Ejecutar: java ExternalSearchBlocks
 */
public class ExternalSearch extends JFrame {
    // datos globales
    private int totalCapacity = 0;       // n
    private int blockSize = 0;           // floor(sqrt(n))
    private int numberBlocks = 0;        // ceil(n / blockSize)
    private boolean lastSaveSuccess = false;

    // lista con todos los datos, siempre ordenada
    private final List<Integer> allData = new ArrayList<>();
    // bloques (cada bloque es lista de Integer)
    private final List<List<Integer>> blocks = new ArrayList<>();

    // componentes Swing
    private final JPanel blocksPanel = new JPanel();
    private final JTextField tfN = new JTextField(6);
    private final JTextField tfValue = new JTextField(8);
    private final JTextArea logArea = new JTextArea(6, 30);

    // para mantener referencias a las etiquetas dentro de los bloques
    private final List<List<JLabel>> blockLabels = new ArrayList<>();

    public ExternalSearch() {
        super("Búsqueda Externa / Estructura por Bloques");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // fondo blanco
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(8, 8));

        // Top: label + textfield + botones en la MISMA linea y con misma altura
        JPanel top = new JPanel();
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

        // Label para n (pequeño)
        JLabel lblN = new JLabel("n:");
        lblN.setFont(lblN.getFont().deriveFont(Font.BOLD, 13f));
        lblN.setForeground(new Color(10, 50, 120));
        top.add(lblN);
        top.add(Box.createRigidArea(new Dimension(6, 0)));

        tfN.setMaximumSize(new Dimension(100, 30));
        tfN.setPreferredSize(new Dimension(100, 30));
        top.add(tfN);
        top.add(Box.createRigidArea(new Dimension(12, 0)));

        // Label y campo principal para claves (input)
        JLabel lbl = new JLabel("Clave:");
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 13f));
        lbl.setForeground(new Color(10, 50, 120));
        top.add(lbl);
        top.add(Box.createRigidArea(new Dimension(6, 0)));

        tfValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tfValue.setPreferredSize(new Dimension(500, 30));
        top.add(tfValue);
        top.add(Box.createRigidArea(new Dimension(10, 0)));

        // Botones con la misma altura que el textfield
        Dimension btnSize = new Dimension(110, 30);

        JButton btnCrear = new JButton("Crear");
        btnCrear.setPreferredSize(btnSize);
        btnCrear.setMaximumSize(btnSize);

        JButton btnInsert = new JButton("Insertar");
        btnInsert.setPreferredSize(btnSize);
        btnInsert.setMaximumSize(btnSize);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(btnSize);
        btnBuscar.setMaximumSize(btnSize);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setPreferredSize(btnSize);
        btnEliminar.setMaximumSize(btnSize);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setPreferredSize(btnSize);
        btnLimpiar.setMaximumSize(btnSize);

        // (Opcionales: botones extra si los quieres)
        JButton saveBtn = new JButton("Guardar");
        saveBtn.setPreferredSize(btnSize); saveBtn.setMaximumSize(btnSize);

        JButton saveExitBtn = new JButton("Guardar y Salir");
        saveExitBtn.setPreferredSize(btnSize); saveExitBtn.setMaximumSize(btnSize);

        JButton loadBtn = new JButton("Recuperar");
        loadBtn.setPreferredSize(btnSize); loadBtn.setMaximumSize(btnSize);

        JButton volverBtn = new JButton("Volver");
        volverBtn.setPreferredSize(btnSize); volverBtn.setMaximumSize(btnSize);

        // Estilo simple azul en los botones
        Color azul = new Color(30, 120, 220);
        for (JButton b : Arrays.asList(btnCrear, btnInsert, btnBuscar, btnEliminar, btnLimpiar,
                                    saveBtn, saveExitBtn, loadBtn, volverBtn)) {
            b.setBackground(azul);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
        }

        // Añadir botones al panel (ajusta el orden si lo deseas)
        top.add(btnCrear);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(btnInsert);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(btnBuscar);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(btnEliminar);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(btnLimpiar);
        top.add(Box.createRigidArea(new Dimension(12,0)));

        // Si quieres mostrar también las acciones de guardado / cargar / volver:
        top.add(saveBtn);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(saveExitBtn);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(loadBtn);
        top.add(Box.createRigidArea(new Dimension(6,0)));
        top.add(volverBtn);

        add(top, BorderLayout.NORTH);


        // Panel central: representacion de bloques
        blocksPanel.setBackground(Color.WHITE);
        JScrollPane centerScroll = new JScrollPane(blocksPanel);
        centerScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(centerScroll, BorderLayout.CENTER);

        // Panel derecho: log
        JPanel right = new JPanel();
        right.setLayout(new BorderLayout(8, 8));
        right.setBackground(Color.WHITE);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(320, 200));
        right.add(new JLabel("Registro / Mensajes:"), BorderLayout.NORTH);
        right.add(logScroll, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);

        // acciones
        btnCrear.addActionListener(_ -> onCrear());
        btnInsert.addActionListener(_ -> onInsert());
        btnBuscar.addActionListener(_ -> onBuscar());
        btnEliminar.addActionListener(_ -> onEliminar());
        btnLimpiar.addActionListener(_ -> onLimpiar());
        saveBtn.addActionListener(_ -> onSave());
        saveExitBtn.addActionListener(_ -> onSaveExit());
        loadBtn.addActionListener(_ -> onLoad());
        volverBtn.addActionListener(_ -> onVolver());

        appendLog("Bienvenido — cree la estructura ingresando n y presionando 'Crear'.");
    }

    private void onCrear() {
        String s = tfN.getText().trim();
        if (s.isEmpty()) {
            appendLog("Ingrese un valor n antes de Crear.");
            return;
        }
        try {
            int n = Integer.parseInt(s);
            if (n <= 0) {
                appendLog("n debe ser positivo.");
                return;
            }
            createStructure(n);
            appendLog(String.format("Estructura creada: n=%d, bloqueSize=%d, numberBlocks=%d", totalCapacity, blockSize, numberBlocks));
        } catch (NumberFormatException ex) {
            appendLog("n debe ser un número entero.");
        }
    }

    private void onInsert() {
        String s = tfValue.getText().trim();
        if (s.isEmpty()) {
            appendLog("Ingrese un valor a insertar.");
            return;
        }
        if (totalCapacity == 0) {
            appendLog("Primero cree la estructura (botón Crear)." );
            return;
        }
        try {
            int val = Integer.parseInt(s);
            insertKey(val);
        } catch (NumberFormatException ex) {
            appendLog("El valor a insertar debe ser un entero.");
        }
    }

    private void onBuscar() {
        String s = tfValue.getText().trim();
        if (s.isEmpty()) {
            appendLog("Ingrese un valor a buscar.");
            return;
        }
        if (totalCapacity == 0) {
            appendLog("Primero cree la estructura (botón Crear).");
            return;
        }
        try {
            int key = Integer.parseInt(s);
            animatedSearch(key); // <-- ahora con animación
        } catch (NumberFormatException ex) {
            appendLog("El valor a buscar debe ser un entero.");
        }
    }


    private void animatedSearch(int key) {
        // limpiar resaltados previos
        clearHighlights();

        // desactivar controles para evitar conflictos
        setControlsEnabled(false);

        SwingWorker<ExternalSearchResult, HighlightStep> worker = new SwingWorker<>() {
            @Override
            protected ExternalSearchResult doInBackground() throws Exception {
                int comparisons = 0;

                for (int bi = 0; bi < numberBlocks; bi++) {
                    List<Integer> block = blocks.get(bi);
                    if (block.isEmpty()) continue;

                    int lastIdx = block.size() - 1;
                    int last = block.get(lastIdx);

                    comparisons++;
                    // destacar el último del bloque (comparación externa)
                    publish(new HighlightStep(bi, lastIdx, new Color(255, 153, 153),
                            "Comparando " + key + " con último del bloque " + (bi + 1) + ": " + last));
                    Thread.sleep(350); // duración visible de la comparación

                    if (key > last) {
                        publish(new HighlightStep(bi, lastIdx, null,
                                key + " > " + last + " -> no está en bloque " + (bi + 1)));
                        Thread.sleep(150);
                        // limpiar el resaltado del último
                        publish(new HighlightStep(bi, lastIdx, Color.WHITE, null));
                        continue;
                    } else {
                        publish(new HighlightStep(bi, lastIdx, null,
                                key + " <= " + last + " -> buscar lineal en bloque " + (bi + 1)));
                        Thread.sleep(200);

                        // **IMPORTANTE**: limpiar el resaltado del 'last' antes de iniciar la búsqueda
                        // para evitar que quede rojo si luego encontramos en el bloque.
                        publish(new HighlightStep(bi, lastIdx, Color.WHITE, null));
                        Thread.sleep(80);

                        // búsqueda lineal dentro del bloque (de principio a fin)
                        for (int pos = 0; pos < block.size(); pos++) {
                            comparisons++;
                            publish(new HighlightStep(bi, pos, new Color(255, 153, 153),
                                    "Comparando con bloque[" + (bi + 1) + "][" + (pos + 1) + "] = " + block.get(pos)));
                            Thread.sleep(300);

                            if (block.get(pos) == key) {
                                // encontrado: pintar verde y retornar
                                publish(new HighlightStep(bi, pos, Color.GREEN,
                                        "Encontrado " + key + " en bloque " + (bi + 1) + ", posición " + (pos + 1)));
                                return new ExternalSearchResult(true, bi, pos, comparisons);
                            } else {
                                // limpiar el rojo de la celda actual
                                publish(new HighlightStep(bi, pos, Color.WHITE, null));
                            }
                        }
                        // si terminó el bloque sin encontrar:
                        return new ExternalSearchResult(false, bi, -1, comparisons);
                    }
                }
                return new ExternalSearchResult(false, -1, -1, comparisons);
            }

            @Override
            protected void process(List<HighlightStep> chunks) {
                // process recibe varios pasos; aplicamos en orden
                for (HighlightStep s : chunks) {
                    if (s.log != null) appendLog(s.log);
                    if (s.color != null) {
                        // aplicar color a la celda indicada
                        if (s.blockIndex >= 0 && s.blockIndex < blockLabels.size()) {
                            List<JLabel> labels = blockLabels.get(s.blockIndex);
                            if (s.position >= 0 && s.position < labels.size()) {
                                JLabel cell = labels.get(s.position);
                                if (s.color.equals(Color.WHITE)) {
                                    // reset visual
                                    cell.setBackground(Color.WHITE);
                                    cell.setOpaque(true);
                                    cell.setBorder(new LineBorder(new Color(10, 80, 200), 1));
                                } else {
                                    cell.setBackground(s.color);
                                    cell.setOpaque(true);
                                    if (s.color.equals(Color.GREEN)) {
                                        cell.setBorder(new LineBorder(Color.GREEN.darker(), 2));
                                    } else {
                                        cell.setBorder(new LineBorder(Color.RED.darker(), 2));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            protected void done() {
                try {
                    ExternalSearchResult res = get(); // resultado de la búsqueda
                    if (!res.found) {
                        appendLog("No encontrado " + tfValue.getText().trim() + " (comparaciones: " + res.comparisons + ")");
                    } else {
                        appendLog(String.format("Encontrado %d en bloque %d, posición %d (comparaciones: %d)",
                                Integer.parseInt(tfValue.getText().trim()), res.blockIndex + 1, res.position + 1, res.comparisons));
                    }
                } catch (Exception ex) {
                    appendLog("Búsqueda interrumpida: " + ex.getMessage());
                } finally {
                    // reactivar controles
                    setControlsEnabled(true);
                }
            }
        };

        worker.execute();
    }



    private void onEliminar() {
        String s = tfValue.getText().trim();
        if (s.isEmpty()) {
            appendLog("Ingrese un valor a eliminar.");
            return;
        }
        if (totalCapacity == 0) {
            appendLog("Primero cree la estructura (botón Crear)." );
            return;
        }
        try {
            int key = Integer.parseInt(s);
            boolean ok = deleteKey(key);
            if (ok) appendLog("Eliminado " + key + " y reordenada la estructura.");
            else appendLog("No se encontró el valor " + key + " para eliminar.");
        } catch (NumberFormatException ex) {
            appendLog("El valor a eliminar debe ser un entero.");
        }
    }

    private void onLimpiar() {
        clearAll();
        appendLog("Estructura limpiada.");
    }

    // Guarda la estructura en un archivo .ext (formato simple):
    // primera línea: n (totalCapacity)
    // segunda línea: valores separados por comas (ordenados)
    // devuelve void pero actualiza lastSaveSuccess
    public void onSave() {
        lastSaveSuccess = false;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar estructura");
        chooser.setFileFilter(new FileNameExtensionFilter("External structure files (*.ext)", "ext"));

        int res = chooser.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) {
            appendLog("Guardado cancelado.");
            return;
        }

        File f = chooser.getSelectedFile();
        if (!f.getName().toLowerCase().endsWith(".ext")) {
            f = new File(f.getParentFile(), f.getName() + ".ext");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            // Línea 1: n
            bw.write(Integer.toString(totalCapacity));
            bw.newLine();
            // Línea 2: datos separados por comas (si no hay datos, línea vacía)
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < allData.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(allData.get(i));
            }
            bw.write(sb.toString());
            bw.newLine();

            appendLog("Guardado correctamente en: " + f.getAbsolutePath());
            lastSaveSuccess = true;
        } catch (IOException ex) {
            appendLog("Error al guardar: " + ex.getMessage());
            lastSaveSuccess = false;
        }
    }

    // Guarda y luego sale (si el guardado fue exitoso se cierra la app; si se canceló o falló,
    // pregunta al usuario mediante un dialog simple si quiere salir de todas formas)
    public void onSaveExit() {
        onSave(); // actualiza lastSaveSuccess
        if (lastSaveSuccess) {
            appendLog("Guardado exitoso. Saliendo...");
            System.exit(0);
        } else {
            int opt = JOptionPane.showConfirmDialog(this,
                    "No se pudo guardar o se canceló. ¿Salir igualmente?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                System.exit(0);
            } else {
                appendLog("Salida cancelada.");
            }
        }
    }

    // Carga un archivo .ext (formato esperado: primera línea n, segunda línea valores separados por comas
    // o bien múltiples líneas con un valor por línea). Reconstruye la estructura y carga los datos.
    public void onLoad() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Recuperar estructura");
        chooser.setFileFilter(new FileNameExtensionFilter("External structure files (*.ext)", "ext"));

        int res = chooser.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) {
            appendLog("Recuperación cancelada.");
            return;
        }

        File f = chooser.getSelectedFile();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line1 = br.readLine();
            if (line1 == null) {
                appendLog("Archivo vacío o inválido.");
                return;
            }
            int n;
            try {
                n = Integer.parseInt(line1.trim());
            } catch (NumberFormatException ex) {
                appendLog("Formato inválido en primera línea (n).");
                return;
            }

            // leer el resto de líneas
            List<String> rest = new ArrayList<>();
            String l;
            while ((l = br.readLine()) != null) {
                if (!l.trim().isEmpty()) rest.add(l.trim());
            }

            List<Integer> loaded = new ArrayList<>();
            if (rest.size() == 1 && rest.get(0).contains(",")) {
                // formato: coma-separado en una sola línea
                String[] parts = rest.get(0).split(",");
                for (String p : parts) {
                    p = p.trim();
                    if (!p.isEmpty()) {
                        try { loaded.add(Integer.parseInt(p)); }
                        catch (NumberFormatException ex) {
                            appendLog("Valor inválido ignorado: " + p);
                        }
                    }
                }
            } else {
                // formato: un valor por línea o múltiples líneas
                for (String sVal : rest) {
                    try { loaded.add(Integer.parseInt(sVal)); }
                    catch (NumberFormatException ex) {
                        appendLog("Valor inválido ignorado: " + sVal);
                    }
                }
            }

            // ahora reconstruir
            createStructure(n); // crea y limpia estructura
            // añadir los valores (si exceden n, se truncarán)
            for (int v : loaded) {
                if (allData.size() >= totalCapacity) {
                    appendLog("Se alcanzó capacidad n; algunos valores cargados fueron truncados.");
                    break;
                }
                allData.add(v);
            }
            Collections.sort(allData);
            redistributeToBlocks();
            appendLog("Recuperado archivo: " + f.getAbsolutePath() + " (datos cargados: " + allData.size() + ")");
        } catch (IOException ex) {
            appendLog("Error al leer archivo: " + ex.getMessage());
        }
    }

    public void onVolver() {
        this.dispose();
        PrincipalPage.getInstance().setVisible(true);
    }

    // --- Lógica de la estructura ---
    private void createStructure(int n) {
        totalCapacity = n;
        blockSize = (int) Math.floor(Math.sqrt(n));
        if (blockSize <= 0) blockSize = 1;
        numberBlocks = (int) Math.ceil((double) n / blockSize);

        allData.clear();
        blocks.clear();
        blockLabels.clear();
        for (int i = 0; i < numberBlocks; i++) {
            blocks.add(new ArrayList<>());
            blockLabels.add(new ArrayList<>());
        }
        rebuildBlocksPanel();
    }

    private void insertKey(int key) {
        if (allData.size() >= totalCapacity) {
            appendLog("La estructura está llena (n alcanzado). No se puede insertar más.");
            return;
        }
        allData.add(key);
        Collections.sort(allData);
        redistributeToBlocks();
        appendLog(String.format("Insertado %d. Total datos: %d/%d", key, allData.size(), totalCapacity));
    }

    private boolean deleteKey(int key) {
        boolean removed = allData.remove((Integer) key);
        if (removed) {
            redistributeToBlocks();
        }
        return removed;
    }

    private void redistributeToBlocks() {
        // vaciar
        for (List<Integer> b : blocks) b.clear();
        // llenar secuencialmente con capacidad blockSize por bloque
        int idx = 0;
        for (int val : allData) {
            int bi = idx / blockSize;
            if (bi >= numberBlocks) bi = numberBlocks - 1; // safety
            blocks.get(bi).add(val);
            idx++;
        }
        rebuildBlocksPanel();
    }

    // --- Algoritmo de búsqueda externa (según la descripción) ---

    // --- GUI reconstrucción y utilidades ---
    private void rebuildBlocksPanel() {
        blocksPanel.removeAll();
        blockLabels.clear();

        // mostrar bloques en una fila horizontal con scroll si many
        blocksPanel.setLayout(new GridLayout(1, numberBlocks, 12, 12));

        for (int bi = 0; bi < numberBlocks; bi++) {
            JPanel bp = new JPanel();
            bp.setBackground(Color.WHITE);
            bp.setBorder(new LineBorder(new Color(30, 120, 255), 3, true)); // borde azul
            bp.setLayout(new BorderLayout());

            // Titulo del bloque
            JLabel title = new JLabel("Bloque " + (bi + 1), SwingConstants.CENTER);
            title.setPreferredSize(new Dimension(120, 24));
            title.setOpaque(true);
            title.setBackground(Color.WHITE);
            bp.add(title, BorderLayout.NORTH);

            // panel con filas (cada fila representa un "slot")
            JPanel rows = new JPanel();
            rows.setBackground(Color.WHITE);
            rows.setLayout(new GridLayout(blockSize, 1, 2, 2));

            List<JLabel> labelsThisBlock = new ArrayList<>();
            List<Integer> data = blocks.get(bi);
            for (int r = 0; r < blockSize; r++) {
                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setPreferredSize(new Dimension(120, 26));
                cell.setOpaque(true);
                cell.setBackground(Color.WHITE);
                cell.setBorder(new LineBorder(new Color(10, 80, 200), 1));
                // si hay dato en esta fila, mostrarlo (fila r corresponde a index r in block)
                if (r < data.size()) {
                    cell.setText(String.valueOf(data.get(r)));
                    cell.setFont(cell.getFont().deriveFont(Font.BOLD, 20f));
                    cell.setForeground(new Color(30, 120, 255));
                }
                rows.add(cell);
                labelsThisBlock.add(cell);
            }
            blockLabels.add(labelsThisBlock);

            bp.add(rows, BorderLayout.CENTER);
            // espacio inferior para total de elementos del bloque
            JLabel footer = new JLabel(String.format("%d / %d", data.size(), blockSize), SwingConstants.CENTER);
            footer.setPreferredSize(new Dimension(120, 22));
            footer.setOpaque(true);
            footer.setBackground(Color.WHITE);
            bp.add(footer, BorderLayout.SOUTH);

            blocksPanel.add(bp);
        }

        blocksPanel.revalidate();
        blocksPanel.repaint();
    }

    private void clearHighlights() {
        for (List<JLabel> lbls : blockLabels) {
            for (JLabel l : lbls) {
                if (l == null) continue;
                l.setBackground(Color.WHITE);
                l.setBorder(new LineBorder(new Color(10, 80, 200), 1));
            }
        }
    }

    private void appendLog(String s) {
        logArea.append(s + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void clearAll() {
        totalCapacity = 0;
        blockSize = 0;
        numberBlocks = 0;
        allData.clear();
        blocks.clear();
        blockLabels.clear();
        blocksPanel.removeAll();
        blocksPanel.revalidate();
        blocksPanel.repaint();
        logArea.setText("");
    }

    private void setControlsEnabled(boolean enabled) {
        // ajusta según los botones que tengas como campos de instancia
        // si declaraste los botones localmente en initUI(), conviértelos a campos de instancia
        // en mi ejemplo original nombres: btnCrear, btnInsert, btnBuscar, btnEliminar, btnLimpiar, saveBtn, saveExitBtn, loadBtn, volverBtn
        for (Component c : ((JPanel) getContentPane().getComponent(0)).getComponents()) {
            // esto es solo una protección genérica; preferible deshabilitar cada botón por nombre si son campos
            if (c instanceof JButton) c.setEnabled(enabled);
        }
        // También evitar inserciones por teclado mientras anima:
        tfValue.setEnabled(enabled);
        tfN.setEnabled(enabled);
    }


    // Helper para la estructura de resultado
    private static class ExternalSearchResult {
        boolean found;
        int blockIndex;
        int position;
        int comparisons;

        ExternalSearchResult(boolean f, int bi, int p, int c) {
            found = f; blockIndex = bi; position = p; comparisons = c;
        }
    }

    private static ExternalSearch instance;

    public static ExternalSearch getInstance() {
        if (instance == null) {
            instance = new ExternalSearch();
        }
        return instance;
    } 


    private static class HighlightStep {
        final int blockIndex;
        final int position;
        final Color color;   // color a aplicar, null = solo log
        final String log;    // mensaje para el log, puede ser null

        HighlightStep(int blockIndex, int position, Color color, String log) {
            this.blockIndex = blockIndex;
            this.position = position;
            this.color = color;
            this.log = log;
        }
    }

}
