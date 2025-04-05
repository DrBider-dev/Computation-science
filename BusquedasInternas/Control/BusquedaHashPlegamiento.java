/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

/**
 *
 * @author brayan
 */
public class BusquedaHashPlegamiento {
    
    private int[] arrayPlegamiento;

    public BusquedaHashPlegamiento() {
    }
    
    private int obtenerIndice(int clave) {
        // Calcula el tamaño del grupo en base a la cantidad de dígitos del tamaño del array menos 1.
        String claveText = Integer.toString(clave);
        int groupSize = String.valueOf(arrayPlegamiento.length).length() - 1;
        int suma = 0;

        // Itera sobre la cadena tomando grupos de 'groupSize' dígitos.
        for (int i = 0; i < claveText.length(); i += groupSize) {
            // Calcula el índice final del grupo. Si se pasa de la longitud, se toma hasta el final.
            int end = i + groupSize;
            if (end > claveText.length()) {
                end = claveText.length();
            }
            String grupo = claveText.substring(i, end);
            suma += Integer.parseInt(grupo);
        }

        int index = (suma % arrayPlegamiento.length) + 1;
        return index;
    }
    
    public boolean agregarPlegamiento(int clave) {
        boolean agregado = false;
        int index = obtenerIndice(clave);
        if (arrayPlegamiento[index] == -1) {
            arrayPlegamiento[index] = clave;
            agregado = true;
        }
        return agregado;
    }
    
    public int buscarPlegamiento(int clave) {
        int index = obtenerIndice(clave);
        if (arrayPlegamiento[index] == clave) {
            return index;
        }
        return -1;
    }
    
    public boolean eliminarPlegamiento(int clave) {
        boolean eliminado = false;
        int index = obtenerIndice(clave);
        if (arrayPlegamiento[index] == clave) {
            arrayPlegamiento[index] = -1;
            eliminado = true;
        }
        return eliminado;
    }
    
    public int[] getArrayPlegamiento() {
        return arrayPlegamiento;
    }

    public void setArrayPlegamiento(int arraySize) {
        this.arrayPlegamiento = new int[arraySize];
        
        for (int i = 0; i < arraySize; i++) { 
            arrayPlegamiento[i] = -1;
        }
    }
    
    
    
}
