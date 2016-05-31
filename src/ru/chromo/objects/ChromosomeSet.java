package ru.chromo.objects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Последовательность хромосом.
 * Поддерживает операции Кроссинговера и мутации.
 * Осуществляет селекцию хромосом.
 * Является ЯДРОМ программы.
 */
public class ChromosomeSet {
    
    private int popSize;
    private int worksSize;
    public ArrayList<Chromosome> pop; // набор хромосом
    public ArrayList<Chromosome> nPop; // набор хромосом (результирующий)
    public Chromosome finalResult; //лучшиая хромосома цикла
    public int finalIdx; //индекс лучшей хромосомы
    private ArrayList<Chromosome> trash; // набор отброшенных хромосом
    private ArrayList<Computer> computers; // набор отброшенных хромосом

    public ChromosomeSet( int popSize, int worksSize, ArrayList<Computer> computers ) {
        this.popSize = popSize; //он же m
        this.worksSize = worksSize;
        //инициализация накопителя хромосом для проверки на повторы
        trash = new ArrayList<>();
        
        //инициализация коллекции хромосом
        pop = new ArrayList<>();
        //инициализация результирующей коллекции хромосом
        nPop = new ArrayList<>();
        
        //инициализация трёх компьютеров
        this.computers = computers;
        
        //---===---
        
        //действия по заполнению коллекции pop
        //Формирование хромосом
        
        for (int i = 0; i < this.popSize; i++) { //инициализация хромосом
            pop.add( new Chromosome( this.worksSize, this.computers.size()-1 ) );
            //проверка на повторы
        }
        
        for (int i = 0; i < pop.size(); i++) {
            //расчеты
//            System.out.println("Хромосома "+(i+1)+": "+pop.get(i).toString());
            if( calculate(pop.get(i)) != CALCULATE_CODE_REPEAT ) {
                
            } else {
                pop.set(i, new Chromosome(this.worksSize, this.computers.size()-1)); //пересоздать хромосому, если она повторялась
//                System.err.println("Хромосома[обход повтора] "+(i+1)+": "+pop.get(i).toString());
                i--; //шаг назад по циклу
            }
            
        }
//        System.out.println("\n-=-=\nконец расчетов\n-=-=\n");
                
    }
    
    public ChromosomeSet( int popSize, int worksSize, ArrayList<Computer> computers, ArrayList<Chromosome> prevs ) {
        this.popSize = popSize; //он же m
        this.worksSize = worksSize;
        //инициализация накопителя хромосом для проверки на повторы
        trash = new ArrayList<>();
        
        //инициализация коллекции хромосом
        pop = new ArrayList<>();
        //инициализация результирующей коллекции хромосом
        nPop = new ArrayList<>();
        
        //инициализация трёх компьютеров
        this.computers = computers;
        //---===---
        
        //действия по заполнению коллекции pop
        //Формирование хромосом
        for (int i = 0; i < prevs.size(); i++) { //заполнение переходящих хромосом
            pop.add( prevs.get(i) );
        }
        for (int i = prevs.size(); i < this.popSize; i++) { //инициализация новых
            pop.add( new Chromosome( this.worksSize, this.computers.size()-1 ) );
            //проверка на повторы
        }
        
        for (int i = prevs.size(); i < pop.size(); i++) {
            //расчеты
//            System.out.println("Хромосома "+(i+1)+": "+pop.get(i).toString());
            if( calculate(pop.get(i)) != CALCULATE_CODE_REPEAT ) {
                
            } else {
                pop.set(i, new Chromosome(this.worksSize, this.computers.size()-1)); //пересоздать хромосому, если она повторялась
//                System.err.println("Хромосома[обход повтора] "+(i+1)+": "+pop.get(i).toString());
                i--; //шаг назад по циклу
            }
            
        }
//        System.out.println("\n-=-=\nконец расчетов\n-=-=\n");
                
    }
    
