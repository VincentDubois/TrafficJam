package fr.iutlens.trafficjam.traffic;

public class Signalisation {
        private Feu feu;

    public boolean invertLight() {
        return feu.invertLight();
    }

    public Signalisation(Feu feu) {
        this.feu = feu;
    }

    public boolean allowMove(int position, int direction){
           return feu.allowMove(position, direction);

        }
 }
