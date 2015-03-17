package fr.iutlens.trafficjam.traffic;

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

        update();
    }

    private void update() {
        for(int i=0;i<=3;i++){
            if(!light) {
                map.setColor(ndx[i], 18 + i % 2);
            }
            else{
                map.setColor(ndx[i], 19 - i % 2);
            }
        }
    }

    public boolean invertLight(){
        light=!light;
        update();
        return light;
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