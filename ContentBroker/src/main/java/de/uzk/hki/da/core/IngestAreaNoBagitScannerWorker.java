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

package de.uzk.hki.da.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.Session;
import org.slf4j.MDC;

import de.uzk.hki.da.model.Job;
import de.uzk.hki.da.model.Node;
import de.uzk.hki.da.model.Object;
import de.uzk.hki.da.model.User;
import de.uzk.hki.da.service.HibernateUtil;
import de.uzk.hki.da.utils.C;
import de.uzk.hki.da.utils.Path;


/**
 * Scans the staging area for new packages. If new files are there whose stay 
 * has been longer as specified in (minAge), they get moved to a destination area
 * and a job gets created for them.
 * 
 * <pre><code>
 *   Staging area may look like
 *   .../stagingRoot/TEST1
 *   .../stagingRoot/TEST2
 *   
 *   and destination area like
 *   .../destinationRoot/TEST1
 *   .../destinationRoot/TEST2
 * </code></pre>
 * A file located under .../stagingRoot/TEST1/a.txt would then be moved to .../destinationRoot/TEST1/a.txt
 * 
 * @author Daniel M. de Oliveira
 *
 */
public class IngestAreaNoBagitScannerWorker extends Worker{

	
//	/**
//	 * To rule everything out except containers of valid formats.
//	 */
//	private class AcceptedContainerFormatsFilter implements FilenameFilter {
//
//		/* (non-Javadoc)
//		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
//		 */
//		@Override
//		public boolean accept(File dir, String name) {
//
//			return dir.isDirectory();
//		
//		}
//		
//		
//	}

	
	/** The min age. */
	private int minAge; // required minimum age in milliseconds
	
	private Path ingestAreaNoBagitRootPath;
	
	/** The local node name. */
	private String localNodeId;
	
	/** The register object service. */
	private RegisterObjectService registerObjectService;
	
	/** The files. */
	private Map<String,Long> sips = new HashMap<String,Long>();
	
	/** The contractor short names. */
	private List<User> contractors = new ArrayList<User>();

	/**
	 * @return The contractors whose ingest folder will get scanned for files. 
	 */
	public Set<User> init(){
		if ((ingestAreaNoBagitRootPath==null)
			||(ingestAreaNoBagitRootPath.toString().isEmpty())) 
			throw new IllegalStateException("ingestAreaNoBagitRootPath must not set");
		
		if (! ingestAreaNoBagitRootPath.toFile().exists()) throw new RuntimeException("No file or directory: "+ingestAreaNoBagitRootPath);
		
		logger.info("Scanning staging area for contractor folders");
		String children[] = ingestAreaNoBagitRootPath.toFile().list();

		for (int i=0;i<children.length;i++){
			if (! Path.makeFile(ingestAreaNoBagitRootPath,children[i]).isDirectory())
				continue;
			
			Session session = HibernateUtil.openSession();
			session.beginTransaction();
			User contractor = getContractor(session, children[i]);
			session.close();
			if (contractor==null) {
				logger.warn("Cannot find in ObjectDB: "+children[i]+" -  will not scan sips for contractor");
				continue;
			}else
				logger.info("Will scan sips for contractor: "+children[i]);
			
			contractors.add(contractor);
		}
		return new HashSet<User>(contractors);
	}
	
