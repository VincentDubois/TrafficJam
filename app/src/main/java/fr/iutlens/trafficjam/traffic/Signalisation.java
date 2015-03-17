package fr.iutlens.trafficjam.traffic;

public class Signalisation {
        private Feu[] feu;


    public boolean invertLight(float[] pts) {
        for(Feu f : feu ) {
            f.invertLight(pts);
        }
        return false;
    }

    public Signalisation(Feu[] feu) {
        this.feu = feu;
    }

    public boolean allowMove(int position, int direction) {
        for (Feu f : feu) {
            if(!f.allowMove(position,direction)){
                return false;
            }
        }

        return true;
    }
 }
