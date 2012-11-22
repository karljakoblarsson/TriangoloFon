/*
package as.adamsmith.etherealdialpad.dsp;


init {
	WtOsc ugOscA1 = new WtOsc();
	WtOsc ugOscA2 = new WtOsc();
	ExpEnv ugEnvA = new ExpEnv();
	
	ugOscA1.fillWithHardSin(7.0f);
	ugOscA2.fillWithHardSin(2.0f);
	
	Dac ugDac = new Dac();

	Delay ugDelay = new Delay(UGen.SAMPLE_RATE/2);
	
	ugEnvA.chuck(ugDelay);
	ugDelay.chuck(ugDac);
	
	ugOscA1.chuck(ugEnvA);
	ugOscA2.chuck(ugEnvA);
	
	ugEnvA.setFactor(ExpEnv.hardFactor);
}				

touch {
	ugOscA1.setFreq(buildFrequency(scale, octaves, x));
	ugOscA2.setFreq(buildFrequency(scale, octaves, y));
}

run {
	ugDac.open();
	while(!isInterrupted()) {
		ugDac.tick();
	}
	ugDac.close();
}
*/