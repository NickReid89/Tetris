/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TetrisGame;

import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author McAwesome
 */
public abstract class Background extends javax.swing.JPanel implements java.awt.event.KeyListener {

    boolean gameOver = false;
    int[][] occupied = new int[10][20];
    boolean leftPressed = false;
    boolean rightPressed = false;
    boolean downPressed = false;
    boolean spacePressed = false;
    int score = 0;  // score
    int lineCompleted = 0;   // number of lines completed
    int level = 0;
    static int NumOfPlays = 0;
    static boolean paused;
    javax.swing.JLabel lblScore = new javax.swing.JLabel("Score : 0");
    javax.swing.JLabel lblLevel = new javax.swing.JLabel("Level : 0");
    //This creates the JFrame form and sets the back ground to dark gray.
    // [two tokens] [ four rotations ] [ four cells]
    // [seven tokens] [ four rotations ] [ four cells]
    static int[][][] xRotationArray = {
        {{0, 0, 1, 2}, {0, 0, 0, 1}, {2, 0, 1, 2}, {0, 1, 1, 1}}, // token number 0
        {{0, 0, 1, 1}, {1, 2, 0, 1}, {0, 0, 1, 1}, {1, 2, 0, 1}}, // token number 1
        {{1, 1, 0, 0}, {0, 1, 1, 2}, {1, 1, 0, 0}, {0, 1, 1, 2}}, // token number 2
        {{0, 1, 2, 2}, {0, 1, 0, 0}, {0, 0, 1, 2}, {1, 1, 0, 1}}, // token number 3
        {{1, 0, 1, 2}, {1, 0, 1, 1}, {0, 1, 1, 2}, {0, 0, 1, 0}}, // token number 4
        {{0, 1, 0, 1}, {0, 1, 0, 1}, {0, 1, 0, 1}, {0, 1, 0, 1}}, // token number 5
        {{0, 1, 2, 3}, {0, 0, 0, 0}, {0, 1, 2, 3}, {0, 0, 0, 0}} // token number 6
    };
    static int[][][] yRotationArray = {
        {{0, 1, 0, 0}, {0, 1, 2, 2}, {0, 1, 1, 1}, {0, 0, 1, 2}}, // token number 0
        {{0, 1, 1, 2}, {0, 0, 1, 1}, {0, 1, 1, 2}, {0, 0, 1, 1}}, // token number 1
        {{0, 1, 1, 2}, {0, 0, 1, 1}, {0, 1, 1, 2}, {0, 0, 1, 1}}, // token number 2
        {{0, 0, 0, 1}, {0, 0, 1, 2}, {0, 1, 1, 1}, {0, 1, 2, 2}}, // token number 3
        {{0, 1, 1, 1}, {0, 1, 1, 2}, {0, 0, 1, 0}, {0, 1, 1, 2}}, // token number 4
        {{0, 0, 1, 1}, {0, 0, 1, 1}, {0, 0, 1, 1}, {0, 0, 1, 1}}, // token number 5
        {{0, 0, 0, 0}, {0, 1, 2, 3}, {0, 0, 0, 0}, {0, 1, 2, 3}} // token number 6
    };

    public void init() {
        this.setPreferredSize(new java.awt.Dimension(400, 480));
        this.setBackground(java.awt.Color.darkGray);

        this.setLayout(null); //abolute coordinate system.

        lblScore.setBounds(300, 50, 100, 30);
        lblScore.setForeground(Color.yellow);
        this.add(lblScore);

        lblLevel.setBounds(300, 100, 100, 30);
        lblLevel.setForeground(Color.yellow);
        this.add(lblLevel);


    }

    public void DrawCell(int x, int y) {

        occupied[x][y] = 1;
    }

    //Code to erase a cell
    public void EraseCell(int x, int y) {
        occupied[x][y] = 0;
    }

    public void eraseToken(int x, int y, int[] xArray, int[] yArray) {
        for (int i = 0; i < 4; i++) {
            EraseCell(x + xArray[i], y + yArray[i]);
        }
    }

    public void drawToken(int x, int y, int[] xArray, int[] yArray) {
        for (int i = 0; i < 4; i++) {
            DrawCell(x + xArray[i], y + yArray[i]);
        }
    }

