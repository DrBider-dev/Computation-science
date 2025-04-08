package control;
import java.util.HashMap;
import java.util.Map;

public class BusquedaResiduosMultiples {

    // Clase interna que representa un nodo del árbol.
    // Cada nodo contiene:
    //   - un campo 'letra' que se asigna en los nodos terminales (default '\0' = vacío).
    //   - un mapa de hijos, donde la clave es el grupo de bits (puede ser "00", "01", "10", "11" o "0", "1")
    private class Nodo {
        char letra;  
        Map<String, Nodo> hijos;  

        Nodo() {
            this.letra = '\0';
            this.hijos = new HashMap<>();
        }
    }

    // La raíz del árbol.
    private Nodo raiz;

    // Constructor: se crea la raíz vacía.
    public BusquedaResiduosMultiples() {
        raiz = new Nodo();
    }

    /**
     * Convierte una letra de A a Z a un número (A=1, …, Z=26) y luego a una cadena
     * binaria de 5 bits.
     *
     * @param letra La letra a convertir.
     * @return Cadena de 5 dígitos en binario.
     */
    private String convertirLetraABinario(char letra) {
        int num = letra - 'A' + 1;
        String bin = Integer.toBinaryString(num);
        while (bin.length() < 5) {
            bin = "0" + bin;
        }
        return bin;
    }

    /**
     * Inserta la clave completa en el árbol. Para cada letra se obtiene su
     * representación binaria de 5 bits y se inserta utilizando grupos de bits.
     *
     * @param clave Cadena de caracteres (A-Z) a insertar.
     */
    public void insertarClave(String clave) {
        clave = clave.toUpperCase();
        for (int i = 0; i < clave.length(); i++) {
            char letra = clave.charAt(i);
            if (letra < 'A' || letra > 'Z') {
                System.err.println("Error: la letra '" + letra + "' no está en el rango A-Z.");
                continue;
            }
            String binario = convertirLetraABinario(letra);
            insertar(raiz, binario, 0, letra);
        }
    }

    /**
     * Inserta recursivamente la letra en el árbol usando la cadena binaria.
     * Se agrupan los dígitos en grupos de 2, excepto cuando no queden 2 bits completos.
     *
     * @param nodo   Nodo actual en el recorrido.
     * @param binario Cadena binaria completa (de 5 bits) de la letra.
     * @param pos    Posición actual en la cadena a procesar.
     * @param letra  La letra que se desea insertar.
     */
    private void insertar(Nodo nodo, String binario, int pos, char letra) {
        if (pos >= binario.length()) {
            // Ya se han consumido todos los bits; se almacena la letra en este nodo.
            nodo.letra = letra;
            return;
        }
        
        // Determinar el tamaño del grupo: si quedan 2 o más bits se usa grupo de 2, sino grupo de 1.
        int restantes = binario.length() - pos;
        int tamGrupo = restantes >= 2 ? 2 : 1;
        String grupo = binario.substring(pos, pos + tamGrupo);
        
        // Si no existe el nodo hijo para este grupo, se crea.
        if (!nodo.hijos.containsKey(grupo)) {
            nodo.hijos.put(grupo, new Nodo());
        }
        
        // Se continúa la inserción en el nodo hijo correspondiente, incrementando pos en tamGrupo.
        insertar(nodo.hijos.get(grupo), binario, pos + tamGrupo, letra);
    }

    /**
     * Recorre el árbol y muestra por consola la ruta (la concatenación de grupos) y
     * la letra almacenada en cada nodo terminal.
     */
    public void imprimirArbol() {
        System.out.println("Recorrido del Árbol de Búsqueda por Residuos Múltiples:");
        imprimirRecursivo(raiz, "");
    }

    /**
     * Método recursivo para imprimir el árbol.
     *
     * @param nodo Nodo actual.
     * @param ruta Acumulación de la ruta (grupos de bits) desde la raíz.
     */
    private void imprimirRecursivo(Nodo nodo, String ruta) {
        if (nodo != null) {
            if (nodo.letra != '\0') {
                System.out.println("Ruta: " + (ruta.isEmpty() ? "Raíz" : ruta) + " => Letra: " + nodo.letra);
            }
            // Recorrer cada hijo: la llave representa el grupo de bits que se usó para llegar a él.
            for (Map.Entry<String, Nodo> entrada : nodo.hijos.entrySet()) {
                imprimirRecursivo(entrada.getValue(), ruta + entrada.getKey());
            }
        }
    }

    // Método main para pruebas.
    public static void main(String[] args) {
        BusquedaResiduosMultiples arbol = new BusquedaResiduosMultiples();

        // Ejemplo: inserción de la clave "PRUEBA"
        // Para la letra P ("10000"):
        //   - Primer grupo: "10" -> se dirige al hijo con clave "10"
        //   - Segundo grupo: "00" -> en ese nodo se crean 4 hijos y se va al hijo "00"
        //   - Último grupo: "0" -> en el nodo actual se crean 2 hijos y se selecciona el hijo "0"
        String clave = "VACIO";
        arbol.insertarClave(clave);
        arbol.imprimirArbol();
    }
}
