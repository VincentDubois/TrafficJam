package fr.iutlens.trafficjam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import fr.iutlens.trafficjam.traffic.Car;
import fr.iutlens.trafficjam.traffic.Feu;
import fr.iutlens.trafficjam.traffic.LevelMap;
import fr.iutlens.trafficjam.traffic.Signalisation;
import fr.iutlens.trafficjam.traffic.Track;
import fr.iutlens.trafficjam.traffic.Traffic;
import fr.iutlens.trafficjam.util.CoordSystem;
import fr.iutlens.trafficjam.util.SpriteSheet;

/**
 * Created by dubois on 20/01/15.
 */
public class TrafficView extends View {

    LevelMap map;
    CoordSystem coord;

    SpriteSheet sprite;
    Traffic traffic;

    // Entre 0 et 1, proportion du mouvent déjà effectué.
    private float progress;

    // Transformation permettant le centrage de la vue.
    private Matrix transform, reverse;

    // Rectangle réutilisable (pour éviter les instanciations)
    private RectF tmp;


    // Configuration du mode de dessin
    static PaintFlagsDrawFilter setfil= new PaintFlagsDrawFilter(0,
                Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
    private Rect src;


    // Configuration du compteur de voitures
    private int nbVoitures; // crée un champ du nombre de voitures à faire passer

    public int getNbVoitures() { // fonction permettant de récupérer le nombre de voiture restant à faire passer
        return nbVoitures;
    } // get du nombre de voitures

    public void deleteCar() {
        nbVoitures--;
    }


    // 3 constructeurs obligatoires pour une vue. Les 3 appellent init() pour ne pas dupliquer le code.

    public TrafficView(Context context) {
        super(context);
        init();
    }

    public TrafficView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrafficView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /***
     * Fait progresser la vue d'une image
     */
    void act(){
       progress += 0.1; // les voitures en mouvement avancent de 10% d'une case
        if (progress >=1){ // Si on arrive à la case d'après, on recalcule les prochains mouvements.
            progress = 0;
            traffic.moveAll();
        }
        this.invalidate(); // La vue a changé, on demande le rafraîchissement de l'affichage.
    }

    public void init(){

        nbVoitures = 60; // nombre de voiture initiailisé en début de partie
        // TODO : nombre de voitures à définir selon la difficulté du jeu

        coord = new CoordSystem(-7,2,4,6);

        //                                       NIVEAU 1 (didacticiel)
//        int[][] data = {
//                //                              1  1  1  1  1  1  1  1  1  1  2  2  2  2  2  2
//                //0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//0
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//1
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//2
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//3
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//4
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//5
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//6
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//7
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//8
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//9
//                {-1,05,05,05,05,05,05,05,05,05, 8,12,13,06,05,05,05,05,05,05,02,20,20,20,20,-1},//10
//                {-1,15,15,15,15,15,15,15,15,15,15,18,19,15,15,15,15,15,16,16,04,20,20,20,20,-1},//11
//                {-1,14,14,14,14,14,14,14,14,14,14,19,18,10,14,14,14,14,16,16,04,20,20,20,20,-1},//12
//                {-1,06,06,06,06,06,06,06,06,06,11,12,13, 9,06,06,06,06,12,13,04,20,20,20,20,-1},//13
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,07,12,13,04,20,20,20,20,-1},//14
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,07,12,13,04,20,20,20,20,-1},//15
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,07,12,13,04,20,20,20,20,-1},//16
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,07,12,13,04,20,20,20,20,-1},//17
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,07,12,13,04,20,20,20,20,-1},//18
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,07,12,13,04,20,20,20,20,-1},//19
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,07,12,13,04,20,20,20,20,-1},//20
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,07,12,13,04,20,20,20,20,-1},//21
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,07,12,13,04,20,20,20,20,-1},//22
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//23
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//24
//                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,-1},//25
//
//        };


