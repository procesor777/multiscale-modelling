/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package montecarlo;

import java.awt.*;

public class ColorCnt
{
    private Color color;
    private int counter;

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public int getCounter()
    {
        return counter;
    }

    public void setCounter(int counter)
    {
        this.counter = counter;
    }

    public void incrementCounter()
    {
        this.counter++;
    }

    public ColorCnt(Color color)
    {
        this.color = color;
        this.counter = 1;
    }
}
