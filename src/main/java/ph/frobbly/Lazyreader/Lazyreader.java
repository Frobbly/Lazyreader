package ph.frobbly.Lazyreader;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Menu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import javax.swing.SwingUtilities;

import ph.frobbly.Lazyreader.TesseractOCR.Language;

public class Lazyreader {

	Language lang = Language.OSD;
	
	Lazyreader() {
		
		Image lazyreaderIcon = Toolkit.getDefaultToolkit().getImage("classes/sloth.png");
		
		final SystemTray tray = SystemTray.getSystemTray();
		final PopupMenu menu = new PopupMenu();
		final TrayIcon icon = new TrayIcon(lazyreaderIcon, "Lazyreader", menu);
		
		icon.setImageAutoSize(true);
		try {
			tray.add(icon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
	    final MenuItem snip = new MenuItem("Snip Screen");
	    snip.setEnabled(false);
	    snip.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		new Capture(lang);
	    	}
	    });
	    menu.add(snip);
		
	    final Menu ocrLanguage = new Menu("Tesseract OCR Language");
	    menu.add(ocrLanguage);
	    
	    final CheckboxMenuItem ocrKorean = new CheckboxMenuItem("Korean");
	    final CheckboxMenuItem ocrJapanese = new CheckboxMenuItem("Japanese");
	    final CheckboxMenuItem ocrChineseS = new CheckboxMenuItem("Chinese (Simplified)");
	    final CheckboxMenuItem ocrChineseT = new CheckboxMenuItem("Chinese (Traditional)");
	    
	    ocrKorean.addItemListener(new ItemListener() {
	    	public void itemStateChanged(ItemEvent e) {
	    		if(ocrKorean.getState()) {
	    			lang = Language.KOREAN;
	    			ocrJapanese.setState(false);
	    			ocrChineseS.setState(false);
	    			ocrChineseT.setState(false);
	    			snip.setEnabled(true);
	    		}
	    		else snip.setEnabled(false);
	    	}
	    });
	    ocrLanguage.add(ocrKorean);
	    
	    ocrJapanese.addItemListener(new ItemListener() {
	    	public void itemStateChanged(ItemEvent e) {
	    		if(ocrJapanese.getState()) {
	    			lang = Language.JAPANESE;
	    			ocrKorean.setState(false);
	    			ocrChineseS.setState(false);
	    			ocrChineseT.setState(false);
	    			snip.setEnabled(true);
	    		}
	    		else snip.setEnabled(false);
	    	}
	    });
	    ocrLanguage.add(ocrJapanese);
	    
	    ocrChineseS.addItemListener(new ItemListener() {
	    	public void itemStateChanged(ItemEvent e) {
	    		if(ocrChineseS.getState()) {
	    			lang = Language.CHINESE_S;
	    			ocrKorean.setState(false);
	    			ocrJapanese.setState(false);
	    			ocrChineseT.setState(false);
	    			snip.setEnabled(true);
	    		}
	    		else snip.setEnabled(false);
	    	}
	    });
	    ocrLanguage.add(ocrChineseS);

	    ocrChineseT.addItemListener(new ItemListener() {
	    	public void itemStateChanged(ItemEvent e) {
	    		if(ocrChineseT.getState()) {
	    			lang = Language.CHINESE_T;
	    			ocrKorean.setState(false);
	    			ocrJapanese.setState(false);
	    			ocrChineseS.setState(false);
	    			snip.setEnabled(true);
	    		}
	    		else snip.setEnabled(false);
	    	}
	    });
	    ocrLanguage.add(ocrChineseT);
	    
	    menu.addSeparator();
		
	    final MenuItem exit = new MenuItem("Exit Lazyreader");
	    exit.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  System.exit(0);
	      }
	    });
	    menu.add(exit);
	}
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
    			new Lazyreader();
            }
        });
		
	}

}
