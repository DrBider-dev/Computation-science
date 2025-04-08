package Control;
public class BusquedaArbolDigital {
    
    // Clase interna que representa un nodo en el árbol digital
    private class Nodo {
        char letra;      // Almacena la letra insertada en ese nodo
        Nodo izquierdo;  // Rama que representa el bit '0'
        Nodo derecho;    // Rama que representa el bit '1'
        
        // Constructor que inicializa el nodo sin letra (con valor '\0')
        Nodo() {
            this.letra = '\0';
            this.izquierdo = null;
            this.derecho = null;
        }
    }
    
    // Raíz del árbol
    private Nodo raiz;
    
    // Constructor del árbol
    public BusquedaArbolDigital() {
        raiz = null;
    }
    
    /**
     * Inserta una cadena (compuesta de letras entre A y Z) en el árbol.
     * Se respeta el orden de entrada:
     *   - La primera letra se asigna a la raíz.
     *   - Para las siguientes letras, se utiliza la representación binaria de 5 bits
     *     (obtenida a partir del número asignado a cada letra, A=1, …, Z=26) para recorrer
     *     el árbol y buscar la primera posición vacía siguiendo estos dígitos.
     * 
     * @param clave La cadena de letras a insertar.
     */
    public void insertarClave(String clave) {
        // Convertir la cadena a mayúsculas para trabajar uniformemente
        clave = clave.toUpperCase();
        
        for (int i = 0; i < clave.length(); i++) {
            char letra = clave.charAt(i);
            // Verificar que la letra esté en el rango A-Z
            if (letra < 'A' || letra > 'Z') {
                System.err.println("Error: la letra '" + letra + "' no está en el rango A-Z.");
                continue;
            }
            if (i == 0) {
                // La primera letra se inserta en la raíz
                raiz = new Nodo();
                raiz.letra = letra;
            } else {
                // Para las siguientes letras se inserta siguiendo el camino determinado
                // a partir de la representación binaria de 5 bits
                int numeroLetra = letra - 'A' + 1; // A=1, B=2, ..., Z=26
                String binario = convertirACadenaBinaria(numeroLetra);
                insertarLetra(letra, binario);
            }
        }
    }
    
    /**
     * Convierte el número (del 1 al 26) a una cadena binaria de 5 bits.
     * @param numero Número a convertir.
     * @return Una cadena de 5 caracteres compuesta de '0' y '1'.
     */
    private String convertirACadenaBinaria(int numero) {
        String binario = Integer.toBinaryString(numero);
        // Rellenar con ceros a la izquierda hasta obtener 5 dígitos
        while (binario.length() < 5) {
            binario = "0" + binario;
        }
        return binario;
    }
    
    /**
     * Inserta una letra en el árbol siguiendo el camino dado por la cadena binaria.
     * Se recorre la cadena binaria de 5 bits y en el primer nodo no ocupado se inserta la letra.
     * Si no se encuentra una posición vacía en el camino, se muestra un mensaje de error.
     * 
     * @param letra La letra a insertar.
     * @param caminoCadena La representación binaria de 5 bits asignada a la letra.
     */
    private void insertarLetra(char letra, String caminoCadena) {
        Nodo actual = raiz;
        boolean insertado = false;
        
        // Recorrer cada dígito del camino
        for (int i = 0; i < caminoCadena.length(); i++) {
            char bit = caminoCadena.charAt(i);
            if (bit == '0') {
                if (actual.izquierdo == null) {
                    actual.izquierdo = new Nodo();
                    actual.izquierdo.letra = letra;
                    insertado = true;
                    break;
                } else {
                    actual = actual.izquierdo;
                }
            } else if (bit == '1') {
                if (actual.derecho == null) {
                    actual.derecho = new Nodo();
                    actual.derecho.letra = letra;
                    insertado = true;
                    break;
                } else {
                    actual = actual.derecho;
                }
            }
        }
        if (!insertado) {
            System.err.println("Error: No se pudo insertar la letra '" + letra 
                               + "'. El camino binario '" + caminoCadena + "' ya está completamente ocupado.");
        }
    }
    
    /**
     * Recorre e imprime el árbol mediante recorrido preorden, mostrando la ruta
     * (dada por la secuencia de bits) y la letra almacenada en cada nodo.
     */
    public void imprimirArbol() {
        System.out.println("Recorrido del árbol de búsqueda digital (Preorden):");
        imprimirPreorden(raiz, "");
    }
    
