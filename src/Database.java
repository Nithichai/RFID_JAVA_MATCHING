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
	private JSONArray countList;
	private JSONArray checkTagList;
	private String[] runnerSetData = {"user_name", "txt_running_no", "event_name", "is_registered"};
	private String[] tagSetData = {"running_no", "Tagdata", "event_name"};
	private String mainIP;
	public String[] eventIDList;
	
	public Database() {
		runnerList = new JSONArray();
		tagList = new JSONArray();
	}
	
	public boolean updateRunnerList(String runningNO, String eventID) {
		try {
			JSONObject jo = new JSONObject();
			jo.put("running_no", runningNO);
			jo.put("event_id", eventID);
			String ip = mainIP + "/select_run";
			URL url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
//			con.connect();
			if (con.getOutputStream() == null) {
				return false;
			}
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jo.toString());
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
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
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
//			if (con.getInputStream() == null) {
//				return false;
//			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
//			con.disconnect();
			tagList = new JSONArray(response.toString());
			System.out.println(tagList.toString());
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
	
	public boolean checkTag(String tag) {
		try {
			JSONObject jo = new JSONObject();
			jo.put("Tagdata", tag);
			String ip = mainIP + "/tag_check";
			URL url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
//			con.connect();
			if (con.getOutputStream() == null) {
				return false;
			}
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jo.toString());
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
//			con.disconnect();
			checkTagList = new JSONArray(response.toString());
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
	
	public String getCheckTag() throws JSONException {
		if (checkTagList.length() == 0)
			return "";
		JSONObject obj = (JSONObject) checkTagList.get(0);
		return obj.get("running_no").toString();
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
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jo.toString());
			wr.flush();			
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
			wr.close();
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
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jo.toString());
			wr.flush();			
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
			wr.close();
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
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
//			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jo.toString());
			wr.flush();			
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
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
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
			if (con.getInputStream() == null) {
				return new String[0];
			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
//			con.disconnect();
			eventList = new JSONArray(response.toString());
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
		eventIDList = new String[eventList.length()];
		String[] eventTable = new String[eventList.length()];
		try {
			if (eventList.length() > 0) {
				for (int i = 0; i < eventTable.length; i++) {
					JSONObject obj = new JSONObject(eventList.get(i).toString());
					eventTable[i] = obj.getString("event_name");
					eventIDList[i] = obj.get("event_id").toString();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return eventTable;
	}
	
	public boolean countRegisted() {
		try {
			String ip = mainIP + "/count_runner";
			URL url = new URL(ip);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
//			if (con.getInputStream() == null) {
//				return false;
//			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = rd.readLine()) != null)
				response.append(inputLine);
			rd.close();
//			con.disconnect();
			countList = new JSONArray(response.toString());
			System.out.println(countList.toString());
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
			System.out.print(tagList.toString());
			if (tagList.length() > 0) {
				System.out.println(tagList.toString());
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
	
	public String[] getCountTable() {
		String[] data = new String[countList.length()];
		try {
			if (countList.length() > 0) {
				for (int i = 0; i < countList.length(); i++) {
					data[i] = countList.get(i).toString();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public void setIP(String ip) {
		mainIP = ip;
	}
	
	public String getIP() {
		return mainIP;
	}
	
}
