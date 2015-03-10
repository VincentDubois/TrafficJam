package fr.iutlens.trafficjam.traffic;

/**
 * Created by dubois on 20/01/15.
 */
public class LevelMap {

    public final int[][] map;

    public final int width, height;

    public LevelMap(int[][] map){
        this.map = map;
        width = map.length;
        height = map[0].length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /***
     * calcule la position de départ.
     * @param track
     * @return
     */
    public int getNdx(Track track){
        return getNdx(track.iS,track.jS);
    }

    /***
     * Calcule la position à la fin du round (prend en compte la vitesse)
     * @param car
     * @return
     */
    public int getNextNdx(Car car){
        if (car.speed == 0) return getNdx(car);
        return getNextMoveNdx(car);
    }

    /***
     * Calcule la prochaine position sur la trajectoire
     * @param car
     * @return
     */
    public int getNextMoveNdx(Car car) {
        return getNdx(car.nextX(),car.nextY());
    }

    /***
     * Calcule la position actuelle
     * @param car
     * @return
     */
    public int getNdx(Car car){
        return getNdx(car.x,car.y);
    }


    /***
     * Associe un entier unique à chaque paire (i,j) sur la carte et une case autour.
     * @param i
     * @param j
     * @return
     */
    public int getNdx(int i, int j){
        return 1+i+j*(width+2);
    }

    public int getI(int ndx){
        return ndx%(width+2)-1;
    }

    public int getJ(int ndx){
        return ndx/(width+2);
    }

    public void setColor(int ndx, int value) {
        int i=getI(ndx);
        int j=getJ(ndx);
        map[i][j]=value;
    }
}
