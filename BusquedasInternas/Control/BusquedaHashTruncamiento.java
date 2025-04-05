/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

/**
 *
 * @author brayan
 */
public class BusquedaHashTruncamiento {
    
    private int[] arrayTruncamiento;

    public BusquedaHashTruncamiento() {
    }
    
    private int obtenerIndice(int clave) {
        String claveText = Integer.toString(clave);
        int groupSize = String.valueOf(arrayTruncamiento.length).length() - 1;
                    
        StringBuilder hashBuilder = new StringBuilder();

        int numberOfGroups = (claveText.length() + groupSize - 1) / groupSize;

        for (int grupo = 0; grupo < numberOfGroups; grupo++) {
            // Calcular la posición del carácter a tomar en cada grupo
            int posicion = grupo * groupSize + (groupSize - 1);

            // Ajustar si la posición excede la longitud del texto
            if (posicion >= claveText.length()) {
                posicion = claveText.length() - 1;
            }

            hashBuilder.append(claveText.charAt(posicion));
        }

        int index = (Integer.parseInt(hashBuilder.toString()) + 1);
        
        return index;
    }
    
    public boolean agregarTruncamiento(int clave) {
        boolean agregado = false;
        int index = obtenerIndice(clave);
        if (arrayTruncamiento[index] == -1) {
            arrayTruncamiento[index] = clave;
            agregado = true;
        }
        return agregado;
    }
    
    public int buscarTruncamiento (int clave) {
        int index = obtenerIndice(clave);
        if (arrayTruncamiento[index] == clave) {
            return index;
        }
        return -1;
    }
    
    public boolean eliminarTruncamiento (int clave) {
        boolean eliminado = false;
        int index = obtenerIndice(clave);
        if (arrayTruncamiento[index] == clave) {
            arrayTruncamiento[index] = -1;
            eliminado = true;
        }
        return eliminado;
    }

    public int[] getArrayTruncamiento() {
        return arrayTruncamiento;
    }

    public void setArrayTruncamiento(int arraySize) {
        this.arrayTruncamiento = new int[arraySize];
        
        for (int i = 0; i < arraySize; i++) {
            arrayTruncamiento[i] = -1;
        }
    }
    
    
    
}
