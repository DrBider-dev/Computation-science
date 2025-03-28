import java.util.*;

public class BusquedaLineal {

    private List<Integer> indices = new ArrayList<>();

    public BusquedaLineal() {
        this.indices = new ArrayList<>();
    }

    public void busquedaLineal(int[] arr, int x) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == x) {
                indices.add(i);
            }
        }
    }

    public List<Integer> getIndices() {
        return indices;
    }

    
}