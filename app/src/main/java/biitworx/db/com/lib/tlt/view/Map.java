package biitworx.db.com.lib.tlt.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import biitworx.db.com.lib.tlt.MainActivity;
import biitworx.db.com.lib.tlt.R;
import biitworx.db.com.lib.tlt.data.Brick;
import biitworx.db.com.lib.tlt.data.Dragon;
import biitworx.db.com.lib.tlt.data.GameObject;
import biitworx.db.com.lib.tlt.data.Grass;
import biitworx.db.com.lib.tlt.data.GrassStair;
import biitworx.db.com.lib.tlt.data.Gul;
import biitworx.db.com.lib.tlt.data.House;
import biitworx.db.com.lib.tlt.data.Player;
import biitworx.db.com.lib.tlt.data.PositionRect;
import biitworx.db.com.lib.tlt.data.Sand;
import biitworx.db.com.lib.tlt.data.Stone;
import biitworx.db.com.lib.tlt.data.Villa;
import biitworx.db.com.lib.tlt.data.Water;
import biitworx.db.com.lib.tlt.data.Willow;


/**
 * Created by mweissgerber on 04.08.2017.
 */

public class Map extends View {
    private Rect rc;
    private Rect left;
    private Rect up;
    private Rect right;
    private Rect down;

    private Shader grad;
    private Paint back;
    private Paint text;
    private Paint upper;
    private Paint lower;
    private Paint upper1;
    private Paint lower1;
    private Bitmap stone;
    private Bitmap grass;
    private Bitmap sand;
    private Bitmap willow;
    private Bitmap leavesgreen;
    private Bitmap water;
    private Bitmap grassstairs;
    private Bitmap brick;
    private Bitmap house;
    private Bitmap villa;
    private Paint filler;
    private Paint filler2;
    private Paint borderProgress;
    private Bitmap frame1;
    private Bitmap frame2;
    private Bitmap frame3;
    private Bitmap frame4;
    private Bitmap frame5;
    private Bitmap frame6;
    private Bitmap frame7;
    private Bitmap frame8;
    private Bitmap player;
    private Bitmap dragon;
    private Bitmap gul;

    private Rect topFrame;
    private Rect bottomFrame;
    private int size = 70;
    private ArrayList<ArrayList<PositionRect>> layers = null;

    private Rect button1;
    private Rect button2;
    private Rect button3;
    private Rect button4;

    private Rect head1;
    private Rect head2;
    private Rect head3;
    private Rect head4;

