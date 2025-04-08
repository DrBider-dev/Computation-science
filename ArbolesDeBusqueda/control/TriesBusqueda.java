package control;
public class TriesBusqueda {

    // Clase interna que representa un nodo en el trie.
    // Un nodo terminal almacena una letra en el campo 'letra'.
    // Un nodo de enlace (interno) tiene 'letra' igual a '\0'.
    private class Nodo {
        char letra;
        Nodo izquierdo;
        Nodo derecho;
        
        Nodo() {
            this.letra = '\0';
            this.izquierdo = null;
            this.derecho = null;
        }
    }
    
    // La raíz del trie; permanece vacía (nodo de enlace) y se crean dos hijos.
    private Nodo raiz;

    // Constructor: inicializa la raíz vacía y crea dos nodos hijos.
    public TriesBusqueda() {
        raiz = new Nodo();
        raiz.izquierdo = new Nodo();
        raiz.derecho   = new Nodo();
    }
    
    /**
     * Convierte una letra (A–Z) a su representación binaria de 5 bits.
     * Se asigna A=1, B=2, …, Z=26.
     * @param letra La letra a convertir.
     * @return Una cadena binaria de 5 dígitos.
     */
    private String convertirLetraABinario(char letra) {
        int num = letra - 'A' + 1;  // A=1, B=2, ..., Z=26
        String bin = Integer.toBinaryString(num);
        while(bin.length() < 5) {
            bin = "0" + bin;
        }
        return bin;
    }
    
    /**
     * Inserta cada letra del string en el trie.
     * Se recorre el string (convertido a mayúsculas) y para cada letra se obtiene su
     * representación binaria de 5 bits. La inserción se realiza siguiendo dígito a dígito.
     * La raíz permanece vacía.
     * 
     * @param clave El string a insertar (debe contener letras de A a Z).
     */
    public void insertarClave(String clave) {
        clave = clave.toUpperCase();
        
        for (int i = 0; i < clave.length(); i++) {
            char letra = clave.charAt(i);
            if (letra < 'A' || letra > 'Z') {
                System.err.println("Error: La letra '" + letra + "' no está en el rango A-Z.");
                continue;
            }
            String binario = convertirLetraABinario(letra);
            // Para cada letra se parte desde la raíz.
            raiz = insertarNodo(raiz, letra, binario, 0);
        }
    }
    
    /**
     * Inserta una letra en el trie siguiendo su representación binaria.
     * Este método trabaja recursivamente. El parámetro 'pos' indica
     * el dígito actual a leer de la cadena binaria.
     * 
     * @param nodo El nodo actual (puede ser nulo).
     * @param letra La letra a insertar.
     * @param binario La representación binaria de 5 bits de la letra.
     * @param pos La posición actual en la cadena binaria.
     * @return El nodo actualizado tras la inserción.
     */
    private Nodo insertarNodo(Nodo nodo, char letra, String binario, int pos) {
        // Caso base: si el nodo es null, se crea y se asigna la letra.
        if (nodo == null) {
            nodo = new Nodo();
            nodo.letra = letra;
            return nodo;
        }
        
        // Si el nodo ya almacena una letra, hay colisión.
        if (nodo.letra != '\0') {
            char letraExistente = nodo.letra;
            nodo.letra = '\0';  // Vaciar el nodo para convertirlo en nodo de enlace.
            
            // Crear los dos hijos si aún no existen.
            if (nodo.izquierdo == null) {
                nodo.izquierdo = new Nodo();
            }
            if (nodo.derecho == null) {
                nodo.derecho = new Nodo();
            }
            
            // Reinserta la letra que estaba almacenada.
            String binExistente = convertirLetraABinario(letraExistente);
            if (pos < binExistente.length()) {
                char bitExistente = binExistente.charAt(pos);
                if (bitExistente == '0') {
                    nodo.izquierdo = insertarNodo(nodo.izquierdo, letraExistente, binExistente, pos + 1);
                } else {  // bitExistente == '1'
                    nodo.derecho = insertarNodo(nodo.derecho, letraExistente, binExistente, pos + 1);
                }
            } else {
                nodo.letra = letraExistente;
            }
            
            // Inserta la nueva letra, usando también el dígito siguiente.
            if (pos < binario.length()) {
                char bitNuevo = binario.charAt(pos);
                if (bitNuevo == '0') {
                    nodo.izquierdo = insertarNodo(nodo.izquierdo, letra, binario, pos + 1);
                } else {  // bitNuevo == '1'
                    nodo.derecho = insertarNodo(nodo.derecho, letra, binario, pos + 1);
                }
            } else {
                nodo.letra = letra;
            }
            return nodo;
        } else {
            // Nodo de enlace (sin letra almacenada)
            // Si ya se han consumido todos los dígitos, se coloca la letra aquí.
            if (pos >= binario.length()) {
                nodo.letra = letra;
                return nodo;
            }
            // Se lee el dígito actual.
            char bit = binario.charAt(pos);
            if (bit == '0') {
                nodo.izquierdo = insertarNodo(nodo.izquierdo, letra, binario, pos + 1);
            } else {  // bit == '1'
                nodo.derecho = insertarNodo(nodo.derecho, letra, binario, pos + 1);
            }
            return nodo;
        }
    }
    
    /**
     * Recorre el trie en preorden para imprimir las rutas (secuencias de bits)
     * y la letra almacenada en cada nodo terminal.
     */
    public void imprimirTrie() {
        System.out.println("Recorrido del Trie (Preorden):");
        imprimirPreorden(raiz, "");
    }
    
    /**
     * Recorre recursivamente el trie en preorden.
     * @param nodo Nodo actual en el recorrido.
     * @param ruta Cadena que representa la secuencia de bits desde la raíz.
     */
    private void imprimirPreorden(Nodo nodo, String ruta) {
        if (nodo != null) {
            if (nodo.letra != '\0') {
                System.out.println("Ruta: " + (ruta.equals("") ? "Raíz" : ruta) 
                        + " => Letra: " + nodo.letra);
            }
            imprimirPreorden(nodo.izquierdo, ruta + "0");
            imprimirPreorden(nodo.derecho, ruta + "1");
        }
    }
    
    // Método main para pruebas.
    public static void main(String[] args) {
        TriesBusqueda trie = new TriesBusqueda();
        
        // Ejemplo con la clave "PRUEBA". Verifica que, al generarse colisiones (por ejemplo al insertar 'R'),
        // se realice el proceso de vaciado del nodo y reinserción.
        String clave1 = "PRUEBA";
        System.out.println("Insertando clave: " + clave1);
        trie.insertarClave(clave1);
        trie.imprimirTrie();
        
        // Otra prueba con otra clave.
        System.out.println("\nInsertando clave: ARBOL");
        TriesBusqueda trie2 = new TriesBusqueda();
        trie2.insertarClave("ARBOL");
        trie2.imprimirTrie();
    }
}
