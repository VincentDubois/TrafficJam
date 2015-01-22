package fr.iutlens.trafficjam.traffic;

/**
 * Created by dubois on 20/01/15.
 */
public class Car {

    int x,y;
    int angle,speed;

    Track track; // Piste suivie
    int ndx; // position sur la piste

    /***
     * Crée une voiture au début d'une piste.
     * @param track
     */
    public Car(Track track){
        this.track = track;

        this.x = track.iS;
        this.y = track.jS;

        this.ndx = 0;
        this.angle =  track.getMove(ndx);
        this.speed = 0;
    }

    public int getAngle(){
        return angle;
    }

    /***
     * Position actuelle, entre précédente position et future position.
     * @param progress entre 0 (précédente) et 1 (suivante)
     * @return
     */
    public float getX(float progress){
        return x + Track.dir[angle][0]*progress*speed;
    }

    /***
     * Position actuelle, entre précédente position et future position.
     * @param progress entre 0 (précédente) et 1 (suivante)
     * @return
     */
    public float getY(float progress){
        return y + Track.dir[angle][1]*progress*speed;
    }

    /***
     * Prochaine position sur la trajectoire.
     * @return
     */
    public int nextX(){
        return x+ Track.dir[angle][0];
    }

    /***
     * Prochaine position sur la trajectoire.
     * @return
     */
    public int nextY(){
        return y+ Track.dir[angle][1];
    }


    /***
     * Change la vitesse
     * @param speed 0 :  arrêt ; 1 : roule.
     */
    public void setSpeed(int speed){
        this.speed = speed;
    }

    /***
     * Passe à la prochaine position
     * @return true si il y a une prochaine position, false si la trajectoire est terminée.
     */
    public boolean next(){
        if (speed != 0) {
            ++ndx;
            x = nextX();
            y = nextY();
            this.angle = track.getMove(ndx);
        }

        return angle != -1;
    }


}
