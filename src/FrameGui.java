import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.jlrfid.service.RFIDException;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

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
	
	private Database db;
	private Rfid rfid;
	
	private String[] tableHeaderRunner = {"Username", "Running ID", "Event ID"};
	private String[][] tableDataRunner = new String[0][tableHeaderRunner.length];
	private String[] tableHeaderTag = {"Running ID", "Tag ID"};
	private String[][] tableDataTag = new String[0][tableHeaderTag.length];
	private String[] antennaSet = {"Antenna1", "Antenna2", "Antenna3", "Antenna4"};
	private String[] selectionRow;

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
		frame.setBounds(100, 100, 1280, 720);
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
		runningPanel.setBounds(12, 526, 1246, 153);
		frame.getContentPane().add(runningPanel);
		runningPanel.setLayout(null);
		
		runnerScrollPane = new JScrollPane();
		runnerScrollPane.setBounds(12, 23, 1219, 116);
		runningPanel.add(runnerScrollPane);
		
		runnerTable = new JTable();
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
		
		tagTablePanel = new JPanel();
		tagTablePanel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, null, null), 
				"Tag Table", 
				TitledBorder.LEADING, 
				TitledBorder.TOP, null, null));
		tagTablePanel.setBounds(12, 16, 972, 498);
		frame.getContentPane().add(tagTablePanel);
		tagTablePanel.setLayout(null);
		
		tagScrollPane = new JScrollPane();
		tagScrollPane.setBounds(12, 31, 947, 451);
		tagTablePanel.add(tagScrollPane);
		
		tagTable = new JTable();
		tagTable.setModel(new DefaultTableModel(tableDataTag, tableHeaderTag) {
			private static final long serialVersionUID = -9098365186691267440L;
			public boolean isCellEditable(int row, int column){
				return false;
			}
		});
		tagScrollPane.setViewportView(tagTable);
		
		rfidPanel = new JPanel();
		rfidPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "RFID Status", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		rfidPanel.setBounds(1000, 16, 260, 277);
		frame.getContentPane().add(rfidPanel);
		rfidPanel.setLayout(null);
		
		ipLabel = new JLabel("IP");
		ipLabel.setBounds(12, 30, 11, 25);
		rfidPanel.add(ipLabel);
		ipLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		ipTextField = new JTextField("192.168.1.201");
		ipTextField.setBounds(60, 30, 186, 25);
		rfidPanel.add(ipTextField);
		ipTextField.setColumns(10);
		
		portLabel = new JLabel("Port");
		portLabel.setBounds(13, 65, 25, 25);
		rfidPanel.add(portLabel);
		portLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		portTextField = new JTextField("20058");
		portTextField.setBounds(60, 65, 186, 25);
		rfidPanel.add(portTextField);
		portTextField.setColumns(10);
		
		connectButton = new JToggleButton("Connect");
		connectButton.setBounds(13, 100, 235, 25);
