package kjl.trianglofon;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
//import android.os.SystemClock;

public class MyGLRenderer implements GLSurfaceView.Renderer {
	
	float[] mProjMatrix = new float[16];
	float[] mMVPMatrix = new float[16];
	float[] mVMatrix = new float[16];
	float[] mRotationMatrixZ = new float[16];
	float[] mRotationMatrixX = new float[16];
	float[] entityMatrix = new float[]{1,0,0,0,
                                       0,1,0,0,
                                       0,0,1,0,
                                       0,0,0,1};
	
	public volatile static float angleZ;
	public volatile static float angleX;
	
	Triangle tri;
	Triangle sq1;
	Triangle sq2;
	
	float[] triangleCoords = { // in counterclockwise order:
            0.0f,  0.602008459f, 0.0f,   // top
           -0.5f, -0.311004243f, 0.0f,   // bottom left
            0.5f, -0.311004243f, 0.0f    // bottom right
       };
	float[] sq1Coords = { // in counterclockwise order:
           -1.0f,  1.0f, -1.0f,   // top L
           -1.0f, -1.0f, -1.0f,   // bottom L
            1.0f, -1.0f, -1.0f    // bottom R
       };
	float[] sq2Coords = { // in counterclockwise order:
            1.0f,  1.0f, -1.0f,   // top R
           -1.0f,  1.0f, -1.0f,   // top L
            1.0f, -1.0f, -1.0f    // bottom R
       };
	
	float[] triColor = new float[]{ 0.23671875f, 0.96953125f, 0.42265625f, 1.0f };
	float[] sqColor  = new float[]{ 1.0f, 0.2f, 0.7f, 1.0f};
	
    private static final String vertexShaderCode =
    	      "attribute vec4 vPosition;"
    	    + "uniform mat4 uMVPMatrix;"
    	    + "void main() {"
    	    + "  gl_Position = vPosition * uMVPMatrix;"
    	    + "}";

    private static final String fragmentShaderBackground =
              "precision mediump float;"
            + "uniform vec4 vColor;"
            + "uniform float resolution;"
    	    + "void main() {"
            + "  float PI = 3.1415;"
    	    // 225.0 hard coding is the shit!
            + "  float bar = 0.8 + (sign(sin(15.0*PI*(gl_FragCoord.x/225.0))) * 0.2);"
    	    + "  gl_FragColor = vec4(vColor.xyz * bar, 1.0);"
    	    + "}";
    private static final String fragmentShaderTrangle =
              "precision mediump float;"
            + "uniform vec4 vColor;"
            + "uniform float resolution;"
  	        + "void main() {"
  	        + "  gl_FragColor = vec4(vColor.xyz * (gl_FragCoord.z * 2.0 - 0.5), 1.0);"
  	        + "}";
	
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
        
        int shaderBG = createShaderProgram(vertexShaderCode, fragmentShaderBackground);
        int shaderTri = createShaderProgram(vertexShaderCode, fragmentShaderTrangle);
        
        tri = new Triangle(triangleCoords, shaderTri, triColor);
        sq1 = new Triangle(sq1Coords, shaderBG, sqColor);
        sq2 = new Triangle(sq2Coords, shaderBG, sqColor);
    }

    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        
        sq1.draw(entityMatrix);
        sq2.draw(entityMatrix);
        
        Matrix.setRotateM(mRotationMatrixZ, 0, angleZ, 0, 0, -1.0f);
        Matrix.setRotateM(mRotationMatrixX, 0, angleX, 0, -1.0f, 0);
        
        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrixX, 0, mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrixZ, 0, mMVPMatrix, 0);
        tri.draw(mMVPMatrix);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        
        float ratio = (float) width / height;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }
    
    /*************************************************************************/
    public static int createShaderProgram(String vertex, String fragment){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertex);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragment);

        int mProgram = GLES20.glCreateProgram();         // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);
        return mProgram;
    }
    
    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}