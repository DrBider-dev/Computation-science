/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

/**
 *
 * @author brayan
 */
public class BusquedaHashCuadrado {
    
    private int[] arrayCuadrado;

    public BusquedaHashCuadrado() {
    }
    
    private int obtenerIndice(int clave) {
        
        long elementoCuadrado =(long) clave * clave;
        String cuadradoString = String.valueOf(elementoCuadrado);

        int numDigitosRequeridos = String.valueOf(arrayCuadrado.length).length() - 1;
        String resultado;

        // Caso especial: si se requieren 2 dígitos y el cuadrado tiene cantidad impar de dígitos
        // (es decir, tiene un único dígito central) se completa tomando también el dígito a la izquierda.
        if (numDigitosRequeridos == 2 && cuadradoString.length() % 2 != 0) {
            int centro = cuadradoString.length() / 2;
            // Si el dígito central es el primero, no se puede tomar el dígito a la izquierda, así que se toma de otra forma.
            if (centro == 0) {
                resultado = cuadradoString.substring(0, 2);
            } else {
                resultado = cuadradoString.substring(centro - 1, centro + 1);
            }
        } else {
            // Caso general: extraer los dígitos centrales
            // Se calcula el índice de inicio para obtener la subcadena central.
            int inicio = (cuadradoString.length() - numDigitosRequeridos) / 2;
            // Para evitar problemas (por ejemplo, cuando el cuadrado tiene menos dígitos que numDigitosRequeridos)
            if (inicio < 0) {
                // En este caso se puede completar con ceros a la izquierda o simplemente retornar el número completo.
                // Aquí retornamos el valor completo
                resultado = cuadradoString;
            } else {
                // Tomamos la subcadena de longitud numDigitosRequeridos
                resultado = cuadradoString.substring(inicio, inicio + numDigitosRequeridos);
            }
        }
        
        int index = Integer.parseInt(resultado);
        return index;
    }

    public boolean agregarCuadrado(int clave) {
        boolean agregado = false;
        int index = obtenerIndice(clave);
        if (arrayCuadrado[index] == -1){ 
            arrayCuadrado[index] = clave;
            agregado = true;
        }
        
        return agregado;
    }
    
    public int buscarCuadrado (int clave) {
        int index = obtenerIndice(clave);
        if (arrayCuadrado[index] == clave) {
            return index;
        }
        return -1;
    }
    
    public boolean eliminarCuadrado(int clave) {
        boolean eliminado = false;
        int index = obtenerIndice(clave);
        if (arrayCuadrado[index] == clave) {
            arrayCuadrado[index] = -1;
            eliminado = true;
        }
        return eliminado;
    }
    
    public int[] getArrayCuadrado() {
        return arrayCuadrado;
    }

    public void setArrayCuadrado(int arraySize) {
        this.arrayCuadrado = new int[arraySize];
        
        for (int i = 0; i < arraySize; i++) {
            arrayCuadrado[i] = -1;
        }
    }
    
    
    
}
