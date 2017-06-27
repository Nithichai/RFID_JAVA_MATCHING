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
	private JComboBox antennaComboBox;
	private JToggleButton startButton;
	private JButton setButton;
	private JLabel tagSetLabel;
	private JLabel searchLabel;
	private JButton searchButton;
	
	public static JTextField tagLabel;
//	private JButton refreshButton;
	
	private String[] tableHeaderRunner = {"Username", "Running ID", "Event ID"};
	private String[][] tableDataRunner = new String[0][tableHeaderRunner.length];
	private String[] tableHeaderTag = {"Running ID", "Tag ID"};
	private String[][] tableDataTag = new String[0][tableHeaderTag.length];
	private String[] antennaSet = {"Antenna1", "Antenna2", "Antenna3", "Antenna4"};
	
	private Database db;
	private Rfid rfid;
	private JTextField nameTextField;
	private String[] selectionRow;
	private JPanel tagTablePanel;
	private JScrollPane tagScrollPane;
	private JPanel runningPanel;
	private JTable tagTable;

	public static void main(String[] args) {
		FrameGui frame = new FrameGui();
		frame.start();
		frame.frame.setVisible(true);
	}
	
	public void run() {
		while (true) {
			try {
				update();
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public FrameGui() {
		rfid = new Rfid();
		db = new Database();
		initialize();
		db.updateTagList();
    	tableDataTag = db.getTagTable();
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
		frame.setBounds(100, 100, 800, 600);
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
		runningPanel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, null, null), 
				"Running Table", TitledBorder.LEADING, 
				TitledBorder.TOP, null, null));
		runningPanel.setBounds(22, 361, 760, 191);
		frame.getContentPane().add(runningPanel);
		runningPanel.setLayout(null);
		
		runnerScrollPane = new JScrollPane();
		runnerScrollPane.setBounds(12, 23, 736, 155);
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
		tagTablePanel.setBounds(22, 16, 488, 332);
		frame.getContentPane().add(tagTablePanel);
		tagTablePanel.setLayout(null);
		
		tagScrollPane = new JScrollPane();
		tagScrollPane.setBounds(12, 23, 464, 296);
		tagTablePanel.add(tagScrollPane);
		
		tagTable = new JTable();
		tagTable.setModel(new DefaultTableModel(tableDataTag, tableHeaderTag) {
			private static final long serialVersionUID = -9098365186691267440L;
			public boolean isCellEditable(int row, int column){
				return false;
			}
		});
		tagScrollPane.setViewportView(tagTable);
		
		tagSetLabel = new JLabel("Tag ID");
		tagSetLabel.setHorizontalAlignment(SwingConstants.LEFT);
		tagSetLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tagSetLabel.setBounds(513, 229, 50, 20);
		frame.getContentPane().add(tagSetLabel);
		
		tagLabel = new JTextField();
		tagLabel.setEditable(false);
		tagLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tagLabel.setHorizontalAlignment(SwingConstants.LEFT);
		tagLabel.setBounds(575, 229, 207, 20);
		frame.getContentPane().add(tagLabel);
		
		setButton = new JButton("Set");
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = nameTextField.getText().trim();
				String tag = tagLabel.getText();
				if (name.length() == 0) {
					JOptionPane.showMessageDialog(frame, "Please insert data in message box !!");
				}
				if (tag.length() == 0) {
					JOptionPane.showMessageDialog(frame, "Please scan RFID !!");
				}
				if (runnerTable.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(frame, "Please select name in \"Running Table\"");
				}
				if (name.length() >= 1 && tag.length() >= 1 && db.datainJSON(name) && runnerTable.getSelectedRow() != -1) {
					int reply = JOptionPane.showConfirmDialog(
							frame, 
							"Are you sure to set tag to runner : " + selectionRow[1] + " ?", "Really Set Tag?", 
							JOptionPane.YES_NO_OPTION );
			        if (reply == JOptionPane.YES_OPTION){
			        	System.out.println("Set");
			        	db.addTagToTable(selectionRow[2], selectionRow[1], Rfid.tag);
			        	tableDataRunner = db.getRunnerTable();
			        	db.updateTagList();
			        	tableDataTag = db.getTagTable();
			        	JOptionPane.showMessageDialog(frame, selectionRow[1] + " has tag : " + rfid.getTag());
			        }
				}
			}
		});
		setButton.setBounds(642, 257, 141, 25);
		frame.getContentPane().add(setButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(517, 257, 120, 25);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Cancel");
				rfid.getReadData("", antennaComboBox.getSelectedIndex());
			}
		});
		frame.getContentPane().add(cancelButton);
		
		ipLabel = new JLabel("IP");
		ipLabel.setHorizontalAlignment(SwingConstants.LEFT);
		ipLabel.setBounds(515, 16, 25, 16);
		frame.getContentPane().add(ipLabel);
		
		ipTextField = new JTextField("192.168.1.200");
		ipTextField.setBounds(575, 13, 207, 22);
		frame.getContentPane().add(ipTextField);
		ipTextField.setColumns(10);
		
		portLabel = new JLabel("Port");
		portLabel.setHorizontalAlignment(SwingConstants.LEFT);
		portLabel.setBounds(515, 46, 25, 16);
		frame.getContentPane().add(portLabel);
		
		portTextField = new JTextField("20058");
		portTextField.setBounds(575, 43, 207, 22);
		frame.getContentPane().add(portTextField);
		portTextField.setColumns(10);
		
		connectButton = new JToggleButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (connectButton.isSelected()) {
					boolean isConnect = rfid.connectRFID(ipTextField.getText().toString(), 
							0, 
							Integer.parseInt(portTextField.getText().toString()));
					if (isConnect) {
						connectButton.setText("Disconnect");
						antennaComboBox.setEnabled(true);
						startButton.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(frame, "No connection from machine !!");
//						System.out.println("Disconnect");
						connectButton.setSelected(false);
						connectButton.setText("Connect");
						antennaComboBox.setEnabled(false);
						startButton.setEnabled(false);
						setButton.setEnabled(false);
						cancelButton.setEnabled(false);
						nameTextField.setEnabled(false);
						searchButton.setEnabled(false);
					}
				} else {
					connectButton.setText("Connect");
					antennaComboBox.setEnabled(false);
					startButton.setEnabled(false);
					setButton.setEnabled(false);
					cancelButton.setEnabled(false);
					nameTextField.setEnabled(false);
					searchButton.setEnabled(false);
					if (startButton.isSelected()) {
		        		rfid.disconnect();
		        		startButton.setSelected(false);
						startButton.setText("Start");
		        	}
				}
			}
		});
		connectButton.setBounds(641, 73, 141, 25);
		frame.getContentPane().add(connectButton);
		
		antennaButton = new JLabel("Antenna");
		antennaButton.setBounds(515, 106, 56, 16);
		frame.getContentPane().add(antennaButton);
		
		antennaComboBox = new JComboBox(antennaSet);
		antennaComboBox.setBounds(575, 106, 207, 22);
		antennaComboBox.setSelectedIndex(0);
		antennaComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					rfid.setAntenna(true, antennaComboBox.getSelectedIndex());
				} catch (RFIDException e) {
					e.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(antennaComboBox);
		
		startButton = new JToggleButton("Start");
		startButton.setBounds(641, 135, 141, 25);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (startButton.isSelected()) {
					try {
						rfid.setStart(true);
					} catch (RFIDException e) {
						e.printStackTrace();
					}
					System.out.println("Start");
					startButton.setText("Stop");
					setButton.setEnabled(true);
					cancelButton.setEnabled(true);
					nameTextField.setEnabled(true);
					searchButton.setEnabled(true);
//					refreshButton.setEnabled(true);
				} else {
					System.out.println("Stop");
					try {
						rfid.setStart(false);
					} catch (RFIDException e) {
						e.printStackTrace();
					}
					startButton.setText("Start");
					setButton.setEnabled(false);
					cancelButton.setEnabled(false);
					nameTextField.setEnabled(false);
					searchButton.setEnabled(false);
//					refreshButton.setEnabled(false);
				}
			}
		});
		frame.getContentPane().add(startButton);
		
		searchLabel = new JLabel("Name");
		searchLabel.setBounds(515, 170, 43, 16);
		frame.getContentPane().add(searchLabel);
		
		nameTextField = new JTextField();
		nameTextField.setEnabled(false);
		nameTextField.setBounds(575, 167, 207, 22);
		frame.getContentPane().add(nameTextField);
		nameTextField.setColumns(10);
		
		searchButton = new JButton("Search");
		searchButton.setBounds(641, 196, 141, 25);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (nameTextField.getText().trim().length() > 0) {
					db.updateRunnerList(nameTextField.getText().trim());
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
					JOptionPane.showMessageDialog(frame, "Please insert name in message box");
				}
			}
		});
		frame.getContentPane().add(searchButton);
		
		antennaComboBox.setEnabled(false);
		startButton.setEnabled(false);
		setButton.setEnabled(false);
		cancelButton.setEnabled(false);
		searchButton.setEnabled(false);
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