    public void paint(java.awt.Graphics gr) {
        super.paint(gr);

        for (int x = 0; x < occupied.length; x++) {
            for (int y = 0; y < occupied[0].length; y++) {
                if (occupied[x][y] == 1) {
                    // draw cell
                    gr.setColor(java.awt.Color.BLACK);
                    gr.fillRect(x * 24, y * 24, 24, 24);
                    gr.setColor(java.awt.Color.RED);
                    gr.fillRect(x * 24 + 1, y * 24 + 1, 22, 22);
                } else {
                    // erase cell
                    gr.setColor(java.awt.Color.BLACK);
                    gr.fillRect(x * 24, y * 24, 24, 24);
                }
            }
        }
    }

    public boolean isValidPosition(int x, int y, int tokenNumber, int rotationNumber) {
        int[] xArray = xRotationArray[tokenNumber][rotationNumber];
        int[] yArray = yRotationArray[tokenNumber][rotationNumber];

        for (int i = 0; i < 4; i++) // loop over the four cells
        {
            int xCell = x + xArray[i];
            int yCell = y + yArray[i];

            // range check
            if (xCell < 0) {
                return false;
            }

            if (xCell >= 10) {
                return false;
            }
            if (yCell < 0) {
                return false;
            }
            if (yCell >= 20) {
                return false;
            }

            // occupancy check
            if (occupied[xCell][yCell] == 1) {
                return false;
            }
        }
        return true;
    }

    public void randomTokenTest() {
        try {
            Thread.sleep(1000);
        } catch (Exception ignore) {
        }

        int x, y, tokenNumber, rotationNumber;

        while (true) // loop until position is valid
        {
            x = (int) (10 * Math.random());    // random x: 0 to 9
            y = (int) (20 * Math.random());    // random y: 0 to 19

            tokenNumber = (int) (7 * Math.random());
            rotationNumber = (int) (4 * Math.random());

            if (isValidPosition(x, y, tokenNumber, rotationNumber)) {
                break;
            }

            int[] xArray = xRotationArray[tokenNumber][rotationNumber];
            int[] yArray = yRotationArray[tokenNumber][rotationNumber];

            drawToken(x, y, xArray, yArray);
            repaint();
        }
    }

    public void clearCompleteRow(int[] completed) {
        // erase
        for (int i = 0; i < completed.length; i++) {
            if (completed[i] == 1) {
                for (int x = 0; x < 10; x++) {
                    occupied[x][i] = 0;
                }
            }
        }

        repaint();
    }

    public void shiftDown(int[] completed) {
        for (int row = 0; row < completed.length; row++) {
            if (completed[row] == 1) {
                for (int y = row; y >= 1; y--) {
                    for (int x = 0; x < 10; x++) {
                        occupied[x][y] = occupied[x][y - 1];
                    }
                }
            }
        }
    }

    public void checkRowCompletion() {
        int[] complete = new int[20];
        for (int y = 0; y < 20; y++) // 20 rows
        {
            int filledCell = 0;
            for (int x = 0; x < 10; x++) // 10 columns
            {
                if (occupied[x][y] == 1) {
                    filledCell++;
                }
                if (filledCell == 10) // row completed
                {
                    complete[y] = 1;
                }
            }
        }

        clearCompleteRow(complete);

        shiftDown(complete);

        addScore(complete);
    }

    void addScore(int[] complete) {
        int bonus = 10;  // score for the first completed line
        for (int row = 0; row < complete.length; row++) {
            if (complete[row] == 1) {
                lineCompleted += 1;
                score += bonus;
                bonus *= 2;  // double the bonus for every additional line
            }
        }

        // advance level for every 3 completed lines
        level = lineCompleted / 3;
        if (level > 30) {
            lineCompleted = 0;
            level = 0;
        }  // MAX LEVEL

        lblScore.setText("SCORE : " + score);
        lblLevel.setText("LEVEL : " + level);
    }

