package wwr.controllers.rest.secure;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import wwr.model.enities.UserDocument;

public class UserDocFileIoController {
	
	static String baseDirectory = System.getProperty( "user.home" ) + "/wwr/user_files/" ;
	
	private static String getBasePath( UserDocument udo ){
		return baseDirectory + "/" + udo.getOwner().getNickname().toLowerCase() +"/" ;
	}
	
	
	private static String getInternalFileName( UserDocument udo ){
		String fileExt = "jpg" ;
		String filename = udo.getFilename() ;
		String[] myparts = filename.split("\\.") ;
		if( myparts.length > 1 ){
			fileExt = myparts[ myparts.length -1 ] ; 
		}
		StringBuilder sb = new StringBuilder("udo") ;
		sb.append( udo.getId().toString() ) ;
		sb.append( "." ) ;
		sb.append( fileExt ) ; 
		return sb.toString() ; 
	}
	
	public static byte[] readFile( UserDocument udo ) throws IOException {
		Path path = Paths.get( getBasePath(udo)  +"/"+ getInternalFileName( udo ));
		return Files.readAllBytes(path ) ;		
	}
	
	public static void writeFile( UserDocument udo , byte[] data ) throws IOException {

      File dir = new File(getBasePath(udo));
      if (! dir.isDirectory()){
    	  new File(getBasePath(udo)).mkdirs() ;    	  
      }
		
		String fullpathFilename = getBasePath(udo) +  getInternalFileName(udo);		
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File( fullpathFilename ) ));
	    stream.write(data);
	    stream.close();
	}
	

	public static void deleteFile( UserDocument udo ) throws IOException {
		String fullpathFilename = getBasePath(udo) +  getInternalFileName(udo);
		File file = new File(fullpathFilename);
		if( file.exists()){
			file.delete() ;			
		}		
	}
	
	
	
	
}
