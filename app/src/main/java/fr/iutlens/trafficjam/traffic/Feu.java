package fr.iutlens.trafficjam.traffic;

import java.util.Map;

public class Feu {
    private LevelMap map;
    private int i;
    private int j;
    private final int[] ndx;
    private boolean light;

    public Feu(LevelMap map, int i, int j) {
        this.map = map;
        this.i = i;
        this.j = j;
        ndx = new int[]{map.getNdx(i+1,j),map.getNdx(i,j),map.getNdx(i,j+1),map.getNdx(i+1,j+1)};
                       //0=Nord           1=Est            2=Sud            3=Ouest
        light=true;
    }

    public boolean allowMove(int position, int direction) {
        if(ndx[direction] == position){
            if(light) {
                return direction % 2 == 0;
            }
            else{
                return direction % 2 == 1;
            }
        }
        else{
            return true;
        }
    }
}