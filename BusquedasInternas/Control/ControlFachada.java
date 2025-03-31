/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Vista.Ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Brayan
 */
public class ControlFachada implements ActionListener {

    private Ventana vista;
    private int[] arrayMod = null;
    private int[] arrayCuadrado = null;
    private int[] arrayTruncamiento = null;
    private int[] arrayPlegamiento = null;
    private int[] arraySecuencial = null;
    private int[] arrayBinario = null;
    private int arraySize = -1;

    public ControlFachada() {
        this.vista = new Ventana();

        //Radio buttons
        this.vista.rbtnSecuencial.addActionListener(this);
        this.vista.rbtnBinaria.addActionListener(this);
        this.vista.rbtnHashMod.addActionListener(this);
        this.vista.rbtnCuadrado.addActionListener(this);
        this.vista.rbtnPlegamiento.addActionListener(this);
        this.vista.rbtnTruncamiento.addActionListener(this);
        //Botones de cambio de elementos
        this.vista.jCambiador.addActionListener(this);

        //Botones de acciones
        this.vista.btnDinamico.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);

        //Agregando ActionCommand a los Radio buttons
        this.vista.rbtnSecuencial.setActionCommand("secuencial");
        this.vista.rbtnBinaria.setActionCommand("binario");
        this.vista.rbtnHashMod.setActionCommand("hashMod");
        this.vista.rbtnCuadrado.setActionCommand("cuadrado");
        this.vista.rbtnPlegamiento.setActionCommand("plegamiento");
        this.vista.rbtnTruncamiento.setActionCommand("truncamiento");

        //Agregando ActionCommand a los Botones de Acciones
        this.vista.btnLimpiar.setActionCommand("limpiar");
        this.vista.btnSalir.setActionCommand("salir");
        this.vista.btnDinamico.setActionCommand("dinamico");

        //Agregar un key Listener (Evitar que el usuario ingrese cadenas de texto en ubicaciones no deseadas)
        this.vista.cajaArray.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();

                if (((caracter < '0') || caracter > '9')) {
                    e.consume();
                }
            }

        });

        this.vista.cajaElemento.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();

                if (((caracter < '0') || caracter > '9')) {
                    e.consume();
                }
            }

        });

        //Agregar un keylistener
        this.iniciar();
    }

    //Metodo de inicializador de las características de la ventana
    public void iniciar() {
        this.vista.setTitle("BUSQUEDAS INTERNAS");
        this.vista.setLocationRelativeTo(null);
        this.vista.setSize(1280, 720);
        this.vista.setResizable(false);
        this.vista.setVisible(true);

    }

    //Metodo para Mostrar los Elementos Graficos
    
