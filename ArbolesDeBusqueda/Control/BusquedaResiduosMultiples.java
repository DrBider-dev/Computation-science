package Control;
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
    
    /**
     * Busca una clave en el árbol y devuelve las rutas binarias de cada letra.
     * @param clave La clave a buscar (solo letras A-Z).
     * @return Rutas binarias en formato "Letra 'X': RUTA", o mensaje de error.
     */
    public String buscarClave(String clave) {
        clave = clave.toUpperCase();
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < clave.length(); i++) {
            char letra = clave.charAt(i);
            if (letra < 'A' || letra > 'Z') {
                resultado.append("Letra '").append(letra).append("': Inválida\n");
                continue;
            }

            String binario = convertirLetraABinario(letra);
            String ruta = buscarRuta(raiz, binario, 0, new StringBuilder());

            if (ruta == null) {
                resultado.append("Letra '").append(letra).append("': No encontrada\n");
            } else {
                resultado.append("Letra '").append(letra).append("': ").append(ruta).append("\n");
            }
        }

        return resultado.toString();
    }

    /**
     * Método auxiliar recursivo para buscar una letra.
     * @param nodo Nodo actual.
     * @param binario Binario de 5 bits de la letra.
     * @param pos Posición actual en el binario.
     * @param rutaAcumulada Ruta construida con grupos de bits.
     * @return Ruta completa como String o null si no se encuentra.
     */
    private String buscarRuta(Nodo nodo, String binario, int pos, StringBuilder rutaAcumulada) {
        if (pos >= binario.length()) {
            return (nodo.letra != '\0') ? rutaAcumulada.toString() : null;
        }

        int restantes = binario.length() - pos;
        int tamGrupo = restantes >= 2 ? 2 : 1;
        String grupo = binario.substring(pos, pos + tamGrupo);

        if (!nodo.hijos.containsKey(grupo)) {
            return null;
        }

        rutaAcumulada.append(grupo);
        return buscarRuta(nodo.hijos.get(grupo), binario, pos + tamGrupo, rutaAcumulada);
    }

    /**
     * Elimina una clave del árbol marcando los nodos terminales como vacíos.
     * @param clave La clave a eliminar (solo letras A-Z).
     * @return true si todas las letras fueron eliminadas, false en caso contrario.
     */
    public boolean eliminarClave(String clave) {
        clave = clave.toUpperCase();
        boolean eliminadoCompleto = true;

        for (int i = 0; i < clave.length(); i++) {
            char letra = clave.charAt(i);
            if (letra < 'A' || letra > 'Z') continue;

            String binario = convertirLetraABinario(letra);
            if (!eliminarLetra(raiz, binario, 0)) {
                eliminadoCompleto = false;
            }
        }

        return eliminadoCompleto;
    }

    /**
     * Método auxiliar recursivo para eliminar una letra.
     * @param nodo Nodo actual.
     * @param binario Binario de 5 bits de la letra.
     * @param pos Posición actual en el binario.
     * @return true si se marcó el nodo terminal como vacío.
     */
    private boolean eliminarLetra(Nodo nodo, String binario, int pos) {
        if (pos >= binario.length()) {
            if (nodo.letra != '\0') {
                nodo.letra = '\0';
                return true;
            }
            return false;
        }

        int restantes = binario.length() - pos;
        int tamGrupo = restantes >= 2 ? 2 : 1;
        String grupo = binario.substring(pos, pos + tamGrupo);

        if (!nodo.hijos.containsKey(grupo)) {
            return false;
        }

        boolean eliminado = eliminarLetra(nodo.hijos.get(grupo), binario, pos + tamGrupo);

        // Opcional: eliminar nodos huérfanos (solo si no afecta otras rutas)
        if (eliminado && nodo.hijos.get(grupo).hijos.isEmpty() && nodo.hijos.get(grupo).letra == '\0') {
            nodo.hijos.remove(grupo);
        }

        return eliminado;
    }
}
