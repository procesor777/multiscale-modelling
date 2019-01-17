/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package growup;

public class GeoOps
{
    public static boolean belongsRadius(int x, int y, int rad, int rootX, int rootY)
    {
        double d = Math.sqrt(((rootX - x) * (rootX - x)) - ((rootY - y) * (rootY - y)));

        if (d <= rad) return true;
        else
            return false;

    }
}