    /**
     * Método recursivo para recorrer el árbol en preorden.
     * @param nodo Nodo actual del recorrido.
     * @param ruta Acumulador de la ruta en forma de cadena de bits.
     */
    private void imprimirPreorden(Nodo nodo, String ruta) {
        if (nodo != null) {
            // Mostrar la letra almacenada en el nodo si no es el carácter nulo
            if (nodo.letra != '\0') {
                System.out.println("Ruta: " + (ruta.equals("") ? "Raíz" : ruta) + " => Letra: " + nodo.letra);
            }
            imprimirPreorden(nodo.izquierdo, ruta + "0");
            imprimirPreorden(nodo.derecho, ruta + "1");
        }
    }
    
    /**
     * Elimina una clave (cadena de letras) del árbol digital.
     * Para cada letra de la clave se recorre el árbol siguiendo su representación
     * binaria de 5 bits y, si se encuentra la letra en el nodo correspondiente, se procede a
     * eliminarla. La eliminación se efectúa asignando el carácter nulo '\0' y, de ser posible,
     * eliminando el nodo si es hoja (sin hijos) para optimizar la estructura.
     * 
     * @param clave La cadena de letras a eliminar.
     */
    public boolean eliminarClave(String clave) {
        // Convertir la cadena a mayúsculas para trabajar uniformemente
        clave = clave.toUpperCase();

        if (clave == null || clave.length() == 0) {
            System.err.println("La clave está vacía.");
            return false;
        }

        boolean eliminada = false;

        // Eliminar la primera letra (debe encontrarse en la raíz)
        char letra = clave.charAt(0);
        if (raiz == null) {
            System.err.println("El árbol está vacío. No se puede eliminar la letra '" + letra + "'.");
            return false;
        }
        if (raiz.letra == letra) {
            if (raiz.izquierdo == null && raiz.derecho == null) {
                raiz = null;
            } else {
                raiz.letra = '\0';
            }
            System.out.println("Letra '" + letra + "' eliminada de la raíz.");
            eliminada = true;
        } else {
            System.err.println("La letra '" + letra + "' no se encontró en la raíz. No se puede eliminar.");
            return false;
        }

        // Eliminar el resto de letras de la clave
        for (int i = 1; i < clave.length(); i++) {
            char letraActual = clave.charAt(i);
            int numeroLetra = letraActual - 'A' + 1;
            String binario = convertirACadenaBinaria(numeroLetra);
            Nodo actual = raiz;
            Nodo padre = null;
            char direccion = ' ';
            boolean encontrado = false;

            for (int j = 0; j < binario.length(); j++) {
                char bit = binario.charAt(j);
                padre = actual;
                if (bit == '0') {
                    if (actual.izquierdo != null) {
                        actual = actual.izquierdo;
                        direccion = '0';
                    } else {
                        break;
                    }
                } else {
                    if (actual.derecho != null) {
                        actual = actual.derecho;
                        direccion = '1';
                    } else {
                        break;
                    }
                }
                if (actual.letra == letraActual) {
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                actual.letra = '\0';
                System.out.println("Letra '" + letraActual + "' eliminada en el camino " + binario + ".");
                eliminada = true;

                if (actual.izquierdo == null && actual.derecho == null) {
                    if (direccion == '0') {
                        padre.izquierdo = null;
                    } else if (direccion == '1') {
                        padre.derecho = null;
                    }
                }
            } else {
                System.err.println("La letra '" + letraActual + "' no se encontró en el árbol.");
            }
        }

        return eliminada;
    }

    
    public String buscarClave(String clave) {
        clave = clave.toUpperCase();
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < clave.length(); i++) {
            char letraObjetivo = clave.charAt(i);
            String rutaEncontrada = buscarLetra(raiz, letraObjetivo, "");
            if (rutaEncontrada == null) {
                return "La clave no se encontró en el árbol.";
            } else {
                resultado.append(letraObjetivo)
                         .append(": ")
                         .append(rutaEncontrada.equals("") ? "Raíz" : rutaEncontrada)
                         .append("\n");
            }
        }
        return resultado.toString();
    }

    private String buscarLetra(Nodo nodo, char letraObjetivo, String ruta) {
        if (nodo == null) {
            return null;
        }
        if (nodo.letra == letraObjetivo) {
            return ruta;
        }
        String izquierda = buscarLetra(nodo.izquierdo, letraObjetivo, ruta + "0");
        if (izquierda != null) {
            return izquierda;
        }
        return buscarLetra(nodo.derecho, letraObjetivo, ruta + "1");
    }
}
