package malthusian.qr.net;

import java.util.Vector;

import malthusian.qr.Logger;

public class RequestQueue
{
	private static Logger logger = Logger.getLogger(RequestQueue.class);
	
	private Vector<Request> requests = new Vector<Request>();
	
    public synchronized void add(Request req)
    {
    	requests.add(req);
    	notifyAll();
    }
	
    public synchronized Request get()
    {    	
    	try
		{
			while(requests.size()==0)
			{
				wait();
			}
		}
	    catch (InterruptedException ie)
	    {
	    	logger.debug("request queue interrupted");
	    	return null;
	    }
    	
	    return requests.firstElement();
    }
    
    public synchronized void remove()
    {
    	requests.remove(0);
    }
}
