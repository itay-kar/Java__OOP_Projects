package gameClient;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame {
	private My_Jpanel panel;
	private Arena _ar;

	public MyFrame(Arena ar,String s) {
		super(s);
		this._ar=ar;
		initFrame();
	}

	private void initFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int height = dimension.height;
		int width = dimension.width;
		this.setSize(width, height);
		this.setVisible(true);

		ImageIcon icon = new ImageIcon("./");
		this.setIconImage(icon.getImage());
		//this.getContentPane().setBackground(Color.blue);
		initPanel();
		this.add(panel);
	}

	public void initPanel(){
		panel = new My_Jpanel(_ar);
	}

	public My_Jpanel getPanel(){
		return panel;
	}
}