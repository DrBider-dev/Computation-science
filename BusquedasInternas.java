import java.util.List;
import java.util.Scanner;

public class BusquedasInternas {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        while(true) {
            System.out.println("Por favor seleccione un tipo de búsqueda:");
            System.out.println("1. Búsqueda Lineal");
            System.out.println("2. Búsqueda Binaria");
            System.out.println("3. Busqueda por Hash (normal)");
            System.out.println("4. Busqueda por Hash (medio Cuadrado)");
            System.out.println("5. Busqueda por Hash (Metodo Plegamiento)");
            System.out.println("6. Busqueda por Hash (Metodo Truncamiento)");
            System.out.println("7. Salir");
            System.out.print("Opción: ");
            if (sc.hasNextInt()) {
                int opcion = sc.nextInt();
                if (opcion >= 1 && opcion <= 7) {
                    switch (opcion) {
                        case 1:
                            System.out.println("Búsqueda Lineal");
                            System.out.println("----------------");
                            int[] datos = pedirData();
                            System.out.println("Ingrese el número a buscar:");
                            while(!sc.hasNextInt()) {
                                System.out.println("Por favor ingrese un número entero.");
                                sc.next();
                            }
                            int x = sc.nextInt();
                            sc.nextLine();

                            BusquedaLineal bl = new BusquedaLineal();
                            bl.busquedaLineal(datos, x);
                            if (bl.getIndices().isEmpty()) {
                                System.out.println("El número no se encuentra en el arreglo.");
                            } else {
                                System.out.println("El número se encuentra en las posiciones:");
                                for (int i : bl.getIndices()) {
                                    System.out.println(i);
                                }
                            }
                            break;
                        case 2:
                            System.out.println("Búsqueda Binaria");
                            System.out.println("----------------");
                            int[] datos2 = pedirData();
                            System.out.println("Ingrese el número a buscar:");
                            while(!sc.hasNextInt()) {
                                System.out.println("Por favor ingrese un número entero.");
                                sc.next();
                            }
                            int x2 = sc.nextInt();

                            List<Integer> indices = BusquedaBinaria.busquedaBinariaTodas(datos2, x2);
                            if (indices.isEmpty()) {
                                System.out.println("El número no se encuentra en el arreglo.");
                            } else {
                                System.out.println("El número se encuentra en las posiciones:");
                                for (int i : indices) {
                                    System.out.println(i);
                                }
                            }

                            break;
                        case 3:
                            System.out.println("Busqueda por Hash (normal)");
                            System.out.println("----------------------------");
                            int[] datos3 = BusquedaClaves.pedirDataHash(3);
                            System.out.println("Ingrese el número a buscar:");
                            while(!sc.hasNextInt()) {
                                System.out.println("Por favor ingrese un número entero.");
                                sc.next();
                            }
                            int x3 = sc.nextInt();
                            int indexMod = BusquedaClaves.hashMod(x3, datos3.length);
                            if (datos3[indexMod] == x3) {
                                System.out.println("El número se encuentra en la posición " + indexMod);
                            } else {
                                System.out.println("El número no se encuentra en el arreglo.");
                            }

                            break;
                        case 4:
                            System.out.println("Busqueda por Hash (medio Cuadrado)");
                            System.out.println("-----------------------------------");
                            int[] datos4 = BusquedaClaves.pedirDataHash(4);
                            System.out.println("Ingrese el número a buscar:");
                            while(!sc.hasNextInt()) {
                                System.out.println("Por favor ingrese un número entero.");
                                sc.next();
                            }
                            int x4 = sc.nextInt();
                            int indexCuadrado = BusquedaClaves.hashMedioCuadrado(x4, datos4.length);
                            if (datos4[indexCuadrado] == x4) {
                                System.out.println("El número se encuentra en la posición " + indexCuadrado);
                            } else {
                                System.out.println("El número no se encuentra en el arreglo.");
                            }

                            break;
                        case 5:
                            System.out.println("Busqueda por Hash (Metodo Plegamiento)");
                            System.out.println("---------------------------------------");
                            int[] datos5 = BusquedaClaves.pedirDataHash(5);
                            System.out.println("Ingrese el número a buscar:");
                            while(!sc.hasNextInt()) {
                                System.out.println("Por favor ingrese un número entero.");
                                sc.next();
                            }
                            int x5 = sc.nextInt();
                            int indexPlegamiento = BusquedaClaves.hashPlegamiento(x5, datos5.length);
                            if (datos5[indexPlegamiento] == x5) {
                                System.out.println("El número se encuentra en la posición " + indexPlegamiento);
                            } else {
                                System.out.println("El número no se encuentra en el arreglo.");
                            } 

                            break;
                        case 6:
                            System.out.println("Busqueda por Hash (Metodo Truncamiento)");
                            System.out.println("----------------------------------------");
                            int[] datos6 = BusquedaClaves.pedirDataHash(6);
                            System.out.println("Ingrese el número a buscar:");
                            while(!sc.hasNextInt()) {
                                System.out.println("Por favor ingrese un número entero.");
                                sc.next();
                            }
                            int x6 = sc.nextInt();
                            int indexTruncamiento = BusquedaClaves.hashTruncamiento(x6, datos6.length);
                            if (datos6[indexTruncamiento] == x6) {
                                System.out.println("El número se encuentra en la posición " + indexTruncamiento);
                            } else {
                                System.out.println("El número no se encuentra en el arreglo.");
                            }

                            break;
                        case 7:
                            System.out.println("Saliendo...");
                            System.exit(0);
                            break;
                    }
                } else {
                    System.out.println("Opción inválida");
                    sc.next();
                }
            } else {
                System.out.println("Opción inválida");
                sc.next();
            }
        }
    }

    public static int[] pedirData() {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el tamaño del arreglo: ");

        while(!sc.hasNextInt()) {
            System.out.println("Por favor ingrese un número entero.");
            sc.next();
        }
        int n = sc.nextInt();
        int[] arr = new int[n];
        System.out.println("Ingrese los elementos del arreglo:");
        for (int i = 0; i < n; i++) {
            while(!sc.hasNextInt()) {
                System.out.println("Por favor ingrese un número entero.");
                sc.next();
            }
            arr[i] = sc.nextInt();
        }
            return arr;
    }
}
