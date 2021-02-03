import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import common.CommonData;
import common.EntityEditor_Helper;
import common.EntityRenderer;

@SuppressWarnings("serial")
public class GUIHelper_MainMenu {
	public static JMenuItem showTextMenu = null;
	public static JMenuItem selectorModeMenu = null;
	public static JMenuItem printForumMenu = null;
	public static void addActionsToMenuBar(JMenuBar menuBar) {
		JMenu fileMenu = new  JMenu(common.Lang.InnerTable.Item.itemFileMenuName);
		menuBar.add(fileMenu);
		
		Action loadAction = new  AbstractAction(common.Lang.InnerTable.Action.loadCircuitActionName) {
			public void actionPerformed(ActionEvent event) {
				
				JFileChooser jf= new  JFileChooser(CommonData.appDir);
				int  result = jf.showOpenDialog(null);
				EntityEditor_Helper.panel.requestFocus();
				if(result==JFileChooser.APPROVE_OPTION) {
					//try {
					// при выборе изображения подстраиваем размеры формы
					// и панели под размеры данного изображения
					try {
						CommonData.fileName = jf.getSelectedFile().getCanonicalPath();
					} catch (IOException e) {
						System.out.println(common.Lang.InnerTable.Action.fileNotFoundName);
						System.out.println(jf.getSelectedFile().toString());
					}
						//File iF= new  File(fileName);
						jf.addChoosableFileFilter(new  TextFileFilter(".txt"));
						jf.addChoosableFileFilter(new  TextFileFilter(".bmp"));
						jf.addChoosableFileFilter(new  TextFileFilter(".htm,.html"));
						//jf.addChoosableFileFilter(new  TextFileFilter(".jpg"));

					if(CommonData.fileName.indexOf(".htm", CommonData.fileName.length()-6)>=0)
						CommonData.scheme.loadDuscuss(CommonData.fileName);
					if(CommonData.fileName.indexOf(".txt", CommonData.fileName.length()-6)>=0)
						CommonData.scheme.loadResult(CommonData.fileName);
						//imag = ImageIO.read(iF);
						//loading=true;
						//f.setSize(GUI.WIDTH+40, GUI.HEIGHT+80);//f.setSize(imag.getWidth()+40, imag.getWidth()+80);
						//japan.setSize(GUI.WIDTH, GUI.HEIGHT);//japan.setSize(imag.getWidth(), imag.getWidth());
						//japan.repaint();
					//CommonGraphic.panel.requestFocus();
					//CommonGraphic.panel.paint(CommonGraphic.imag.getGraphics());
				}
			}
		};
		JMenuItem loadMenu = new  JMenuItem(loadAction);
		fileMenu.add(loadMenu);
		
		Action saveAction = new  AbstractAction(common.Lang.InnerTable.Action.saveCircuitActionName) {
			public void actionPerformed(ActionEvent event) {
				JFileChooser jf= new  JFileChooser(CommonData.appDir);
				TextFileFilter txtFilter = new TextFileFilter(".txt");
				TextFileFilter bmpFilter = new  TextFileFilter(".bmp");
				if(CommonData.fileName==null) {
					jf.addChoosableFileFilter(txtFilter);
					jf.addChoosableFileFilter(bmpFilter);
					int result = jf.showSaveDialog(null);
					if(result==JFileChooser.APPROVE_OPTION) {
						CommonData.fileName = jf.getSelectedFile().getAbsolutePath();
					}
				}
				// Now looking for selected filter
				if(jf.getFileFilter()==bmpFilter || (CommonData.fileName.indexOf(".bmp")>0)) {
					try {
						ImageIO.write(EntityRenderer.getCanvasImage(), "BMP", new File(CommonData.fileName));
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println(common.Lang.InnerTable.Action.saveCircuitActionErroredName);
					}
				} else {
					if(CommonData.fileName.indexOf(".txt")<0)CommonData.fileName=CommonData.fileName+".txt";
					CommonData.scheme.saveResult(CommonData.fileName);
				}
				//CommonGraphic.panel.requestFocus();
				//CommonGraphic.panel.paint(CommonGraphic.imag.getGraphics());
			}
		};
		JMenuItem saveMenu = new  JMenuItem(saveAction);
		fileMenu.add(saveMenu);
		
		Action saveasAction = new  AbstractAction(common.Lang.InnerTable.Action.saveAsCircuitActionName) {
			public void actionPerformed(ActionEvent event) {
				JFileChooser jf= new  JFileChooser(CommonData.appDir);
				TextFileFilter txtFilter = new  TextFileFilter(".txt");
				TextFileFilter bmpFilter = new  TextFileFilter(".bmp");
				jf.addChoosableFileFilter(txtFilter);
				jf.addChoosableFileFilter(bmpFilter);
				int  result = jf.showSaveDialog(null);
				if(result==JFileChooser.APPROVE_OPTION) {
					CommonData.fileName = jf.getSelectedFile().getAbsolutePath();
				}
				if(jf.getFileFilter()==bmpFilter || (CommonData.fileName.indexOf(".bmp")>0)) {
					CommonData.fileName=CommonData.fileName+".bmp";
					try {
						ImageIO.write(EntityRenderer.getCanvasImage(), "BMP", new File(CommonData.fileName));
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println(common.Lang.InnerTable.Action.saveCircuitActionErroredName);
					}
				} else {
					if(CommonData.fileName.indexOf(".txt")<0)CommonData.fileName=CommonData.fileName+".txt";
					CommonData.scheme.saveResult(CommonData.fileName);
				}
				//CommonGraphic.panel.requestFocus();
				//CommonGraphic.panel.paint(CommonGraphic.imag.getGraphics());
			}
		};
		JMenuItem saveasMenu = new  JMenuItem(saveasAction);
		fileMenu.add(saveasMenu);
		
		
		Action showTextMenuAction = new  AbstractAction(common.Lang.InnerTable.Action.showTextMenuActionName) {
			public void actionPerformed(ActionEvent event) {
				EntityEditor_Helper.panel.requestFocus();
				EntityEditor_Helper.isShowPartNamesAlways=1-EntityEditor_Helper.isShowPartNamesAlways;
				CommonData.scheme.redrawAllOnGraph();
				if(EntityEditor_Helper.isShowPartNamesAlways==0) {
					showTextMenu.setText(common.Lang.InnerTable.Item.itemTextMenuSwitchedName);
				}else {
					showTextMenu.setText(common.Lang.InnerTable.Item.itemTextMenuName);
				}
			}
		};
		Action selectorModeMenuAction = new  AbstractAction(common.Lang.InnerTable.Action.selectorModeMenuActionName) {
			public void actionPerformed(ActionEvent event) {
				EntityEditor_Helper.panel.requestFocus();
				CommonData.selectorMode=(CommonData.selectorMode+1)%3;
				switch(CommonData.selectorMode) {
				case 0:
					selectorModeMenu.setText(common.Lang.InnerTable.Item.selectorModeMenuName);
					break;
				case 1:
					selectorModeMenu.setText(common.Lang.InnerTable.Item.selectorModeMenuSwitchedName);
					break;
				case 2:
					selectorModeMenu.setText(common.Lang.InnerTable.Item.selectorModeMenuSwitched2Name);
					break;
				}
			}
		};
		
		//----------------------SETUP------------
		JMenu parametersMenu = new  JMenu(common.Lang.InnerTable.Item.itemParametersMenuName);
		menuBar.add(parametersMenu);
		
		showTextMenu = new  JMenuItem(showTextMenuAction);
		parametersMenu.add(showTextMenu);

		selectorModeMenu = new  JMenuItem(selectorModeMenuAction);
		parametersMenu.add(selectorModeMenu);

		//----------------------EDITOR------------
		JMenu editorMenu = new  JMenu(common.Lang.InnerTable.Item.itemEditorMenuName);
		menuBar.add(editorMenu);

		Action printForumMenuAction = new  AbstractAction(common.Lang.InnerTable.Action.printForumMenuActionName) {
			public void actionPerformed(ActionEvent event) {
				CommonData.scheme.printForum();
			}
		};
		printForumMenu = new  JMenuItem(printForumMenuAction);
		editorMenu.add(printForumMenu);
		
		
		//------------------------COMPUTATION--------------------------------
		JMenu computationMenu = new  JMenu(common.Lang.InnerTable.Item.itemComputationsMenuName);
		menuBar.add(computationMenu);
		
		Action computationMenuAction0 = new  AbstractAction(common.Lang.InnerTable.Action.computationsMenuAction0Name) {
			public void actionPerformed(ActionEvent event) {
				common.Reports.createReport();
			}
		};
		JMenuItem computationMenu0 = new  JMenuItem(computationMenuAction0);
		computationMenu.add(computationMenu0); 
		
		Action computationMenuAction1 = new  AbstractAction(common.Lang.InnerTable.Action.computationsMenuAction1Name) {
			public void actionPerformed(ActionEvent event) {
				common.Reports.makeResult1();
			}
		};
		JMenuItem computationMenu1 = new  JMenuItem(computationMenuAction1);
		computationMenu.add(computationMenu1);
		
		Action computationMenuAction2 = new  AbstractAction(common.Lang.InnerTable.Action.computationsMenuAction2Name) {
			public void actionPerformed(ActionEvent event) {
				common.Reports.makeResult2();
			}
		};
		JMenuItem computationMenu2 = new  JMenuItem(computationMenuAction2);
		computationMenu.add(computationMenu2);
		Action computationMenuAction3 = new  AbstractAction(common.Lang.InnerTable.Action.computationsMenuAction3Name) {
			public void actionPerformed(ActionEvent event) {
				common.Reports.makeResult3();
			}
		};
		JMenuItem computationMenu3 = new  JMenuItem(computationMenuAction3);
		computationMenu.add(computationMenu3);
	}
}
class TextFileFilter extends FileFilter {
	private String ext;
	public TextFileFilter(String ext) {
		this.ext=ext;
	}
	public boolean accept(java.io.File file) {
		if (file.isDirectory()) return true;
		return (file.getName().endsWith(ext));
	}
	public String getDescription() {
		return "*"+ext;
	}
}
@SuppressWarnings("serial")
class MyInternalFrame extends JInternalFrame {
	static int openFrameCount = 0;
	static final int xOffset = 30, yOffset = 30;

	public MyInternalFrame() {
	    super("Document #" + (++openFrameCount),
	          true, //resizable
	          true, //closable
	          true, //maximizable
	          true);//iconifiable
	    //...Create the GUI and put it in the window...
	    //...Then set the window size or call pack...
	    //...
	    //Set the window's location.
	    setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
	}
}
