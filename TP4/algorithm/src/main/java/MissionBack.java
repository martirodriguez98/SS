import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MissionBack {
    public static void main(String[] args) {
        runMultipleDays();
//        runOneDay();
    }

    public static void runMultipleDays(){
        double minDistance = Double.MAX_VALUE;
        String minDay = "";
        final int daysTested = 366;
        Map<String, Particle> constantData = Parser.parse("planets-17-08-2023.txt");
        Particle sun = constantData.get("SUN");
        double earthMass = constantData.get("EARTH").getM();
        double earthRadius = constantData.get("EARTH").getRadio();
        double venusMass = constantData.get("VENUS").getM();
        double venusRadius = constantData.get("VENUS").getRadio();
        int earthId = constantData.get("EARTH").getId();
        int venusId = constantData.get("VENUS").getId();
        double distanceToSpaceship = -1500;
        List<List<String>> earthLines = null;
        List<List<String>> venusLines = null;
        try {
            earthLines = Files.readAllLines(Paths.get("earth-year.txt")).stream().skip(1).map(line -> Arrays.asList(line.split(",")))
                    .collect(Collectors.toList());
            venusLines = Files.readAllLines(Paths.get("venus-year.txt")).stream().skip(1).map(line -> Arrays.asList(line.split(",")))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> dates = new LinkedList<>();

        for (int i = 100; i < daysTested; i++) {
            List<String> dayEarth = earthLines.get(i);
            List<String> dayVenus = venusLines.get(i);
            String day = dayEarth.get(0);
            Particle earth = new Particle("EARTH", earthId, earthMass, earthRadius, Double.parseDouble(dayEarth.get(1)), Double.parseDouble(dayEarth.get(2)), Double.parseDouble(dayEarth.get(3)), Double.parseDouble(dayEarth.get(4)));
            Particle venus = new Particle("VENUS", venusId, venusMass, venusRadius, Double.parseDouble(dayVenus.get(1)), Double.parseDouble(dayVenus.get(2)), Double.parseDouble(dayVenus.get(3)), Double.parseDouble(dayVenus.get(4)));
            Map<String, Particle> particles = new HashMap<>();
            particles.put("SUN", sun);
            particles.put("EARTH", earth);
            particles.put("VENUS", venus);
            double distanceVenusSun = Math.sqrt(Math.pow((venus.getX() - sun.getX()), 2) + Math.pow((venus.getY() - sun.getY()), 2));
            //versors earth to sun
            double rx = (venus.getX() - sun.getX()) / distanceVenusSun;
            double ry = (venus.getY() - sun.getY()) / distanceVenusSun;
            //tangential components
            double tx = -ry;
            double ty = rx;

            double spaceshipX = (distanceToSpaceship + venus.getRadio()) * rx + venus.getX();
            double spaceshipY = (distanceToSpaceship + venus.getRadio()) * ry + venus.getY();

            double velocity = 8.8 + venus.getVx() * tx + venus.getVy() * ty;
            double spaceshipVx = tx * velocity;
            double spaceshipVy = ty * velocity;

            //radio 100 to visualize in ovito
            particles.put("SPACESHIP", new Particle("SPACESHIP", 1, 2 * Math.pow(10, 5), 100, spaceshipX, spaceshipY, spaceshipVx, spaceshipVy));
//            Parser.addSpaceshipData("static-file-" + day + ".txt", particles.values().stream().toList());
            double finalTime = 700 * 24 * 60 * 60;
            //double dt = 300.885;
            double dt = 300;
            ResultsMission resultsMission = GearPredictorCorrector.runMissionGear(particles, finalTime, dt);
            resultsMission.exportResults2(earthId);
            System.out.println(day + ": " + resultsMission.getMinDistance());
            dates.add(day + "," + resultsMission.getMinDistance());
            if (resultsMission.getMinDistance() < minDistance) {
                minDistance = resultsMission.getMinDistance();
                minDay = day;
            }
        }
        Utils.exportList(dates, "datesMissionBack.txt");

        System.out.println("*******************************");
        System.out.println("Best day was " + minDay + " with distance: " + minDistance);
    }

    public static void runOneDay(){
        String day = "17-08-2023";
        Map<String, Particle> particles = Parser.parse("planets-" + day + ".txt");
        double distanceToSpaceship = 1500;

        Particle venus = particles.get("VENUS");
        Particle sun = particles.get("SUN");
        double distanceVenusSun = Math.sqrt(Math.pow((venus.getX() - sun.getX()),2) + Math.pow((venus.getY() - sun.getY()),2));
        //versors venus to sun
        double rx = (venus.getX() - sun.getX()) / distanceVenusSun;
        double ry = (venus.getY() - sun.getY()) / distanceVenusSun;
        //tangential components
        double tx = -ry;
        double ty = rx;

        double spaceshipX = (distanceToSpaceship + venus.getRadio()) * rx + venus.getX();
        double spaceshipY = (distanceToSpaceship + venus.getRadio()) * ry + venus.getY();

        double velocity = 8.8 + venus.getVx() * tx + venus.getVy() * ty;
        double spaceshipVx = tx * velocity;
        double spaceshipVy = ty * velocity;
        //radio 100 to visualize in ovito
        particles.put("SPACESHIP", new Particle("SPACESHIP",1, 2*Math.pow(10,5),100, spaceshipX, spaceshipY, spaceshipVx, spaceshipVy));
        Parser.addSpaceshipData("missionBackStatic-" + day + ".txt", particles.values().stream().toList());
        double finalTime = 700 * 24 * 60 * 60;
        double dt = 300;
        ResultsMission resultsMission = GearPredictorCorrector.runMissionGear(particles, finalTime, dt);
        resultsMission.exportResults("missionBack-" + day + ".txt",0);
        System.out.println(resultsMission.getMinDistance());
    }
}
