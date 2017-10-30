package biitworx.db.com.lib.tlt.data;

import java.util.ArrayList;

/**
 * Created by mweissgerber on 17.08.2017.
 */

public class World {
    private ArrayList<Layer> layers = new ArrayList<>();
    public static int dimens = 512;
    public static int worldX=0;
    public static int worldY=0;


    public void generate() {
        layers.add(new Layer(dimens,0));
        layers.add(new Layer(dimens,1));
        layers.add(new Layer(dimens,2));
        layers.add(new Layer(dimens,3));
        layers.add(new Layer(dimens,4));
        layers.get(4).get(2,2).getObjects().add(new Player());

        layers.get(4).get(4,5).getObjects().add(new Dragon());
        layers.get(4).get(2,6).getObjects().add(new Gul());

    }

    public GameObject get(int layer, int x, int y) {
        if (layers.size() <= layer) return null;
        return layers.get(layer).get(x, y);
    }
}
