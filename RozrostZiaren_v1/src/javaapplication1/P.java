package javaapplication1;

import java.util.Random;

//komorka macierzy; rozmiar = Core.Config.pixelSize

public class P
{
    // komorka jest ziarnem lub nie //cell is grain or not
    public int act = 0;
    public int prev = 0;
    public int subs = 0;

    // RGB ziaren //RGB of grains
    public int R = 250;
    public int G = 250;
    public int B = 250;

    public P()
    {
    }

    public P(P p)
    {
        this.prev = p.prev;
        this.act = p.act;
        this.R = p.R;
        this.G = p.G;
        this.B = p.B;
    }

    public P(int act, int prev, int integerRGB)
    {
        this.act = act;
        this.prev = prev;

        loadRGB(integerRGB);
    }

    public void set(int x)
    {
        this.prev = x;
        this.act = x;

        this.R = 250;
        this.G = 250;
        this.B = 250;
    }

    public void set_random(int x)
    {
        this.set(x);

        Random r = new Random();

        this.R = r.nextInt(250);
        this.G = r.nextInt(250);
        this.B = r.nextInt(250);
    }

    public void recolor(P p)
    {
        this.R = p.R;
        this.G = p.G;
        this.B = p.B;
    }

    //setBlack
    public void setBlack()
    {
        this.R = 0;
        this.G = 0;
        this.B = 0;
    }
    //------------

    //setWhite
    public void setWhite()
    {
        this.R = 255;
        this.G = 255;
        this.B = 255;
    }
    //------------

    //setPink
    public void setPink()
    {
        this.R = 255;
        this.G = 192;
        this.B = 203;
    }
    //------------

    public boolean colorMatch(P p)
    {
        if (this.R == 250 && this.G == 250 && this.B == 250)
        {
            return false;
        }
        if (this.R == p.R && this.G == p.G && this.B == p.B)
        {
            return true;
        }
        return false;
    }

    public Integer color2int()
    {
        int rgb = this.R;
        rgb = (rgb << 8) + this.G;
        rgb = (rgb << 8) + this.B;
        return rgb;
    }

    public void loadRGB(Integer _rgb)
    {
        int rgb = _rgb;
        this.R = (rgb >> 16) & 0xFF;
        this.G = (rgb >> 8) & 0xFF;
        this.B = rgb & 0xFF;
    }

    public boolean equals(P p)
    {
        return this.colorMatch(p);
    }
}
