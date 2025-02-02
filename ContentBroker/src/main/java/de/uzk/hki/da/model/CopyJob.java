package de.uzk.hki.da.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="copyjob")
public class CopyJob {

	/** The id. */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String source;
	private String aipGridPath;
	private Date last_tried;
	private String source_node_identifier;
	private String dest_node_identifier;
	private String params;
	
	public String getAipGridPath() {
		return aipGridPath;
	}
	public void setAipGridPath(String aipGridPath) {
		this.aipGridPath = aipGridPath;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Date getLast_tried() {
		return last_tried;
	}
	public void setLast_tried(Date last_tried) {
		this.last_tried = last_tried;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSource_name() {
		return source_node_identifier;
	}
	public void setSource_name(String source_node_identifier) {
		this.source_node_identifier = source_node_identifier;
	}
	public String getDest_name() {
		return dest_node_identifier;
	}
	public void setDest_name(String dest_node_identifier) {
		this.dest_node_identifier = dest_node_identifier;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
}
