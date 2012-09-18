package malthusian.qr.net;

public class Request
{
	String player;
	String location;
	
	public Request(String location, String player)
	{
		this.location = location;
		this.player = player;
	}
	
	public String getPlayer()
	{
		return player;
	}
	
	public void setPlayer(String player)
	{
		this.player = player;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public void setLocation(String location)
	{
		this.location = location;
	}
}
