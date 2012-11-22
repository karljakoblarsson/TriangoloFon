package kjl;

public final class Music {

	public Music() {
	}
	
	public static float toScale(int in, int root, int[] scale){
		in += root;
		int octave = in / scale.length;
		int degree = in % scale.length;
		
		return octave * 12 + scale[degree];
	}
	
	
	/*
	 * Stub function. and not really needed anyways.
	 */
	public static int toNoteNr(String note){
		if(!note.matches("[CDEFGAB][#b]?")){
			return -1;
		} else {
			return 0;
		}
	}
	
	public static void main(String[] arg){
		System.out.println("");
	}
}
