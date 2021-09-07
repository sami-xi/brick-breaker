import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
    public boolean brickMap[][];
    public int brickWidth;
    public int brickHeight;

    public MapGenerator (int row, int col) {
        brickMap = new boolean[row][col];
        for(int i = 0; i<brickMap.length; i++) {
            for(int j =0; j<brickMap[0].length; j++) {
                brickMap[i][j] = true;
            }
        }

        brickWidth = 540/col;
        brickHeight = 150/row;
    }

    /**
     * Purpose: Draws the bricks onto the GUI
     * @param g Default graphics param
     */
    public void draw(Graphics2D g) {
        for(int i = 0; i<brickMap.length; i++) {
            for(int j =0; j<brickMap[0].length; j++) {
                if(brickMap[i][j]) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    // this is just to show separate brick, game can still run without it
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    /**
     * Purpose: When the ball hits a brick, the brick will be removed
     * @param row Row where brick lies
     * @param col Column where brick lies
     */
    public void setBrickValue(int row, int col) {
        brickMap[row][col] = false;
    }
}