package gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import common.CommonData;
import common.EntityRenderer;
import util.AdditionalDataObjects;

@SuppressWarnings("serial")
public class GUIHelper_MainMenu {
	public static JMenuItem showTextMenu = null;
	public static JMenuItem selectorModeMenu = null;
	public static void addActionsToMenuBar(JMenuBar menuBar) {
		JMenu fileMenu = new  JMenu(common.Lang.InnerTable.Item.itemFileMenuName);
		menuBar.add(fileMenu);
		
		Action loadAction = new  AbstractAction(common.Lang.InnerTable.Action.loadCircuitActionName) {
			public void actionPerformed(ActionEvent event) {
				
				JFileChooser jf= new  JFileChooser(CommonData.appDir);
				jf.addChoosableFileFilter(new  TextFileFilter(".txt"));
				jf.addChoosableFileFilter(new  TextFileFilter(".htm,.html"));
				jf.addChoosableFileFilter(new  TextFileFilter(".schematic"));
				int  result = jf.showOpenDialog(null);
				//CommonData.panel.requestFocus();
				if(result==JFileChooser.APPROVE_OPTION) {
					try {
						CommonData.fileName = jf.getSelectedFile().getCanonicalPath();
					} catch (IOException e) {
						System.out.println(common.Lang.InnerTable.Action.fileNotFoundName);
						System.out.println(jf.getSelectedFile().toString());
						return;
					}
						//File iF= new  File(fileName);

					if(CommonData.fileName.indexOf(".htm", CommonData.fileName.length()-6)>=0)
						CommonData.taskManager.addOperation(CommonData.scheme,"loadDiscuss",AdditionalDataObjects.pack("fileName",CommonData.fileName));//CommonData.scheme.loadDuscuss(CommonData.fileName);
					if(CommonData.fileName.indexOf(".schematic", CommonData.fileName.length()-12)>=0)
						CommonData.taskManager.addOperation(CommonData.scheme,"loadResult",AdditionalDataObjects.pack("fileName",CommonData.fileName));//CommonData.scheme.loadResult(CommonData.fileName);
					if(CommonData.fileName.indexOf(".txt", CommonData.fileName.length()-6)>=0) {
						CommonData.frame_tableSelector.prepare(GUIAbstractSelector.innerStateLoad);
					}
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
				TextFileFilter schematicFilter = new  TextFileFilter(".schematic");
				//if(CommonData.fileName==null) {//-----autosave feature
				jf.addChoosableFileFilter(txtFilter);
				jf.addChoosableFileFilter(bmpFilter);
				jf.addChoosableFileFilter(schematicFilter);
				int result = jf.showSaveDialog(null);
				if(result==JFileChooser.APPROVE_OPTION) {
					CommonData.fileName = jf.getSelectedFile().getAbsolutePath();
				}else return;
				//}//-----autosave feature
				// Now looking for selected filter
				if(jf.getFileFilter()==bmpFilter || (CommonData.fileName.indexOf(".bmp")>0)) {
					if(CommonData.fileName.indexOf(".bmp")<0)CommonData.fileName+=".bmp";
					try {ImageIO.write(EntityRenderer.getCanvasImage(), "BMP", new File(CommonData.fileName));
					} catch (IOException e) {System.out.println(common.Lang.InnerTable.Action.saveCircuitActionErroredName);}
				}
				if(jf.getFileFilter()==schematicFilter || (CommonData.fileName.indexOf(".schematic")>0)) {
					if(CommonData.fileName.indexOf(".schematic")<0)CommonData.fileName+=".schematic";
					try {ImageIO.write(EntityRenderer.getCanvasImage(), "BMP", new File(CommonData.fileName));
					} catch (IOException e) {System.out.println(common.Lang.InnerTable.Action.saveCircuitActionErroredName);}
				}
				if(jf.getFileFilter()==txtFilter || (CommonData.fileName.indexOf(".txt")>0)) {
					if(CommonData.fileName.indexOf(".txt")<0)CommonData.fileName+=".txt";
					CommonData.frame_tableSelector.prepare(GUIAbstractSelector.innerStateSave);
				}
			}
		};
		JMenuItem saveMenu = new  JMenuItem(saveAction);
		fileMenu.add(saveMenu);

		Action exportReportAction = new  AbstractAction(common.Lang.InnerTable.Action.exportReportActionName) {
			public void actionPerformed(ActionEvent event) {
				if(CommonData.frame_reportSelector==null)CommonData.frame_reportSelector = new GUIReportSelector();
				CommonData.frame_reportSelector.prepare(GUIAbstractSelector.innerStateSave);
			}
		};
		JMenuItem exportReportMenu = new  JMenuItem(exportReportAction);
		fileMenu.add(exportReportMenu);
		
		
		Action showTextMenuAction = new  AbstractAction(common.Lang.InnerTable.Action.showTextMenuActionName) {
			public void actionPerformed(ActionEvent event) {
				//EntityEditor_Helper.panel.requestFocus();
				EntityRenderer.isShowPartNamesAlways=1-EntityRenderer.isShowPartNamesAlways;
				if(EntityRenderer.isShowPartNamesAlways==0) {
					showTextMenu.setText(common.Lang.InnerTable.Item.itemTextMenuSwitchedName);
				}else {
					showTextMenu.setText(common.Lang.InnerTable.Item.itemTextMenuName);
				}
			}
		};
		Action selectorModeMenuAction = new  AbstractAction(common.Lang.InnerTable.Item.selectorModeMenuName) {
			public void actionPerformed(ActionEvent event) {
				//CommonData.panel.requestFocus();
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
				CommonData.frame_tableSelector.prepare(GUIAbstractSelector.innerStatePrint);
			}
		};
		JMenuItem printForumMenu = new  JMenuItem(printForumMenuAction);
		editorMenu.add(printForumMenu);

		Action printSequenceMenuAction = new  AbstractAction(common.Lang.InnerTable.Action.printSequenceMenuActionName) {
			public void actionPerformed(ActionEvent event) {
				CommonData.frame_sequenceSelector.prepare(GUIAbstractSelector.innerStatePrint);
			}
		};
		JMenuItem printSequenceMenu = new  JMenuItem(printSequenceMenuAction);
		editorMenu.add(printSequenceMenu);

		Action printSyntaxTreeMenuAction = new  AbstractAction(common.Lang.InnerTable.Action.printSyntaxTestMenuActionName) {
			public void actionPerformed(ActionEvent event) {
				CommonData.frame_bankSelector.prepare(GUIAbstractSelector.innerStatePrint);
			}
		};
		JMenuItem printSyntaxTreeMenu = new  JMenuItem(printSyntaxTreeMenuAction);
		editorMenu.add(printSyntaxTreeMenu);
		//---------------------COMMON----------------------
		JMenu commonMenu = new JMenu(common.Lang.InnerTable.Item.itemCommonMenuName);
		menuBar.add(commonMenu);

		Action showTableEditorMenuAction = new  AbstractAction(common.Lang.InnerTable.Action.showTableEditorMenuActionName) {
			public void actionPerformed(ActionEvent event) {
				CommonData.frame2.setVisible(true);
			}
		};
		JMenuItem showTableEditorMenu = new  JMenuItem(showTableEditorMenuAction);
		commonMenu.add(showTableEditorMenu);
		
		
		//------------------------COMPUTATION--------------------------------
		JMenu computationMenu = new  JMenu(common.Lang.InnerTable.Item.itemComputationsMenuName);
		menuBar.add(computationMenu);
		

		Action analyzeMessagesMenuAction = new  AbstractAction(common.Lang.InnerTable.Action.analyzeMessagesMenuActionName) {
			public void actionPerformed(ActionEvent event) {
				CommonData.frame_tableSelector.prepare(GUITableSelector.innerState_languageprocessing);
			}
		};
		JMenuItem analyzeMessagesMenu = new  JMenuItem(analyzeMessagesMenuAction);
		computationMenu.add(analyzeMessagesMenu);

		Action analyzeLemmaPatternsMenuAction = new  AbstractAction(common.Lang.InnerTable.Action.analyzeLemmaPatternsMenuActionName) {
			public void actionPerformed(ActionEvent event) {
				CommonData.frame_bankSelector.prepare(GUIMessageBankSelector.innerState_MakeSequenceTree);
			}
		};
		JMenuItem analyzeLemmaPatternsMenu = new  JMenuItem(analyzeLemmaPatternsMenuAction);
		computationMenu.add(analyzeLemmaPatternsMenu);

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
/*
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
*/
