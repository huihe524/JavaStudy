package com.kuang.snake;

import javax.swing.*;

public class StartGame {
    //游戏的主启动类
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);  //窗口大小不可变
        frame.setBounds(10,10,900,720);

        //正常的游戏节目都应该在面板上
        frame.add(new GamePanel());

        frame.setVisible(true);
    }
}
