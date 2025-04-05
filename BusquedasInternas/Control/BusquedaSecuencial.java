/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brayan
 */
public class BusquedaSecuencial {
    
    private int[] arraySecuencial;

    public BusquedaSecuencial() {
      
    }
    
    public boolean agregarSecuencial(int clave) {
        boolean agregado = false;
        for (int i = 0; i < arraySecuencial.length; i++) {
            if (arraySecuencial[i] == -1) {
                arraySecuencial[i] = clave;
                agregado = true;
                break;
            }
        }
        
        return agregado;
    }
    
    public List<Integer> buscarSecuencial(int clave) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < arraySecuencial.length; i++) {

            if (arraySecuencial[i] == clave) {
                indices.add((i+1));
            }
        }
        
        return indices;
    }
    
    public boolean eliminarSecuencial(int clave) {
        boolean eliminado = false;
        
        for (int i = 0; i < arraySecuencial.length; i++) {
            if (arraySecuencial[i] == clave) {
                arraySecuencial[i] = -1;
                eliminado = true;
                break;
            }
        }
        return eliminado;
    }

    public int[] getArraySecuencial() {
        return arraySecuencial;
    }

    public void setArraySecuencial(int arraySize) {
        this.arraySecuencial = new int[arraySize];
        
        for (int i = 0; i < arraySize; i++) {
            arraySecuencial[i] = -1;
        }
    }
    
    
}
