package Control;
import java.util.*;

public class ArbolHuffman {
    private String input = "";
    private HuffmanNode root;
    private Map<Character, Integer> frequencyMap;

    private Map<Character, String> codes = new HashMap<>();

    private void generateCodes(HuffmanNode node, String code) {
        if (node == null) return;
        if (node.character != null) {
            codes.put(node.character, code.isEmpty() ? "0" : code); // Caso para 1 único carácter
            return;
        }
        generateCodes(node.left, code + "0");
        generateCodes(node.right, code + "1");
    }

    public void generateCodes() {
        codes.clear(); // Limpiar códigos previos
        generateCodes(root, ""); // Iniciar recursión desde la raíz
    }

    public Map<Character, String> getCodes() {
        return codes;
    }

    // Clase interna para representar nodos del árbol
    private static class HuffmanNode implements Comparable<HuffmanNode> {
        Character character;
        int frequency;
        HuffmanNode left, right;

        HuffmanNode(Character character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        @Override
        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }
    }

    // Método para insertar una nueva cadena y construir el árbol
    public void insert(String str) {
        this.input = str;
        buildFrequencyMap();
        buildHuffmanTree();
        generateCodes(); // Generar códigos después de construir el árbol
    }

    // Construye el mapa de frecuencias
    private void buildFrequencyMap() {
        frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
    }

    // Construye el árbol de Huffman
    private void buildHuffmanTree() {
        if (frequencyMap.isEmpty()) {
            root = null;
            return;
        }

        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        
        // Crear nodos iniciales
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // Caso especial para un único carácter
        if (pq.size() == 1) {
            HuffmanNode node = pq.poll();
            HuffmanNode parent = new HuffmanNode(null, node.frequency);
            parent.left = node;
            pq.add(parent);
        }

        // Construir el árbol combinando nodos
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            
            HuffmanNode parent = new HuffmanNode(
                null, 
                left.frequency + right.frequency
            );
            parent.left = left;
            parent.right = right;
            
            pq.add(parent);
        }
        
        root = pq.poll();
    }

    // Método para buscar caracteres en la cadena original
    public String search(String characters) {
        if (characters == null || characters.isEmpty() || codes.isEmpty()) {
            return null; // Casos inválidos o códigos no generados
        }
    
        StringBuilder result = new StringBuilder();
        for (char c : characters.toCharArray()) {
            String code = codes.get(c);
            if (code != null) {
                result.append(c).append(": ").append(code).append("\n");
            } else {
                result.append(c).append(": No se encontró\n");
            }
        }
        return result.toString().trim(); // Elimina el último salto de línea
    }

    // Método para eliminar caracteres de la cadena y reconstruir el árbol
    public boolean delete(String charsToDelete) {
        if (input.isEmpty() || charsToDelete == null || charsToDelete.isEmpty()) {
            return false; // No hay nada que eliminar
        }
    
        // Guardar longitud original para comparar después
        int originalLength = input.length();
        
        StringBuilder filtered = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (charsToDelete.indexOf(c) == -1) {
                filtered.append(c);
            }
        }
        
        // Verificar si hubo cambios
        if (filtered.length() == originalLength) {
            return false; // No se eliminó ningún carácter
        }
        
        // Actualizar la cadena y reconstruir el árbol
        input = filtered.toString();
        buildFrequencyMap();
        buildHuffmanTree();
        generateCodes(); // Regenerar códigos
        
        return true;
    }

    // Método auxiliar para visualizar el árbol (opcional)
    public void printTree() {
        System.out.println("Árbol de Huffman (pre-order):");
        printTree(root, 0);
    }

    private void printTree(HuffmanNode node, int level) {
        if (node == null) return;
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) sb.append("  ");
        
        if (node.character != null) {
            sb.append("'").append(node.character).append("': ").append(node.frequency);
        } else {
            sb.append("*: ").append(node.frequency);
        }
        
        System.out.println(sb.toString());
        printTree(node.left, level + 1);
        printTree(node.right, level + 1);
    }

    

    // Ejemplo de uso
    public static void main(String[] args) {
        ArbolHuffman huffman = new ArbolHuffman();
        
        huffman.insert("ABRACADABRA");
        huffman.printTree();
        System.out.println("\nCódigos Huffman:");
        huffman.getCodes().forEach((c, code) -> 
            System.out.println("'" + c + "': " + code)
        );
    }
}