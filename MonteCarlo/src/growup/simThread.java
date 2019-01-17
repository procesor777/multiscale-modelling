/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package growup;

public class simThread implements Runnable
{

    private Frame accessToFrame;

    @Override
    public void run()
    {
        Frame f = new Frame();

        accessToFrame = f;

        //f.iterationTime=simSpeed;

        f.setVisible(true);
        f.setResizable(false);

        f.setLocationRelativeTo(null);

        f.createScreen();


        while (true)
        {
            /*long thisFrame= System.currentTimeMillis();
            float tslf = (float) ((thisFrame-lastFrame)/100.0);
            lastFrame=thisFrame;*/

            //f.update(tslf);

            
            f.update();

            f.repaint();

            /*try {
            Thread.sleep(10);
            } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }*/
        }
    }

    public Frame getAccessToFrame()
    {
        return accessToFrame;
    }
}
