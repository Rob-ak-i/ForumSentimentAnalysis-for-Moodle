package common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import parts.PartBinaryBasic;
import parts.PartNode;

public class EntityEditor_Helper {
//SETUP BLOCK
	/**Color*/
	public static Color colorMain=Color.BLACK;
	public static Color colorSelected=Color.red;
	public static Color colorSelectedSub=Color.blue;
	public static Color colorSelectedSub1=Color.yellow;
	public static Color getColor(int index) {
		switch(index) {
		case 0:	return colorMain;
		case 1:	return colorSelected;
		case 2:	return colorSelectedSub;
		case 3:	return colorSelectedSub1;
		}
		return Color.gray;
	};
	public static Color getTextColor(int index) {
		switch(index) {
		case 0:	return Color.gray;
		case 1:	return Color.magenta;
		case 2:	return Color.cyan;
		case 3:	return Color.orange;
		}
		return Color.gray;
	};
	public static Color colorBackground=Color.white;
	public static Color colorText=Color.BLACK;
	private static java.awt.Color[] colors = {Color.red,Color.orange,Color.yellow,Color.green,Color.cyan,Color.blue,Color.magenta};
	public static Color colorRandomPallete() {return colors[(int)(Math.random()*(double)colors.length)];	}
	public static Color colorRandom() {return new Color((int)(Math.random()*16777215.));	}
	public static int isShowPartNamesAlways=0;
	
//VARS BLOCK
	
	public static parts.PartNode nodeFrom=null, nodeTo=null;public static int partClassIndex=0;
	public static parts.PartBasic tempelement=null;
	public static JComboBox partsList;
	
	/**paint surface*/
	public static EntityRenderer renderer=null;
	//public static BufferedImage bi=null;//imag=null;
	public static int width=CommonData.WIDTH;
	public static int height=CommonData.HEIGHT;
	public static Graphics graphics;
	public static Graphics2D graphics2d;
	public static JFrame frame;
	public static JPanel panel;
	

	/**common Interface*/
	public static JColorChooser tcc;
	public static JButton colorButton;
	
	static int xPad,yPad;
	static int xf,yf;
	static int thickness;
	static boolean pressed=false;


	//public static final int MODE_DELETE=-1;
	public static final int MODE_OBSERVE=0;
	public static final int MODE_SELECT=1;
	public static final int MODE_MOVE=2;
	
	public static final int MODE_PRINT_TEXT=3;
	public static final int MODE_DRAW_LINE=4;
	
	public static final int MODE_SETUP_NODE=10;
	
	
	
	public static final int MODE_SETUP_PART_STAGE1_selectNodeFrom=30;
	public static final int MODE_SETUP_PART_STAGE2_selectNodeTo=31;
	public static final int MODE_SETUP_PART_STAGE3_selectPosition=32;

	public static final int MODE_SELECT_WITH_CONTROL=40;
	public static final int MODE_SETUP_GenBy_STAGE1_selectNodeFrom=41;
	public static final int MODE_SETUP_GenBy_STAGE2_selectNodeTo=42;
	public static final int MODE_SETUP_GenBy_STAGE1_selectElementCurrence=43;
	
	public static int mode=MODE_OBSERVE;//public void setMode(int arg) {mode=arg;}

	public static boolean isLShiftPressed=false;
	public static boolean isLControlPressed=false;
	/**if we load picture*/
	public static boolean loading=false;
	
