package imbacad.model.ocr;

/**
 * Wrapper class for Tesseract-OCR library.
 * @author Dirk Kretschmann
 *
 */
public class Tesseract {

	private Tesseract() {}
	
	/**
	 * Entry point for TessBaseAPI::Init
	 * @param filename
	 * @param language
	 */
	public static native void init(String filename, String language);
	
	/**
	 * Entry point for TessBaseAPI::GetUTF8Text
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 */
	public static native String getUTF8Text(int[] data, int width, int height);
	
	/**
	 * Entry point for TessBaseAPI::End
	 */
	public static native void end();

}
