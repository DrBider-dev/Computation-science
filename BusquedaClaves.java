import java.util.Scanner;

/**
 * Ejemplo de algoritmo de búsqueda por transformación de claves
 * utilizando 4 funciones hash:
 * 1. Funcion Hash mod
 * 2. Funcion Hash Método del medio cuadrado
 * 3. Funcion Hash Método Plegamiento
 * 4. Funcion Hash Método Truncamiento
 */
public class BusquedaClaves {

    public static int[] pedirDataHash(int opcion) {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el tamaño del arreglo: ");

        while(!sc.hasNextInt()) {
            System.out.println("Por favor ingrese un número entero.");
            sc.next();
        }
        int n = sc.nextInt();
        int[] arr = new int[n];

        System.out.println("Ingrese los elementos del arreglo: (-1 para finalizar)");
        while (!sc.hasNextInt()) {
            System.out.println("Por favor ingrese un número entero.");
            sc.next();
        }
        int key = 0;
        while (true) {
            if (key == -1) {
                break;
            }
            switch (opcion) {
                case 3:
                    key = sc.nextInt();
                    int indexMod = hashMod(key, n);
                    arr[indexMod] = key;
                    break;
                
                case 4:
                    key = sc.nextInt();
                    int indexCuadrado = hashMedioCuadrado(key, n);
                    arr[indexCuadrado] = key;
                    break;
                
                case 5:
                    key = sc.nextInt();
                    int indexPlegamiento = hashPlegamiento(key, n);
                    arr[indexPlegamiento] = key;
                    break;
                
                case 6:
                    key = sc.nextInt();
                    int indexTruncamiento = hashTruncamiento(key, n);
                    arr[indexTruncamiento] = key;
                    break;
                
                default:
                    System.out.println("Opción inválida");
                    break;
            }
            System.out.println("Ingrese el siguiente elemento: (-1 para finalizar)");
            while (!sc.hasNextInt()) {
                System.out.println("Por favor ingrese un número entero.");
                sc.next();
            }
            key = sc.nextInt();
        }

        return arr;
    }

    /**
     * Función Hash Mod:
     * Se calcula el residuo de dividir la clave entre el tamaño de la tabla.
     * Ejemplo: si key = 12345 y tableSize = 100, se calcula 12345 % 100 = 45.
     */
    public static int hashMod(int key, int tableSize) {
        return (key % tableSize);
    }

    /**
     * Función Hash Método del Medio Cuadrado:
     * 1. Se eleva la clave al cuadrado.
     * 2. Se convierte el resultado a String para extraer los dígitos del medio.
     * 3. Se obtiene el valor central (dos dígitos si la longitud es par, uno si es impar).
     * 4. Se aplica la operación mod con el tamaño de la tabla.
     * Ejemplo:
     *    key = 12345
     *    12345 * 12345 = 152399025
     *    Cadena "152399025" → dígitos centrales "9"
     *    Resultado final: Integer.parseInt("9") % tableSize.
     */
    public static int hashMedioCuadrado(int key, int tableSize) {
        int square = key * key;  // Elevar la clave al cuadrado
        String squareStr = Integer.toString(square);
        int mid = squareStr.length() / 2;
        String middleDigits;
        // Se extraen 2 dígitos si es posible, o 1 dígito en caso impar
        if (squareStr.length() >= 2) {
            if (squareStr.length() % 2 == 0) {
                // Para longitud par, se toman dos dígitos centrales
                middleDigits = squareStr.substring(mid - 1, mid + 1);
            } else {
                // Para longitud impar, se toma el dígito central
                middleDigits = squareStr.substring(mid, mid + 1);
            }
        } else {
            middleDigits = squareStr;
        }
        int middleNumber = Integer.parseInt(middleDigits);
        return middleNumber % tableSize;
    }

    /**
     * Función Hash Método Plegamiento:
     * Se divide la clave (convertida a cadena) en grupos de dígitos (en este ejemplo de a 2)
     * y se suman dichos grupos. Luego se aplica el módulo para que el resultado esté
     * dentro del tamaño de la tabla.
     * Ejemplo:
     *    key = 12345 → "12", "34" y "5" → suma = 12 + 34 + 5 = 51 → hash = 51 % tableSize.
     */
    public static int hashPlegamiento(int key, int tableSize) {
        String keyStr = Integer.toString(key);
        int sum = 0;
        int groupSize = 2; // Tamaño del grupo de dígitos a sumar
        for (int i = 0; i < keyStr.length(); i += groupSize) {
            int end = Math.min(i + groupSize, keyStr.length());
            int part = Integer.parseInt(keyStr.substring(i, end));
            sum += part;
        }
        return sum % tableSize;
    }

    /**
     * Función Hash Método Truncamiento:
     * Se selecciona una parte de la clave (en este ejemplo, los dos últimos dígitos)
     * y luego se aplica el módulo con el tamaño de la tabla.
     * Ejemplo:
     *    key = 12345 → se toman "45" → hash = 45 % tableSize.
     */
    public static int hashTruncamiento(int key, int tableSize) {
        String keyStr = Integer.toString(key);
        String trunc;
        if (keyStr.length() >= 2) {
            trunc = keyStr.substring(keyStr.length() - 2);
        } else {
            trunc = keyStr;
        }
        int truncValue = Integer.parseInt(trunc);
        return truncValue % tableSize;
    }
}
