/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Vista.Ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Brayan
 */
public class Control implements ActionListener {

    private Ventana vista;
    private ArbolHuffman arbolHuffman;

    public Control() {
        this.vista = new Ventana();
        this.arbolHuffman = new ArbolHuffman();
        
        //Botones de cambio de elementos
        this.vista.jCambiador.addActionListener(this);

        //Botones de acciones
        this.vista.btnDinamico.addActionListener(this);
        this.vista.btnMostrar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);

        //Agregando ActionCommand a los Botones de Acciones
        this.vista.btnMostrar.setActionCommand("mostrar");
        this.vista.btnSalir.setActionCommand("salir");
        this.vista.btnDinamico.setActionCommand("dinamico");

        //Agregar un keylistener
        this.iniciar();
    }

    //Metodo de inicializador de las características de la ventana
    public void iniciar() {
        this.vista.setTitle("ARBOLES DE BUSQUEDA");
        this.vista.setLocationRelativeTo(null);
        this.vista.setSize(1280, 720);
        this.vista.setResizable(false);
        this.vista.setVisible(true);

    }

    //Metodo que almacena arboles :)
    public void guardarArbol() {
        
        String elementoText = this.vista.cajaBuscador.getText();
        
        if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {

            if (elementoText.isEmpty()) {
                this.vista.mostrarMensajes("INGRESE LA CLAVE A AGREGAR");
            } else {
                this.arbolHuffman.insert(elementoText);
                this.vista.mostrarMensajes("CLAVE INSERTADA CORRECTAMENTE");
            }

        } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {

            if (elementoText.isEmpty()) {
                this.vista.mostrarMensajes("INGRESE LA CLAVE A BUSCAR");
            } else {
                String busqueda = this.arbolHuffman.search(elementoText);
                this.vista.mostrarMensajes(busqueda);
            }

        } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {

            if (elementoText.isEmpty()) {
                this.vista.mostrarMensajes("INGRESE LA CLAVE A ELIMINAR");
            } else {
                boolean eliminado = this.arbolHuffman.delete(elementoText);
                if (eliminado) {
                    this.vista.mostrarMensajes("AL MENOS UN ELEMENTO ELIMINADO CORRECTAMENTE");
                } else {
                    this.vista.mostrarMensajes("ELEMENTO O ELEMENTOS NO ENCONTRADOS, NO SE ELIMINO NADA");
                }

            }
        }

    }
    
    public void mostrarArbol() {
        this.vista.mostrarMensajes("MOSTRAR ARBOL HUFFMAN, todavia no implementado ;v");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {

            this.vista.setImages("/Assets/saveIcon.png");


        } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
            
            this.vista.setImages("/Assets/searchIcon.png");
            

        } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
            
            this.vista.setImages("/Assets/deleteIcon.png");
            
        }
        
        if ("mostrar".equals(e.getActionCommand())) {
            mostrarArbol();
            

        } else if ("salir".equals(e.getActionCommand())) {
            this.vista.setVisible(false);
            this.vista.dispose();

        } else if ("dinamico".equals(e.getActionCommand())) {

            guardarArbol();
        }

    }
}
