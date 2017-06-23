/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.marchingtetrahedra;

import object.Surface;
import java.util.HashMap;
import java.util.Map;
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

        int pointsPerEdge = resolution + 1; // indicates the # of points per side

        float xrange = xMax - xMin;
        float yrange = yMax - yMin;
        float zrange = zMax - zMin;

        Map grid = new HashMap<>();
        for (int i = 0; i <= resolution; ++i) {
            float x = (float) i / resolution * xrange + xMin;
            for (int j = 0; j <= resolution; ++j) {
                float y = (float) j / resolution * yrange + yMin;
                for (int k = 0; k <= resolution; ++k) {
                    float z = (float) k / resolution * zrange + zMin;
                    boolean value = surface.isIn(x, y, z);
                    StringBuilder str = new StringBuilder(0);
                    str.append(i);
                    str.append(j);
                    str.append(k);
                    grid.put(str.toString(), value);
                }
            }
        }
        for (int i = 0; i < resolution; ++i) {
            float x1 = (float) i / resolution * xrange + xMin;
            float x2 = (float) (i + 1) / resolution * xrange + xMin;
            for (int j = 0; j < resolution; ++j) {
                float y1 = (float) j / resolution * yrange + yMin;
                float y2 = (float) (j + 1) / resolution * yrange + yMin;
                for (int k = 0; k < resolution; ++k) {
                    float z1 = (float) k / resolution * zrange + zMin;
                    float z2 = (float) (k + 1) / resolution * zrange + zMin;

                    /*
                 Coordinates:
                      z
                      |
                      |___ y
                      /
                     /
                    x
                 Cube layout:
                    4-------7
                   /|      /|
                  / |     / |
                 5-------6  |
                 |  0----|--3
                 | /     | /
                 |/      |/
                 1-------2
                 Tetrahedrons are:
                     0, 7, 3, 2
                     0, 7, 2, 6
                     0, 4, 6, 7
                     0, 6, 1, 2
                     0, 6, 1, 4
                     5, 6, 1, 4
                     */
                    StringBuilder str = new StringBuilder(0);
                    str.append(i);
                    str.append(j);
                    str.append(k);
                    String key1 = str.toString();

                    str = new StringBuilder(0);
                    str.append(i + 1);
                    str.append(j);
                    str.append(k);
                    String key2 = str.toString();

                    str = new StringBuilder(0);
                    str.append(i + 1);
                    str.append(j + 1);
                    str.append(k);
                    String key3 = str.toString();

                    str = new StringBuilder(0);
                    str.append(i);
                    str.append(j + 1);
                    str.append(k);
                    String key4 = str.toString();

                    str = new StringBuilder(0);
                    str.append(i);
                    str.append(j);
                    str.append(k + 1);
                    String key5 = str.toString();

                    str = new StringBuilder(0);
                    str.append(i + 1);
                    str.append(j);
                    str.append(k + 1);
                    String key6 = str.toString();

                    str = new StringBuilder(0);
                    str.append(i + 1);
                    str.append(j + 1);
                    str.append(k + 1);
                    String key7 = str.toString();

                    str = new StringBuilder(0);
                    str.append(i);
                    str.append(j + 1);
                    str.append(k + 1);
                    String key8 = str.toString();

                    GridPoint[] cube = new GridPoint[]{
                        new GridPoint(x1, y1, z1, (boolean) grid.get(key1)),
                        new GridPoint(x2, y1, z1, (boolean) grid.get(key2)),
                        new GridPoint(x2, y2, z1, (boolean) grid.get(key3)),
                        new GridPoint(x1, y2, z1, (boolean) grid.get(key4)),
                        new GridPoint(x1, y1, z2, (boolean) grid.get(key5)),
                        new GridPoint(x2, y1, z2, (boolean) grid.get(key6)),
                        new GridPoint(x2, y2, z2, (boolean) grid.get(key7)),
                        new GridPoint(x1, y2, z2, (boolean) grid.get(key8))
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
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[1]));
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[3]));
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[2]));
                break;

            // only vert 1 is inside
            case "1":
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[2]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[3]));
                break;

            // only vert 2 is inside
            case "2":
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[3]));
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[1]));
                break;

            // only vert 3 is inside
            case "3":
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[1]));
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[2]));

                break;

            // verts 0, 1 are inside
            case "01":
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[3]));

                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[1]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[3]));
                break;

            // verts 0, 2 are inside
            case "02":
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[2]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[0]));

                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[2]));
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[3]));
                break;

            // verts 0, 3 are inside
            case "03":
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[1]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[3]));
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[2]));

                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[3]));
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[2]));
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[2]));
                break;

            // verts 1, 2 are inside
            case "12":
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[1]));
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[2]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[3]));

                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[3]));
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[2]));
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[2]));
                break;

            // verts 2, 3 are inside
            case "23":
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[3]));
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[0]));

                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[3]));
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[1]));
                break;

            // verts 1, 3 are inside
            case "13":
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[2]));

                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[2]));
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[3]));
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[0]));
                break;

            // verts 0, 1, 2 are inside
            case "012":
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[2]));
                surface.addVertex(getMidVertex(tetrahedra[3], tetrahedra[1]));
                break;

            // verts 0, 1, 3 are inside
            case "013":
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[1]));
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[3]));
                surface.addVertex(getMidVertex(tetrahedra[2], tetrahedra[0]));
                break;

            // verts 0, 2, 3 are inside
            case "023":
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[0]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[3]));
                surface.addVertex(getMidVertex(tetrahedra[1], tetrahedra[2]));
                break;

            // verts 1, 2, 3 are inside
            case "123":
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[1]));
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[2]));
                surface.addVertex(getMidVertex(tetrahedra[0], tetrahedra[3]));
                break;

            // what is this I don't even
            default:
                throw new Exception("Wrong indexes");
        }

    }

    private static Vector3f getMidVertex(GridPoint p1, GridPoint p2) {

        float x, y, z;

        x = (p1.vertex.x + p2.vertex.x) / 2.0f;
        y = (p1.vertex.y + p2.vertex.y) / 2.0f;
        z = (p1.vertex.z + p2.vertex.z) / 2.0f;

        return new Vector3f(x, y, z);
    }
}
