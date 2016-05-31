package ru.chromo.objects;

import java.util.ArrayList;
import java.util.Collections;

public class Computer {

    private Double delayRate; //значение заданного доверительного уровня
    private Double delayLevelMax; //уровень запаздывания max
    private Double delayLevel; //уровень запаздывания
    private Integer workNo; //тек. работа
    private ArrayList<Integer> works; //работы
    private Double endurance; //длительность
    private Double enduranceMax; //длит. макс.
    private Integer generatorType; //текущий тип генератора
    
    public ArrayList<Integer> delayLevelSet; //уровень запаздывания
    
    //типы генераторов
    public static final int GENERATOR_TYPE_NORMAL=1;
    public static final int GENERATOR_TYPE_EVEN=2;
    public static final int GENERATOR_TYPE_TRIANGULAR=3;
    
    public Computer(Double delayLevelMax, Double delayRate, int generatorType) {
        this.delayLevelMax = delayLevelMax;
        this.delayRate = delayRate;
        this.generatorType = generatorType;
        
        this.delayLevelSet = new ArrayList<>();
        
        this.workNo=0;
    }
    
    public Integer mu;
    public Double a, b, c, m;
    public Double sigma;
    public Double mu1;
    public Double mu2;
    public Double y;
    public Double U;
    public Double N;
    public boolean calculateWorks(Chromosome chromosome, int part) {
        
        ArrayList<String> calcPartRow;
        Object[][] calcPart;
        
        switch(generatorType) {
            
            case GENERATOR_TYPE_NORMAL:
                setWorkNo((Integer) (getWorkNo() + 1)); //началась работа
                setWorks(new ArrayList<>());
                setEndurance(0.0);
                
                calcPart = new Object[chromosome.getPart(part).length+1][7];
                calcPart[0][0]=("№");
                calcPart[0][1]=("mu = i");
                calcPart[0][2]=("sigma");
                calcPart[0][3]=("mu1");
                calcPart[0][4]=("mu2");
                calcPart[0][5]=("y");
                calcPart[0][6]=("N");
                
                for (int i = 0; i < chromosome.getPart(part).length; i++) {
                    //отделение частей
                    
                    mu = chromosome.getPart(part)[i];
                    getWorks().add(mu); //добавление отделенной/выполняемой работы в список зачад компьютера
                    
                    mu1=getRandom();
                    mu2=getRandom();
                    
                    y = Math.pow( ((-2)*Math.log(mu1)), 0.5 ) * Math.sin( 2*Math.PI*mu2 );
                    sigma = Math.pow( ((double)mu/chromosome.getWorks().length), 0.5 ); 
                    N = mu + sigma * y;
                    //запись результата
                    calcPart[i+1][0]=( (i+1) );
                    calcPart[i+1][1]=( mu );
                    calcPart[i+1][2]=( sigma );
                    calcPart[i+1][3]=( mu1 );
                    calcPart[i+1][4]=( mu2 );
                    calcPart[i+1][5]=( y );
                    calcPart[i+1][6]=( N );
                    //конец записи результата
//                    System.out.print(mu + " " + sigma +" "+ mu1 +" "+ mu2 +" "+ y +" "+ N +"\n");
                    //вычисление длительности
                    setEndurance( getEndurance() + N); // -1 используется для получения индекса элемента
                    
                }
                
                chromosome.setEndurance(getEndurance(), part);  
                chromosome.calcResult.add(calcPart);
//                System.out.print(" | " + getEndurance());
                break;
                
            case GENERATOR_TYPE_EVEN:
                setWorkNo((Integer) (getWorkNo() + 1)); //началась работа
                setWorks(new ArrayList<>());
                setEndurance(0.0);
                
                calcPart = new Object[chromosome.getPart(part).length+1][4];
                calcPart[0][0]=("№");
                calcPart[0][1]=("i");
                calcPart[0][2]=("U");
                calcPart[0][3]=("N");
                
                for (int i = 0; i < chromosome.getPart(part).length; i++) {
                    //отделение частей
                    mu = chromosome.getPart(part)[i];
                    b = mu + 1.0;
                    getWorks().add(mu); //добавление отделенной/выполняемой работы в список зачад компьютера
                    
                    U=getRandom();
                    
                    N = mu + U*(b-mu);
                    //запись результата
                    calcPart[i+1][0]=((i+1));
                    calcPart[i+1][1]=(mu);
                    calcPart[i+1][2]=(U);
                    calcPart[i+1][3]=(N);
                    
//                    System.out.print(mu + " " + U + " " + N +"\n");
                    //вычисление длительности
                    setEndurance( getEndurance() + N); // -1 используется для получения индекса элемента
                    
                }
                chromosome.setEndurance(getEndurance(), part); 
                chromosome.calcResult.add(calcPart);
//                System.out.print(" | " + getEndurance());
                break;
                
            case GENERATOR_TYPE_TRIANGULAR:
                setWorkNo((Integer) (getWorkNo() + 1)); //началась работа
                setWorks(new ArrayList<>());
                setEndurance(0.0);
                
                calcPart = new Object[chromosome.getPart(part).length+1][9];
                calcPart[0][0]=("№");
                calcPart[0][1]=("i");
                calcPart[0][2]=("a");
                calcPart[0][3]=("b");
                calcPart[0][4]=("m");
                calcPart[0][5]=("U");
                calcPart[0][6]=("c");
                calcPart[0][7]=("y");
                calcPart[0][8]=("N");
                
                for (int i = 0; i < chromosome.getPart(part).length; i++) {
                    //отделение частей
                    mu = chromosome.getPart(part)[i];
                    getWorks().add(mu); //добавление отделенной/выполняемой работы в список зачад компьютера
                    a = (double)mu;
                    b = (double)mu + 1.0;
                    m = (double)mu+1.5;
                    c = (m-a)/(b-a);
                    
                    U=getRandom();
                    
                    y = Math.pow( (c*U), 0.5);
                    N = a + (b-a)*y;
                    //запись результата
                    calcPart[i+1][0]=((i+1));
                    calcPart[i+1][1]=(mu);
                    calcPart[i+1][2]=(a);
                    calcPart[i+1][3]=(b);
                    calcPart[i+1][4]=(m);
                    calcPart[i+1][5]=(U);
                    calcPart[i+1][6]=(c);
                    calcPart[i+1][7]=(y);
                    calcPart[i+1][8]=(N);
                    //конец записи
//                    System.out.print(mu + " " + a + " " + b+ " " + m+ " " + U+ " " + c+ " " + y+ " " + N+"\n");
                    //вычисление длительности
                    setEndurance( getEndurance() + N); // -1 используется для получения индекса элемента
                    
                }
                chromosome.setEndurance(getEndurance(), part); 
                chromosome.calcResult.add(calcPart);
//                System.out.print(" | " + getEndurance());
                break;    
            
            default:
                break;
                
        }    
        
//        System.out.println("###");
        return true;
    }
    
