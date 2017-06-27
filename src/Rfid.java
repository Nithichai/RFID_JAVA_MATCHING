//import com.jlrfid.service.AntStruct;

import com.jlrfid.service.AntStruct;
import com.jlrfid.service.GetReadData;
import com.jlrfid.service.MainHandler;
import com.jlrfid.service.RFIDException;

public class Rfid implements GetReadData {
	
	public static String tag;
	
	private String ip = "192.168.1.200";
	private int baudrate = 9600;
	private int port = 20058;
	private byte[] antEnable = new byte[4];
	private String r2KPath = System.getProperty("user.dir") + "\\R2k.dll";
	
//	public Rfid() {
//		System.out.println("Start Rfid");
//	}
	
	public boolean connectRFID(String ip, int baudrate, int port) {
		System.out.println(ip + " " + baudrate + " " + port);
		MainHandler handler = new MainHandler();
		if(handler.dllInit(r2KPath)){
			if(handler.deviceInit(ip, baudrate, port)){
				this.ip = ip;
				this.baudrate = baudrate;
				this.port = port;
				return true;
			}
		}
		return false;
	}
	
	public void disconnect() {
		MainHandler handler = new MainHandler();
		if(handler.dllInit(r2KPath)){
			if(handler.deviceInit(ip, baudrate, port)){
				handler.StopInv();
			}
		}
	}
	
	public boolean getAntenna(int index) throws RFIDException{
		MainHandler handler = new MainHandler();
		if(handler.dllInit(r2KPath)){
			if(handler.deviceInit(ip, baudrate, port)){
				AntStruct struct = handler.GetAnt();
				for(int i=0; i<4; i++){
					System.out.println(
							"antenna" + (i+1) 
							+ (struct.antEnable[i]==1 ? "connected":"disconnected") 
							+ "work time:" + struct.dwellTime[i] 
							+ "power:" + struct.power[i].longValue()/10 +"dBm"
					);
				}
			}
			return false;
		}
		return true;
	}
	
	public boolean setAntenna(boolean isSelected, int index) throws RFIDException {
		MainHandler handler = new MainHandler();
		if(handler.dllInit(r2KPath)){
			if(handler.deviceInit(ip, baudrate, port)){
				if (isSelected)
					antEnable[index] = 1;
				else
					antEnable[index] = 0;
				long[] dwellTime = new long[]{500, 500, 500, 500};
				long[] power = new long[]{300,300,250,200};
				if(handler.SetAnt(antEnable,dwellTime,power)){
					System.out.println("succeed to set antenna parameter");
				}else{
					System.out.println("failed to set antenna parameter");
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean setStart(boolean isSelected) throws RFIDException {
		MainHandler handler = new MainHandler();
		if(handler.dllInit(r2KPath)){
			if(handler.deviceInit(ip, baudrate, port)){
				if (isSelected) {
					handler.BeginInv(new Rfid());
				} else {
					handler.StopInv();
				}
				return true;
			}
		}
		return false;
	}
	
	public void getReadData(String data, int antNo) {
		if ("F0".equals(data)) {
			System.out.println("Antenna 1 finished inventory");
		}else if ("F1".equals(data)) {
			System.out.println("Antenna 2 finished inventory");
		}else if ("F2".equals(data)) {
			System.out.println("Antenna 3 finished inventory");
		}else if ("F3".equals(data)) {
			System.out.println("Antenna 4 finished inventory");
		}else if(!"".equals(data)){
//			System.out.println("data" + data + "  antenna" + antNo);
			tag = data;
			FrameGui.tagLabel.setText(tag);
		}
	}
	
	public String getTag() {
		return tag;
	}
}
