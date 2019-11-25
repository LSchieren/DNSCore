package de.uzk.hki.da.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.input.BOMInputStream;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLUtils {
	
	private static ErrorHandler  myHandler=new XMLErrorHandler(System.err);
	/**
	 * 
	 * Basical skeleton taked from https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html#gcnsr
	 * @author trebunski
	 *
	 */
	private static class XMLErrorHandler implements ErrorHandler {
	    private PrintStream out;

	    XMLErrorHandler(PrintStream out) {
	        this.out = out;
	    }

	    private String getParseExceptionInfo(SAXParseException spe) {
	        String systemId = spe.getSystemId();

	        if (systemId == null) {
	            systemId = "null";
	        }

	        String info = "URI=" + systemId + " Line=" 
	            + spe.getLineNumber() + ": " + spe.getMessage();

	        return info;
	    }

	    public void warning(SAXParseException spe) throws SAXException {
	        String message = "Warning: " + getParseExceptionInfo(spe);
	        out.println(message);
	        throw new SAXException(message);
	    }
	        
	    public void error(SAXParseException spe) throws SAXException {
	        String message = "Error: " + getParseExceptionInfo(spe);
	        out.println(message);
	        throw new SAXException(message);
	    }

	    public void fatalError(SAXParseException spe) throws SAXException {
	        String message = "Fatal Error: " + getParseExceptionInfo(spe);
	        out.println(message);
	        throw new SAXException(message);
	    }
	}
	
	/**
	 * Instantiates a new SAXBuilder of SAXParser with every a feature set
	 * that prevents it from trying to access the web
	 * @author Sebastian Cuy
	 * @return the SAXBuilder
	 */
	public static SAXBuilder createNonvalidatingSaxBuilder() {
		SAXBuilder builder =  new SAXBuilder(true); //new SAXBuilder(false);
		builder.setValidation(true);
		builder.setErrorHandler(myHandler);
		builder.setFeature("http://xml.org/sax/features/validation", false);
		builder.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		return builder;
	}
	
	public static SAXParser createNonvalidatingSaxParser() throws ParserConfigurationException, SAXException {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);
		factory.setFeature("http://xml.org/sax/features/validation", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		SAXParser saxParser = factory.newSAXParser();
		return saxParser;
	}

	public static Document getDocumentFromXMLFile(File file) throws IOException, JDOMException {
		SAXBuilder builder = XMLUtils.createNonvalidatingSaxBuilder();	
		
		FileInputStream fileInputStream = new FileInputStream(file);
		BOMInputStream bomInputStream = new BOMInputStream(fileInputStream);
		Reader reader = new InputStreamReader(bomInputStream,"UTF-8");
		InputSource is = new InputSource(reader);
		is.setEncoding("UTF-8");
		Document metsDoc = builder.build(is);
		fileInputStream.close();
		bomInputStream.close();
		reader.close();
		return metsDoc;
	}
	
	public static File getRelativeFileFromReference(String ref, File metadataFile) throws IOException {
		String parentFilePath = "";
		if (metadataFile.getParentFile() != null)
			parentFilePath=metadataFile.getParentFile().getPath();
		File file = new File(new File(parentFilePath, ref).getCanonicalFile().toString().replace(new File("").getCanonicalFile().toString(), ""));
		return file;
	}
}