	public static void addListeners() {
		panel.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				// TODO Auto-generated method stub
				double value=arg0.getPreciseWheelRotation();
				if(isLControlPressed) {
					EntityRenderer.camvisionradius+=value;
				}else {
					if(isLShiftPressed) {
						EntityRenderer.camposx+=value;
					}else
						EntityRenderer.camposy+=value;
				}
			}
			
		});
		panel.addMouseMotionListener(new  MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) { 
				if (pressed==true) {
					//Graphics g = CommonGraphic.imag.getGraphics();
					Graphics2D g2 = EntityEditor_Helper.graphics2d;//(Graphics2D)g;
					g2.setColor(EntityEditor_Helper.colorMain);
					switch (mode) {
					
					case MODE_SELECT:
						break;
						
					}
					xPad=e.getX();
					yPad=e.getY();
				}
				//panel.repaint();
			}
		});
		panel.addMouseListener(new  MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//Graphics g = CommonGraphic.imag.getGraphics();
				Graphics2D g2 = EntityEditor_Helper.graphics2d;//Graphics2D g2 = (Graphics2D)g;
				g2.setColor(EntityEditor_Helper.colorMain);
				panel.requestFocus();
				switch (mode) {
				// text
				case MODE_PRINT_TEXT:
					// setting focus on panel
					// for input text on it
					panel.requestFocus();
					break;
				
       
				}
				xPad=e.getX();
				yPad=e.getY();
				
				pressed=true;
				//panel.repaint();      
			}
			public void mousePressed(MouseEvent e) {
				xPad=e.getX();
				yPad=e.getY();
				xf=e.getX();
				yf=e.getY();
				pressed=true;
			}
			public void mouseReleased(MouseEvent e) {
				int x=e.getX(),y=e.getY();
				Graphics g = EntityEditor_Helper.graphics;//Graphics g = CommonGraphic.imag.getGraphics();
				Graphics2D g2 = EntityEditor_Helper.graphics2d;//Graphics2D g2 = (Graphics2D)g;
				g2.setColor(EntityEditor_Helper.colorMain);
				//common computations for oval and rectangle
				int  x1=xf, x2=xPad, y1=yf, y2=yPad;
				if(xf>xPad) {
					x2=xf; x1=xPad; 
				}
				if(yf>yPad) {
					y2=yf; y1=yPad; 
				}
				switch(mode) {
				// line
				case MODE_DRAW_LINE:
					g.drawLine(xf, yf, e.getX(), e.getY());
					break;
				case MODE_MOVE:
					if(CommonData.partSelected==null)break;
					CommonData.scheme.drawElementOnMatrix(CommonData.partSelected, true);
					if(CommonData.scheme.whatElementIndexInZone(x,y,PartBinaryBasic.SIZE_WIDTH_STANDART)!=-1) {
						CommonData.scheme.drawElementOnMatrix(CommonData.partSelected, false);
						break;
					}
					CommonData.partSelected.drawWithColor(colorBackground);
					if(EntityEditor_Helper.isShowPartNamesAlways==0)	CommonData.partSelected.drawName(colorBackground);
					CommonData.partSelected.drawWithColor(colorBackground);
					CommonData.partSelected.setPos(x,y);
					CommonData.scheme.drawElementOnMatrix(CommonData.partSelected, false);
					CommonData.partSelected.drawWithColor(colorSelected);
					if(EntityEditor_Helper.isShowPartNamesAlways==0)	CommonData.partSelected.drawName(colorSelected);
					//CommonData.scheme.redrawAll();
					break;
				case MODE_SELECT:
					if(CommonData.partSelected!=null)CommonData.partSelected.draw();
					
					tempelement=CommonData.scheme.whatElementInZone(x, y, 10);
					if(tempelement==null) {
						CommonData.partSelected=null;
						CommonData.nameedit.setText(common.Lang.InnerTable.Item.itemNameEditFantomCaptionName);
						CommonData.captionedit.setText(common.Lang.InnerTable.Item.itemCaptionEditFantomCaptionName);
						break;
					}

					CommonData.nameedit.setText(tempelement.partName);
					CommonData.captionedit.setText(tempelement.getCaption());
					CommonData.partSelected=tempelement;
					CommonData.partSelected.drawSelected();
					//if(CommonData.partSelected.getClass().getSimpleName().compareTo(PartBinaryBasic.classesList[6].getSimpleName())==0)
					//	((parts.PartBinaryRelation)CommonData.partSelected).drawCurrent(Color.blue, 1, 1);
					break;
					
				case MODE_SETUP_NODE:
					tempelement=CommonData.scheme.whatElementInZone(x, y, 10);
					if(tempelement!=null)break;
					CommonData.scheme.addNode(x, y);
					break;
				case MODE_SELECT_WITH_CONTROL:
					if(CommonData.partSelected==null)break;
					tempelement=CommonData.scheme.whatElementInZone(x, y, 10);
					if(tempelement==null)break;
					//((PartBinaryControlledBasic)CommonData.partSelected).getNextElement(tempelement);
					break;
					
					
					
					
				case MODE_SETUP_PART_STAGE1_selectNodeFrom:
					/**setting up double port
					 * part 1 - basic node selection*/
					tempelement=CommonData.scheme.whatElementInZone(xPad, yPad, 10);
					if(tempelement==null)break;
					if(tempelement.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())!=0)break;
					nodeFrom=(PartNode) tempelement;
					nodeFrom.drawWithColor(Color.blue);
					mode=MODE_SETUP_PART_STAGE2_selectNodeTo;
					break;
				case MODE_SETUP_PART_STAGE2_selectNodeTo:
					/**setting up double port
					 * part 2 - second node selection*/
					tempelement=CommonData.scheme.whatElementInZone(xPad, yPad, 10);
					if(tempelement==null)break;
					if(tempelement.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())!=0)break;
					nodeTo=(PartNode) tempelement;
					if(nodeTo==nodeFrom)break;
					nodeTo.drawWithColor(Color.red);
					mode=MODE_SETUP_PART_STAGE3_selectPosition;
					break;
				case MODE_SETUP_PART_STAGE3_selectPosition:
					/**setting up double port
					 * part 3 - positioning and setting*/
					CommonData.scheme.addPart(xPad, yPad, nodeFrom, nodeTo, partsList.getSelectedIndex());
					nodeFrom.drawWithColor(Color.black);
					nodeTo.drawWithColor(Color.black);
					nodeFrom=null;
					nodeTo=null;
					
					mode=MODE_SETUP_PART_STAGE1_selectNodeFrom;
					break;
					
					
					
					
				}
				xf=0; yf=0;
				pressed=false;
				
				panel.repaint();
			}
		});
		
		addKeyListenerToPanel();
		
		frame.addComponentListener(new  ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent evt) {
				/**if loading, then change forms size*/
				/**making this in loading code section(bottom)*/
				System.out.println("resizingWindow WIP!");
				loading=true;
				if(loading==false) {
					loading=true;
					panel.setSize(frame.getWidth(), frame.getHeight()-130);
					renderer.createCanvasComponents(true);
					panel.repaint();
					//Graphics graphics = CommonGraphic.imag.getGraphics();
					//Graphics2D graphics2d = (Graphics2D)graphics;
					//CommonGraphic.graphics=graphics;
					//CommonGraphic.graphics2d=graphics2d;
				}
				
				loading=false;
			}
		});
	}
	public static void addKeyListenerToPanel() {
		panel.addKeyListener(new  KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int keyCode=e.getExtendedKeyCode();
				switch(keyCode) {
				case KeyEvent.VK_CONTROL:
					isLControlPressed=false;
					if(mode==MODE_SELECT_WITH_CONTROL)
						mode=MODE_SELECT;
					break;
				case KeyEvent.VK_SHIFT:
					isLShiftPressed=false;
					break;
				
				}
				// setting focus on panel,
				// for writing text on it
				//panel.requestFocus();
			}
			public void keyPressed(KeyEvent e) {
				int keyCode=e.getExtendedKeyCode();
				switch(keyCode) {
				case KeyEvent.VK_CONTROL:
					isLControlPressed=true;
					if(mode==MODE_SELECT)
						mode=MODE_SELECT_WITH_CONTROL;
					break;
				case KeyEvent.VK_SHIFT:
					isLShiftPressed=true;
					break;
				
				}
			}
			public void keyTyped(KeyEvent e) {
				if(mode==MODE_SELECT && CommonData.partSelected!=null){
					if(CommonData.partSelected.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())!=0) {
						if(e.getKeyChar()=='-') {
							((PartBinaryBasic)CommonData.partSelected).additionalWidthDecrease();
						}
						if(e.getKeyChar()=='=') {
							((PartBinaryBasic)CommonData.partSelected).additionalWidthIncrease();
						}						
					} 
				}
				if(mode==MODE_PRINT_TEXT){
					Graphics g = EntityEditor_Helper.graphics;//Graphics g = CommonGraphic.imag.getGraphics();
					Graphics2D g2 = EntityEditor_Helper.graphics2d;//Graphics2D g2 = (Graphics2D)g;
					g2.setColor(EntityEditor_Helper.colorMain);
					g2.setStroke(new  BasicStroke(2.0f));
					
					String str = new  String("");
					str+=e.getKeyChar();
					g2.setFont(new  Font("Arial", 0, 15));
					g2.drawString(str, xPad, yPad);
					xPad+=10;
					panel.requestFocus();
					
					panel.paint(EntityEditor_Helper.graphics);				//panel.repaint();
				}
			}
		});
	}
}
