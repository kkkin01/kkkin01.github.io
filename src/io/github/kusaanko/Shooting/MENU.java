package io.github.kusaanko.Shooting;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class MENU {
	public static ShootingFrame shootingFrame;
	
	
	public static void MENU_draw(){
		shootingFrame = new ShootingFrame();
		Graphics gra = shootingFrame.panel.image.getGraphics();
		gra.setColor(Color.BLACK);
        Font font = new Font("SansSerif", Font.PLAIN, 30);
        gra.setFont(font);
        FontMetrics metrics = gra.getFontMetrics(font);
        gra.drawString("�V���[�e�B���O�Q�[��", 250 - (metrics.stringWidth("�V���[�e�B���O�Q�[��") / 2), 100);
        font = new Font("SansSerif", Font.PLAIN, 20);
        gra.setFont(font);
        metrics = gra.getFontMetrics(font);
        gra.drawString("Press SPACE to Start", 250 - (metrics.stringWidth("Press SPACE to Start") / 2), 160);
        gra.drawString("������@", 250 - (metrics.stringWidth("������@") / 2), 180);
        
	}

}
