package NotePad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import javax.swing.border.EmptyBorder;

public class TextEditorFindAndReplace extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2827781005671204174L;
	
	private JLabel findTextLabel, replaceTextLabel, message;
	private JTextArea findTextArea, replaceTextArea;
	private JButton findButton, replaceButton, replaceAllButton, doneButton;
	private JCheckBox matchCase;
	private JTextArea textArea;
	private JDialog findAndReplacePopup;
	private static int counter;
	private String findText, inText;
	
	public TextEditorFindAndReplace(JTextArea textArea, JDialog findAndReplacePopup){
		this.textArea = textArea;
		this.findAndReplacePopup = findAndReplacePopup;
		
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(10, 5, 10, 10));
		
		this.findTextLabel = new JLabel("Find text:");
		this.replaceTextLabel = new JLabel("Replace with:");
		this.findTextArea = new JTextArea(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 5633685287762116224L;

			public void addNotify() {
	            super.addNotify();
	            requestFocusInWindow();
	        }
	    };
	    this.replaceTextArea = new JTextArea();
		this.findButton = new JButton("Find Next");
		this.replaceButton = new JButton("Replace");
		this.replaceAllButton = new JButton("Replace All");
		this.doneButton = new JButton("Done");
		this.matchCase = new JCheckBox("Match Case", false);
		this.message = new JLabel("");
		this.message.setFont(getFont());
		this.message.setForeground(Color.RED);
		this.findText = "";
		this.inText = "";
		
		JScrollPane scrollFindText = new JScrollPane(findTextArea);
		scrollFindText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		findTextArea.setLineWrap(true);
		
		JScrollPane scrollReplaceText = new JScrollPane(replaceTextArea);
		scrollReplaceText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		replaceTextArea.setLineWrap(true);
		
		JPanel textField = new JPanel();
		textField.setLayout(new BoxLayout(textField, BoxLayout.Y_AXIS));
		textField.setBorder(new EmptyBorder(0, 5, 0, 10));
		
		JPanel findPanel = new JPanel();
		findPanel.setLayout(new BorderLayout());
		findPanel.add(findTextLabel, BorderLayout.NORTH);
		findTextLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
		findPanel.add(scrollFindText, BorderLayout.CENTER);
		findPanel.setBorder(new EmptyBorder(0, 0, 5, 0));
		
		JPanel replacePanel = new JPanel();
		replacePanel.setLayout(new BorderLayout());
		replacePanel.add(replaceTextLabel, BorderLayout.NORTH);
		replaceTextLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
		replacePanel.add(scrollReplaceText, BorderLayout.CENTER);
		
		textField.add(findPanel);
		textField.add(replacePanel);
		
		JPanel buttonsField = new JPanel();
		buttonsField.setPreferredSize(new Dimension(120, this.getHeight()));
		buttonsField.setLayout(new GridLayout(6, 1, 5, 5));
		this.addButton(buttonsField, findButton, getFont(), Color.WHITE);
		this.addButton(buttonsField, replaceButton, getFont(), Color.WHITE);
		this.addButton(buttonsField, replaceAllButton, getFont(), Color.WHITE);
		this.addButton(buttonsField, doneButton, getFont(), Color.WHITE);
		buttonsField.add(matchCase);
		buttonsField.add(message);
		
		this.add(textField, BorderLayout.CENTER);
		this.add(buttonsField, BorderLayout.EAST);
		
		FindNextListener fnl = new FindNextListener();
		ReplaceListener rl = new ReplaceListener();
		CloseListener cl = new CloseListener();
		CloseWindowListener cwl = new CloseWindowListener();
		FindTextAreaMouseListener ftaml = new FindTextAreaMouseListener();
		
		findTextArea.addMouseListener(ftaml);
		findButton.addActionListener(fnl);
		replaceButton.addActionListener(rl);
		replaceAllButton.addActionListener(rl);
		doneButton.addActionListener(cl);
		doneButton.registerKeyboardAction(cl, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		findAndReplacePopup.addWindowListener(cwl);
	}

	private class FindNextListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			findTextAreaFocus();
			message.setText("");
			matchCaseText();
			if ("".equals(textArea.getText()) || "".equals(findTextArea.getText())) {
				message.setText("Empty field(s)!");
			}
			else{
				if (inText.contains(findText)) {
					if (counter != 0 && textArea.getCaretPosition() != 0){
						counter = textArea.getCaretPosition();
					}
					else{
						counter = 0;
					}
					for (int i = counter; counter < inText.length(); counter++) {
						int selectionStart = inText.indexOf(findText);
						int selectionNext = inText.indexOf(findText, i);
						int selectionEnd = findText.length();

						if (selectionStart == counter) {
							textArea.select(selectionStart, selectionStart + selectionEnd);
							counter++;
							return;
						}
						if (selectionNext == counter) {
							textArea.select(selectionNext, selectionNext + selectionEnd);
							counter++;
							return;
						}
					}
				}
				else {
					message.setText("Not Found!");
				}
			}
			if (counter >= inText.length()){
				counter = 0;
			}
		}
	
	}
	
	private class ReplaceListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			findTextAreaFocus();
			message.setText("");
			JButton clicked = (JButton)e.getSource();
			matchCaseText();			
			if (inText.contains(findText)) {
				if (replaceButton.getText().equals(clicked.getText())) {
					if (textArea.getSelectedText() != null) {
						textArea.replaceSelection(replaceTextArea.getText());
					}
					else {
						message.setText("No text selected!");
					}
				}
				if (replaceAllButton.getText().equals(clicked.getText())) {
					if (!"".equals(findTextArea.getText())) {
						textArea.setText(inText.replace(findText, replaceTextArea.getText()));
						message.setText("");
					}
					else {
						message.setText("Replace what?");
					}
				}
			}
			else {
				message.setText("Not Found!");
			}
		}
		
	}

	private class CloseListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			message.setText("");
			findAndReplacePopup.setVisible(false);
		}
		
	}
	
	private class CloseWindowListener implements WindowListener{

		@Override
		public void windowActivated(WindowEvent e) {
			message.setText("");
			findTextAreaFocus();
		}

		@Override
		public void windowClosed(WindowEvent e) {
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			
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
	
	private class FindTextAreaMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			findAndReplacePopup.toFront();
			findTextArea.selectAll();
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private void addButton(JPanel panel, JButton button, Font font, Color color){
		button.setSize(new Dimension(100, 20));
		button.setFont(font);
		button.setBackground(color);
		button.setFocusPainted(false);
		panel.add(button);
	}
	
	private void matchCaseText(){
		if (matchCase.isSelected()){
			findText = findTextArea.getText();
			inText = textArea.getText();
		}
		else {
			findText = findTextArea.getText().toLowerCase();
			inText = textArea.getText().toLowerCase();
		}
	}
	
	private void findTextAreaFocus(){
		findTextArea.requestFocusInWindow();
	}

	public JTextArea getFindTextArea() {
		return findTextArea;
	}

}