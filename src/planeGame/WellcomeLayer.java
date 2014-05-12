package planeGame;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;

import android.view.MotionEvent;

public class WellcomeLayer extends CCLayer {
	public WellcomeLayer() {
		this.setIsTouchEnabled(true);
		CCSprite background = CCSprite.sprite("background.jpg");
		CCSprite logo = CCSprite.sprite("LOGO.png");
		background.setAnchorPoint(0, 0);
		background.setPosition(0, 0);
		this.addChild(background);
		logo.setAnchorPoint(0, 0);
		logo.setPosition(20, CCDirector.sharedDirector().winSize().height/2);
		this.addChild(logo);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		CCScene scene = CCScene.node();
		GameLayer gameLayer = new GameLayer();
		scene.addChild(gameLayer);
		CCDirector.sharedDirector().replaceScene(scene);
		return super.ccTouchesBegan(event);
	}
	
	
}