//                                        NIVEAU 2
        int[][] data = {
                //                              1  1  1  1  1  1  1  1  1  1  2  2  2  2  2  2  2  2  2  2, 3, 3
                // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,-1},//0
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,-1},//1
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,-1},//2
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,-1},//3
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,-1},//4
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,-1},//5
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,-1},//6
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,-1},//7
                {-1,05,05,05,05,05,05,05,05,05,05,05,05,05,05,05,05,05,03,12,13,00,05,05,05,05,05,05,05,05,05,-1},//8
                {-1,15,15,15,15,15,15,15,15,15,15,16,16,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,-1},//9
                {-1,14,14,14,14,14,14,14,14,14,14,16,16,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,-1},//10
                {-1,06,06,06,06,06,06,06,06,06,02,12,13,01,06,06,06,06,02,12,13,01,06,06,06,06,06,06,06,06,06,-1},//11
                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,-1},//12
                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,-1},//13
                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,11,05,05,03,12,13,04,20,20,20,20,20,20,20,20,20,-1},//14
                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,07,16,16,15,16,16,04,20,20,20,20,20,20,20,20,20,-1},//15
                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,07,16,16,14,16,16,04,20,20,20,20,20,20,20,20,20,-1},//16
                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,07,12,13,01,06,06, 9,20,20,20,20,20,20,20,20,20,-1},//17
                {-1,20,20,20,20,20,20,20,20,20,07,12,13,04,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,20,-1},//18
                {-1,05,05,05,05,05,05,05,05,05,03,12,13,00,05,03,12,13,00,05,05,05,05,05,05,05,05,05,05,05,05,-1},//19
                {-1,15,15,15,15,15,15,15,15,15,15,16,16,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,-1},//20
                {-1,14,14,14,14,14,14,14,14,14,14,16,16,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,-1},//21
                {-1,06,06,06,06,06,06,06,06,06,06,06,06,06,06,02,12,13,01,06,06,06,06,06,06,06,06,06,06,06,06,-1},//22
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,20,-1},//23
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,20,-1},//24
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,20,-1},//25
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,20,-1},//26
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,20,-1},//27
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,20,-1},//28
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,20,-1},//29
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,20,-1},//30
                {-1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,07,12,13,04,20,20,20,20,20,20,20,20,20,20,20,20,-1},//31
        };


        map = new LevelMap(data);

        Track[] track = new Track[]{

//          NIVEAU 2
                new Track("9:26:222222222222222111111111112222222:10"), //BAS DROITE suis la route puis tourne à gauche à la prochaine intersection, puis à droite
                new Track("9:26:22222233333:5"), //BAS DROITE vers la droite

                new Track("21:3:00000000000000333330003333333333333:2"), //HAUT GAUCHE prend la petite route avec le virage
                new Track("3:19:111111111111222111111111111111:5"), //HAUT DROITE vas tout droit et prend la petite route

                new Track("20:21:2222222222222222222:15"), //BAS MILIEU
                new Track("21:2:00000000003333333333300000000333333:10"), //virages

                new Track("28:17:3333333000000000:5"), //BAS GAUCHE

                new Track("21:2:000000000000000000:12"), //HAUT GAUCHE  vas tout droit
                new Track("21:2:0000000000000011111111:7"), //HAUT GAUCHE tourne à droite


//          NIVEAU 1 (didacticiel)
//                  new Track("12:0:000000000000000000011111:10"), //HAUT GAUCHE
//                  new Track("22:20:333333333332222222222222222222:10"), //BAS DROITE
//                  new Track("1:11:11111111111111111111111111111:20"), //BAS GAUCHE
//                  new Track("25:12:3333333333333333333333333333:20"), //HAUT DROITE
//
        };

        Feu[] feu = new Feu[]{

                new Feu(map,20,11),
                new Feu(map,20,16),
                new Feu(map,9,11),
                new Feu(map,9,19),
        };

        Signalisation signalisation = new Signalisation(feu);
        traffic = new Traffic(map, track, signalisation, this);

        if (transform == null) {
            transform = new Matrix();
            reverse = new Matrix();


            sprite = SpriteSheet.get(this.getContext(), R.drawable.sprite);
            src = new Rect(0, 0, sprite.w, sprite.h);
        }
    }

    public void onDraw(Canvas canvas){
        canvas.setDrawFilter(setfil);


        // on sauvegarde la transformation en cours et on applique la transformation désirée
        canvas.save();
        canvas.concat(transform);

        if (sprite == null){ //si les sprites ne sont pas chargé, on ne fait rien.
            return;
        }


    //On parcours la carte
        for(int i = 0; i < map.getWidth(); ++i){
            for(int j = 0; j < map.getHeight(); ++j){
                int code = map.map[i][j];
                if (code != -1)
                    canvas.drawBitmap(sprite.getBitmap(code), src,coord.bounds(i,j,i+1,j+1,tmp),null);
            }
        }

     // On parcours les voitures
        for(Car car : traffic.getCar()){
            float i,j;
            i = car.getX(progress);
            j = car.getY(progress);
            canvas.drawBitmap(sprite.getBitmap((car.getAngle() & 1)+21), src,coord.bounds(i,j,i+1,j+1,tmp),null);
        }


        // On restore la transformation originale.
        canvas.restore();
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setZoom(w, h);
    }

    /***
     * Calcul du centrage du contenu de la vue
     * @param w
     * @param h
     */
    private void setZoom(int w, int h) {
        if (w<=0 ||h <=0) return;

        // Dimensions dans lesquelles ont souhaite dessiner
        RectF src = coord.bounds(0,0,map.getWidth(),map.getHeight(),tmp);

        src.top += 81;           //30        88
        src.left += 90;          //35        48
        src.bottom += -81;       //30        88
        src.right += -90;        //35        48

        // Dimensions à notre disposition
        RectF dst = new RectF(0,0,w,h);

        // Calcul de la transformation désirée (et de son inverse)
        transform.setRectToRect(src,dst, Matrix.ScaleToFit.CENTER);
        transform.invert(reverse);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int stopfeu = event.getAction();
        switch (stopfeu) {
            case MotionEvent.ACTION_DOWN:
                traffic.invertLight();
                break;
        }
        return true;


    }

    public int getTmpstotal() {
        return traffic.getTmpstotal();
    }
}
