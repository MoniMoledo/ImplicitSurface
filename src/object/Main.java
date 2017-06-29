package object;

/**
 *
 * @author marcoslage
 */
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Keyboard;
import util.camera.Camera;
import util.marchingtetrahedra.MarchingTetrahedra;
import util.math.FastMath;
import util.math.Matrix4f;
import util.math.Vector3f;
import util.projection.Projection;

public class Main {

    //sphere = (1, 1, 1, 2, 2, 2, 0.5f);
    //cone = 1, 1, -1, 2, 2, 2, 0.0f
    //paraboloide = 1, 1, -1, 2, 2, 1, 0.0f
    //cilinder = 1, 1, 0, 2, 2, 1, 0.5f
    //sela = 1, -1, -1, 2, 2, 1, 0.0f
    private final SurfaceGL graphicObject = new SurfaceGL(1, 1, 1, 2, 2, 2, 0.5f);

    // Animation:
    private float currentAngle = 0.0f;

    // Projection Matrix
    private final Projection proj = new Projection(45, 1.3333f, 0.0f, 100f);

    // View Matrix
    private final Vector3f eye = new Vector3f(0.0f, 0.0f, 2.0f);
    private final Vector3f at = new Vector3f(0.0f, 0.0f, 0.0f);
    private final Vector3f up = new Vector3f(0.0f, 1.0f, 2.0f);

    // Camera
    private final Camera cam = new Camera(eye, at, up);

    // Light
    private final Vector3f lightPos = new Vector3f(0.0f, 2.0f, -2.0f);
    private final Vector3f ambientColor = new Vector3f(1.0f, 1.0f, 1.0f);
    private final Vector3f diffuseColor = new Vector3f(1.0f, 1.0f, 1.0f);
    private final Vector3f speclarColor = new Vector3f(1.0f, 1.0f, 1.0f);

    private final float kA = 0.4f;
    private final float kD = 0.5f;
    private final float kS = 0.1f;
    private final float sN = 60.0f;

    // Model Matrix:
    private final Matrix4f scaleMatrix = new Matrix4f();
    // Final Matrix
    private Matrix4f modelMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f projMatrix = new Matrix4f();

    public enum RotationType {
        LEFT, RIGHT, UP, DOWN
    }

    public enum ProjectionType {
        O, P
    }

    private RotationType currentRotation = RotationType.RIGHT;
    private ProjectionType currentProjection = ProjectionType.P;

