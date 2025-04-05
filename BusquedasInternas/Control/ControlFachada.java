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
import java.util.List;

/**
 *
 * @author Brayan
 */
public class ControlFachada implements ActionListener {

    private Ventana vista;
    private BusquedaSecuencial bSecuencial;
    private BusquedaBinaria bBinaria;
    private BusquedaHashMod bMod;
    private BusquedaHashCuadrado bCuadrado;
    private BusquedaHashTruncamiento bTruncamiento;
    private BusquedaHashPlegamiento bPlegamiento;
    private int arraySize = -1;

    public ControlFachada() {
        this.vista = new Ventana();
        this.bSecuencial = new BusquedaSecuencial();
        this.bBinaria = new BusquedaBinaria();
        this.bMod = new BusquedaHashMod();
        this.bCuadrado = new BusquedaHashCuadrado();
        this.bTruncamiento = new BusquedaHashTruncamiento();
        this.bPlegamiento = new BusquedaHashPlegamiento();

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
    
    public void mostrarElementos() {
        
        this.vista.textArray.setVisible(true);
        this.vista.cajaArray.setVisible(true);
        this.vista.cajaArray.setEnabled(true);
            
        this.vista.textElemento.setVisible(false);
        this.vista.cajaElemento.setVisible(false);
        this.vista.cajaElemento.setEnabled(false);
            

        if (this.vista.jCambiador.getSelectedItem().equals("Crear")) {
            
            this.vista.textArray.setVisible(true);
            this.vista.cajaArray.setVisible(true);
            this.vista.cajaArray.setEnabled(true);
            
            this.vista.textElemento.setVisible(false);
            this.vista.cajaElemento.setVisible(false);
            this.vista.cajaElemento.setEnabled(false);
            
            
        } else if ((this.vista.jCambiador.getSelectedItem().equals("Agregar")) || (this.vista.jCambiador.getSelectedItem().equals("Eliminar"))) {
            
            this.vista.textArray.setVisible(false);
            this.vista.cajaArray.setVisible(false);
            this.vista.cajaArray.setEnabled(false);
            
            this.vista.textElemento.setVisible(true);
            this.vista.cajaElemento.setVisible(true);
            this.vista.cajaElemento.setEnabled(true);
            
        } else if (this.vista.jCambiador.getSelectedItem().equals("Buscar")) {
           
            this.vista.textArray.setVisible(false);
            this.vista.cajaArray.setVisible(false);
            this.vista.cajaArray.setEnabled(false);
            
            this.vista.textElemento.setVisible(true);
            this.vista.cajaElemento.setVisible(true);
            this.vista.cajaElemento.setEnabled(true);
            
        }
    }
    
    public int[] crearArray(int n) {
        int[] arrayTemporal = new int[n];
        
        for (int i = 0; i < n; i++) {
            arrayTemporal[i] = -1;
        }
        
        return arrayTemporal;
    }

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
                    bSecuencial.setArraySecuencial(arraySize);
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (bSecuencial.getArraySecuencial() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    
                    boolean agregado = bSecuencial.agregarSecuencial(elemento);
                    if (agregado) {
                        this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("EL ELEMENTO NO FUE AGREGADO, EL ARRAY ESTA LLENO");
                    }
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (bSecuencial.getArraySecuencial() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    List<Integer> indices = bSecuencial.buscarSecuencial(elemento);
                    
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
                    }
                    
                    
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (bSecuencial.getArraySecuencial() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean eliminado = bSecuencial.eliminarSecuencial(elemento);

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
                    bBinaria.setArrayBinario(arraySize);
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (bBinaria.getArrarBinario() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean agregado = bBinaria.agregarBinaria(elemento);
                    if (agregado) {
                        this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("NO SE PUDO AGREGAR EL ELEMENTO");
                    }
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (bBinaria.getArrarBinario() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    List<Integer> indices = bBinaria.buscarBinaria(elemento);
                    
                    if (indices.isEmpty()) {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO");
                    } else {
                        
                        StringBuilder ocurrenciasBuilder = new StringBuilder();
                        for (int j = 0; j < indices.size(); j++) {
                            ocurrenciasBuilder.append(indices.get(j)).append(", ");
                        }

                        ocurrenciasBuilder.setLength(ocurrenciasBuilder.length() - 2);
                        String ocurrencias = ocurrenciasBuilder.toString();
                        this.vista.mostrarMensajes("El elemento se encuentra en las posiciones: " + ocurrencias);
                    
                    }
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (bBinaria.getArrarBinario() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean eliminado = bBinaria.eliminarBinaria(elemento);

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
                    bMod.setArrayMod(arraySize);
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (bMod.getArrayMod() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean agregado = bMod.agregarMod(elemento);
                    
                    if (agregado) {
                        this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("LA POSICION YA ESTA OCUPADA");
                    }
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                int index = bMod.buscarMod(elemento);
                if (index == -1) {
                    this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO");
                } else {
                    this.vista.mostrarMensajes("ELEMENTO ENCONTRADO EN LA POSICION: " + (index + 1));
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (bMod.getArrayMod() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean eliminado = bMod.eliminarMod(elemento);
                    
                    if (eliminado) {
                        this.vista.mostrarMensajes("ELEMENTO ELIMINADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO, NO SE ELIMINO NADA");
                    }
                    
                }
            }

        } else if (this.vista.rbtnCuadrado.isSelected()) {
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

                if (this.vista.cajaArray.getText().isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE EL TAMAÑO DEL ARRAY");
                } else {
                    arraySize = Integer.parseInt(this.vista.cajaArray.getText());
                    bCuadrado.setArrayCuadrado(arraySize);
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (bCuadrado.getArrayCuadrado() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean agregado = bCuadrado.agregarCuadrado(elemento);
                    if (agregado) {
                        this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("LA POSICION SE ENCUENTRA OCUPADA");
                    }
                    
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (bCuadrado.getArrayCuadrado() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    int index = bCuadrado.buscarCuadrado(elemento);
                    if (index == -1) {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO ENCONTRADO EN LA POSICION: " + (index + 1));
                    }
                    
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (bCuadrado.getArrayCuadrado() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean eliminado = bCuadrado.eliminarCuadrado(elemento);
                    if (eliminado) {
                        this.vista.mostrarMensajes("ELEMENTO ELIMINADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("NO SE ENCONTRO EL ELEMENTO, NADA SE ELIMINO");
                    }
                    
                }
            }

        } else if (this.vista.rbtnTruncamiento.isSelected()) {
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

                if (this.vista.cajaArray.getText().isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE EL TAMAÑO DEL ARRAY");
                } else {
                    arraySize = Integer.parseInt(this.vista.cajaArray.getText());
                    bTruncamiento.setArrayTruncamiento(arraySize);
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (bTruncamiento.getArrayTruncamiento() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean agregado = bTruncamiento.agregarTruncamiento(elemento);
                    if (agregado) {
                        this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("lA POSICION ESTA OCUPADA");
                    }
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (bTruncamiento.getArrayTruncamiento() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    int index = bTruncamiento.buscarTruncamiento(elemento);
                    if (index == -1) {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO ENCONTRADO EN LA POSICION: " + index);
                    }
                    
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (bTruncamiento.getArrayTruncamiento() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean eliminado = bTruncamiento.eliminarTruncamiento(elemento);
                    if (eliminado) { 
                        this.vista.mostrarMensajes("ELEMENTO ELIMINADO CORRECTAMENTE");
                    } else { 
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO, NO SE ELIMINO NADA");
                    }
                    
                }
            }

        } else if (this.vista.rbtnPlegamiento.isSelected()) {

            
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

                if (this.vista.cajaArray.getText().isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE EL TAMAÑO DEL ARRAY");
                } else {
                    arraySize = Integer.parseInt(this.vista.cajaArray.getText());
                    bPlegamiento.setArrayPlegamiento(arraySize);
                    this.vista.mostrarMensajes("ARRAY CREADO EXITOSAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO");
                } else if (bPlegamiento.getArrayPlegamiento() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean agregado = bPlegamiento.agregarPlegamiento(elemento);
                    if (agregado) {
                        this.vista.mostrarMensajes("ELEMENTO AGREGADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("LA POSICION ESTA OCUPADA");
                    }
                    
                }
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A BUSCAR");
                } else if (bPlegamiento.getArrayPlegamiento() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    int index = bPlegamiento.buscarPlegamiento(elemento);
                    if (index == -1) {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO ENCONTRADO EN LA POSICION: " + index);
                    }
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elemento == -1) {
                    this.vista.mostrarMensajes("POR FAVOR DIGITE EL ELEMENTO A ELIMINAR");
                } else if (bPlegamiento.getArrayPlegamiento() == null) {
                    this.vista.mostrarMensajes("POR FAVOR CREE EL ARREGLO");
                } else {
                    
                    boolean eliminado = bPlegamiento.eliminarPlegamiento(elemento);
                    if (eliminado) {
                        this.vista.mostrarMensajes("ELEMENTO ELIMINADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO, NO SE ELIMINO NADA");
                    }
                    
                }
            }

        } else {
            this.vista.mostrarMensajes("POR FAVOR, SELECCIONE UN TIPO DE BUSQUEDA");
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mostrarElementos();
        if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Crear")) {

            this.vista.btnDinamico.setText("Crear");
            
            this.vista.cajaArray.setEditable(true);
            this.vista.cajaElemento.setEditable(false);

        } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {
            
            this.vista.btnDinamico.setText("Agregar");
            
            this.vista.cajaArray.setEditable(false);
            this.vista.cajaElemento.setEditable(true);

        } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
            
            this.vista.btnDinamico.setText("Buscar");
            
            this.vista.cajaArray.setEditable(false);
            this.vista.cajaElemento.setEditable(true);
            
        } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
            
            this.vista.btnDinamico.setText("Eliminar");
            
            this.vista.cajaArray.setEditable(false);
            this.vista.cajaElemento.setEditable(true);
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