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
import util.math.FastMath;
import util.math.Matrix4f;
import util.math.Vector3f;
import util.projection.Projection;

public class Main {

    // Creates a new cube
    //private final CubeGL graphicObject = new CubeGL();
    //Creates a new Piramide
    private final PiramideGL graphicObject = new PiramideGL();

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

    public enum Rotation {
        X, Y, Z
    }

    private Rotation currentRotation = Rotation.X;

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

    public void run() {
        // Creates the vertex array object. 
        // Must be performed before shaders compilation.
        graphicObject.fillVAOs();
        graphicObject.loadShaders();
        
        // Model Matrix setup
        scaleMatrix.m11 = 1.0f;
        scaleMatrix.m22 = 1.0f;
        scaleMatrix.m33 = 1.0f;

        // light setup
        graphicObject.setVector("lightPos"    , lightPos);
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

            // Projection and View Matrix Setup
            projMatrix.setTo(proj.perspective());
            viewMatrix.setTo(cam.viewMatrix());

            currentAngle += 0.01f;

            currentRotation = getRotationInput();
           
            modelMatrix = getModelMatrix();
            
            modelMatrix.multiply(scaleMatrix);
            graphicObject.setMatrix("modelmatrix", modelMatrix);
            graphicObject.setMatrix("viewmatrix" , viewMatrix);
            graphicObject.setMatrix("projection" , projMatrix);
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

    private Rotation getRotationInput() {
        if (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                int input = Keyboard.getEventKey();
                switch (input) {
                    case Keyboard.KEY_R:
                    case Keyboard.KEY_L:
                    case Keyboard.KEY_RIGHT:
                    case Keyboard.KEY_LEFT:
                        return Rotation.Y;
                        
                    case Keyboard.KEY_C:
                    case Keyboard.KEY_B:
                    case Keyboard.KEY_DOWN:
                    case Keyboard.KEY_UP:
                        return Rotation.X;
                }
            }
        }
        return currentRotation;
    }

    private Matrix4f getRotationMatrixX(float angle) {

        float c = FastMath.cos(currentAngle);
        float s = FastMath.sin(currentAngle);

        Matrix4f rotationMatrixX = new Matrix4f();

        //rotaçao em torno de x
        rotationMatrixX.m22 = c;
        rotationMatrixX.m32 = -s;
        rotationMatrixX.m23 = s;
        rotationMatrixX.m33 = c;

        return rotationMatrixX;
    }

    private Matrix4f getRotationMatrixY(float angle) {

        float c = FastMath.cos(currentAngle);
        float s = FastMath.sin(currentAngle);

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
                case X:
                    rotationMatrix = getRotationMatrixX(currentAngle);
                    break;
                case Y:
                     rotationMatrix = getRotationMatrixY(currentAngle);
                    break;
                default: rotationMatrix = getRotationMatrixX(currentAngle);
            }
        matrix.multiply(rotationMatrix);
        return matrix;
    }

    /**
     * main method to run the example
     *
     * @param args
     * @throws org.lwjgl.LWJGLException
     */
    public static void main(String[] args) throws LWJGLException {
        Main example = new Main();
        example.initGl();
        example.run();
    }
}