    public JTabbedPane operationCheck() {
        
//        JTabbedPane pane = new JTabbedPane();
//        ArrayList<JPanel> tabs = new ArrayList<>();
//        tabs.add(new JPanel(new GridLayout(0, 1)));
        
        Double rate;
        //computer 1
        int compNo=1;
        int step = 1;
        boolean next = false;
        while (next==false) { //итерация по циклам сверки и компьютерам
            
            //отрисовка панели вкладки
//            JPanel panel = tabs.get( tabs.size()-1 ); //посл-ий эл. из коллекции 
//            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            
//            JPanel panC = new JPanel(new BorderLayout());
            
//            JLabel lbl = new JLabel("<html><p>Компьютер "+compNo+
//                    " </p><p>Время запаздывания: "+computers.get(compNo-1).getDelayLevelMax()+
//                    " </p><p>Доверительный уровень: ");
//            panC.add( lbl, BorderLayout.NORTH );
//            System.out.println("Компьютер "+compNo);
            
//            JTable table;
//            DefaultTableModel model;
//            Object[][] data = new Object[pop.size()][3];
//            Object[] columnnames = new Object[] {"Vm", "Значение времени запаздывания",
//                "Относительно доверительного уровня"};
                        
            ArrayList<Integer> defects = new ArrayList<>();
            rate = 0.0;

            for (int i = 0; i < pop.size(); i++) {

                Chromosome v = pop.get(i);
//                data[i][0] = "V" + (i);
//                data[i][1] = v.getDelayLevel(compNo);
                String output = ("V" + (i) + " |" + v.getDelayLevel(compNo));
//                System.out.println(output);
                if ( v.getDelayLevel(compNo) <= computers.get(compNo - 1).getDelayLevelMax()) {
                    rate += (1.0 / this.popSize);
//                    data[i][2] = "1";
                } else {
                    defects.add(i); //добавление индекса хромосомы к коллекции дефектов
                    output += " false";
//                    data[i][2] = "0";
                }
//                System.out.println(output);
                
            }
            //отрисовка таблицы
//            model = new DefaultTableModel(data, columnnames);
//            table = new JTable(model);
//            panC.add( new JScrollPane(table) , BorderLayout.CENTER );
//            panel.add( panC );
            //заключение
//            lbl.setText( lbl.getText()+rate+
//                    " </p><p>Минимально необходимый: "+computers.get(compNo-1).getDelayRate()+"</p></html>"  );
//            System.out.println(rate + " _ " + computers.get(compNo - 1).getDelayRate());
            if (rate < computers.get(compNo - 1).getDelayRate()) {
//                System.out.println("Дефект на компьютере "+compNo);
                for (int i = 0; i < defects.size(); i++) {
                    Chromosome vNew = new Chromosome(this.worksSize, this.computers.size()-1);
                    //расчеты
//                    System.out.println("Хромосома " + (i + 1) + ": " + pop.get(i).toString());
                    while (true) {
                        if (calculate(vNew) != CALCULATE_CODE_REPEAT) {
                            break;
                        } else {
                            vNew = new Chromosome(this.worksSize, this.computers.size()-1); //пересоздать хромосому, если она повторялась
//                        System.err.println("Хромосома[обход повтора] " + (i + 1) + ": " + pop.get(i).toString());
                            //шаг назад по циклу
                        }
                    }

                    pop.set(defects.get(i), vNew); //установка новой хромосомы
                    
//                    panel.add(vNew.renderChromosome( defects.get(i) + 1 ) );
                    next=false;
                    compNo = 1; //установка расчета в начальное положение
                }//после чего повторить расчет для текущего компьютера
//                tabs.add(new JPanel()); //след. элементы будут размещаться на новый tab
                step++; //увеличить счетчик шага
//                System.out.println("Шаг " + (step));
            } else { //иначе - конец операции для данного компьютера
                compNo++;
                if(compNo>computers.size()) {
                    next = true;
                }
            }
            
        }
        
//        int cnt = 1;
//        for (int i = 0; i < step; i++) {
//            System.out.println("Шаг " + (i + 1));
//        }
//        for(JPanel panel : tabs) {
//            pane.add("Шаг "+cnt,panel);
//            cnt++;
//        }
        
//        return pane;
        return null;
    }
    
    
    public Double pc;
    public JTabbedPane operationCrossover() {
        JTabbedPane pane = new JTabbedPane();
        
        //определяем случайное pc
        pc = getRandom();
        //определяем rс для всех хромосом
        for (int i = 0; i < pop.size(); i++) {
            pop.get(i).setRm(getRandom());
        }
        //заполнение dataChoose
        Object[][] dataChoose = new Object[pop.size()][pop.get(0).getRawChromosome().length + 2];
        for (int i = 0; i < pop.size(); i++) {
            dataChoose[i][0] = (i+1); //m
            int lastIdx=0;
            for (int j = 0; j < pop.get(i).getRawChromosome().length; j++) {
                dataChoose[i][j+1] = pop.get(i).getRawChromosome()[j];
                lastIdx = j;
                
            }
            dataChoose[i][lastIdx+1] = pc; //pc
            dataChoose[i][lastIdx+2] = pop.get(i).getRm(); //rm
        }
        
        DefaultTableModel model = new DefaultTableModel(dataChoose, dataChoose[0]);
        JTable tableChoose = new JTable(model);
        pane.add("Выявление родительских хромосом", tableChoose);
        
        ArrayList<Integer> crossoverChromosomeIdxs = new ArrayList<>();
        for (int i = 0; i < pop.size(); i++) {
            if(pop.get(i).getRm() < pc) {
                crossoverChromosomeIdxs.add(i);
            }
        }
        //отметить соотв. строки красным в tableChoose
        tableChoose.setDefaultRenderer(Object.class, new MyRenderer(crossoverChromosomeIdxs));
        
        if(crossoverChromosomeIdxs.size() >= 2) {
        int iterations = Math.abs( crossoverChromosomeIdxs.size() / 2 );
//            System.out.println(iterations+" iterations");
            //проделать n раз
            for (int itr = 0; itr < iterations; itr++) {
                //getChilds
                Chromosome v1 = pop.get(crossoverChromosomeIdxs.get( (itr*2) ));
                Chromosome v2 = pop.get(crossoverChromosomeIdxs.get( (itr*2)+1 ));
                Chromosome[] childs = getChildsCrossover(v1, v2);
                //calculate
                for (int i = 0; i < childs.length; i++) {
//                    System.out.println("child[" + i + "] =====");
                    do {
                        calculate(childs[i]);
                    } while(childs[i].getEnduranceMax()==null);
                    //render childs
                    pane.add("Потомок "+(itr+1)+"-"+(i+1), childs[i].renderChromosome(i+1));
                }

                //getBest
                Chromosome[] best = getBestChromosomes(v1, v2, childs);
                for (int i = 0; i < best.length; i++) {
                    //установка лучших хромосом в общую коллекцию
                    pane.add("Лучшая "+(itr+1)+"-"+(i+1), best[i].renderChromosome(i+1));
                    pop.set( crossoverChromosomeIdxs.get( (itr*2)+i ), best[i]);
                }
            }
            
            Object[][] dataResult = new Object[pop.size()][pop.get(0).getRawChromosome().length+1];
            for (int i = 0; i < pop.size(); i++) {
                dataResult[i][0] = (i + 1); //m
                for (int j = 0; j < pop.get(i).getRawChromosome().length; j++) {
                    dataResult[i][j+1] = pop.get(i).getRawChromosome()[j];

                }
            }

            DefaultTableModel modelResult = new DefaultTableModel(dataResult, dataResult[0]);
            JTable tableResult = new JTable(modelResult);
            pane.add("Итог", tableResult);
            
        }
        
        return pane;
    }
    
