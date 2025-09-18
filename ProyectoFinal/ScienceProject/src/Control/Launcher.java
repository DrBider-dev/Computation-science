package Control;


import View.PrincipalPage;

public class Launcher {
    public static void main(String[] args) {
        
        PrincipalPage ventana = PrincipalPage.getInstance();
        ventana.setVisible(true);
    }
}
