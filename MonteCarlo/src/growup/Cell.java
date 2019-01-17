/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package growup;

import java.awt.*;

/*! \brief Class responsible for cell logic
 *        
 */

public class Cell
{
    private int x;
    private int y;
    public static int size = 20;  ///< Size of one cell in px's
    private boolean alive;
    public boolean nextIteration;
    public boolean nextIterationRec = false;
    public Color color;
    public Color colorS; //id for energy
    private boolean recrystalizationReady = false;
    public boolean border = false;
    public double disCounter = 0;

    public int H; //energy

    public boolean isAlive()
    {
        return alive;
    }

    public void setAlive(boolean alive)
    {
        this.alive = alive;
    }

    public void setNextIteration(boolean nextIt)
    {
        this.nextIteration = nextIt;
    }

    public void nextIteration()
    {
        alive = nextIteration;
    }

    public void nextIterationRec()
    {
        recrystalizationReady = nextIterationRec;
    }

    public Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void drawCell(Graphics g)
    {
            /*g.setColor(Color.BLACK);
            g.drawRect(x*size, y*size, size, size);
            if(alive) g.setColor(this.color);
            else
            g.setColor(Color.WHITE);
            g.fillRect(x*size+1, y*size+1, size-1 , size-1);*/

        if (isAlive())
            g.setColor(this.color);
        else
            g.setColor(Color.WHITE);

        g.fillRect(x * size, y * size, size, size);
    }
    
    public void drawE(Graphics g)
    {
            /*g.setColor(Color.BLACK);
            g.drawRect(x*size, y*size, size, size);
            if(alive) g.setColor(this.color);
            else
            g.setColor(Color.WHITE);
            g.fillRect(x*size+1, y*size+1, size-1 , size-1);*/

        if (isAlive())
            g.setColor(this.colorS);
        else
            g.setColor(Color.WHITE);

        g.fillRect(x * size, y * size, size, size);
    }

    public boolean isReady()
    {
        return recrystalizationReady;
    }

    public void setReady(boolean ready)
    {
        this.recrystalizationReady = ready;
    }
}
