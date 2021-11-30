package ph.frobbly.Lazyreader;

import java.io.File;

import net.sourceforge.tess4j.*;

public class TesseractOCR {
	
	ITesseract tesseract;
	Language lang = Language.OSD;
	static String text;
	
	public enum Language {
		OSD,
		KOREAN,
		JAPANESE,
		CHINESE_S,
		CHINESE_T
	}
	
	TesseractOCR() {
		this.tesseract = new Tesseract();
		tesseract.setDatapath("classes/tessdata");
	}
	
	public void setSourceLanguage(Language lang) {
		switch(lang) {
			case OSD:
				tesseract.setLanguage("osd");
				break;
			case KOREAN:
				tesseract.setLanguage("kor");
				break;
			case JAPANESE:
				tesseract.setLanguage("jpn");
				break;
			case CHINESE_S:
				tesseract.setLanguage("chi_sim");
				break;
			case CHINESE_T:
				tesseract.setLanguage("chi_tra");
				break;
		}
	}
	
	public String readImage(Language lang) {
		setSourceLanguage(lang);
		try {
			text = tesseract.doOCR(new File("classes/captured.png"));
		} catch(TesseractException e) {
			e.printStackTrace();
		}
		return text;
	}

}
