package malthusian.qr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import malthusian.qr.Logger;
import malthusian.qr.net.NetClient;
import malthusian.qr.net.Request;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity
{
	private static Logger logger = Logger.getLogger(MainActivity.class);
	
	public NetClient netClient;
	
	String location = "defaultlocation";
	EditText locationEdit;
    TextView debugView; 
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.scan_button).setOnClickListener(scanQRCode);
        locationEdit = (EditText) findViewById(R.id.location);
        locationEdit.setText(this.location);
        debugView = (TextView) findViewById(R.id.debug);

    	netClient = new NetClient(this);
    	netClient.start();
    }
    
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
    	netClient.requestStop();
	}
    
	private final Button.OnClickListener scanQRCode = new Button.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
			//integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
			integrator.initiateScan();
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		
		if (result != null)
	    {
			String contents = result.getContents();

			if (contents != null)
			{
				this.location = locationEdit.getText().toString();
				debugView.setText("format: " + result.getFormatName() + " location: " + this.location + " data: " + contents);
				netClient.addRequest(new Request(location, contents));
			}
			else
			{
				logger.debug("no result");
			}
	    }
	}
}