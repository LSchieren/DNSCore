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

 package de.uzk.hki.da.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;





/**
 * Dieses Konstrukt dient dazu, gefundene Formate und entsprechende
 * Routinen, die diese verarbeiten können, zuzuordnen. 
 * 
 * @author daniel
 * 
 *
 */
@Entity
@Table(name="conversion_policies")
public class ConversionPolicy {
	public static enum FormatType{LZA,NONLZA}

	/** The id. */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private boolean presentation = false; 
	
	/** The source_format. */
	@Column(columnDefinition="varchar(20)")
	private String source_format;

	
	/** The conversion_routine. */
	@ManyToOne(targetEntity=ConversionRoutine.class)
	private ConversionRoutine conversion_routine;
	
	/**
	 * Marks a type as LZA-able. It means this type can be converted to LZA-format, or it is already LZA-format. 
	 * This is important for Quality-Level decision in  {@link de.uzk.hki.da.cb.QualityLevelCheckAction}.
	 */
	@Column(name="format_type",columnDefinition="varchar(20)")
	@Enumerated(EnumType.STRING)
	private FormatType format_type;

	
	/**
	 * Instantiates a new conversion policy.
	 */
	public ConversionPolicy(){}
	
	/**
	 * Instantiates a new conversion policy.
	 *
	 * @param contractor the contractor
	 * @param sourceFormat puid
	 * @param conversionRoutine business key of conversion routine
	 * @param fallbackRoutine the fallback routine
	 * @param audience the audience
	 */
	public ConversionPolicy(
			String sourceFormat,
			ConversionRoutine conversionRoutine){
		
		this.source_format= sourceFormat;
		this.conversion_routine= conversionRoutine;
	}
	
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the source_format.
	 *
	 * @param sourceFormat the new source_format
	 */
	public void setSource_format(String sourceFormat) {
		this.source_format = sourceFormat;
	}

	/**
	 * Gets the source_format.
	 *
	 * @return the source_format
	 */
	public String getSource_format() {
		return source_format;
	}

	/**
	 * Sets the conversion_routine.
	 *
	 * @param conversionRoutine the new conversion_routine
	 */
	public void setConversion_routine(ConversionRoutine conversionRoutine) {
		this.conversion_routine = conversionRoutine;
	}

	/**
	 * Gets the conversion_routine.
	 *
	 * @return the conversion_routine
	 */
	public ConversionRoutine getConversion_routine() {
		return conversion_routine;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		
		return "ConversionPolicy[" +
			source_format+","
		+conversion_routine.getName()+"]";
	}

	public boolean isPresentation() {
		return presentation;
	}

	public void setPresentation(boolean presentation) {
		this.presentation = presentation;
	}

	public FormatType getFormat_type() {
		return format_type;
	}

	public void setFormat_type(FormatType format_type) {
		this.format_type = format_type;
	}
	
	
}
