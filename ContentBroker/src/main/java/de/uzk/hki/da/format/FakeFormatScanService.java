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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uzk.hki.da.utils.C;
import de.uzk.hki.da.utils.Path;

/**
 * @author Daniel M. de Oliveira
 */
public class FakeFormatScanService implements FormatScanService{

	@Override
	public List<FileWithFileFormat> identify(Path workPath,List<FileWithFileFormat> files, boolean pruneExceptions) throws FileNotFoundException{
		
		for (FileWithFileFormat f:files){
			
			if (f.getPath().toString().toLowerCase().endsWith(".avi")){
				f.setFormatPUID("fmt/5");
				f.setSubformatIdentifier("cinepak");
				continue;
			}
			
			if (f.getPath().toString().toLowerCase().toString().toLowerCase().endsWith(".mxf")){
				f.setFormatPUID("fmt/200");
				f.setSubformatIdentifier("dvvideo");
				continue;
			}
			
			if (f.getPath().toString().toLowerCase().toString().toLowerCase().endsWith(".mov")){
				f.setFormatPUID("x-fmt/384");
				f.setSubformatIdentifier("svq1");
				continue;
			}
			
			if (f.getPath().toString().toLowerCase().toLowerCase().endsWith(".tif")){
				f.setFormatPUID("fmt/353");
				continue;
			}
			
			if (f.getPath().toString().toLowerCase().toLowerCase().endsWith(".bmp")){
				f.setFormatPUID("fmt/116");
				continue;
			}
			
			if (f.getPath().toString().toLowerCase().toLowerCase().endsWith(".jp2")){
				f.setFormatPUID("x-fmt/392");
				continue;
			}
			
			if (f.getPath().toString().toLowerCase().toLowerCase().endsWith(".gif")){
				f.setFormatPUID("fmt/4");
				continue;
			}
			
			if (f.getPath().toString().toLowerCase().toLowerCase().endsWith(".pdf")){
				f.setFormatPUID("fmt/16");
				continue;
			}

			if (f.getPath().toString().toLowerCase().toLowerCase().endsWith(".xml")){
				f.setFormatPUID("fmt/101");
			}
			
			if (f.getPath().toString().toLowerCase().toLowerCase().endsWith(".rdf")){
				f.setFormatPUID("fmt/101");
			}
			if (f.getPath().toString().toLowerCase().toLowerCase().endsWith(".jpg")){
				f.setFormatPUID("fmt/43");
			}
			
			BufferedReader br=new BufferedReader(new FileReader(Path.makeFile(workPath,f.getPath())));
	        String line;
	        try {
				while((line=br.readLine())!=null){
				    if (patternFound(line,"<mets.*>")){
				    	f.setSubformatIdentifier(C.SUBFORMAT_IDENTIFIER_METS);
				    	f.setFormatPUID(FFConstants.XML_PUID);
				    	break;
				    	}
				    if (patternFound(line,"<ead .*>") || patternFound(line,"<ead.*>")){
				    	f.setSubformatIdentifier(C.SUBFORMAT_IDENTIFIER_EAD);
				    	f.setFormatPUID(FFConstants.XML_PUID);
				    	break;
				    	}
				    if (patternFound(line,"<lido:lido>")){
				    	
				    	f.setSubformatIdentifier(C.SUBFORMAT_IDENTIFIER_LIDO);
				    	f.setFormatPUID(FFConstants.XML_PUID);
				    	break;
				    	}
				    if (patternFound(line,"<x:xmpmeta.*")){
				    	f.setSubformatIdentifier(C.SUBFORMAT_IDENTIFIER_XMP);
				    	f.setFormatPUID(FFConstants.XML_PUID);
				    	break;
				    	}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}		
	        try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
	    	
		}
		return (List<FileWithFileFormat>) files;
	}
	
	
	private boolean patternFound(String line,String pattern){
		Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(line);
        if (m.find()) return true;
        return false;
	}


	
	@Override
	public boolean isConnectable() {
		return true;
	}


	@Override
	public void setKnownFormatCmdLineErrors(KnownFormatCmdLineErrors knownerrors) {
		
	}

}
