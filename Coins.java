import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Coins {
    File coinFile;
    Scanner sc = null;
    int[][] coinMap = new int[25][40];
    
    Coins (String coinPath) {
        this.coinFile = new File(coinPath);
        try {
            sc = new Scanner(this.coinFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        createCoin();
    }


    void createCoin () {
        int j = 0;
        while (this.sc.hasNextLine()) {
            String[] tempCharArr = this.sc.nextLine().split(" ");
            for (int i=0; i<40; i++) {
                this.coinMap[j][i] = Integer.valueOf(tempCharArr[i]);
            }
            j++;    
        }
    }

    int[][] getCoin() {
        return this.coinMap;
    }

    void drawMap(Graphics g, int TILE_HEIGHT, int TILE_WIDTH) {
        for (int i=0; i<25; i++) {
            for (int j=0; j<40; j++) {
                if(coinMap[i][j]==1){
                g.setColor(Color.white);
                g.fillOval(j*TILE_WIDTH + 8 , i*TILE_HEIGHT + 8, TILE_WIDTH/2, TILE_HEIGHT/2);
                }
            }
        }
    }
}