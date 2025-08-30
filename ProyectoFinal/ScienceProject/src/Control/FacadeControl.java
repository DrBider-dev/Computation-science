package Control;

import View.PrincipalPage;

public class FacadeControl {
    

    public FacadeControl() {
        iniciarVentanas();
    }

    public void iniciarVentanas() {
        PrincipalPage principalPage = PrincipalPage.getInstance();
        principalPage.setVisible(true);
    }
}
