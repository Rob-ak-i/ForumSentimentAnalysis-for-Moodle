package common;


import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import gui.GUIHelper;
import parts.GenericPhantom;
import parts.GenericPhantomRectangle;
import parts.PartBasic;
import parts.PartBinaryBasic;
import parts.PartNode;
import util.Dot;

public class EntityEditorMouseListener extends MouseInputAdapter{

	public static MouseWheelListener mouseWheelListener = (new MouseWheelListener() {
		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			double value=arg0.getPreciseWheelRotation();
			moveAndScaleCamera(value, EntityEditorMouseListener.isLControlPressed, EntityEditorMouseListener.isLShiftPressed);
			EntityRenderer.updateWindow(false);
		}
	});
	public static parts.PartNode nodeFrom=null, nodeTo=null;public static int partClassIndex=0;
	public static parts.PartBasic tempelement=null;
	public static JComboBox partsList;
	

	static boolean pressed=false;
	
	
	//public static final int MODE_DELETE=-1;
	public static final int MODE_OBSERVE=0;
	public static final int MODE_SELECT=1;
	public static final int MODE_SELECT_ZONE=2;
	public static final int MODE_MOVE=3;
	
	public static final int MODE_DRAW_LINE=4;
	public static final int MODE_SETUP_NODE=10;
	public static final int MODE_PUT_PHANTOM_MODE=11;
	
	public static final int MODE_SETUP_PART_STAGE1_selectNodeFrom=30;
	public static final int MODE_SETUP_PART_STAGE2_selectNodeTo=31;
	public static final int MODE_SETUP_PART_STAGE3_selectPosition=32;
	public static final int MODE_SELECT_WITH_CONTROL=40;
	public static final int MODE_SETUP_GenBy_STAGE1_selectNodeFrom=41;
	public static final int MODE_SETUP_GenBy_STAGE2_selectNodeTo=42;
	public static final int MODE_SETUP_GenBy_STAGE1_selectElementCurrence=43;
	
	private static int mode=MODE_OBSERVE;//public void setMode(int arg) {mode=arg;}
	public static int getMode() {return mode;}
	public static void setMode(int modeNew) {
		mode=modeNew;
		switch(mode) {
		case MODE_OBSERVE:
			GUIHelper.selectButton(GUIHelper.arrowbutton);
			break;
		case MODE_SELECT:
			GUIHelper.selectButton(GUIHelper.selectbutton);
			break;
		case MODE_SETUP_NODE:
			GUIHelper.selectButton(GUIHelper.buttonBrushAddNode);
			break;
		case MODE_SETUP_PART_STAGE1_selectNodeFrom:
			GUIHelper.selectButton(GUIHelper.buttonBrushAddPart);
			break;
		case MODE_MOVE:
			GUIHelper.selectButton(GUIHelper.buttonMovePart);
			break;
		}
	}
	
	public static boolean isLShiftPressed=false;
	public static boolean isLControlPressed=false;

	private static int xPush=0;
	private static int yPush=0;
	private static int xPop=0;
	private static int yPop=0;
	
	private static void moveAndScaleCamera(double value, boolean isScale, boolean isXAxis) {
		if(isScale) {
			EntityRenderer.scalex*=1-value*0.1;
			EntityRenderer.scaley*=1-value*0.1;
		}else {
			if(isXAxis) {
				EntityRenderer.camposx+=value/EntityRenderer.scalex;
			}else
				EntityRenderer.camposy+=value/EntityRenderer.scaley;
		}
	}
	JFrame frame;//EntityEditorGlassPane glassPane;
	Container contentPane;
    JMenuBar menuBar;
    JToolBar toolbar;
	public EntityEditorMouseListener(
			JToolBar toolbar,
			JMenuBar menuBar,
			JFrame frame,//EntityEditorGlassPane glassPane,
			Container contentPane){
		this.toolbar=toolbar;
		this.menuBar=menuBar;
		this.frame=frame;
		this.contentPane=contentPane;
	}
	
    public void mouseMoved(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;

		if(mode==MODE_PUT_PHANTOM_MODE){
			GenericPhantom.xi=e.getX();//
			GenericPhantom.yi=e.getY();//cur.y;
		}
    }
    
    public void mouseDragged(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
		if(mode==MODE_PUT_PHANTOM_MODE){
			GenericPhantom.xi=e.getX();//
			GenericPhantom.yi=e.getY();//cur.y;
		}
		if((mode==MODE_SELECT&&pressed==true)||mode==MODE_SELECT_ZONE) {
			mode=MODE_SELECT_ZONE;
			GenericPhantomRectangle.x0=xPush;
			GenericPhantomRectangle.y0=yPush;
			GenericPhantomRectangle.x1=e.getX();
			GenericPhantomRectangle.y1=e.getY();
			
		}
		if (pressed==true) {
			switch (mode) {
			
			case MODE_SELECT:
				break;
				
			}
		}
	}
    /*
	public void mouseClicked(MouseEvent e) {
	}
	*/
 	/*
    public void mouseEntered(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
    }
 
    public void mouseExited(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
    }*/
    public void mousePressed(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
		xPush=e.getX();
		yPush=e.getY();
		pressed=true;
    }
    public void mouseReleased(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
		int xScr=e.getX();//cur.x;
		int yScr=e.getY();//cur.y;
		xPop=xScr;
		yPop=yScr;
		Dot  MousePosOnRealMap= EntityRenderer.getRealDotFromImageCoords(xScr, yScr);
		int xMath=MousePosOnRealMap.x;//cur.x;
		int yMath=MousePosOnRealMap.y;//cur.y;
		//Dot MousePosOnRealMap=EntityRenderer.getRealDotFromImageCoords(xScr, yScr);
		//common computations for oval and rectangle
		switch(mode) {
		case MODE_SELECT_ZONE:
			mode=MODE_SELECT;
			{
				ArrayList<PartBasic> partsInZone=CommonData.scheme.whatElementsInZone(xPush,yPush,xPop,yPop);
				if(!isLShiftPressed) {
					for(int i=0;i<CommonData.partsSelected.size();++i)CommonData.partsSelected.get(i).selected=0;
					CommonData.partsSelected.clear();
				}
				for(int i=0;i<partsInZone.size();++i)partsInZone.get(i).selected=1;
				CommonData.partsSelected.addAll(partsInZone);
				partsInZone.clear();
				partsInZone=null;
				if(CommonData.partsSelected.size()>1) {
					CommonData.nameedit.setText(common.Lang.InnerTable.Item.itemNameEditFantomMultipleCaptionName);
					CommonData.captionedit.setText(common.Lang.InnerTable.Item.itemCaptionEditFantomMultipleCaptionName);
				}else {
					if(CommonData.partsSelected.size()==1) {
						CommonData.nameedit.setText(CommonData.partsSelected.get(0).partName);
						CommonData.captionedit.setText(CommonData.partsSelected.get(0).getCaption());
					}else {
						CommonData.nameedit.setText(common.Lang.InnerTable.Item.itemNameEditFantomCaptionName);
						CommonData.captionedit.setText(common.Lang.InnerTable.Item.itemCaptionEditFantomCaptionName);
					}
				}
			}
			break;
		case MODE_PUT_PHANTOM_MODE:
			mode=MODE_OBSERVE;
			GenericPhantom.performAction();
			GenericPhantom.flush(true);
			break;
		// line
		case MODE_DRAW_LINE:
			System.out.println("Attempting to make line with EntityEditor_Helper");
			//g.drawLine(xf, yf, e.getX(), e.getY());
			break;
		case MODE_MOVE:
			if(CommonData.partsSelected.size()!=1)break;
			if(CommonData.scheme.whatElementIndexInZone(xScr,yScr,PartBinaryBasic.SIZE_WIDTH_STANDART)!=-1)break;
			//CommonData.partSelected.selected=0;//.drawWithColor(colorBackground);
			//if(EntityEditor_Helper.isShowPartNamesAlways==0)	CommonData.partSelected.drawName(colorBackground);
			//CommonData.partSelected.selected=0;//drawWithColor(colorBackground);
			CommonData.partsSelected.get(0).setPos(MousePosOnRealMap.x,MousePosOnRealMap.y);
			//CommonData.scheme.drawElementOnMatrix(CommonData.partSelected, false);
			//CommonData.partSelected.selected=1;//.drawWithColor(colorSelected);
			//if(EntityEditor_Helper.isShowPartNamesAlways==0)	CommonData.partSelected.drawName(colorSelected);
			//CommonData.scheme.redrawAll();
			break;
		case MODE_SELECT:
			if(!isLShiftPressed){
				for(int i=0;i<CommonData.partsSelected.size();++i)
					CommonData.partsSelected.get(i).selected=0;
				CommonData.partsSelected.clear();
				CommonData.nameedit.setText(common.Lang.InnerTable.Item.itemNameEditFantomCaptionName);
				CommonData.captionedit.setText(common.Lang.InnerTable.Item.itemCaptionEditFantomCaptionName);
			}
			tempelement=CommonData.scheme.whatElementInZone(xScr, yScr, 10);
			if(tempelement==null)break;
			if(CommonData.partsSelected.size()==0) {
				CommonData.nameedit.setText(tempelement.partName);
				CommonData.captionedit.setText(tempelement.getCaption());
			}else {
				CommonData.nameedit.setText(common.Lang.InnerTable.Item.itemNameEditFantomMultipleCaptionName);
				CommonData.captionedit.setText(common.Lang.InnerTable.Item.itemCaptionEditFantomMultipleCaptionName);
			}
			tempelement.selected=1;
			CommonData.partsSelected.add(tempelement);
			//if(CommonData.partSelected.getClass().getSimpleName().compareTo(PartBinaryBasic.classesList[6].getSimpleName())==0)
			//	((parts.PartBinaryRelation)CommonData.partSelected).drawCurrent(Color.blue, 1, 1);
			break;
			
		case MODE_SETUP_NODE:
			tempelement=CommonData.scheme.whatElementInZone(xScr, yScr, 10);
			if(tempelement!=null)break;
			CommonData.scheme.addNode(xMath, yMath);
			break;
		case MODE_SELECT_WITH_CONTROL:
			System.out.println("Attempting to select with VKControl key pressed.");
			//((PartBinaryControlledBasic)CommonData.partSelected).getNextElement(tempelement);
			break;
			
			
			
			
		case MODE_SETUP_PART_STAGE1_selectNodeFrom:
			/**setting up double port
			 * part 1 - basic node selection*/
			tempelement=CommonData.scheme.whatElementInZone(xScr, yScr, 10);
			if(tempelement==null)break;
			if(tempelement.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())!=0)break;
			nodeFrom=(PartNode) tempelement;
			nodeFrom.selected=2;
			mode=MODE_SETUP_PART_STAGE2_selectNodeTo;
			break;
		case MODE_SETUP_PART_STAGE2_selectNodeTo:
			/**setting up double port
			 * part 2 - second node selection*/
			tempelement=CommonData.scheme.whatElementInZone(xScr, yScr, 10);
			if(tempelement==null)break;
			if(tempelement.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())!=0)break;
			nodeTo=(PartNode) tempelement;
			if(nodeTo==nodeFrom)break;
			nodeTo.selected=3;
			mode=MODE_SETUP_PART_STAGE3_selectPosition;
			break;
		case MODE_SETUP_PART_STAGE3_selectPosition:
			/**setting up double port
			 * part 3 - positioning and setting*/
			CommonData.scheme.addPart(xMath, yMath, nodeFrom, nodeTo, partsList.getSelectedIndex());
			nodeFrom.selected=0;
			nodeTo.selected=0;
			nodeFrom=null;
			nodeTo=null;
			
			mode=MODE_SETUP_PART_STAGE1_selectNodeFrom;
			break;
			
			
			
			
		}
		pressed=false;
    }

    //A basic implementation of redispatching events.
    private boolean redispatchMouseEvent(MouseEvent e) {
    	boolean result =true;
    	if(result==true)return true;
        Point glassPanePoint = e.getPoint();
        Container container = contentPane;
        Point canvasPoint = SwingUtilities.convertPoint(
                                        frame,
                                        glassPanePoint,
                                        CommonData.canvas);
        //cur=canvasPoint;
        if (canvasPoint.y < 0) { //we're not in the content pane
            if (canvasPoint.y + menuBar.getHeight() >= 0) { 
                //The mouse event is over the menu bar.
                //Could handle specially.
            } else { 
                //The mouse event is over non-system window 
                //decorations, such as the ones provided by
                //the Java look and feel.
                //Could handle specially.
            }
            result=false;
        } else {
            //The mouse event is probably over the content pane.
            //Find out exactly which component it's over.  
            Component component = 
                SwingUtilities.getDeepestComponentAt(
                                        container,
                                        canvasPoint.x,
                                        canvasPoint.y);
                             
            if ((component != null) 
                && (component.equals(toolbar))) {
                //Forward events over the check box.
                Point componentPoint = SwingUtilities.convertPoint(
                                            frame,
                                            glassPanePoint,
                                            component);
                component.dispatchEvent(new MouseEvent(component,
                                                     e.getID(),
                                                     e.getWhen(),
                                                     e.getModifiers(),
                                                     componentPoint.x,
                                                     componentPoint.y,
                                                     e.getClickCount(),
                                                     e.isPopupTrigger()));
                result=false;
            }
        }
        return result;
    }
    public static KeyAdapter keyAdapter = (new  KeyAdapter() {
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
			case 37:case 39://left-right
			case 38:case 40://up-down
				EntityRenderer.updateWindow(false);
				break;
			default:
				System.out.println(keyCode);
				break;
			}
			//System.out.println(keyCode);
			// setting focus on panel,
			// for writing text on it
			//panel.requestFocus();
		}
		public void keyPressed(KeyEvent e) {
			double value=0;
			int keyCode=e.getExtendedKeyCode();
			switch(keyCode) {
			case KeyEvent.VK_ESCAPE:
				if(mode==MODE_PUT_PHANTOM_MODE){
					mode=MODE_OBSERVE;
					GenericPhantom.flush(true);
				}
				break;
			case KeyEvent.VK_CONTROL:
				isLControlPressed=true;
				if(mode==MODE_SELECT)
					mode=MODE_SELECT_WITH_CONTROL;
				break;
			case KeyEvent.VK_SHIFT:
				isLShiftPressed=true;
				break;
			case 45:case 61:
				value=1;
				if(keyCode==45)value=-1;
				//if(mode==MODE_PUT_PHANTOM_MODE) {GenericPhantom.changeElementIndex((int)value);} - deprecated
				if(!isLControlPressed)break;
				moveAndScaleCamera(value, true, false);
				EntityRenderer.updateWindow(false);
				break;
			case 37:case 39://left-right
				value=1;
				if(keyCode==37)value=-1;
				moveAndScaleCamera(value, false, true);
				break;
			case 38:case 40://up-down
				value=1;
				if(keyCode==38)value=-1;
				moveAndScaleCamera(value, false, false);
				break;
			default:
				System.out.println(keyCode);
				break;	
			}
			//panel.requestFocus();
		}
		public void keyTyped(KeyEvent e) {
			int keyCode=e.getExtendedKeyCode();
			double value=1;
			//System.out.println(keyCode);
			switch(keyCode){

			case 37:case 39://left-right
				value=1;
				if(keyCode==37)value=-1;
				moveAndScaleCamera(value,false,true);
				break;
			case 38:case 40://up-down
				value=1;
				if(keyCode==38)value=-1;
				moveAndScaleCamera(value,false,false);
				break;
			}
			/* useless, marked as junk
			if(mode==MODE_SELECT && CommonData.partSelected!=null){
				if(CommonData.partSelected.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())!=0) {
					if(e.getKeyChar()=='=') {
						//keyCharAction
					}						
				} 
			}
			*/
			//panel.requestFocus();
		}
	});
}
