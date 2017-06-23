/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

import java.util.ArrayList;
import util.math.Vector3f;
import util.math.Vector4f;

/**
 *
 * @author moniq
 */
public class Surface {

    protected ArrayList<Vector4f> colors;
    protected ArrayList<Vector4f> positions;

    public Surface() {
        positions = new ArrayList<>();
        colors = new ArrayList<>();
    }

    public void addVertex(Vector3f p) {

        Vector4f p4f = new Vector4f(p, 1.0f);
        positions.add(p4f);

        // Fill the colors
        Vector4f black = new Vector4f(0.3f, 0.2f, 0.1f, 1.0f);
        Vector4f red = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
        Vector4f yellow = new Vector4f(1.0f, 1.0f, 0.0f, 1.0f);
        Vector4f green = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);
        Vector4f blue = new Vector4f(0.5f, 0.0f, 1.0f, 1.0f);

        colors.add(blue);
    }

    public boolean isIn(float x, float y, float z) {
        float r = 0.5f;
        if (x * x + y * y + z * z < r * r) {
            return true;
        }
        return false;
    }
}
