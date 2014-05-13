package planeGame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.neewgame.R;


public class GameLayer extends CCLayer {
	private CCSprite player;
	
	private CCSprite background1, background2, background3;
	
	CCSprite gameOverTitle = CCSprite.sprite("gameover.png");
	
	private CGPoint startPoint, endPoint;
	
	private Random random = new Random();
	
	private List<CCSprite> enemyList = new ArrayList<CCSprite>();
	
	private List<CCSprite> bulletList = new LinkedList<CCSprite>();
	
	private List<CCSprite> enemybulletList = new LinkedList<CCSprite>();
	
	CCLabel scorelabel = CCLabel.makeLabel("分数 ：0", "宋体", 20);
	
	private CGSize winSize = null;
	/**
	 * 记录当前分数
	 */
	private int score = 0;
	
	private CCAnimate enemyAnimate = null;
	private CCSequence enemyDownSeq = null;
	private CCHide hide = null;
	private Context context = null;
	
	public GameLayer() {
		this.setIsTouchEnabled(true);
		this.setIsKeyEnabled(true);
		this.setIsAccelerometerEnabled(true);
		
		context = CCDirector.sharedDirector().getActivity();
	    SoundEngine.sharedEngine().preloadEffect(context, R.raw.game_music);
	    SoundEngine.sharedEngine().playSound(context, R.raw.game_music, true);
		
		winSize = CCDirector.sharedDirector().winSize();
		
		background1 = CCSprite.sprite("background.jpg");
		background2 = CCSprite.sprite("background.jpg");
		background3 = CCSprite.sprite("background.jpg");
		
		
		background3.setAnchorPoint(0, 0);
		background2.setAnchorPoint(0, 0);
		background1.setAnchorPoint(0, 0);
		
		this.addChild(background1);
		this.addChild(background2);
		this.addChild(background3);
		
		player = CCSprite.sprite("hero1.png");
		player.setPosition(100, 100);
		

		CCAnimation animation = CCAnimation.animation("fly", 0.1f);
		animation.addFrame("hero1.png");
		animation.addFrame("hero2.png");
		CCAnimate animate = CCAnimate.action(animation);
		CCRepeatForever repeat = CCRepeatForever.action(animate);
		player.runAction(repeat);
		
		this.addChild(player);	
		scorelabel.setAnchorPoint(0, 0);
		scorelabel.setPosition(0, winSize.height - 30);
		this.addChild(scorelabel);
		
		CCAnimation enemyAnimation = CCAnimation.animation("enemyAni", 0.1f);
		enemyAnimation.addFrame("enemy1_down1.png");
		enemyAnimation.addFrame("enemy1_down2.png");
		enemyAnimation.addFrame("enemy1_down3.png");
		enemyAnimation.addFrame("enemy1_down4.png");
		enemyAnimation.addFrame("enemy1_down5.png");
		enemyAnimate = CCAnimate.action(enemyAnimation);
		hide = CCHide.action();
		enemyDownSeq = CCSequence.actions(enemyAnimate, hide);
		
		CCMenu menu = CCMenu.menu();
		menu.setPosition(0,0);
		CCSprite btn = CCSprite.sprite("backbtn.png");
		CCMenuItem item = CCMenuItemSprite.item(btn, btn, this, "backOperate");
		item.setAnchorPoint(0,0);
		item.setPosition(winSize.width - 50, winSize.height - 50);
		menu.addChild(item);
		this.addChild(menu);
		
		CCTouchDispatcher.sharedDispatcher().addTargetedDelegate(this, 0, true);
		
		this.schedule("backgroundMove", 0.01f);
		this.schedule("shoot", 0.8f);
		this.schedule("enemyShoot", 1.2f);
		this.schedule("addEnemy", 2.5f);
		this.schedule("boom", 1/30);
		this.schedule("bulletJudge", 1/30);
		this.schedule("shootPlayer", 1/30);
	}
	