//    public void mostrarElementos() {
//
//        this.vista.textIdentificacion.setText("Tamaño del Array:");
//
//        if (this.vista.rbtnReservista.isSelected() || this.vista.rbtnReclutamiento.isSelected()) {
//
//            this.vista.textFecha.setVisible(false);
//            this.vista.cajaFecha.setVisible(false);
//
//        } else if (this.vista.rbtnRemiso.isSelected() || this.vista.rbtnMenor.isSelected()) {
//            this.vista.textFecha.setVisible(false);
//            this.vista.cajaFecha.setVisible(false);
//            this.vista.textLibreta.setVisible(false);
//            this.vista.cajaLibreta.setVisible(false);
//
//        } else if (this.vista.rbtnAplazado.isSelected()) {
//
//            this.vista.textLibreta.setVisible(false);
//            this.vista.cajaLibreta.setVisible(false);
//
//        }
//    }

    //Metodo que Guarda las personas :)
    public void guardarPersonas() {
        
        int elemento;
        String elementoText = this.vista.cajaElemento.getText();
        if (elementoText.isEmpty()) {
            elemento = -1;
        } else {
            elemento = Integer.parseInt(elementoText);
        }
        
        if (this.vista.rbtnSecuencial.isSelected()) {
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

                if (this.vista.cajaArray.getText().isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE EL TAMAÑO DEL ARRAY");
                } else {
                    arraySize = Integer.parseInt(this.vista.cajaArray.getText());
                    arraySecuencial = new int[arraySize];
                    
                    for (int i = 0; i < arraySize; i++) {
                        arraySecuencial[i] = -1;
                    }
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (arraySecuencial == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    for (int i = 0; i < arraySize; i++) {
                        if (arraySecuencial[i] == -1) {
                            arraySecuencial[i] = elemento;
                            this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                            break;
                        }
                    }
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (arraySecuencial == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    List<Integer> indices = new ArrayList<>();
                    for (int i = 0; i < arraySize; i++) {
                        
                        if (arraySecuencial[i] == elemento) {
                            indices.add(i);
                        }
                    }
                    
                    if (indices.isEmpty()) {
                        this.vista.mostrarMensajes("EL ELEMENTO NO SE ENCUENTRA EN EL ARREGLO");
                    } else {
                        StringBuilder ocurrenciasBuilder = new StringBuilder();
                        for (int i = 0; i < indices.size(); i++) {
                            ocurrenciasBuilder.append(indices.get(i).toString()).append(", ");
                        }
                        
                        ocurrenciasBuilder.setLength(ocurrenciasBuilder.length() - 2);
                        String ocurrencias = ocurrenciasBuilder.toString();
                        this.vista.mostrarMensajes("El elemento se encuentra en las posiciones: " + ocurrencias);
                        this.vista.cajaPosicion.setText(ocurrencias);
                    }
                    
                    
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (arraySecuencial == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean eliminado = false;

                    for (int i = 0; i < arraySize; i++) {                         
                        if (arraySecuencial[i] == elemento) {                             
                            arraySecuencial[i] = -1;                             
                            eliminado = true; 
                        }                     
                    }  

                    if (eliminado) {
                        this.vista.mostrarMensajes("ELEMENTO ELIMINADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO, NO SE ELIMINO NADA");
                    }
                    
                }
            }
            
        } else if (this.vista.rbtnBinaria.isSelected()) {
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

                if (this.vista.cajaArray.getText().isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE EL TAMAÑO DEL ARRAY");
                } else {
                    arraySize = Integer.parseInt(this.vista.cajaArray.getText());
                    arrayBinario = new int[arraySize];
                    
                    for (int i = 0; i < arraySize; i++) {
                        arrayBinario[i] = -1;
                    }
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (arrayBinario == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    for (int i = 0; i < arraySize; i++) {
                        if (arrayBinario[i] == -1) {
                            arrayBinario[i] = elemento;
                            this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                            break;
                        }
                    }
                    Arrays.sort(arrayBinario);
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (arrayBinario == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    List<Integer> indices = new ArrayList<>();
                    Arrays.sort(arrayBinario);  // Ordenamos el array primero

                    int low = 0;
                    int high = arrayBinario.length - 1;
                    int pos = -1;

                    // Búsqueda binaria estándar
                    while (low <= high) {
                        int mid = low + (high - low) / 2;
                        if (arrayBinario[mid] == elemento) {
                            pos = mid;
                            break;  // Salimos del bucle al encontrar una ocurrencia
                        } else if (arrayBinario[mid] < elemento) {
                            low = mid + 1;
                        } else {
                            high = mid - 1;
                        }
                    }

                    // Si no se encontró el elemento
                    if (pos == -1) {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO");
                    } else {
                        // Buscamos todas las ocurrencias hacia la izquierda y derecha
                        indices.add(pos + 1);  // Convertimos a índice 1-based

                        // Exploración hacia la izquierda
                        int i = pos - 1;
                        while (i >= 0 && arrayBinario[i] == elemento) {
                            indices.add(i + 1);  // Índice 1-based
                            i--;
                        }

                        // Exploración hacia la derecha
                        i = pos + 1;
                        while (i < arrayBinario.length && arrayBinario[i] == elemento) {
                            indices.add(i + 1);  // Índice 1-based
                            i++;
                        }

                        Collections.sort(indices);  // Ordenamos los índices
                        
                        StringBuilder ocurrenciasBuilder = new StringBuilder();
                        for (int j = 0; j < indices.size(); j++) {
                            ocurrenciasBuilder.append(indices.get(j)).append(", ");
                        }

                        ocurrenciasBuilder.setLength(ocurrenciasBuilder.length() - 2);
                        String ocurrencias = ocurrenciasBuilder.toString();
                        this.vista.mostrarMensajes("El elemento se encuentra en las posiciones: " + ocurrencias);
                        this.vista.cajaPosicion.setText(ocurrencias);
                    
                    }

                    
                    
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (arrayMod == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean eliminado = false;

                    for (int i = 0; i < arraySize; i++) {                         
                        if (arrayBinario[i] == elemento) {                             
                            arrayBinario[i] = -1;                             
                            eliminado = true; 
                        }                     
                    }  

                    if (eliminado) {
                        this.vista.mostrarMensajes("ELEMENTO ELIMINADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO, NO SE ELIMINO NADA");
                    }
                    
                }
            }
            
        } else if (this.vista.rbtnHashMod.isSelected()) {
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

                if (this.vista.cajaArray.getText().isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE EL TAMAÑO DEL ARRAY");
                } else {
                    arraySize = Integer.parseInt(this.vista.cajaArray.getText());
                    arrayMod = new int[arraySize];
                    
                    for (int i = 0; i < arraySize; i++) {
                        arrayMod[i] = -1;
                    }
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (arrayMod == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    int index = (elemento % arraySize) + 1;
                    
                    if (arrayMod[index] != -1) {
                        
                        this.vista.mostrarMensajes("LA POSICION YA ESTA OCUPADA");
                    } else {
                        arrayMod[index] = elemento;
                        this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                    }
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (arrayMod == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    int index = (elemento % arraySize) + 1;
                    if (arrayMod[index] == -1) {
                        this.vista.mostrarMensajes("NO SE ENCONTRO EL ELEMENTO");
                    } else {
                        String indexText = Integer.toString(index);
                        this.vista.cajaPosicion.setText(indexText);
                        this.vista.mostrarMensajes("Elemento encontrado en la posicion: " + indexText);
                    }
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (arrayMod == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    int index = (elemento % arraySize) + 1;
                    if (arrayMod[index] == -1) {
                        this.vista.mostrarMensajes("EL ELEMENTO NO EXISTE; NO SE ELIMINO NADA");
                    } else {
                        arrayMod[index] = -1;
                        this.vista.mostrarMensajes("ELEMENTO ELIMINADO CORRECTAMENTE");
                    }
                    
                }
            }

        } else if (this.vista.rbtnCuadrado.isSelected()) {
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

                if (this.vista.cajaArray.getText().isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE EL TAMAÑO DEL ARRAY");
                } else {
                    arraySize = Integer.parseInt(this.vista.cajaArray.getText());
                    arrayCuadrado = new int[arraySize];
                    
                    for (int i = 0; i < arraySize; i++) {
                        arrayCuadrado[i] = -1;
                    }
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (arrayCuadrado == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    long elementoCuadrado =(long) elemento * elemento;
                    String cuadradoString = String.valueOf(elementoCuadrado);
                    
                    int numDigitosRequeridos = String.valueOf(arraySize).length() - 1;
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
                    if (arrayCuadrado[index] != -1) {
                        this.vista.mostrarMensajes("LA POSICION YA ESTA OCUPADA");
                    } else {
                        arrayCuadrado[index] = elemento;
                        this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                    }
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (arrayCuadrado == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    long elementoCuadrado =(long) elemento * elemento;
                    String cuadradoString = String.valueOf(elementoCuadrado);
                    
                    int numDigitosRequeridos = String.valueOf(arraySize).length() - 1;
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
                    if (arrayCuadrado[index] == -1) {
                        this.vista.mostrarMensajes("NO SE ENCONTRO EL ELEMENTO");
                    } else {
                        String indexText = Integer.toString(index);
                        this.vista.cajaPosicion.setText(indexText);
                        this.vista.mostrarMensajes("Elemento encontrado en la posicion: " + indexText);
                    }
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (arrayCuadrado == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    long elementoCuadrado =(long) elemento * elemento;
                    String cuadradoString = String.valueOf(elementoCuadrado);
                    
                    int numDigitosRequeridos = String.valueOf(arraySize).length() - 1;
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
                    if (arrayCuadrado[index] == -1) {
                        this.vista.mostrarMensajes("EL ELEMENTO NO EXISTE; NO SE ELIMINO NADA");
                    } else {
                        arrayCuadrado[index] = -1;
                        this.vista.mostrarMensajes("ELEMENTO ELIMINADO CORRECTAMENTE");
                    }
                    
                }
            }

        } else if (this.vista.rbtnTruncamiento.isSelected()) {
            // FALTA IMPLEMENTAR BIEN EL METODO TRUNCAMIENTO
            
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

                if (this.vista.cajaArray.getText().isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE EL TAMAÑO DEL ARRAY");
                } else {
                    arraySize = Integer.parseInt(this.vista.cajaArray.getText());
                    arrayTruncamiento = new int[arraySize];
                    
                    for (int i = 0; i < arraySize; i++) {
                        arrayTruncamiento[i] = -1;
                    }
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (arrayTruncamiento == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    int groupSize = String.valueOf(arraySize).length() - 1;
                    
                    StringBuilder hashBuilder = new StringBuilder();
                    
                    int numberOfGroups = (elementoText.length() + groupSize - 1) / groupSize;

                    for (int grupo = 0; grupo < numberOfGroups; grupo++) {
                        // Calcular la posición del carácter a tomar en cada grupo
                        int posicion = grupo * groupSize + (groupSize - 1);

                        // Ajustar si la posición excede la longitud del texto
                        if (posicion >= elementoText.length()) {
                            posicion = elementoText.length() - 1;
                        }

                        hashBuilder.append(elementoText.charAt(posicion));
                    }
                    
                    int index = (Integer.parseInt(hashBuilder.toString()) + 1);
                    
                    if (arrayTruncamiento[index] != -1) {
                        this.vista.mostrarMensajes("LA POSICION YA ESTA OCUPADA");
                    } else {
                        arrayTruncamiento[index] = elemento;
                        this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                    }
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (arrayTruncamiento == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    int groupSize = String.valueOf(arraySize).length() - 1;
                    
                    StringBuilder hashBuilder = new StringBuilder();
                    
                    int numberOfGroups = (elementoText.length() + groupSize - 1) / groupSize;

                    for (int grupo = 0; grupo < numberOfGroups; grupo++) {
                        // Calcular la posición del carácter a tomar en cada grupo
                        int posicion = grupo * groupSize + (groupSize - 1);

                        // Ajustar si la posición excede la longitud del texto
                        if (posicion >= elementoText.length()) {
                            posicion = elementoText.length() - 1;
                        }

                        hashBuilder.append(elementoText.charAt(posicion));
                    }
                    
                    int index = (Integer.parseInt(hashBuilder.toString()) + 1);
                    
                    if (arrayTruncamiento[index] == -1) {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO");
                    } else {
                        String indexText = Integer.toString(index);
                        this.vista.cajaPosicion.setText(indexText);
                        this.vista.mostrarMensajes("Elemento encontrado en la posicion: " + indexText);
                    }
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (arrayTruncamiento == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    int groupSize = String.valueOf(arraySize).length() - 1;
                    
                    StringBuilder hashBuilder = new StringBuilder();
                    
                    int numberOfGroups = (elementoText.length() + groupSize - 1) / groupSize;

                    for (int grupo = 0; grupo < numberOfGroups; grupo++) {
                        // Calcular la posición del carácter a tomar en cada grupo
                        int posicion = grupo * groupSize + (groupSize - 1);

                        // Ajustar si la posición excede la longitud del texto
                        if (posicion >= elementoText.length()) {
                            posicion = elementoText.length() - 1;
                        }

                        hashBuilder.append(elementoText.charAt(posicion));
                    }
                    
                    int index = (Integer.parseInt(hashBuilder.toString()) + 1);
                    
                    if (arrayTruncamiento[index] == -1) {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO, NO SE ELIMINO NADA");
                    } else {
                        arrayTruncamiento[index] = -1;
                        this.vista.mostrarMensajes("ELEMENTO ELIMINADO CORRECTAMENTE");
                    }
                    
                }
            }

        } else if (this.vista.rbtnPlegamiento.isSelected()) {

            
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

                if (this.vista.cajaArray.getText().isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE EL TAMAÑO DEL ARRAY");
                } else {
                    arraySize = Integer.parseInt(this.vista.cajaArray.getText());
                    arrayPlegamiento = new int[arraySize];
                    
                    for (int i = 0; i < arraySize; i++) {
                        arrayPlegamiento[i] = -1;
                    }
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (arrayPlegamiento == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    // Calcula el tamaño del grupo en base a la cantidad de dígitos del tamaño del array menos 1.
                    int groupSize = String.valueOf(arraySize).length() - 1;
                    int suma = 0;

                    // Itera sobre la cadena tomando grupos de 'groupSize' dígitos.
                    for (int i = 0; i < elementoText.length(); i += groupSize) {
                        // Calcula el índice final del grupo. Si se pasa de la longitud, se toma hasta el final.
                        int end = i + groupSize;
                        if (end > elementoText.length()) {
                            end = elementoText.length();
                        }
                        String grupo = elementoText.substring(i, end);
                        suma += Integer.parseInt(grupo);
                    }
                    
                    int index = (suma % arraySize) + 1;
                    if (arrayPlegamiento[index] != -1) {
                        this.vista.mostrarMensajes("LA POSICION YA ESTA OCUPADA");
                    } else {
                        arrayPlegamiento[index] = elemento;
                        this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                    }
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (arrayPlegamiento == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    // Calcula el tamaño del grupo en base a la cantidad de dígitos del tamaño del array menos 1.
                    int groupSize = String.valueOf(arraySize).length() - 1;
                    int suma = 0;

                    // Itera sobre la cadena tomando grupos de 'groupSize' dígitos.
                    for (int i = 0; i < elementoText.length(); i += groupSize) {
                        // Calcula el índice final del grupo. Si se pasa de la longitud, se toma hasta el final.
                        int end = i + groupSize;
                        if (end > elementoText.length()) {
                            end = elementoText.length();
                        }
                        String grupo = elementoText.substring(i, end);
                        suma += Integer.parseInt(grupo);
                    }
                    
                    int index = (suma % arraySize) + 1;
                    if (arrayPlegamiento[index] == -1) {
                        this.vista.mostrarMensajes("NO SE ENCONTRO EL ELEMENTO");
                    } else {
                        String indexText = Integer.toString(index);
                        this.vista.cajaPosicion.setText(indexText);
                        this.vista.mostrarMensajes("Elemento encontrado en la posicion: " + indexText);
                    }
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (arrayPlegamiento == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    // Calcula el tamaño del grupo en base a la cantidad de dígitos del tamaño del array menos 1.
                    int groupSize = String.valueOf(arraySize).length() - 1;
                    int suma = 0;

                    // Itera sobre la cadena tomando grupos de 'groupSize' dígitos.
                    for (int i = 0; i < elementoText.length(); i += groupSize) {
                        // Calcula el índice final del grupo. Si se pasa de la longitud, se toma hasta el final.
                        int end = i + groupSize;
                        if (end > elementoText.length()) {
                            end = elementoText.length();
                        }
                        String grupo = elementoText.substring(i, end);
                        suma += Integer.parseInt(grupo);
                    }
                    
                    int index = (suma % arraySize) + 1;
                    if (arrayPlegamiento[index] == -1) {
                        this.vista.mostrarMensajes("EL ELEMENTO NO EXISTE; NO SE ELIMINO NADA");
                    } else {
                        arrayPlegamiento[index] = -1;
                        this.vista.mostrarMensajes("ELEMENTO ELIMINADO CORRECTAMENTE");
                    }
                    
                }
            }

        } else {
            this.vista.mostrarMensajes("POR FAVOR, SELECCIONE UN TIPO DE BUSQUEDA");
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

            this.vista.btnDinamico.setText("Crear");
            
            this.vista.cajaArray.setEditable(true);
            this.vista.cajaPosicion.setEditable(false);
            this.vista.cajaElemento.setEditable(false);

            this.vista.coloresBuscar("crear");

        } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
            
            this.vista.btnDinamico.setText("Agregar");
            
            this.vista.cajaArray.setEditable(false);
            this.vista.cajaPosicion.setEditable(false);
            this.vista.cajaElemento.setEditable(true);
            

            this.vista.coloresBuscar("agregar");

        } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
            
            this.vista.btnDinamico.setText("Buscar");
            
            this.vista.cajaArray.setEditable(false);
            this.vista.cajaPosicion.setEditable(false);
            this.vista.cajaElemento.setEditable(true);
            

            this.vista.coloresBuscar("buscar");
        } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
            
            this.vista.btnDinamico.setText("Eliminar");
            
            this.vista.cajaArray.setEditable(false);
            this.vista.cajaPosicion.setEditable(false);
            this.vista.cajaElemento.setEditable(true);
            

            this.vista.coloresBuscar("eliminar");
        }
        
        if ("secuencial".equals(e.getActionCommand())) {
            
            this.vista.mostrarImagenes("/imagenes/1.png");
        } else if ("binario".equals(e.getActionCommand())) {
            
            this.vista.mostrarImagenes("/imagenes/1.png");
        } else if ("hashMod".equals(e.getActionCommand())) {
            
            this.vista.mostrarImagenes("/imagenes/2.png");
        } else if ("cuadrado".equals(e.getActionCommand())) {
            
            this.vista.mostrarImagenes("/imagenes/3.png");
        } else if ("plegamiento".equals(e.getActionCommand())) {
            
            this.vista.mostrarImagenes("/imagenes/4.png");
        } else if ("truncamiento".equals(e.getActionCommand())) {
       
            this.vista.mostrarImagenes("/imagenes/5.png");

        } else if ("limpiar".equals(e.getActionCommand())) {
            this.vista.blanquearCampos();

        } else if ("salir".equals(e.getActionCommand())) {
            this.vista.setVisible(false);
            this.vista.dispose();

        } else if ("dinamico".equals(e.getActionCommand())) {

            guardarPersonas();
        }

    }
}