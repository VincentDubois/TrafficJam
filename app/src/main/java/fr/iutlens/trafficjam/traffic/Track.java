package fr.iutlens.trafficjam.traffic;

/**
 * Created by dubois on 20/01/15.
 */
public class Track {

    public static final int[][] dir = {{0,1},{1,0},{0,-1},{-1,0}};
    public int iS,jS; // Position de départ
    String moves; // déplacements :  0 1 2 3 -> N E S O

    public int freq;  //**********modif************

    /***
     * Créé une Track à partir de la description
     * @param description "x0:y0:chemin", avec chemin une suite de direction.
     *                  (0 1 2 3 correspondent respectivement à N E S O)
     */

    public Track(String description){

        String[] part = description.split(":");
        iS = Integer.parseInt(part[0]);
        jS = Integer.parseInt(part[1]);
        moves = part[2];
        freq = Integer.parseInt(part[3]); //***********modif************
    }

    /***
     * Retourne le ndx ième angle
     * @param ndx
     * @return Angle sous forme d'entier.
     * (0 1 2 3 correspondent respectivement à N E S O)
     */
    public int getMove(int ndx){
        if (ndx>= moves.length()) return -1;
        return moves.charAt(ndx)-'0';
    }

}
