/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

import java.util.ArrayList;
import static util.math.FastMath.sqrt;
import util.math.Vector3f;
import util.math.Vector4f;

/**
 *
 * @author moniq
 */
public class Surface {

    protected ArrayList<Vector4f> colors;
    protected ArrayList<Vector4f> positions;
    protected ArrayList<Vector4f> normal;

    public Surface() {
        positions = new ArrayList<>();
        colors = new ArrayList<>();
        normal = new ArrayList<>();
    }
//

    public void addVertex(Vector3f p) {

        Vector4f p4f = new Vector4f(p, 1.0f);
        positions.add(p4f);
        normal.add(p4f);
        Vector4f yellow = new Vector4f(1.0f, 1.0f, 0.0f, 1.0f);
        colors.add(yellow);
    }

    Vector3f gradientAt(float x, float y, float z) {

        Vector3f result = new Vector3f(x, y, z);
        normalize(result);
        return result;
    }

    public float valueAt(float x, float y, float z) {
        double a = 1;
        double b = 1;
        double c = -1;
        
        double xPo = 2;
        double yPo = 2;
        double zPo = 2;
        //1, 2, -1, 2, 1, 2, 300
        return (float) (((Math.pow((double) x, xPo)) * a)
                + ((Math.pow((double) y, yPo)) * b)
                + ((Math.pow((double) z, zPo)) * c));
    }

    public boolean isIn(float x, float y, float z) {

        float epsilon = 0.01f;

        float value = valueAt(x, y, z);
        if (value <= epsilon) {
            return true;
        }
        return false;
    }

    void normalize(Vector3f v) {
        float l = sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        assert (l > 0);
        v.x /= l;
        v.y /= l;
        v.z /= l;
    }
}
