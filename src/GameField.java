import com.sun.deploy.security.SelectableSecurityManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int size = 320;
    private final int dotSize = 20; // размер пикселя
    private final int allDots = 400; // сколько единиц может поместится на поле
    private Image dot; // имидж под игровую ячейку
    private Image apple; // имидж яблока
    private int appleX; // x позиция яблока
    private int appleY; // y позиция яблока
    private int[] x = new int[allDots];
    private int[] y = new int[allDots];
    private int dots; // размер змейки
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    public GameField() {
        setBackground(Color.BLACK);
        loadImages();
        initGame();
        addKeyListener( new FieldKeyListener());
        setFocusable(true);
    }

    /**
     * Инициализация начала игры
     */
    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots;i++){
            x[i] = 60 -i*dotSize; //Начальное значение змейки по оси у
            y[i] = 60; //Начальное значение змейки по оси у
        }
        timer = new Timer(250,this);
        timer.start();
        createApple();
    }

    public void createApple(){
        appleX = new Random().nextInt(20)*dotSize;
        appleY = new Random().nextInt(20)*dotSize;
    }

    public void loadImages(){
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("snake.png");
        dot = iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){
            g.drawImage(apple,appleX,appleY,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);
            }
        } else {
          String str = "YOU LOSE";
         // Font f = new Font("Arial",14,Font.BOLD);
          g.setColor(Color.WHITE);
         // g.setFont(f);
          g.drawString(str,125,size/2);
        }
    }

    /**
     * Движение змейки
     */
    public void move(){
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(left){
            x[0] -= dotSize;
        }
        if (right) {
            x[0] += dotSize;
        }
        if (up){
            y[0] -= dotSize;
        }
        if (down) {
            y[0] += dotSize;
        }
    }

    /**
     * Проверяем не наткнулись ли мы на яблоко
     */
    public void checkApple(){
        if (x [0] == appleX && y[0] == appleY){
            dots++;
            createApple();
        }
    }

    /**
     * Проверка игрового поля не удалились ли мы друг о друга и не вышли ли мы за поля
     */
    public void checkCollisions(){
        for (int i = dots; i > 0 ; i--) {
            if (i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
            if (x[0]>size){
                inGame = false;
            }
            if (x[0]<0){
                inGame = false;
            }
            if (y[0]>size){
                inGame = false;
            }
            if (y[0]<0){
                inGame = false;
            }
        }
    }

    /**
     * Таймер каждые 250 сек
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame){
            checkApple();
            checkCollisions();
            move();
        }
        repaint(); // перерисовка поля
    }

    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && ! right){
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && ! left){
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && ! down){
                up = true;
                right = false;
                left = false;
            }
            if (key == KeyEvent.VK_DOWN && ! up){
                down = true;
                right = false;
                left = false;
            }
        }
    }
}
