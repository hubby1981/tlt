package biitworx.db.com.lib.tlt;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import biitworx.db.com.lib.tlt.data.World;

public class MainActivity extends AppCompatActivity {

    public static Resources res;
    public static World world=new World();
    public static Bitmap layer0;
    public static Bitmap layer1;
    public static Bitmap layer2;
    public static Bitmap layer3;
    public static Bitmap layer4;

    public static Typeface fairyFace;
    public static Typeface royalface;
    public static Typeface textFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res=getResources();

        fairyFace = Typeface.createFromAsset(this.getAssets(), "tribal.ttf");
        royalface = Typeface.createFromAsset(this.getAssets(), "royal.ttf");
        textFace = Typeface.createFromAsset(this.getAssets(), "text.ttf");

        layer0 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.layer0);
        layer1 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.layer1);
        layer2 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.layer2);
        layer3 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.layer3);
        layer4 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.layer4);
        world.generate();

        setContentView(R.layout.activity_main);

    }
}
