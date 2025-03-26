import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class BusquedaBinaria {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el tamaño del arreglo: ");
        int len = sc.nextInt();
        int[] arr = new int[len];
        System.out.println("Ingrese los elementos del arreglo: ");
        for (int i = 0; i < len; i++) {
            arr[i] = sc.nextInt();
        }
        System.out.println("Ingrese el elemento a buscar: ");
        int x = sc.nextInt();
        sc.close();
        List<Integer> indices = busquedaBinariaTodas(arr, x);
        
        if (indices.isEmpty()) {
            System.out.println("Elemento no encontrado");
        } else {
            System.out.println("Elemento " + x + " encontrado en las posiciones: " + indices);
        }
    }

    // Método de búsqueda binaria que devuelve una ocurrencia cualquiera del elemento buscado.
    public static int busquedaBinaria(int[] arr, int x) {
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
