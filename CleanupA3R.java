/*
 * 
 * Author: Chandeep Singh
 * Last Modified: July 18, 2013
 *
 * 
 */


import java.io.File;


public class Cleanupxml {
	public static void removeTempXPathFile(String outputXPathTempPath) {
		File tempFile = new File(outputXPathTempPath);
		System.out.println(tempFile.delete()? "Temp XPath file deleted: " + outputXPathTempPath:"Temp file not found: " + outputXPathTempPath);
	}
	public static void removeXPathFile(String xpathOutputPath) {
		File tempFile = new File(xpathOutputPath);
		System.out.println(tempFile.delete()? "XPath file deleted: " + xpathOutputPath:"Temp file not found: " + xpathOutputPath);
	}
	public static void removeTagsFile(String tagListOutputPath) {
		File tempFile = new File(tagListOutputPath);
		System.out.println(tempFile.delete()? "Tag file deleted: " + tagListOutputPath:"Temp file not found: " + tagListOutputPath);
	}
	public static void removeHiveScriptFile(String hiveScriptPath) {
		File tempFile = new File(hiveScriptPath);
		System.out.println(tempFile.delete()? "Tag file deleted: " + hiveScriptPath:"Temp file not found: " + hiveScriptPath);
		
	}
	public static void changeFileName(String hiveScriptPathTxt, String hiveScriptPathHive) {
		File oldFile = new File(hiveScriptPathTxt);
	    File newFile = new File(hiveScriptPathHive);
	    
	    newFile.delete();
	    if(oldFile.renameTo(newFile)) {
	    	System.out.println("Hive script located at: " + hiveScriptPathHive);
	    }
	}

}
