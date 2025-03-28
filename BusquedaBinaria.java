import java.util.*;

public class BusquedaBinaria {

    // Método de búsqueda binaria que devuelve una ocurrencia cualquiera del elemento buscado.
    private static int busquedaBinaria(int[] arr, int x) {
        Arrays.sort(arr);
        int low = 0;
        int high = arr.length - 1;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == x) {
                return mid;
            } else if (arr[mid] < x) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    // Método para obtener todas las ocurrencias del dato en el array
    public static List<Integer> busquedaBinariaTodas(int[] arr, int x) {
        List<Integer> indices = new ArrayList<>();
        int pos = busquedaBinaria(arr, x);
        
        // Si no se encuentra el elemento, se retorna la lista vacía.
        if (pos == -1) {
            return indices;
        }
        
        // Agregamos la posición encontrada inicialmente
        indices.add(pos+1);
        
        // Buscamos hacia la izquierda de la posición encontrada
        int i = pos - 1;
        while (i >= 0 && arr[i] == x) {
            indices.add(i+1);
            i--;
        }
        
        // Buscamos hacia la derecha de la posición encontrada
        i = pos + 1;
        while (i < arr.length && arr[i] == x) {
            indices.add(i+1);
            i++;
        }
        
        // Ordenamos la lista para tener las posiciones en orden ascendente
        Collections.sort(indices);
        return indices;
    }
}
