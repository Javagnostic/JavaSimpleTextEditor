package NotePad;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.PlainDocument;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;

public class TextEditorMenuBar extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5957742002814014653L;
	private static final String NEW_FILE_LABEL = "New file...";
	private static final String OPEN_FILE_LABEL = "Open file...";
	private static final String FILE_TYPE = ".txt";
	
	private TextEditorPanel panel;
	private JMenuBar editorMenuBar;
	private JMenu fileMenu, editMenu, viewMenu, aboutMenu;
	private JMenuItem newFile, openFile, saveFile, saveAsFile, closeFile,
	exitApp, undo, redo, cutText, copyText, pasteText, selectAllText, findAndReplace, simpleTextEditor;
	private JCheckBoxMenuItem textWrap;
	private JFileChooser chooseFile;
	private boolean change;
	private JTextArea tempOptionArea, tempTextArea, openedTextArea;
	private UndoManager undoManager;
	private JDialog findAndReplacePopup;
	private JDialog aboutPopup;
	private TextEditorFindAndReplace popUpPanel;
	private Preferences prefs;
	private static int lineNumber = 1;
	
	public TextEditorMenuBar(){
		this.setTitle("Simple Text Editor");
		Font menuFont = new Font("San-Serif", Font.PLAIN, 12);
		
		this.panel = new TextEditorPanel();
		this.editorMenuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");
		this.editMenu = new JMenu("Edit");
		this.viewMenu = new JMenu("View");
		this.aboutMenu = new JMenu("About");
		
		this.newFile = new JMenuItem("New", KeyEvent.VK_N);
		this.openFile = new JMenuItem("Open", KeyEvent.VK_O);
		this.saveFile = new JMenuItem("Save", KeyEvent.VK_S);
		this.saveAsFile = new JMenuItem("Save As", KeyEvent.VK_S);
		this.closeFile = new JMenuItem("Close", KeyEvent.VK_W);
		this.exitApp = new JMenuItem("Exit", KeyEvent.VK_Q);
		
		this.undo = new JMenuItem("Undo", KeyEvent.VK_Z);
		this.redo = new JMenuItem("Redo", KeyEvent.VK_Z);
		this.cutText = new JMenuItem(new DefaultEditorKit.CutAction());
		this.cutText.setText("Cut");
		this.copyText = new JMenuItem(new DefaultEditorKit.CopyAction());
		this.copyText.setText("Copy");
		this.pasteText = new JMenuItem(new DefaultEditorKit.PasteAction());
		this.pasteText.setText("Paste");
		this.selectAllText = new JMenuItem("Select All Text", KeyEvent.VK_A);
		this.findAndReplace = new JMenuItem("Find/Replace", KeyEvent.VK_F);
		
		this.textWrap = new JCheckBoxMenuItem("Wrap text", true);
		
		this.simpleTextEditor = new JMenuItem("Simple Text Editor");
		
		this.tempOptionArea = new JTextArea();
		this.tempTextArea = new JTextArea();
		this.openedTextArea = new JTextArea();
		this.undoManager = new UndoManager();
		this.findAndReplacePopup = new JDialog(this, false);
		this.aboutPopup = new JDialog(this, true);
		
		this.prefs = Preferences.userRoot().node(getClass().getName());
		this.chooseFile = new JFileChooser(prefs.get(fileDir(panel.getTextFileLabel().getText()),
			    new File(".").getAbsolutePath()));
		chooseFile.setAcceptAllFileFilterUsed(false);
		chooseFile.setFileFilter(new FileNameExtensionFilter(FILE_TYPE, FILE_TYPE.substring(1)));
		
		this.setJMenuBar(editorMenuBar);
		this.add(panel);
		panel.setVisible(false);
		
		this.addMenuBarMenu(fileMenu, menuFont);
		this.addMenuBarMenu(editMenu, menuFont);
		this.addMenuBarMenu(viewMenu, menuFont);
		this.addMenuBarMenu(aboutMenu, menuFont);
		
		this.addMenuItem(fileMenu, newFile, menuFont, createImageIcon("images/icon_01_New.gif"));
		this.addMenuItem(fileMenu, openFile, menuFont, createImageIcon("images/icon_02_Open.gif"));
		this.addMenuItem(fileMenu, saveFile, menuFont, createImageIcon("images/icon_03_Save.gif"));
		this.addMenuItem(fileMenu, saveAsFile, menuFont, createImageIcon("images/icon_04_SaveAs.gif"));
		this.addMenuItem(fileMenu, closeFile, menuFont, createImageIcon("images/icon_05_Close.gif"));
		this.fileMenu.addSeparator();
		this.addMenuItem(fileMenu, exitApp, menuFont, createImageIcon("images/icon_06_Exit.gif"));
		
		this.addMenuItem(editMenu, undo, menuFont, createImageIcon("images/icon_07_Undo.gif"));
		this.addMenuItem(editMenu, redo, menuFont, createImageIcon("images/icon_08_Redo.gif"));
		this.editMenu.addSeparator();
		this.addMenuItem(editMenu, cutText, menuFont, createImageIcon("images/icon_09_Cut.gif"));
		this.addMenuItem(editMenu, copyText, menuFont, createImageIcon("images/icon_10_Copy.gif"));
		this.addMenuItem(editMenu, pasteText, menuFont, createImageIcon("images/icon_11_Paste.gif"));
		this.addMenuItem(editMenu, selectAllText, menuFont, createImageIcon("images/icon_12_SelectAll.gif"));
		this.editMenu.addSeparator();
		this.addMenuItem(editMenu, findAndReplace, menuFont, createImageIcon("images/icon_13_FindReplace.gif"));
		
		this.addMenuItem(viewMenu, textWrap, menuFont, createImageIcon("images/icon_15_WrapText.gif"));
		
		this.addMenuItem(aboutMenu, simpleTextEditor, menuFont, createImageIcon("images/icon_14_About.gif"));
		
		NewCloseExitListener ncel = new NewCloseExitListener();
		OpenListener ol = new OpenListener();
		SaveListener sl = new SaveListener();
		SaveAsListener sal = new SaveAsListener();
		SelectAllTextWrapListener satwl = new SelectAllTextWrapListener();
		UndoRedoListener url = new UndoRedoListener();
		FindAndReplaceListener frl = new FindAndReplaceListener();
		AboutListener al = new AboutListener();
		DocumentChangeListener dcl = new DocumentChangeListener();
		ExitWindowListener ewl = new ExitWindowListener();
		DropListener dl = new DropListener();
		
		this.newFile.addActionListener(ncel);
		this.openFile.addActionListener(ol);
		this.saveFile.addActionListener(sl);
		this.saveAsFile.addActionListener(sal);
		this.closeFile.addActionListener(ncel);
		this.exitApp.addActionListener(ncel);
		this.undo.addActionListener(url);
		this.redo.addActionListener(url);
		this.selectAllText.addActionListener(satwl);
		this.findAndReplace.addActionListener(frl);
		this.textWrap.addActionListener(satwl);
		this.simpleTextEditor.addActionListener(al);
		this.addWindowListener(ewl);
		
		DropTarget target = new DropTarget(panel.getTextArea(), dl);
		this.setDropTarget(target);
		
		panel.getTextArea().setDocument(new CustomUndoPlainDocument());
		panel.getTextArea().getDocument().addUndoableEditListener(undoManager);
		panel.getTextArea().getDocument().addDocumentListener(dcl);
		
		this.appShortKeys();
		this.enableDisableMenuItems();
		
		findAndReplacePopup.setTitle("Find/Replace");
		findAndReplacePopup.setSize(420, 200);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - findAndReplacePopup.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - findAndReplacePopup.getHeight()) / 2);
	    findAndReplacePopup.setLocation(x, y);
		findAndReplacePopup.setResizable(false);
		
		popUpPanel = new TextEditorFindAndReplace(panel.getTextArea(), findAndReplacePopup);
		
		findAndReplacePopup.add(popUpPanel);
	}

	private class CustomUndoPlainDocument extends PlainDocument {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5735876911026895514L;
		
		private CompoundEdit compoundEdit;

		@Override
		protected void fireUndoableEditUpdate(UndoableEditEvent e) {
			if (compoundEdit == null) {
				super.fireUndoableEditUpdate(e);
			}
			else {
				compoundEdit.addEdit(e.getEdit());
			}
		}

		@Override
		public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			if (length == 0) {
				super.replace(offset, length, text, attrs);
			}
			else {
				compoundEdit = new CompoundEdit();
				super.fireUndoableEditUpdate(new UndoableEditEvent(this, compoundEdit));
				super.replace(offset, length, text, attrs);
				compoundEdit.end();
				compoundEdit = null;
			}
		}
	}
	
	private class DocumentChangeListener implements DocumentListener{

		@Override
		public void changedUpdate(DocumentEvent e) {
			textChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textChange();
			for (int i = lineNumber; i <= panel.getTextArea().getLineCount() - 1; i++){
				lineNumber++;
				panel.getNumbersArea().append("\n" + lineNumber);
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			textChange();
			
			for (int i = lineNumber; i >= 1; i--){
				if (lineNumber > panel.getTextArea().getLineCount()){
					panel.getNumbersArea().setText(panel.getNumbersArea().getText().replace("\n" + lineNumber, ""));
					lineNumber--;
				}
			}
		}
		
	}
	
	private class NewCloseExitListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem clicked = (JMenuItem)e.getSource();
			tempOptionArea.setText(clicked.getText());
			saveChanges();
			if (newFile.getText().equals(clicked.getText())){
				if(NEW_FILE_LABEL.equals(panel.getTextLabel().getText()) || !closeFile.isEnabled()
						|| newFile.getText().equals(tempOptionArea.getText())){
					if(!NEW_FILE_LABEL.equals(panel.getTextLabel().getText()) && !change){
						panel.getTextArea().setText("");
						change = false;
					}
					panel.getTextLabel().setText(NEW_FILE_LABEL);
					panel.getTextFileLabel().setText("");
					panel.setVisible(true);
					panel.getTextArea().requestFocusInWindow();
				}
				else{
					return;
				}
			}
			if (closeFile.getText().equals(clicked.getText())){
				if (!"".equals(tempOptionArea.getText()) && !change){
					clearFrame();
				}
				return;

			}
			if (exitApp.getText().equals(clicked.getText())){
				if (!"".equals(tempOptionArea.getText()) && !change){
					System.exit(0);
				}
				return;
			}
			enableDisableMenuItems();
		}
		
	}
	
	private class OpenListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JMenuItem clicked = (JMenuItem)e.getSource();
			tempOptionArea.setText(clicked.getText());
			saveChanges();
			if (!"".equals(tempOptionArea.getText()) && !change){
				clearFrame();
				chooseFile.setDialogTitle(OPEN_FILE_LABEL);
				chooseFile.setSelectedFile(new File(""));
				int actionDialog = chooseFile.showOpenDialog(chooseFile);
				if (actionDialog == JFileChooser.APPROVE_OPTION){
					openOrDropFile(chooseFile.getSelectedFile(), clicked.getText());
					
				}
				else{
					return;
				}
			}
			else{
				return;
			}
		}
		
	}
	
	private class SaveListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			saveNotSavedFile();
		}
		
	}
	
	private class SaveAsListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			saveAs();
		}
		
	}
	
	private class UndoRedoListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem clicked = (JMenuItem) e.getSource();

			if (undo.getText().equals(clicked.getText())) {
				redo.setEnabled(true);
				if (undoManager.canUndo()) {
					undoManager.undo();
				}
			}
			if (redo.getText().equals(clicked.getText())) {
				if (undoManager.canRedo()) {
					undoManager.redo();
				}
				else{
					redo.setEnabled(false);
				}
			}
		}
		
	}
	
	private class FindAndReplaceListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (panel.getTextArea().getSelectedText() != null){
				popUpPanel.getFindTextArea().setText(panel.getTextArea().getSelectedText());
			}
			if (popUpPanel.getFindTextArea().getText() != null){
				popUpPanel.getFindTextArea().selectAll();
			}
			findAndReplacePopup.setVisible(true);
		}
		
	}
	
	private class SelectAllTextWrapListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem clicked = (JMenuItem)e.getSource();
			if(textWrap.getText().equals(clicked.getText())){
				if(textWrap.isSelected()){
					panel.getTextArea().setLineWrap(true);
					panel.getScrollText().setRowHeaderView(null);
				}
				else{
					panel.getTextArea().setLineWrap(false);
					panel.getScrollText().setRowHeaderView(panel.getNumbersArea());
				}
			}
			if(selectAllText.getText().equals(clicked.getText())){
				panel.getTextArea().selectAll();
			}
		}
		
	}
	
	private class AboutListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			aboutPopup.setTitle("About Simple Text Editor");
			aboutPopup.setSize(420, 200);
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) ((dimension.getWidth() - aboutPopup.getWidth()) / 2);
			int y = (int) ((dimension.getHeight() - aboutPopup.getHeight()) / 2);
			aboutPopup.setLocation(x, y);
			aboutPopup.setResizable(false);
			
			AboutTextEditor aboutPopupPanel = new AboutTextEditor(aboutPopup);
			aboutPopup.add(aboutPopupPanel);		
			
			aboutPopup.setVisible(true);
		}
		
	}
	
	private class ExitWindowListener implements WindowListener{

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosing(WindowEvent e) {
			if (change){
				saveChanges();
				if (!panel.isVisible() || !change){
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
				else{
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
			else{
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class DropListener implements DropTargetListener{

		@Override
		public void dragEnter(DropTargetDragEvent dtde) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dragExit(DropTargetEvent dte) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dragOver(DropTargetDragEvent dtde) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void drop(DropTargetDropEvent dtde) {
			try{
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                
                @SuppressWarnings("unchecked")
				java.util.List<File> list = (java.util.List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                File file = (File) list.get(0);
    			tempOptionArea.setText(OPEN_FILE_LABEL.substring(0, 4));
    			saveChanges();
    			if (!"".equals(tempOptionArea.getText()) && !change){
    				openOrDropFile(file, OPEN_FILE_LABEL.substring(0, 4));
    			}
    			else{
    				return;
    			}
               
            }
			catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private void appShortKeys(){
		newFile.setMnemonic(KeyEvent.VK_N);
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		
		openFile.setMnemonic(KeyEvent.VK_O);
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		
		saveFile.setMnemonic(KeyEvent.VK_S);
		saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		
		saveAsFile.setMnemonic(KeyEvent.VK_S);
		saveAsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		
		closeFile.setMnemonic(KeyEvent.VK_W);
		closeFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		
		exitApp.setMnemonic(KeyEvent.VK_Q);
		exitApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		
		undo.setMnemonic(KeyEvent.VK_Z);
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		
		redo.setMnemonic(KeyEvent.VK_Z);
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.ALT_MASK));
		
		cutText.setMnemonic(KeyEvent.VK_X);
		cutText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		
		copyText.setMnemonic(KeyEvent.VK_C);
		copyText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		
		pasteText.setMnemonic(KeyEvent.VK_V);
		pasteText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		
		findAndReplace.setMnemonic(KeyEvent.VK_F);
		findAndReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		
		selectAllText.setMnemonic(KeyEvent.VK_A);
		selectAllText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
	}
	
	private void chooseFileSetAprove(JFileChooser chooseFile, int actionDialog) throws IOException{
		File fileName = chooseFile.getSelectedFile();
		String fileNameWithExtension = fileName.getAbsolutePath();
		if (!fileNameWithExtension.endsWith(FILE_TYPE)) {
		    fileName = new File(fileNameWithExtension + FILE_TYPE);
		}
		prefs.put(fileDir(panel.getTextFileLabel().getText()), fileName.getParent());
		FileWriter fw = null;
        if(fileName.exists()){
        	actionDialog = JOptionPane.showConfirmDialog(chooseFile, "Replace existing file?", "Choose", JOptionPane.YES_NO_OPTION);
        	if (actionDialog == JFileChooser.APPROVE_OPTION){
        		panel.getTextLabel().setText("File:");
        		panel.getTextFileLabel().setText(chooseFile.getSelectedFile().getAbsolutePath());
        	}
        	else {
        		saveAs();
        		return;
        	}
        }
    	try {
    		panel.getTextLabel().setText("File:");
        	panel.getTextFileLabel().setText(fileName.getAbsolutePath());
			fw = new FileWriter(fileName, false);
			panel.getTextArea().write(fw);
			change = false;
		}
		catch (IOException ioe){
			JOptionPane.showMessageDialog(null, ioe.getMessage());
			ioe.printStackTrace();
		}
    	finally{
    		try {
				fw.close();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
				e1.printStackTrace();
			}
    	}
	}

	private void saveNotSavedFile(){
		if(NEW_FILE_LABEL.equals(panel.getTextLabel().getText())){
			saveAs();
		}
		else{
			FileWriter fw = null;
			try{
				fw = new FileWriter(panel.getTextFileLabel().getText(), false);
				panel.getTextArea().write(fw);
				change = false;
			}
			catch (IOException ioe){
				JOptionPane.showMessageDialog(null, ioe.getMessage());
				ioe.printStackTrace();
			}
			finally{
				try {
					fw.close();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}
	
	private void saveAs(){
		chooseFile.setSelectedFile(new File(""));
		chooseFile.setDialogTitle("Save file As...");
	    int actionDialog = chooseFile.showSaveDialog(this);
	    if (actionDialog == JFileChooser.APPROVE_OPTION){
	    	try {
				chooseFileSetAprove(chooseFile, actionDialog);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
				e1.printStackTrace();
			}
	    }
	    else{
	    	return;
	    }
	}
	
	private void enableDisableMenuItems(){
		if(!panel.isVisible()){
			saveFile.setEnabled(false);
			saveAsFile.setEnabled(false);
			closeFile.setEnabled(false);
			editMenu.setEnabled(false);
			viewMenu.setEnabled(false);
		}
		else{
			saveFile.setEnabled(true);
			saveAsFile.setEnabled(true);
			closeFile.setEnabled(true);
			undo.setEnabled(false);
			redo.setEnabled(false);
			editMenu.setEnabled(true);
			viewMenu.setEnabled(true);
		}
	}
	
	private void textChange(){
		
		if(tempTextArea.getText().equals(panel.getTextArea().getText())){
			change = false;
			undo.setEnabled(false);
		}
		else{
			change = true;
			undo.setEnabled(true);
		}

	}

	private void saveChanges(){
		if(!NEW_FILE_LABEL.equals(panel.getTextLabel().getText()) && panel.getTextArea().getText().equals(openedTextArea.getText())){
			change = false;
		}
		if(change){
			int saveMessage = JOptionPane.showConfirmDialog(chooseFile, "Do you want to save changes?", "Changes", JOptionPane.YES_NO_CANCEL_OPTION);
			if (saveMessage == JOptionPane.YES_OPTION){
				if(newFile.getText().equals(tempOptionArea.getText()) || openFile.getText().equals(tempOptionArea.getText())
						|| closeFile.getText().equals(tempOptionArea.getText()) || exitApp.getText().equals(tempOptionArea.getText())){
					saveNotSavedFile();
					return;
				}
				saveAs();
			}
			else if(saveMessage == JOptionPane.CANCEL_OPTION){
				if(NEW_FILE_LABEL.equals(panel.getTextLabel().getText())){
					tempTextArea.setText("");
				}
				tempOptionArea.setText("");
				return;
			}
			else{
				clearFrame();
			}
		}
	}
	
	private void clearFrame(){
		panel.getTextLabel().setText("");
		panel.getNumbersArea().setText("1");
		lineNumber = 1;
		panel.getTextFileLabel().setText("");
		panel.getTextArea().setText("");
		tempTextArea.setText("");
		openedTextArea.setText("");
		undo.setEnabled(false);
		redo.setEnabled(false);
		change = false;
		panel.setVisible(false);
		enableDisableMenuItems();
	}
	
	private String fileDir (String fileDir){
		String fileDirOnly = "";
		for (int i = fileDir.toCharArray().length - 1; i >= 0; i--){
			fileDirOnly = fileDir.substring(0, i);
			if(fileDir.toCharArray()[i] == '\\'){
				break;
			}
		}
		return fileDirOnly;
	}
	
	private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = this.getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
	
	private void addMenuItem(JMenu menu, JMenuItem menuItem, Font font, ImageIcon icon){
		menuItem.setFont(font);
		menuItem.setIcon(icon);
		menu.add(menuItem);
	}
	
	private void addMenuBarMenu(JMenu menu, Font font){
		menu.setFont(font);
		this.editorMenuBar.add(menu);
	}
	
	private void openOrDropFile(File file, String text){
		FileReader reader = null;
		BufferedReader br = null;
		try{
			File selectedFile = file;
			prefs.put(fileDir(panel.getTextFileLabel().getText()), selectedFile.getParent());
			reader = new FileReader(selectedFile);
			br = new BufferedReader(reader);
			tempOptionArea.read(br, null);
			panel.getTextLabel().setText("File:");
			panel.getTextFileLabel().setText(selectedFile.getAbsolutePath());
			panel.getTextArea().setText(tempOptionArea.getText());
			openedTextArea.setText(panel.getTextArea().getText());
			tempTextArea.setText(panel.getTextArea().getText());
			tempOptionArea.setText(text);
			change = false;
			panel.setVisible(true);
			panel.getTextArea().setCaretPosition(0);
			panel.getTextArea().requestFocusInWindow();
			enableDisableMenuItems();
		}
		catch (IOException ioe){
			JOptionPane.showMessageDialog(null, ioe.getMessage());
			ioe.printStackTrace();
		}
		finally{
			try {
				reader.close();
				br.close();
			} catch (IOException ioe2) {
				JOptionPane.showMessageDialog(null, ioe2.getMessage());
				ioe2.printStackTrace();
			}
		}
	}
	
}