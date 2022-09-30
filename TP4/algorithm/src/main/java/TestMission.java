import java.util.HashMap;
import java.util.Map;

public class TestMission {
    public static void main(String[] args) {
        Map<String, Particle> particles = Parser.parse("planets.txt");
        particles.values().forEach(System.out::println);
        //tierra propulsion orbital
        //calcular v tangencial inicial de la nave sumando todas las v, despues pasar a x e y
    }
}
