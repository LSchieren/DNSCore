/*
  DA-NRW Software Suite | SIP-Builder
  Copyright (C) 2014 Historisch-Kulturwissenschaftliche Informationsverarbeitung
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

package de.uzk.hki.da.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uzk.hki.da.sb.MessageWriter;

/**
 * A specialized message writer responsible for displaying info messages in CLI mode
 * 
 * @author Thomas Kleinke
 */
public class CliMessageWriter extends MessageWriter{

	private static Logger logger = LogManager.getLogger(CliMessageWriter.class);
	
	UserInput standardAnswerAlwaysOverwrite = UserInput.NO;
	UserInput standardAnswerIgnoreWrongReferencesInMetadata = UserInput.NO;
	
	/**
	 * Displays a message
	 * 
	 * @param message The message to display
	 */
	@Override
	public void showMessage(String message) {
		showMessage(message, 0);
	}
	
	/**
	 * Displays a message. The message type is ignored in CLI mode.
	 * 
	 * @param message The message to display
	 * @param type Not necessary in CLI mode
	 */
	@Override
	public void showMessage(String message, int type) {
		logger.info(message);		
	}

	/**
	 * The user isn't asked each time an already existing SIP is found in CLI mode.
	 * Instead, a previously set standard answer is returned.
	 * 
	 * @param message Not necessary in CLI mode
	 * @return The previously set standard answer
	 */
	@Override
	public UserInput showOverwriteDialog(String message) {
		return standardAnswerAlwaysOverwrite;
	}
	
	/**
	 * The user isn't asked each time an already existing collection is found in CLI mode.
	 * Instead, a previously set standard answer is returned.
	 * 
	 * @param message Not necessary in CLI mode
	 * @return The previously set standard answer
	 */
	@Override
	public UserInput showCollectionOverwriteDialog(String message) {
		
		if (standardAnswerAlwaysOverwrite == UserInput.NO) {
			System.out.println("\nIm Zielverzeichnis existiert bereits eine Lieferung des gewählten Namens."); 
			System.out.println("Bitte löschen Sie die bestehende Lieferung oder wählen Sie einen anderen Namen.");
		}
		
		return standardAnswerAlwaysOverwrite;
	}
	
	/**
	 * Displays a message that informs the user about which zero byte files were found
	 */
	public void showZeroByteFileMessage() {
		
		String message = "";
		if (zeroByteFiles.size() == 1) {
			message = "\nWARNUNG: Während der SIP-Erstellung wurde eine Datei der Größe 0 Byte gefunden:\n" 
					+ zeroByteFiles.get(0) + "\n" +					
					"Diese Datei wurde bei der SIP-Erstellung nicht berücksichtigt.";		
		}
		else {
			message = "\n\nWARNUNG: Während der SIP-Erstellung wurden mehrere Dateien der Größe 0 Byte gefunden:\n\n";
			for (String s : zeroByteFiles) {
				message += s;
				message += "\n";
			}				
			message += "\nDiese Dateien wurden bei der SIP-Erstellung nicht berücksichtigt.";		
		}
		System.out.println(message);
	}

	public UserInput getStandardAnswerAlwaysOverwrite() {
		return standardAnswerAlwaysOverwrite;
	}

	public void setStandardAnswerAlwaysOverwrite(
			UserInput standardAnswerAlwaysOverwrite) {
		this.standardAnswerAlwaysOverwrite = standardAnswerAlwaysOverwrite;
	}

	@Override
	public UserInput showWrongReferencesInMetadataDialog(String message) {
		logger.info("ANSWER "+standardAnswerIgnoreWrongReferencesInMetadata);
		if (standardAnswerIgnoreWrongReferencesInMetadata == UserInput.NO) {
			logger.error(message);
			logger.error("Das SIP wird nicht erstellt. Bitte korrigieren Sie Ihre Metadaten.");
		}
		return standardAnswerIgnoreWrongReferencesInMetadata;
	}

	public UserInput getStandardAnswerIgnoreWrongReferencesInMetadata() {
		return standardAnswerIgnoreWrongReferencesInMetadata;
	}

	public void setStandardAnswerIgnoreWrongReferencesInMetadata(
			UserInput standardAnswerIgnoreWrongReferencesInMetadata) {
		this.standardAnswerIgnoreWrongReferencesInMetadata = standardAnswerIgnoreWrongReferencesInMetadata;
	}

	@Override
	public void showLongErrorMessage(String message) {
		logger.error(message);	
	}
}
