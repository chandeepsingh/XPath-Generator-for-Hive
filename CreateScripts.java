/*
 * 
 * Author: Chandeep Singh
 * Last Modified: July 18, 2013
 *
 * 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Hashtable;



public class CreateScripts {

	public static void createHiveScriptMsdn(String xpathInputPath, String tagListInputPath, String hiveScriptPath, String hiveInputFilePath, String xmlStartingElement, String hiveTableName, String hiveViewName, String hiveScriptAuthor) throws IOException
	{
		BufferedWriter outHiveScript = new BufferedWriter(new FileWriter(hiveScriptPath));
		java.util.Date date = new java.util.Date();
		String timeStamp = new Timestamp(date.getTime()).toString();
		BufferedReader inXPath = new BufferedReader(new FileReader(xpathInputPath));
		BufferedReader inTagList = new BufferedReader(new FileReader(tagListInputPath));

		outHiveScript.write("-- Extract fields from xml XML Data");
		outHiveScript.newLine();
		outHiveScript.write("-- Data file needs to be preprocessed before it can be consumed");
		outHiveScript.newLine();
		outHiveScript.write("-- External jar needed. msdn.hadoop.readers");
		outHiveScript.newLine();
		outHiveScript.write("-- Author: " + hiveScriptAuthor);
		outHiveScript.newLine();
		outHiveScript.write("-- Last Modified: " + timeStamp);
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("add JARS msdn.hadoop.readers.jar;");
		outHiveScript.newLine();
		outHiveScript.write("set xmlinput.element=" + xmlStartingElement + ";");
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("DROP TABLE IF EXISTS " + hiveTableName + ";");
		outHiveScript.newLine();
		outHiveScript.write("DROP VIEW IF EXISTS " + hiveViewName + ";");
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("CREATE EXTERNAL TABLE " + hiveTableName + " (policyxml string)");
		outHiveScript.newLine();
		outHiveScript.write("STORED AS");
		outHiveScript.newLine();
		outHiveScript.write("INPUTFORMAT 'msdn.hadoop.mapreduce.input.XmlElementStreamingInputFormat'");
		outHiveScript.newLine();
		outHiveScript.write("OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'");
		outHiveScript.newLine();
		outHiveScript.write("LOCATION " + hiveInputFilePath + ";");
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("CREATE VIEW " + hiveViewName + "("); 

		String nextTag, lineTag = inTagList.readLine();		
		for (boolean lastTag = (lineTag == null); !lastTag; lineTag = nextTag) {
			lastTag = ((nextTag = inTagList.readLine()) == null);
			String trimmedLine = lineTag.trim();
			if (lastTag) 
			{
				outHiveScript.write(trimmedLine);
			} else 
			{
				outHiveScript.write(trimmedLine + ", ");
			}
		}

		outHiveScript.write(")");
		outHiveScript.newLine();
		outHiveScript.write("AS");
		outHiveScript.newLine();
		outHiveScript.write("SELECT");
		outHiveScript.newLine();

		String nextXPath, lineXPath = inXPath.readLine();
		for (boolean lastXPath = (lineXPath == null); !lastXPath; lineXPath = nextXPath) {
			lastXPath = ((nextXPath = inXPath.readLine()) == null);
			String trimmedLine = lineXPath.trim();
			if (lastXPath) 
			{
				outHiveScript.write(trimmedLine);
			} else 
			{
				outHiveScript.write(trimmedLine + ", ");
				outHiveScript.newLine();
			}
		}
		outHiveScript.newLine(); 
		outHiveScript.write("FROM " + hiveTableName + ";");
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("SELECT * FROM " + hiveViewName + " limit 15;");

		inXPath.close();
		inTagList.close();
		outHiveScript.close();
	}
	
	public static void createHiveScriptFlatFile(String xpathInputPath, String tagListInputPath, String hiveScriptPath, String hiveInputFilePath, String xmlStartingElement, String hiveArchiveNameFlat, String hiveTableNameFlat, String hiveViewNameFlat, String hiveScriptAuthor) throws IOException
	{
		BufferedWriter outHiveScript = new BufferedWriter(new FileWriter(hiveScriptPath));
		java.util.Date date = new java.util.Date();
		String timeStamp = new Timestamp(date.getTime()).toString();
		BufferedReader inXPath = new BufferedReader(new FileReader(xpathInputPath));
		BufferedReader inTagList = new BufferedReader(new FileReader(tagListInputPath));
		
		Hashtable<String, Integer> nodeCheck = new Hashtable<String, Integer>();
		HashSet<String> nodeCheck2 = new HashSet<String>();

		outHiveScript.write("-- Extract fields from xml XML Data");
		outHiveScript.newLine();
		outHiveScript.write("-- Data file needs to be preprocessed before it can be consumed");
		outHiveScript.newLine();
		outHiveScript.write("-- Needs flat file");
		outHiveScript.newLine();
		outHiveScript.write("-- Author: " + hiveScriptAuthor);
		outHiveScript.newLine();
		outHiveScript.write("-- Last Modified: " + timeStamp);
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("DROP TABLE IF EXISTS " + hiveArchiveNameFlat + ";");
		outHiveScript.newLine();
		outHiveScript.write("DROP VIEW IF EXISTS " + hiveViewNameFlat + ";");
		outHiveScript.newLine();
		outHiveScript.write("DROP TABLE IF EXISTS " + hiveTableNameFlat + ";");
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("CREATE EXTERNAL TABLE " + hiveArchiveNameFlat + " (policyxml string)");
		outHiveScript.newLine();
		outHiveScript.write("STORED AS TEXTFILE");
		outHiveScript.newLine();
		outHiveScript.write("LOCATION " + hiveInputFilePath + ";");
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("SET mapred.min.split.size=1024000;");
		outHiveScript.newLine();
		outHiveScript.write("SET mapred.max.split.size=4096000;");
		outHiveScript.newLine();
		outHiveScript.write("SET hive.merge.mapfiles=false;");
		outHiveScript.newLine();
		outHiveScript.newLine();
		
		outHiveScript.write("CREATE VIEW " + hiveViewNameFlat + "("); 
		
		String nextTag, lineTag = inTagList.readLine();
		System.out.println();
		System.out.println("Duplicate tags while creating script: "); 
		
		for (boolean lastTag = (lineTag == null); !lastTag; lineTag = nextTag) {
			lastTag = ((nextTag = inTagList.readLine()) == null);
			String trimmedLine = lineTag.trim();
		
			if(nodeCheck.containsKey(trimmedLine))
			{
				System.out.println(trimmedLine);
				int counter = nodeCheck.get(trimmedLine);
				nodeCheck.remove(trimmedLine);
				nodeCheck.put(trimmedLine, counter + 1);
				trimmedLine = trimmedLine + nodeCheck.get(trimmedLine);
			}
			else
			{
				nodeCheck.put(trimmedLine, 1);
			}
			
			if (lastTag) 
			{
				outHiveScript.write(trimmedLine);
			} 
			else 
			{
				outHiveScript.write(trimmedLine + ", ");
			}
		}
		System.out.println();
		outHiveScript.write(")");
		outHiveScript.newLine();
		outHiveScript.write("AS");
		outHiveScript.newLine();
		outHiveScript.write("SELECT");
		outHiveScript.newLine();

		String nextXPath, lineXPath = inXPath.readLine();
		for (boolean lastXPath = (lineXPath == null); !lastXPath; lineXPath = nextXPath) {
			lastXPath = ((nextXPath = inXPath.readLine()) == null);
			String trimmedLine = lineXPath.trim();
			if (lastXPath) 
			{
				outHiveScript.write(trimmedLine);
			} else 
			{
				outHiveScript.write(trimmedLine + ", ");
				outHiveScript.newLine();
			}
		}
		outHiveScript.newLine(); 
		outHiveScript.write("FROM " + hiveArchiveNameFlat + ";");
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("CREATE TABLE " + hiveTableNameFlat + " AS SELECT * FROM " + hiveViewNameFlat + ";");
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("DROP TABLE IF EXISTS " + hiveArchiveNameFlat + ";");
		outHiveScript.newLine();
		outHiveScript.write("DROP VIEW IF EXISTS " + hiveViewNameFlat + ";");
		outHiveScript.newLine();
		outHiveScript.newLine();
		outHiveScript.write("SELECT * FROM " + hiveTableNameFlat + " limit 1;");

		inXPath.close();
		inTagList.close();
		outHiveScript.close();
	}
}
