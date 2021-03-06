package Entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import Packets.BulletFire;
import Packets.Position;
import States.States;

public class Player extends Entity{
	
	protected int fireRate = 350; //Smaller the better
	protected int moveSpeed = 5; //Lower is faster
	public int playerId;

	public Player(Vector2f pos, int playerId) {
		super(pos);
		this.playerId = playerId;
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (!isAlive()) return;
		super.render(gc, g);
		
		 g.setColor(Color.blue);
		if (Networking.ClientListener.playerInfo.get(playerId) != null) {
			g.fillOval(Networking.ClientListener.playerInfo.get(playerId).x-30, Networking.ClientListener.playerInfo.get(playerId).y-30, 60, 60);
		} else {
			//Initialize the position of another player's
			g.fillOval(400-30, 300-30, 60, 60);
		}
		
		if (Networking.ClientListener.bulletPos != null && delta > fireRate) {
			fireBullet(Networking.ClientListener.bulletPos, Networking.ClientListener.b, Networking.ClientListener.playerId);
			Networking.ClientListener.bulletPos = null;
			Networking.ClientListener.b = null;
		}
		
		
		//health bar here
		
	}
	
	public void update(GameContainer gc, StateBasedGame s, int t, boolean chatEnabled) throws SlickException {
		super.update(gc, t); //Run the Entity "parent"'s update()
		if( gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && delta > fireRate) {
			Vector2f mousePos = new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY());
			Bullet newBullet = new Bullet();
			Networking.ClientStarter.client.getServerConnection().sendTcp(new BulletFire(mousePos, newBullet, States.playerId));
		}
		
		//Get the amount of distance to displace the Player
		//This distance would be equal to the ratio of the update delta t and the given moveSpeed
		float distance = (float)t/moveSpeed;
		
		//Controls 
		
		//Move right
		if(( gc.getInput().isKeyDown(Input.KEY_RIGHT) || (!chatEnabled && gc.getInput().isKeyDown(Input.KEY_D)) ) && 
				pos.getX() < States.GAME_HEIGHT ) {
			pos.add(new Vector2f(distance,0));
			//Send updated position to server to redistribute to the rest of the clients
			Networking.ClientStarter.client.getServerConnection().sendTcp(new Position(pos, States.playerId));
		}
		
		//Move left
		if(( gc.getInput().isKeyDown(Input.KEY_LEFT) || (!chatEnabled && gc.getInput().isKeyDown(Input.KEY_A)) )&& 
				pos.getX() > 0  ) {
		
			pos.add(new Vector2f(-distance,0));
			//Send updated position to server to redistribute to the rest of the clients
			Networking.ClientStarter.client.getServerConnection().sendTcp(new Position(pos, States.playerId));
		}
		
		//Move down
		if( ( gc.getInput().isKeyDown(Input.KEY_DOWN) || (!chatEnabled && gc.getInput().isKeyDown(Input.KEY_S)) ) 	 && 
				pos.getY() < States.GAME_WIDTH  ) {
		
			pos.add(new Vector2f(0,distance));
			//Send updated position to server to redistribute to the rest of the clients
			Networking.ClientStarter.client.getServerConnection().sendTcp(new Position(pos, States.playerId));
			
		}
		
		//Move up
		if( ( gc.getInput().isKeyDown(Input.KEY_UP) || (!chatEnabled && gc.getInput().isKeyDown(Input.KEY_W)) )  && 
				pos.getY() > 0  ) {
		
			pos.add(new Vector2f(0,-distance));
			//Send updated position to server to redistribute to the rest of the clients
			Networking.ClientStarter.client.getServerConnection().sendTcp(new Position(pos, States.playerId));
		}
		
	}
	
	public void die(StateBasedGame s) {
		s.enterState(States.MENU);
	}

	public void init(GameContainer gc) throws SlickException {
		super.init(gc);
		
	}

	


}
