import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {
    public static Map<String,Particle> parse(String path){
        List<List<String>> planetLines = null;
        try {
            planetLines = Files.readAllLines(Paths.get(path)).stream().skip(1).map(line-> Arrays.asList(line.split(",")))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Particle> particles = new HashMap<>();
        if(planetLines != null){
            for(List<String> line : planetLines){
                Particle particle = new Particle(line.get(0),Double.parseDouble(line.get(1)),Double.parseDouble(line.get(2)),
                        Double.parseDouble(line.get(3)),Double.parseDouble(line.get(4)),Double.parseDouble(line.get(5)),
                        Double.parseDouble(line.get(6)));
                particles.put(particle.getName(), particle);
            }
        }
        return particles;
    }

    public static void addSpaceshipData(String path, List<Particle> particles){
        BufferedWriter bf = null;
        try{
            bf = new BufferedWriter(new FileWriter(path, false));
            particles = particles.stream().sorted().collect(Collectors.toList());
            for (Particle particle : particles){
                bf.write(particle.getName() + "," + particle.getRadio() + "," + particle.getM() + "," + particle.getX() + "," + particle.getY() + "," + particle.getVx() + "," + particle.getVy());
                bf.newLine();
            }
            bf.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                assert bf != null;
                bf.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
