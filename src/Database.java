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
	
	private JSONArray runnerList;
	private JSONArray tagList;
	private String[] runnerSetData = {"user_name", "txt_running_no", "event_id"};
	private String[] tagSetData = {"running_no", "Tagdata"};
	private String mainIP;
	
	public Database() {
		runnerList = new JSONArray();
		tagList = new JSONArray();
	}
	
	public boolean updateRunnerList(String runningNO) {
		try {
			JSONObject jo = new JSONObject();
			jo.put("txt_running_no", runningNO);
			String ip = mainIP + "/select_run";
			URL url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
//			con.connect();
			if (con.getOutputStream() == null) {
				return false;
			}
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jo.toString());
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
//			con.disconnect();
			runnerList = new JSONArray(response.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateTagList() {
		try {
			String ip = mainIP + "/select_all";
			URL url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
//			if (con.getInputStream() == null) {
//				return false;
//			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
//			con.disconnect();
			tagList = new JSONArray(response.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addTagToTable(int id, String run_no, String tag) {
		try {
			JSONObject jo = new JSONObject();
			jo.put("event_id", id);
			jo.put("running_no", run_no);
			jo.put("Tagdata", tag);
			String ip = mainIP + "/insert";
			URL url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
//			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jo.toString());
			wr.flush();			
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
			wr.close();
//			System.out.println(response);
//			if (response.toString().equals("Conflict") || response.toString().equals("No Content"))
//				return false;
//			con.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (ProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateTagToTable(int id, String run_no, String tag) {
		try {
			JSONObject jo = new JSONObject();
			jo.put("event_id", id);
			jo.put("running_no", run_no);
			jo.put("Tagdata", tag);
			String ip = mainIP + "/update";
			URL url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
//			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jo.toString());
			wr.flush();			
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
			wr.close();
			System.out.println(response.toString());
//			if (response.toString().equals("Not Found") || response.toString().equals("No Content"))
//				return false;
//			con.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (ProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteRowFromTable(String tag) {
		try {
			JSONObject jo = new JSONObject();
			jo.put("Tagdata", tag);
			String ip = mainIP + "/delete_match";
			URL url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("DELETE");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
//			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jo.toString());
			wr.flush();			
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
			wr.close();
//			System.out.println(response.toString());
//			if (response.toString().equals("Not Found") || response.toString().equals("No Content"))
//				return false;
//			con.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (ProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String[] getEventComboBox() {
		JSONArray eventList;
		try {
			String ip = mainIP + "/select_event";
			URL url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			if (con.getInputStream() == null) {
				return new String[0];
			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
//			con.disconnect();
			eventList = new JSONArray(response.toString());
			System.out.println(eventList.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return new String[0];
		} catch (IOException e) {
			e.printStackTrace();
			return new String[0];
		} catch (JSONException e) {
			e.printStackTrace();
			return new String[0];
		}
		
		String[] eventTable = new String[eventList.length()];
		try {
			if (eventList.length() > 0) {
				for (int i = 0; i < eventTable.length; i++) {
					JSONObject obj = new JSONObject(eventList.get(i).toString());
					eventTable[i] = obj.getString("event_name");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(eventTable.length);
		return eventTable;
	}
	
//	public void deleteTagToDatabase(String run_no) {
//		String ip = mainIP + "/delete/" + run_no;
//		URL url;
//		try {
//			url = new URL(ip);
//			HttpURLConnection con = (HttpURLConnection) url.openConnection();
//			con.setRequestMethod("DELETE");
//			con.setConnectTimeout(10000);
//			con.setReadTimeout(10000);
//			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
//			StringBuffer response = new StringBuffer();
//			String inputLine;
//			while ((inputLine = rd.readLine()) != null)
//				response.append(inputLine);
//			rd.close();
//			con.disconnect();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public String[][] getRunnerTable() {
		String[][] table = new String[0][runnerSetData.length];
		try {
			if (runnerList.length() > 0) {
				table = new String[runnerList.length()][runnerSetData.length];
				for (int i = 0; i < table.length; i++) {
					JSONObject obj = new JSONObject(runnerList.get(i).toString());
					for (int j = 0; j < runnerSetData.length; j++) {
						table[i][j] = obj.get(runnerSetData[j].toString()).toString();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return table;
	}
	
	public String[][] getTagTable() {
		String[][] table = new String[0][tagSetData.length];
		try {
			if (tagList.length() > 0) {
				table = new String[tagList.length()][tagSetData.length];
				for (int i = 0; i < table.length; i++) {
					JSONObject obj = new JSONObject(tagList.get(i).toString());
					for (int j = 0; j < tagSetData.length; j++) {
						table[i][j] = obj.get(tagSetData[j].toString()).toString();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return table;
	}
	
	public boolean isDataInRunnerList(String data) {
		try {
			for (int i = 0; i < runnerList.length(); i++) {
				JSONObject obj = new JSONObject(runnerList.get(i).toString());
				if (obj.get(runnerSetData[1]).toString().equals(data)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isDataInTagList(String running_no) {
		try {
			for (int i = 0; i < tagList.length(); i++) {
				JSONObject obj = new JSONObject(tagList.get(i).toString());
				if (obj.get(tagSetData[0]).toString().equals(running_no)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isTagIsRegis(String data) {
		try {
			for (int i = 0; i < tagList.length(); i++) {
				JSONObject obj = new JSONObject(tagList.get(i).toString());
				if (obj.get(tagSetData[1]).toString().equals(data)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void setIP(String ip) {
		mainIP = ip;
//		System.out.println(mainIP);
	}
	
	public String getIP() {
		return mainIP;
	}
	
}
