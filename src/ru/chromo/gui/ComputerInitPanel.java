package ru.chromo.gui;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import ru.chromo.objects.Computer;


public class ComputerInitPanel extends JPanel{

    private JLabel lblName;
    private JLabel lblDelayLevel;
    private JTextField txtDelayLevel;
    private JLabel lblRate;
    private JTextField txtRate;
    private JLabel lblGenType;
    private JComboBox cbGenType;
    
    
    
    public ComputerInitPanel(int number) {
        super();
        lblName = new JLabel("Компьютер "+number);
        lblDelayLevel = new JLabel("Уровень запаздывания");
        txtDelayLevel = new JTextField();
        txtDelayLevel.setInputVerifier(new doubleVerifier());
        lblRate = new JLabel("Доверительный уровень");
        txtRate = new JTextField();
        txtRate.setInputVerifier(new rateVerifier());
        lblGenType = new JLabel("Тип генератора");
        Object[] cbModel = new Object[] {Computer.GENERATOR_TYPE_NORMAL+" : Нормальная генерация",
            Computer.GENERATOR_TYPE_EVEN+" : Равномерная генерация",
            Computer.GENERATOR_TYPE_TRIANGULAR+" : Треугольная генерация"};
        cbGenType = new JComboBox(cbModel);
        
        setLayout(new GridLayout(1, 0));
        add(lblName);
        add(lblDelayLevel);
        add(txtDelayLevel);
        add(lblRate);
        add(txtRate);
        add(lblGenType);
        add(cbGenType);
        
    }
    
    class doubleVerifier extends InputVerifier {
        
        @Override
        public boolean verify(JComponent input) {
            JTextComponent txt = (JTextComponent)input;
            String text = txt.getText();
            if(text.matches("^[\\d]+[.][\\d]+")) {
                txt.setBackground(Color.white);
                return true;
            } else if(text.length()<1) {
                txt.setBackground(Color.white);
                return true;
            } 
            else {
                txt.setBackground(Color.red);
                return false;
            }
        }
        
    }
    
    class rateVerifier extends InputVerifier {
        
        @Override
        public boolean verify(JComponent input) {
            JTextComponent txt = (JTextComponent)input;
            String text = txt.getText();
            if(text.matches("^[0][.][\\d]+")) {
                txt.setBackground(Color.white);
                return true;
            } else if(text.length()<1) {
                txt.setBackground(Color.white);
                return true;
            } 
            else {
                txt.setBackground(Color.red);
                return false;
            }
        }
        
    }
    
    class integerVerifier extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            JTextComponent txt = (JTextComponent)input;
            String text = txt.getText();
            if(text.matches("^\\d+")) {
                return true;
            } else if(text.length()<1) {
                return true;
            } 
            else {
                return false;
            }
        }
        
    }

    public JLabel getLblName() {
        return lblName;
    }

    public JLabel getLblDelayLevel() {
        return lblDelayLevel;
    }

    public JTextField getTxtDelayLevel() {
        return txtDelayLevel;
    }

    public JLabel getLblRate() {
        return lblRate;
    }

    public JTextField getTxtRate() {
        return txtRate;
    }

    public JLabel getLblGenType() {
        return lblGenType;
    }

    public JComboBox getCbGenType() {
        return cbGenType;
    }

    public void setLblName(JLabel lblName) {
        this.lblName = lblName;
    }

    public void setLblDelayLevel(JLabel lblDelayLevel) {
        this.lblDelayLevel = lblDelayLevel;
    }

    public void setTxtDelayLevel(JTextField txtDelayLevel) {
        this.txtDelayLevel = txtDelayLevel;
    }

    public void setLblRate(JLabel lblRate) {
        this.lblRate = lblRate;
    }

    public void setTxtRate(JTextField txtRate) {
        this.txtRate = txtRate;
    }

    public void setLblGenType(JLabel lblGenType) {
        this.lblGenType = lblGenType;
    }

    public void setCbGenType(JComboBox cbGenType) {
        this.cbGenType = cbGenType;
    }
    
    
    
}
