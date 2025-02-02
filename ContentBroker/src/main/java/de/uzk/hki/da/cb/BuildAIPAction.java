/*
  DA-NRW Software Suite | ContentBroker
  Copyright (C) 2013 Historisch-Kulturwissenschaftliche Informationsverarbeitung
  Universität zu Köln

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

package de.uzk.hki.da.cb;

import java.io.File;
import java.io.IOException;

import de.uzk.hki.da.action.AbstractAction;
import de.uzk.hki.da.model.WorkArea;
import de.uzk.hki.da.pkg.BagitUtils;
import de.uzk.hki.da.utils.FolderUtils;
import de.uzk.hki.da.utils.Path;

/**
 * <ol>
 * <li>Copies derivates for presentation out of the package
 * <li>Deletes unnecessary representations which in case of deltas have been added.
 * <li>Adds bagit files
 * </ol>
 * @author Daniel M. de Oliveira
 */
public class BuildAIPAction extends AbstractAction {

	@Override
	public void checkConfiguration() {
	}
	
	@Override
	public void checkPreconditions() {
	}

	@Override
	public boolean implementation() {

		deleteOldPremisFile();
		deleteUnnecessaryReps( wa.objectPath(),j.getRep_name());		
		BagitUtils.buildBagit ( wa.objectPath().toString() );
		
		return true;
	}
	


	@Override
	public void rollback() throws Exception {
		deleteBagitFiles(wa.objectPath());
	}
	
	static void deleteBagitFiles(Path objectPath) {
		final String TAGMANIFEST = "tagmanifest-md5.txt";
		final String MANIFEST = "manifest-md5.txt";
		final String BAGIT = "bagit.txt";
		final String BAGINFO = "bag-info.txt";
		if ((objectPath==null)||(objectPath.toString().equals(""))) 
			throw new IllegalArgumentException("object path null or empty");
		if (Path.makeFile(objectPath,BAGINFO).exists()) Path.makeFile(objectPath,BAGINFO).delete();
		if (Path.makeFile(objectPath,BAGIT).exists()) Path.makeFile(objectPath,BAGIT).delete();
		if (Path.makeFile(objectPath,MANIFEST).exists()) Path.makeFile(objectPath,MANIFEST).delete();
		if (Path.makeFile(objectPath,TAGMANIFEST).exists()) Path.makeFile(objectPath,TAGMANIFEST).delete();
	}
	
	
	
	
	/**
	 * Only the reps with the submitted files and the one with the conversion are to be 
	 * packaged into the final container. This plays a role in case of deltas where prior 
	 * to conversions all previous reps of an object get loaded onto the local disk.
	 * Logs on info level an entry for each representation destroyed.
	 * @author Daniel M. de Oliveira
	 */
	static void deleteUnnecessaryReps(Path objectPath,String repName){
		
		String children[] = Path.make(objectPath,WorkArea.DATA).toFile().list();
		for (int i=0;i<children.length;i++){
			if (!children[i].contains(repName) &&
					Path.make(objectPath,WorkArea.DATA,children[i]).toFile().isDirectory()) {
				try {
					FolderUtils.deleteDirectorySafe(Path.make(objectPath,WorkArea.DATA,children[i]).toFile());
				} catch (IOException e) {
					throw new RuntimeException("Couldn't delete folder: "+children[i], e);
				}
			}
		}
	}
	
	
	/** 
	 * Deletes premis_old.xml if it exists (which is the case if the package is a delta package).
	 * 
	 *  @author Thomas Kleinke
	 */
	void deleteOldPremisFile() {

		File oldPremis = Path.make(wa.dataPath(),"premis_old.xml").toFile();
		
		logger.debug("Deleting " + oldPremis.getAbsolutePath());
				
		oldPremis.delete();
	}
}
