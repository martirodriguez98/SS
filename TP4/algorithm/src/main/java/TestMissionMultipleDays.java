import java.util.Map;

public class TestMissionMultipleDays {
    public static void main(String[] args) {
        String[] days = {"22-09-2022","23-09-2022", "24-09-2022", "25-09-2022","06-10-2022", "20-10-2022","23-10-2022", "01-01-2023", "25-09-2023"};
        double minDistance = Double.MAX_VALUE;
        String minDay = "";
        for (String day : days){
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
            double velocity = -7.12 -8 + earth.getVx() * tx + earth.getVy() * ty;
            double spaceshipVx = tx * velocity;
            double spaceshipVy = ty * velocity;

            //radio 100 to visualize in ovito
            particles.put("SPACESHIP", new Particle("SPACESHIP",1, 2*Math.pow(10,5),100, spaceshipX, spaceshipY, spaceshipVx, spaceshipVy));
            Parser.addSpaceshipData("static-file-" + day + ".txt", particles.values().stream().toList());
            double finalTime = 365 * 24 * 60 * 60;
            double dt = 300;
            ResultsMission resultsMission = GearPredictorCorrector.runMissionGear(particles, finalTime, dt);
            resultsMission.exportResults("results-" + day + ".txt");
            System.out.println(day + ": " + resultsMission.getMinDistance());
            if(resultsMission.getMinDistance() < minDistance){
                minDistance = resultsMission.getMinDistance();
                minDay = day;
            }
        }
        System.out.println("*******************************");
        System.out.println("Best day was " + minDay + " with distance: " + minDistance);
    }
}
