/*
 * 
 * Author: Chandeep Singh
 * Last Modified: July 26, 2013
 *
 * 
 */


public class GeneratexmlXPathScript {
	public static void main(String args[]) throws Exception
	{
		String currentDir = System.getProperty("user.dir");
		//String inputPath = "C:\\Users\\chandeep\\Documents\\test\\xmlArchive\\xmlextract-2013-07-05T14-57-24-018.xml";
		String inputPath = "C:\\Users\\chandeep\\Documents\\test\\xmlArchive\\xml-one-record-aug22.txt";
		String outputXPathTempPath = currentDir + "\\" + "temp-xml-xml-xpath-tags.txt";
		String xpathOutputPath = currentDir + "\\" + "xml-xml-xpath-tags.txt";
		String tagListOutputPath = currentDir + "\\" + "xml-xml-tag-list.txt";
		String hiveScriptPathTxt = currentDir + "\\" + "xml-all-hive-script.txt";
		String hiveScriptPathHive = currentDir + "\\" + "xml-all-hive-script.hive";
		String hiveInputFilePath = "'/user/test_data/xml-data'";
		String xmlStartingElement = "policy";
		String hiveTableNameMSDN = "xmlarchive";
		String hiveViewNameMSDN = "xmlview";
		String hiveArchiveNameFlat = "xmlarchive_flat";
		String hiveViewNameFlat = "xmlview_flat";
		String hiveTableNameFlat = "xmltable_flat";
		String hiveScriptAuthor = "Chandeep Singh";
		

		PreProcessxml.extractNodes(inputPath, outputXPathTempPath);
		PostProcessxml.removeRemainingParentTags(outputXPathTempPath, xpathOutputPath, tagListOutputPath);
		
		//CreateScripts.createHiveScriptMsdn(xpathOutputPath, tagListOutputPath, hiveScriptPathTxt, hiveInputFilePath, xmlStartingElement, hiveTableNameMSDN, hiveViewNameMSDN, hiveScriptAuthor);
		CreateScripts.createHiveScriptFlatFile(xpathOutputPath, tagListOutputPath, hiveScriptPathTxt, hiveInputFilePath, xmlStartingElement, hiveArchiveNameFlat, hiveTableNameFlat, hiveViewNameFlat, hiveScriptAuthor);
		
		Cleanupxml.changeFileName(hiveScriptPathTxt, hiveScriptPathHive);
		System.out.println();
		Cleanupxml.removeTempXPathFile(outputXPathTempPath);
		Cleanupxml.removeXPathFile(xpathOutputPath);
		Cleanupxml.removeTagsFile(tagListOutputPath);
		
		//Cleanupxml.removeHiveScriptFile(hiveScriptPath);
	}
}
