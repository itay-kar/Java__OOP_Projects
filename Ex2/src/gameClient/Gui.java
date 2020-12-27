package gameClient;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame implements ActionListener {
    JButton button = new JButton("Start");
    JLabel id;
    JLabel level;
    JLabel notice;
    JTextField idfield = new JTextField(9);
    JTextField levelfield = new JTextField(2);
    String idString =null;
    String levelString=null;
    boolean f;


    public Gui() {
        id=new JLabel();
        level=new JLabel();

        ImageIcon icon = new ImageIcon("C:\\Software development\\Java Directory\\Tasks\\Java-Projects\\Java__OOP_Projects\\Ex2\\photos\\pickachujpg.jpg");
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width - (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.75), Toolkit.getDefaultToolkit().getScreenSize().height - (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.60));
        id.setText("Please Enter Id\n");
        id.setBounds(50, 50, 100, 100);
        level.setText("Enter level number between 0-23\n");
        level.setBounds(50, 70, 100, 100);
        button.addActionListener(this);

        this.add(id);
        this.add(idfield);
        this.add(level);
        this.add(levelfield);

        this.add(button);

        this.setLayout(new FlowLayout());

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(ifRight()){
            Ex2.gameLevel=levelfield.getText();
            Ex2.id=idfield.getText();
            Thread client = new Thread(new Ex2());
            client.start();
            this.setVisible(false);
        }
        else{
            notice=new JLabel();
            notice.setText("You've entered wrong details");
            this.add(notice);
            this.update(this.getGraphics());
            this.remove(notice);
        }
    }

    private boolean ifRight() {
        this.update(this.getGraphics());
        if (idfield.getText().length()!=9){
            return false;
        }
        if(Integer.parseInt(levelfield.getText())>23||Integer.parseInt(levelfield.getText())<0){
            return false;
        }
        return true;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public String getLevelString() {
        return levelString;
    }

    public void setLevelString(String levelString) {
        this.levelString = levelString;
    }

}

