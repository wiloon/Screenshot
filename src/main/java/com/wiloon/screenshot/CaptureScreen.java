package com.wiloon.screenshot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

public class CaptureScreen extends JFrame implements ActionListener {

	private static final long serialVersionUID = 8902530562056823687L;
	private static final Logger logger = Logger.getLogger(CaptureScreen.class);
	private JButton capture, exit, save;
	private JPanel center;
	private BufferedImage bufferedImage;

	/** Creates a new instance of CaptureScreen */
	public CaptureScreen() {
		super("ScreenCapture v2.0");
		initWindow();
	}

	private void initWindow() {
		capture = new JButton("Capture");
		exit = new JButton("exit");
		save = new JButton("save");
		save.setEnabled(false);
		save.addActionListener(this);
		capture.addActionListener(this);
		exit.addActionListener(this);
		JPanel south = new JPanel();
		center = new JPanel(new BorderLayout());
		JLabel welcome = new JLabel("screenCapture", JLabel.CENTER);
		JLabel author = new JLabel("wiloon", JLabel.CENTER);
		welcome.setFont(new Font("", Font.BOLD, 15));
		author.setFont(new Font("", Font.BOLD, 12));
		welcome.setForeground(Color.black);
		author.setForeground(Color.BLUE);
		center.add(welcome, BorderLayout.CENTER);
		center.add(author, BorderLayout.SOUTH);
		south.add(capture);
		south.add(save);
		south.add(exit);
		this.getContentPane().add(center, BorderLayout.CENTER);
		this.getContentPane().add(south, BorderLayout.SOUTH);
		this.setSize(300, 300);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == capture) {
			doCapture();
		} else if (ae.getSource() == save) {
			doSave();
		} else if (ae.getSource() == exit) {
			System.exit(0);
		}
	}

	// capture
	private void doCapture() {
		try {
			this.setVisible(false);
			Thread.sleep(500);
			Robot robot = new Robot();
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension di = tk.getScreenSize();
			Rectangle rec = new Rectangle(0, 0, di.width, di.height);
			BufferedImage bi = robot.createScreenCapture(rec);
			JFrame frame = new JFrame();
			TempPanel tempPanel = new TempPanel(frame, bi, di.width, di.height);
			frame.getContentPane().add(tempPanel, BorderLayout.CENTER);
			frame.setUndecorated(true);
			frame.setSize(di);
			frame.setVisible(true);
			frame.setAlwaysOnTop(true);
		} catch (Exception exe) {
			exe.printStackTrace();
		}
	}

	private void doSave() {
		try {
			 
			JFileChooser jfc = new JFileChooser("E:\\");		 
			jfc.addChoosableFileFilter(new BMPfilter());
			jfc.addChoosableFileFilter(new JPGfilter());
			jfc.addChoosableFileFilter(new GIFfilter());
			jfc.addChoosableFileFilter(new PNGfilter());
 

			int i = jfc.showSaveDialog(this);
			if (i == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				String about = "PNG";
				String ext = file.toString().toLowerCase();
				javax.swing.filechooser.FileFilter ff = jfc.getFileFilter();
				if (ff instanceof JPGfilter) {
					if (!ext.endsWith(".jpg")) {
						String ns = ext + ".jpg";
						file = new File(ns);
						about = "JPG";
					}
				} else if (ff instanceof PNGfilter) {
					if (!ext.endsWith(".png")) {
						String ns = ext + ".png";
						file = new File(ns);
						about = "PNG";
					}
				} else if (ff instanceof BMPfilter) {
					if (!ext.endsWith(".bmp")) {
						String ns = ext + ".bmp";
						file = new File(ns);
						about = "BMP";
					}
				} else if (ff instanceof GIFfilter) {
					if (!ext.endsWith(".gif")) {
						String ns = ext + ".gif";
						file = new File(ns);
						about = "GIF";
					}
				}
				if (ImageIO.write(bufferedImage, about, file)) {
					JOptionPane.showMessageDialog(this, "");
				} else
					JOptionPane.showMessageDialog(this, "");
			}
		} catch (Exception exe) {
			exe.printStackTrace();
		}
	}

	// BMP
	private class BMPfilter extends javax.swing.filechooser.FileFilter {
		public BMPfilter() {

		}

		public boolean accept(File file) {
			if (file.toString().toLowerCase().endsWith(".bmp")
					|| file.isDirectory()) {
				return true;
			} else
				return false;
		}

		public String getDescription() {
			return "*.BMP(BMP)";
		}
	}

	// JPG
	private class JPGfilter extends javax.swing.filechooser.FileFilter {
		public JPGfilter() {

		}

		public boolean accept(File file) {
			if (file.toString().toLowerCase().endsWith(".jpg")
					|| file.toString().toLowerCase().endsWith(".jpeg")
					|| file.isDirectory()) {
				return true;
			} else
				return false;
		}

		public String getDescription() {
			return "*.JPG,*.JPEG(JPG,JPEG)";
		}
	}

	// GIF
	private class GIFfilter extends javax.swing.filechooser.FileFilter {
		public GIFfilter() {

		}

		public boolean accept(File file) {
			if (file.toString().toLowerCase().endsWith(".gif")
					|| file.isDirectory()) {
				return true;
			} else
				return false;
		}

		public String getDescription() {
			return "*.GIF(GIF)";
		}
	}

	// PNG
	private class PNGfilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			if (file.toString().toLowerCase().endsWith(".png")
					|| file.isDirectory()) {
				return true;
			} else
				return false;
		}

		public String getDescription() {
			return "*.PNG(PNG)";
		}
	}

	// 
	private class TempPanel extends JPanel implements MouseListener,
			MouseMotionListener {

		private static final long serialVersionUID = 7596639982759649709L;

		private BufferedImage bi;
		private int width, height;
		private int startX, startY, endX, endY, tempX, tempY;
		private JFrame jf;
		private Rectangle select = new Rectangle(0, 0, 0, 0);
		private Cursor cs = new Cursor(Cursor.CROSSHAIR_CURSOR);
		private States current = States.DEFAULT;
		private Rectangle[] rec;
		public static final int START_X = 1;
		public static final int START_Y = 2;
		public static final int END_X = 3;
		public static final int END_Y = 4;
		private int currentX, currentY;
		private Point p = new Point();
		private boolean showTip = true;

		public TempPanel(JFrame jf, BufferedImage bi, int width, int height) {
			this.jf = jf;
			this.bi = bi;
			this.width = width;
			this.height = height;
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			initRecs();
		}

		private void initRecs() {
			rec = new Rectangle[8];
			for (int i = 0; i < rec.length; i++) {
				rec[i] = new Rectangle();
			}
		}

		public void paintComponent(Graphics g) {
			g.drawImage(bi, 0, 0, width, height, this);
			g.setColor(Color.RED);
			g.drawLine(startX, startY, endX, startY);
			g.drawLine(startX, endY, endX, endY);
			g.drawLine(startX, startY, startX, endY);
			g.drawLine(endX, startY, endX, endY);
			int x = startX < endX ? startX : endX;
			int y = startY < endY ? startY : endY;
			select = new Rectangle(x, y, Math.abs(endX - startX), Math.abs(endY
					- startY));
			int x1 = (startX + endX) / 2;
			int y1 = (startY + endY) / 2;
			g.fillRect(x1 - 2, startY - 2, 5, 5);
			g.fillRect(x1 - 2, endY - 2, 5, 5);
			g.fillRect(startX - 2, y1 - 2, 5, 5);
			g.fillRect(endX - 2, y1 - 2, 5, 5);
			g.fillRect(startX - 2, startY - 2, 5, 5);
			g.fillRect(startX - 2, endY - 2, 5, 5);
			g.fillRect(endX - 2, startY - 2, 5, 5);
			g.fillRect(endX - 2, endY - 2, 5, 5);
			rec[0] = new Rectangle(x - 5, y - 5, 10, 10);
			rec[1] = new Rectangle(x1 - 5, y - 5, 10, 10);
			rec[2] = new Rectangle((startX > endX ? startX : endX) - 5, y - 5,
					10, 10);
			rec[3] = new Rectangle((startX > endX ? startX : endX) - 5, y1 - 5,
					10, 10);
			rec[4] = new Rectangle((startX > endX ? startX : endX) - 5,
					(startY > endY ? startY : endY) - 5, 10, 10);
			rec[5] = new Rectangle(x1 - 5, (startY > endY ? startY : endY) - 5,
					10, 10);
			rec[6] = new Rectangle(x - 5, (startY > endY ? startY : endY) - 5,
					10, 10);
			rec[7] = new Rectangle(x - 5, y1 - 5, 10, 10);
			if (showTip) {
				g.setColor(Color.CYAN);
				g.fillRect(p.x + 1, p.y + 1, 170, 20);
				g.setColor(Color.RED);
				g.drawRect(p.x + 1, p.y + 1, 170, 20);
				g.setColor(Color.RED);
				g
						.drawString("X = " + p.x + " , Y = " + p.y, p.x + 15,
								p.y + 15);
			}
		}

		// 
		private void initSelect(States state) {
			switch (state) {
			case DEFAULT:
				currentX = 0;
				currentY = 0;
				break;
			case EAST:
				currentX = (endX > startX ? END_X : START_X);
				currentY = 0;
				break;
			case WEST:
				currentX = (endX > startX ? START_X : END_X);
				currentY = 0;
				break;
			case NORTH:
				currentX = 0;
				currentY = (startY > endY ? END_Y : START_Y);
				break;
			case SOUTH:
				currentX = 0;
				currentY = (startY > endY ? START_Y : END_Y);
				break;
			case NORTH_EAST:
				currentY = (startY > endY ? END_Y : START_Y);
				currentX = (endX > startX ? END_X : START_X);
				break;
			case NORTH_WEST:
				currentY = (startY > endY ? END_Y : START_Y);
				currentX = (endX > startX ? START_X : END_X);
				break;
			case SOUTH_EAST:
				currentY = (startY > endY ? START_Y : END_Y);
				currentX = (endX > startX ? END_X : START_X);
				break;
			case SOUTH_WEST:
				currentY = (startY > endY ? START_Y : END_Y);
				currentX = (endX > startX ? START_X : END_X);
				break;
			default:
				currentX = 0;
				currentY = 0;
				break;
			}
		}

		public void mouseMoved(MouseEvent me) {
			doMouseMoved(me);
			initSelect(current);
			if (showTip) {
				p = me.getPoint();

				repaint();
			}
		}

		//
		private void doMouseMoved(MouseEvent me) {
			if (select.contains(me.getPoint())) {
				this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
				current = States.MOVE;
			} else {
				States[] st = States.values();
				for (int i = 0; i < rec.length; i++) {
					if (rec[i].contains(me.getPoint())) {
						current = st[i];
						this.setCursor(st[i].getCursor());
						return;
					}
				}
				this.setCursor(cs);
				current = States.DEFAULT;
			}
		}

		public void mouseExited(MouseEvent me) {

		}

		public void mouseEntered(MouseEvent me) {

		}

		public void mousePressed(MouseEvent me) {
			showTip = false;
			tempX = me.getX();
			tempY = me.getY();
		}

		public void mouseDragged(MouseEvent me) {
			boolean resetTempXy = true;
			int x = me.getX();
			int y = me.getY();
			int disX = x - tempX;
			int disY = y - tempY;

			if (current == States.MOVE) {
				startX += disX;
				startY += disY;
				endX += disX;
				endY += disY;

			} else if (current == States.EAST || current == States.WEST) {
				if (currentX == START_X) {
					startX += disX;
				} else {
					endX += disX;
				}
			} else if (current == States.NORTH || current == States.SOUTH) {
				if (currentY == START_Y) {
					startY += disY;
				} else {
					endY += disY;
				}
			} else if (current == States.NORTH_EAST
					|| current == States.NORTH_WEST
					|| current == States.SOUTH_EAST
					|| current == States.SOUTH_WEST) {
				if (currentY == START_Y) {
					startY += disY;
				} else {
					endY += disY;
				}
				if (currentX == START_X) {
					startX += disX;
				} else {
					endX += disX;
				}
			} else {
				startX = tempX;
				startY = tempY;
				endX = me.getX();
				endY = me.getY();
				resetTempXy = false;
			}
			// if change rec size reset the temp x and y
			if (resetTempXy) {
				tempX = x;
				tempY = y;
			}
			this.repaint();
		}

		public void mouseReleased(MouseEvent me) {
			if (me.isPopupTrigger()) {
				if (current == States.MOVE) {
					showTip = true;
					p = me.getPoint();
					startX = 0;
					startY = 0;
					endX = 0;
					endY = 0;
					repaint();
				} else {
					jf.dispose();
					updates();
				}

			}
		}

		public void mouseClicked(MouseEvent me) {
			if (me.getClickCount() == 2) {
				Point p = me.getPoint();
				logger.info(p.toString());
				if (select.contains(p)) {
					if (select.x + select.width < this.getWidth()
							&& select.y + select.height < this.getHeight()) {
						bufferedImage = bi.getSubimage(select.x, select.y,
								select.width, select.height);
						jf.dispose();
						save.setEnabled(true);
						updates();
					} else {
						int wid = select.width, het = select.height;
						if (select.x + select.width >= this.getWidth()) {
							wid = this.getWidth() - select.x;
						}
						if (select.y + select.height >= this.getHeight()) {
							het = this.getHeight() - select.y;
						}
						bufferedImage = bi.getSubimage(select.x, select.y, wid,
								het);
						jf.dispose();
						save.setEnabled(true);
						updates();
					}

				}
			}
		}
	}

	private void updates() {
		this.setVisible(true);
		if (bufferedImage != null) {
			save.setEnabled(true);
			ImageIcon ii = new ImageIcon(bufferedImage);
			JLabel jl = new JLabel(ii);
			center.removeAll();
			center.add(new JScrollPane(jl), BorderLayout.CENTER);
			SwingUtilities.updateComponentTreeUI(this);
		}
	}

	public static void main(String args[]) {
		new CaptureScreen();
	}
}

enum States {
	NORTH_WEST(new Cursor(Cursor.NW_RESIZE_CURSOR)),
	NORTH(new Cursor(Cursor.N_RESIZE_CURSOR)), NORTH_EAST(new Cursor(
			Cursor.NE_RESIZE_CURSOR)), EAST(new Cursor(Cursor.E_RESIZE_CURSOR)), SOUTH_EAST(
			new Cursor(Cursor.SE_RESIZE_CURSOR)), SOUTH(new Cursor(
			Cursor.S_RESIZE_CURSOR)), SOUTH_WEST(new Cursor(
			Cursor.SW_RESIZE_CURSOR)), WEST(new Cursor(Cursor.W_RESIZE_CURSOR)), MOVE(
			new Cursor(Cursor.MOVE_CURSOR)), DEFAULT(new Cursor(
			Cursor.DEFAULT_CURSOR));
	private Cursor cs;

	States(Cursor cs) {
		this.cs = cs;
	}

	public Cursor getCursor() {
		return cs;
	}
}
