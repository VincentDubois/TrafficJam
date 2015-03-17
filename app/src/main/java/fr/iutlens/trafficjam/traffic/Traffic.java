package fr.iutlens.trafficjam.traffic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.iutlens.trafficjam.TrafficView;

/**
 * Created by dubois on 20/01/15.
 */
public class Traffic {


    private final LevelMap map; // Carte du niveau.
    private Map<Integer,Car> current,next,tmp; // Voitures indexées par position.
    private final Track[] track; // liste des chemins emmpruntable.
    private int tmpstotal;
    private Signalisation signalisation;
    private TrafficView trafficView;

    public boolean invertLight(float[] pts) {
        return signalisation.invertLight(pts);
    }

    public Traffic(LevelMap map, Track[] track, Signalisation signalisation, TrafficView trafficView) {
        this.map = map;
        this.track = track;
        this.signalisation = signalisation;
        this.trafficView = trafficView;
        current = new HashMap();
        next = new HashMap();
    }

    public Collection<Car> getCar(){
        return current.values();
    }

    public boolean canMove(Car car){
        int nextNdx = map.getNextMoveNdx(car);
        if(signalisation.allowMove(nextNdx,car.getAngle() )){
            return (current.get(nextNdx) == null);
        }
        else{
            return false;
        }

    }

    public int getTmpstotal() {
        return tmpstotal;
    }

    public void moveAll(){
        tmpstotal = 0;
        //Termine les déplacements du round précédent
        for(Car car : current.values()){
            if (car.next()){ //On déplace la voiture, et si la voiture a encore du chemin à faire
                next.put(map.getNdx(car),car); // on l'insère dans le prochain round
                tmpstotal = car.getAttente() + tmpstotal;
            }
            else {
                trafficView.deleteCar();
            }
        }

        //Remplace current par next, en recyclant les HashMap pour éviter d'instancier
        tmp = current;
        current = next;
        next = tmp;
        next.clear();

        // Les voitures ont maintenant terminé le round précédent.

      /*  for(Car car : current.values()){
            if (canMove(car)){
                car.setSpeed(1);
            } else {
                car.setSpeed(0);
                next.put(map.getNdx(car),car); // Pour les voitures à l'arrêt, la position lors du
                // prochain round est connue et définitive.
            }
        }*/


        // On calcule les voitures qui peuvent avancer
        for(Car car : current.values()){
            if (canMove(car)){
                car.setSpeed(1);
            } else {
                car.setSpeed(0);
                next.put(map.getNdx(car),car); // Pour les voitures à l'arrêt, la position lors du
                                              // prochain round est connue et définitive.
            }

        }

        // Parmi les voitures en mouvement, on règle les conflits (priorité, accidents ?)
        for(Car car : current.values()){
            if (car.speed != 0){ // Si voiture en mouvement
                int ndx = map.getNextNdx(car); // calcule la prochaine position
                if (next.get(ndx) != null) { //Voie occupée ?
                    car.setSpeed(0); // Arrêt
                }
                next.put(map.getNextNdx(car),car); // valide la position
            }
        }

        // Ajout si possible de nouveaux véhicules
        for(Track t : track ){
            double aleatoire = t.freq; //la freq
            int ndx = map.getNdx(t);
            if (current.get(ndx) == null && Math.random() < (aleatoire/100)){ // 1 fois 4 qd la position est libre
                Car car = new Car(t);
                if (next.get(map.getNextMoveNdx(car))== null)  car.setSpeed(1); //roule si voie libre.
                current.put(map.getNdx(car), car);
            }
        }

        next.clear();
    }

}

