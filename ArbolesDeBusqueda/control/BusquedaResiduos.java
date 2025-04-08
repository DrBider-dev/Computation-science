package control;
public class BusquedaResiduos {
    
    // Clase interna que representa un nodo del árbol
    private class Nodo {
        char valor;      // Almacena la letra insertada al final de una ruta
        Nodo izquierdo;  // Rama para el bit '0'
        Nodo derecho;    // Rama para el bit '1'
        
        // Constructor para un nodo sin asignación de valor (inicialmente puede estar vacío)
        Nodo() {
            this.valor = '\0';  // Caracter nulo, indica que aún no se asigna ningún valor
            this.izquierdo = null;
            this.derecho = null;
        }
    }
    
    // Raíz del árbol
    private Nodo raiz;
    
    // Constructor del árbol
    public BusquedaResiduos() {
        raiz = new Nodo();
    }
    
    /**
     * Método para insertar una clave en el árbol.
     * Cada carácter de la clave se procesa de la siguiente manera:
     *   1. Se obtiene su código ASCII.
     *   2. Se convierte ese código en un string binario de 8 bits.
     *   3. Se recorre el árbol: un '0' baja por la izquierda y un '1' por la derecha.
     *   4. Al final de la ruta (8 niveles) se asigna la letra en ese nodo.
     * 
     * @param clave La cadena de texto que se desea insertar.
     */
    public void insertarClave(String clave) {
        // Para cada carácter de la clave
        for (int i = 0; i < clave.length(); i++) {
            char letra = clave.charAt(i);
            int codigoAscii = (int) letra;
            // Convertir a cadena binaria de 8 bits
            String binario = convertirACadenaBinaria(codigoAscii);
            // Insertar la letra siguiendo el camino definido por la cadena binaria
            insertar(letra, binario);
        }
    }
    
    /**
     * Convierte un número entero (código ASCII) a una cadena binaria de 8 bits.
     * @param numero El número a convertir.
     * @return Una cadena de 8 caracteres compuesta por '0' y '1'.
     */
    private String convertirACadenaBinaria(int numero) {
        String binario = Integer.toBinaryString(numero);
        // Rellenar con ceros a la izquierda si la longitud es menor a 8
        while (binario.length() < 8) {
            binario = "0" + binario;
        }
        return binario;
    }
    
    /**
     * Inserta un carácter en el árbol siguiendo el camino dado por la cadena binaria.
     * Si la posición final ya está ocupada, muestra un mensaje de error.
     * @param letra El carácter a insertar.
     * @param caminoCadena La cadena binaria que determina el camino en el árbol.
     */
    private void insertar(char letra, String caminoCadena) {
        Nodo actual = raiz;
        // Recorrer cada bit de la cadena binaria
        for (int i = 0; i < caminoCadena.length(); i++) {
            char bit = caminoCadena.charAt(i);
            if (bit == '0') {
                // Si no existe nodo izquierdo, se crea
                if (actual.izquierdo == null) {
                    actual.izquierdo = new Nodo();
                }
                actual = actual.izquierdo;
            } else if (bit == '1') {
                // Si no existe nodo derecho, se crea
                if (actual.derecho == null) {
                    actual.derecho = new Nodo();
                }
                actual = actual.derecho;
            }
        }
        // Si la posición ya tiene una letra asignada, se muestra un error
        if (actual.valor != '\0') {
            System.err.println("Error: la posición para la letra '" + letra 
                    + "' ya está ocupada por la letra '" + actual.valor + "'.");
        } else {
            // Al final de la ruta se asigna la letra en el nodo
            actual.valor = letra;
        }
    }
    
    /**
     * Método para imprimir el árbol en preorden mostrando las rutas y las letras almacenadas.
     */
    public void imprimirArbol() {
        System.out.println("Recorrido del árbol (Preorden):");
        imprimirPreorden(raiz, "");
    }
    
    /**
     * Método recursivo para recorrer el árbol en preorden.
     * @param nodo El nodo actual.
     * @param ruta La cadena que representa el camino (ruta) desde la raíz hasta el nodo actual.
     */
    private void imprimirPreorden(Nodo nodo, String ruta) {
        if (nodo != null) {
            // Si el nodo tiene un valor asignado (diferente de '\0'), se imprime
            if (nodo.valor != '\0') {
                System.out.println("Ruta: " + ruta + " => Letra: " + nodo.valor);
            }
            imprimirPreorden(nodo.izquierdo, ruta + "0");
            imprimirPreorden(nodo.derecho, ruta + "1");
        }
    }
    
    // Método main para pruebas
    public static void main(String[] args) {
        BusquedaResiduos arbol = new BusquedaResiduos();
        
        // Ejemplo de inserción: La clave "ARROZ"
        // Se intentará insertar la letra 'R' dos veces, lo cual generará un mensaje de error.
        String clave = "PRUEBA";
        arbol.insertarClave(clave);
        
        // Imprimir el árbol
        arbol.imprimirArbol();
    }
}
