package Control;
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
    
    /**
     * Método para eliminar una clave (o cada letra de la cadena) del árbol.
     * Se utiliza la misma ruta basada en la representación binaria de 8 bits.
     * 
     * @param clave La cadena de texto que se desea eliminar.
     */
    public boolean eliminarClave(String clave) {
        boolean eliminado = false;
        // Para cada carácter de la clave, se elimina según su camino binario
        for (int i = 0; i < clave.length(); i++) {
            char letra = clave.charAt(i);
            int codigoAscii = (int) letra;
            String binario = convertirACadenaBinaria(codigoAscii);

            // Llamamos al método eliminar y comprobamos si el nodo final fue eliminado
            boolean[] fueEliminada = {false};
            raiz = eliminar(raiz, binario, 0, letra, fueEliminada);
            if (fueEliminada[0]) {
                eliminado = true;
            }
        }
        return eliminado;
    }

    /**
     * Método recursivo para eliminar una letra del árbol.
     * Se recorre el árbol siguiendo la cadena binaria. Cuando se alcanza el nodo final,
     * se verifica que la letra coincida y se elimina (se vacía el nodo).
     * Luego se "poda" el árbol eliminando nodos sin valor y sin hijos.
     * 
     * @param nodo   Nodo actual en el recorrido.
     * @param camino Cadena binaria que determina el camino en el árbol.
     * @param index  Posición actual en la cadena binaria.
     * @param letra  La letra que se desea eliminar.
     * @return El nodo actualizado (o null si se elimina).
     */
    private Nodo eliminar(Nodo nodo, String camino, int index, char letra, boolean[] fueEliminada) {
        if (nodo == null) {
            System.err.println("No se encontró la letra '" + letra + "' en el árbol.");
            return null;
        }
        if (index == camino.length()) {
            if (nodo.valor == letra) {
                nodo.valor = '\0';
                fueEliminada[0] = true;
            } else {
                System.err.println("Error: en la ruta definida por " + camino 
                        + " se encontró la letra '" + nodo.valor + "', que no coincide con '" + letra + "'.");
            }
        } else {
            char bit = camino.charAt(index);
            if (bit == '0') {
                nodo.izquierdo = eliminar(nodo.izquierdo, camino, index + 1, letra, fueEliminada);
            } else {  // bit == '1'
                nodo.derecho = eliminar(nodo.derecho, camino, index + 1, letra, fueEliminada);
            }
        }

        if (nodo.valor == '\0' && nodo.izquierdo == null && nodo.derecho == null) {
            return null;
        }
        return nodo;
    }
    
    /**
     * Método para buscar una letra en el árbol.
     * Se calcula la ruta a partir de la representación binaria de 8 bits (ASCII)
     * de la letra y se recorre el árbol siguiendo dicha ruta.
     * 
     * @param letra La letra que se desea buscar.
     * @return La cadena de bits (ruta) que indica la posición de la letra en el árbol,
     *         o un mensaje indicando que la letra no fue encontrada.
     */
    private String buscarLetra(char letra) {
        int codigoAscii = (int) letra;
        String camino = convertirACadenaBinaria(codigoAscii);
        Nodo actual = raiz;
        StringBuilder ruta = new StringBuilder();
        for (int i = 0; i < camino.length(); i++) {
            char bit = camino.charAt(i);
            ruta.append(bit);
            if (bit == '0') {
                if (actual.izquierdo == null) {
                    return "La letra '" + letra + "' no se encuentra en el árbol.";
                }
                actual = actual.izquierdo;
            } else { // bit == '1'
                if (actual.derecho == null) {
                    return "La letra '" + letra + "' no se encuentra en el árbol.";
                }
                actual = actual.derecho;
            }
        }
        if (actual.valor == letra) {
            return ruta.toString();
        } else {
            return "La letra '" + letra + "' no se encuentra en el árbol.";
        }
    }
    
    /**
     * Método para buscar múltiples letras en el árbol.
     * Para cada letra en el string ingresado se busca la posición en el árbol
     * usando el método buscarLetra(char) y se acumulan los resultados.
     * 
     * @param claves String con una o más letras a buscar.
     * @return Una cadena con el resultado de la búsqueda para cada letra.
     */
    public String buscarLetras(String claves) {
        StringBuilder resultado = new StringBuilder();
        // Se recorren cada uno de los caracteres de la cadena
        for (int i = 0; i < claves.length(); i++) {
            char letra = claves.charAt(i);
            // Se busca la letra
            String pos = buscarLetra(letra);
            resultado.append("Letra '")
                     .append(letra)
                     .append("': ")
                     .append(pos)
                     .append("\n");
        }
        return resultado.toString();
    }
}
