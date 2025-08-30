package Model;

public class LinealSearch {
    private int[] arreglo;
    private int tamaño;     // tamaño máximo del arreglo
    private int contador;   // cuántos elementos se han agregado realmente

    // Constructor: pide el tamaño del arreglo
    public LinealSearch(int tamaño) {
        this.tamaño = tamaño;
        this.arreglo = new int[tamaño];
        this.contador = 0;
    }

    // ------------------- CRUD -------------------

    // Create: agregar un elemento (si hay espacio)
    public boolean agregar(int valor) {
        if (contador < tamaño) {
            arreglo[contador] = valor;
            contador++;
            return true;
        }
        return false; // no hay espacio
    }

    // Read: obtener todos los elementos actuales
    public int[] obtenerElementos() {
        int[] copia = new int[contador];
        for (int i = 0; i < contador; i++) {
            copia[i] = arreglo[i];
        }
        return copia;
    }

    // Update: modificar un elemento en una posición válida
    public boolean actualizar(int posicion, int nuevoValor) {
        if (posicion >= 0 && posicion < contador) {
            arreglo[posicion] = nuevoValor;
            return true;
        }
        return false;
    }

    // Delete: eliminar un elemento y correr el resto a la izquierda
    public boolean eliminar(int posicion) {
        if (posicion >= 0 && posicion < contador) {
            for (int i = posicion; i < contador - 1; i++) {
                arreglo[i] = arreglo[i + 1];
            }
            contador--;
            return true;
        }
        return false;
    }

    // ------------------- BÚSQUEDA -------------------

    // Búsqueda lineal: devuelve la posición o -1 si no existe
    public int busquedaLineal(int valor) {
        for (int i = 0; i < contador; i++) {
            if (arreglo[i] == valor) {
                return i;
            }
        }
        return -1;
    }

    // ------------------- UTILIDADES -------------------

    public int getTamaño() {
        return tamaño;
    }

    public int getCantidad() {
        return contador;
    }

    public boolean estaLleno() {
        return contador == tamaño;
    }

    public boolean estaVacio() {
        return contador == 0;
    }
}
