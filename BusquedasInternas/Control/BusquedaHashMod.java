/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

/**
 *
 * @author brayan
 */
public class BusquedaHashMod {
    
    private int[] arrayMod;

    public BusquedaHashMod() {
    }

    public int[] getArrayMod() {
        return arrayMod;
    }
    
    private int obtenerIndice(int clave) {
        return (clave % arrayMod.length);
    }
    
    public boolean agregarMod(int clave) {
        boolean agregado = false;
        int index = obtenerIndice(clave);
        if (arrayMod[index] == -1){ 
            arrayMod[index] = clave;
            agregado = true;
        }
        
        return agregado;
    }
    
    public int buscarMod(int clave) {
        int index = obtenerIndice(clave);
        if (arrayMod[index] == clave) {
            return index;
        }
        return -1;
    }
    
    public boolean eliminarMod(int clave) {
        boolean eliminado = false;
        int index = obtenerIndice(clave);
        if (arrayMod[index] == clave) {
            arrayMod[index] = -1;
            eliminado = true;
        }
        return eliminado;
    }
    

    public void setArrayMod(int arraySize) {
        this.arrayMod = new int[arraySize];
        
        for (int i = 0 ; i < arraySize; i++){
            arrayMod[i] = -1;
        }
    }
    
    
    
}
