/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.marchingtetrahedra;

import object.Surface;
import util.math.Vector3f;

/**
 *
 * @author moniq Code based on https://github.com/Calvin-L/MarchingTetrahedrons
 */
public class MarchingTetrahedra {

    public static void generateTetrahedron(Surface surface, int resolution) throws Exception { // resolution indicates # of cubes

        float r = 2.0f / resolution;
        float isolevel = surface.isolevel;

        Vector3f v0 = new Vector3f(-1.0f, -1.0f, -1.0f);

        for (int i = 0; i < resolution; i++) {
            float varX = i * r;
            for (int j = 0; j < resolution; j++) {
                float varY = j * r;
                for (int k = 0; k < resolution; k++) {
                    float varZ = k * r;

                    Vector3f v = new Vector3f(v0.x + varX, v0.y + varY, v0.z + varZ);
                    Vector3f v1 = new Vector3f(v.x, v.y, v.z + r);
                    Vector3f v2 = new Vector3f(v.x + r, v.y, v.z + r);
                    Vector3f v3 = new Vector3f(v.x + r, v.y, v.z);
                    Vector3f v4 = new Vector3f(v.x, v.y + r, v.z);
                    Vector3f v5 = new Vector3f(v.x, v.y + r, v.z + r);
                    Vector3f v6 = new Vector3f(v.x + r, v.y + r, v.z + r);
                    Vector3f v7 = new Vector3f(v.x + r, v.y + r, v.z);

                    GridPoint[] cube = new GridPoint[]{
                        new GridPoint(v.x, v.y, v.z, surface.isIn(v.x, v.y, v.z, isolevel)),
                        new GridPoint(v1.x, v1.y, v1.z, surface.isIn(v1.x, v1.y, v1.z, isolevel)),
                        new GridPoint(v2.x, v2.y, v2.z, surface.isIn(v2.x, v2.y, v2.z, isolevel)),
                        new GridPoint(v3.x, v3.y, v3.z, surface.isIn(v3.x, v3.y, v3.z, isolevel)),
                        new GridPoint(v4.x, v4.y, v4.z, surface.isIn(v4.x, v4.y, v4.z, isolevel)),
                        new GridPoint(v5.x, v5.y, v5.z, surface.isIn(v5.x, v5.y, v5.z, isolevel)),
                        new GridPoint(v6.x, v6.y, v6.z, surface.isIn(v6.x, v6.y, v6.z, isolevel)),
                        new GridPoint(v7.x, v7.y, v7.z, surface.isIn(v7.x, v7.y, v7.z, isolevel))
                    };

                    GridPoint[][] tetrahedron = new GridPoint[][]{
                        {cube[0], cube[7], cube[3], cube[2]},
                        {cube[0], cube[7], cube[2], cube[6]},
                        {cube[0], cube[4], cube[7], cube[6]},
                        {cube[0], cube[1], cube[6], cube[2]},
                        {cube[0], cube[4], cube[6], cube[1]},
                        {cube[5], cube[1], cube[6], cube[4]}
                    };
                    for (int t = 0; t < 6; t++) {
                        drawTetrahedra(tetrahedron[t], surface, isolevel);
                    }
                }

            }
        }
    }

    private static void drawTetrahedra(GridPoint[] tetrahedra, Surface surface, float isolevel) throws Exception {

        StringBuilder pointInside = new StringBuilder(4);
        for (int i = 0; i < 4; ++i) {
            if (tetrahedra[i].isIn) {
                pointInside.append(i);
            }
        }

        switch (pointInside.toString()) {

            // we don't do anything if everyone is inside or outside
            case "":
            case "0123":
                break;

            // only vert 0 is inside
            case "0":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[1], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[3], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[2], isolevel));
                break;

            // only vert 1 is inside
            case "1":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[2], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[3], isolevel));
                break;

            // only vert 2 is inside
            case "2":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[3], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[1], isolevel));
                break;

            // only vert 3 is inside
            case "3":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[1], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[2], isolevel));

                break;

            // verts 0, 1 are inside
            case "01":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[3], isolevel));

                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[1], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[3], isolevel));
                break;

            // verts 0, 2 are inside
            case "02":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[2], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[0], isolevel));

                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[2], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[3], isolevel));
                break;

            // verts 0, 3 are inside
            case "03":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[1], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[3], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[2], isolevel));

                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[3], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[2], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[2], isolevel));
                break;

            // verts 1, 2 are inside
            case "12":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[1], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[2], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[3], isolevel));

                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[3], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[2], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[2], isolevel));
                break;

            // verts 2, 3 are inside
            case "23":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[3], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[0], isolevel));

                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[3], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[1], isolevel));
                break;

            // verts 1, 3 are inside
            case "13":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[2], isolevel));

                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[2], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[3], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[0], isolevel));
                break;

            // verts 0, 1, 2 are inside
            case "012":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[2], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[3], tetrahedra[1], isolevel));
                break;

            // verts 0, 1, 3 are inside
            case "013":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[1], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[3], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[2], tetrahedra[0], isolevel));
                break;

            // verts 0, 2, 3 are inside
            case "023":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[0], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[3], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[1], tetrahedra[2], isolevel));
                break;

            // verts 1, 2, 3 are inside
            case "123":
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[1], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[2], isolevel));
                surface.addVertex(getInterpolatedVertice(surface, tetrahedra[0], tetrahedra[3], isolevel));
                break;

            // what is this I don't even
            default:
                throw new Exception("Wrong indexes");
        }

    }

    private static Vector3f getInterpolatedVertice(Surface surface, GridPoint p1, GridPoint p2, float isolevel) {
        float v1 = surface.valueAt(p1.vertex.x, p1.vertex.y, p1.vertex.z);
        float v2 = surface.valueAt(p2.vertex.x, p2.vertex.y, p2.vertex.z);

        float x, y, z;

        if (v2 == v1) {
            x = (p1.vertex.x + p2.vertex.x) / 2.0f;
            y = (p1.vertex.y + p2.vertex.y) / 2.0f;
            z = (p1.vertex.z + p2.vertex.z) / 2.0f;
        } else {
            float interp = (isolevel - v1) / (v2 - v1);
            float oneMinusInterp = 1 - interp;

            x = p1.vertex.x * oneMinusInterp + p2.vertex.x * interp;
            y = p1.vertex.y * oneMinusInterp + p2.vertex.y * interp;
            z = p1.vertex.z * oneMinusInterp + p2.vertex.z * interp;
        }
        return new Vector3f(x, y, z);
    }
}
