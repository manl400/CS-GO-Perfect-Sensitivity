package com.varietycube;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Client extends JFrame implements ClipboardOwner {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField lowField;
	private JTextField avgField;
	private DecimalFormat df2 = new DecimalFormat("##.#####");
	private double avg, temp, low, high;
	private int tests = 0, timesRan = 1;
	private JCheckBox highCheckBox, lowCheckBox; 
	private JLabel lblStarting, lblLow, lblHigh, lblModifier, lblMod;
	private JButton btnFinish, btnReset, btnContinue, btnStart;
	public static final double LOWMOD1 = 0.5, HIGHMOD1 = 1.5, LOWMOD2 = 0.8, HIGHMOD2 = 1.2, LOWMOD3 = 0.95, HIGHMOD3 = 1.05;
	private JTextField highField;
	private JMenuItem mntmAbout;
	private JMenuItem mntmHowToUse;
	private JSeparator separator;
	private JLabel lblStatus;
	
	public void setClipboard(String aString) {
		StringSelection stringSelection = new StringSelection( aString );
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents( stringSelection, this );
	}
	
	public String getClipboardContents() {
	    String result = "";
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    Transferable contents = clipboard.getContents(null);
	    boolean hasTransferableText =
	      (contents != null) &&
	      contents.isDataFlavorSupported(DataFlavor.stringFlavor)
	    ;
	    if ( hasTransferableText ) {
	      try {
	        result = (String)contents.getTransferData(DataFlavor.stringFlavor);
	      }
	      catch (UnsupportedFlavorException ex){
	        System.out.println(ex);
	        ex.printStackTrace();
	      }
	      catch (IOException ex) {
	        System.out.println(ex);
	        ex.printStackTrace();
	      }
	    }
	    return result;
	 }
	
	public void lostOwnership(Clipboard e, Transferable e1) {
		
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Client.class.getResource("/com/varietycube/Half-Life-Counter-Strike-icon.png")));
		setResizable(false);
		setTitle("CSGO Sensitivity Finder");
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
            System.out.println("Bad");
        }
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 410, 220);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lowField = new JTextField();
		lowField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!lowField.getText().equals("")){
					setClipboard("sensitivity " + lowField.getText());
					lblStatus.setText("Status: Low field (" + lowField.getText() + ") copied with command!");
				}
			}
		});
		lowField.setEnabled(false);
		lowField.setBounds(81, 28, 86, 20);
		contentPane.add(lowField);
		lowField.setColumns(10);
		
		avgField = new JTextField();
		avgField.setBounds(81, 79, 86, 20);
		contentPane.add(avgField);
		avgField.setColumns(10);
		
		highField = new JTextField();
		highField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!highField.getText().equals("")){
					setClipboard("sensitivity " + highField.getText());
					lblStatus.setText("Status: High field (" + highField.getText() + ") copied with command!");
				}
			}
		});
		highField.setEnabled(false);
		highField.setBounds(81, 130, 86, 20);
		contentPane.add(highField);
		highField.setColumns(10);
		
		lblStarting = new JLabel("Starting/Avg:");
		lblStarting.setBounds(10, 82, 65, 14);
		contentPane.add(lblStarting);
		
		lblLow = new JLabel("Low:");
		lblLow.setBounds(10, 31, 46, 14);
		contentPane.add(lblLow);
		
		lblHigh = new JLabel("High:");
		lblHigh.setBounds(10, 133, 46, 14);
		contentPane.add(lblHigh);
		
		
		
		lblModifier = new JLabel("Modifier:");
		lblModifier.setBounds(200, 31, 46, 14);
		contentPane.add(lblModifier);
		
		lblMod = new JLabel("");
		lblMod.setBounds(295, 31, 89, 14);
		contentPane.add(lblMod);
		
		lowCheckBox = new JCheckBox("");
		lowCheckBox.setEnabled(false);
		lowCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLowMod(lblMod);
				highCheckBox.setSelected(false);
			}
		});
		lowCheckBox.setBounds(173, 27, 21, 23);
		contentPane.add(lowCheckBox);
		
		highCheckBox = new JCheckBox("");
		highCheckBox.setEnabled(false);
		highCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setHighMod(lblMod);
				lowCheckBox.setSelected(false);
			}
		});
		highCheckBox.setBounds(173, 129, 21, 23);
		contentPane.add(highCheckBox);
		
		btnFinish = new JButton("Finish");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedOption = JOptionPane.showConfirmDialog(null, 
                        "Are you sure you want to Finish?", 
                        "Choose", 
                        JOptionPane.YES_NO_OPTION); 
				if (selectedOption == JOptionPane.YES_OPTION) {
					setClipboard("sensitivity " + String.valueOf(df2.format(avg)));
					lblStatus.setText("Status: The average value (" + df2.format(avg) + ") + command copied");
					JOptionPane.showMessageDialog(null, "Congratulations, You've found your perfect sensitvity in " + timesRan + " trys!\n"
							+ "The average value (" + df2.format(avg) + "), aswell as the command, has been copied to your clipboard.", 
							"Congratulations", JOptionPane.INFORMATION_MESSAGE);
					highField.setText("");
					lowField.setText("");
					btnContinue.setEnabled(false);
					avgField.setEnabled(true);
					lowField.setEnabled(false);
					highField.setEnabled(false);
					lowCheckBox.setEnabled(false);
					highCheckBox.setEnabled(false);
					lowCheckBox.setSelected(false);
					highCheckBox.setSelected(false);
					lblMod.setText("");
				}
			}
		});
		btnFinish.setEnabled(false);
		btnFinish.setBounds(295, 94, 89, 23);
		contentPane.add(btnFinish);
		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedOption = JOptionPane.showConfirmDialog(null, 
                        "Are you sure you want to reset?", 
                        "Choose", 
                        JOptionPane.YES_NO_OPTION); 
				if (selectedOption == JOptionPane.YES_OPTION) {
					Reset();
				}
				
			}
		});
		btnReset.setEnabled(false);
		btnReset.setBounds(200, 94, 89, 23);
		contentPane.add(btnReset);
		
		btnContinue = new JButton("Continue");
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (lowCheckBox.isSelected()) {
						timesRan++;
						if(tests < 5) {
							tests++;
						}
						setLowMod(lblMod);
						if(tests >= 2 && tests <= 4) {
							temp = low * LOWMOD2;
							high = avg;
							avg = (avg + temp)/2;
							low = temp;
							lowField.setText(String.valueOf(Double.valueOf(df2.format(low))));
							avgField.setText(String.valueOf(Double.valueOf(df2.format(avg))));
							highField.setText(String.valueOf(Double.valueOf(df2.format(high))));
						}
						else if(tests == 5) {
							temp = low * LOWMOD3;
							high = avg;
							avg = (low + temp)/2;
							low = temp;
							lowField.setText(String.valueOf(Double.valueOf(df2.format(low))));
							avgField.setText(String.valueOf(Double.valueOf(df2.format(avg))));
							highField.setText(String.valueOf(Double.valueOf(df2.format(high))));
						}
						lblStatus.setText("Status: Try " + timesRan + "!");
					}
					if (highCheckBox.isSelected()) {
						timesRan++;
						if(tests < 5) {
							tests++;
						}
						setHighMod(lblMod);
						if(tests >= 2 && tests <= 4) {
							temp = high * HIGHMOD2;
							low = avg;
							avg = (avg + temp)/2;
							high = temp;
							lowField.setText(String.valueOf(Double.valueOf(df2.format(low))));
							avgField.setText(String.valueOf(Double.valueOf(df2.format(avg))));
							highField.setText(String.valueOf(Double.valueOf(df2.format(high))));
						}
						else if(tests == 5) {
							temp = high * HIGHMOD3;
							low = avg;
							avg = (avg + temp)/2;
							high = temp;
							lowField.setText(String.valueOf(Double.valueOf(df2.format(low))));
							avgField.setText(String.valueOf(Double.valueOf(df2.format(avg))));
							highField.setText(String.valueOf(Double.valueOf(df2.format(high))));
							lblStatus.setText("Status: Try " + timesRan + "!");
						}
					}
				} catch(Exception ex) {
					
				}
			}
		});
		btnContinue.setEnabled(false);
		btnContinue.setBounds(295, 60, 89, 23);
		contentPane.add(btnContinue);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {			
				try{
					tests++;
					avg = Double.parseDouble(avgField.getText());
					lowField.setText(String.valueOf(findLow(avg)));
					highField.setText(String.valueOf(findHigh(avg)));
					lblStarting.setText("Avg:");
					btnStart.setEnabled(false);
					btnContinue.setEnabled(true);
					btnReset.setEnabled(true);
					btnFinish.setEnabled(true);
					avgField.setEnabled(false);
					lowCheckBox.setEnabled(true);
					highCheckBox.setEnabled(true);
					
				} catch(NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Try adding a starting value", "Error No value", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnStart.setBounds(200, 60, 89, 23);
		contentPane.add(btnStart);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 404, 21);
		contentPane.add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedOption = JOptionPane.showConfirmDialog(null, 
                        "Are you sure you want to Exit?", 
                        "Choose", 
                        JOptionPane.YES_NO_OPTION); 
				if (selectedOption == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmHowToUse = new JMenuItem("How to use?");
		mntmHowToUse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "1. Find the sensitivity that completes a 360 moving your mouse across your mousepad from one end to the other.\n"
						+ "2. Put that into the starting/avg text field and click 'Start'.\n"
						+ "3. The program will calculate a low and high senstivity based on your starting senstivity, click the text box to copy the senstivity\n"
						+ "and the command. Now test that senstivity by strafing while keeping your crosshair on a fixed target. Switch between the low and high to\n"
						+ "test them both.\n"
						+ "4. Choose the one that most lets you stick on the fixed target, not always the highest, by clicking the checkbox then clicking 'Continue'.\n"
						+ "The program will calculate your new high and low ready to be tested again. As you get more and more into your trys, the modifer gets closer\n"
						+ "to one, making for finer adjustments.\n"
						+ "5. Keep repeating till you can't see a noticable difference with either the high or low setting. Then press 'Finish'. This will copy the.\n"
						+ "average value of the high and low and the command to be put into game.\n"
						+ "\n"
						+ "For more information go to: http://bit.ly/1SmNQfY", 
						"How to use?", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnHelp.add(mntmHowToUse);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Made by Zain Ashraf\n"
						+ "Based on /u/mr_sneakyTV at: http://bit.ly/1SmNQfY",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnHelp.add(mntmAbout);
		
		separator = new JSeparator();
		separator.setBounds(0, 169, 404, 11);
		contentPane.add(separator);
		
		lblStatus = new JLabel("Status:");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblStatus.setBounds(10, 174, 374, 14);
		contentPane.add(lblStatus);
	}
	
	private double findLow(double avg) {
		
		low = avg * LOWMOD1;
		return low;
	}
	
	private double findHigh(double avg) {

		high = avg * HIGHMOD1;
		return high;
	}
	
	private void setHighMod(JLabel lblMod) {
		switch(tests) {
			case 1:
				lblMod.setText(String.valueOf(HIGHMOD1));	break;
			case 2:
				lblMod.setText(String.valueOf(HIGHMOD2));	break;
			case 3:
				lblMod.setText(String.valueOf(HIGHMOD2));	break;
			case 4:
				lblMod.setText(String.valueOf(HIGHMOD2));	break;
			case 5:
				lblMod.setText(String.valueOf(HIGHMOD3));	break;
		}
	}
	
	private void setLowMod(JLabel lblMod) {
		switch(tests) {
			case 1:
				lblMod.setText(String.valueOf(LOWMOD1));	break;
			case 2:
				lblMod.setText(String.valueOf(LOWMOD2));	break;
			case 3:
				lblMod.setText(String.valueOf(LOWMOD2));	break;
			case 4:
				lblMod.setText(String.valueOf(LOWMOD2));	break;
			case 5:
				lblMod.setText(String.valueOf(LOWMOD3));	break;
		}
	}
	
	private void Reset() {
		lblStarting.setText("Starting/Avg:");
		btnStart.setEnabled(true);
		btnContinue.setEnabled(false);
		btnReset.setEnabled(false);
		btnFinish.setEnabled(false);
		avgField.setEnabled(true);
		lowField.setEnabled(false);
		highField.setEnabled(false);
		lowCheckBox.setEnabled(false);
		highCheckBox.setEnabled(false);
		lowCheckBox.setSelected(false);
		highCheckBox.setSelected(false);
		tests = 0;
		timesRan = 1;
		high = 0;
		low = 0;
		avg = 0;
		lowField.setText("");
		avgField.setText("");
		highField.setText("");
		lblMod.setText("");
		lblStatus.setText("Status:");
	}
}
