 /* Tunes Playlist Converter is a project written to fill the gap between
  * iTunes and Android, while not switching to another music management system.
  * Copyright (C) 2011  Christian Larson
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see https://www.gnu.org/licenses/gpl.html */
    	
package obj.MainCode;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.PatternSyntaxException;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.Position;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JTextArea;

public class MainForm extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private static final String title = "Tunes Playlist Converter";  //  @jve:decl-index=0:
	private static final String ver = "v2.0 Beta 2";
	private static JPanel jContentPane = null;
	static ArrayList<String> playlist = new ArrayList<String>();  //  All the playlists available
	static ArrayList<String> list = new ArrayList<String>();  //  Whole library with track names or track ids
	static ArrayList<Integer> currentindex = new ArrayList<Integer>();  //  Selected index from form (JScroll)
	static ArrayList<ArrayList<String>> trackdata = new ArrayList<ArrayList<String>>();  //  Track ID, Name, Location
	static ArrayList<ArrayList<String>> duplicate = new ArrayList<ArrayList<String>>();  //  Track ID, Dup of Track ID, Name Interation
	private static JButton Export = null;
	private static JProgressBar Progress = null;
	private JScrollPane ScrollPane = null;
	private Preferences prefs = Preferences.userRoot().node(this.getClass().getName());  //  @jve:decl-index=0:
	
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private static JList<?> List = null;
	private JMenuBar MainMenuBar = null;
	private JMenu FileMenu = null;
	private JMenu OptionMenu = null;
	private static JCheckBoxMenuItem OptChk1 = null;
	private static JCheckBoxMenuItem OptChk2 = null;
	private static JCheckBoxMenuItem OptChk3 = null;
	private static JCheckBoxMenuItem OptChk4 = null;
	private static JCheckBoxMenuItem OptChk5 = null;
	private JMenu AboutMenu = null;
	private static Integer Skipcntr = 0; 
	private static Integer Newcntr = 0;
	private static Integer Delcntr = 0;

	/**
	 * This method initializes Export	
	 * @return 
	 * 	
	 * @return javax.swing.JButton	
	 */
	
	private JButton getExport() {
		if (Export == null) {
			Export = new JButton();
			Export.setText("Export");
			Export.setEnabled(false);
			Export.setPreferredSize(new Dimension(this.getWidth(),25));
			Export.addActionListener(aListener);
		}
		return Export;
	}

	/**
	 * This method initializes Progress	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getProgress() {
		if (Progress == null) {
			Progress = new JProgressBar();
			Progress.setValue(0);
			Progress.setStringPainted(true);
			Progress.setVisible(false);
		}
		return Progress;
	}

	/**
	 * This method initializes ScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPane() {
		if (ScrollPane == null) {
			ScrollPane = new JScrollPane();
			ScrollPane.setViewportView(getList());
			//ScrollPane.setMinimumSize(new Dimension((this.getWidth()/2),this.getHeight()+25+7));
			//ScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			
		}
		return ScrollPane;
	}

	/**
	 * This method initializes List	
	 * 	
	 * @return javax.swing.JList	
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JList getList() {
		if (List == null) {
			Object[] moveStrings = playlist.toArray();
			List = new JList(moveStrings);
			//System.out.println(moveStrings.length);
			List.setPreferredSize(new Dimension(((this.getWidth()/2)-50),moveStrings.length*18));
			List.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			List.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					if (e.getValueIsAdjusting() == false) {

				        if (List.getSelectedIndex() == -1) {
				        //No selection, disable fire button.
				            Export.setEnabled(false);
				        } else {
				        //Selection, enable the fire button.
				            Export.setEnabled(true);
				            currentindex.clear();
				            for (int i = 0; i < List.getModel().getSize(); i++){
				            	if (List.isSelectedIndex(i)){
				            		currentindex.add(i);
				            		}
				            }
				        }
				    }
					 
				}
			});
		}
		return List;
	}

	/**
	 * This method initializes MainMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getMainMenuBar() {
		if (MainMenuBar == null) {
			MainMenuBar = new JMenuBar();
			MainMenuBar.setPreferredSize(new Dimension(this.getWidth(),25));
			MainMenuBar.add(getFileMenu());
			MainMenuBar.add(getOptionMenu());
			MainMenuBar.add(getAboutMenu());
		}
		return MainMenuBar;
	}

	/**
	 * This method initializes FileMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFileMenu() {
		if (FileMenu == null) {
			FileMenu = new JMenu("File");
			FileMenu.add(new MyMenuItem("Reset")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			FileMenu.add(new JSeparator());
			FileMenu.add(new MyMenuItem("Exit")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		}
		return FileMenu;
	}

	/**
	 * This method initializes OptionMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getOptionMenu() {
		if (OptionMenu == null) {
			OptionMenu = new JMenu("Options");
			OptionMenu.add(getOptChk1());
			OptionMenu.add(getOptChk2());
			OptionMenu.add(getOptChk3());
			OptionMenu.add(getOptChk4());
			OptionMenu.add(getOptChk5());
			OptionMenu.add(new JSeparator());
			JMenuItem temp = new JMenuItem("Clear Settings");
				temp.addActionListener(aListener);
				temp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			OptionMenu.add(temp);
		}
		return OptionMenu;
	}

	/**
	 * This method initializes OptChk1	
	 * 	
	 * @return javax.swing.JCheckBoxMenuItem	
	 */
	private JCheckBoxMenuItem getOptChk1() {
		if (OptChk1 == null) {
			OptChk1 = new JCheckBoxMenuItem("Export Music with Playlists");
			OptChk1.addActionListener(aListener);
			OptChk1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			//Gets & Sets old preferences http://www.vogella.de/articles/JavaPreferences/article.html
			OptChk1.setSelected(prefs.getBoolean("Check1",false));
		}
		return OptChk1;
	}

	/**
	 * This method initializes OptChk2	
	 * 	
	 * @return javax.swing.JCheckBoxMenuItem	
	 */
	private JCheckBoxMenuItem getOptChk2() {
		if (OptChk2 == null) {
			OptChk2 = new JCheckBoxMenuItem("Export in Current Directory");
			OptChk2.addActionListener(aListener);
			OptChk2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			OptChk2.setSelected(prefs.getBoolean("Check2",false));
		}
		return OptChk2;
	}
	
	private JCheckBoxMenuItem getOptChk3() {
		if (OptChk3 == null) {
			OptChk3 = new JCheckBoxMenuItem("Remember Selected Playlists");
			OptChk3.addActionListener(aListener);
			OptChk3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			OptChk3.setSelected(prefs.getBoolean("Check3",true));
		}
		return OptChk3;
	}
	
	private JCheckBoxMenuItem getOptChk4() {
		if (OptChk4 == null) {
			OptChk4 = new JCheckBoxMenuItem("<html>Export Only Android&#0153; Compatible</html>");
			OptChk4.addActionListener(aListener);
			OptChk4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			OptChk4.setSelected(prefs.getBoolean("Check4",true));
		}
		return OptChk4;
	}
	
	private JCheckBoxMenuItem getOptChk5() {
		if (OptChk5 == null) {
			OptChk5 = new JCheckBoxMenuItem("Delete Unused Files and Playlists");
			OptChk5.addActionListener(aListener);
			OptChk5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			OptChk5.setSelected(prefs.getBoolean("Check5",true));
		}
		return OptChk5;
	}
	
	ActionListener aListener = new ActionListener() {
	      public void actionPerformed(ActionEvent event) {
	        AbstractButton aButton = (AbstractButton) event.getSource();  //  @jve:decl-index=0:
	        String text = event.getActionCommand().toString();
	        boolean selected = aButton.getModel().isSelected();
	        
	        if (text == "Export Music with Playlists"){
	        	prefs.putBoolean("Check1",selected);
	        }else if(text == "Export in Current Directory"){
	        	prefs.putBoolean("Check2",selected);
	        }else if(text == "Remember Selected Playlists"){
	        	prefs.putBoolean("Check3",selected);
	        }else if(text == "<html>Export Only Android&#0153; Compatible</html>"){ 
	        	prefs.putBoolean("Check4",selected);
	        }else if(text == "Delete Unused Files and Playlists"){ 
	        	prefs.putBoolean("Check5",selected);
	        }else if(text == "Export"){
	        	new Thread(new Runnable() {@Override public void run() {LoopPlaylists();}}).start();
	        	SelList();
	        }else if(text == "Clear Settings"){
	        	try {
					prefs.clear();
					OptChk1.setSelected(false);
					OptChk2.setSelected(false);
					OptChk3.setSelected(true);
					OptChk4.setSelected(true);
					OptChk5.setSelected(true);
					List.clearSelection();
				} catch (BackingStoreException e) {
					System.out.println("Cannot clear preferences");
				}
	        }
	      }
	    };
	private JScrollPane RightScroll = null;
	private static JTextArea ErrArea = null;
	    
	private void SelList(){//Get index of selected playlists & adds to string
		if (OptChk3.isSelected()){
		String output = null;
		for (int i = 0; i < currentindex.size(); i++){ 
			if (output != null){output = output + "||" + playlist.get(currentindex.get(i));}
			else{output = playlist.get(currentindex.get(i));}}
		prefs.put("SelList",output);}
	}
	
	private void GetSelList(){
		String input = prefs.get("SelList", null);
		if (prefs.getBoolean("Check3",true) && input != null){
		int max = input.split("\\|\\|", -1).length - 1;
		for (int i = 0; i <= max; i++){
			if (i == max && i == 0){
				//Only one playlist selected
				List.setSelectedIndex(List.getNextMatch(input, 0, Position.Bias.Forward));
			}else{
				//More than one playlist selected
				String[] words = input.split("\\|\\|");
				int temp = List.getNextMatch(words[i], 0, Position.Bias.Forward);
				input = input.replaceFirst(words[i], Integer.toString(temp));//Convert to indices
				if (i == max) {
					words = input.split("\\|\\|");
					int intarray[] = new int[words.length];
					for (int j = 0; j < words.length; j++) { //Selected indices from above
						intarray[j] = Integer.parseInt(words[j]);
						}
				List.setSelectedIndices(intarray);}
				}}
		}
	}

	/**
	 * This method initializes AboutMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getAboutMenu() {
		if (AboutMenu == null) {
			AboutMenu = new JMenu("About");
			Border paddingBorder = BorderFactory.createEmptyBorder(0,5,0,7);
			JLabel AboutTxt = new JLabel("<html><center><br>Tunes Playlist Converter<br>Version " + ver + "<br><br><a href=''>Click to visit website.</a><br><br>Copyright (C) 2011  Christian Larson<br>This program comes with ABSOLUTELY<br>NO WARRANTY. This is free software,<br>and you are welcome to redistribute<br>it under certain conditions.<br><br>See website for more info.</center></html>");
			AboutTxt.setBorder(paddingBorder);
			AboutTxt.addMouseListener(new java.awt.event.MouseListener() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					String html = "http://code.google.com/p/tunes-playlist-converter/";
					try {
						java.awt.Desktop.getDesktop().browse(java.net.URI.create(html));
					} catch (IOException e1) {
						System.out.println("Default browser isn't set!");
					}
				}
				public void mousePressed(java.awt.event.MouseEvent e) {
				}
				public void mouseReleased(java.awt.event.MouseEvent e) {
				}
				public void mouseEntered(java.awt.event.MouseEvent e) {
				}
				public void mouseExited(java.awt.event.MouseEvent e) {
				}
			});
			AboutMenu.add(AboutTxt);
		}
		return AboutMenu;
	}

	/**
	 * This method initializes jCheckBoxMenuItem	
	 * 	
	 * @return javax.swing.JCheckBoxMenuItem	
	 */

	/**
	 * This method initializes RightScroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	
	private JScrollPane getRightScroll() {
		if (RightScroll == null) {
			RightScroll = new JScrollPane();
			RightScroll.setViewportView(getErrArea());
		}
		return RightScroll;
	}

	/**
	 * This method initializes ErrArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getErrArea() {
		if (ErrArea == null) {
			ErrArea = new JTextArea();
			ErrArea.setSize(new Dimension((this.getWidth()/2),100));
			ErrArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
			ErrArea.setForeground(Color.RED);
			ErrArea.setMinimumSize(new Dimension(this.getWidth()/2,100));
			ErrArea.setEditable(false);
			//ErrArea.setLineWrap(true);
			//ErrArea.setText("Errors will be written here");
		}
		return ErrArea;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JLabel label = new JLabel("Loading ..."); //Creates & opens 'Loading' dialog
		JPanel center_panel = new JPanel();
		center_panel.add(label);
		JDialog dialog = new JDialog((JFrame)null, "Tunes Playlist Converter");
		dialog.getContentPane().add(center_panel, BorderLayout.CENTER);
		dialog.pack();
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(null);
		
		try{
			// Open the xml file
			String str = null;
			if (System.getProperty("os.name").startsWith("Mac OS X")){
				//TODO Add OSX exception
				//http://developer.apple.com/library/mac/#documentation/Java/Conceptual/Java14Development/00-Intro/JavaDevelopment.html
				str = System.getProperty("user.home") + "/Music/iTunes/";
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //Gets default Swing instead of the Aqua Mac UI
				JOptionPane.showMessageDialog(null,str,"File Location",JOptionPane.INFORMATION_MESSAGE);
			}else if (System.getProperty("os.name").startsWith("Windows")){
					float ver = Float.valueOf(System.getProperty("os.version"));
					
					if (ver >= 5 && ver < 6){ //For Windows 2000 & XP (Ver 5 & 5.1)
						str = System.getProperty("user.home") + "/My Documents/My Music/iTunes/";
					} else if (ver >= 6 && ver < 6.1) { //For Windows Vista (Ver 6)
						str = System.getProperty("user.home") + "/Music/iTunes/";
					} else if (ver >= 6.1 && ver < 6.3){ //For Windows 7 & 8 (Ver 6.1 & 6.2)
						str = System.getProperty("user.home") + "/My Music/iTunes/";
					} else {
						str = "Unsupported version of Windows detected!\n"+
								"Unknown results are expected.";
						JOptionPane.showMessageDialog(dialog,str,"Unknown Windows",JOptionPane.WARNING_MESSAGE);
						//throw new FileNotFoundException("Your system is not supported.");
					}} 
			else {
				str = "Unsupported Operating System detected!\n"+
				"Please use either Windows or Mac OS X supported by iTunes.";
				JOptionPane.showMessageDialog(dialog,str,"Unknown OS",JOptionPane.ERROR_MESSAGE);	
				throw new FileNotFoundException("Your system is not supported.");
				}
					str = str + "iTunes Music Library.xml";
				
				while (new File(str).exists() != true){
					System.out.println("File not found");
					
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Select the 'iTunes Music Library.xml' file");
					chooser.setApproveButtonText("Select");
					chooser.setApproveButtonMnemonic('s');
					chooser.setApproveButtonToolTipText("Select this file");
					chooser.setAcceptAllFileFilterUsed(false);
					chooser.addChoosableFileFilter(new XMLFilter());
					
					int val = chooser.showOpenDialog(null);
					if (val == JFileChooser.APPROVE_OPTION) {
					    str = chooser.getSelectedFile().toString(); 
					} else if(val == JFileChooser.CANCEL_OPTION){//Cancel event
						System.exit(0);
					}else if(val == JFileChooser.ERROR_OPTION){//Err event
						throw new FileNotFoundException("Error on XML file chooser!");
					}
				}
				
				FileInputStream fstream = new FileInputStream(str);
				DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				String strLine;
				Boolean bolPlaylists = false;
				
				String comparison1 = null;
				// Read File Line By Line into ArryLists
				for ( int i = 0; i < 3; i++ ){trackdata.add(new ArrayList<String>());}
				
				while ((strLine = br.readLine()) != null) {
					//Parse Music ID & Location
					if (bolPlaylists != true && strLine.indexOf("<key>Track ID</key><integer>") != -1){ //Track ID
						comparison1 = "<integer>";
						strLine = strLine.substring(strLine.indexOf(comparison1) + comparison1.length(), strLine.indexOf("</integer>"));
						trackdata.get(0).add(strLine);
					}
					if (bolPlaylists != true && strLine.indexOf("<key>Location</key><string>") != -1){ //Track File Name
						comparison1 = "<string>";
						strLine = strLine.substring(strLine.indexOf(comparison1) + comparison1.length(), strLine.indexOf("</string>"));
							strLine = strLine.replace("file://localhost/", "");
							trackdata.get(2).add(Replaces(strLine));
						strLine = strLine.substring(strLine.lastIndexOf("/")+1);
						strLine = Replaces(strLine);
						trackdata.get(1).add(strLine);
					}
					//----------------------------------
					//Parse Playlist Title
					if (bolPlaylists == true && strLine.indexOf("<key>Name</key><string>") != -1){
						comparison1 = "<string>";
						strLine = strLine.substring(strLine.indexOf(comparison1) + comparison1.length(), strLine.indexOf("</string>"));
						strLine = Replaces(strLine);
						list.add(strLine);
						playlist.add(strLine);
					}
					//Parse Playlist Music
					if (bolPlaylists == true && strLine.indexOf("<key>Track ID</key>") != -1){
						comparison1 = "<integer>";
						strLine = strLine.substring(strLine.indexOf(comparison1) + comparison1.length(), strLine.indexOf("</integer>"));
						list.add(strLine);
					}
					//Sets true for Playlist Parse
					if (strLine.indexOf("<key>Playlists</key>") != -1){
						bolPlaylists = true;
					}
				}
				in.close();

				//Collect duplicate track names in ArrayList
				for ( int i = 0; i < 3; i++ ){duplicate.add(new ArrayList<String>());}
				for (int i = 0; i < trackdata.get(0).size(); i++){
					int temp = trackdata.get(1).indexOf(trackdata.get(1).get(i));
					String id = ""; String dupid = "";
					if(temp != i){
						id = trackdata.get(0).get(i); //Proposed duplicate track id
						dupid = trackdata.get(0).get(temp); //First or Orig track id
					}
					duplicate.get(0).add(id);duplicate.get(1).add(dupid);duplicate.get(2).add("");
				}
				//Duplicate track names interation
				for (int i = 0; i < duplicate.get(0).size(); i++){
					int counter = 1;
					if(duplicate.get(0).get(i)!="" && duplicate.get(2).get(i)==""){
						String orgid = duplicate.get(1).get(i);
						for (int j = 0; j < duplicate.get(0).size(); j++){ //Loop to set interation
							String search = duplicate.get(1).get(j);
							if (search.matches(orgid)){
							duplicate.get(2).set(j, Integer.toString(counter));counter++;}
						}
					}
				}
					duplicate.get(1).clear(); //Remove first or orig track id
					duplicate.get(0).removeAll(Arrays.asList(new Object[]{""})); //Clean-up arraylist
					duplicate.get(2).removeAll(Arrays.asList(new Object[]{""})); //Clean-up arraylist
				System.out.println("Finished!");
				Runtime.getRuntime().gc(); //Call garbage collection
				
				}catch (FileNotFoundException e){//catch exception if any
						System.err.println(e.getLocalizedMessage());
				}
				catch (Exception e){
					System.err.println("Error: " + e.getMessage());
				}
				dialog.dispose(); //closes 'Loading' dialog
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainForm thisClass = new MainForm();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	private static String Replaces(String strLine) { //List -> http://kellyjones.netfirms.com/webtools/ascii_utf8_table.shtml
		try {
			strLine = strLine.replace("+", "%2B").replace("&#38;", "&");
			strLine = URLDecoder.decode(strLine,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strLine;
	}
	
	private static boolean AndroidFile(String f){ //Android 2.3 r1 -> http://developer.android.com/guide/appendix/media-formats.html
		boolean bool = false;
		String[] arry = {"3gp","mp4","m4a","mp3","mid","xmf","mxmf","rtttl","rtx","ota","imy","ogg","wav","3gp","mp4"};
		for (int i = 0; i < arry.length; i++){
			if (f.toLowerCase().matches(arry[i])){
				bool = true; break;
			}
		}
		return bool;
	}
	
	private static void DoProgress(String str, Integer i){
			Progress.setString(str + " - " + i + "%");
			Progress.setValue(i);
	}
	
	public static void DisableThings(boolean bool){
		if (bool == false) {jContentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));}
		else{jContentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));}
		
		ErrArea.setText(null);
		List.setEnabled(bool);
		Progress.setVisible(!bool);
		
		if (List.getSelectedIndex() == -1 || !List.isEnabled()) {
	        //No selection, disable fire button.
	            Export.setEnabled(false);
	        } else {Export.setEnabled(true);}
	}
	
	private static String GetDir(){
		JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Select the save directory");
	    chooser.setApproveButtonText("Select");
	    chooser.setApproveButtonToolTipText("Select this directory");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);

	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile().toString();
	    } else {
	    	//return chooser.getCurrentDirectory().toString();
	    	return null;
	    }
	}
	
	private static void LoopPlaylists(){
		//currentindex = selected index from form
		//list = Now updated whole library with track names
		//playlist = All the playlists available
		String str = "Processing";
		Integer pro = 0;
		DoProgress(str,pro); //Updates the Progress bar
		String currentDir = null;
		if (!OptChk2.isSelected()){ currentDir = GetDir(); if (currentDir == null) return;}
		if (OptChk2.isSelected()) currentDir = new File("").getAbsolutePath();
		BufferedWriter output = null;
		File file = null;
		Integer total = 0;
		ArrayList<Integer> playlistmin = new ArrayList<Integer>();
		ArrayList<Integer> playlistmax = new ArrayList<Integer>();
		Progress.setIndeterminate(true);
		DisableThings(false);
		
		for (int i = 0; i < playlist.size(); i++){ //Calculates start & end of playlists
			//System.out.println(playlist.size());
			playlistmin.add(list.indexOf(playlist.get(i))+1);
			if (i+1 != playlist.size()){
				playlistmax.add(list.indexOf(playlist.get(i+1))-1);
			}else {
				playlistmax.add(list.size());
			}
		}
		for (int i = 0; i < currentindex.size(); i++){ //Get index of selected playlists
			str = playlist.get(currentindex.get(i));
			total = playlistmax.get(currentindex.get(i)) - playlistmin.get(currentindex.get(i));
			if (total < 0) total = 0;
			int counter = 0; //Counter for number of writes of a track in playlist file
			
			for (int j = 0; j <= total; j++){
				if (total == 0 )break; //Doesn't output blank playlists
				DoProgress(str,Math.round((((float)j)/total)*100));
				int min = playlistmin.get(currentindex.get(i));
				int max = playlistmax.get(currentindex.get(i));
				int curr = playlistmin.get(currentindex.get(i))+j;
				String track = null;
				String trackid = null;
				int trackidindex = 0;
				
				if (playlistmin.get(currentindex.get(i))+j != list.size()) {
					trackid = list.get(playlistmin.get(currentindex.get(i))+j);
					trackidindex = trackdata.get(0).indexOf(trackid);
					track = trackdata.get(1).get(trackidindex);
					if (!DupFile(trackdata.get(0).get(trackidindex)).equals("0")){//Check for duplicates on ArrayList
						track = new StringBuffer(track).insert(track.lastIndexOf("."), DupFile(trackdata.get(0).get(trackidindex))).toString();
					}
				}
			
				if (curr == min){ //Create Title of Playlist
					try {
						file = new File(Slash(currentDir) + str + ".m3u");
						output = new BufferedWriter(new FileWriter(file));
						if (OptChk1.isSelected()){
								if (OptChk4.isSelected() && AndroidFile(track.substring(track.lastIndexOf(".")+1, track.length()))){
									CopyFile(trackdata.get(2).get(trackidindex),track,currentDir);
									output.write(track);counter++;}
								else if(!OptChk4.isSelected()){
									CopyFile(trackdata.get(2).get(trackidindex),track,currentDir);
									output.write(track);counter++;}
						}else{output.write(track);counter++;}//if unchecked - only playlist
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}else if (curr < max){ //Add Tracks of Playlist
					try {
						if (OptChk1.isSelected()){
							if (OptChk4.isSelected() && AndroidFile(track.substring(track.lastIndexOf(".")+1, track.length()))){
								CopyFile(trackdata.get(2).get(trackidindex),track,currentDir);
								output.write("\r\n" + track);counter++;}
							else if(!OptChk4.isSelected()){
								CopyFile(trackdata.get(2).get(trackidindex),track,currentDir);
								output.write("\r\n" + track);counter++;}
					}else{output.write("\r\n" + track);counter++;}//if unchecked - only playlist
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}else if (curr >= max){ //Close the Playlist
					try {
						if (OptChk1.isSelected()){
							if (track != null){
							if (OptChk4.isSelected() && AndroidFile(track.substring(track.lastIndexOf(".")+1, track.length()))){
								CopyFile(trackdata.get(2).get(trackidindex),track,currentDir);
								output.write("\r\n" + track);counter++;}
							else if(!OptChk4.isSelected()){
								CopyFile(trackdata.get(2).get(trackidindex),track,currentDir);
								output.write("\r\n" + track);counter++;}}
					}else{output.write("\r\n" + track);counter++;}//if unchecked - only playlist
						output.flush();output.close();
						if (counter == 0){file.delete();} //Delete blank playlists
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		//DisableThings(true); //Keep 'Finished!' status shown
		if (OptChk5.isSelected()){
			Progress.setString("Removing Old Playlists...");
			RemovePlaylist(currentDir); //Deletes playlists not selected in dir
			Progress.setString("Removing Old Music Files...");
			RemoveFile(currentDir);} //Deletes files not in playlists created
		jContentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		String top = "Copied: " + Newcntr + "|Removed: " + Delcntr +"|Errors: " + Skipcntr + "\n----------\n";
		ErrArea.setText(top + ErrArea.getText());
		ErrArea.setCaretPosition(0);
		Newcntr = 0;Delcntr = 0;Skipcntr = 0;
		Progress.setString("Finished!");
		Progress.setValue(100);
		Progress.setIndeterminate(false);
	}

	/**
	 * This is the default constructor
	 */
	public MainForm() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		System.setProperty("file.encoding", "UTF-8");
		this.setSize(new Dimension(374, 292));
		this.setResizable(false);
		this.setJMenuBar(getMainMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle(title + " - " + ver);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
			       MainForm.class.getResource("imgs/Logo.png")));
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		this.setLocation((width/2)-200,(height/2)-200);
			String javaver = System.getProperty("java.version");
			javaver = javaver.substring(0, javaver.lastIndexOf("."));
		if (Double.valueOf(javaver) < 1.6){ //Check Java version - 1.6+ only
			String msg = "Tunes Playlist Converter requires Java 6 or higher.\nDownload from www.java.com";
			JOptionPane.showMessageDialog(this,msg,"Old Java Version",JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		
		System.out.println("File system roots returned byFileSystemView.getFileSystemView():");
	    FileSystemView fsv = FileSystemView.getFileSystemView();
	    File[] roots = fsv.getRoots();
	    for (int i = 0; i < roots.length; i++)
	    {
	        System.out.println("Root: " + roots[i]);
	    }

	    System.out.println("Home directory: " + fsv.getHomeDirectory());

	    System.out.println("File system roots returned by File.listRoots():");
	    File[] f = File.listRoots();
	    for (int i = 0; i < f.length; i++)
	    {
	        System.out.println("Drive: " + f[i]);
	        System.out.println("Display name: " + fsv.getSystemDisplayName(f[i]));
	        System.out.println("Is drive: " + fsv.isDrive(f[i]));
	        System.out.println("Is floppy: " + fsv.isFloppyDrive(f[i]));
	        System.out.println("Readable: " + f[i].canRead());
	        System.out.println("Writable: " + f[i].canWrite());
	        System.out.println("Total space: " + f[i].getTotalSpace());
	        System.out.println("Usable space: " + f[i].getUsableSpace());
	    }


	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getExport(), BorderLayout.SOUTH);
			jContentPane.add(getProgress(), BorderLayout.NORTH);
			jContentPane.add(getScrollPane(), BorderLayout.WEST);
			jContentPane.add(getRightScroll(), BorderLayout.CENTER);
			
			GetSelList(); //Get ScrollPane's selected text
		}
		return jContentPane;
	}
	private static String DupFile(String fileid){
		for (int i = 0; i < duplicate.get(0).size(); i++){
			if (duplicate.get(0).get(i) == fileid){ //Match found; return interation
				return duplicate.get(2).get(i);
			}
		}
		return "0";
	}
	//Delete playlists not selected
	private static void RemovePlaylist(String path){
		path = Slash(path);
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
		for (int i = 0; i < listOfFiles.length; i++) 
		{if (listOfFiles[i].isFile()) 
		{files = listOfFiles[i].getName();
			if (files.toLowerCase().endsWith(".m3u")){
				boolean delete = true;
				for (int j = 0; j < currentindex.size(); j++){ //Get index of selected playlists
					if (files.substring(0, files.lastIndexOf('.')).matches(playlist.get(currentindex.get(j)))){
						delete = false;break;
					}}if(delete == true){new File(path+files).delete();Delcntr++;}}
			}
		}
	}
	
	//Delete files not in playlists
	private static void RemoveFile(String path){
		
		int playlistcnt = 0;
		path = Slash(path);
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
		for (int i = 0; i < listOfFiles.length; i++) 
		{if (listOfFiles[i].isFile()) 
		{files = listOfFiles[i].getName();
			if (files.toLowerCase().endsWith(".m3u")){playlistcnt++;}}}
		 
		for (int i = 0; i < listOfFiles.length; i++) 
		{
		if (listOfFiles[i].isFile()) 
		{
		files = listOfFiles[i].getName();
			if (files.toLowerCase().endsWith(".mp3")||files.toLowerCase().endsWith(".m4a")||files.toLowerCase().endsWith(".m4p")||files.toLowerCase().endsWith(".aac")||files.toLowerCase().endsWith(".aiff")||files.toLowerCase().endsWith(".wav")||files.toLowerCase().endsWith(".aa")||files.toLowerCase().endsWith(".m4v")||files.toLowerCase().endsWith(".mov")||files.toLowerCase().endsWith(".mp4")){ //Gets media files in directory
				try {
					String mfiles;
					File mfolder = new File(path);
					File[] mlistOfFiles = mfolder.listFiles(); 
					boolean found = false;
					int counter = 0;
					for (int j = 0; j < mlistOfFiles.length; j++) 
					{
						if (mlistOfFiles[j].isFile()) 
						{
							mfiles = mlistOfFiles[j].getName();
							if (mfiles.toLowerCase().endsWith(".m3u"))//Gets playlist files created in dir
							{
								counter++;
								FileInputStream fstream = new FileInputStream(path + mfiles);
								DataInputStream in = new DataInputStream(fstream);
									BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));

								while (true) {
									String strLine = br.readLine();
								
								if (strLine!=null && files.matches(strLine.replace("(","\\(").replace(")", "\\)").replace("[", "\\[").replace("]","\\]"))){ //File is in this playlist, keep
									found=true;break;
								}else if(strLine==null && playlistcnt <= counter){ //On last playlist, delete if not in it
									new File(path+files).delete();Delcntr++;
									System.out.println(files);
									break;
								}else if(strLine==null){ //End of the current playlist
									break;}
								}in.close();if(found==true)break;
					        }
					     }
					  }
					
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (PatternSyntaxException e){
					System.out.println(e.getLocalizedMessage());
				}
		 	}
		}
		}
	}
	private static void CopyFile(String fileloc, String track, String fileout){
		File f = new File(fileloc);
		fileout = Slash(fileout);
		File o = new File(fileout +  track);
			if (o.exists()){
				if(o.lastModified() < f.lastModified()){
					//Newer file available & overrides
					System.out.println("Newer");
						try {
				        	FileInputStream from = new FileInputStream(f);
							FileOutputStream to = new FileOutputStream(o);
				        byte[] buffer = new byte[4096];
				        int bytesRead;
	
				        while ((bytesRead = from.read(buffer)) != -1) {
				            to.write(buffer, 0, bytesRead);
				        }from.close();to.close();o.setLastModified(f.lastModified()); //Changes Modified date from old file
				        Newcntr++;
				        } catch (FileNotFoundException e) {
				        	System.err.println("Copy Error - Not Found");
				        } catch (IOException e) {
				        	System.err.println("Copy Error - Write Error");
						}
			        }
				else{/*Non-iTunes is newer or same*/}
			}else if(f.exists()){			   
		        try {
		        	FileInputStream from = new FileInputStream(f);
					FileOutputStream to = new FileOutputStream(o);
		        byte[] buffer = new byte[4096];
		        int bytesRead;

		        while ((bytesRead = from.read(buffer)) != -1) {
		            to.write(buffer, 0, bytesRead);
		        }from.close();to.close();o.setLastModified(f.lastModified()); //Changes Modified date from old file
		        Newcntr++;
		        } catch (FileNotFoundException e) {
		        	System.err.println("Copy Error - Not Found");
		        } catch (IOException e) {
		        	System.err.println("Copy Error - Write Error");
				}
		}else{
			Skipcntr++;
			String msg = "Skipped - " + track;
			if(Skipcntr!=1){msg = "\n"+msg;}
			ErrArea.append(msg);
		}
	}
	private static String Slash(String filedir){ //Converts & adds a backslash to file string
		filedir = filedir.replaceAll("\\\\", "/");
		
		if (!filedir.substring(filedir.length()-1).matches("/")){
			filedir = filedir + "/";
		}
		return filedir;
	}

}  //  @jve:decl-index=0:visual-constraint="249,31"
class XMLFilter extends FileFilter {

	  public boolean accept(File f) {
	    if (f.isDirectory())
	      return true;
	    String s = f.getName();
	    int i = s.lastIndexOf('.');

	    if (i > 0 && i < s.length() - 1)
	      if (s.substring(i + 1).toLowerCase().equals("xml"))
	        return true;

	    return false;
	  }

	  public String getDescription() {
	    return "Accepts xml files only.";
	  }
	}

class MyMenuItem extends JMenuItem implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MyMenuItem(String text){
		super(text);
		addActionListener(this);
	}
	public void actionPerformed(ActionEvent e){ 
		String i = e.getActionCommand().toString();
		if (i == "Reset"){
			MainForm.DisableThings(true);
		}else if(i == "Exit"){
			System.exit(ABORT);
		}else{
		System.out.println("Item: " + i);}
	} 
}