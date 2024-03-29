/*
 * Modified by Jakob Larsson
 * November 2012
 */
package as.adamsmith.etherealdialpad.dsp;

import android.util.FloatMath;

public class WtOsc extends UGen {
	public static final int BITS = 8;
	public static final int ENTRIES = 1<<(BITS-1);
	public static final int MASK = ENTRIES-1;
	
	private float phase;
	private float cyclesPerSample;
	
	final float[] table;
	
	public WtOsc () {
		table = new float[ENTRIES];
	}
	
	public synchronized void setFreq(float freq) {
		cyclesPerSample = freq/SAMPLE_RATE;
	} 
	
	public synchronized boolean render(final float[] buffer) { // assume t is in 0.0 to 1.0
		for(int i = 0; i < CHUNK_SIZE; i++) {
			float scaled = phase*ENTRIES;
			final float fraction = scaled-(int)scaled;
			final int index = (int)scaled;
			buffer[i] += (1.0f-fraction)*table[index&MASK]+fraction*table[(index+1)&MASK];
			phase = (phase+cyclesPerSample) - (int)phase; 
		}
		
		return true;
	}
	
	public WtOsc fillWithSin() {
		final float dt = (float)(2.0*Math.PI/ENTRIES);
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = FloatMath.sin(i*dt);
		}
		return this;
	}
	
	public WtOsc fillWithHardSin(final float exp) {
		final float dt = (float)(2.0*Math.PI/ENTRIES);
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = (float) Math.pow(FloatMath.sin(i*dt),exp);
		}
		return this;
	}
	
	public WtOsc fillWithZero() {
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = 0;
		}
		return this;
	}
	
	public WtOsc fillWithSqr() {
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = i<ENTRIES/2?1f:-1f;
		}
		return this;
	}
	
	public WtOsc fillWithSqrDuty(float fraction) {
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = (float)i/ENTRIES<fraction?1f:-1f;
		}
		return this;
	}
	
	public WtOsc fillWithSaw() {
		float dt = (float)(2.0/ENTRIES);
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = 1.0f-i*dt;
		}
		return this;
	}
	
	/*
	 * Modified by Jakob Larsson
	 */
	public WtOsc fillWithFiltredSaw() {
		float dt = (float)(2.0/ENTRIES);
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = (1.0f-i*dt) * (-0.000195f*i*i + 0.025f*i + 0.2f);
		}
		return this;
	}
}