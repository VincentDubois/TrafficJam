package fr.iutlens.trafficjam.traffic;

import java.util.Map;

public class Feu {
    private LevelMap map;

    public Feu(LevelMap map) {
        this.map = map;
    }

    public boolean allowMove(int position, int direction) {
        int light = map.getNdx(3,2);
        if(light==position&&direction==0){
            return false;
        }
        else{
            return true;
        }
    }
}
