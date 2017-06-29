/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.marchingtetrahedra;

import object.Surface;
import java.util.HashMap;
import java.util.Map;
import object.SurfaceGL;
import util.math.Vector3f;

/**
 *
 * @author moniq
 */
public class MarchingTetrahedra {

    public static void generateTetrahedron(Surface surface,
            float xMin, float xMax,
            float yMin, float yMax,
            float zMin, float zMax,
            int resolution) throws Exception { // resolution indicates # of cubes

        float r = 2.0f / resolution;
        System.out.println(r);

        Vector3f v0 = new Vector3f(-1.0f, -1.0f, -1.0f);

        // Cube marching = new Cube();
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
                        new GridPoint(v.x,  v.y,  v.z, surface.isIn(v.x,  v.y,  v.z)),
                        new GridPoint(v1.x, v1.y, v1.z,surface.isIn(v1.x,  v1.y,  v1.z)),
                        new GridPoint(v2.x, v2.y, v2.z, surface.isIn(v2.x,  v2.y,  v2.z)),
                        new GridPoint(v3.x, v3.y, v3.z, surface.isIn(v3.x,  v3.y,  v3.z)),
                        new GridPoint(v4.x, v4.y, v4.z, surface.isIn(v4.x,  v4.y,  v4.z)),
                        new GridPoint(v5.x, v5.y, v5.z, surface.isIn(v5.x,  v5.y,  v5.z)),
                        new GridPoint(v6.x, v6.y, v6.z, surface.isIn(v6.x,  v6.y,  v6.z)),
                        new GridPoint(v7.x, v7.y, v7.z, surface.isIn(v7.x,  v7.y,  v7.z))
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
                        drawTetrahedra(tetrahedron[t], surface);
                    }
                }

            }
        }
    }

    private static void drawTetrahedra(GridPoint[] tetrahedra, Surface surface) throws Exception {

        /*
     Tetrahedron layout:
           0
           *
          /|
         / |
      3 *-----* 1
         \ | /
          \|/
           *
           2
         */
//        StringBuilder pointInside = new StringBuilder(4);
//        for (int i = 0; i < 4; ++i) {
//            if (tetrahedra[i].isIn) {
//                pointInside.append(i);
//            }
//        }
        char index = 0;
        for (int i = 0; i < 4; ++i) {
            if (tetrahedra[i].isIn) {
                index |= (1 << i);
            }
        }

        switch (index) {

            // we don't do anything if everyone is inside or outside
//            case "":
//            case "0123":
            case 0x00:
            case 0x0F:
                break;

            // only vert 0 is inside
            //case "0":
            case 0x01:
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[1]));
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[3]));
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[2]));
                break;

            // only vert 1 is inside
            //case "1":
            // only vert 1 is inside
            case 0x02:
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[2]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[3]));
                break;

            // only vert 2 is inside
            //case "2":
            // only vert 2 is inside
            case 0x04:
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[3]));
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[1]));
                break;

            // only vert 3 is inside
            //case "3":
            // only vert 3 is inside
            case 0x08:
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[1]));
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[2]));
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[0]));
                break;

            // verts 0, 1 are inside
            //case "01":
            // verts 0, 1 are inside
            case 0x03:

                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[3]));

                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[1]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[3]));
                break;

            // verts 0, 2 are inside
            //case "02":
            // verts 0, 2 are inside
            case 0x05:
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[2]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[0]));

                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[2]));
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[3]));
                break;

            // verts 0, 3 are inside
            //case "03":
            // verts 0, 3 are inside
            case 0x09:
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[1]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[3]));
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[2]));

                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[3]));
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[2]));
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[2]));
                break;

            // verts 1, 2 are inside
            //case "12":
            // verts 1, 2 are inside
            case 0x06:
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[1]));
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[2]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[3]));

                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[3]));
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[2]));
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[2]));
                break;

            // verts 2, 3 are inside
            //case "23":
            // verts 2, 3 are inside
            case 0x0C:
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[3]));
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[0]));

                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[3]));
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[1]));
                break;

            // verts 1, 3 are inside
            //case "13":
            // verts 1, 3 are inside
            case 0x0A:
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[2]));

                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[2]));
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[3]));
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[0]));
                break;

            // verts 0, 1, 2 are inside
            //case "012":
            // verts 0, 1, 2 are inside
            case 0x07:
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[2]));
                surface.addVertex(drawVert(surface, tetrahedra[3], tetrahedra[1]));
                break;

            // verts 0, 1, 3 are inside
            //case "013":
            // verts 0, 1, 3 are inside
            case 0x0B:
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[1]));
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[3]));
                surface.addVertex(drawVert(surface, tetrahedra[2], tetrahedra[0]));
                break;

            // verts 0, 2, 3 are inside
            //case "023":
            // verts 0, 2, 3 are inside
            case 0x0D:

                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[0]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[3]));
                surface.addVertex(drawVert(surface, tetrahedra[1], tetrahedra[2]));
                break;

            // verts 1, 2, 3 are inside
            //case "123":
            // verts 1, 2, 3 are inside
            case 0x0E:

                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[1]));
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[2]));
                surface.addVertex(drawVert(surface, tetrahedra[0], tetrahedra[3]));
                break;

            default:
                throw new Exception("Wrong indexes");
        }

    }

//    private static Vector3f getMidPoint(surface, GridPoint p1, GridPoint p2) {
//
//        float x, y, z;
//
//        x = (p1.vertex.x + p2.vertex.x) / 2.0f;
//        y = (p1.vertex.y + p2.vertex.y) / 2.0f;
//        z = (p1.vertex.z + p2.vertex.z) / 2.0f;
//
//        return new Vector3f(x, y, z);
//    }
    private static Vector3f drawVert(Surface surface, GridPoint p1, GridPoint p2) {
        float v1 = surface.valueAt(p1.vertex.x, p1.vertex.y, p1.vertex.z);
        float v2 = surface.valueAt(p2.vertex.x, p2.vertex.y, p2.vertex.z);

        float isolevel = 0.01f;
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