	public void backOperate(Object sender) {
		exitGame();
	}
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		CGPoint p1 = CGPoint.ccp(event.getX(), event.getY());
		startPoint =  CCDirector.sharedDirector().convertToGL(p1);
		return super.ccTouchesBegan(event);
	}

	@Override
	public boolean ccTouchesCancelled(MotionEvent event) {
		SoundEngine.sharedEngine().pauseSound();
		return super.ccTouchesCancelled(event);
	}
	
	private void moveThePlayer(CGPoint start, CGPoint end) {
		CGPoint offset = CGPoint.ccpSub(end, start);
		CGPoint playPosition = CGPoint.ccpAdd(player.getPosition(), offset);
		if(playPosition.x < 0) {
			playPosition.x = 0;
		}else if(playPosition.x > winSize.width) {
			playPosition.x = winSize.width;
		}
		if(playPosition.y < 0) {
			playPosition.y = 0;
		}else if(playPosition.y > winSize.height) {
			playPosition.y = winSize.height;
		}
		CCMoveTo move = CCMoveTo.action(0.1f, playPosition);
		player.runAction(move);
		
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		CGPoint p1 = CGPoint.ccp(event.getX(), event.getY());
		endPoint =  CCDirector.sharedDirector().convertToGL(p1);
		if(!CGRect.containsPoint(player.getBoundingBox(), endPoint)) {
			moveThePlayer(this.startPoint, this.endPoint);
		}
		return super.ccTouchesEnded(event);
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		CGPoint p1 = CGPoint.ccp(event.getX(), event.getY());
		CGPoint p2 =  CCDirector.sharedDirector().convertToGL(p1);
		if(CGRect.containsPoint(player.getBoundingBox(), p2))
			player.setPosition(p2);
		return super.ccTouchesMoved(event);
	}

	/**
	 * 发射子弹
	 * @param dalat
	 */
	public void shoot(float dalat) {
		CCSprite bullet = CCSprite.sprite("bullet1.png");
		bullet.setPosition(player.getPosition().x, player.getPosition().y + player.getContentSize().height);
		addChild(bullet);
		CCMoveBy move = CCMoveBy.action(5, CGPoint.ccp(0, winSize.getHeight() + 10));
		bullet.runAction(move);
		this.bulletList.add(bullet);
	}
	
	public void enemyShoot(float dalat) {
		CCSprite enemy = this.enemyList.get(random.nextInt(this.enemyList.size()));
		CCSprite bullet = CCSprite.sprite("bullet2.png");
		bullet.setPosition(enemy.getPosition());
		addChild(bullet);
		CCMoveTo moveTo = CCMoveTo.action(8, CGPoint.ccp(player.getPosition().x, -20));
		bullet.runAction(moveTo);
		this.enemybulletList.add(bullet);
	}
	
	
	
	/**
	 * 随机生成敌机
	 * @param dalat
	 */
	public void addEnemy(float dalat) {
		CCSprite enemy = CCSprite.sprite("enemy1.png");
		enemy.setPosition(random.nextInt((int)winSize.getWidth()), winSize.getHeight());
		CGPoint p = CGPoint.ccp(0, 0 - winSize.getHeight() - 20);
		addChild(enemy);
		CCMoveBy move = CCMoveBy.action(20, p);
		enemy.runAction(move);
		this.enemyList.add(enemy);	
	}
	
	/**
	 * 判断主机是否与敌机发生碰撞
	 * @param dalat
	 * @return
	 */
	public void boom(float dalat) { 
		for(int i = 0; i < enemyList.size(); i++) {
			CCSprite enemy = enemyList.get(i);
			if(CGRect.intersects(player.getBoundingBox(), enemy.getBoundingBox())){  
				CCBlink blink = CCBlink.action(1, 3);
				CCHide hide = CCHide.action();
				this.enemyList.remove(i);
				CCSequence seq = CCSequence.actions(blink, hide);
				enemy.runAction(enemyDownSeq);
				player.runAction(seq);
				new Thread(){
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						exitGame();
					}
				}.start();
			}
			if(enemy.getPosition().y < 0) {
				this.enemyList.remove(i);
				this.removeChild(enemy, true);
			}
		}
	}
	
	/**
	 * 判断主机是否被子弹击中
	 * @param dalat
	 */
	public void shootPlayer(float dalat) { 
		for(int i = 0; i < enemybulletList.size(); i++) {
			CCSprite bullet = enemybulletList.get(i);
			if(CGRect.intersects(player.getBoundingBox(), bullet.getBoundingBox())){  
				CCBlink blink = CCBlink.action(1, 3);
				CCHide hide = CCHide.action();
				this.enemybulletList.remove(i);
				CCSequence seq = CCSequence.actions(blink, hide);
				bullet.runAction(hide);
				player.runAction(seq);
				new Thread(){
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						exitGame();
					}
				}.start();
			}
			if(bullet.getPosition().y < 0) {
				this.enemybulletList.remove(i);
				this.removeChild(bullet, true);
			}
		}
	}
	
	/**
	 * 判断子弹是否与敌机发生碰撞
	 * @param dalat
	 */
	public void bulletJudge(float dalat) {
		for(int i = 0; i < enemyList.size(); i++) {
			CCSprite enemy = enemyList.get(i);
			for(int j = 0; j < this.bulletList.size() ; j++) {
				CCSprite bullet = this.bulletList.get(j);
				if(enemy.getPosition().y - bullet.getPosition().y > enemy.getContentSize().height)
					break;
				if(bullet.getPosition().y > winSize.getHeight()) {
					this.bulletList.remove(j);
					this.removeChild(bullet, true);
					continue;
				}
				if(CGRect.intersects(enemy.getBoundingBox(), bullet.getBoundingBox())){  					
					bullet.runAction(hide);
					enemy.runAction(enemyDownSeq);
					this.enemyList.remove(i);
					this.bulletList.remove(j);	
					this.removeChild(bullet, true);
					score++;
					scorelabel.setString("分数 ：" + score);
					new Thread(new CleanSprite(this, enemy)).start();
				}	
			}
		}
	}
	
	/**
	 * 背景移动
	 * @param dalat
	 */
	public void backgroundMove(float dalat) {
		background1.setPosition(0, background1.getPosition().y - 2);
		background2.setPosition(0, background1.getPosition().y + background1.getContentSize().height);
		background3.setPosition(0, background2.getPosition().y + background2.getContentSize().height);
		if(background2.getPosition().y == 0) {
			background1.setPosition(0, 0);
		}
	}
	
	/**
	 * 退出游戏
	 */
	public void exitGame() {
		SoundEngine.sharedEngine().pauseSound();
		this.unschedule("backgroundMove");
		this.unschedule("shoot");
		this.unschedule("enemyShoot");
		this.unschedule("addEnemy");
		this.unschedule("boom");
		this.unschedule("bulletJudge");
		this.unschedule("shootPlayer");
		this.removeAllChildren(true);
		background1.setPosition(0, 0);
		this.addChild(background1);
		background2.setPosition(0, background1.getContentSize().height);
		this.addChild(background2);
		gameOverTitle.setAnchorPoint(0, 0);
		gameOverTitle.setPosition((winSize.width - gameOverTitle.getContentSize().width) / 2, 
				winSize.height /2);
		this.addChild(gameOverTitle);
		scorelabel.setPosition(gameOverTitle.getPosition().x, winSize.height / 2 - 50);
		this.addChild(scorelabel);
	}

	@Override
	public boolean ccKeyDown(int keyCode, KeyEvent event) {
		exitGame();
		return super.ccKeyDown(keyCode, event);
	}
	
	
	
}

class CleanSprite implements Runnable{
	private GameLayer layer;
	private CCSprite sprite;
	public CleanSprite(GameLayer layer, CCSprite sprite) {
		this.layer = layer;
		this.sprite = sprite;
	}
	
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.layer.removeChild(sprite, true);
	}
	
}
