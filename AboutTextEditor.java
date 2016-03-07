package NotePad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

public class AboutTextEditor extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6770557592457196783L;
	
	private JDialog aboutPopup;
	private JTextArea title, infoText, contactInfo;
	private JButton confirmButton;
	private EmptyBorder emptyBorder;
	
	public AboutTextEditor(JDialog aboutPopup){
		this.aboutPopup = aboutPopup;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.emptyBorder = new EmptyBorder(20, 20, 20, 20);
		this.setBorder(emptyBorder);
		Font titleFont = new Font("San-Serif", Font.BOLD, 16);
		Font contactFont = new Font("San-Serif", Font.PLAIN, 11);
		
		this.title = new JTextArea("Simple Text Editor");
		this.infoText = new JTextArea("This program was created for the purposes of SoftAcad's course\n"
				+ "\"Programing with Java\".");
		this.contactInfo = new JTextArea("Autor: Svetlin Burgov\n"
				+ "e-mail: info@standart.xyz");
		
		this.confirmButton = new JButton("OK");
		this.confirmButton.setBackground(Color.WHITE);
		this.confirmButton.setFont(getFont());
		this.confirmButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		this.confirmButton.setSize(new Dimension(100, 20));
		this.confirmButton.setFocusPainted(false);
		
		this.addInfoElement(title, titleFont);
		this.addInfoElement(infoText, getFont());
		this.addInfoElement(contactInfo, contactFont);
		
		this.add(confirmButton);
		
		CloseListener cl = new CloseListener();
		
		confirmButton.addActionListener(cl);
		this.confirmButton.registerKeyboardAction(cl, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		
	}
	
	private class CloseListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			aboutPopup.setVisible(false);
		}
		
	}
	
	private void addInfoElement(JTextArea text, Font font){
		text.setFont(font);
		text.setBorder(new EmptyBorder(0, 0, 10, 0));
		text.setBackground(null);
		text.setEditable(false);
		this.add(text);
	}

}