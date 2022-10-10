import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestMissionMultipleDays {
    public static void main(String[] args) {
        double minDistance = Double.MAX_VALUE;
        String minDay = "";
        final int daysTested = 366;
        Map<String, Particle> constantData = Parser.parse("planets-01-01-2023.txt");
        Particle sun = constantData.get("SUN");
        double earthMass = constantData.get("EARTH").getM();
        double earthRadius = constantData.get("EARTH").getRadio();
        double venusMass = constantData.get("VENUS").getM();
        double venusRadius = constantData.get("VENUS").getRadio();
        int earthId = constantData.get("EARTH").getId();
        int venusId = constantData.get("VENUS").getId();
        double distanceToSpaceship = 1500;
        List<List<String>> earthLines = null;
        List<List<String>> venusLines = null;
        try {
            earthLines = Files.readAllLines(Paths.get("earth-year.txt")).stream().skip(1).map(line-> Arrays.asList(line.split(",")))
                    .collect(Collectors.toList());
            venusLines = Files.readAllLines(Paths.get("venus-year.txt")).stream().skip(1).map(line-> Arrays.asList(line.split(",")))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=328; i<329; i++){
            List<String> dayEarth = earthLines.get(i);
            List<String> dayVenus = venusLines.get(i);
            String day = dayEarth.get(0);
            Particle earth = new Particle("EARTH", earthId, earthMass, earthRadius, Double.parseDouble(dayEarth.get(1)), Double.parseDouble(dayEarth.get(2)),Double.parseDouble(dayEarth.get(3)), Double.parseDouble(dayEarth.get(4)));
            Particle venus = new Particle("VENUS", venusId, venusMass, venusRadius, Double.parseDouble(dayVenus.get(1)), Double.parseDouble(dayVenus.get(2)),Double.parseDouble(dayVenus.get(3)), Double.parseDouble(dayVenus.get(4)));
            Map<String,Particle> particles = new HashMap<>();
            particles.put("SUN", sun);
            particles.put("EARTH", earth);
            particles.put("VENUS", venus);
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
            //double dt = 300.885;
            double dt = 300.8851;
            ResultsMission resultsMission = GearPredictorCorrector.runMissionGear(particles, finalTime, dt);
            resultsMission.exportResults("results-" + day + ".txt");
            System.out.println(day + ": " + resultsMission.getMinDistance());
            if(resultsMission.getMinDistance() < minDistance){
                minDistance = resultsMission.getMinDistance();
                minDay = day;
            }
        }
        /*String[] days = {"23-09-2022", "24-09-2022", "25-09-2022","06-10-2022", "20-10-2022","23-10-2022", "01-01-2023"};
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
        }*/

        System.out.println("*******************************");
        System.out.println("Best day was " + minDay + " with distance: " + minDistance);
    }
}
