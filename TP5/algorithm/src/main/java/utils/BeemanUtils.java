package utils;

import java.util.HashMap;
import java.util.Map;

public class BeemanUtils {

    static void getStartingAcc(Map<Particle, R> states, double gravity){
        states.forEach((particle, r) -> r.set(2, 0.0, -gravity));
    }

    static Map<Particle, R> euler(Map<Particle,R> initialRs, double dt, double gravity){

    }
}
