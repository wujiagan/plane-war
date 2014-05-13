package planeGame;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;

public class WellcomeLayer extends CCLayer {
	public WellcomeLayer() {
		this.setIsTouchEnabled(true);
		CCSprite background1 = CCSprite.sprite("background.jpg");
		CCSprite background2 = CCSprite.sprite("background.jpg");
		background1.setAnchorPoint(0, 0);
		background1.setPosition(0, 0);
		this.addChild(background1);
		background2.setAnchorPoint(0, 0);
		background2.setPosition(0, background1.getContentSize().height);
		this.addChild(background2);
		
		CCMenu menu = CCMenu.menu();
		menu.setPosition(0,0);
		CCSprite btn = CCSprite.sprite("LOGO.png");
		CCMenuItem item = CCMenuItemSprite.item(btn, btn, this, "startOperate");
		item.setAnchorPoint(0,0);
		item.setPosition((CCDirector.sharedDirector().winSize().width - item.getContentSize().width)/2, CCDirector.sharedDirector().winSize().height/2);
		menu.addChild(item);
		this.addChild(menu);
	}

	public void startOperate(Object sender) {
		CCScene scene = CCScene.node();
		GameLayer gameLayer = new GameLayer();
		scene.addChild(gameLayer);
		CCDirector.sharedDirector().replaceScene(scene);
	}
	
	
}
