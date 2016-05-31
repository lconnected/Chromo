
package ru.chromo.objects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Chromosome {

    private int[] works; //работы
    Object[][] worksList; //значения, полученные при генерации хромосомы
    
    private int[] dealing; //распределение
    Object[][] dealingList; //значения, полученные при генерации хромосомы
    
    private Double delayLevelMax; //уровень запаздывания max
    private Double[] delayLevel; //уровень запаздывания
    private Double[] endurance;
    private Double enduranceMax;
    
    private Double eval; //вероятность воспроизведения
    private Double qm; //кумулятивная вероятность
    private Double r; //???
    
    public ArrayList<Object[][]> calcResult;
    private Double c = null;
    private Integer m = null;
    private Chromosome parent1 = null;
    private Chromosome parent2 = null;
    private ArrayList<Double> crossoverValues = null;
    private ArrayList<Integer> mutationValues = null;
    
    private Double rm; //rm - для операции кроссинговера
    
    
    public Chromosome(int[] works, int[] dealing) throws Exception {
            this.works = works;
            this.dealing = dealing;
            this.calcResult = new ArrayList<>();
            this.delayLevel = new Double[dealing.length+1];
            this.endurance = new Double[dealing.length+1];
    }
    
    /**
     * Определяет (создает) хромосому случайным образом
     */
    public Chromosome(int worksSize, int dealingSize) {
        
            this.works = getRandomWorks(worksSize);
            this.dealing = getRandomDealing(dealingSize);
            this.calcResult = new ArrayList<>();
            this.delayLevel = new Double[dealingSize+1];
            this.endurance = new Double[dealingSize+1];
    }
    
    /**
     * Определяет (создает) хромосому - потомка по результату операции кроссинговера
     */
    public Chromosome(ArrayList<Double> values, int worksSize, int dealingSize) {
        
            this.crossoverValues = values;
            this.dealing = new int[dealingSize];
            this.works = new int[worksSize];
            HashMap<Double, Integer> worksMap = new HashMap<>();
            ArrayList<Double> worksList = new ArrayList<>();
            for (int i = 0; i < worksSize; i++) {
                worksMap.put(values.get(i), i+1);
                worksList.add(values.get(i));
            }
            Collections.sort(worksList);
            for (int i = 0; i < worksSize; i++) {
                this.works[i] = worksMap.get( worksList.get(i) );
            }
            for (int i = 0; i < dealingSize; i++) {
                this.dealing[i] = (int)Math.floor( values.get(i+worksSize) );
            }
            
            
            this.calcResult = new ArrayList<>();
            this.delayLevel = new Double[dealing.length+1];
            this.endurance = new Double[dealing.length+1];
        
        
    }
    
    /**
     * Определяет (создает) хромосому - потомка по результату операции мутации
     */
    public Chromosome(int worksSize, int dealingSize, ArrayList<Integer> values) {
        
            this.mutationValues = values;
            this.dealing = new int[dealingSize];
            this.works = new int[worksSize];
            
            for (int i = 0; i < this.mutationValues.size(); i++) {
                if(i<worksSize) {
                    int vl = this.mutationValues.get(i);
                    if(vl == worksSize+1) { vl = 1; }
                    if(vl == 0) { vl = worksSize; }
                    this.works[i] = vl;
//                    System.err.println(vl);
                } else {
                    int vl = this.mutationValues.get(i);
                    if(vl > worksSize) { vl = worksSize; }
                    if(vl == 0) { vl = 1; }
                    this.dealing[i-worksSize] = vl;
//                    System.err.println(vl);
                }
                
            }
            
            this.calcResult = new ArrayList<>();
            this.delayLevel = new Double[dealing.length+1];
            this.endurance = new Double[dealing.length+1];
        
    }
    
    public Integer getRandom(int min, int max) {
        Integer random = 0;
        random = min + (int)(Math.random() * ((max - min) + 1));
        return random;
    }
    
    private int[] getRandomWorks(int length) {
        
        int[] result = new int[length];
        ArrayList<Double> diList = new ArrayList<>();
        worksList = new Object[length+1][4];
        worksList[0][0] = "i";
        worksList[0][1] = "Ui";
        worksList[0][2] = "di";
        worksList[0][3] = "позиция";
        
        for (int i = 1; i < worksList.length; i++) { 
            
            worksList[i][0] = i;
            Double ui = getRandom();
            worksList[i][1] = ui;
            Double di = 1 + ui*(length-1);
            worksList[i][2] = di;
            diList.add(di);
            
        }
        Collections.sort(diList);
        for (int i = 0; i < diList.size(); i++) {
            for (int j = 1; j < worksList.length; j++) {
                if( worksList[j][2].equals(diList.get(i)) ) {
                    worksList[j][3] = i+1;
                }
            }
        }
        
        for (int i = 1; i < worksList.length; i++) {
            result[i-1] = (int)worksList[i][3];
        }
        
        return result;
    }
    
    private int[] getRandomDealing(int length) {
        
        int[] result = new int[length];
        ArrayList<Integer> dpList = new ArrayList<>();
        dealingList = new Object[length+1][5];
        dealingList[0][0] = "p";
        dealingList[0][1] = "Up";
        dealingList[0][2] = "dp";
        dealingList[0][3] = "округление";
        dealingList[0][4] = "позиция";
        
        for (int i = 1; i < dealingList.length; i++) { 
            
            dealingList[i][0] = this.works.length + i;
            Double up = getRandom();
            dealingList[i][1] = up;
            Double dp = 1 + up*(this.works.length-1); //по формуле
            dealingList[i][2] = dp;
            Integer dpRounded = (int)Math.round(dp);
            dealingList[i][3] = dpRounded;
            
            dpList.add(dpRounded);
            
        }
        Collections.sort(dpList);
        for (int i = 0; i < dpList.size(); i++) {
            for (int j = 1; j < dealingList.length; j++) {
                if( dealingList[j][3].equals(dpList.get(i)) ) {
                    dealingList[j][4] = i+1;
                }
            }
        }
        
        Collections.sort(dpList);
        for (int i = 0; i < dpList.size(); i++) {
            result[i] = dpList.get(i);
        }
        
        return result;
    }
    
    public ArrayList<Double> dblRandoms;
    public Double getRandom() {
        if(dblRandoms==null) {
            dblRandoms=new ArrayList<>();
        }
        Double random;
        boolean tag=true;
        random = Math.random();
        
        dblRandoms.add(random);
        return random;
    }
    
    
    /**
     * 
     * @param part значение 1, 2 или 3
     * @return 
     */
    public int[] getPart(int part) {
        
        int begin, end;
        int[] result;
        int resultSize=0;
        
        if(part==1) {
            begin = 0;
            end = getDealing()[part-1]-1;
        } else {
            begin = getDealing()[part-2]-1;
            try {    
                end = getDealing()[part-1]-1;
            } catch(ArrayIndexOutOfBoundsException ex) {
                end = works.length;
            }
        }
        
        for (int i = begin; i < end; i++) {
            resultSize++;
        }
        result = new int[resultSize]; //инициализация результата определенной длины
//        System.out.println(resultSize);
        for (int i = 0; i < resultSize; i++) {
                result[i] = getWorks()[i+begin];
        }
        return result;
    }
    
    public void resetCalcResult() {
        calcResult.clear();
        calcResult = new ArrayList<>();
        eval = null; //вероятность воспроизведения
        qm = null; //кумулятивная вероятность
        r = null; 
        c = null;
        m = null;
        parent1 = null; //родители
        parent2 = null;
        crossoverValues = null;
        mutationValues = null;
        rm = null; //rm - для операции кроссинговера
    }
    
    public JPanel renderChromosome(int number) {
        
        JPanel panel = new JPanel();
        JComponent panWorks = createTable(getRawChromosome());
        JComponent panWorksList=null;
        JComponent panDealingList=null;
        try {
        panWorksList = createTable(worksList);
        panDealingList = createTable(dealingList);
        } catch(NullPointerException ex) {
            //хромосома получена не случайным образом
            
        }
        
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        GridBagConstraints c = new  GridBagConstraints();
        
        c.insets = new Insets(5,5,5,5);  //top padding
//        c.anchor = GridBagConstraints.PAGE_START; //bottom of space
        c.fill = GridBagConstraints.VERTICAL;
        
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Хромосома " + number), c);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(panWorks, c);
        
        try {
        c.gridx = 0;
        c.gridy = 1;
        panel.add(panWorksList, c);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(panDealingList, c);
        } catch(NullPointerException ex) {
//            System.err.println("--неслучайная генерация--");
            //render child
        }
        
        if(this.c!=null) {
            Object[][] out = new Object[parent1.getRawChromosome().length][1];
            JPanel pOut = new JPanel(new GridLayout(0, 1));
            for (int i = 0; i < parent1.getRawChromosome().length; i++) {
                if(number == 1) {
                    out[i][0] = (String)(""+this.c+"*"+parent1.getRawChromosome()[i]+" + (1 - "+this.c+")*"+parent2.getRawChromosome()[i]+" = "+crossoverValues.get(i));
                } else {
                    out[i][0] = (String)(""+this.c+"*"+parent2.getRawChromosome()[i]+" + (1 - "+this.c+")*"+parent1.getRawChromosome()[i]+" = "+crossoverValues.get(i));
                }
                pOut.add(new JLabel((String)out[i][0]));
            }
            c.gridx=1;
            c.gridy++;
            
            panel.add(pOut, c);
        } else if(this.m != null) {
            Object[][] out = new Object[parent1.getRawChromosome().length][1];
            JPanel pOut = new JPanel(new GridLayout(0, 1));
            for (int i = 0; i < parent1.getRawChromosome().length; i++) {
                if(number == 1) {
                    out[i][0] = (String)""+parent1.getRawChromosome()[i]+"-"+this.m;
                } else {
                    out[i][0] = (String)""+parent2.getRawChromosome()[i]+"+"+this.m;
                }
                pOut.add(new JLabel((String)out[i][0]));
            }
            c.gridx=1;
            c.gridy++;
            
            panel.add(pOut, c);
        }
        
        c.gridy++;
        for (int i = 0; i < calcResult.size(); i++) {

            JComponent comp = createTable(calcResult.get(i));

            c.gridx = 0;
            c.gridy += i;
            panel.add(new JLabel("Компьютер "+(i+1)), c);
            c.gridx = 1;
            c.gridy += i;
            panel.add(comp,c);
            
        }
        
        c.gridx = 1;
        c.gridy++;
        panel.add(new JLabel("EVmax = "+getEnduranceMax()), c); 
        
        
        return panel;
    }
    
    private JComponent createTable( Object[][] list ) {
      
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        DefaultTableModel model = new DefaultTableModel(list, list[0]);
        
        JTable table = new JTable(model);
        panel.add(table);
        
        return panel;
    }
    
    private JComponent createTable( Object[] array ) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        DefaultTableModel model = new DefaultTableModel(null, array);
        model.addRow(array);
        
        JTable table = new JTable(model);
        panel.add(table);
        
        return panel;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Chromosome c = (Chromosome) obj;
            //выполняется сравнение по массиву(коллекции) работ
            return Arrays.equals(c.getWorks(), this.getWorks());
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * @return the delayLevelMax
     */
    public Double getDelayLevelMax() {
        return delayLevelMax;
    }

    /**
     * @return the delayLevel
     */
    public Double getDelayLevel(int part) {
        return delayLevel[part-1];
    }

    /**
     * @return the endurance
     */
    public Double getEndurance(int part) {
        return endurance[part-1];
    }

    /**
     * @return the enduranceMax
     */
    public Double getEnduranceMax() {
        return enduranceMax;
    }

    /**
     * @param delayLevelMax the delayLevelMax to set
     */
    public void setDelayLevelMax(Double delayLevelMax) {
        this.delayLevelMax = delayLevelMax;
    }

    /**
     * @param delayLevel the delayLevel to set
     */
    public void setDelayLevel(Double delayLevel, int part) {
        this.delayLevel[part-1] = delayLevel;
    }

    /**
     * @param endurance the endurance to set
     */
    public void setEndurance(Double endurance, int part) {
        this.endurance[part-1] = endurance;
    }

    /**
     * @param enduranceMax the enduranceMax to set
     */
    public void setEnduranceMax(Double enduranceMax) {
        this.enduranceMax = enduranceMax;
    }

    @Override
    public String toString() {
        
        String str="";
        for (int i = 0; i < works.length; i++) {
            str+=getWorks()[i]+" ";
        }
        for (int i = 0; i < dealing.length; i++) {
            str+=getDealing()[i]+" ";
        }
        
        str += "\n-\n";
        try {
        for (int i = 0; i < worksList.length; i++) {
            for (int j = 0; j < worksList[i].length; j++) {
                str+= worksList[i][j]+" ";
            }
            str+="\n";
        }
        
        str += "\n-\n";
        for (int i = 0; i < dealingList.length; i++) {
            for (int j = 0; j < dealingList[i].length; j++) {
                str+= dealingList[i][j]+" ";
            }
            str+="\n";
        }
        } catch(NullPointerException ex) {
            str+="Хромосома получена не случайным образом\n";
        }
        
        return str; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the rm
     */
    public Double getRm() {
        return rm;
    }

    /**
     * @param rm the rm to set
     */
    public void setRm(Double rm) {
        this.rm = rm;
    }
    
    public Object[] getRawChromosome() {
        Object[] rc = new Object[works.length+dealing.length];
        for (int i = 0; i < rc.length; i++) {
            
            if(i<works.length) {
                rc[i] = getWorks()[i];
            } else {
                rc[i] = getDealing()[i-works.length];
            }
        }
        return rc;
    }

    /**
     * @return the works
     */
    public int[] getWorks() {
        return works;
    }

    /**
     * @return the dealing
     */
    public int[] getDealing() {
        return dealing;
    }

    public Double getC() {
        return c;
    }

    public Chromosome getParent1() {
        return parent1;
    }

    public Chromosome getParent2() {
        return parent2;
    }

    public void setC(Double c) {
        this.c = c;
    }

    public void setParent1(Chromosome parent1) {
        this.parent1 = parent1;
    }

    public void setParent2(Chromosome parent2) {
        this.parent2 = parent2;
    }

    public Integer getM() {
        return m;
    }

    public void setM(Integer m) {
        this.m = m;
    }

    public Double getEval() {
        return eval;
    }

    public Double getQm() {
        return qm;
    }

    public void setEval(Double eval) {
        this.eval = eval;
    }

    public void setQm(Double qm) {
        this.qm = qm;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }
    

    
}
