/*
  DA-NRW Software Suite | ContentBroker
  Copyright (C) 2014 LVRInfoKom
  Landschaftsverband Rheinland

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.uzk.hki.da.at;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uzk.hki.da.model.Object;
import de.uzk.hki.da.model.WorkArea;
import de.uzk.hki.da.repository.RepositoryException;
import de.uzk.hki.da.utils.C;
import de.uzk.hki.da.utils.Path;
import de.uzk.hki.da.utils.XMLUtils;


/**
 * Relates to AK-T/02 Ingest - Sunny Day Scenario.
 * The Rheinlaender package is of type EAD.
 * This test checks if the metadata have been updated correctly.
 *  
 * @author Daniel M. de Oliveira
 * @author Polina Gubaidullina
 */
public class ATMetadataUpdatesRheinlaender extends AcceptanceTest{

	private static final String METS_ELEMENT_F_LOCAT = "FLocat";
	private static final String METS_ELEMENT_FILE = "file";
	private static final String METS_ELEMENT_FILE_GRP = "fileGrp";
	private static final String METS_ELEMENT_FILE_SEC = "fileSec";
	private static final String URL = "URL";
	private static final String EAD_XML = "EAD.xml";
	private static final String origName = 		"ATMetadataUpdatesRheinlaender";
	private static Object object;
	private String EAD_XPATH_EXPRESSION = 		"//daoloc/@href";

	
	@BeforeClass
	public static void setUp() throws IOException{
		ath.putSIPtoIngestArea(origName,"tgz",origName);
		ath.awaitObjectState(origName,Object.ObjectStatus.ArchivedAndValidAndNotInWorkflow);
		object=ath.getObject(origName);
	}
	
	@Test
	public void testReferencesInPip() throws FileNotFoundException, JDOMException, IOException, RepositoryException{
		
		SAXBuilder builder = XMLUtils.createValidatingSaxBuilder();
		Document doc = builder.build(new FileReader(ath.loadFileFromPip(object.getIdentifier(), "mets_2_32044.xml")));
		assertTrue(getURL(doc).contains(preservationSystem.getUrisFile()));
		assertEquals(URL, getLoctype(doc));
		assertEquals(C.MIMETYPE_IMAGE_JPEG, getMimetype(doc));
		
		SAXBuilder eadSaxBuilder = XMLUtils.createValidatingSaxBuilder();
		Document eadDoc = eadSaxBuilder.build(new FileReader(ath.loadFileFromPip(object.getIdentifier(), EAD_XML)));
		
		List<String> metsReferences = getMetsRefsInEad(eadDoc);
		assertTrue(metsReferences.size()==5);
		for(String metsRef : metsReferences) {
			if(metsRef.contains("mets_2_32044.xml")) {
				System.out.println("metsRef: "+metsRef);
				assertTrue(metsRef.equals(preservationSystem.getUrisFile()+"/"+ object.getIdentifier() +"/mets_2_32044.xml"));
			} else if(metsRef.contains("mets_2_32045.xml")) {
				assertTrue(metsRef.equals(preservationSystem.getUrisFile()+"/"+ object.getIdentifier() +"/mets_2_32045.xml"));
			} else if(metsRef.contains("mets_2_32046.xml")) {
				assertTrue(metsRef.equals(preservationSystem.getUrisFile()+"/"+ object.getIdentifier() +"/mets_2_32046.xml"));
			} else if(metsRef.contains("mets_2_32047.xml")) {
				assertTrue(metsRef.equals(preservationSystem.getUrisFile()+"/"+ object.getIdentifier() +"/mets_2_32047.xml"));
			} else {
				assertTrue(metsRef.equals(preservationSystem.getUrisFile()+"/"+ object.getIdentifier() +"/mets_2_32048.xml"));
			}
		}
	}
	
	private String getURL(Document doc){
		return doc.getRootElement()
				.getChild(METS_ELEMENT_FILE_SEC, C.METS_NS)
				.getChild(METS_ELEMENT_FILE_GRP, C.METS_NS)
				.getChild(METS_ELEMENT_FILE, C.METS_NS)
				.getChild(METS_ELEMENT_F_LOCAT, C.METS_NS)
				.getAttributeValue("href", C.XLINK_NS);
	}
	
	private String getLoctype(Document doc){
		return doc.getRootElement()
				.getChild(METS_ELEMENT_FILE_SEC, C.METS_NS)
				.getChild(METS_ELEMENT_FILE_GRP, C.METS_NS)
				.getChild(METS_ELEMENT_FILE, C.METS_NS)
				.getChild(METS_ELEMENT_F_LOCAT, C.METS_NS)
				.getAttributeValue("LOCTYPE");
	}

	private String getMimetype(Document doc){
		return doc.getRootElement()
				.getChild(METS_ELEMENT_FILE_SEC, C.METS_NS)
				.getChild(METS_ELEMENT_FILE_GRP, C.METS_NS)
				.getChild(METS_ELEMENT_FILE, C.METS_NS)
				.getAttributeValue("MIMETYPE");
	}
	
	private List<String> getMetsRefsInEad(Document eadDoc) throws JDOMException, IOException {
		
		List<String> metsReferences = new ArrayList<String>();
	
		XPath xPath = XPath.newInstance(EAD_XPATH_EXPRESSION);
		
		@SuppressWarnings("rawtypes")
		List allNodes = xPath.selectNodes(eadDoc);
		
		for (java.lang.Object node : allNodes) {
			Attribute attr = (Attribute) node;
			String href = attr.getValue();
			metsReferences.add(href);
		}
		return metsReferences;
	}	
}
	
