import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 48;

    private final Timer timer;

    private int playerX = 310;

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    public Gameplay() {
        map = new MapGenerator(4, 12);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        int delay = 8;
        timer=new Timer(delay,this);
        timer.start();
    }

    /**
     * Purpose: Renders all the objects and words on the screen
     * @param g Default graphics param
     */
    public void paint(Graphics g) {
        //Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        //Drawing a map (the bricks)
        map.draw((Graphics2D) g);

        //Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        //Scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD, 25));
        g.drawString(""+score, 590,30);

        //Paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        //Ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        //When all bricks are destroyed, player won the game
        if(totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("You Won!", 260,300);

            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press ENTER to Restart", 230,350);
        }

        //When ball is out of bounds, player lost the game
        if(ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("Game Over, Score: "+score, 190,300);

            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230,350);
        }
        g.dispose();
    }

    /**
     * Purpose: Event listener that handles the keyboard controls
     * @param e The key being pressed on the keyboard
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(4, 12);

                repaint();
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public void moveLeft() {
        play = true;
        playerX-=40;
    }

    public void moveRight() {
        play = true;
        playerX+=40;
    }

    /**
     * Purpose: Event listener that handles all the game actions, including the ball hitting the paddle, brick or wall
     * @param e The action that has occured
     */
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play) {
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8))) {
                ballYdir = -ballYdir;
                ballXdir = -2;
            } else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 70, 550, 30, 8))) {
                ballYdir = -ballYdir;
                ballXdir = ballXdir + 1;
            } else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 30, 550, 40, 8))) {
                ballYdir = -ballYdir;
            }

            //Check map collision with the ball
            A: for(int i = 0; i<map.brickMap.length; i++) {
                for(int j =0; j<map.brickMap[0].length; j++) {
                    if(map.brickMap[i][j]) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);

                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(i, j);
                            score++;
                            totalBricks--;

                            //When ball hit right or left of brick
                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            }
                            //When ball hits top or bottom of brick
                            else {
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            if(ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if(ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if(ballposX > 670) {
                ballXdir = -ballXdir;
            }
            repaint();
        }
    }
}