/*
 * 
 * Author: Chandeep Singh
 * Last Modified: July 18, 2013
 *
 * 
 */



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PreProcessxml {
	public static void extractNodes(String inputPath, String outputXPathTempPath) throws Exception {
		int counterLevel1 = 0;
		int counterLevel2 = 0;
		int counterLevel3 = 0;
		int counterLevel4 = 0;
		HashSet<String> unwantedParentTags = new HashSet<String>();		
		Hashtable<String, Integer> nodeCounters= new Hashtable<String, Integer>();
		String currentNode = null;
		Integer currentNodeCounter = 0;

		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		org.w3c.dom.Document inFile = db.parse(new File(inputPath));
		BufferedWriter outXPath = new BufferedWriter(new FileWriter(outputXPathTempPath));

		//Level 1
		NodeList base = inFile.getElementsByTagName("field1");
		Node baseNode = base.item(0);
		NodeList children = baseNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node item = children.item(i);
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				if(item.getPreviousSibling() == null)
				{
					counterLevel1++;

					currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + item.getNodeName() + "') as " + item.getNodeName().toLowerCase();
					if(nodeCounters.containsKey(currentNode))
					{
						currentNodeCounter = nodeCounters.get(currentNode);
						nodeCounters.remove(currentNode);
						nodeCounters.put(currentNode, currentNodeCounter + 1);
						outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "[" + (currentNodeCounter + 1) + "]" + "/" + item.getNodeName() + "') as " + item.getNodeName().toLowerCase() + (currentNodeCounter + 1));
						outXPath.newLine();
					}
					else
					{
						nodeCounters.put(currentNode, 1);
						outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "[1]"  + "/" + item.getNodeName() + "') as " + item.getNodeName().toLowerCase() + "1");
						outXPath.newLine();
					}
				}
				else
				{
					if(!(item.getPreviousSibling().toString().equalsIgnoreCase("[" + item.getNodeName() + ": null]")))
					{
						counterLevel1++;

						currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + item.getNodeName() + "') as " + item.getNodeName().toLowerCase();
						if(nodeCounters.containsKey(currentNode))
						{
							currentNodeCounter = nodeCounters.get(currentNode); System.out.println(currentNodeCounter+currentNode);
							nodeCounters.remove(currentNode);
							nodeCounters.put(currentNode, currentNodeCounter + 1);
							outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "[" + (currentNodeCounter + 1) + "]" + "/" + item.getNodeName() + "') as " + item.getNodeName().toLowerCase() + (currentNodeCounter + 1));
							outXPath.newLine();
						}
						else
						{
							nodeCounters.put(currentNode, 1);
							outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "[1]"  + "/" + item.getNodeName() + "') as " + item.getNodeName().toLowerCase() + "1");
							outXPath.newLine();
						}
					}
					else
					{
						if(!unwantedParentTags.contains(baseNode.getNodeName() + "/" + item.getNodeName()))
						{
							unwantedParentTags.add(baseNode.getNodeName() + "/" + item.getNodeName()); 
							currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + item.getNodeName() + "') as " + item.getNodeName().toLowerCase();
							nodeCounters.remove(currentNode);
						}
					}
				}

				//Level 2
				NodeList baseChild = inFile.getElementsByTagName(item.getNodeName());
				Node baseNodeChild = baseChild.item(0);
				NodeList secondChildren = baseNodeChild.getChildNodes();
				for (int j = 0; j < secondChildren.getLength(); j++) {
					Node itemChild = secondChildren.item(j);
					if (itemChild.getNodeType() == Node.ELEMENT_NODE) {
						if(itemChild.getPreviousSibling() == null)
						{
							counterLevel2++;

							currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + itemChild.getNodeName() + "') as " + itemChild.getNodeName().toLowerCase();
							if(nodeCounters.containsKey(currentNode))
							{
								currentNodeCounter = nodeCounters.get(currentNode);
								nodeCounters.remove(currentNode);
								nodeCounters.put(currentNode, currentNodeCounter + 1);
								outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() +"[" + (currentNodeCounter + 1) + "]" + "/" + itemChild.getNodeName() + "') as " + itemChild.getNodeName().toLowerCase() + (currentNodeCounter + 1));
								outXPath.newLine();
							}
							else
							{
								nodeCounters.put(currentNode, 1);
								outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName()  + "[1]" + "/" + itemChild.getNodeName() + "') as " + itemChild.getNodeName().toLowerCase() + "1");
								outXPath.newLine();
							}
						}
						else
						{
							if(!(itemChild.getPreviousSibling().toString().equalsIgnoreCase("["+itemChild.getNodeName()+": null]")))
							{
								counterLevel2++;

								currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + itemChild.getNodeName() + "') as " + itemChild.getNodeName().toLowerCase();
								if(nodeCounters.containsKey(currentNode))
								{
									currentNodeCounter = nodeCounters.get(currentNode);
									nodeCounters.remove(currentNode);
									nodeCounters.put(currentNode, currentNodeCounter + 1);
									outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() +"[" + (currentNodeCounter + 1) + "]" + "/" + itemChild.getNodeName() + "') as " + itemChild.getNodeName().toLowerCase() + (currentNodeCounter + 1));
									outXPath.newLine();;
								}
								else
								{
									nodeCounters.put(currentNode, 1);
									outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName()  + "[1]" + "/" + itemChild.getNodeName() + "') as " + itemChild.getNodeName().toLowerCase() + "1");
									outXPath.newLine();
								}
							}
							else
							{
								if(!unwantedParentTags.contains(baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + itemChild.getNodeName()))
								{
									unwantedParentTags.add(baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + itemChild.getNodeName());
									currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + itemChild.getNodeName() + "') as " + itemChild.getNodeName().toLowerCase();
									nodeCounters.remove(currentNode);
								}
							}
						}

						//Level 3
						NodeList baseChildChild = inFile.getElementsByTagName(itemChild.getNodeName());
						Node baseNodeChildChild = baseChildChild.item(0);
						NodeList thirdChildren = baseNodeChildChild.getChildNodes();
						for (int k = 0; k < thirdChildren.getLength(); k++) {
							Node itemChildChild = thirdChildren.item(k);
							if (itemChildChild.getNodeType() == Node.ELEMENT_NODE) {
								if(itemChildChild.getPreviousSibling() == null)
								{
									counterLevel3++;

									currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "/" + itemChildChild.getNodeName() + "') as " + itemChildChild.getNodeName().toLowerCase();
									if(nodeCounters.containsKey(currentNode))
									{
										currentNodeCounter = nodeCounters.get(currentNode);
										nodeCounters.remove(currentNode);
										nodeCounters.put(currentNode, currentNodeCounter + 1);
										outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "[" + (currentNodeCounter + 1) + "]" + "/" + itemChildChild.getNodeName() + "') as " + itemChildChild.getNodeName().toLowerCase() + (currentNodeCounter + 1));
										outXPath.newLine();
									}
									else
									{
										nodeCounters.put(currentNode, 1);
										outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "[1]" + "/" + itemChildChild.getNodeName() + "') as " + itemChildChild.getNodeName().toLowerCase() + "1");
										outXPath.newLine();
									}

								}
								else
								{
									if(!(itemChildChild.getPreviousSibling().toString().equalsIgnoreCase("["+itemChildChild.getNodeName()+": null]")))
									{
										counterLevel3++;

										currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "/" + itemChildChild.getNodeName() + "') as " + itemChildChild.getNodeName().toLowerCase();
										if(nodeCounters.containsKey(currentNode))
										{
											currentNodeCounter = nodeCounters.get(currentNode);
											nodeCounters.remove(currentNode);
											nodeCounters.put(currentNode, currentNodeCounter + 1);
											outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "[" + (currentNodeCounter + 1) + "]" + "/" + itemChildChild.getNodeName() + "') as " + itemChildChild.getNodeName().toLowerCase() + (currentNodeCounter + 1));
											outXPath.newLine();
										}
										else
										{
											nodeCounters.put(currentNode, 1);
											outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "[1]" + "/" + itemChildChild.getNodeName() + "') as " + itemChildChild.getNodeName().toLowerCase() + "1");
											outXPath.newLine();
										}

									}
									else
									{
										if(!unwantedParentTags.contains(baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "/" + itemChildChild.getNodeName()))
										{
											unwantedParentTags.add(baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "/" + itemChildChild.getNodeName());
											currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "/" + itemChildChild.getNodeName() + "') as " + itemChildChild.getNodeName().toLowerCase();											
											nodeCounters.remove(currentNode);
										}
									}
								}

								//Level 4
								NodeList baseChildChildChild = inFile.getElementsByTagName(itemChildChild.getNodeName());
								Node baseNodeChildChildChild = baseChildChildChild.item(0);
								NodeList fourthChildren = baseNodeChildChildChild.getChildNodes();
								for (int l = 0; l < fourthChildren.getLength(); l++) {
									Node itemChildChildChild = fourthChildren.item(l);
									if (itemChildChildChild.getNodeType() == Node.ELEMENT_NODE) {

										if(itemChildChild.getPreviousSibling() == null)
										{
											counterLevel4++;

											currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "/" + baseNodeChildChildChild.getNodeName() + "') as " + itemChildChildChild.getNodeName().toLowerCase();
											if(nodeCounters.containsKey(currentNode))
											{
												currentNodeCounter = nodeCounters.get(currentNode);
												nodeCounters.remove(currentNode);
												nodeCounters.put(currentNode, currentNodeCounter + 1);
												outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName()  + "[" + (currentNodeCounter + 1) + "]" + "/" +  "/" + baseNodeChildChildChild.getNodeName() + "') as " + itemChildChildChild.getNodeName().toLowerCase() + (currentNodeCounter + 1));
												outXPath.newLine();
											}
											else
											{
												nodeCounters.put(currentNode, 1);
												outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName()  + "[1]" +  "/" + baseNodeChildChildChild.getNodeName() + "') as " + itemChildChildChild.getNodeName().toLowerCase() + "1");
												outXPath.newLine();
											}

										}
										else
										{
											if(!(itemChildChild.getPreviousSibling().toString().equalsIgnoreCase("["+itemChildChild.getNodeName()+": null]")))
											{
												counterLevel4++;

												currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "/" + baseNodeChildChildChild.getNodeName() + "') as " + itemChildChildChild.getNodeName().toLowerCase();
												if(nodeCounters.containsKey(currentNode))
												{
													currentNodeCounter = nodeCounters.get(currentNode);
													nodeCounters.remove(currentNode);
													nodeCounters.put(currentNode, currentNodeCounter + 1);
													outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName()  + "[" + (currentNodeCounter + 1) + "]" + "/" +  "/" + baseNodeChildChildChild.getNodeName() + "') as " + itemChildChildChild.getNodeName().toLowerCase() + (currentNodeCounter + 1));
													outXPath.newLine();
												}
												else
												{
													nodeCounters.put(currentNode, 1);
													outXPath.write("xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName()  + "[1]" +  "/" + baseNodeChildChildChild.getNodeName() + "') as " + itemChildChildChild.getNodeName().toLowerCase() + "1");
													outXPath.newLine();
												}

											}
											else
											{
												if(!unwantedParentTags.contains(baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "/" + baseNodeChildChildChild.getNodeName()))
												{
													unwantedParentTags.add(baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "/" + baseNodeChildChildChild.getNodeName() + "') as " + itemChildChildChild.getNodeName().toLowerCase());
													currentNode = "xpath_string(field1xml, '/" + baseNode.getNodeName() + "/" + baseNodeChild.getNodeName() + "/" + baseNodeChildChild.getNodeName() + "/" + baseNodeChildChildChild.getNodeName() + "') as " + itemChildChildChild.getNodeName().toLowerCase();
													nodeCounters.remove(currentNode);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		outXPath.close();
		System.out.println("XML tags generated.");
		System.out.println("Level 1: " + counterLevel1);
		System.out.println("Level 2: " + counterLevel2);
		System.out.println("Level 3: " + counterLevel3);
		System.out.println("Level 4: " + counterLevel4);
		System.out.println("Total: " + (counterLevel1 + counterLevel2 + counterLevel3 + counterLevel4));

		System.out.println("\nUnwanted Parent Tags:");
		for (String parentTagsList : unwantedParentTags) {
			System.out.println(parentTagsList);
		}
	}
}
