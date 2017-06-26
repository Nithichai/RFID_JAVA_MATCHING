import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;

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
	private JLabel tagLabel;
	private JLabel tagSetLabel;
	private JLabel searchLabel;
	private JButton searchButton;
	private JButton refreshButton;
	
	public static String[] tableHeader = {"user_name", "txt_running_no", "event_id"};
	private  String[][] tableData = new String[0][tableHeader.length];
	private String[] antennaSet = {"Antenna1", "Antenna2", "Antenna3", "Antenna4"};
	
	private Database db;
	private Rfid rfid;
	private JTextField nameTextField;
	private String[] selectionRow;

	public static void main(String[] args) {
		FrameGui frame = new FrameGui();
		frame.start();
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
		initialize();
		db = new Database();
		rfid = new Rfid();
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
		        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        }
		    }
		});
		
		runnerScrollPane = new JScrollPane();
		runnerScrollPane.setBounds(0, 0, 563, 565);
		frame.getContentPane().add(runnerScrollPane);
		
		runnerTable = new JTable();
		runnerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		runnerTable.setModel(new DefaultTableModel(tableData, tableHeader) {
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
					selectionRow = tableData[colSelect];
					nameTextField.setText(selectionRow[1]);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				int[] colSelects = runnerTable.getSelectedRows();
				if (colSelects.length > 0) {
					int colSelect = colSelects[0];
					selectionRow = tableData[colSelect];
					nameTextField.setText(selectionRow[1]);
				}
			}
		});
		runnerTable.getSelectedRows();
		runnerScrollPane.setViewportView(runnerTable);
		
		tagSetLabel = new JLabel("Tag ID");
		tagSetLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tagSetLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tagSetLabel.setBounds(653, 173, 50, 20);
		frame.getContentPane().add(tagSetLabel);
		
		tagLabel = new JLabel();
		tagLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tagLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tagLabel.setBounds(575, 201, 207, 16);
		frame.getContentPane().add(tagLabel);
		
		setButton = new JButton("Set");
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = nameTextField.getText().trim();
				if (name.length() >= 1 && db.datainJSON(name)) {
					// Make Tag
					Random random = new Random();
		        	String data = "";
		        	for (int i = 0; i < 30; i++) {
		        		data += String.valueOf(random.nextInt(10));
		        	}
					rfid.getReadData(data, antennaComboBox.getSelectedIndex());
					
					int reply = JOptionPane.showConfirmDialog(frame, 
			            "Are you sure to set tag to runner ?", "Really Set Tag?", 
			            JOptionPane.YES_NO_OPTION);
			        if (reply == JOptionPane.YES_OPTION){
			        	System.out.println("Set");
			        	db.addTagToDatabase(selectionRow[2], selectionRow[1], rfid.tag);
			        	tableData = db.getTable();
			        }
				} else {
					JOptionPane.showMessageDialog(frame, "Please insert data in message box");
				}
				
			}
		});
		setButton.setBounds(575, 230, 103, 25);
		frame.getContentPane().add(setButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(679, 230, 103, 25);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Cancel");
				rfid.getReadData("", antennaComboBox.getSelectedIndex());
			}
		});
		frame.getContentPane().add(cancelButton);
		
		ipLabel = new JLabel("IP");
		ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ipLabel.setBounds(575, 16, 25, 16);
		frame.getContentPane().add(ipLabel);
		
		ipTextField = new JTextField();
		ipTextField.setBounds(612, 13, 170, 22);
		frame.getContentPane().add(ipTextField);
		ipTextField.setColumns(10);
		
		portLabel = new JLabel("Port");
		portLabel.setHorizontalAlignment(SwingConstants.CENTER);
		portLabel.setBounds(575, 46, 25, 16);
		frame.getContentPane().add(portLabel);
		
		portTextField = new JTextField();
		portTextField.setBounds(612, 43, 170, 22);
		frame.getContentPane().add(portTextField);
		portTextField.setColumns(10);
		
		connectButton = new JToggleButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (connectButton.isSelected()) {
					System.out.println("Connect");
					connectButton.setText("Disconnect");
					antennaComboBox.setEnabled(true);
					startButton.setEnabled(true);
				} else {
					System.out.println("DisConnect");
					connectButton.setText("Connect");
					antennaComboBox.setEnabled(false);
					startButton.setEnabled(false);
					setButton.setEnabled(false);
					cancelButton.setEnabled(false);
					nameTextField.setEnabled(false);
					searchButton.setEnabled(false);
//					refreshButton.setEnabled(false);
				}
			}
		});
		connectButton.setBounds(575, 73, 207, 25);
		frame.getContentPane().add(connectButton);
		
		antennaButton = new JLabel("Antenna");
		antennaButton.setBounds(575, 106, 56, 16);
		frame.getContentPane().add(antennaButton);
		
		antennaComboBox = new JComboBox(antennaSet);
		antennaComboBox.setBounds(635, 106, 147, 22);
		antennaComboBox.setSelectedIndex(0);
		frame.getContentPane().add(antennaComboBox);
		
		startButton = new JToggleButton("Start");
		startButton.setBounds(575, 135, 207, 25);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (startButton.isSelected()) {
					System.out.println("Start");
					startButton.setText("Stop");
					setButton.setEnabled(true);
					cancelButton.setEnabled(true);
					nameTextField.setEnabled(true);
					searchButton.setEnabled(true);
//					refreshButton.setEnabled(true);
				} else {
					System.out.println("Stop");
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
		searchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		searchLabel.setBounds(575, 268, 43, 16);
		frame.getContentPane().add(searchLabel);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(621, 265, 161, 22);
		frame.getContentPane().add(nameTextField);
		nameTextField.setColumns(10);
		
		searchButton = new JButton("Search");
		searchButton.setBounds(575, 295, 207, 25);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				db.updateRunnerList(nameTextField.getText().trim());
				tableData = db.getTable();
				
				ArrayList<String[]> list = new ArrayList<String[]> ();
				for (int i = 0; i < tableData.length; i++) {
					String[] datas = new String[tableHeader.length];
					System.arraycopy(tableData[i], 0, datas, 0, tableHeader.length);
					if (datas[1].equals(nameTextField.getText().trim()))
						list.add(datas);
				}
				String[][] datas = new String[list.size()][tableHeader.length];
				for (int i = 0; i < datas.length; i++) {
					datas[i] = list.get(i);
				}
				if (datas.length > 0)
					tableData = datas;
				else
					tableData = db.getTable();
			}
		});
		frame.getContentPane().add(searchButton);
//		refreshButton = new JButton("Refresh");
//		refreshButton.setBounds(575, 328, 207, 25);
//		refreshButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				
//			}
//		});
//		frame.getContentPane().add(refreshButton);
		
		frame.setVisible(true);
		antennaComboBox.setEnabled(false);
		startButton.setEnabled(false);
		setButton.setEnabled(false);
		cancelButton.setEnabled(false);
		nameTextField.setEnabled(false);
		searchButton.setEnabled(false);
//		refreshButton.setEnabled(false);
	}
	
	private void update() {
		DefaultTableModel model = (DefaultTableModel) runnerTable.getModel();
		model.setNumRows(tableData.length);
		for (int i = 0; i < tableData.length; i++) {
			for (int j = 0; j < tableData[i].length; j++) {
				model.setValueAt(tableData[i][j], i, j);
			}
		}
		tagLabel.setText(rfid.tag);
	}
}
