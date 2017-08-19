import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import com.jlrfid.service.RFIDException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.ImageIcon;

public class FrameGui extends Thread {

	public static JTextField tagLabel;
	private JFrame frame;
	private JTable runnerTable;
	private JScrollPane runnerScrollPane;
	private JButton cancelButton;
	private JLabel ipLabel;
	private JTextField ipTextField;
	private JLabel portLabel;
	private JTextField portTextField;
	private JToggleButton connectButton;
	private JLabel antennaButton;
	private JComboBox<Object> antennaComboBox;
	private JToggleButton startButton;
	private JButton setButton;
	private JLabel tagSetLabel;
	private JLabel searchLabel;
	private JButton searchButton;
	private JTextField nameTextField;
	private JPanel tagTablePanel;
	private JScrollPane tagScrollPane;
	private JPanel runningPanel;
	private JTable tagTable;
	private JTextField ipServerTextField;
	private JLabel stateServerLabel;
	private JPanel rfidPanel;
	private JToggleButton connectServerButton;
	private JComboBox<Object> eventComboBox;
	private JTextField registedTextField;
	private JLabel registedLabel;
	private JLabel dbRunnerLabel;
	private JTextField dbRunnerValue;
	
	private Database db;
	private Rfid rfid;
	
	private String[] tableHeaderRunner = {"Username", "Running ID", "Event ID", "Register Status"};
	private String[][] tableDataRunner = new String[0][tableHeaderRunner.length];
	private String[] tableHeaderTag = {"Running ID", "Tag ID", "Event ID"};
	private String[][] tableDataTag = new String[0][tableHeaderTag.length];
	private String[] antennaSet = {"Antenna1", "Antenna2", "Antenna3", "Antenna4"};
	private String[] selectionRow;
	private String[] eventCombo = new String[0];
	private String[] countTable = new String[0];
	private JTextField maleTextField;
	private JTextField femaleTextField;
	private String searchText;
	

	public static void main(String[] args) {
		FrameGui frame = new FrameGui();
		frame.start();
	}
	
