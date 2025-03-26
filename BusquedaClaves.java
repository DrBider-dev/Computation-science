/**
 * Ejemplo de algoritmo de búsqueda por transformación de claves
 * utilizando 4 funciones hash:
 * 1. Funcion Hash mod
 * 2. Funcion Hash Método del medio cuadrado
 * 3. Funcion Hash Método Plegamiento
 * 4. Funcion Hash Método Truncamiento
 */
public class BusquedaClaves {

    /**
     * Función Hash Mod:
     * Se calcula el residuo de dividir la clave entre el tamaño de la tabla.
     * Ejemplo: si key = 12345 y tableSize = 100, se calcula 12345 % 100 = 45.
     */
    public static int hashMod(int key, int tableSize) {
        return key % tableSize;
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
     *    Cadena "152399025" → dígitos centrales "99" (en caso par) o "3" (si se elige otro criterio)
     *    Resultado final: Integer.parseInt("99") % tableSize.
     */
    public static int hashMiddleSquare(int key, int tableSize) {
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
    public static int hashFolding(int key, int tableSize) {
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
    public static int hashTruncation(int key, int tableSize) {
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

    public static void main(String[] args) {
        // Ejemplo de uso de las funciones hash con una clave y tamaño de tabla
        int key = 12345;
        int tableSize = 100;

        // 1. Usando la función Hash Mod
        int modHash = hashMod(key, tableSize);
        System.out.println("Hash Mod de la clave " + key + " es: " + modHash);
        System.out.println("Explicación: Se calcula clave % tamaño_tabla.\n");

        // 2. Usando la función Hash Método del Medio Cuadrado
        int middleSquareHash = hashMiddleSquare(key, tableSize);
        System.out.println("Hash Método del Medio Cuadrado de la clave " + key + " es: " + middleSquareHash);
        System.out.println("Explicación: Se eleva la clave al cuadrado, se extraen los dígitos del medio y se aplica mod con el tamaño de la tabla.\n");

        // 3. Usando la función Hash Método Plegamiento
        int foldingHash = hashFolding(key, tableSize);
        System.out.println("Hash Método Plegamiento de la clave " + key + " es: " + foldingHash);
        System.out.println("Explicación: Se divide la clave en grupos de dígitos, se suman dichos grupos y se aplica mod con el tamaño de la tabla.\n");

        // 4. Usando la función Hash Método Truncamiento
        int truncationHash = hashTruncation(key, tableSize);
        System.out.println("Hash Método Truncamiento de la clave " + key + " es: " + truncationHash);
        System.out.println("Explicación: Se toma una parte de la clave (los dos últimos dígitos) y se aplica mod con el tamaño de la tabla.\n");
    }
}