    public Double pm;
    public JTabbedPane operationMutation() {
        JTabbedPane pane = new JTabbedPane();
        
        //определяем случайное pc
        pm = getRandom();
        //определяем rс для всех хромосом
        for (int i = 0; i < pop.size(); i++) {
            pop.get(i).setRm(getRandom());
        }
        //заполнение dataChoose
        Object[][] dataChoose = new Object[pop.size()][pop.get(0).getRawChromosome().length + 2];
        for (int i = 0; i < pop.size(); i++) {
            dataChoose[i][0] = (i+1); //m
            int lastIdx=0;
            for (int j = 0; j < pop.get(i).getRawChromosome().length; j++) {
                dataChoose[i][j+1] = pop.get(i).getRawChromosome()[j];
                lastIdx = j;
                
            }
            dataChoose[i][lastIdx+1] = pm; //pm
            dataChoose[i][lastIdx+2] = pop.get(i).getRm(); //rm
        }
        
        DefaultTableModel model = new DefaultTableModel(dataChoose, dataChoose[0]);
        JTable tableChoose = new JTable(model);
        pane.add("Выявление родительских хромосом", tableChoose);
        
        ArrayList<Integer> mutationChromosomeIdxs = new ArrayList<>();
        for (int i = 0; i < pop.size(); i++) {
            if(pop.get(i).getRm() < pm) {
                mutationChromosomeIdxs.add(i);
            }
        }
        //отметить соотв. строки красным в tableChoose
        tableChoose.setDefaultRenderer(Object.class, new MyRenderer(mutationChromosomeIdxs));
        
        if(mutationChromosomeIdxs.size() >= 2) {
        int iterations = Math.abs( mutationChromosomeIdxs.size() / 2 );
//            System.out.println(iterations+" iterations");
            //проделать n раз
            for (int itr = 0; itr < iterations; itr++) {
                //getChilds
                Chromosome v1 = pop.get(mutationChromosomeIdxs.get( (itr*2) ));
                Chromosome v2 = pop.get(mutationChromosomeIdxs.get( (itr*2)+1 ));
                Chromosome[] childs = getChildsMutation(v1, v2);
                //calculate
                for (int i = 0; i < childs.length; i++) {
//                    System.out.println("child[" + i + "] =====");
                    calculate(childs[i]);
                    //render childs
                    pane.add("Потомок "+(itr+1)+"-"+(i+1), childs[i].renderChromosome(i+1));
                }

                //getBest
                Chromosome[] best = null;
                try {
                best = getBestChromosomes(v1, v2, childs);
                } catch(NullPointerException ex) {
                    System.err.println("gus: "+v1);
                    System.err.println("gus: "+v2);
                    System.err.println("gus: "+childs[0]);
                    System.err.println("gus: "+childs[1]);
                }
                for (int i = 0; i < best.length; i++) {
                    //установка лучших хромосом в общую коллекцию
                    pane.add("Лучшая "+(itr+1)+"-"+(i+1), best[i].renderChromosome(i+1));
                    pop.set( mutationChromosomeIdxs.get( (itr*2)+i ), best[i]);
                }
            }
            
            Object[][] dataResult = new Object[pop.size()][pop.get(0).getRawChromosome().length+1];
            for (int i = 0; i < pop.size(); i++) {
                dataResult[i][0] = (i + 1); //m
                for (int j = 0; j < pop.get(i).getRawChromosome().length; j++) {
                    dataResult[i][j+1] = pop.get(i).getRawChromosome()[j];

                }
            }

            DefaultTableModel modelResult = new DefaultTableModel(dataResult, dataResult[0]);
            JTable tableResult = new JTable(modelResult);
            pane.add("Итог", tableResult);
            
        }
        
        return pane;
    }
    