    public boolean calculateDelayLevel(Chromosome chromosome, int part) {
        setDelayLevel(getEnduranceMax() - getEndurance());
        chromosome.setDelayLevel(getDelayLevel(), part);
        chromosome.setDelayLevelMax(getDelayLevelMax());
//        delayLevelSet.add(getDelayLevel());
        
//        System.out.println("DELAY_LEVEL: "+getDelayLevel());
        if(getDelayLevelMax() > getDelayLevel()) {
            return true;
        } else {
            return false;
        }
    }
    
    
    public static Double getEnduranceMax(ArrayList<Computer> computers) {
        Double enduranceMax; //Emax - результат
        ArrayList<Double> allEndurance = new ArrayList<>(); //список всех длительностей
        for(Computer computer : computers) { //заполнение списка всех длительностей
            allEndurance.add(computer.getEndurance());
        }
        enduranceMax = Collections.max(allEndurance); //нахождение максимальной
        return enduranceMax;
    }
    
    
//    public ArrayList<Double> randoms;
    public Double getRandom() {
//        if(randoms==null) {
//            randoms=new ArrayList<>();
//        }
        Double random;
        boolean tag=true;
        random = Math.random();
        
//        randoms.add(random);
        return random;
    }

    /**
     * @return the delayRate
     */
    public Double getDelayRate() {
        return delayRate;
    }

    /**
     * @param delayRate the delayRate to set
     */
    public void setDelayRate(Double delayRate) {
        this.delayRate = delayRate;
    }

    /**
     * @return the delayLevelMax
     */
    public Double getDelayLevelMax() {
        return delayLevelMax;
    }

    /**
     * @param delayLevelMax the delayLevelMax to set
     */
    public void setDelayLevelMax(Double delayLevelMax) {
        this.delayLevelMax = delayLevelMax;
    }

    /**
     * @return the delayLevel
     */
    public Double getDelayLevel() {
        return delayLevel;
    }

    /**
     * @param delayLevel the delayLevel to set
     */
    public void setDelayLevel(Double delayLevel) {
        this.delayLevel = delayLevel;
    }

    /**
     * @return the workNo
     */
    public Integer getWorkNo() {
        return workNo;
    }

    /**
     * @param workNo the workNo to set
     */
    public void setWorkNo(Integer workNo) {
        this.workNo = workNo;
    }

    /**
     * @return the works
     */
    public ArrayList<Integer> getWorks() {
        return works;
    }

    /**
     * @param works the works to set
     */
    public void setWorks(ArrayList<Integer> works) {
        this.works = works;
    }

    /**
     * @return the endurance
     */
    public Double getEndurance() {
        return endurance;
    }

    /**
     * @param endurance the endurance to set
     */
    public void setEndurance(Double endurance) {
        this.endurance = endurance;
    }

    /**
     * @return the enduranceMax
     */
    public Double getEnduranceMax() {
        return enduranceMax;
    }

    /**
     * @param enduranceMax the enduranceMax to set
     */
    public void setEnduranceMax(Chromosome v, Double enduranceMax) {
        v.setEnduranceMax(enduranceMax);
        this.enduranceMax = enduranceMax;
    }
    
}
