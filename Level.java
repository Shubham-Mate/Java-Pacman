import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level {
    File levelFile;
    Scanner sc = null;
    int[][] levelMap = new int[25][40];
    
    Level (String levelPath) {
        this.levelFile = new File(levelPath);
        try {
            sc = new Scanner(this.levelFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        createLevel();
    }


    void createLevel () {
        int j = 0;
        while (this.sc.hasNextLine()) {
            String[] tempCharArr = this.sc.nextLine().split(" ");
            for (int i=0; i<40; i++) {
                this.levelMap[j][i] = Integer.valueOf(tempCharArr[i]);
            }
            j++;    
        }
    }

    int[][] getLevel() {
        return this.levelMap;
    }
}