    /**
     * General initialization stuff for OpenGL
     *
     * @throws org.lwjgl.LWJGLException
     */
    public void initGl() throws LWJGLException {

        // width and height of window and view port
        int width = 640;
        int height = 480;

        // set up window and display
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.setVSyncEnabled(true);
        Display.setTitle("Shader OpenGL Hello");

        // set up OpenGL to run in forward-compatible mode
        // so that using deprecated functionality will
        // throw an error.
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
        Display.create(pixelFormat, contextAtrributes);

        // Standard OpenGL Version
        System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
        System.out.println("GLSL version: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));

        // initialize basic OpenGL stuff
        GL11.glViewport(0, 0, width, height);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void run() throws Exception {
        // Creates the vertex array object. 
        // Must be performed before shaders compilation.
        MarchingTetrahedra.generateTetrahedron(graphicObject, 100);
        graphicObject.fillVAOs();
        graphicObject.loadShaders();

        // Model Matrix setup
        scaleMatrix.m11 = 0.5f;
        scaleMatrix.m22 = 0.5f;
        scaleMatrix.m33 = 0.5f;
        // light setup
        graphicObject.setVector("lightPos", lightPos);
        graphicObject.setVector("ambientColor", ambientColor);
        graphicObject.setVector("diffuseColor", diffuseColor);
        graphicObject.setVector("speclarColor", speclarColor);

        graphicObject.setFloat("kA", kA);
        graphicObject.setFloat("kD", kD);
        graphicObject.setFloat("kS", kS);
        graphicObject.setFloat("sN", sN);

        while (Display.isCloseRequested() == false) {

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glCullFace(GL11.GL_BACK);

            currentAngle += 0.01f;

            setCurrentRotationAndProjectionFromInput();

            // Projection and View Matrix Setup
            projMatrix.setTo(getProjectionMatrix());
            viewMatrix.setTo(cam.viewMatrix());

            modelMatrix = getModelMatrix();

            modelMatrix.multiply(scaleMatrix);
            graphicObject.setMatrix("modelmatrix", modelMatrix);
            graphicObject.setMatrix("viewmatrix", viewMatrix);
            graphicObject.setMatrix("projection", projMatrix);
            graphicObject.render();

            // check for errors
            if (GL11.GL_NO_ERROR != GL11.glGetError()) {
                throw new RuntimeException("OpenGL error: " + GLU.gluErrorString(GL11.glGetError()));
            }

            // swap buffers and sync frame rate to 60 fps
            Display.update();
            Display.sync(60);

        }

        Display.destroy();
    }

    private void setCurrentRotationAndProjectionFromInput() {
        if (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                int input = Keyboard.getEventKey();
                switch (input) {
                    case Keyboard.KEY_LEFT:
                    case Keyboard.KEY_L:
                        currentRotation = RotationType.LEFT;
                        break;
                    case Keyboard.KEY_R:
                    case Keyboard.KEY_RIGHT:
                        currentRotation = RotationType.RIGHT;
                        break;

                    case Keyboard.KEY_C:
                    case Keyboard.KEY_UP:
                        currentRotation = RotationType.UP;
                        break;
                    case Keyboard.KEY_B:
                    case Keyboard.KEY_DOWN:
                        currentRotation = RotationType.DOWN;
                        break;
                    case Keyboard.KEY_O:
                        currentProjection = ProjectionType.O;
                        break;
                    case Keyboard.KEY_P:
                        currentProjection = ProjectionType.P;
                        break;
                }
            }
        }
    }

    private Matrix4f getRotationMatrixX(float angle) {

        float c = FastMath.cos(angle);
        float s = FastMath.sin(angle);

        Matrix4f rotationMatrixX = new Matrix4f();

        //rotaçao em torno de x
        rotationMatrixX.m22 = c;
        rotationMatrixX.m32 = -s;
        rotationMatrixX.m23 = s;
        rotationMatrixX.m33 = c;

        return rotationMatrixX;
    }

    private Matrix4f getRotationMatrixY(float angle) {

        float c = FastMath.cos(angle);
        float s = FastMath.sin(angle);

        Matrix4f rotationMatrixY = new Matrix4f();

        //rotaçao em torno de y
        rotationMatrixY.m11 = c;
        rotationMatrixY.m31 = s;
        rotationMatrixY.m13 = -s;
        rotationMatrixY.m33 = c;

        return rotationMatrixY;
    }

    private Matrix4f getModelMatrix() {
        Matrix4f matrix = new Matrix4f();
        matrix.setToIdentity();
        Matrix4f rotationMatrix;

        switch (currentRotation) {
            case UP:
                rotationMatrix = getRotationMatrixX(currentAngle);
                break;
            case DOWN:
                rotationMatrix = getRotationMatrixX(-currentAngle);
                break;
            case LEFT:
                rotationMatrix = getRotationMatrixY(-currentAngle);
                break;
            case RIGHT:
                rotationMatrix = getRotationMatrixY(currentAngle);
                break;
            default:
                rotationMatrix = getRotationMatrixX(currentAngle);
        }
        matrix.multiply(rotationMatrix);
        return matrix;
    }

    private Matrix4f getProjectionMatrix() {

        switch (currentProjection) {
            case O:
                return proj.orthogonal();
            case P:
                return proj.perspective();
        }
        return proj.perspective();
    }

    /**
     * main method to run the example
     *
     * @param args
     * @throws org.lwjgl.LWJGLException
     */
    public static void main(String[] args) throws LWJGLException, Exception {
        Main example = new Main();
        example.initGl();
        example.run();
    }
}