    public JComponent objectiveFunc() {
        
        Object[][] dataResult = new Object[pop.size()][4];
        Object[] dataHdrs = new Object[] {"n", "EVm(Max)", "Eval(Vn)", "qm"};
            for (int i = 0; i < pop.size(); i++) {
                
                dataResult[i][0] = (i + 1); //m
                dataResult[i][1] = pop.get(i).getEnduranceMax(); //EVMax
                
                Double a = 0.4;
                Double eval = a * Math.pow( (1-a), i);
                pop.get(i).setEval(eval);
                dataResult[i][2] = pop.get(i).getEval();
                
                Double qm = 0.0;
                for (int j = 0; j <= i; j++) {
                    qm += pop.get(j).getEval();
                }
                pop.get(i).setQm(qm);
                dataResult[i][3] = pop.get(i).getQm();
                        
            }
            
            JTable tbl = new JTable(new DefaultTableModel(dataResult, dataHdrs));
            JScrollPane sc = new JScrollPane(tbl);
        
        return sc;
    }
    
    public JComponent selectionFunc() {
        
        JPanel panResult = new JPanel(new GridLayout());
        String resultText="В следующий цикл попадут:";    
        
        Object[][] dataResult = new Object[pop.size()][4];
        Object[] dataHdrs = new Object[] {"№", "r", "qi", "EVi(Max)"};
            for (int i = 0; i < pop.size(); i++) {
                
                dataResult[i][0] = (i + 1); //№
                
                Double r = getRandom( pop.get(pop.size()-1).getQm() );
                pop.get(i).setR(r);
                dataResult[i][1] = pop.get(i).getR();

                for (int j = 0; j < pop.size(); j++) {
                    Double qPrew;
                    Double qi;
                    if (j == 0) {
                        qPrew = 0.0;
                    } else {
                        qPrew = pop.get(j - 1).getQm();
                    }
                    qi = pop.get(j).getQm();
                    if( (qPrew<r) && (r<qi)) {
                        if(!(nPop.contains(pop.get(j)))) {
                            nPop.add(pop.get(j));
                            resultText+=" V"+(j+1)+" ";
                        }
                        dataResult[i][2] = pop.get(j).getQm();
                        dataResult[i][3] = "EV"+(j+1)+"(Max) = "+pop.get(j).getEnduranceMax(); //EVMax
                    }
                }
                
        }

        finalIdx=0;    
        Double minVal = nPop.get(0).getEnduranceMax();
        finalResult = nPop.get(0);
        
        for (int i = 0; i < nPop.size(); i++) {
            if (minVal > nPop.get(i).getEnduranceMax()) {
                minVal = nPop.get(i).getEnduranceMax();
                finalResult = nPop.get(i);
            }
        }
        
            for (int i = 0; i < pop.size(); i++) {
                if(pop.get(i).getEnduranceMax().equals(finalResult.getEnduranceMax())) {
                    finalIdx = i;
                }
            }
            resultText += " Наилучшая: V"+(finalIdx+1);
//            System.out.println("Наилучшая"+finalResult.toString());
            
//            System.out.println("Прошло "+nPop.size()+"хромосом");
            JPanel pan = new JPanel(new BorderLayout());
            JTable tbl = new JTable(new DefaultTableModel(dataResult, dataHdrs));
            JScrollPane sc = new JScrollPane(tbl);
            JPanel panSc = new JPanel(new GridLayout(0, 1));
            panSc.add(sc);
            
            JLabel lblResult = new JLabel("<html><span style=\"color: red;\">"+resultText+"</span></html>");
            
            panResult.add(lblResult);
            panResult.setPreferredSize(new Dimension(100, 50));
            pan.add(panSc, BorderLayout.CENTER);
            pan.add(panResult, BorderLayout.SOUTH);
        
        
        return pan;
    }
    
