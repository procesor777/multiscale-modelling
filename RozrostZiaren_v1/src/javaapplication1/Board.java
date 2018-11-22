package javaapplication1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by procesor777 on 2016-05-05.
 */
public class Board extends JPanel implements Runnable
{
    private static final long serialVersionUID = 1L;
    private int pixelSize = Base.InitConditions.pixelSize;
    private int width = 1000;
    private int height = 1000;
    private int tabSizeX = this.width / this.pixelSize;
    private int tabSizeY = this.height / this.pixelSize;
    private P[][] P = new P[this.tabSizeX][this.tabSizeY];
    private double p_before = 0.0;

    private int itr = 0; // aktualny krok iteracji
    private Map<Integer, int[]> pattern;
    private List<P> kolory = new ArrayList<P>();

    private Set<Map.Entry<Pkt, P>> savedEntries;


    Set<Map.Entry<Pkt, P>> savedSubstructure;

    public Board()
    {
        pattern = new HashMap<Integer, int[]>();

        pattern.put(0, new int[]{1, 1, 1, 1, 1, 1, 1, 1}); // Moore
        pattern.put(1, new int[]{0, 1, 0, 1, 0, 1, 0, 1}); // von Neumann
        pattern.put(2, new int[]{1, 1, 0, 1, 1, 1, 0, 1}); // Hexagonal left
        pattern.put(3, new int[]{0, 1, 1, 1, 0, 1, 1, 1}); // Hexagonal right
        pattern.put(4, new int[]{}); // Hexagonal random
        pattern.put(9, new int[]{}); // Pentagonal random

        for (int i = 0; i < this.tabSizeX; i++)
        {
            for (int j = 0; j < this.tabSizeY; j++)
            {
                this.P[i][j] = new P();
            }
        }

        setPreferredSize(new Dimension(500, 500));

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                int x = arg0.getX();
                int y = arg0.getY();
                Board.this.clickPixel(x, y);
                Board.this.refresh();
            }
        });
    }


    @Override
    public void run()
    {
        System.out.println("Board");
        while (true)
        {
            this.resizeP(); // rozmiar planszy
            if (Base.InitConditions.StatusStart == 1)
            {
                this.reverse(); // P: prev = act
                this.refresh(); // iterakcja programu
            } else
            {
                itr = 0;
            }
            try
            {
                Thread.sleep(Base.InitConditions.delay);
            } catch (Exception ex)
            {
                System.out.print(ex.toString());
            }
        }
    }

    public void refresh()
    {
        int x = this.iteracja_naiwny();
        repaint();
    }

    private int iteracja_naiwny()
    {
        if (Base.InitConditions.StatusStart == 0) return -1;
        int x = 0;
        P tmp;

        for (int i = 0; i <= this.tabSizeX - 1; i++)
        {
            for (int j = 0; j <= this.tabSizeY - 1; j++)
            {
                if (this.P[i][j].act == 1)
                    continue;
                if (this.P[i][j].subs == 1)
                    continue;
                ++x;

                tmp = this.makeAct(i, j, "act"); // wzor sasiada od ktorego mam przejac kolor
                if (tmp.act < 0 && tmp.prev < 0)
                {
                    continue;
                }
                this.P[i][j].act = tmp.act;
                this.P[i][j].recolor(tmp);

            }//j
        }//i
        return x;
    }//--

//losuj zarodek w macierzy
    public void randomPixel()
    {
        Random r = new Random();
        int x = r.nextInt(this.width);
        int y = r.nextInt(this.height);
        this.clickPixel(x, y);
    }

//wyczysc macierz
    public void clean()
    {
        this.itr = 0; // wyzeruj licznik iterakcji

        for (int i = 0; i < this.tabSizeX; i++)
        {
            for (int j = 0; j < this.tabSizeY; j++)
            {
                this.P[i][j].set(0);
            }
        }
    }


