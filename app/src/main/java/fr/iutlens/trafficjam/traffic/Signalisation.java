package fr.iutlens.trafficjam.traffic;

public class Signalisation {
        private Feu[] feu;


    public boolean invertLight() {
        for(Feu f : feu ) {
            f.invertLight();
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
