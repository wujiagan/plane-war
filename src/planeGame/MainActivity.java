package planeGame;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import com.example.neewgame.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private CCGLSurfaceView view = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		view = new CCGLSurfaceView(this);
		
		setContentView(view);
		
		CCDirector director = CCDirector.sharedDirector();
		
		director.attachInView(view);
		
		director.setAnimationInterval(1/30.0);
		
		CCScene wellcomeScene = CCScene.node();
		
		WellcomeLayer wellcomeLayer = new WellcomeLayer();
		wellcomeScene.addChild(wellcomeLayer);
		
		director.runWithScene(wellcomeScene);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
