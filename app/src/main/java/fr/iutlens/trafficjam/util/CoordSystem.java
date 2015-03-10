package fr.iutlens.trafficjam.util;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by dubois on 20/01/15.
 */
public class CoordSystem {

    //Système de projection cavalière (pour une grille en perspective)

    Matrix projection,inverse;

    /***
     * Crée le système de projection à partir des vecteurs i et j
     * @param ix
     * @param iy
     * @param jx
     * @param jy
     */
    public CoordSystem(int ix, int iy,
                    int jx, int jy){

        projection =  new Matrix();
        projection.setValues(new float[]{ // matrice de passage de (O,i,j) vers (O,x,y)
                ix,jx,0,
                iy,jy,0,
                0,0,1});
        inverse = new Matrix();
        projection.invert(inverse);
    }

    /***
     * Indique le rectangle projeté contenant les points du rectangle dans le repère (O,i,j)
     * @param minI
     * @param minJ
     * @param maxI
     * @param maxJ
     * @param result Rectangle à utiliser pour le résultat. Si null, un nouveau rectangle est créé.
     * @return
     */
    public RectF bounds(float minI, float minJ, float maxI, float maxJ, RectF result){
        if (result == null) result = new RectF();

        projection.mapRect(result, new RectF(minI,minJ,maxI,maxJ));

//        Log.d("bound", result.toString());
        return result;
    }




}
