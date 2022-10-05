import java.util.Map;

public class TestMission {
    public static void main(String[] args) {
        Map<String, Particle> particles = Parser.parse("planets-23-09-2022.txt");
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

        double spaceshipX = (distanceToSpaceship + earth.getRadio()) * -rx + earth.getX();
        double spaceshipY = (distanceToSpaceship + earth.getRadio()) * -ry + earth.getY();
        double velocity = -7.12 -8 + earth.getVx() * tx + earth.getVy() * ty;
        double spaceshipVx = tx * velocity;
        double spaceshipVy = ty * velocity;

        //radio 100 to visualize in ovito
        particles.put("SPACESHIP", new Particle("SPACESHIP",1, 2*Math.pow(10,5),100, spaceshipX, spaceshipY, spaceshipVx, spaceshipVy));
        Parser.addSpaceshipData("static_file.txt", particles.values().stream().toList());
        double finalTime = 365 * 24 * 60 * 60;
        double dt = 300;
        ResultsMission resultsMission = GearPredictorCorrector.runMissionGear(particles, finalTime, dt);
        resultsMission.exportResults("results.txt");
    }
}