	public void run() {
		while (true) {
			try {
				update();
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public FrameGui() {
		rfid = new Rfid();
		db = new Database();
		initialize();
		resetAntenna();
		frame.setVisible(true);
	}
	
	private void resetAntenna() {
		for (int i = 0; i < 4; i++) {
    		try {
				rfid.setAntenna(false, i);
			} catch (RFIDException e) {
				e.printStackTrace();
			}
    	}
	}

	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 1024, 680);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        int reply = JOptionPane.showConfirmDialog(frame, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION);
		        if (reply == JOptionPane.YES_OPTION){
		        	if (startButton.isSelected()) {
		        		rfid.disconnect();
		        	}
		        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        }
		    }
		});
		
		runningPanel = new JPanel();
		runningPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Runner Table", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		runningPanel.setBounds(334, 12, 672, 306);
		frame.getContentPane().add(runningPanel);
		runningPanel.setLayout(null);
		
		runnerScrollPane = new JScrollPane();
		runnerScrollPane.setBounds(12, 59, 647, 231);
		runningPanel.add(runnerScrollPane);
		
		runnerTable = new JTable();
		runnerTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
		runnerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		runnerTable.setModel(new DefaultTableModel(tableDataRunner, tableHeaderRunner) {
			private static final long serialVersionUID = -9098365186691267440L;
			public boolean isCellEditable(int row, int column){
				return false;
			}
		});
		runnerTable.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				int[] colSelects = runnerTable.getSelectedRows();
				if (colSelects.length > 0) {
					int colSelect = colSelects[0];
					selectionRow = tableDataRunner[colSelect];
					nameTextField.setText(selectionRow[1]);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				int[] colSelects = runnerTable.getSelectedRows();
				if (colSelects.length > 0) {
					int colSelect = colSelects[0];
					selectionRow = tableDataRunner[colSelect];
					nameTextField.setText(selectionRow[1]);
				}
			}
		});
		runnerScrollPane.setViewportView(runnerTable);
		eventComboBox = new JComboBox<Object> (eventCombo);
		eventComboBox.setBounds(58, 24, 284, 25);
		runningPanel.add(eventComboBox);
		eventComboBox.setEnabled(false);
		
		JLabel eventLabel = new JLabel("Event");
		eventLabel.setBounds(13, 24, 55, 25);
		runningPanel.add(eventLabel);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(424, 24, 95, 25);
		runningPanel.add(nameTextField);
		nameTextField.setEnabled(false);
		nameTextField.setColumns(10);
		
		searchLabel = new JLabel("Runner ID");
		searchLabel.setBounds(354, 24, 70, 25);
		runningPanel.add(searchLabel);
		
		searchButton = new JButton("Search Runner");
		searchButton.setBounds(528, 25, 131, 25);
		runningPanel.add(searchButton);
		searchButton.setEnabled(false);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchButton.setText("Searching....");
				searchText = nameTextField.getText();
				if (nameTextField.getText().trim().length() > 0) {
					boolean isConnected = db.updateRunnerList(nameTextField.getText().trim(), db.eventIDList[eventComboBox.getSelectedIndex()]);
					if (isConnected) {
						tableDataRunner = db.getRunnerTable();
						ArrayList<String[]> list = new ArrayList<String[]> ();
						for (int i = 0; i < tableDataRunner.length; i++) {
							String[] datas = new String[tableHeaderRunner.length];
							System.arraycopy(tableDataRunner[i], 0, datas, 0, tableHeaderRunner.length);
							if (datas[1].equals(nameTextField.getText().trim()))
								list.add(datas);
						}
						String[][] datas = new String[list.size()][tableHeaderRunner.length];
						for (int i = 0; i < datas.length; i++) {
							datas[i] = list.get(i);
						}
						if (datas.length > 0)
							tableDataRunner = datas;
						else
							tableDataRunner = db.getRunnerTable();
					} else {
						JOptionPane.showMessageDialog(frame, "Server Error");
						stateServerLabel.setText("No Connection");
						connectServerButton.setSelected(false);
						nameTextField.setEnabled(false);
						searchButton.setEnabled(false);
						setButton.setEnabled(false);
						connectServerButton.setText("Connect Server");
						setButton.setEnabled(false);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Please insert name in message box");
				}
				searchButton.setText("Search");
			}
		});
		
		tagTablePanel = new JPanel();
		tagTablePanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Tagged Table", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		tagTablePanel.setBounds(338, 376, 670, 253);
		frame.getContentPane().add(tagTablePanel);
		tagTablePanel.setLayout(null);
		
		tagScrollPane = new JScrollPane();
		tagScrollPane.setBounds(12, 25, 642, 214);
		tagTablePanel.add(tagScrollPane);
		
		tagTable = new JTable();
		tagTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
		tagTable.setRowSelectionAllowed(false);
		tagTable.setModel(new DefaultTableModel(tableDataTag, tableHeaderTag) {
			private static final long serialVersionUID = -9098365186691267440L;
			public boolean isCellEditable(int row, int column){
				return false;
			}
		});
		tagScrollPane.setViewportView(tagTable);
		
		rfidPanel = new JPanel();
		rfidPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Register Status", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		rfidPanel.setBounds(11, 475, 308, 153);
		frame.getContentPane().add(rfidPanel);
		rfidPanel.setLayout(null);
		
		registedLabel = new JLabel("Registed");
		registedLabel.setBounds(10, 35, 79, 25);
		rfidPanel.add(registedLabel);
		registedLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		registedTextField = new JTextField("");
		registedTextField.setBounds(121, 35, 169, 25);
		rfidPanel.add(registedTextField);
		registedTextField.setEditable(false);
		registedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel maleLabel = new JLabel("Male Registed");
		maleLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		maleLabel.setBounds(10, 70, 79, 25);
		rfidPanel.add(maleLabel);
		
		maleTextField = new JTextField("");
		maleTextField.setHorizontalAlignment(SwingConstants.CENTER);
		maleTextField.setEditable(false);
		maleTextField.setBounds(121, 71, 169, 25);
		rfidPanel.add(maleTextField);
		
		femaleTextField = new JTextField("");
		femaleTextField.setHorizontalAlignment(SwingConstants.CENTER);
		femaleTextField.setEditable(false);
		femaleTextField.setBounds(121, 108, 169, 25);
		rfidPanel.add(femaleTextField);
		
		JLabel femaleLabel = new JLabel("Female Registed");
		femaleLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		femaleLabel.setBounds(10, 107, 95, 25);
		rfidPanel.add(femaleLabel);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, null, null), 
				"Server Status", TitledBorder.LEADING, 
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		statusPanel.setBounds(12, 12, 313, 308);
		frame.getContentPane().add(statusPanel);
		statusPanel.setLayout(null);
		
		JLabel statusIPLabel = new JLabel("IP Server");
		statusIPLabel.setBounds(13, 30, 80, 25);
		statusPanel.add(statusIPLabel);
		
		JLabel stateLabel = new JLabel("Status");
		stateLabel.setBounds(13, 65, 54, 25);
		statusPanel.add(stateLabel);
		
		connectServerButton = new JToggleButton("Connect");
		connectServerButton.setBounds(13, 100, 284, 25);
		connectServerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectServerButton.setText("Connecting...");
				if (connectServerButton.isSelected()) {
					if (!checkIPPort(ipServerTextField.getText())) {
						JOptionPane.showMessageDialog(frame, "Your IP or port is not correct.\n Please try again");
						stateServerLabel.setText("No Connection");
						connectServerButton.setSelected(false);
						nameTextField.setEnabled(false);
						searchButton.setEnabled(false);
						connectServerButton.setText("Connect Server");
						eventComboBox.setEnabled(false);
						connectServerButton.setText("Connect");
						setButton.setEnabled(false);
						return;
					}					
					db.setIP(ipServerTextField.getText().toString().trim());
					boolean isConnected = db.updateTagList() && db.countRegisted(); 
					if (isConnected) {
						stateServerLabel.setText("Connect");
						connectServerButton.setEnabled(true);
						nameTextField.setEnabled(true);
						searchButton.setEnabled(true);
						connectServerButton.setText("Disconnected");
						eventComboBox.setEnabled(true);
						tableDataTag = db.getTagTable();
						eventCombo = db.getEventComboBox();
						tableDataRunner = db.getRunnerTable();
						countTable = db.getCountTable();
						if (eventCombo.length > 0) {
							eventComboBox.setModel(new DefaultComboBoxModel<Object>(eventCombo));
							eventComboBox.setSelectedIndex(0);
						}
						setButton.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(frame, "Server Error");
						stateServerLabel.setText("No Connection");
						connectServerButton.setSelected(false);
						nameTextField.setEnabled(false);
						searchButton.setEnabled(false);
						connectServerButton.setText("Connect Server");
						eventComboBox.setEnabled(false);
						setButton.setEnabled(false);
						
					}
				} else {
					stateServerLabel.setText("No Connection");
					connectServerButton.setSelected(false);
					nameTextField.setEnabled(false);
					searchButton.setEnabled(false);
					connectServerButton.setText("Connect Server");
					eventComboBox.setEnabled(false);
					setButton.setEnabled(false);
				}
			}
		});
		statusPanel.add(connectServerButton);
		
		ipServerTextField = new JTextField("http://alumni.eng.kmutnb.ac.th:7777");
		ipServerTextField.setBounds(100, 30, 201, 25);
		statusPanel.add(ipServerTextField);
		
		stateServerLabel = new JLabel("No Connection");
		stateServerLabel.setBounds(106, 65, 195, 25);
		statusPanel.add(stateServerLabel);
		
		ipLabel = new JLabel("IP RFID");
		ipLabel.setBounds(14, 137, 68, 25);
		statusPanel.add(ipLabel);
		ipLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		ipTextField = new JTextField("192.168.1.201");
		ipTextField.setBounds(100, 137, 195, 25);
		statusPanel.add(ipTextField);
		ipTextField.setColumns(10);
		
		portLabel = new JLabel("Port");
		portLabel.setBounds(15, 172, 41, 25);
		statusPanel.add(portLabel);
		portLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		portTextField = new JTextField("20058");
		portTextField.setBounds(102, 172, 193, 25);
		statusPanel.add(portTextField);
		portTextField.setColumns(10);
		
		connectButton = new JToggleButton("Connect");
		connectButton.setBounds(13, 205, 281, 25);
		statusPanel.add(connectButton);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connectButton.setText("Connecting...");
				if (!checkIPPort(ipTextField.getText(), portTextField.getText())) {
					JOptionPane.showMessageDialog(frame, "Your IP or port is not correct.\n Please try again");
					connectButton.setSelected(false);
					connectButton.setText("Connect");
					antennaComboBox.setEnabled(false);
					startButton.setEnabled(false);
					setButton.setEnabled(false);
					cancelButton.setEnabled(false);
					connectServerButton.setText("Connect");
					return;
				}
				
				String ip = ipTextField.getText().toString().trim();
				String port = portTextField.getText().toString().trim();
				if (ip.length() > 0 && port.length() > 0) {
					if (connectButton.isSelected()) {
						boolean isConnect = rfid.connectRFID(ip, 0, Integer.parseInt(port));
						if (isConnect) {
							connectButton.setText("Disconnect");
							antennaComboBox.setEnabled(true);
							startButton.setEnabled(true);
						} else {
							JOptionPane.showMessageDialog(frame, "No connection from machine !!");
							connectButton.setSelected(false);
							connectButton.setText("Connect");
							antennaComboBox.setEnabled(false);
							startButton.setEnabled(false);
							setButton.setEnabled(false);
							cancelButton.setEnabled(false);
						}
					} else {
						connectButton.setText("Connect");
						antennaComboBox.setEnabled(false);
						startButton.setEnabled(false);
						setButton.setEnabled(false);
						cancelButton.setEnabled(false);
						if (startButton.isSelected()) {
			        		rfid.disconnect();
			        		startButton.setSelected(false);
							startButton.setText("Start");
			        	}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Please insert IP or port !!!");
					connectButton.setSelected(false);
					connectButton.setText("Connect");
				}
			}
		});
		
		antennaComboBox = new JComboBox<Object>(antennaSet);
		antennaComboBox.setBounds(103, 237, 191, 25);
		statusPanel.add(antennaComboBox);
		antennaComboBox.setSelectedIndex(0);
		
		antennaComboBox.setEnabled(false);
		
		antennaButton = new JLabel("Antenna");
		antennaButton.setBounds(20, 237, 47, 25);
		statusPanel.add(antennaButton);
		
		startButton = new JToggleButton("Start");
		startButton.setBounds(13, 271, 283, 25);
		statusPanel.add(startButton);
		startButton.setEnabled(false);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startButton.setText("Loading....");
				if (startButton.isSelected()) {
					try {
						resetAntenna();
						rfid.setAntenna(true, antennaComboBox.getSelectedIndex());
						rfid.setStart(true);
					} catch (RFIDException e) {
						e.printStackTrace();
					}
					startButton.setText("Stop");
					setButton.setEnabled(true);
					cancelButton.setEnabled(true);
				} else {
					try {
						rfid.setStart(false);
					} catch (RFIDException e) {
						e.printStackTrace();
					}
					startButton.setText("Start");
					setButton.setEnabled(false);
					cancelButton.setEnabled(false);
				}
			}
		});
		
		
