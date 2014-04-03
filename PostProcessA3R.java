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
import java.util.ArrayList;
import java.util.HashSet;

public class PostProcessxml {
	public static void removeRemainingParentTags(String inputXPathTempPath, String xpathOutputPath, String tagListOutputPath) throws IOException
	{	
		ArrayList<String> tagList = new ArrayList<String>();
		BufferedReader inXPathTemp = new BufferedReader(new FileReader(inputXPathTempPath));
		BufferedWriter outXPath = new BufferedWriter(new FileWriter(xpathOutputPath));
		BufferedWriter outTagList = new BufferedWriter(new FileWriter(tagListOutputPath));
		HashSet<String> lineToRemoveSet = new HashSet<String>();
		int finalCounter = 0;
		String currentLine = null;
		String tagName = null;
		
		for(int i=1; i<=50; i++)
		{
			lineToRemoveSet.add("xpath_string(testxml, '/test["+ i + "]/vehicle') as vehicle" + i);
			lineToRemoveSet.add("xpath_string(testxml, '/test/vehicle[" + i + "]/vehicleCoverage') as vehiclecoverage" + i);
			lineToRemoveSet.add("xpath_string(testxml, '/test[" + i + "]/testMember') as testmember" + i);
			lineToRemoveSet.add("xpath_string(testxml, '/test[" + i + "]/testAdditionalInterest') as testadditionalinterest" + i);
			lineToRemoveSet.add("xpath_string(testxml, '/test[" + i + "]/endorsement') as endorsement" + i);
			lineToRemoveSet.add("xpath_string(testxml, '/test[" + i + "]/interestInformation') as interestinformation" + i);
			lineToRemoveSet.add("xpath_string(testxml, '/test[" + i + "]/othertest') as othertest" + i);
			lineToRemoveSet.add("xpath_string(testxml, '/test[" + i + "]/filed1Event') as filed1event" + i);
			lineToRemoveSet.add("xpath_string(testxml, '/test/testMember[" + i + "]/vehicleOperatorUse') as vehicleoperatoruse" + i);
		}

		while((currentLine = inXPathTemp.readLine()) != null) {
			String trimmedLine = currentLine.trim();
			if(lineToRemoveSet.contains(trimmedLine)) continue;
			outXPath.write(currentLine);
			outXPath.newLine();
			tagName = trimmedLine.substring(trimmedLine.lastIndexOf(" ") + 1);
			tagList.add(tagName);
		}

		for(String tagNameList : tagList)
		{
			outTagList.write(tagNameList);
			outTagList.newLine();
			finalCounter++;
		}

		inXPathTemp.close();
		outXPath.close();
		outTagList.close();

		System.out.println("\nFinal tag counter after cleanup: " + finalCounter);
	}
}
