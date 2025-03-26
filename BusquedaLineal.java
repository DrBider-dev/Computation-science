import java.util.Scanner;

public class BusquedaLineal {
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
        int pos = busquedaLineal(arr, x);
        if (pos == -1) {
            System.out.println("Elemento no encontrado");
        } else {
            System.out.println("Elemento encontrado en la posición: " + (pos + 1));
        }
    }

    public static int busquedaLineal(int[] arr, int x) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == x) {
                return i;
            }
        }
        return -1;
    }
}