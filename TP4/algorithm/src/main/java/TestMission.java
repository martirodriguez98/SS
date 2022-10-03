import java.util.Map;

public class TestMission {
    public static void main(String[] args) {
        Map<String, Particle> particles = Parser.parse("planets.txt");
        double distanceToSpaceship = 1500;

        Particle earth = particles.get("EARTH");
        Particle sun = particles.get("SUN");
        double distanceEarthSun = Math.sqrt(Math.pow((earth.getX() - sun.getX()),2) + Math.pow((earth.getY() - sun.getY()),2));
        //versors earth to sun
        double rx = (earth.getX() - sun.getX()) / distanceEarthSun;
        double ry = (earth.getY() - sun.getY()) / distanceEarthSun;
        //tangential components
        double tx = -ry;
        double ty = rx;

        double spaceshipX = distanceToSpaceship * -rx + earth.getX() + earth.getRadio();
        double spaceshipY = distanceToSpaceship * -ry + earth.getY() + earth.getRadio();
        double velocity = -7.12 -8 + earth.getVx() * tx + earth.getVy() * ty;
        double spaceshipVx = tx * velocity;
        double spaceshipVy = ty * velocity;

        //radio 100 to visualize in ovito
        particles.put("SPACESHIP", new Particle("SPACESHIP", 2*Math.pow(10,5),100, spaceshipX, spaceshipY, spaceshipVx, spaceshipVy));
        particles.values().forEach(System.out::println);
        //tierra propulsion orbital
        //calcular v tangencial inicial de la nave sumando todas las v, despues pasar a x e y
    }
}