    public void addFallingToken() {
        int x = 5, y = 0;
        int tokenNumber, rotationNumber;


        tokenNumber = (int) (7 * Math.random());
        rotationNumber = (int) (4 * Math.random());



        int[] xArray = xRotationArray[tokenNumber][rotationNumber];
        int[] yArray = yRotationArray[tokenNumber][rotationNumber];

        if (!isValidPosition(x, y, tokenNumber, rotationNumber)) {
            gameOver = true;
            drawToken(x, y, xArray, yArray);
            repaint();
            return;
        }

        drawToken(x, y, xArray, yArray);
        repaint();

        int delay = 50;  // mini second
        int frame = 0;
        boolean reachFloor = false;
        while (!reachFloor) {
            try {
                Thread.sleep(delay);
            } catch (Exception ignore) {
            }
            eraseToken(x, y, xArray, yArray);

            // add keyboard control
            if (leftPressed && isValidPosition(x - 1, y, tokenNumber, rotationNumber)) {
                x -= 1;
            }
            if (rightPressed && isValidPosition(x + 1, y, tokenNumber, rotationNumber)) {
                x += 1;
            }
            if (downPressed && isValidPosition(x, y + 1, tokenNumber, rotationNumber)) {
                y += 1;
            }
            if (spacePressed && isValidPosition(x, y, tokenNumber, (rotationNumber + 1) % 4)) {
                rotationNumber = (rotationNumber + 1) % 4;
                xArray = xRotationArray[tokenNumber][rotationNumber];
                yArray = yRotationArray[tokenNumber][rotationNumber];
                spacePressed = false;
            }
            // This will be the fall rate as the level gets higher this will decrease causing faster falls.
            int fallRate = 31 - level;
            if (frame % fallRate == 0) {
                y += 1;  // fall for every 30 frame
            }
            if (!isValidPosition(x, y, tokenNumber, rotationNumber)) // reached floor
            {
                reachFloor = true;
                y -= 1;  // restore position
            }
            drawToken(x, y, xArray, yArray);
            repaint();
            frame++;
        }

    }

    public void printGameOver() {
        JOptionPane.showMessageDialog(null, "Game over!");

        repaint();
    }
    // must implement this method for KeyListener

    public void keyPressed(java.awt.event.KeyEvent event) {

//    System.out.println(event);
        if (event.getKeyCode() == 37) // left arrow
        {
            leftPressed = true;
        }
        if (event.getKeyCode() == 39) // right arrow
        {
            rightPressed = true;
        }
        if (event.getKeyCode() == 40) // down arrow
        {
            downPressed = true;
        }
        if (event.getKeyCode() == 32) // space
        {
            spacePressed = true;
        }
        if (event.getKeyCode() == 80){
            
            paused = true;
        }
        if (event.getKeyCode() == 67){
            paused = false;
        }

    }
    // must implements this method for KeyListener

    public void keyReleased(java.awt.event.KeyEvent event) {
//    System.out.println(event);

        if (event.getKeyCode() == 37) // left arrow
        {
            leftPressed = false;
        }
        if (event.getKeyCode() == 39) // right arrow
        {
            rightPressed = false;
        }
        if (event.getKeyCode() == 40) // down arrow
        {
            downPressed = false;
        }
        if (event.getKeyCode() == 32) // space
        {
            spacePressed = false;
        }

    }

    // must implements this method for KeyListener
    public void keyTyped(java.awt.event.KeyEvent event) {
        System.out.println(event);
    }

    public static void main(String[] args) throws Exception {
        javax.swing.JFrame window = new javax.swing.JFrame("Nickolas Reid - Tetris");
        window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        Background tetris = new Background() {
        };
        tetris.init();

        window.add(tetris);
        window.pack();
        window.setVisible(true);

        int result = JOptionPane.showConfirmDialog(null, "Are you ready to play tetris?", "Nickolas Reid - Tetris", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {

            try {
                Thread.sleep(1000);
            } catch (Exception ignore) {
            }
            window.addKeyListener(tetris);  // listen to keyboard event
            tetris.gameOver = false;
            while (!tetris.gameOver) {
                tetris.addFallingToken();
                tetris.checkRowCompletion();
            }

            tetris.printGameOver();
            NumOfPlays++;
        }
        else{
            System.exit(0);
        }
    }
}
