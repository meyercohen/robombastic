package fr.dauphine.robombastic.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Utilitary class to facilitate the management of jar files and to extract folders from them
 * 
 * @author Meyer
 * @author Inspired from this link -> http://www.codejava.net/java-se/file-io/programmatically-extract-a-zip-file-using-java
 */
public class JarUtils {
	
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Extracts a zip file specified by the zipFilePath to a directory specified by
	 * destDirectory (will be created if does not exists)
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	public static void extractFromJar(String zipFilePath, String destDirectory, String directoryToExtract) throws IOException  {

		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));

		ZipEntry entry = zipIn.getNextEntry();
		
		// iterates over entries in the zip file
		while (entry != null) {
			if(entry.getName().startsWith(directoryToExtract)){
				String filePath = null;
				if(System.getProperty("os.name").contains("Windows")){
					filePath = destDirectory + File.separator + entry.getName() + File.separator; 
				}
				else{
					filePath = destDirectory + File.separator + entry.getName(); 
				}

				if (!entry.isDirectory()) {
					// if the entry is a file, extracts it
					extractFile(zipIn, filePath);
				} else {
					// if the entry is a directory, make the directory
					File dir = new File(filePath);
					dir.mkdir();
				}
				
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	/**
	 * Extracts a zip entry (file entry)
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 */
	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	
}