//		ipTextField
		rfidPanel.add(connectButton);
		
		antennaButton = new JLabel("Antenna");
		antennaButton.setBounds(20, 135, 47, 25);
		rfidPanel.add(antennaButton);
		
		antennaComboBox = new JComboBox<Object>(antennaSet);
		antennaComboBox.setBounds(78, 135, 170, 25);
		rfidPanel.add(antennaComboBox);
		antennaComboBox.setSelectedIndex(0);
		
		antennaComboBox.setEnabled(false);
		
		startButton = new JToggleButton("Start");
		startButton.setEnabled(false);
		startButton.setBounds(13, 170, 235, 25);
		rfidPanel.add(startButton);
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
		
		tagSetLabel = new JLabel("Tag ID");
		tagSetLabel.setBounds(13, 205, 38, 25);
		rfidPanel.add(tagSetLabel);
		tagSetLabel.setHorizontalAlignment(SwingConstants.LEFT);
		tagSetLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		tagLabel = new JTextField();
		tagLabel.setBounds(63, 205, 185, 25);
		rfidPanel.add(tagLabel);
		tagLabel.setEditable(false);
		tagLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tagLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		setButton = new JButton("Set");
		setButton.setBounds(139, 240, 109, 25);
		rfidPanel.add(setButton);
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setButton.setText("Setting....");
				String name = nameTextField.getText().trim();
				String tag = tagLabel.getText().trim();
				if (name.length() == 0) {
					JOptionPane.showMessageDialog(frame, "Please insert data in message box !!");
				}
				if (tag.length() == 0) {
					JOptionPane.showMessageDialog(frame, "Please scan RFID !!");
				}
				if (runnerTable.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(frame, "Please select name in \"Runner Table\"");
				}
				if (name.length() > 0 && tag.length() > 0 && runnerTable.getSelectedRow() != -1) {
					if (db.isDataInTagList(name)) {
						int reply = JOptionPane.showConfirmDialog(
								frame, 
								"Are you sure to 'update' tag to runner : " + selectionRow[1] + " ?", "Really Set Tag?", 
								JOptionPane.YES_NO_OPTION );
				        if (reply == JOptionPane.YES_OPTION){
				        	db.addTagToTable(selectionRow[2], selectionRow[1], Rfid.tag);
				        	tableDataRunner = db.getRunnerTable();
				        	boolean isConnected = db.updateTagList();
				    		if (isConnected) {
				    			tableDataTag = db.getTagTable();
				    			JOptionPane.showMessageDialog(frame, selectionRow[1] + " has tag " + rfid.getTag());
				    		} else {
				    			JOptionPane.showMessageDialog(frame, "Server Error");
								stateServerLabel.setText("No Connection");
								connectServerButton.setSelected(false);
								nameTextField.setEnabled(false);
								searchButton.setEnabled(false);
								connectServerButton.setText("Connect Server");
				    		}
				        }
					} else {
						int reply = JOptionPane.showConfirmDialog(
								frame, 
								"Are you sure to 'insert' tag to runner : " + selectionRow[1] + " ?", "Really Set Tag?", 
								JOptionPane.YES_NO_OPTION );
				        if (reply == JOptionPane.YES_OPTION){
				        	db.addTagToTable(selectionRow[2], selectionRow[1], Rfid.tag);
				        	tableDataRunner = db.getRunnerTable();
				        	boolean isConnected = db.updateTagList();
				    		if (isConnected) {
				    			tableDataTag = db.getTagTable();
				    			JOptionPane.showMessageDialog(frame, selectionRow[1] + " has tag " + rfid.getTag());
				    		} else {
				    			JOptionPane.showMessageDialog(frame, "Server Error");
								stateServerLabel.setText("No Connection");
								connectServerButton.setSelected(false);
								nameTextField.setEnabled(false);
								searchButton.setEnabled(false);
								connectServerButton.setText("Connect Server");
				    		}
				        }
					}
				}
				setButton.setText("Set");
			}
		});
		setButton.setEnabled(false);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);
		cancelButton.setBounds(13, 239, 116, 25);
		rfidPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tagLabel.setText("");
				rfid.getReadData("", antennaComboBox.getSelectedIndex());
			}
		});
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connectButton.setText("Connecting...");
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
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, null, null), 
				"Server Status", TitledBorder.LEADING, 
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		statusPanel.setBounds(1000, 306, 260, 208);
		frame.getContentPane().add(statusPanel);
		statusPanel.setLayout(null);
		
		JLabel statusIPLabel = new JLabel("IP : ");
		statusIPLabel.setBounds(13, 30, 56, 25);
		statusPanel.add(statusIPLabel);
		
		JLabel stateLabel = new JLabel("State : ");
		stateLabel.setBounds(13, 65, 56, 25);
		statusPanel.add(stateLabel);
		
		connectServerButton = new JToggleButton("Connect Server");
		connectServerButton.setBounds(13, 100, 235, 25);
		connectServerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectServerButton.setText("Connecting...");
				if (connectServerButton.isSelected()) {
					db.setIP(ipServerTextField.getText().toString().trim());
					boolean isConnected = db.updateTagList();
					if (isConnected) {
						tableDataTag = db.getTagTable();
						stateServerLabel.setText("Connect");
						connectServerButton.setEnabled(true);
						nameTextField.setEnabled(true);
						searchButton.setEnabled(true);
						connectServerButton.setText("Disconnected");
					} else {
						JOptionPane.showMessageDialog(frame, "Server Error");
						stateServerLabel.setText("No Connection");
						connectServerButton.setSelected(false);
						nameTextField.setEnabled(false);
						searchButton.setEnabled(false);
						connectServerButton.setText("Connect Server");
					}
				} else {
					stateServerLabel.setText("No Connection");
					connectServerButton.setSelected(false);
					nameTextField.setEnabled(false);
					searchButton.setEnabled(false);
					connectServerButton.setText("Connect Server");
				}
			}
		});
		statusPanel.add(connectServerButton);
		
		ipServerTextField = new JTextField("192.168.1.198:7777");
		ipServerTextField.setBounds(60, 30, 188, 25);
		statusPanel.add(ipServerTextField);
		
		stateServerLabel = new JLabel("No Connection");
		stateServerLabel.setBounds(60, 65, 188, 25);
		statusPanel.add(stateServerLabel);
		
		searchLabel = new JLabel("Runner ID");
		searchLabel.setBounds(14, 135, 68, 25);
		statusPanel.add(searchLabel);
		
		nameTextField = new JTextField();
		nameTextField.setEnabled(false);
		nameTextField.setBounds(84, 135, 168, 22);
		statusPanel.add(nameTextField);
		nameTextField.setColumns(10);
		
		searchButton = new JButton("Search");
		searchButton.setEnabled(false);
		searchButton.setBounds(13, 170, 238, 25);
		statusPanel.add(searchButton);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchButton.setText("Searching....");
				if (nameTextField.getText().trim().length() > 0) {
					boolean isConnected = db.updateRunnerList(nameTextField.getText().trim());
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
						connectServerButton.setText("Connect Server");
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Please insert name in message box");
				}
				searchButton.setText("Search");
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
	}
}
