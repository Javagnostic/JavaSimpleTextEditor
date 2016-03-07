package NotePad;

import java.awt.Dimension;
import java.awt.Toolkit;

public class TextEditor {

	public static void main(String[] args) {

		TextEditorMenuBar frame = new TextEditorMenuBar();
		frame.setSize(TextEditorPanel.FRAME_WIDTH, TextEditorPanel.FRAME_HEIGHT);
				
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
				
		frame.setVisible(true);

	}

}