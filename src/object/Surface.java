/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

import java.util.ArrayList;
import java.util.Random;
import static util.math.FastMath.sin;
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
    double a = 1;
    double b = 1;
    double c = 1;
    double xPo = 2;
    double yPo = 2;
    double zPo = 2;
    public float isolevel = 0.5f;
    private static Random rand = new Random();

    public Surface(double a, double b, double c, double xPo, double yPo, double zPo, float isolevel) {
        this.a = a;
        this.b = b;
        this.c = c;

        this.xPo = xPo;
        this.yPo = yPo;
        this.zPo = zPo;
        this.isolevel = isolevel;
        positions = new ArrayList<>();
        colors = new ArrayList<>();
        normal = new ArrayList<>();
    }
//

    public void addVertex(Vector3f p) {

        Vector4f p4f = new Vector4f(p, 1.0f);
        positions.add(p4f);
        Vector4f newNormal = new Vector4f(gradientAt(p.x, p.y, p.z), 1.0f);
        normal.add(newNormal);
        Vector4f color;
        double randVal = rand.nextGaussian();
        if (Math.abs(randVal) < 1) {
            color = new Vector4f(0.0f, 1.0f, 1.0f, 1.0f); //green
        } else if (Math.abs(randVal) < 2) {
            color = new Vector4f(1.0f, 1.0f, 0.0f, 1.0f); //yellow
        } else if (Math.abs(randVal) < 3) {
            color = new Vector4f(1.0f, 0.5f, 0.0f, 1.0f); //orange
        } else {
            color = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f); //red
        }
        colors.add(color);

    }

    Vector3f gradientAt(float x, float y, float z) {
        float epsilon = 0.0001f;

        float dx = valueAt(x + epsilon, y, z) - valueAt(x - epsilon, y, z);
        float dy = valueAt(x, y + epsilon, z) - valueAt(x, y - epsilon, z);
        float dz = valueAt(x, y, z + epsilon) - valueAt(x, y, z - epsilon);

        Vector3f result = new Vector3f(dx, dy, dz);

        normalize(result);

        return result;
    }

    public float valueAt(float x, float y, float z) {

        return (float) (((Math.pow((double) x, xPo)) * a)
                + ((Math.pow((double) y, yPo)) * b)
                + ((Math.pow((double) z, zPo)) * c));
    }

    public boolean isIn(float x, float y, float z, float isolevel) {

        float value = valueAt(x, y, z);
        if (value <= isolevel) {
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
