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

package de.uzk.hki.da.format;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uzk.hki.da.test.CTTestHelper;
import de.uzk.hki.da.test.TC;
import de.uzk.hki.da.utils.CommandLineConnector;
import de.uzk.hki.da.utils.Path;

/**
 * @author Daniel M. de Oliveira
 */
public class CTFileFormatFacadeExtractTests {
	private static final String PUID_XML = "fmt/120";
	private static final Path testRoot = Path.make(TC.TEST_ROOT_FORMAT,"CTFileFormatFacadeExtract");
	private static final ConfigurableFileFormatFacade fff = new ConfigurableFileFormatFacade();;
	private static final JhoveMetadataExtractorAndVerifier metadataExtractor = new JhoveMetadataExtractorAndVerifier();
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		CTTestHelper.prepareWhiteBoxTest();
		metadataExtractor.setCli(new CommandLineConnector());
		if (!metadataExtractor.isConnectable()) fail();
		fff.setMetadataExtractor(metadataExtractor);
		fff.setFormatScanService(new FakeFormatScanService());
	}
	
	
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		CTTestHelper.cleanUpWhiteBoxTest();
	}
	
	@Test
	public void extractEAD() {
		assertTrue(fff.connectivityCheck());
		
		try {
			fff.extract(Path.makeFile(testRoot,"vda3.XML"), Path.makeFile(testRoot,"vda3.XML.output"),PUID_XML);
		} catch (ConnectionException e) {
			fail();
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void binaryNotPresent() {
		CTTestHelper.cleanUpWhiteBoxTest();

		try {
			fff.extract(Path.makeFile(testRoot,"vda3.XML"), Path.makeFile(testRoot,"vda3.XML.output"),PUID_XML);
			fail();
		} catch (ConnectionException e) {
			assertTrue(e!=null);
		} catch (Exception e) {
			fail();
		}
	}
	
	
	
}
