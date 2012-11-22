package kjl.trianglofon;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import kjl.Music;

/***************************/
class MyGLSurfaceView extends GLSurfaceView {

	public AudioThread audioThread;

	float previousX;
	float previousY;
	
	public int[] minorPent;
	public int[] major = {0,2,4,5,7,9,11};
	public int[] minor = {0,2,3,5,7,8,10};
	public int[] chromatic = {1,2,3,4,5,6,7,8,9,10,11};
	public int[] furElise = {0,2,3,5,6,7};
	public int[] furElise2 = {0,4,8,9,11,12,14,15,16};

	public MyGLSurfaceView(Context context){
		super(context);
		setEGLContextClientVersion(2);

		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(new MyGLRenderer());
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		audioThread = new AudioThread();
		audioThread.setPriority(Thread.MAX_PRIORITY);
		audioThread.start();
		
		minorPent = new int[]{0,3,5,7,10};
	}

	public boolean onTouchEvent(MotionEvent e){
		// MotionEvent reports input details from the touch screen
		// and other input controls. In this case, you are only
		// interested in events where the touch position changed.

		float x = e.getX();
		float y = e.getY();
		
		float note = Music.toScale((int) (x/15), 12+5, minorPent);

		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:

			float dx = x - previousX;
			float dy = y - previousY;

			// reverse direction of rotation above the mid-line
			if (y > getHeight() / 2) {
				dx = dx * -1 ;
			}

			// reverse direction of rotation to left of the mid-line
			if (x < getWidth() / 2) {
				dy = dy * -1 ;
			}

			MyGLRenderer.angleZ += dx;
			MyGLRenderer.angleX += dy;
			requestRender();
			
			audioThread.setMidiNote(note);
			break;
		case MotionEvent.ACTION_UP:
			audioThread.noteOff();
		case MotionEvent.ACTION_DOWN:
			audioThread.setMidiNote(note);
			audioThread.noteOn();
			break;
		}

		previousX = x;
		previousY = y;
		return true;
	}

	public void onStop(){
		audioThread.stop();
	}
}
