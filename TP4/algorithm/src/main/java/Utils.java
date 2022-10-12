import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Utils {
    public static void exportList(List<String> info,String path){
        File file = new File(path);
        BufferedWriter bf = null;
        try{
            bf = new BufferedWriter(new FileWriter(file));
            for(String date : info){
                bf.write(date + "\n");
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
