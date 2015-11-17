package de.uzk.hki.da.model;


/**
 * The Class StoragePolicy.
 * @author Jens Peters
 */
public class StoragePolicy {
	
	private int min_nodes;
	
	private String forbiddenNodes;
	
	private String adminEmail;

	private Boolean mailsPooled;
	
	private String gridCacheAreaRootPath;
	
	private String workAreaRootPath;
	
	private String nodeName;
	
	private String replDestinations;
	
	private String workingResource;
	
	private String commonStorageRescName;
	
	public StoragePolicy() {
		
	}
	
	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public Boolean isMailsPooled() {
		return mailsPooled;
	}

	public void setMailsPooled(Boolean mailsPooled) {
		this.mailsPooled = mailsPooled;
	}

	public String getGridCacheAreaRootPath() {
		return gridCacheAreaRootPath;
	}

	public void setGridCacheAreaRootPath(String gridCacheAreaRootPath) {
		this.gridCacheAreaRootPath = gridCacheAreaRootPath;
	}

	public String getWorkAreaRootPath() {
		return workAreaRootPath;
	}

	public void setWorkAreaRootPath(String workAreaRootPath) {
		this.workAreaRootPath = workAreaRootPath;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public int getMinNodes() {
		return min_nodes;
	}


	public void setMinNodes(int min_nodes) {
		this.min_nodes = min_nodes;
	}

	public String getReplDestinations() {
		return replDestinations;
	}

	public void setReplDestinations(String replDestinations) {
		this.replDestinations = replDestinations;
	}

	public String getForbiddenNodes() {
		return forbiddenNodes;
	}


	public void setForbiddenNodes(String forbiddenNodes) {
		this.forbiddenNodes = forbiddenNodes;
	}

	public String getWorkingResource() {
		return workingResource;
	}

	public void setWorkingResource(String workingResource) {
		this.workingResource = workingResource;
	}

	public String getCommonStorageRescName() {
		return commonStorageRescName;
	}

	public void setCommonStorageRescName(String commonStorageName) {
		this.commonStorageRescName = commonStorageName;
	}
	

		
}
