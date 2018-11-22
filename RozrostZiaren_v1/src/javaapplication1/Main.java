package javaapplication1;

import javaapplication1.View;
import javax.swing.*;

/**
 * Created by procesor777 on 2016-05-05.
 */
public class Main
{
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            View frame = new View();
            frame.setVisible(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
