package common;

import java.io.File;
import java.io.IOException;

public class Settings {

	/** in UNIX is '/', but in Windows is '\\'
	 * */
	public static char linkDelimiter='\\';
	/** application directory path, like this:
	 * "/home/user1/Documents/Zen/projects/schematicSolver"
	 * */
	public static String appPath="";
	/** full application directory path, like this:
	 * "/home/user1/Documents/Zen/projects/schematicSolver/"
	 * */
	public static String reportSubDirectory="report";
	public static String appPathFull="";
	private static void getSymbolicLinks() {
		File file=null;
		try {
			file=new File( "." );
			appPath=file.getCanonicalPath();
		} catch (IOException e) {e.printStackTrace();}
		appPathFull=file.getAbsoluteFile().toString();
		appPathFull=appPathFull.substring(0,appPathFull.length()-1);
		linkDelimiter=appPathFull.charAt(appPathFull.length()-1);
		return;
	}
	private static void setCommonData() {
		CommonData.dataSubDir = appPathFull+"data"+linkDelimiter;
		CommonData.savesSubDir = appPathFull+"saves"+linkDelimiter;
		CommonData.reportsSubDir = appPathFull+"reports"+linkDelimiter;
		CommonData.appDir = appPath;
	}
	public static void initialize() {
		getSymbolicLinks();
		setCommonData();
		Lang.LoadInnerTable(CommonData.dataSubDir+"LangRU.txt");
		try {Lang.SaveInnerTable(CommonData.dataSubDir+"LangOutDBG.txt");} catch (Exception e) {}
	}

	public static String returnFullFileName(String fileName, String fileDirectory) {
		return appPathFull+fileDirectory+linkDelimiter+fileName;
	}
	public static String returnFullFileName(String fileName, String fileDirectory, String fileSubDirectory) {
		return appPathFull+fileDirectory+linkDelimiter+fileSubDirectory+linkDelimiter+fileName;
	}
}
