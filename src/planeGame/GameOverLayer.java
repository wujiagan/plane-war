package planeGame;


import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;

import android.view.KeyEvent;

public class GameOverLayer extends CCLayer {
	public GameOverLayer(int score) {
		this.setIsTouchEnabled(true);
		CCSprite background1 = CCSprite.sprite("background.jpg");
		CCSprite background2 = CCSprite.sprite("background.jpg");
		background1.setAnchorPoint(0, 0);
		background1.setPosition(0, 0);
		this.addChild(background1);
		background2.setAnchorPoint(0, 0);
		background2.setPosition(0, background1.getContentSize().height);
		this.addChild(background2);
		CCLabel scorelabel = CCLabel.makeLabel("score: " + score, "DroidSans", 20);
		scorelabel.setAnchorPoint(0, 0);
		scorelabel.setPosition(30, CCDirector.sharedDirector().winSize().height / 2);
		this.addChild(scorelabel);
		
		CCSprite titleSprite = CCSprite.sprite("gameover.png");
		titleSprite.setAnchorPoint(0, 0);
		titleSprite.setPosition(30, scorelabel.getPosition().y + 40);
		titleSprite.setScale(0.5f);
		titleSprite.setOpacity(128);
		this.addChild(titleSprite);
	}
	@Override
	public boolean ccKeyDown(int keyCode, KeyEvent event) {
		System.exit(1);
		return super.ccKeyDown(keyCode, event);
	}
	
	
}
