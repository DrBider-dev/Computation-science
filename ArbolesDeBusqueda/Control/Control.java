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
    private BusquedaResiduos bResiduos;
    private BusquedaArbolDigital bDigital;
    private TriesBusqueda bTries;
    private BusquedaResiduosMultiples bMultiples;
    private int arraySize = -1;

    public Control() {
        this.vista = new Ventana();
        this.bResiduos = new BusquedaResiduos();
        this.bDigital = new BusquedaArbolDigital();
        this.bTries = new TriesBusqueda();
        this.bMultiples = new BusquedaResiduosMultiples();

        //Radio buttons
        this.vista.rbtnResiduos.addActionListener(this);
        this.vista.rbtnDigitales.addActionListener(this);
        this.vista.rbtnTries.addActionListener(this);
        this.vista.rbtnMultiples.addActionListener(this);
        
        //Botones de cambio de elementos
        this.vista.jCambiador.addActionListener(this);

        //Botones de acciones
        this.vista.btnDinamico.addActionListener(this);
        this.vista.btnMostrar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);

        //Agregando ActionCommand a los Radio buttons
        this.vista.rbtnResiduos.setActionCommand("residuos");
        this.vista.rbtnDigitales.setActionCommand("digitales");
        this.vista.rbtnTries.setActionCommand("tries");
        this.vista.rbtnMultiples.setActionCommand("multiples");

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
        
        if (this.vista.rbtnResiduos.isSelected()) {
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {

                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A AGREGAR");
                } else {
                    this.bResiduos.insertarClave(elementoText);
                    this.vista.mostrarMensajes("CLAVE INSERTADA CORRECTAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A BUSCAR");
                } else {
                    String busqueda = this.bResiduos.buscarLetras(elementoText);
                    this.vista.mostrarMensajes(busqueda);
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A ELIMINAR");
                } else {
                    boolean eliminado = this.bResiduos.eliminarClave(elementoText);
                    if (eliminado) {
                        this.vista.mostrarMensajes("ELIMINADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO, NO SE ELIMINO NADA");
                    }
                    
                }
            }
            
        } else if (this.vista.rbtnDigitales.isSelected()) {
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {

                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A AGREGAR");
                } else {
                    this.bDigital.insertarClave(elementoText);
                    this.vista.mostrarMensajes("CLAVE INSERTADA CORRECTAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A BUSCAR");
                } else {
                    String busqueda = this.bDigital.buscarClave(elementoText);
                    this.vista.mostrarMensajes(busqueda);
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A ELIMINAR");
                } else {
                    boolean eliminado = this.bDigital.eliminarClave(elementoText);
                    if (eliminado) {
                        this.vista.mostrarMensajes("ELIMINADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO, NO SE ELIMINO NADA");
                    }
                    
                }
            }
            
        } else if (this.vista.rbtnTries.isSelected()) {
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {

                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A AGREGAR");
                } else {
                    this.bTries.insertarClave(elementoText);
                    this.vista.mostrarMensajes("CLAVE INSERTADA CORRECTAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A BUSCAR");
                } else {
                    String busqueda = this.bTries.buscarClave(elementoText);
                    this.vista.mostrarMensajes(busqueda);
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A ELIMINAR");
                } else {
                    boolean eliminado = this.bTries.eliminarClave(elementoText);
                    if (eliminado) {
                        this.vista.mostrarMensajes("ELIMINADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO, NO SE ELIMINO NADA");
                    }
                    
                }
            }

        } else if (this.vista.rbtnMultiples.isSelected()) {
            
            if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Agregar")) {

                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A AGREGAR");
                } else {
                    this.bMultiples.insertarClave(elementoText);
                    this.vista.mostrarMensajes("CLAVE INSERTADA CORRECTAMENTE");
                }

            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Buscar")) {
                
                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A BUSCAR");
                } else {
                    String busqueda = this.bMultiples.buscarClave(elementoText);
                    this.vista.mostrarMensajes(busqueda);
                }
                
            } else if (String.valueOf(this.vista.jCambiador.getSelectedItem()).equals("Eliminar")) {
                
                if (elementoText.isEmpty()) {
                    this.vista.mostrarMensajes("INGRESE LA CLAVE A ELIMINAR");
                } else {
                    boolean eliminado = this.bMultiples.eliminarClave(elementoText);
                    if (eliminado) {
                        this.vista.mostrarMensajes("ELIMINADO CORRECTAMENTE");
                    } else {
                        this.vista.mostrarMensajes("ELEMENTO NO ENCONTRADO, NO SE ELIMINO NADA");
                    }
                    
                }
            }

        } else {
            this.vista.mostrarMensajes("POR FAVOR, SELECCIONE UN TIPO DE BUSQUEDA");
        }

    }
    
    public void mostrarArbol() {
        if (this.vista.rbtnResiduos.isSelected()) {
            this.vista.mostrarMensajes("MOSTRANDO ARBOL RESIDUOS");
        } else if (this.vista.rbtnDigitales.isSelected()) {
            this.vista.mostrarMensajes("MOSTRANDO ARBOL DIGITAL");
        } else if (this.vista.rbtnTries.isSelected()) {
            this.vista.mostrarMensajes("MOSTRANDO ARBOL DIGITAL");
        } else if (this.vista.rbtnMultiples.isSelected()) {
            this.vista.mostrarMensajes("MOSTRANDO ARBOL POR RESIDUOS MULTIPLES");
        } else {
            this.vista.mostrarMensajes("SELECCIONE UNA OPCION");
        }
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