//rozmieszczenie zarodkow na macierzy
    //arangements of nucleons on matrix
    public void random()
    {
        Random r = new Random();
        int x, y;
        int srodek_x = this.tabSizeX / 2;
        int srodek_y = this.tabSizeY / 2;

        switch (Base.InitConditions.rozmieszczenie)
        {
            case 1: // losowe rownomierne
                double px, py;

                py = Math.sqrt(this.tabSizeY * Base.InitConditions.punkty / this.tabSizeX);
                px = ((double) Base.InitConditions.punkty) / py;
                int x_size = (int) px;
                int y_size = (int) py;
                int x_width = this.tabSizeX / x_size;
                int y_height = this.tabSizeY / y_size;
                srodek_x = x_size / 2;
                srodek_y = y_size / 2;

                for (int i = 0; i < x_size; i++)
                {
                    for (int j = 0; j < y_size; j++)
                    {
                        int t_x = (i * 2 + 1) * x_width / 2, t_y = (j * 2 + 1) * y_height / 2;

                        do
                        {
                            this.P[t_x][t_y].set_random(1);
                        }
                        while (this.colorExists(this.P[t_x][t_y], t_x, t_y));

                        kolory.add(new P(this.P[t_x][t_y]));
                    }//j
                }//i

                break;// 1
            case 2: // losowy promien okregu
                int PRx, PRy;

                for (int i = 0; i < 360; i = i + (360 / Base.InitConditions.punkty))
                {
                    PRx = r.nextInt(this.tabSizeX / 2);
                    PRy = r.nextInt(this.tabSizeY / 2);

                    x = (int) (PRx * Math.cos(i * Math.PI / 180));
                    x = x + srodek_x;
                    y = (int) (PRy * Math.sin(i * Math.PI / 180));
                    y = y + srodek_y;

                    while (x < 0)
                    {
                        x++;
                    }
                    while (y < 0)
                    {
                        y++;
                    }
                    while (x >= this.tabSizeX)
                    {
                        x--;
                    }

                    while (y >= this.tabSizeY)
                    {
                        y--;
                    }

                    do
                    {
                        this.P[x][y].set_random(1);
                    }
                    while (this.colorExists(this.P[x][y], x, y));

                    kolory.add(new P(this.P[x][y]));
                }// for
                break; // 2

            case 3: // losowe przypadkowe
                for (int i = 0; i < Base.InitConditions.punkty; i++)
                {
                    x = r.nextInt(this.tabSizeX);
                    y = r.nextInt(this.tabSizeY);
                    this.P[x][y].set_random(1);
                    while (this.colorExists(this.P[x][y], x, y))
                    {
                        this.P[x][y].set_random(1); // losuj nowe az do uzyskania unikatu
                    }

                    kolory.add(new P(this.P[x][y]));
                }
                break;// 3

            case 4: // losowe z promieniem
                // Base.InitConditions.rozmieszczenie_r
                List<Pkt> wsio = new ArrayList<Pkt>();
                for (int i = 0; i < this.tabSizeX; i++)
                {
                    for (int j = 0; j < this.tabSizeY; j++)
                    {
                        wsio.add(new Pkt(i, j));
                    }
                }

                for (int i = 0; i < Base.InitConditions.punkty; i++)
                {//i
                    if (wsio.size() < 1)
                    {
                        System.out.println("wyczerpano zasob punktow!");
                        return;
                    }
                    Pkt p = wsio.get(r.nextInt(wsio.size()));

                    // losuj nowe az do uzyskania unikatu
                    do
                    {
                        this.P[p.x][p.y].set_random(1);
                    }
                    while (this.colorExists(this.P[p.x][p.y], p.x, p.y));

                    kolory.add(new P(this.P[p.x][p.y]));

                    Iterator<Pkt> wsio_i = wsio.iterator();
                    while (wsio_i.hasNext())
                    {//next
                        Pkt v = wsio_i.next();
                        if (p.inR(v, Base.InitConditions.rozmieszczenie_r))
                        {
                            wsio_i.remove();
                        }
                    }//next
                }//i
                break;// 4
        }
    }// --

    /***
     * sprawdz czy dany kolor wystepuje juz na planszy
     * check if given color is already on the board
     *
     * @param p punkt na planszy
     * @parm x, y współrzędne punktu (jeśli >=0 pomijaj przy sprawdzaniu koloru); coordinates of point (while more or less 0 skip while changing colors)
     */
    private boolean colorExists(P p, int x, int y)
    {
        for (int i = 0; i < this.tabSizeX; i++)
        {
            for (int j = 0; j < this.tabSizeY; j++)
            {
                if (x == i && y == j && x >= 0 && y >= 0) continue;
                if (this.P[i][j].colorMatch(p)) return true;
            }//j
        }//i
        return false;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        for (int x = 0; x < this.tabSizeX; x++)
        {
            for (int y = 0; y < this.tabSizeY; y++)
            {
                g.setColor(new Color(this.P[x][y].R, this.P[x][y].G, this.P[x][y].B));

                g.fillRect(x * this.pixelSize, // pozycja X
                        y * this.pixelSize, // pozycja Y
                        this.pixelSize, // width
                        this.pixelSize // height
                );
            }
        }
    }// g

