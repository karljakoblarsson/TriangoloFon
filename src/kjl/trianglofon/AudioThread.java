package kjl.trianglofon;

import as.adamsmith.etherealdialpad.dsp.*;

public class AudioThread extends Thread {
	
	WtOsc osc;
	Dac dac;
	ExpEnv env;
	Delay del;
	public boolean on;
	private boolean noteOff;
	private boolean noteOn;
	public float freq;
	public int release = 15;
	
	public AudioThread() {
        osc = new WtOsc();
        dac = new Dac();
        env = new ExpEnv();
        del = new Delay(4000);
        
        freq = 440.0f;
        
        osc.fillWithFiltredSaw();
        osc.setFreq(freq);
        env.setFactor(ExpEnv.hardFactor);
        
        osc.chuck(env).chuck(del).chuck(dac);

	}
	
	public void noteOn(){
		noteOn = true;
	}
	
	public void noteOff(){
		noteOff = true;
	}
	
	public void setMidiNote(float note){
		freq = (float) (Math.pow(2, (note - 69.0f)/12.0f) * 440.0f);
		osc.setFreq(freq);
	}
	
	public void run() {
		on = true;
		dac.open();
		while(on && !isInterrupted()){
			if (noteOn){
				env.setActive(true);
				noteOn = false;
			}
			
			if (noteOff) {
				env.setActive(false);
				noteOff = false;
				long startTime = System.currentTimeMillis();
				while(System.currentTimeMillis() < startTime + release) {
					dac.tick();
				}
				env.setActive(false);
			}
			dac.tick();
		} 
		dac.close();
	}

}
