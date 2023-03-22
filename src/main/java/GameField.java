import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GameField extends JPanel implements KeyListener {
    private static final int labyrinthWidth = 12;
    private static final int labyrinthHeight = 12;
    private static final int[][] labyrinthArray = new int[labyrinthWidth][labyrinthHeight];
    private static final int[][] labyrinthRightWalls = new int[labyrinthWidth][labyrinthHeight];
    private static final int[][] labyrinthBottomWalls = new int[labyrinthWidth][labyrinthHeight];
    private static int personX = 0;
    private static int personY = 0;

    public GameField(){
        setBackground(Color.GREEN);
        addKeyListener(this);
        generateLabyrinth();
        setFocusable(true);
    }

    private void fillArray(){
        for(int i = 0; i < labyrinthRightWalls.length; i++){
            for(int j = 0; j < labyrinthRightWalls[0].length; j++){
                labyrinthRightWalls[i][j] = 0;
            }
        }
        for(int i = 0; i < labyrinthBottomWalls.length; i++){
            for(int j = 0; j < labyrinthBottomWalls[0].length; j++){
                labyrinthBottomWalls[i][j] = 0;
            }
        }
    }

    //Calculate unique set for horizontal walls
    private int calculateUniqueSet(int row, int num) {
        int countUniqSet = 0;
        for (int i = 0; i < labyrinthArray[0].length; i++) {
            if (labyrinthArray[row][i] == num) {
                countUniqSet++;
            }
        }
        return countUniqSet;
    }
    private void checkedHorizontalWalls(int row){
        for (int i = 0; i < labyrinthArray[row].length; i++) {
            if (calculateHorizontalWalls(labyrinthArray[row][i], row) == 0) {
                labyrinthBottomWalls[row][i] = 0;
            }
        }
    }

    private int calculateHorizontalWalls(int num, int row) {
        int countHorizontalWalls = 0;
        for (int i = 0; i < labyrinthArray[0].length; i++) {
            if (labyrinthArray[row][i] == num && labyrinthBottomWalls[row][i] == 0) {
                countHorizontalWalls++;
            }
        }
        return countHorizontalWalls;
    }

    public void generateLabyrinth(){
        fillArray();

        //UniqSet
        for(int j = 0; j < labyrinthArray[0].length; j++){
            labyrinthArray[0][j] = j + 1;
        }

        for(int i = 0; i < labyrinthArray.length - 1; i++){

            //Add vertical walls
            for(int j = 0; j < labyrinthArray[0].length - 1; j++){
                boolean flag = new Random().nextBoolean();

                if(flag || labyrinthArray[i][j] == labyrinthArray[i][j+1]){
                    labyrinthRightWalls[i][j] = 1;
                }
                else{
                    labyrinthArray[i][j+1] = labyrinthArray[i][j];
                }
            }

            //Add last vertical walls
            labyrinthRightWalls[i][labyrinthArray[0].length - 1] = 1;

            //Add horizontal walls
            for(int j = 0; j < labyrinthArray[0].length; j++){
                boolean flag = new Random().nextBoolean();

                if(flag && calculateUniqueSet(i, labyrinthArray[i][j]) > 1){
                    labyrinthBottomWalls[i][j] = 1;
                }
            }

            checkedHorizontalWalls(i);

            //Copy array
            System.arraycopy(labyrinthArray[i], 0, labyrinthArray[i + 1], 0, labyrinthArray[0].length);

            if(i != labyrinthArray.length - 2){
                int uniqValue = (i+1)*labyrinthArray[0].length + 1;

                for(int j = 0; j < labyrinthBottomWalls[0].length; j++){
                    if(labyrinthBottomWalls[i][j] == 1){
                        labyrinthArray[i+1][j] = uniqValue;
                        uniqValue++;
                    }
                }
            }
        }

        //Copy vertical walls in last row
        System.arraycopy(labyrinthRightWalls[labyrinthArray.length - 2], 0, labyrinthRightWalls[labyrinthArray.length - 1], 0, labyrinthArray[0].length);

        for(int i = 0; i < labyrinthArray[0].length - 1; i++){
            labyrinthBottomWalls[labyrinthArray.length - 1][i] = 1;

            if(labyrinthArray[labyrinthArray.length - 1][i] != labyrinthArray[labyrinthArray.length - 1][i+1]){
                labyrinthRightWalls[labyrinthArray.length - 1][i] = 0;
                labyrinthArray[labyrinthArray.length - 1][i+1] = labyrinthArray[labyrinthArray.length - 1][i];
            }
        }

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);

        //QR-code image
        Image qr_code = new ImageIcon("src/main/resources/qr-code.png").getImage();
        g.drawImage(qr_code, 900, 30, 250, 250, null);

        g.setFont(new Font("Calibri", Font.BOLD, 28));
        g.drawString("Twitter: The 16s", 930, 330);

        Image qr_code2 = new ImageIcon("src/main/resources/qr-code2.png").getImage();
        g.drawImage(qr_code2, 900, 400, 250, 250, null);
        g.drawString("Discord: The 16s", 930, 700);

        //Create left and top border
        for(int i = 0; i < labyrinthArray[0].length; i++){
            g.fillRect(30, (i*60)+30, 4, 60);
        }

        for(int i = 0; i < labyrinthArray[0].length; i++){
            g.fillRect((i*60)+30, 30, 60, 4);
        }

        //Create labyrinth
        for(int i = 0; i < labyrinthRightWalls.length; i++){
            for(int j = 0; j < labyrinthRightWalls[0].length; j++){
                if(labyrinthRightWalls[i][j] == 1){
                    g.fillRect(((j+1)*60)+30, (i*60)+30, 4, 60);
                }
            }
        }

        for(int i = 0; i < labyrinthBottomWalls.length; i++){
            for(int j = 0; j < labyrinthBottomWalls[0].length; j++){
                if(labyrinthBottomWalls[i][j] == 1){
                    g.fillRect((j*60)+30, ((i+1)*60)+30, 60, 4);
                }
            }
        }

        //Draw person
        Image person = new ImageIcon("src/main/resources/person.png").getImage();
        g.drawImage(person, (personX*60)+30+9, (personY*60)+30+9, 45, 45, null);

        if(personX == labyrinthWidth - 1 && personY == labyrinthHeight - 1){
            removeKeyListener(this);
            g.setColor(new Color(0, 0,0,230));
            g.fillRect(0,0, 1280, 835);
            g.setFont(new Font("Calibri", Font.BOLD, 128));
            g.setColor(Color.WHITE);
            g.drawString("You win!", 390, 420);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                if ((GameField.personX < GameField.labyrinthWidth - 1) && (labyrinthRightWalls[personY][personX] != 1)) {
                    GameField.personX++;
                }
            }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                if ((GameField.personX > 0) && (labyrinthRightWalls[personY][personX - 1] != 1)) {
                    GameField.personX--;
                }
            }
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                if ((GameField.personY < GameField.labyrinthHeight - 1) && (labyrinthBottomWalls[personY][personX] != 1)) {
                    GameField.personY++;
                }
            }
            case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                if ((GameField.personY > 0) && (labyrinthBottomWalls[personY - 1][personX] != 1)) {
                    GameField.personY--;
                }
            }
            default -> System.out.println("Error");
        }

        audio();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyReleased(KeyEvent e) {}

    public void audio(){
        try {
            File soundFile = new File("src/main/resources/audio.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.setFramePosition(0);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e){
            throw new RuntimeException(e);
        }
    }
}