    private class MyRenderer extends DefaultTableCellRenderer{

        ArrayList<Integer> highlightIdxs;
        
        public MyRenderer(ArrayList<Integer> highlightIdxs) {
            super();
            this.highlightIdxs = highlightIdxs;
        }
        
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(highlightIdxs.contains( row )) {
                c.setBackground(Color.red);
            } else {
                c.setBackground(table.getBackground());
            }
            
            table.repaint();
            return c;
        }
    }
    
    public static final int CALCULATE_CODE_REPEAT = -1;
    public static final int CALCULATE_CODE_SUCCESS = 1;
    public static final int CALCULATE_CODE_FAIL = 0;
    
    private int calculate(Chromosome v) {
        
        for (int i = 0; i < computers.size(); i++) {
            computers.get(i).calculateWorks(v, i+1); // i+1 - номер части работ
        }
        for (int i = 0; i < computers.size(); i++) {
            //теперь получив длительности работ вычислим максимальную
            computers.get(i).setEnduranceMax( v, Computer.getEnduranceMax(computers) );
        }
        
        //расчет уровня запаздывания
        boolean success = false;
        
        for (int i = 0; i < computers.size(); i++) {
            boolean tmp = computers.get(i).calculateDelayLevel(v, i+1);
            success = success && tmp;
        }
        
        if (success) {
//            System.out.println("success!");
            return CALCULATE_CODE_SUCCESS;
        } else {
            return CALCULATE_CODE_FAIL;
        }
    }
    
    private Chromosome[] getChildsMutation(Chromosome v1, Chromosome v2) {
        Chromosome[] childs = new Chromosome[2];
        
        ArrayList<Integer> values1 = new ArrayList<>();
        ArrayList<Integer> values2 = new ArrayList<>();
        Integer m = 1;
        
        for (int i = 0; i < v1.getRawChromosome().length; i++) {
            Integer value1 = (Integer)v1.getRawChromosome()[i] - 1;
            values1.add(value1);
            Integer value2 = (Integer)v2.getRawChromosome()[i] + 1;
            values2.add(value2);
        }
        
        Chromosome x = new Chromosome(v1.getWorks().length, v1.getDealing().length, values1 );
        x.setParent1(v1);
        x.setParent2(v2);
        x.setM(m);
        
        Chromosome y = new Chromosome(v2.getWorks().length, v2.getDealing().length, values2);
        y.setParent1(v1);
        y.setParent2(v2);
        y.setM(m);
        
        childs[0] = x;
        childs[1] = y;
        
        return childs;
    }
    
    private Chromosome[] getChildsCrossover(Chromosome v1, Chromosome v2) {
        Chromosome[] childs = new Chromosome[2];
        
        ArrayList<Double> values1 = new ArrayList<>();
        ArrayList<Double> values2 = new ArrayList<>();
        Double c = getRandom();
        
        for (int i = 0; i < v1.getRawChromosome().length; i++) {
            Double value1 = c*((Integer)v1.getRawChromosome()[i]) + (1-c)*((Integer)v2.getRawChromosome()[i]);
            values1.add(value1);
            Double value2 = (1-c)*((Integer)v1.getRawChromosome()[i]) + c*((Integer)v2.getRawChromosome()[i]);
            values2.add(value2);
        }
        
        Chromosome x = new Chromosome(values1, v1.getWorks().length, v1.getDealing().length);
        x.setParent1(v1);
        x.setParent2(v2);
        x.setC(c);
        
        Chromosome y = new Chromosome(values2, v2.getWorks().length, v2.getDealing().length);
        y.setParent1(v1);
        y.setParent2(v2);
        y.setC(c);
        
        childs[0] = x;
        childs[1] = y;
        
        return childs;
    }
    
    private Chromosome[] getBestChromosomes(Chromosome v1, Chromosome v2, Chromosome[] childs) {
        Chromosome[] best = new Chromosome[2];
        Chromosome[] all = new Chromosome[4];
        ArrayList<Double> endurances = new ArrayList<>();
        
        all[0] = v1;
        all[1] = v2;
        all[2] = childs[0];
        all[3] = childs[1];
        
        for (int i = 0; i < all.length; i++) {
            Double emax = all[i].getEnduranceMax();
            if(emax==null) {
                System.err.println("idx: "+i);
                for (int j = 0; j < computers.size(); j++) {
                    System.err.println(all[i].getEndurance(j+1));
                }
                
            }
            endurances.add(emax);
            
        }
        
        try {
            Collections.sort(endurances);
        } catch(Exception e) {
            for (int i = 0; i < all.length; i++) {
                System.out.println(all[i]+" "+all[i].getEnduranceMax());
            }
        }
        for (int i = 0; i < 2; i++) {
            
            for (int j = 0; j < all.length; j++) {
                if(all[j].getEnduranceMax() == endurances.get(i)) {
                    best[i] = all[j];
                }
            }
            
        }
        //обработка исключения
        for (int i = 0; i < best.length; i++) {
            if(best[i]==null) {
                best[i] = all[i];
            }
        }
        
        return best;
    }
    
    public ArrayList<Double> randoms;
    public Double getRandom() {
        if(randoms==null) {
            randoms=new ArrayList<>();
        }
        Double random;
        boolean tag=true;
        random = Math.random();
        
        randoms.add(random);
        return random;
    }
    
    public Double getRandom(Double maxVal) {
        
        Double random;
        
        while(true) {
            random = Math.random();
            if( (random>0) && (random<maxVal)) {
                break;
            }
        }
        return random;
    }
    
    public Integer getRandom(int min, int max) {
        Integer random = 0;
        random = min + (int)(Math.random() * ((max - min) + 1));
        return random;
    }

    @Override
    public String toString() {
        String str = "";
        
        for (int i = 0; i < pop.size(); i++) {
            str+="V"+(i+1)+": "+pop.get(i).toString()+"\n";
        }
        
        return str; //To change body of generated methods, choose Tools | Templates.
    }
    
}
