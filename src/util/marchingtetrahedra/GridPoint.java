/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.marchingtetrahedra;

import util.math.Vector3f;

/**
 *
 * @author moniq
 */
public class GridPoint {

    public Vector3f vertex;
    public boolean isIn;

    public GridPoint(float i, float j, float k, boolean value) {
        vertex = new Vector3f(i, j, k);
        isIn = value;
    }
}