//wczytaj zapisana macierz punktow
    public void loadMap()
    {
        // TODO Auto-generated method stub
        for (Map.Entry<Pkt, P> o : Base.pkts.entrySet())
        {
            this.P[o.getKey().x][o.getKey().y] = new P(o.getValue());
        }
    }

    public void loadSavedMap()
    {
        // TODO Auto-generated method stub
        for (Map.Entry<Pkt, P> o : savedEntries)
        {
            this.P[o.getKey().x][o.getKey().y] = new P(o.getValue());
        }
    }

//exportuj tablice punktow
    public void saveMap()
    {
        Base.pkts.clear();
        for (int i = 0; i < tabSizeX; i++)
        {
            for (int j = 0; j < tabSizeY; j++)
            {
                if (this.P[i][j].act == 1)
                {
                    Base.pkts.put(new Pkt(i, j), new P(this.P[i][j]));
                }
            }
        }
    }

    //!!export to file!!
    public void exportToFile()
    {
        this.saveMap();

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("ExportToTXT.txt"));
           // BufferedWriter out1 = new BufferedWriter(new FileWriter("../MonteCarlo/ExportCAMC.txt"));

            out.write(String.valueOf(this.tabSizeX) + " " + String.valueOf(this.tabSizeY));
            out.newLine();

           // out1.write(String.valueOf(this.tabSizeX) + " " + String.valueOf(this.tabSizeY));
            //out1.newLine();
            for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
            {
                //out.write(entry.getKey().x + " " + entry.getKey().y + " 0 " + entry.getValue().color2int());
                //do importu
                out.write(entry.getKey().x + " " + entry.getKey().y + " 0 " + entry.getValue().color2int() + " " +  entry.getValue().prev + " " +  entry.getValue().act );
                out.newLine();

               // out1.write(entry.getKey().x + " " + entry.getKey().y + " 0 " + entry.getValue().color2int() + " " +  entry.getValue().prev + " " +  entry.getValue().act );
               // out1.newLine();
            }
            out.close();
            //out1.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //import from file
    public void importFromFile()
    {
        String filePath = "ExportToTXT.txt";
        if (Base.InitConditions.MC_CA)
        {
            filePath = "ExportMCCA.txt";
        }
        else
        {
            filePath = "ExportToTXT.txt";
        }


        Map<Pkt, P> map = new HashMap<Pkt, P>();

        String line;
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(filePath));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        try
        {
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(" ", 6);
                if (parts.length >= 6)
                {
                    int xx = Integer.parseInt(parts[0]);
                    int yy = Integer.parseInt(parts[1]);
                    String phase = parts[2];
                    int integerRGBInt = Integer.parseInt(parts[3]);
                    int actInt = Integer.parseInt(parts[4]);
                    int prevInt = Integer.parseInt(parts[5]);

                    if (actInt == 1)
                    {
                        if (Base.InitConditions.MC_CA)
                        {
                            if (integerRGBInt != 16777215)
                            {
                                if (xx <159 && yy<116 && integerRGBInt != -1)
                                {
                                    Base.pkts.put(new Pkt(xx, yy), new P(actInt,prevInt,integerRGBInt));
                                }
                            }
                        }
                        else
                        {
                            Base.pkts.put(new Pkt(xx, yy), new P(actInt,prevInt,integerRGBInt));
                        }
                    }

                } else {
                    System.out.println("ignoring line: " + line);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (Pkt key : map.keySet())
        {
            System.out.println(key + ":" + map.get(key));
        }
        try
        {
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //-------------------------

    //dodanie wtracen po symulacji!!
    public void addInclusionsAfter()
    {
        this.saveMap();

        int iteratorForInclusions = 0;

        //tablica  punktow na granicy
        int[][] tabOfBoundaryGrains = new int[Base.pkts.size()*50][2];

        for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
        {
            //sprawdzenie ktore sa na granicy
            boolean checkIfOnBoundary = naGranicy(entry.getKey().x, entry.getKey().y);

            if(checkIfOnBoundary)
            {
                //System.out.println(entry.getKey().x + " " + entry.getKey().y + " " + entry.getValue().color2int() + " is on border");

                tabOfBoundaryGrains[iteratorForInclusions][0] = entry.getKey().x;
                tabOfBoundaryGrains[iteratorForInclusions][1] = entry.getKey().y;

                iteratorForInclusions++;
            }
        }

        //przechodzimy przez nia, sprawdzamy ile uzytkownik chce tych wtracen, nastepnie losowo wybieramy miejsca gdzie maja sie pojawic
        Random rand = new Random();

        int numberOfInclusionsBegining = 0;
        while(numberOfInclusionsBegining<Base.InitConditions.numberOfInclusions)
        {
            int n = rand.nextInt(iteratorForInclusions)+1;


            for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
            {
                //oryginal

                if(Base.InitConditions.rangeOfInclusions != 0)
                {
                    if(!Base.InitConditions.circleInclusions)
                        for (int i = 0; i < Base.InitConditions.rangeOfInclusions; i++)
                        {
                            for (int j=0; j<Base.InitConditions.rangeOfInclusions; j++)
                            {
                                if (tabOfBoundaryGrains[n][0]+i == entry.getKey().x && tabOfBoundaryGrains[n][1]+j == entry.getKey().y)
                                {
                                    entry.getValue().setBlack();
                                }
                            }
                        }
                    else
                    {
                        //okragle wtracenia
                        int x2 = tabOfBoundaryGrains[n][0];
                        int y2 = tabOfBoundaryGrains[n][1];
                        int r = Base.InitConditions.rangeOfInclusions;


                        int dx = x2 - entry.getKey().x; // horizontal offset
                        int dy = y2 - entry.getKey().y; // vertical offset
                        if ( (dx*dx + dy*dy) <= (r*r) )
                        {
                            entry.getValue().setBlack();
                        }
                    }
                }
                else
                {
                    if (tabOfBoundaryGrains[n][0] == entry.getKey().x && tabOfBoundaryGrains[n][1] == entry.getKey().y)
                    {
                        entry.getValue().setBlack();
                    }
                }
                //---------
            }
            numberOfInclusionsBegining++;
        }
    }
    //-------------------------

    //dodanie wtracen przed symulacja
//    public void addInclusionsBefore()
//    {
//        this.saveMap();
//
//        savedEntries = Base.pkts.entrySet();
//        Random generator = new Random();
//
//        Object[] values = savedEntries.toArray();
//
//        int numberOfInclusionsBegining = 0;
//        while(numberOfInclusionsBegining<Core.InitConditions.numberOfInclusions)
//        {
//            Map.Entry randomValue = (Map.Entry) values[generator.nextInt(values.length)];
//
//            Pkt key = (Pkt) randomValue.getKey();
//            P value = (P) randomValue.getValue();
//            value.setBlack();
//
//            for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
//            {
//                if (entry.getKey().equals(key))
//                {
//                    entry.getValue().setBlack();
//                    System.out.println("x= " + entry.getKey().x + " y= " + entry.getKey().y);
//                }
//            }
//            numberOfInclusionsBegining++;
//        }
//
//        for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
//        {
//            if (entry.getValue().color2int() != 0)
//            {
////                entry.getValue().setWhite();
//            }
//        }
//            //--------
//    }
    //--------=======================








    public void addInclusionsBefore()
    {
        this.saveMap();

        savedEntries = Base.pkts.entrySet();
        Random generator = new Random();

        Object[] values = savedEntries.toArray();

        int numberOfInclusionsBegining = 0;
        while(numberOfInclusionsBegining<Base.InitConditions.numberOfInclusions)
        {
            Map.Entry randomValue = (Map.Entry) values[generator.nextInt(values.length)];

            Pkt key = (Pkt) randomValue.getKey();
            P value = (P) randomValue.getValue();
            value.setBlack();

            for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
            {
                if (Base.InitConditions.rangeOfInclusions != 0)
                {
                    if (!Base.InitConditions.circleInclusions)
                    {
                        for (int i = 0; i < Base.InitConditions.rangeOfInclusions; i++)
                        {
                            for (int j = 0; j < Base.InitConditions.rangeOfInclusions; j++)
                            {
                                if (entry.getKey().x+i == key.x && entry.getKey().y+j == key.y)
                                {
                                    entry.getValue().setBlack();
                                    System.out.println("x= " + entry.getKey().x + " y= " + entry.getKey().y);
                                }
                            }
                        }
                    }
                    else
                    {
                        //okragle wtracenia
                        int x2 = key.x;
                        int y2 = key.y;
                        int r = Base.InitConditions.rangeOfInclusions;


                        int dx = x2 - entry.getKey().x; // horizontal offset
                        int dy = y2 - entry.getKey().y; // vertical offset
                        if ( (dx*dx + dy*dy) <= (r*r) )
                        {
                            entry.getValue().setBlack();
                        }
                    }
                }
            }
            numberOfInclusionsBegining++;
        }

        for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
        {
            if (entry.getValue().color2int() != 0)
            {
//                entry.getValue().setWhite();
            }
        }


        this.invalidate();
        this.validate();
        this.refresh();
        //--------
    }










    //TEST
    public void addInclusionsAfterRandom()
    {
        this.saveMap();

        Random generator = new Random();

        Object[] values = Base.pkts.entrySet().toArray();

        int numberOfInclusionsBegining = 0;

        while(numberOfInclusionsBegining<Base.InitConditions.numberOfInclusions)
        {
            Map.Entry randomValue = (Map.Entry) values[generator.nextInt(values.length)];

            Pkt key = (Pkt) randomValue.getKey();
            P value = (P) randomValue.getValue();
            value.setBlack();

            for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
            {
                if (Base.InitConditions.rangeOfInclusions != 0)
                {
                    if (!Base.InitConditions.circleInclusions)
                    {
                        for (int i = 0; i < Base.InitConditions.rangeOfInclusions; i++)
                        {
                            for (int j = 0; j < Base.InitConditions.rangeOfInclusions; j++)
                            {
                                if (entry.getKey().x+i == key.x && entry.getKey().y+j == key.y)
                                {
                                    entry.getValue().setBlack();
                                    System.out.println("x= " + entry.getKey().x + " y= " + entry.getKey().y);
                                }
                            }
                        }
                    }
                    else
                    {
                        //okragle wtracenia
                        int x2 = key.x;
                        int y2 = key.y;
                        int r = Base.InitConditions.rangeOfInclusions;


                        int dx = x2 - entry.getKey().x; // horizontal offset
                        int dy = y2 - entry.getKey().y; // vertical offset
                        if ( (dx*dx + dy*dy) <= (r*r) )
                        {
                            entry.getValue().setBlack();
                        }
                    }
                }

            }
            numberOfInclusionsBegining++;
        }
    }


    //Substructure CA->CA
    public void addSubstructureVer1()
    {
        this.saveMap();
        
        Set<Map.Entry<Pkt, P>> entries = Base.pkts.entrySet();

        Map.Entry<Pkt, P> next =  entries.iterator().next();

        savedSubstructure = new HashSet<>();

        int color2int = next.getValue().color2int();

        System.out.println("Pierwszy z brzegu kolor to: " + color2int);

        for (Map.Entry<Pkt, P> entry : entries)
        {
            int colors = entry.getValue().color2int();

            //wybranie koloru

            //przejscie po mapie, i ustawienie bialego koloru na pozostale ziarna

            if (colors != color2int)
            {
                entry.getValue().setWhite();
            }
            else
            {
                savedSubstructure.add(entry);
                entry.getValue().subs = 1;
            }

            //zachowanie ziarna z wybranym kolorem

            //ponowne uruchomienie algorytmu
        }
    }
    //-------------------

    public void addRerunSubstructureVer1()
    {
        this.saveMap();

        for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
        {
            if(entry.getKey().x == savedSubstructure.iterator().next().getKey().x &&
               entry.getKey().y == savedSubstructure.iterator().next().getKey().y)
            {
                entry.setValue(savedSubstructure.iterator().next().getValue());
            }
        }
    }

    //Substructure CA->CA ------- DUALPHASE
    public void addSubstructureVer2()
    {
        this.saveMap();

        Set<Map.Entry<Pkt, P>> entries = Base.pkts.entrySet();

        Map.Entry<Pkt, P> next =  entries.iterator().next();

        int color2int = next.getValue().color2int();

        System.out.println("Pierwszy z brzegu kolor to: " + color2int);


        for (Map.Entry<Pkt, P> entry : entries)
        {
            int colors = entry.getValue().color2int();

            //wybranie koloru

            //przejscie po mapie, i ustawienie bialego koloru na pozostale ziarna

            if (colors != color2int)
            {
                entry.getValue().setWhite();
            }
            else
            {
                entry.getValue().setPink();
                entry.getValue().subs = 1;
            }

            //zachowanie ziarna z wybranym kolorem

            //ponowne uruchomienie algorytmu
        }
    }
    //-------------------


    public void showOnlyBoundaries()
    {
        this.saveMap();

        double iteratorForGB = 0.0;

        for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
        {
            //sprawdzenie ktore sa na granicy
            boolean checkIfOnBoundary = naGranicy(entry.getKey().x, entry.getKey().y);

            if(checkIfOnBoundary)
            {
                //System.out.println(entry.getKey().x + " " + entry.getKey().y + " " + entry.getValue().color2int() + " is on border");

                entry.getValue().setBlack();

                iteratorForGB++;
            }
        }

        for (Map.Entry<Pkt, P> entry : Base.pkts.entrySet())
        {
            int colors = entry.getValue().color2int();

            if (colors != 0)
            {
                entry.getValue().setWhite();
            }
        }

        System.out.println("Number of boundary grains: " + iteratorForGB);
        double size = Base.pkts.size();
        System.out.println("Number of all grains: " + size);

        double percent = (iteratorForGB/size)*100;

        System.out.println("% GB: " + percent);

    }

//sprawdzaj czy rozmiar planszy (JPanel) sie zmienil i uaktualij rozmiar macierzy
    private void resizeP()
    {

        int w = this.width;
        int h = this.height;

        if (this.getHeight() > 0)
        {
            h = this.getHeight();
        }
        if (this.getWidth() > 0)
        {
            w = this.getWidth();
        }

        this.tabSizeX = this.width / this.pixelSize;
        this.tabSizeY = this.height / this.pixelSize;

        if (h != this.height || w != this.width)
        { // zmiana rozmiaru planszy -  przebuduj tab[t]


            System.out.print("Changing board size from : " + this.tabSizeX
                    + " x " + this.tabSizeY + ";"); // --------

            P[][] P_new = new P[w / this.pixelSize][h / this.pixelSize];
            for (int i = 0; i < w / this.pixelSize; i++)
            {
                for (int j = 0; j < h / this.pixelSize; j++)
                {
                    P_new[i][j] = new P();
                }
            }
            // foreach old array
            for (int i = 0; i < this.tabSizeX; i++)
            {
                for (int j = 0; j < this.tabSizeY; j++)
                {
                    if (i >= (w / this.pixelSize) || j >= (h / this.pixelSize))
                    {
                        continue;
                    }
                    P_new[i][j] = this.P[i][j];
                }
            }

            // set new size
            this.width = w;
            this.height = h;
            this.tabSizeX = this.width / this.pixelSize;
            this.tabSizeY = this.height / this.pixelSize;

            System.out.print(" on: " + this.tabSizeX + " x " + this.tabSizeY
                    + "; \n"); // -------------------------
            // odswiez tab
            this.P = P_new;
        }//[t]
    }

    private P makeAct(int x, int y, String t)
    {
        int[] s = {0, 0, 0, 0, 0, 0, 0, 0};
        int[] s_pattern = {0, 0, 0, 0, 0, 0, 0, 0};

        /**
         * 0 - lewa gorna; 1 - centralna gorna; 2 - prawa gorna; 3 - prawa
         * srodkowa 4 - prawa dolna; 5 - centralna dolna; 6 - lewa dolna; 7 -
         * lewa srodkowa
         */
        int left_x = x - 1;
        int right_x = x + 1;
        int top_y = y - 1;
        int bottom_y = y + 1;

        if (Base.InitConditions.bc == 0)
        { // periodycznosc
            if (left_x < 0)
            {
                left_x = this.tabSizeX - 1;
            }
            if (right_x > this.tabSizeX - 1)
            {
                right_x = 0;
            }

            if (top_y < 0)
            {
                top_y = this.tabSizeY - 1;
            }
            else if (bottom_y > this.tabSizeY - 1)
            {
                bottom_y = 0;
            }
        }

        if (t == "act")
        {
            // uzupelnij tabele sasiadow
            //complete the table of neighbors
            if (left_x >= 0 && top_y >= 0)
            {
                s[0] = this.P[left_x][top_y].prev;
            }
            if (top_y >= 0)
            {
                s[1] = this.P[x][top_y].prev;
            }
            if (top_y >= 0 && right_x <= this.tabSizeX - 1)
            {
                s[2] = this.P[right_x][top_y].prev;
            }
            if (right_x <= this.tabSizeX - 1)
            {
                s[3] = this.P[right_x][y].prev;
            }
            if (bottom_y <= this.tabSizeY - 1 && right_x <= this.tabSizeX - 1)
            {
                s[4] = this.P[right_x][bottom_y].prev;
            }
            if (bottom_y <= this.tabSizeY - 1)
            {
                s[5] = this.P[x][bottom_y].prev;
            }
            if (left_x >= 0 && bottom_y <= this.tabSizeY - 1)
            {
                s[6] = this.P[left_x][bottom_y].prev;
            }
            if (left_x >= 0)
            {
                s[7] = this.P[left_x][y].prev;
            }
        }
        else
        {
            System.out.println("-- blad: " + t + " nie ma sensu :[ --");
        }

        P newP = new P();
        newP.set(-1);

        Random r = new Random();
        switch (Base.InitConditions.sasiedztwo)
        {
            case 4:
                s_pattern = pattern.get(r.nextInt(2) + 2);
                break;
            case 9:
                s_pattern = pattern.get(r.nextInt(4) + 5);
                break;
            default:
                s_pattern = pattern.get(Base.InitConditions.sasiedztwo);
                break;
        }

        if (s[0] == 1 && s_pattern[4] == 1)
        {
            return this.P[left_x][top_y];
        }
        else if (s[1] == 1 && s_pattern[5] == 1)
        {
            return this.P[x][top_y];
        }
        else if (s[2] == 1 && s_pattern[6] == 1)
        {
            return this.P[right_x][top_y];
        }
        else if (s[3] == 1 && s_pattern[7] == 1)
        {
            return this.P[right_x][y];
        }
        else if (s[4] == 1 && s_pattern[0] == 1)
        {
            return this.P[right_x][bottom_y];
        }
        else if (s[5] == 1 && s_pattern[1] == 1)
        {
            return this.P[x][bottom_y];
        }
        else if (s[6] == 1 && s_pattern[2] == 1)
        {
            return this.P[left_x][bottom_y];
        }
        else if (s[7] == 1 && s_pattern[3] == 1)
        {
            return this.P[left_x][y];
        }
        return newP;

    }

    /**
     * zapis obecnego stanu komorek jako poprzedni krok czasowy
     * save current state of cell as previous time step
     */
    private void reverse()
    {
        for (int i = 0; i < this.tabSizeX; i++)
        {
            for (int j = 0; j < this.tabSizeY; j++)
            {
                this.P[i][j].prev = this.P[i][j].act;
            }
        }
    }

    /**
     * generuj nowy zarodek przez klikniecie myszka
     * generate new nucleon by clicking mouse
     */
    private void clickPixel(int x, int y)
    {
        int pixel_x = x / this.pixelSize;
        int pixel_y = y / this.pixelSize;

        while (pixel_x < 1)
        {
            pixel_x++;
        }
        while (pixel_x >= this.tabSizeX - 1)
        {
            pixel_x--;
        }
        while (pixel_y < 1)
        {
            pixel_y++;
        }
        while (pixel_y >= this.tabSizeY - 1)
        {
            pixel_y--;
        }

        if (pixel_x >= this.tabSizeX || pixel_y >= this.tabSizeY)
        {
            return;
        }

        if (this.P[pixel_x][pixel_y].act == 0)
        {
            this.P[pixel_x][pixel_y] = new P();
            this.P[pixel_x][pixel_y].set_random(1);
        }
        this.reverse();
    }

    /**
     * czy dana komorka lezy na granicy swojego ziarna
     * check if the cell is on border of its grain
     */
    private boolean naGranicy(int x, int y)
    {
        boolean[] s = new boolean[8];

        int left_x = x - 1;
        int right_x = x + 1;
        int top_y = y - 1;
        int bottom_y = y + 1;

        if (Base.InitConditions.bc == 0)
        { // periodycznosc
            if (left_x < 0)
            {
                left_x = this.tabSizeX - 1;
            }
            if (right_x > this.tabSizeX - 1)
            {
                right_x = 0;
            }

            if (top_y < 0)
            {
                top_y = this.tabSizeY - 1;
            }
            else if (bottom_y > this.tabSizeY - 1)
            {
                bottom_y = 0;
            }
        }

        if (left_x >= 0 && top_y >= 0)
        {
            s[0] = this.P[left_x][top_y].colorMatch(this.P[x][y]);
        }
        if (top_y >= 0)
        {

        }
        s[1] = this.P[x][top_y].colorMatch(this.P[x][y]);
        if (top_y >= 0 && right_x <= this.tabSizeX - 1)
        {
            s[2] = this.P[right_x][top_y].colorMatch(this.P[x][y]);
        }
        if (right_x <= this.tabSizeX - 1)
        {
            s[3] = this.P[right_x][y].colorMatch(this.P[x][y]);
        }
        if (bottom_y <= this.tabSizeY - 1 && right_x <= this.tabSizeX - 1)
        {
            s[4] = this.P[right_x][bottom_y].colorMatch(this.P[x][y]);
        }
        if (bottom_y <= this.tabSizeY - 1)
        {
            s[5] = this.P[x][bottom_y].colorMatch(this.P[x][y]);
        }
        if (left_x >= 0 && bottom_y <= this.tabSizeY - 1)
        {
            s[6] = this.P[left_x][bottom_y].colorMatch(this.P[x][y]);
        }
        if (left_x >= 0)
        {
            s[7] = this.P[left_x][y].colorMatch(this.P[x][y]);
        }

        //System.out.println("rgb("+this.P[left_x][top_y].R+") == rgb("+this.P[x][y].R+")");
        for (boolean ss : s)
        {
            if (!ss) return true;
        }
        return false;
    }
}
