package ph.frobbly.Lazyreader;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.AWTException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.File;

import javax.imageio.ImageIO;

import ph.frobbly.Lazyreader.TesseractOCR.Language;

public class Capture {
	
	private static final String capturedImage = "classes/captured.png";
	private static BufferedImage croppedImage;
	private static BufferedImage image;
	private static Dimension screenSize;
	private static Rectangle cropSelection;
	private static Robot bot;
	
	Capture(final Language lang) {
		
		final TesseractOCR ocr = new TesseractOCR();
		final Translator translator = new Translator();
		
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.setIconImage(transparentIcon());

		final BufferedImage original = screenshot();
		final BufferedImage copy = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		
		final JLabel screenLabel = new JLabel(new ImageIcon(copy));
		frame.add(screenLabel);
		repaint(original, copy);
		screenLabel.repaint();
		
		screenLabel.addMouseMotionListener(new MouseMotionAdapter() {
			Point start	= new Point();
			@Override
			public void	mouseMoved(MouseEvent me) {
				start = me.getPoint();
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				repaint(original, copy);
				screenLabel.repaint();
			}
			@Override
			public void	mouseDragged(MouseEvent	me) {
				Point end	= me.getPoint();
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				cropSelection = new Rectangle(start, new Dimension(end.x - start.x, end.y - start.y));
				repaint(original, copy);
				screenLabel.repaint();
			}
		});
		
		screenLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent me) {
				if(cropSelection == null) {
					return;
				}
				croppedImage = cropImage(original, cropSelection);
				try {
					// Implement this further
					File file = new File(capturedImage);

					ImageIO.write(croppedImage, "png", file);
				} catch(final Exception ex) {
					ex.printStackTrace();
				}
				frame.dispose();
				cropSelection = null;
			}
		});
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e ) {
		    	String text = ocr.readImage(lang);
		    	if (text == "") text = "No text detected please try again";
				text = translator.filterText(text);
				text = translator.encodeText(text);
				translator.translateText(text, lang);
			}
		});
		
		frame.pack();
		frame.setVisible(true);
	}

	private void repaint(BufferedImage	original, BufferedImage copy) {
		Graphics2D g = copy.createGraphics();
		g.drawImage(original, 0, 0, null);
		g.setColor(Color.GRAY);
		if (cropSelection == null) return;
		else {
			g.draw(cropSelection);
			g.setColor(new Color(25, 25, 25, 50));
			g.fill(cropSelection);
			g.dispose();
		}
	}
	
	private BufferedImage screenshot() {
		try {
			bot = new Robot();
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			image = bot.createScreenCapture(new Rectangle(screenSize));
		} catch(AWTException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	private BufferedImage cropImage(BufferedImage source, Rectangle rectangle) {
		BufferedImage captured = source.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		return captured;
	}
	
	private BufferedImage transparentIcon() {
	    BufferedImage singlePixelImage = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
	    Color transparent = new Color(0, 0, 0, 0);
	    singlePixelImage.setRGB(0, 0, transparent.getRGB());
		return singlePixelImage;
	}
}
