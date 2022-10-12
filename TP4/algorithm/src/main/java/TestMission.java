import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestMission {
    public static void main(String[] args) {
        String day = "17-08-2023";
        double COLLISION_DISTANCE = 100;
        Map<String, Particle> particles = Parser.parse("planets-" + day + ".txt");
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
        List<String> info = new LinkedList<>();
        for (double i =0,j = 0; j <= 5 ; i+=0.0001, j++){
            double velocity = -7.12 -(8 - i) + earth.getVx() * tx + earth.getVy() * ty;
            double spaceshipVx = tx * velocity;
            double spaceshipVy = ty * velocity;
            //radio 100 to visualize in ovito
            particles.put("SPACESHIP", new Particle("SPACESHIP",1, 2*Math.pow(10,5),100, spaceshipX, spaceshipY, spaceshipVx, spaceshipVy));
            Parser.addSpaceshipData("static-file-" + day +"_" + j + ".txt", particles.values().stream().toList());
            double finalTime = 365 * 24 * 60 * 60;
            double dt = 300.8851;
            ResultsMission resultsMission = GearPredictorCorrector.runMissionGear(particles, finalTime, dt);
            resultsMission.exportResults("results-" + day +"_" + j + ".txt");
            info.add((8-i) + "," + resultsMission.getMinDistance() + "," + resultsMission.getMinTime());
            Utils.exportList(info, "different_v.txt");

        }


    }
}
