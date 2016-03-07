package NotePad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.TextArea;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public class TextEditorPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2710333449427043843L;
	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_HEIGHT = 600;
	
	private JLabel textLabel;
	private JTextField textFileLabel;
	private JTextArea textArea, numbersArea;
	private JScrollPane scrollText;

	public TextEditorPanel(){
		this.setLayout(new BorderLayout());
		
		this.textLabel = new JLabel();
		this.textFileLabel = new JTextField();
		this.textArea = new JTextArea() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3631288978123134917L;

			public void addNotify() {
	            super.addNotify();
	            requestFocusInWindow();
	        }
	    };
		this.numbersArea = new JTextArea("1");
		
		JPanel options = new JPanel();
		options.setLayout(new BorderLayout());
		options.add(textLabel, BorderLayout.WEST);
		options.add(textFileLabel, BorderLayout.CENTER);
		
		this.textLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.textFileLabel.setEditable(false);
		this.textFileLabel.setBackground(null);
		this.textFileLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		this.textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.textArea.setLineWrap(true);
		
		this.scrollText = new JScrollPane(this.textArea);
		scrollText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollText.setBorder(null);
		
		this.numbersArea.setAlignmentX(TextArea.CENTER_ALIGNMENT);
		this.numbersArea.setEditable(false);
		this.numbersArea.setBorder(new EmptyBorder(5, 10, 5, 15));
		this.numbersArea.setBackground(Color.decode("#c2c4c7"));
		this.numbersArea.setForeground(Color.WHITE);
		
		this.add(options, BorderLayout.NORTH);
		this.add(scrollText, BorderLayout.CENTER);
	}

	public JLabel getTextLabel() {
		return textLabel;
	}

	public void setTextLabel(JLabel textLabel) {
		this.textLabel = textLabel;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public JTextField getTextFileLabel() {
		return textFileLabel;
	}

	public void setTextFileLabel(JTextField textFileLabel) {
		this.textFileLabel = textFileLabel;
	}

	public JTextArea getNumbersArea() {
		return numbersArea;
	}

	public void setNumbersArea(JTextArea numbersArea) {
		this.numbersArea = numbersArea;
	}

	public JScrollPane getScrollText() {
		return scrollText;
	}

	public void setScrollText(JScrollPane scrollText) {
		this.scrollText = scrollText;
	}
	
}