/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package growup;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
    public static int no;
    private Screen s;
//    private Screen2 s2;
    private Simulation sim;

    private float timeSinceLastUpdate;
    public float iterationTime = 1f;

    public Frame()
    {
        setSize(800, 600);
        setTitle("Simulation");
    }

    public void createScreen()
    {
        sim = new Simulation();
        sim = new Simulation();
            s = new Screen();
            s.setBounds(0, 0, 800, 600);
            add(s);
        this.addKeyListener(sim);
    }

    public void repaint()
    {
        s.repaint();
    }

    public void update()
    {
        sim.update();
    }

    //------------------
    public Cell[][] updateDualPhaseStepOne()
    {
        Cell[][] dualPhaseCells = sim.dualPhaseStepOne();
        repaint();

        return dualPhaseCells;
    }

    public void updateDualPhaseStepTwo(Cell[][] dualPhaseCells)
    {
        sim.dualPhaseStepTwo(dualPhaseCells);
        repaint();
    }

    public void updateDualPhaseCAMC()
    {
        sim.dualPhaseCAMC();
        repaint();
    }

    public void updateDualPhaseMCCA()
    {
        sim.dualPhaseMCCA();
    }
    //------------------

    //------------------
    public void updateEnergyDistribution(int type)
    {
        sim.energyDistribution(type);
        repaint();
    }
    //------------------

    //------------------
    public void updateAddNewNucleons()
    {
        //sim.drawEnergy(g);
       // repaint();
        if(sim.energy == false)
            sim.energy =true;
        else
            sim.energy=false;
    }
    //------------------


    private class Screen extends JLabel
    {
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponents(g);
            sim.drawCells(g);
        }
        
    }
   
}