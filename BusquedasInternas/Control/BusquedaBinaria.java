/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author brayan
 */
public class BusquedaBinaria {
    
    private int[] arrayBinario;

    public BusquedaBinaria() {
        
    }
    
    public boolean agregarBinaria(int clave) {
        boolean agregado = false;
        for (int i = 0; i < arrayBinario.length; i++) {
            if (arrayBinario[i] == -1) {
                arrayBinario[i] = clave;
                agregado = true;
                break;
            }
        }
        
        Arrays.sort(arrayBinario);
        
        return agregado;
    }
    
    public List<Integer> buscarBinaria(int clave) {
        List<Integer> indices = new ArrayList<>();
        Arrays.sort(arrayBinario);  // Ordenamos el array primero

        int low = 0;
        int high = arrayBinario.length - 1;
        int pos = -1;

        // Búsqueda binaria estándar
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arrayBinario[mid] == clave) {
                pos = mid;
                break;  // Salimos del bucle al encontrar una ocurrencia
            } else if (arrayBinario[mid] < clave) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        // Si se encontró el elemento
        if (pos != -1) {
            // Buscamos todas las ocurrencias hacia la izquierda y derecha
            indices.add(pos + 1);  // Convertimos a índice 1-based

            // Exploración hacia la izquierda
            int i = pos - 1;
            while (i >= 0 && arrayBinario[i] == clave) {
                indices.add(i + 1);  // Índice 1-based
                i--;
            }

            // Exploración hacia la derecha
            i = pos + 1;
            while (i < arrayBinario.length && arrayBinario[i] == clave) {
                indices.add(i + 1);  // Índice 1-based
                i++;
            }

            Collections.sort(indices);
        }
        return indices;
    }
    
    public boolean eliminarBinaria(int clave) {
        boolean eliminado = false;
        
        for (int i = 0; i < arrayBinario.length; i++) {
            if (arrayBinario[i] == clave) {
                arrayBinario[i] = -1;
                eliminado = true;
                break;
            }
        }
        return eliminado;
    }
                    

    public int[] getArrarBinario() {
        return arrayBinario;
    }

    public void setArrayBinario(int arraySize) {
        this.arrayBinario = new int[arraySize];
        
        for (int i = 0; i < arraySize; i++) {
            arrayBinario[i] = -1;
        }
    }
    
    
    
}