	@Override
	public void setMDC() {
		MDC.put(WORKER_ID, "ingestNoBagit");
	}
	
	
	/**
	 * Checking for new files in the staging area.	
	 */
	@Override
	public void scheduleTaskImplementation(){
		
		try {
		
			long currentTimeStamp = System.currentTimeMillis();
			
			for (User contractor:contractors){

				for (String child:scanContractorFolderForReadySips(contractor.getShort_name(), currentTimeStamp)){
					
					logger.info("Found file \""+child+"\" in ingestNoBagit Area. Creating job for \""+contractor.getShort_name()+"\"");
					Object object=null;
					try {
						object = registerObjectService.registerObject( child, contractor);	
					}
					catch (UserException e) {
						logger.error("cannot register object "+child+" for contractor "+contractor+". Skip creating job for object.",e);
						continue;
					}
					
					Job job = insertJobIntoQueueAndSetWorkFlowState(
							contractor, 
							convertMaskedSlashes(FilenameUtils.removeExtension(child)),
							localNodeId,
							object);
					logger.debug("Created new Object "+object+ ":::: Created job: "+job);
				}
			}
		}
		catch (Exception e){ // Should catch everything in scheduleTask. Otherwise thread dies without notice.
			logger.error("Caught: "+e,e);
		}
	}
	
	
	
	
	private List<String> scanContractorFolderForReadySips(String short_name,
			long currentTimeStamp) {
		List<String> childrenWhichAreReadyDirs = new ArrayList<String>();
		List<String> childrenWhichAreReadySips = new ArrayList<String>();		
		childrenWhichAreReadyDirs = scanContractorFolderForReadyDirs(short_name, currentTimeStamp);
		if (childrenWhichAreReadyDirs!=null)
		childrenWhichAreReadySips.addAll(childrenWhichAreReadyDirs);
		return childrenWhichAreReadySips;
	}

	
	private List<String> scanContractorFolderForReadyDirs(String short_name,
			long currentTimeStamp) {
        File file = Path.makeFile(ingestAreaNoBagitRootPath, short_name);
		
		File[] files =  file.listFiles();
		String children[] = null;
		boolean isDirectory = false;
		String directory = null;
		List<String> childrenWhichAreReady = new ArrayList<String>();
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) 
			{
				children = files[i].list();
				isDirectory = true;
 				directory = files[i].getName();
			} 
			if (children!=null && isDirectory) {
				childrenWhichAreReady = addToList(directory,short_name,currentTimeStamp,childrenWhichAreReady);
			}
		}
		
		return childrenWhichAreReady;
	}

	/**
	 * Insert job into queue.
	 *
	 * @param contractorShortName the contractor short name
	 * @param origName the orig name
	 * @param responsibleNodeName the initial node name
	 * @return the job
	 */
	private Job insertJobIntoQueueAndSetWorkFlowState(User c,String origName,String responsibleNodeId,Object object){

		object.setObject_state(Object.ObjectStatus.InWorkflow);
		
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.saveOrUpdate(object);
		
		Node node = (Node) session.get(Node.class,Integer.parseInt(responsibleNodeId));
		
		Job job = new Job();
		job.setObject(object);
		job.setStatus(C.WORKFLOW_STATUS_START___INGEST_UNPACK_NO_BAGIT_ACTION);
		job.setResponsibleNodeName(node.getName());
		job.setCreatedAt(new Date());
		job.setModifiedAt(new Date());
	
		session.save(job);
		session.getTransaction().commit();
		session.close();

		return job;
	}
	
	
	
	
	/**
	 * Gets the contractor.
	 *
	 * @param contractorShortName the contractor short name
	 * @return null if no contractor for short name could be found
	 */
	private User getContractor(Session session, String contractorShortName) {
		@SuppressWarnings("rawtypes")
		List list;	
		list = session.createQuery("from User where short_name=?1")
	
				.setParameter("1",contractorShortName).setReadOnly(true).list();
		
		if (list.isEmpty())
			return null;
	
		return (User) list.get(0);
	}
	
	private List<String> addToList(String sipname,  String csn, long currentTimeStamp,List<String> childrenWhichAreReady) {
		if (!sips.containsKey(sipname)){
			Job job = getJob( convertMaskedSlashes(FilenameUtils.removeExtension(sipname)),
					csn);
			if ( job == null) {
				// consider only containers for which there is not already a job 
				// in queue since it is possible that the CB has stopped and now resumes work.
				logger.debug("New file found, making timestamp for: "+sipname);
				sips.put(sipname, currentTimeStamp);
			} else {
//					USER EMAIL 
//				System.out.println("ES gibt schon einen Job !!!  " + job);
			}
		}
		else
		{
			long diff = currentTimeStamp - sips.get(sipname);
			logger.debug("Old file found, lets look how their timestamps differ: "+ sipname+" diff: "+diff);
			if (diff>minAge){
				logger.debug("File "+sipname+" which has lasted "+minAge+" miliseconds is ready.");
				sips.remove(sipname);
				childrenWhichAreReady.add(sipname);
			}
			}
		return childrenWhichAreReady;
	}
	/**
	 * Gets the job.
	 *
	 * @param orig_name the orig_name
	 * @param csn the csn
	 * @return the job
	 */
	@SuppressWarnings("unchecked")
	private Job getJob(String orig_name, String csn) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		List<Job> l = null;
	
			l = session.createQuery(
					"SELECT j FROM Job j left join j.obj as o left join o.user as c where o.orig_name=?1 and c.short_name=?2"
					)
							.setParameter("1", orig_name)
							.setParameter("2", csn)
							.setReadOnly(true).list();
		session.close();

		try {
			return l.get(0);
		} catch (IndexOutOfBoundsException e) {
			logger.debug("search for a job with orig_name " + orig_name + " for user " +
						 csn + " returns null!");
			return null;
		}
	}

	/**
	 * Replaces %2F inside a string to /.
	 *
	 * @param input the input
	 * @return the string
	 */
	String convertMaskedSlashes(String input){
		return input.replaceAll("%2F", "/");
	} 
	
	public Path getIngestAreaNoBagitRootPath() {
		return ingestAreaNoBagitRootPath;
	}

	public void setIngestAreaNoBagitRootPath(Path ingestAreaNoBagitRootPath) {
		this.ingestAreaNoBagitRootPath = ingestAreaNoBagitRootPath;
	}

	/**
	 * Sets the min age.
	 *
	 * @param minAge the new min age
	 */
	public void setMinAge(int minAge){
		this.minAge = minAge;
	}
	
	/**
	 * Gets the min age.
	 *
	 * @return the min age
	 */
	public int getMinAge(){
		return minAge;
	}

	/**
	 * Gets the local node name.
	 *
	 * @return the local node name
	 */
	public String getLocalNodeId() {
		return localNodeId;
	}

	/**
	 * Sets the local node name.
	 *
	 * @param localNodeName the new local node name
	 */
	public void setLocalNodeId(String localNodeId) {
		this.localNodeId = localNodeId;
	}

	/**
	 * Gets the register object service.
	 *
	 * @return the register object service
	 */
	public RegisterObjectService getRegisterObjectService() {
		return registerObjectService;
	}

	/**
	 * Sets the register object service.
	 *
	 * @param registerObjectService the new register object service
	 */
	public void setRegisterObjectService(RegisterObjectService registerObjectService) {
		this.registerObjectService = registerObjectService;
	}

}
