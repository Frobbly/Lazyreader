package ph.frobbly.Lazyreader;

import java.awt.Desktop;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.net.URLEncoder;
import java.net.URISyntaxException;

import ph.frobbly.Lazyreader.TesseractOCR.Language;

public class Translator {
	
	final String baseUrl = "https://papago.naver.com/";
	static String sourceLanguage = "?sk=en";
	static String translationLanguage = "&tk=en&st=";
	
	public void setSourceLanguage(Language lang) {
		switch(lang) {
			case OSD:
				sourceLanguage = "?sk=en";
				break;
			case KOREAN:
				sourceLanguage = "?sk=ko";
				break;
			case JAPANESE:
				sourceLanguage = "?sk=ja";
				break;
			case CHINESE_S:
				sourceLanguage = "?sk=zh-CN";
				break;
			case CHINESE_T:
				sourceLanguage = "?sk=zh-TW";
				break;
		}
	}
	
	public void translateText(String text, Language lang) {
		setSourceLanguage(lang);
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI(baseUrl + sourceLanguage + translationLanguage + text));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public String encodeText(String text) {
        try {
            text = URLEncoder.encode(text, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
            return text;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
	
	public String filterText(String text) {
		return text.replaceAll("[\\n]+", " ").replaceAll("[\\t\\r]+", "");
	}
}
