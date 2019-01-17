package growup;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Przemyslaw Wal on 2018-12-28.
 */
public class MainGUI_1 extends JFrame
{
    private JPanel contentPane;

    private JButton startButton;
    private JButton stopButton;
    private JLabel grainsNumberLabel;
    private JSpinner grainsNumberSpinner;
    private JLabel grainsSizeLabel;
    private JSpinner grainsSizeSpinner;
    private JLabel cyclesNumberLabel;
    private JSpinner cyclesNumberSpinner;
    private JCheckBox cyclesNumberCheckBox;

    //-------------
    private JButton dualPhaseStepOneButton;
    private JButton dualPhaseStepTwoButton;
    private JButton dualPhaseCAMCButton;
    private JButton dualPhaseMCCAButton;
    private simThread runner;
    //--------------

    //--------------
    private Cell[][] dualPhaseCells;
    //--------------

    //--------------
    //Energy distribution
    private JCheckBox homogenousEnergyDistrCheckBox;
    private JCheckBox heterogenousEnergyDistrCheckBox;

    private int energyDistributionType;
    //--------------

    //--------------
    private int newNucleonsNumber = 100;
    private JButton addNewNucleonsButton;
    //--------------

    public MainGUI_1()
    {
        showWindow();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        JPanel panel1 = new JPanel(new GridLayout(0, 1));
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        contentPane.add(panel, BorderLayout.WEST);
        contentPane.add(panel1, BorderLayout.EAST);
        contentPane.add(panel2, BorderLayout.SOUTH);
        contentPane.add(panel3, BorderLayout.NORTH);

        grainsNumberLabel = new JLabel("Number of grains");
        panel.add(grainsNumberLabel);
        grainsSizeLabel = new JLabel("Size of grain (px)");
        panel.add(grainsSizeLabel);
        grainsNumberSpinner = new JSpinner();
        panel1.add(grainsNumberSpinner);
        cyclesNumberCheckBox = new JCheckBox("Control cycles");
        panel.add(cyclesNumberCheckBox);

        cyclesNumberCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean selected = cyclesNumberCheckBox.isSelected();
                cyclesNumberCheckBoxActionPerformed(e, selected);
            }
        });

        cyclesNumberLabel = new JLabel("Number of cycles:");
        panel.add(cyclesNumberLabel);
        grainsSizeSpinner = new JSpinner();
        panel1.add(grainsSizeSpinner);
        cyclesNumberLabel.setEnabled(false);
        cyclesNumberSpinner = new JSpinner();
        panel1.add(cyclesNumberSpinner);
        cyclesNumberSpinner.setEnabled(false);
        cyclesNumberSpinner.setValue(new Integer(50));


        homogenousEnergyDistrCheckBox = new JCheckBox("Homogenous");
        panel.add(homogenousEnergyDistrCheckBox);

        homogenousEnergyDistrCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean selected = homogenousEnergyDistrCheckBox.isSelected();
                int type = 1;
                if (selected)
                {
                    heterogenousEnergyDistrCheckBox.setEnabled(false);
                    energyDistributionType = type;
                }
                else
                {
                    heterogenousEnergyDistrCheckBox.setEnabled(true);
                }
                energyDistributionButtonActionPerformed(e, type);
            }
        });

        heterogenousEnergyDistrCheckBox = new JCheckBox("Heterogenous");
        panel1.add(heterogenousEnergyDistrCheckBox);

        heterogenousEnergyDistrCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean selected = heterogenousEnergyDistrCheckBox.isSelected();
                int type = 2;
                if (selected)
                {
                    homogenousEnergyDistrCheckBox.setEnabled(false);
                    energyDistributionType = type;
                }
                else
                {
                    homogenousEnergyDistrCheckBox.setEnabled(true);
                }
                energyDistributionButtonActionPerformed(e, type);
            }
        });

        startButton = new JButton("Start");
        panel2.add(startButton);

        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                startButtonActionPerformed(e);
            }
        });

        stopButton = new JButton("Stop/Continue");
        panel2.add(stopButton);

        stopButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                stopButtonActionPerformed(e);
            }
        });

        dualPhaseStepOneButton = new JButton("DP_St1");
        panel3.add(dualPhaseStepOneButton);

        dualPhaseStepOneButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame accessToFrame = runner.getAccessToFrame();
                dualPhaseCells = accessToFrame.updateDualPhaseStepOne();
            }
        });

        dualPhaseStepTwoButton = new JButton("DP_St2");
        panel3.add(dualPhaseStepTwoButton);

        dualPhaseStepTwoButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame accessToFrame = runner.getAccessToFrame();
                accessToFrame.updateDualPhaseStepTwo(dualPhaseCells);
            }
        });

        dualPhaseCAMCButton = new JButton("DP_CA->MC");
        panel3.add(dualPhaseCAMCButton);

        dualPhaseCAMCButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame accessToFrame = runner.getAccessToFrame();
                accessToFrame.updateDualPhaseCAMC();
            }
        });

        dualPhaseMCCAButton = new JButton("DP_MC->CA");
        panel3.add(dualPhaseMCCAButton);

        dualPhaseMCCAButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame accessToFrame = runner.getAccessToFrame();
                accessToFrame.updateDualPhaseMCCA();
            }
        });

        addNewNucleonsButton = new JButton("AddNewNucl");
        panel3.add(addNewNucleonsButton);

        addNewNucleonsButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame accessToFrame = runner.getAccessToFrame();
                accessToFrame.updateAddNewNucleons();
            }
        });
    }

    public void showWindow()
    {
        setTitle("Monte Carlo Growth");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(480, 211));
        setLocationByPlatform(true);
        pack();
        setVisible(true);
    }

    private void startButtonActionPerformed(ActionEvent e)
    {
//        simThread runner = new simThread();
        runner = new simThread();
        //  System.out.print(nbhoodCheck.getSelectedItem().toString());
        Cell.size = (int) grainsSizeSpinner.getValue();
        Frame.no = (int) grainsNumberSpinner.getValue();
        Thread thread = new Thread(runner);
        thread.start();
    }

    private void stopButtonActionPerformed(ActionEvent e)
    {
        if (Simulation.pause)
            Simulation.pause = false;
        else
            Simulation.pause = true;
    }

    private void cyclesNumberCheckBoxActionPerformed(ActionEvent e, boolean selected)
    {
        if (selected)
        {
            Simulation.control = true;
            cyclesNumberLabel.setEnabled(true);
            cyclesNumberSpinner.setEnabled(true);
        }
        else
        {
            Simulation.control = false;
            cyclesNumberLabel.setEnabled(false);
            cyclesNumberSpinner.setEnabled(false);
        }
    }

    private void energyDistributionButtonActionPerformed(ActionEvent e, int type)
    {
        //type = 1 = homogenous
        //type = 2 = heterogenous
        Frame accessToFrame = runner.getAccessToFrame();
        accessToFrame.updateEnergyDistribution(type);
    }


    public static void main(String[] args)
    {
        try
        {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new MainGUI_1().setVisible(true);
            }
        });
    }
}
