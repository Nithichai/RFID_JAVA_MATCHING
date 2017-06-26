import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Database {
	
	private JSONArray userEventJSON;
	private int colLength = FrameGui.tableHeader.length;
	private String mainIP = "http://192.168.43.129:7777";
	
	public Database() {
		userEventJSON = new JSONArray();
	}
	
	public void updateRunnerList(String runningNO) {
		String ip = mainIP + "/select_run/" + runningNO;
		URL url;
		try {
			url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
			con.disconnect();
			userEventJSON = new JSONArray(response.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void addTagToDatabase(String id, String run_no, String tag) {
		try {
			JSONObject jo = new JSONObject();
			jo.put("event_id", id);
			jo.put("running_no", run_no);
			jo.put("Tagdata", tag);
			JSONArray ja = new JSONArray();
			ja.put(jo);
			String ip = mainIP + "/insert";
			URL url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jo.toString());
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null)
				System.out.println(line);
			rd.close();
			wr.close();
			con.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTagToDatabase(String run_no) {
		String ip = mainIP + "/delete/" + run_no;
		URL url;
		try {
			url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("DELETE");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
			con.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[][] getTable() {
		String[][] table = new String[0][colLength];
		try {
			if (userEventJSON.length() > 0) {
				table = new String[userEventJSON.length()][colLength];
				for (int i = 0; i < table.length; i++) {
					JSONObject obj = new JSONObject(userEventJSON.get(i).toString());
					for (int j = 0; j < colLength; j++) {
						table[i][j] = obj.get(FrameGui.tableHeader[j].toString()).toString();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return table;
	}
	
	public boolean datainJSON(String data) {
		try {
			for (int i = 0; i < userEventJSON.length(); i++) {
				JSONObject obj = new JSONObject(userEventJSON.get(i).toString());
				System.out.println(obj.get(FrameGui.tableHeader[1].toString()).toString());
				if (obj.get(FrameGui.tableHeader[1]).toString().equals(data)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
