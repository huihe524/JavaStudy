package com.kuang.snake;

import com.kuang.lesson05.JPanelDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

//游戏的面板
public class GamePanel extends JPanel implements KeyListener,ActionListener{
    //定义蛇的数据结构
    int length;  //蛇的长度
    int[] snakeX = new int[600];  //蛇的x坐标25*25
    int[] snakeY = new int[500];  //蛇的y坐标25*25
    String fx;  //初始方向向右
    //游戏当前的状态：开始、停止
    boolean isStart = false;  //默认不开始
    boolean isFail = false;  //失败状态

    //事物的坐标
    int foodx;
    int foody;
    Random random = new Random();

    int score;  //成绩

    //定时器  以ms为单位   1000ms = 1s
    Timer timer = new Timer(100,this);  //100毫秒刷新（执行）一次


    //构造器
    public GamePanel(){
        init();
        //获得焦点和键盘事件
        this.setFocusable(true);  //获得焦点事件
        this.addKeyListener(this);  //获得键盘监听事件
        timer.start();  //游戏一开定时器就启动
    }

    //初始化方法
    public void init(){
        //把食物随机分布在屏幕上
        foodx = 25 + 25*random.nextInt(34); //850/25=34
        foody = 75 + 25*random.nextInt(24);

        length = 3;
        snakeX[0] = 100 ; snakeY[0] = 100 ;  //head的坐标
        snakeX[1] = 75 ; snakeY[1] = 100 ;  //第一节body的坐标
        snakeX[2] = 50 ; snakeY[2] = 100 ;  //第二节body的坐标
        fx = "R";
        score = 0;
    }

    //绘制面板，我们游戏中的所有东西都是用这个画笔来画
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  //清屏
        //绘制静态的面板
        this.setBackground(Color.white);
        Data.header.paintIcon(this,g,25,11);  //头部广告栏
        g.fillRect(25,75,850,600);  //默认的游戏界面

        //画积分
        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑",Font.BOLD,18));
        g.drawString("长度"+length,750,35);
        g.drawString("分数"+score,750,55);

        //画食物
        Data.food.paintIcon(this,g,foodx,foody);

        //把小蛇画上去
        if(fx.equals("R")){
            Data.right.paintIcon(this,g,snakeX[0],snakeY[0]);  //蛇头初始化向右
        }else if(fx.equals("L")){
            Data.left.paintIcon(this,g,snakeX[0],snakeY[0]);  //蛇头初始化向右
        }else if(fx.equals("U")){
            Data.up.paintIcon(this,g,snakeX[0],snakeY[0]);  //蛇头初始化向右
        }else if(fx.equals("D")){
            Data.down.paintIcon(this,g,snakeX[0],snakeY[0]);  //蛇头初始化向右
        }

        for (int i = 1; i < length; i++) {
            Data.body.paintIcon(this,g,snakeX[i],snakeY[i]);   //第i节body的坐标
        }

        //游戏状态
        if(isStart==false){
            g.setColor(Color.white);
            //设置字体
            g.setFont(new Font("微软雅黑",Font.BOLD,40));
            g.drawString("按下空格开始游戏",300,300);
        }
        Data.body.paintIcon(this,g,snakeX[1],snakeY[1]);   //第一节body的坐标
        Data.body.paintIcon(this,g,snakeX[2],snakeY[2]);   //第二节body的坐标

        if(isFail){
            g.setFont(new Font("微软雅黑",Font.BOLD,40));
            g.setColor(Color.red);
            g.drawString("失败，按下空格重新开始",300,300);
        }
    }

     //事件监听--需要通过固定的时间来刷新，
     @Override
     public void actionPerformed(ActionEvent e) {
        if (isStart==true&&isFail==false){  //如果游戏是开始状态，就让小蛇动起来

            //吃食物
            if (snakeX[0]==foodx&&snakeY[0]==foody){
                length++;  //长度加一
                score += 10;//分数+10
                //再次生成食物
                foodx = 25 + 25 * random.nextInt(34); //850/25=34
                foody = 75 + 25 * random.nextInt(24);
            }

            //移动
            for (int i = length-1; i > 0 ; i--) {  //后一节移到前一节的位置
                snakeX[i] = snakeX[i-1];  //向前移一节
                snakeY[i] = snakeY[i-1];
            }

            //走向
            if (fx.equals("R")){
                snakeX[0] = snakeX[0] + 25;
                if (snakeX[0] > 850) snakeX[0] = 25;  //边界判断
            }else if (fx.equals("L")){
                snakeX[0] = snakeX[0] - 25;
                if (snakeX[0] < 25) snakeX[0] = 850;  //边界判断
            }else if (fx.equals("U")){
                snakeY[0] = snakeY[0] - 25;
                if (snakeY[0]<75) snakeY[0] = 650;
            }else if (fx.equals("D")){
                snakeY[0] = snakeY[0] + 25;
                if (snakeY[0]> 650) snakeY[0] = 75;
            }

            //失败判定，撞到自己就失败
            for (int i = 1; i <length ; i++) {
                if(snakeX[0]==snakeX[i]&&snakeY[0]==snakeY[i]){
                    isFail = true;
                }
            }
            repaint();  //重画页面
        }
        timer.start();  //定时器开启
     }

    //键盘监听事件
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();  //获得键盘按键是哪一个
        if (keyCode == KeyEvent.VK_SPACE){  //如果按下空格
            if (isFail){
                init();
                //重新开始
                isFail = false;
            }else{
                isStart = !isStart;  //取反
            }
            repaint();
        }
        //小蛇移动
        if (keyCode == KeyEvent.VK_UP){
            fx = "U";
        }else if (keyCode == KeyEvent.VK_DOWN){
            fx = "D";
        }else if (keyCode == KeyEvent.VK_LEFT){
            fx = "L";
        }else if (keyCode == KeyEvent.VK_RIGHT){
            fx = "R";
        }

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}