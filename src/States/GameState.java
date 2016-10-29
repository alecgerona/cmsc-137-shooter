package States;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Entities.Player;
import Entities.Footmen;

public class GameState extends BasicGameState{
	
	private Player p;
	private LinkedList<Footmen> footMen;
	private Footmen f;

	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		//Initialize a new player with a given starting coords
		p = new Player(new Vector2f(States.GAME_HEIGHT/2, States.GAME_WIDTH/2));
		
		//Initialize Footmen
		footMen = new LinkedList<Footmen>(); 
		f = new Footmen(new Vector2f(150, 200), p);
		
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.fillRect(0, 0, States.GAME_HEIGHT, States.GAME_WIDTH);
		g.drawString("Game State sample", 50, 50);
		
		//Render player
		p.render(gc, g);
		
		
		
		if (f.isAlive()) {
			
			f.render(gc, g);
			f.isHit(p.getBullets());
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int t) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			s.enterState(States.MENU);
		}
		
		//Update the Player in every frame.
		p.update(gc, s, t);
		
		//Update enemies in every frame.
		f.update(gc, t);
	}

	@Override
	public int getID() {
		return States.GAME;
	}

}