    public Map(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        rc = new Rect(0, 0, 0, 0);
        back = new Paint();
        back.setStyle(Paint.Style.FILL);
        upper = new Paint();
        upper.setStyle(Paint.Style.FILL);
        upper.setColor(Color.argb(128, 200, 0, 0));

        lower = new Paint();
        lower.setStyle(Paint.Style.FILL);
        lower.setColor(Color.argb(128, 100, 0, 0));


        upper1 = new Paint();
        upper1.setStyle(Paint.Style.FILL);
        upper1.setColor(Color.argb(128, 0, 200, 0));

        lower1 = new Paint();
        lower1.setStyle(Paint.Style.FILL);
        lower1.setColor(Color.argb(128, 0, 0, 200));

        stone = BitmapFactory.decodeResource(MainActivity.res, R.drawable.stone);
        sand = BitmapFactory.decodeResource(MainActivity.res, R.drawable.sand);
        grass = BitmapFactory.decodeResource(MainActivity.res, R.drawable.grass);
        willow = BitmapFactory.decodeResource(MainActivity.res, R.drawable.willow);
        leavesgreen = BitmapFactory.decodeResource(MainActivity.res, R.drawable.leavesgreen);
        water = BitmapFactory.decodeResource(MainActivity.res, R.drawable.water);
        grassstairs = BitmapFactory.decodeResource(MainActivity.res, R.drawable.grassstairs);
        brick = BitmapFactory.decodeResource(MainActivity.res, R.drawable.brick);
        house = BitmapFactory.decodeResource(MainActivity.res, R.drawable.house);
        villa = BitmapFactory.decodeResource(MainActivity.res, R.drawable.villa);
        frame1 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.frame1);
        frame2 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.frame2);
        frame3 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.frame3);
        frame4 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.frame4);
        frame5 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.frame5);
        frame6 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.frame6);
        frame7 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.frame7);
        frame8 = BitmapFactory.decodeResource(MainActivity.res, R.drawable.frame8);
        player = BitmapFactory.decodeResource(MainActivity.res, R.drawable.char02   );
        dragon = BitmapFactory.decodeResource(MainActivity.res, R.drawable.dragon   );
        gul = BitmapFactory.decodeResource(MainActivity.res, R.drawable.gul   );
        text = new Paint();
        text.setStyle(Paint.Style.FILL_AND_STROKE);
        text.setColor(Color.BLACK);
        text.setTextSize(size);
        filler = new Paint();
        filler.setStyle(Paint.Style.FILL);
        filler.setColor(Color.argb(200, 50, 50, 175));
        filler2 = new Paint();
        filler2.setStyle(Paint.Style.STROKE);
        filler2.setStrokeWidth(4);
        filler2.setColor(Color.argb(125, 250, 250, 250));
        borderProgress = new Paint();
        borderProgress.setStyle(Paint.Style.STROKE);
        borderProgress.setStrokeWidth(4);
        borderProgress.setColor(Color.argb(250, 60, 60, 60));
    }


    public Bitmap getFromPlayer(int x,int y){
        Bitmap bit = Bitmap.createBitmap(32,35, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(bit);
        x*=32;
        y*=35;
        c.drawBitmap(player,new Rect(x,y,x+32,y+35),new Rect(0,0,32,35),null);
        return bit;
    }

    @Override
    public void onDraw(Canvas canvas) {
        rc = canvas.getClipBounds();
        up = new Rect(rc.left, rc.top, rc.right, rc.top + rc.height() / 4);
        down = new Rect(rc.left, rc.bottom - rc.height() / 4, rc.right, rc.bottom);

        left = new Rect(rc.left, up.bottom, rc.left + rc.width() / 2, down.top);
        right = new Rect(rc.right - rc.width() / 2, up.bottom, rc.right, down.top);

        if (grad == null)
            //grad = new LinearGradient(rc.left, rc.top, rc.left, rc.bottom, Color.argb(255, 170,40,40), Color.argb(255, 40, 10, 10), Shader.TileMode.CLAMP);
            grad = new RadialGradient(rc.centerX(), rc.centerY() - rc.height() / 8, rc.width() / 1.5f, Color.argb(255, 255, 80, 80), Color.argb(255, 100, 20, 20), Shader.TileMode.CLAMP);
        back.setShader(grad);
        canvas.drawRect(rc, back);

        if (layers == null) {
            layers = new ArrayList<>();
            rc = new Rect(rc.left, rc.top - size * 2, rc.right, rc.bottom - size * 3);
            layers.add(drawLayer(new Rect(rc.left, rc.top + size * 3, rc.right, rc.bottom + size * 3)));

            layers.add(drawLayer(new Rect(rc.left, rc.top + size * 2, rc.right, rc.bottom + size * 2)));
            layers.add(drawLayer(new Rect(rc.left, rc.top + size, rc.right, rc.bottom + size)));

            layers.add(drawLayer(rc));
            layers.add(drawLayer(new Rect(rc.left, rc.top - size, rc.right, rc.bottom - size)));
        }

        if (layers != null) {
            int index = 0;
            for (ArrayList<PositionRect> layer : layers) {
                drawList(canvas, layer, index);
                index++;
            }

        }


        topFrame = drawFrameRect(new Rect(up.left + 8, up.top + 8, up.right - 8, up.bottom - (int) (up.height() / 1.80f)), canvas, true, 2.5f);
        bottomFrame = drawFrameRect(new Rect(down.left + 8, down.top - up.height() / 4, down.right - 8, down.bottom - 8), canvas, true, 2.5f);

        rc = new Rect(down.right - down.width() / 3, down.bottom - down.height(), down.right, down.bottom);
        up = new Rect(rc.left, rc.top, rc.right, rc.top + rc.height() / 3);
        down = new Rect(rc.left, rc.bottom - rc.height() / 3, rc.right, rc.bottom);

        left = new Rect(rc.left, up.bottom, rc.left + rc.width() / 2, down.top);
        right = new Rect(rc.right - rc.width() / 2, up.bottom, rc.right, down.top);
/*
        canvas.drawRect(up, upper);
        canvas.drawRect(down, lower);
        canvas.drawRect(left, upper1);
        canvas.drawRect(right, lower1);

*/
        drawTextBack(canvas, "d", bottomFrame);
        Paint ttf = new Paint();
        ttf.setStyle(Paint.Style.FILL);
        ttf.setColor(Color.argb(200, 200, 20, 20));
        ttf.setTypeface(MainActivity.royalface);
        ttf.setTextSize(67);
        canvas.drawText("M", topFrame.left + 25, topFrame.top + 75, ttf);


        ttf.setTextSize(57);

        canvas.drawText("W", bottomFrame.left + 25, bottomFrame.top + 75, ttf);


        Paint ttf2 = new Paint();
        ttf2.setStyle(Paint.Style.FILL);
        ttf2.setColor(Color.argb(200, 20, 20, 20));
        ttf2.setTypeface(MainActivity.textFace);
        ttf2.setTextSize(26);
        ttf2.setFakeBoldText(true);

        canvas.drawText("orus City", topFrame.left + 80, topFrame.top + 70, ttf2);
        canvas.drawText("Civ. 342", topFrame.left + 30, topFrame.top + 120, ttf2);
        canvas.drawText("Nothing to see", topFrame.left + 30, topFrame.top + 165, ttf2);

        canvas.drawText("anderer i greet you.", bottomFrame.left + 80, bottomFrame.top + 70, ttf2);
        canvas.drawText("Welcome to the World of Morus.", bottomFrame.left + 30, bottomFrame.top + 120, ttf2);
        canvas.drawText("Listen i will tell you a tale.", bottomFrame.left + 30, bottomFrame.top + 170, ttf2);
        canvas.drawText("7Listen i will tell you a tale.", bottomFrame.left + 30, bottomFrame.top + 220, ttf2);
        canvas.drawText("6Listen i will tell you a tale.", bottomFrame.left + 30, bottomFrame.top + 270, ttf2);
        canvas.drawText("5Listen i will tell you a tale.", bottomFrame.left + 30, bottomFrame.top + 320, ttf2);
        canvas.drawText("4Listen i will tell you a tale.", bottomFrame.left + 30, bottomFrame.top + 370, ttf2);
        canvas.drawText("3Listen i will tell you a tale.", bottomFrame.left + 30, bottomFrame.top + 420, ttf2);

        canvas.drawText("This is the end and should show it.", bottomFrame.left + 30, bottomFrame.top + 470, ttf2);

        button1 = new Rect(bottomFrame.right - (int) (bottomFrame.width() / 3.5f), bottomFrame.top + bottomFrame.height() / 10, bottomFrame.right - bottomFrame.width() / 40, bottomFrame.top + (bottomFrame.height() / 10) * 3);
        button1.top -= button1.height() / 4;
        button1.bottom -= button1.height() / 6;


        button2 = new Rect(button1.left, button1.bottom + button1.height() / 7, button1.right, button1.bottom + button1.height() / 7 + button1.height());
        button3 = new Rect(button2.left, button2.bottom + button2.height() / 7, button2.right, button2.bottom + button2.height() / 7 + button2.height());
        button4 = new Rect(button3.left, button3.bottom + button3.height() / 7, button3.right, button3.bottom + button3.height() / 7 + button3.height());


        drawFrameRect(button1, canvas, false, 0);
        drawFrameRect(button2, canvas, false, 0);
        drawFrameRect(button3, canvas, false, 0);
        drawFrameRect(button4, canvas, false, 0);


        canvas.drawText("I", button1.left + 25, button1.top + 75, ttf);
        canvas.drawText("nventory", button1.left + 65, button1.top + 75, ttf2);
        canvas.drawText("S", button2.left + 25, button2.top + 75, ttf);
        canvas.drawText("pellbook", button2.left + 75, button2.top + 75, ttf2);
        canvas.drawText("M", button3.left + 25, button3.top + 75, ttf);
        canvas.drawText("ovement", button3.left + 75, button3.top + 75, ttf2);
        canvas.drawText("Q", button4.left + 25, button4.top + 75, ttf);
        canvas.drawText("uit Game", button4.left + 75, button4.top + 75, ttf2);


        head1 = new Rect(topFrame.right - (int) (topFrame.width() / 3.5f), topFrame.top + topFrame.height() / 8, topFrame.right - topFrame.width() / 40, topFrame.top + (int) (topFrame.height() / 9.25f) * 3);
        head2 = new Rect(head1.left, head1.bottom, head1.right, head1.bottom + head1.height());
        head3 = new Rect(head2.left, head2.bottom, head2.right, head2.bottom + head2.height());
        head4 = new Rect(head3.left, head3.bottom, head3.right, head3.bottom + head3.height());

        ttf2.setTextSize(24);


        int ac = 150;
        int ad = 30;
        head1 = drawTextAndPercentHead(canvas, head1, "Mana", 75, Color.argb(ac+ad, ad,ad,ac), ttf2);
        head2 = drawTextAndPercentHead(canvas, head2, "Health", 100, Color.argb(ac+ad,ac,ad,ad), ttf2);
        head3 = drawTextAndPercentHead(canvas, head3, "Power", 50, Color.argb(ac+ad, ad,ac,ad), ttf2);
        head4 = drawTextAndPercentHead(canvas, head4, "Exp.", 25, Color.argb(ac+ad, ac,ad,ac), ttf2);


    }

    private Rect drawTextAndPercentHead(Canvas canvas, Rect head, String text, int percent, int color, Paint drawText) {
        int round = 2;
        canvas.drawText(text, head.left, head.top + 24, drawText);

        head = new Rect(head.left + head.width() / 2, head.top + head.height() / 20, head.right - head.width() / 30, head.top + (int) (head.height() / 1.5f));

        Path p1 = new Path();
        p1.addRoundRect(head.left, head.top, head.right, head.bottom, round, round, Path.Direction.CCW);
        p1.close();
        Path p11 = new Path();
        float cw = (head.right-head.left);
cw=cw/100f;
        p11.addRoundRect(head.left, head.top, head.left +(cw*percent), head.bottom, round, round, Path.Direction.CCW);
        p11.close();

        Path p111 = new Path();
        p111.moveTo(head.left + 2, head.bottom - 2);
        p111.lineTo(head.left + 2, head.top + 2);
        p111.lineTo(head.right - 2, head.top + 2);
        p111.lineTo(head.right - 2, head.bottom - 2);
        filler.setColor(color);

        canvas.drawPath(p11, filler);
        canvas.drawPath(p111, filler2);
        canvas.drawPath(p1, borderProgress);
        return head;
    }

    private void drawTextBack(Canvas canvas, String text, Rect rc) {
        Paint ttf = new Paint();
        ttf.setStyle(Paint.Style.FILL);
        ttf.setColor(Color.argb(25, 100, 100, 100));
        ttf.setTypeface(MainActivity.fairyFace);
        ttf.setTextSize(rc.height() * 1.2f);

        canvas.drawText(text, bottomFrame.centerX() - rc.height() / 1.25f, bottomFrame.centerY() + rc.height() / 2.6f, ttf);
    }

    private Rect drawFrameRect(Rect rc, Canvas canvas, boolean divider, float divAt) {
        Paint fill = new Paint();
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(Color.argb(185, 225, 225, 170));
        canvas.drawRoundRect(rc.left, rc.top, rc.right, rc.bottom, 32, 32, fill);
        canvas.drawBitmap(frame1, rc.left, rc.top, null);
        if (divider) {
            int leftDiv = rc.right - (int) (rc.width() / divAt);
            canvas.drawBitmap(frame4, null, new Rect(leftDiv, rc.top, leftDiv + frame4.getWidth(), rc.bottom), null);

        }
        canvas.drawBitmap(frame2, null, new Rect(rc.left + frame3.getWidth(), rc.top, rc.right - frame3.getWidth(), rc.top + frame2.getHeight()), null);


        canvas.drawBitmap(frame3, rc.right - frame3.getWidth(), rc.top, null);
        canvas.drawBitmap(frame4, null, new Rect(rc.right - frame4.getWidth(), rc.top + frame8.getHeight(), rc.right, rc.bottom - frame7.getHeight()), null);
        canvas.drawBitmap(frame5, rc.right - frame3.getWidth(), rc.bottom - frame3.getWidth(), null);
        canvas.drawBitmap(frame6, null, new Rect(rc.left + frame3.getWidth(), rc.bottom - frame6.getHeight(), rc.right - frame3.getWidth(), rc.bottom), null);
        canvas.drawBitmap(frame7, rc.left, rc.bottom - frame3.getWidth(), null);
        canvas.drawBitmap(frame8, null, new Rect(rc.left, rc.top + frame8.getHeight(), rc.left + frame8.getWidth(), rc.bottom - frame7.getHeight()), null);
        return rc;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (left.contains((int) event.getX(), (int) event.getY())) {
                MainActivity.world.worldX -= 1;
                if (MainActivity.world.worldX < 0)
                    MainActivity.world.worldX = 0;
            }
            if (up.contains((int) event.getX(), (int) event.getY())) {
                MainActivity.world.worldY -= 1;
                if (MainActivity.world.worldY < 0)
                    MainActivity.world.worldY = 0;
            }


            if (right.contains((int) event.getX(), (int) event.getY())) {
                MainActivity.world.worldX += 1;
                if (MainActivity.world.worldX >= MainActivity.world.dimens)
                    MainActivity.world.worldX = MainActivity.world.dimens;
            }

            if (down.contains((int) event.getX(), (int) event.getY())) {
                MainActivity.world.worldY += 1;
                if (MainActivity.world.worldY >= MainActivity.world.dimens)
                    MainActivity.world.worldY = MainActivity.world.dimens;
            }
            this.invalidate();
            return true;
        }
        return false;
    }

    private void drawList(Canvas canvas, ArrayList<PositionRect> layer, int index) {
        int x = MainActivity.world.worldX;
        int y = MainActivity.world.worldY;


        for (PositionRect rc : layer) {


            GameObject go = MainActivity.world.get(index, x + rc.getX(), y + rc.getY());
            if (go != null) {
                if (go.getClass() == Stone.class) {
                    canvas.drawBitmap(stone, null, rc.getPos(), null);
                } else if (go.getClass() == Grass.class) {
                    canvas.drawBitmap(grass, null, rc.getPos(), null);
                } else if (go.getClass() == Sand.class) {
                    canvas.drawBitmap(sand, null, rc.getPos(), null);
                } else if (go.getClass() == Water.class) {
                    canvas.drawBitmap(water, null, rc.getPos(), null);
                } else if (go.getClass() == GrassStair.class) {
                    canvas.drawBitmap(grassstairs, null, rc.getPos(), null);
                } else if (go.getClass() == Brick.class) {
                    canvas.drawBitmap(brick, null, rc.getPos(), null);
                }
                if (go.getObjects().size() > 0) {

                    for (GameObject go2 : go.getObjects()) {
                        if (go2.getClass() == Willow.class) {

                            RectF from = new RectF(rc.getPos().centerX() - rc.getPos().width() / 4, rc.getPos().centerY() - rc.getPos().height() / 1.75f, rc.getPos().centerX() + rc.getPos().width() / 4, (rc.getPos().centerY() - rc.getPos().height() / 1.75f) + rc.getPos().width() / 2);
                            float val = 2.35f;
                            canvas.drawBitmap(willow, null, from, null);
                            from = new RectF(from.left, from.top - from.height() / val, from.right, from.bottom - from.height() / val);
                            canvas.drawBitmap(willow, null, from, null);
                            from = new RectF(from.left, from.top - from.height() / val, from.right, from.bottom - from.height() / val);
                            canvas.drawBitmap(willow, null, from, null);
                            from = new RectF(from.left, from.top - from.height() / val, from.right, from.bottom - from.height() / val);
                            canvas.drawBitmap(willow, null, from, null);
                            from = new RectF(from.left - from.width() / 2, (from.top - from.height() / 2) - from.width() / 2, from.right + from.width() / 2, (from.bottom - from.height() / 2) + from.width() / 2);
                            canvas.drawBitmap(leavesgreen, null, from, null);
                        } else if (go2.getClass() == Brick.class) {
                            RectF from = new RectF(rc.getPos().left, rc.getPos().top - rc.getPos().height() / 2, rc.getPos().right, rc.getPos().bottom - rc.getPos().height() / 2);
                            canvas.drawBitmap(brick, null, from, null);
                        } else if (go2.getClass() == House.class) {
                            RectF from = new RectF(rc.getPos().left, rc.getPos().top - rc.getPos().height() / 2, rc.getPos().right, rc.getPos().bottom - rc.getPos().height() / 2);
                            canvas.drawBitmap(house, null, from, null);
                        } else if (go2.getClass() == Villa.class) {
                            RectF from = new RectF(rc.getPos().left, rc.getPos().top - rc.getPos().height() / 2, rc.getPos().right, rc.getPos().bottom - rc.getPos().height() / 2);
                            canvas.drawBitmap(villa, null, from, null);
                        }
                        else if (go2.getClass() == Dragon.class) {
                            RectF from = new RectF(rc.getPos().left, rc.getPos().top - rc.getPos().height() / 1.5f, rc.getPos().right, rc.getPos().bottom - rc.getPos().height() / 1.5f);
                            canvas.drawBitmap(dragon, null, from, null);
                        }
                        else if (go2.getClass() == Gul.class) {
                            RectF from = new RectF(rc.getPos().left, rc.getPos().top - rc.getPos().height() / 1.5f, rc.getPos().right, rc.getPos().bottom - rc.getPos().height() / 1.5f);
                            canvas.drawBitmap(gul, null, from, null);
                        }
                        else if (go2.getClass() == Player.class) {
                            RectF from = new RectF(rc.getPos().centerX() - rc.getPos().width() / 4, rc.getPos().centerY() - rc.getPos().height() / 1.45f, rc.getPos().centerX() + rc.getPos().width() / 4, (rc.getPos().centerY() - rc.getPos().height() /1.45f) + rc.getPos().width() / 2);
                            canvas.drawBitmap(getFromPlayer(1,2), null, from, null);
                        }
                    }
                }

            }


        }
    }


    private ArrayList<PositionRect> drawLayer(Rect rc) {
        ArrayList<PositionRect> result = new ArrayList<>();
        //ebene -6
        result.add(gr(rc, 0, -6, 0, 0));

        //ebene -5
        result.add(gr(rc, -1, -5, 0, 1));
        result.add(gr(rc, 1, -5, 1, 0));

        //ebene -4
        result.add(gr(rc, -2, -4, 0, 2));
        result.add(gr(rc, 0, -4, 1, 1));
        result.add(gr(rc, 2, -4, 2, 0));

        //ebene -3
        result.add(gr(rc, -3, -3, 0, 3));
        result.add(gr(rc, -1, -3, 1, 2));
        result.add(gr(rc, 1, -3, 2, 1));
        result.add(gr(rc, 3, -3, 3, 0));

        //ebene -2
        result.add(gr(rc, -4, -2, 0, 4));
        result.add(gr(rc, -2, -2, 1, 3));
        result.add(gr(rc, 0, -2, 2, 2));
        result.add(gr(rc, 2, -2, 3, 1));
        result.add(gr(rc, 4, -2, 4, 0));

        //ebene -1
        result.add(gr(rc, -5, -1, 0, 5));
        result.add(gr(rc, -3, -1, 1, 4));
        result.add(gr(rc, -1, -1, 2, 3));
        result.add(gr(rc, 1, -1, 3, 2));
        result.add(gr(rc, 3, -1, 4, 1));
        result.add(gr(rc, 5, -1, 5, 0));

        //ebene 0 center
        result.add(gr(rc, -6, 0, 0, 6));
        result.add(gr(rc, -4, 0, 1, 5));
        result.add(gr(rc, -2, 0, 2, 4));
        result.add(gr(rc, 0, 0, 3, 3));
        result.add(gr(rc, 2, 0, 4, 2));
        result.add(gr(rc, 4, 0, 5, 1));
        result.add(gr(rc, 6, 0, 6, 0));


        //ebene 1
        result.add(gr(rc, -5, 1, 1, 6));
        result.add(gr(rc, -3, 1, 2, 5));
        result.add(gr(rc, -1, 1, 3, 4));
        result.add(gr(rc, 1, 1, 4, 3));
        result.add(gr(rc, 3, 1, 5, 2));
        result.add(gr(rc, 5, 1, 6, 1));

        //ebene 2
        result.add(gr(rc, -4, 2, 2, 6));
        result.add(gr(rc, -2, 2, 3, 5));
        result.add(gr(rc, 0, 2, 4, 4));
        result.add(gr(rc, 2, 2, 5, 3));
        result.add(gr(rc, 4, 2, 6, 2));

        //ebene 3
        result.add(gr(rc, -3, 3, 3, 6));
        result.add(gr(rc, -1, 3, 4, 5));
        result.add(gr(rc, 1, 3, 5, 4));
        result.add(gr(rc, 3, 3, 6, 3));

        //ebene 4
        result.add(gr(rc, -2, 4, 4, 6));
        result.add(gr(rc, 0, 4, 5, 5));
        result.add(gr(rc, 2, 4, 6, 4));

        //ebene 5
        result.add(gr(rc, -1, 5, 5, 6));
        result.add(gr(rc, 1, 5, 6, 5));

        //ebene 6
        result.add(gr(rc, 0, 6, 6, 6));

        return result;
    }

    private PositionRect gr(Rect rc, int x, int y, int lx, int ly) {

        int posx = rc.centerX();
        int posy = rc.centerY();

        if (x != 0) {
            posx += (x * size);
        }
        if (y != 0) {
            posy += (y * size) / 2;
        }


        return new PositionRect(new RectF(posx - size, posy - size, posx + size, posy + size), lx, ly);
    }
}
