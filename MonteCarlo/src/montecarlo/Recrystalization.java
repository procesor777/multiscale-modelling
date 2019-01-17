/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package montecarlo;

public class Recrystalization
{
    public double time = 0;
    public long disclocationLast = 0;
    public long dislocationNo = 0;
    public double ro;
    //private double A,B;
    public long dislocationDelta = 0;
    public long critDislocation = 0;
    public double roDelta = 0;
    public double lastRo = 0;
    
    /*      public Recrystalization() throws FileNotFoundException{
    
    zapis = new PrintWriter("wyniki.txt");
    }*/

    public void update()
    {
        double A = 86710969050178.5;
        double B = 9.41268203527779;
        // disclocationLast=dislocationNo;
        lastRo = ro;
        time = time + 0.01;
        ro = A / B + (1 - A / B) * Math.pow(Math.E, -B * time);
        // dislocationNo=(long) (1.9+(0.00000027*10)*(8*100000000)*ro);

        // System.out.println(roDelta/(800*600));
        //  dislocationNo=(long) ro;
        //  dislocationDelta=dislocationNo-disclocationLast;
        roDelta = ro - lastRo;
    }
}
