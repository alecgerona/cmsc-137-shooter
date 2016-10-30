package Networking;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.listener.SocketListener;

import Packets.BulletFire;
import Packets.Position;
import Packets.StartSignal;

public class ServerListener implements SocketListener{

	@Override
	public void connected(Connection con) {
		System.out.println("Client connected");
		ConnectionManager.getInstance().addConnection(con);
	
	}

	@Override
	public void disconnected(Connection con) {
		System.out.println("Client disconnected");
		ConnectionManager.getInstance().removeConnection(con);
	}

	@Override
	public void received(Connection con, Object object) {
		if (object instanceof Position) {
			Position pos = (Position) object;
			for (Connection c : ConnectionManager.getInstance().getConnections()) {
				c.sendTcp(pos);
			}
		}
		
		if (object instanceof BulletFire) {
			BulletFire bf = (BulletFire) object;
			for (Connection c : ConnectionManager.getInstance().getConnections()) {
				c.sendTcp(bf);
			}
		}
		
		if (object instanceof StartSignal) {
			StartSignal ss = (StartSignal) object;
			for (Connection c : ConnectionManager.getInstance().getConnections()) {
				c.sendTcp(ss);
			}
		}
	}
	

}
