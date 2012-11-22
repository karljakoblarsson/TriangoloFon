package kjl.trianglofon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;

/*************************************************************************/

class Triangle {

    private FloatBuffer vertexBuffer;
    public int shaderProgram;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    float color[] = { 0.23671875f, 0.96953125f, 0.42265625f, 1.0f };
    
    public void draw(float[] MVPMatrix) {
        GLES20.glUseProgram(shaderProgram);

        int mPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        int vertexStride = 12;
		// Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride , vertexBuffer);

        int mColorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        int mMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MVPMatrix, 0);
        
        int vertexCount = 3;
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount );
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
    
    public Triangle(float[] triangleCoords, int shaderProgram, float[] color) {
    	this.shaderProgram = shaderProgram;
    	this.color = color;
    	
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
    }
}