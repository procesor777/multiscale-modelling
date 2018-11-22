package javaapplication1;

public class Pkt
{
    public int x;
    public int y;

    Pkt(int a, int b)
    {
        this.x = a;
        this.y = b;
    }

    public boolean equals(Pkt p)
    {
        if (this.x == p.x && this.y == p.y) return true;
        return false;
    }

    public boolean inR(Pkt p, int r)
    {
        if (Math.abs(p.x - this.x) < r && Math.abs(p.y - this.y) < r) return true;
        return false;
    }
}
