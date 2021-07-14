package io.github.kusaanko.Shooting;

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.net.MalformedURLException;
import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Shooting {
    public static ShootingFrame shootingFrame;
    public static boolean loop;
    public static void main(String[] args) {
        shootingFrame = new ShootingFrame();
        loop = true;
        int bdx=0,bdy=0;

        Graphics gra = shootingFrame.panel.image.getGraphics();
        

        //FPS
        long startTime;
        long fpsTime = 0;
        int fps = 30;
        int FPS = 0;
        int FPSCount = 0;
        
        Image jiki = null, ene1 = null, bd1=null,bd2=null,bd3=null,bd4=null,pltm=null,entm=null;
        EnumShootingScreen screen = EnumShootingScreen.MENU;

        //GAME
        int bulletInterval = 0;
        int score = 0;
        int level = 0;
        long levelTimer = 0;
        ArrayList<Bullet> bullets_player = new ArrayList<>();
        ArrayList<Bullet> bullets_enemy = new ArrayList<>();
        ArrayList<Enemy> enemies = new ArrayList<>();
        Player player = new Player();
        Random random = new Random();
        
     // 音楽の生成
        Clip BGM1 = createClip(new File("sound\\赤より紅い夢.wav"));
        Clip BGM2 = createClip(new File("sound\\神々が恋した幻想郷.wav"));
        Clip plst = createClip(new File("sound\\se_plst00.wav"));
        Clip plde = createClip(new File("sound\\se_pldead00.wav"));
        Clip ende = createClip(new File("sound\\se_enep00.wav"));
        Clip seok = createClip(new File("sound\\se_ok00.wav"));
        //画像生成
        try {
    		jiki = ImageIO.read(new File("img\\reimu.png"));
    		ene1 = ImageIO.read(new File("img\\enemy1.png"));
    		bd1 = ImageIO.read(new File("img\\cdbg04b.png"));
    		bd2 = ImageIO.read(new File("img\\stg3bg1-1.png"));
    		bd3 = ImageIO.read(new File("img\\stg3bg1-2.png"));
    		bd4 = ImageIO.read(new File("img\\stg3bg2.png"));
    		pltm = ImageIO.read(new File("img\\pl00.png"));
    		entm = ImageIO.read(new File("img\\etama.png"));
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }

        while(loop) {
            if((System.currentTimeMillis() - fpsTime) >= 1000) {
                fpsTime = System.currentTimeMillis();
                FPS = FPSCount;
                FPSCount = 0;
            }
            FPSCount++;
            startTime = System.currentTimeMillis();

            gra.setColor(Color.WHITE);
            gra.fillRect(0, 0, 500, 500);

            switch (screen) {
                case MENU:
                	BGM1.start();
                    BGM1.loop(Clip.LOOP_CONTINUOUSLY);
                    gra.setColor(Color.BLACK);
                    Font font = new Font("SansSerif", Font.PLAIN, 30);
                    gra.setFont(font);
                    FontMetrics metrics = gra.getFontMetrics(font);
                    gra.drawString("シューティングゲーム", 250 - (metrics.stringWidth("シューティングゲーム") / 2), 100);
                    font = new Font("SansSerif", Font.PLAIN, 20);
                    gra.setFont(font);
                    metrics = gra.getFontMetrics(font);
                    gra.drawString("Press SPACE to Start", 250 - (metrics.stringWidth("Press SPACE to Start") / 2), 160);
                    gra.drawString("操作方法", 250 - (metrics.stringWidth("操作方法") / 2), 180);
                    gra.drawString("矢印キーで移動　Zで弾発射　します", 250 - (metrics.stringWidth("矢印キーで移動　Zで弾発射　します") / 2), 220);
                    if(Keyboard.isKeyPressed(KeyEvent.VK_SPACE)) {
                    	BGM1.stop();
                    	seok.start();
                        screen = EnumShootingScreen.GAME;
                        bullets_player = new ArrayList<>();
                        bullets_enemy = new ArrayList<>();
                        enemies = new ArrayList<>();
                        score = 0;
                        level = 0; //スタート画面で初期化
                    }
                    break;
                case GAME:
                	gra.drawImage(bd4, bdx, bdy,null);gra.drawImage(bd4, bdx, bdy-500,null);
                	gra.drawImage(bd3, bdx, bdy+250,null);gra.drawImage(bd2, bdx+250, bdy+250,null);
                	gra.drawImage(bd3, bdx, bdy,null);  gra.drawImage(bd2, bdx+250, bdy,null);
                	gra.drawImage(bd3,bdx, bdy-250,null);gra.drawImage(bd2,bdx+250, bdy-250,null);
                	gra.drawImage(bd3,bdx, bdy-500,null);gra.drawImage(bd2,bdx+250, bdy-500,null);
                	
                	if(bdy==500) {
                		bdy=0;
                	}
                	bdy++;
                     
                	
                    BGM2.start(); 
                	BGM2.loop(Clip.LOOP_CONTINUOUSLY);
                	
                    if(System.currentTimeMillis() - levelTimer > 10 * 1000) {
                        levelTimer = System.currentTimeMillis();
                        level++;
                    }
                    
                    gra.drawImage(jiki, player.x, player.y,null);
                    gra.setColor(Color.BLUE);  //player表示
                    

                    for (int i = 0; i < bullets_player.size(); i++) {
                        Bullet bullet = bullets_player.get(i);
                        gra.drawImage(pltm,bullet.x, bullet.y, null);
                        bullet.y -= 10;
                        if (bullet.y < 0) { //画面外
                            bullets_player.remove(i);
                            i--;
                        }

                        for (int l = 0; l < enemies.size(); l++) { //敵当たり判定
                            Enemy enemy = enemies.get(l);
                            if(bullet.x>=enemy.x&&bullet.x<=enemy.x+30&&
                            bullet.y>=enemy.y&&bullet.y<=enemy.y+30) {
                            	enemy.HP --;
                            	bullets_player.remove(i);
                            	enemy.y--;
                            	if (enemy.HP == 0) {
                            	ende.stop();
                        		ende.flush();
                        		ende.setFramePosition(0);
                            	ende.start();
                                enemies.remove(l);
                                score += 10;
                            	}
                            }
                        }
                    }

                    gra.setColor(Color.RED);
                    for (int i = 0; i < enemies.size(); i++) {
                        Enemy enemy = enemies.get(i);
                        gra.drawImage(ene1, enemy.x, enemy.y, null);
                        enemy.y += 2;
                        if(enemy.y > 500) {
                            enemies.remove(i);
                            i--;
                        }
                        if(random.nextInt(level<50?80 - level:30)==1) bullets_enemy.add(new Bullet(enemy.x + 12, enemy.y));
                        if((enemy.x>=player.x+20&&enemy.x<=player.x+30&& //当たり判定
                                enemy.y>=player.y+10&&enemy.y<=player.y+30)||
                            (enemy.x+30>=player.x+20&&enemy.x+30<=player.x+30&&
                                    enemy.y+20>=player.y+10&&enemy.y+20<=player.y+30)) {
                        	player.HP--;
                        	plde.stop();
                    		plde.flush();
                    		plde.setFramePosition(0);
                        	plde.start();
                        	player.x=235;
                        	player.y=430;
                        	if(player.HP==0) {
                        	BGM2.stop();
                            screen = EnumShootingScreen.GAME_OVER;
                            score += (level - 1) * 100;
                        	}
                        }
                    }
                    if(random.nextInt(level<10?30 - level:10)==1) enemies.add(new Enemy(random.nextInt(470), 0, 5));

                    for (int i = 0; i < bullets_enemy.size(); i++) { // 画面外
                        Bullet bullet = bullets_enemy.get(i);
                        gra.drawImage(entm,bullet.x-10, bullet.y,null);
                        bullet.y += 10;
                        if (bullet.y > 500) {
                            bullets_enemy.remove(i);
                            i--;
                        }
                        if(bullet.x>=player.x+10&&bullet.x<=player.x+30&& //当たり判定
                        bullet.y>=player.y&&bullet.y<=player.y+20) {
                        	player.HP--;
                        	plde.stop();
                    		plde.flush();
                    		plde.setFramePosition(0);
                        	plde.start();
                        	player.x=235;
                        	player.y=430;
                        	if(player.HP==0) {
                        	BGM2.stop();
                            screen = EnumShootingScreen.GAME_OVER;
                            score += (level - 1) * 100;
                        	}
                        }
                    }

                    if(Keyboard.isKeyPressed(KeyEvent.VK_LEFT)&&player.x>-20) player.x-=8; //操作
                    if(Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)&&player.x<457) player.x+=8;
                    if(Keyboard.isKeyPressed(KeyEvent.VK_UP)&&player.y>30) player.y-=8;
                    if(Keyboard.isKeyPressed(KeyEvent.VK_DOWN)&&player.y<430) player.y+=8;

                    if(Keyboard.isKeyPressed(KeyEvent.VK_Z)&&bulletInterval==0) {
                        bullets_player.add(new Bullet(player.x + 12, player.y));
                        plst.stop();
                		plst.flush();
                		plst.setFramePosition(0);
                        plst.start();
                        bulletInterval = 4;
                    }
                    if(bulletInterval>0) bulletInterval--;

                    gra.setColor(Color.BLACK);
                    font = new Font("SansSerif", Font.PLAIN, 20);
                    metrics = gra.getFontMetrics(font);
                    gra.setFont(font);
                    gra.drawString("SCORE:" + score, 470 - metrics.stringWidth("SCORE:" + score), 410);
                    gra.drawString("LEVEL:" + level, 470 - metrics.stringWidth("LEVEL:" + level), 430);
                    gra.setColor(Color.RED);
                    gra.drawString("HP:" + player.HP, 470 - metrics.stringWidth("LEVEL:" + player.HP), 450);

                	
                    break;
                case GAME_OVER:
                    gra.setColor(Color.BLACK);
                    font = new Font("SansSerif", Font.PLAIN, 50);
                    gra.setFont(font);
                    metrics = gra.getFontMetrics(font);
                    gra.drawString("Game Over", 250 - (metrics.stringWidth("Game Over") / 2), 100);
                    font = new Font("SansSerif", Font.PLAIN, 20);
                    gra.setFont(font);
                    metrics = gra.getFontMetrics(font);
                    gra.drawString("Score:"+score, 250 - (metrics.stringWidth("Score:"+score) / 2), 150);
                    gra.drawString("Press ESC to Return Start Screen", 250 - (metrics.stringWidth("Press ESC to Return Start Screen") / 2), 200);
                    if(Keyboard.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                        screen = EnumShootingScreen.MENU;
                    }
                    break;
            }

            gra.setColor(Color.BLACK);
            gra.setFont(new Font("SansSerif", Font.PLAIN, 10));
            gra.drawString(FPS + "FPS", 0, 470);

            shootingFrame.panel.draw();

            try {
                long runTime = System.currentTimeMillis() - startTime;
                if(runTime<(1000 / fps)) {
                    Thread.sleep((1000 / fps) - (runTime));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
	public static Clip createClip(File path) {
		//指定されたURLのオーディオ入力ストリームを取得
		try (AudioInputStream ais = AudioSystem.getAudioInputStream(path)){
			
			//ファイルの形式取得
			AudioFormat af = ais.getFormat();
			
			//単一のオーディオ形式を含む指定した情報からデータラインの情報オブジェクトを構築
			DataLine.Info dataLine = new DataLine.Info(Clip.class,af);
			
			//指定された Line.Info オブジェクトの記述に一致するラインを取得
			Clip c = (Clip)AudioSystem.getLine(dataLine);
			
			//再生準備完了
			c.open(ais);
			
			return c;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return null;
	}
}

