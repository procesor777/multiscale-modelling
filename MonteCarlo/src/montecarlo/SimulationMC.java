/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package montecarlo;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.event.KeyEvent.VK_SPACE;


public class SimulationMC implements KeyListener
{
    Random r, randColor, rHex;
    private Cell[][] cells;
    private int width = 800 / Cell.size;
    private int height = 600 / Cell.size;

    //    float colorR, colorG, colorB;
    int colorR, colorG, colorB;

    public static boolean pause = false;
    public boolean energy = false;
    public static int howMany = 99;

    public static boolean control = false;

    public int HowManyALive()
    {
        int counter = 0;

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (cells[i][j].isAlive()) counter++;
            }
        }
        return counter;
    }

    public int HowManyReady()
    {
        int counter = 0;

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (cells[i][j].isReady()) counter++;
            }
        }
        return counter;
    }

    public int HowManyNOTReady()
    {
        int counter = 0;

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (!cells[i][j].isReady()) counter++;
            }
        }
        return counter;
    }

    public SimulationMC()
    {
        cells = new Cell[width][height];
        System.out.print("jestem");
        ArrayList<Color> colorList = new ArrayList<>();
        randColor = new Random();

        Random randomGenerator = new Random();

        for (int x = 0; x < View.no; x++)
        {
//            colorR = randColor.nextFloat();
//            colorG = randColor.nextFloat();
//            colorB = randColor.nextFloat();

            colorR = randColor.nextInt(250);
            colorG = randColor.nextInt(250);
            colorB = randColor.nextInt(250);

            colorList.add(new Color(colorR, colorG, colorB));
            System.out.println(colorList.size());
        }

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                cells[i][j] = new Cell(i, j);
                cells[i][j].setAlive(true);
            }
        }
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (cells[i][j].isAlive())
                {
                    int index = randomGenerator.nextInt(View.no);
                    cells[i][j].color = colorList.get(index);
                }
            }
        }
    }

    public void drawCells(Graphics g)
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if(energy == false){
                    cells[i][j].drawCell(g);
                }
                else
                {   
                    cells[i][j].drawE(g);
                }
             }
                
        }
    }
    
    public void drawEnergy(Graphics g)
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                cells[i][j].drawE(g);
            }
        }
    }

    private boolean checkFilled()
    {
        boolean result = false;
        int deadCounter = 0;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (!cells[i][j].isAlive()) deadCounter++;
            }
        }
        if (deadCounter != 0) result = false;
        else
            result = true;

        return result;
    }

    private boolean checkFilledRec()
    {
        boolean result = false;
        int deadCounter = 0;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (!cells[i][j].isReady()) deadCounter++;
            }
        }
        if (deadCounter != 0) result = false;
        else
            result = true;

        return result;
    }

    int iterator = 0;
    ArrayList<Point> points = new ArrayList<>();

    public void update()
    {
        if (control)
        {
            if (howMany == iterator)
            {
                pause = true;

                iterator = 0;
            }
        }
        if (!pause)
        {
            //System.out.println("ITERACJA NR: "+iterator);
            int energyBefore = 0;
            int energyAfter = 0;
            int energySum = 0;
            ArrayList<Color> nbColors = new ArrayList<>();
            ArrayList<Color> nbColorsS = new ArrayList<>();
            if (points.size() >= cells.length)
            {
                iterator++;
                points = new ArrayList<>();
            }
            checkBorders();
            Random rn = new Random();
            int rangeX = (width - 1) - 0 + 1;
            int i = rn.nextInt(rangeX) + 0;

            int rangeY = (height - 1) - 0 + 1;
            int j = rn.nextInt(rangeY) + 0;

            //&&!points.contains(new Cartez(i,j))

            if (cells[i][j].border && !points.contains(new Point(i, j)))
            {
                points.add(new Point(i, j));

                int mx = i - 1;
                if (mx < 0) mx = width - 1;
                int my = j - 1;
                if (my < 0) my = height - 1;
                int gx = (i + 1) % width;
                int gy = (j + 1) % height;

                // nbColors.add(cells[i][j].color);

                nbColors.add(cells[mx][my].color);
                nbColors.add(cells[mx][j].color);
                nbColors.add(cells[mx][gy].color);
                nbColors.add(cells[i][my].color);
                nbColors.add(cells[i][gy].color);
                nbColors.add(cells[gx][my].color);
                nbColors.add(cells[gx][j].color);
                nbColors.add(cells[gx][gy].color);
                
             

                int iterator = 0;

                for (Color object : nbColors)
                {
                    energyBefore = 0;
                    energyAfter = 0;
                    Random rg = new Random();
                    int index = rg.nextInt(nbColors.size());
                    Color tested = nbColors.get(index);

                    if (cells[mx][my].color != tested) energyBefore++;
                    if (cells[mx][j].color != tested) energyBefore++;
              
                    
                    if (cells[mx][gy].color != tested) energyBefore++;
                    if (cells[i][my].color != tested) energyBefore++;
                    if (cells[i][gy].color != tested) energyBefore++;
                    if (cells[gx][my].color != tested) energyBefore++;
                    if (cells[gx][j].color != tested) energyBefore++;
                    if (cells[gx][gy].color != tested) energyBefore++;

                    int index1 = rg.nextInt(nbColors.size());
                    Color tested1 = nbColors.get(index1);

                   if (cells[mx][my].color != tested1) energyBefore++;
                    if (cells[mx][j].color != tested1) energyBefore++;
                    
                    if (cells[mx][gy].color != tested1) energyAfter++;
                    if (cells[i][my].color != tested1) energyAfter++;
                    if (cells[i][gy].color != tested1) energyAfter++;
                    if (cells[gx][my].color != tested1) energyAfter++;
                    if (cells[gx][j].color != tested1) energyAfter++;
                    if (cells[gx][gy].color != tested1) energyAfter++;

                    //  System.out.println(dEnergy);

                    energySum = energyAfter - energyBefore;

                    if (energySum <= 0)
                    {
                        cells[i][j].color = tested1;
                       // cells[i][j].colorS = tested1;
                       // System.out.println(energyAfter + " " + energyBefore);
                        //System.out.println("Changing cell: " + i + " : " + j);
                    }
                    else
                    {
                        cells[i][j].color = tested;
                        //ells[i][j].colorS = tested;
                       // System.out.println(energyAfter + " " + energyBefore);
                       // System.out.println("Not changing cell: " + i + " : " + j);
                    }

//                    if (energyAfter <= energyBefore)
//                    {
//                        cells[i][j].color = tested1;
//                        System.out.println(energyAfter + " " + energyBefore);
//                        System.out.println("Changing cell: " + i + " : " + j);
//                    }
                    //energyBefore = energyAfter;

                }
            }
        }
    }

    public void checkBorders()
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                int mx = i - 1;
                if (mx < 0) mx = width - 1;
                int my = j - 1;
                if (my < 0) my = height - 1;
                int gx = (i + 1) % width;
                int gy = (j + 1) % height;

                Color myColor = cells[i][j].color;

                if (cells[mx][my].color != myColor)
                {
                    cells[i][j].border = true;
                }
                else
                {
                    cells[i][j].border = false;
                }

                if (cells[mx][j].color != myColor)
                {
                    cells[i][j].border = true;
                }
                else
                {
                    cells[i][j].border = false;
                }

                if (cells[mx][gy].color != myColor)
                {
                    cells[i][j].border = true;
                }
                else
                {
                    cells[i][j].border = false;
                }

                if (cells[i][my].color != myColor)
                {
                    cells[i][j].border = true;
                }
                else
                {
                    cells[i][j].border = false;
                }

                if (cells[i][gy].color != myColor)
                {
                    cells[i][j].border = true;
                }
                else
                {
                    cells[i][j].border = false;
                }

                if (cells[gx][my].color != myColor)
                {
                    cells[i][j].border = true;
                }
                else
                {
                    cells[i][j].border = false;
                }

                if (cells[gx][j].color != myColor)
                {
                    cells[i][j].border = true;
                }
                else
                {
                    cells[i][j].border = false;
                }

                if (cells[gx][gy].color != myColor)
                {
                    cells[i][j].border = true;
                }
                else
                {
                    cells[i][j].border = false;
                }
            }
        }
    }

    //-----------------
    //Choose one color, and wipe out others
    public Cell[][] dualPhaseStepOne()
    {
        Cell[][] dualPhaseCells = new Cell[width][height];
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                dualPhaseCells[i][j] = new Cell(i, j);
                dualPhaseCells[i][j].setAlive(true);
            }
        }

        Random rg = new Random();
        int cell_i = rg.nextInt(width);
        System.out.println("cell_i = " + cell_i);
        int cell_j = rg.nextInt(height);
        System.out.println("cell_j = " + cell_j);

        Color myColor = cells[cell_i][cell_j].color;
        System.out.println("cells[cell_i][cell_j].color = " + cells[cell_i][cell_j].color);

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (cells[i][j].color != myColor)
                {
                    cells[i][j].color = Color.WHITE;
                    dualPhaseCells[i][j].color = Color.WHITE;
                }
                else
                {
                    dualPhaseCells[i][j].color = myColor;
                }
            }
        }
        return dualPhaseCells;
    }

    //Insert Dual Phase cells
    public void dualPhaseStepTwo(Cell[][] dualPhaseCells)
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (dualPhaseCells[i][j].color != Color.WHITE)
                {
                    cells[i][j].color = dualPhaseCells[i][j].color;
                }
            }
        }
    }
    //-----------------

    //dualPhase from CA to MC
    public void dualPhaseCAMC()
    {
        String filePath = "ExportCAMC.txt";

        Cell[][] dualPhaseCells = new Cell[width][height];
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                dualPhaseCells[i][j] = new Cell(i, j);
            }
        }

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

                    dualPhaseCells[xx][yy].color = new Color(integerRGBInt);
                }
                else
                {
                    System.out.println("ignoring line: " + line);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (dualPhaseCells[i][j].color != null)
                {
                    if (!dualPhaseCells[i][j].color.equals(Color.white))
                    {
                        cells[i][j].color = dualPhaseCells[i][j].color;
                    }
                }
            }
        }
    }
    //---------------------

    //dualPhase from MC to CA
    public void dualPhaseMCCA()
    {
        int prev = 1;
        int act = 1;

        try
        {
            //BufferedWriter out = new BufferedWriter(new FileWriter("../RozrostZiaren_v1/ExportMCCA.txt"));
            BufferedWriter out = new BufferedWriter(new FileWriter("C:\\ExportCAMC.txt"));
            out.write(String.valueOf(width) + " " + String.valueOf(height));
            out.newLine();

            for (int i = 0; i < width; i++)
            {
                for (int j = 0; j < height; j++)
                {
                    //out.write(entry.getKey().x + " " + entry.getKey().y + " 0 " + entry.getValue().color2int() + " " + entry.getValue().prev + " " + entry.getValue().act);
                    out.write(i + " " + j + " 0 " + cells[i][j].color.getRGB() + " " + prev + " " + act);
                    out.newLine();
                }
            }
            out.close();
            //Process p = new ProcessBuilder("JavaApplication1.jar", "-jar").start();
            ArrayList<String> command = new ArrayList<String>();
		    
		    command.add("java");
		    command.add("-jar");
		    command.add("..\\RozrostZiaren_v1\\dist\\Javaapplication1.jar");
		    
		    ProcessBuilder builder = new ProcessBuilder(command);		    
		    Process process = builder.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }


    //energyDistribution
    public void energyDistribution(int type)
    {
        //type = 1 = homogenous
        //type = 2 = heterogenous

        System.out.println("Width: " + width);
        System.out.println("Height: " + height);

        if (type == 1)
        {
            for (int i = 0; i < width; i++)
            {
                for (int j = 0; j < height; j++)
                {
                    cells[i][j].colorS = new Color(47, 141, 255);
                    cells[i][j].H = 5;
                }
            }
        }
        else if (type == 2)
        {
            //checkBorders();
            for (int i = 1; i < width-1; i++)
            {
                for (int j = 1; j < height - 1; j++)
                {
                   /* if (i == width - 1 && (!cells[i][j].color.equals(cells[0][j].color)))
                    {
                        cells[i][j].colorS = new Color(18, 247, 41);
                        cells[i][j].H = 5;
                    }
                    else*/
                    //{
                        if (!cells[i][j].color.equals(cells[i][j + 1].color))
                        {
                            cells[i][j].colorS = new Color(18, 247, 41);
                            cells[i][j].H = 5;
                        }
                        else if (!cells[i][j].color.equals(cells[i + 1][j].color))
                        {
                           cells[i][j].colorS = new Color(18, 247, 41);
                           cells[i][j].H = 5;
                        }
                        else
                        {
                            cells[i][j].colorS = new Color(47, 141, 255);
                            cells[i][j].H = 2;
                        }
                   // }


//                    if (cells[i][j].border == true)
//                    {
//                        cells[i][j].color = new Color(18,247,41);
//                        cells[i][j].H = 5;
//                    }
//                    else
//                    {
//                        cells[i][j].color = new Color(47,141,255);
//                        cells[i][j].H = 2;
//                    }
                }
            }
        }
    }
    //----------------------


    //----------------------
    public void newNucleons(int numberOfNewNucleons)
    {
        
                Random generator = new Random();

        for (int i = 0; i < numberOfNewNucleons; i++)
        {
            int rand_i = generator.nextInt(width);
            int rand_j = generator.nextInt(height);

            colorR = randColor.nextInt(250);
            colorG = randColor.nextInt(250);
            colorB = randColor.nextInt(250);

            cells[rand_i][rand_j].color = new Color(colorR, colorG, colorB);
            cells[rand_i][rand_j].setAlive(true);
        } 
    }
    //----------------------


    @Override
    public void keyTyped(KeyEvent e)
    {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int codePause = VK_SPACE;

        if (e.getKeyCode() == codePause)
        {
            if (pause) pause = false;
            else pause = true;
            System.out.println("PAUSE");
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