//		eventCombo = new String[0];
		
		dbRunnerLabel = new JLabel("Runner");
		dbRunnerLabel.setBounds(338, 329, 50, 25);
		frame.getContentPane().add(dbRunnerLabel);
		
		dbRunnerValue = new JTextField("");
		dbRunnerValue.setEditable(false);
		dbRunnerValue.setBounds(392, 331, 120, 25);
		frame.getContentPane().add(dbRunnerValue);
		dbRunnerValue.setHorizontalAlignment(SwingConstants.LEFT);
		
		setButton = new JButton("Set");
		setButton.setEnabled(false);
		setButton.setBounds(859, 330, 59, 25);
		frame.getContentPane().add(setButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(926, 330, 80, 25);
		frame.getContentPane().add(cancelButton);
		
		tagLabel = new JTextField();
		tagLabel.setBounds(568, 330, 187, 25);
		frame.getContentPane().add(tagLabel);
		tagLabel.setEditable(false);
		tagLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tagLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		tagSetLabel = new JLabel("Tag ID");
		tagSetLabel.setBounds(518, 330, 38, 25);
		frame.getContentPane().add(tagSetLabel);
		tagSetLabel.setHorizontalAlignment(SwingConstants.LEFT);
		tagSetLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("kmutnb.png"));
		lblNewLabel.setBounds(100, 325, 130, 130);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnCheck = new JButton("Check");
		btnCheck.setBounds(767, 330, 80, 25);
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tag = tagLabel.getText().toString();
				if (tag.length() < 1) {
					JOptionPane.showMessageDialog(frame, "Please scan RFID");
				} else {
					boolean canCheck = db.checkTag(tag);
					if (canCheck) {
						String data;
						try {
							data = db.getCheckTag();
							if (data.equals("")) { 
								JOptionPane.showMessageDialog(frame, "Tag " + tag + " is no registed");
							} else {
								JOptionPane.showMessageDialog(frame, "Tag " + tag + " is tagged with " + data);
							}
						} catch (JSONException e) {
							JOptionPane.showMessageDialog(frame, "JSON Error");
						}
					} else {
						JOptionPane.showMessageDialog(frame, "Tag " + tag + " is no registed");
					}
				}
			}
		});
		
		
		frame.getContentPane().add(btnCheck);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tagLabel.setText("");
				rfid.getReadData("", antennaComboBox.getSelectedIndex());
			}
		});
		
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setButton.setText("Setting....");
				String name = nameTextField.getText().trim();
				String tag = tagLabel.getText().trim();
				if (name.length() == 0) {
					JOptionPane.showMessageDialog(frame, "Please insert data in Runner ID");
					setButton.setText("Set");
					return;
				}
				if (tag.length() == 0) {
					JOptionPane.showMessageDialog(frame, "Please scan RFID !!");
					setButton.setText("Set");
					return;
				}
				if (runnerTable.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(frame, "Please select name in \"Runner Table\"");
					setButton.setText("Set");
					return;
				}
				if (eventComboBox.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(frame, "Please select event in \"Event\"");
					setButton.setText("Set");
					return;
				}
				int eventID = Integer.parseInt(db.eventIDList[eventComboBox.getSelectedIndex()]);
				int reply = JOptionPane.showConfirmDialog(
						frame, 
						"Are you sure to set tag to runner : " + selectionRow[1] + " ?", "Really Set Tag?", 
						JOptionPane.YES_NO_OPTION );
				if (reply == JOptionPane.YES_OPTION){
					boolean canAddTag = db.addTagToTable(eventID, selectionRow[1], Rfid.tag);
					if (canAddTag) {
						JOptionPane.showMessageDialog(frame, "Insert Complete\n" + selectionRow[1] + " has tag " + rfid.getTag());
						runnerTable.setRowSelectionInterval(0, runnerTable.getSelectedRow() + 1);
						int[] colSelects = runnerTable.getSelectedRows();
						if (colSelects.length > 0) {
							int colSelect = colSelects[0];
							selectionRow = tableDataRunner[colSelect];
							nameTextField.setText(selectionRow[1]);
						}
						
					} else {
						int updateReply = JOptionPane.showConfirmDialog(
								frame, 
								"This tag is used or this runner is registed.\n" +
								"Are you sure to 'update' tag to runner : " + selectionRow[1] + " ?", "Really Update Tag?", 
								JOptionPane.YES_NO_OPTION);
						if (updateReply == JOptionPane.YES_OPTION) {
							boolean canDeleteRow = db.deleteRowFromTable(Rfid.tag);
							boolean canUpdateTag = db.updateTagToTable(eventID, selectionRow[1], Rfid.tag);
							if (canDeleteRow && canUpdateTag) {
								JOptionPane.showMessageDialog(frame, "Update Complete\n" + selectionRow[1] + " has tag " + rfid.getTag());
							} else  {
								boolean canAddTagAfterUpdate = db.addTagToTable(eventID, selectionRow[1], Rfid.tag);
								if (canAddTagAfterUpdate) {
									JOptionPane.showMessageDialog(frame, "Update Complete");
								}
								else {
									boolean canUpdateAfterAddTag = db.updateTagToTable(eventID, selectionRow[1], Rfid.tag);
									if (canUpdateAfterAddTag)
										JOptionPane.showMessageDialog(frame, "Update Complete");
									else
										JOptionPane.showMessageDialog(frame, "Update Failed");
								}
							}
						}
					}
				}
				boolean isConnected = db.updateRunnerList(searchText, db.eventIDList[eventComboBox.getSelectedIndex()]) &&
						db.updateTagList() && 
						db.countRegisted();
	    		if (isConnected) {
	    			tableDataRunner = db.getRunnerTable();
	    			tableDataTag = db.getTagTable();
	    			countTable = db.getCountTable();
	    		} else {
	    			JOptionPane.showMessageDialog(frame, "Server Error");
					stateServerLabel.setText("No Connection");
					connectServerButton.setSelected(false);
					nameTextField.setEnabled(false);
					searchButton.setEnabled(false);
					connectServerButton.setText("Connect Server");
					setButton.setEnabled(false);
	    		}
				setButton.setText("Set");
			}
		});
	}
	
	private void update() {
		DefaultTableModel modelRunner = (DefaultTableModel) runnerTable.getModel();
		modelRunner.setNumRows(tableDataRunner.length);
		for (int i = 0; i < tableDataRunner.length; i++) {
			for (int j = 0; j < tableDataRunner[i].length; j++) {
				modelRunner.setValueAt(tableDataRunner[i][j], i, j);
			}
		}
		DefaultTableModel modelTag = (DefaultTableModel) tagTable.getModel();
		modelTag.setNumRows(tableDataTag.length);
		for (int i = 0; i < tableDataTag.length; i++) {
			for (int j = 0; j < tableDataTag[i].length; j++) {
				modelTag.setValueAt(tableDataTag[i][j], i, j);
			}
		}
		if (runnerTable.getSelectedRow() != -1 && tableDataRunner.length > 0) {
			dbRunnerValue.setText(tableDataRunner[runnerTable.getSelectedRow()][0]);
		}
		if (countTable.length > 0) {
			registedTextField.setText(countTable[1] + " / " + countTable[0]);
			maleTextField.setText(countTable[4] + " / " + countTable[3]);
			femaleTextField.setText(countTable[7] + " / " + countTable[6]);
		}
	}
	
	private boolean checkIPPort(String text) {
		Pattern p = Pattern.compile("^"
                + "http://(((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}"
                + "|"
                + "localhost"
                + "|"
                + "(([0-9]{1,3}\\.){3})[0-9]{1,3})"
                + ":"
                + "[0-9]{1,5}$");
		return p.matcher(text).matches();
	}
	
	private boolean checkIPPort(String ip, String port) {
		Pattern pPort = Pattern.compile("^[0-9]{1,5}$");
		Pattern pIP = Pattern.compile("^(([0-9]{1,3}\\.){3})[0-9]{1,3}");
		return pIP.matcher(ip).matches() && pPort.matcher(port).matches();
	}
}
