package cdac.in.util;

import java.io.*;
import javax.activation.MimetypesFileTypeMap;
import java.net.FileNameMap;
import java.net.URLConnection;

public class PhotoNameExt{

	public static String getContentType(String filename) {

		System.out.println( filename );
		File file = new File( filename );
		return new MimetypesFileTypeMap().getContentType(file) ;
	}

	static void printFiles(File folder){

		File[] listOfFiles = folder.listFiles();
		for(int i = 0;  i < listOfFiles.length; i++){
			if( listOfFiles[i].isDirectory() ){
			     printFiles( listOfFiles[i] );
			}else{

				try{
					System.out.println(  getContentType( listOfFiles[i].getCanonicalPath() ) );
					String [] tk = listOfFiles[i].getName().split("\\.");
					if( tk.length < 2)
						continue;
					System.out.println(tk[0]+", "+tk[1]);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args){
	
		String filePath = null;
		int i = 0;
		while( i < args.length ){
			if( args[i].equals("-p")){
				filePath = args[i+1];
			}
		i++;
		}
		if( filePath == null ){
			System.out.println("Uses: java cdac/in/util/PhotoNameExt -p <folder-path> ");
			System.out.println("ex: java cdac/in/util/PhotoNameExt -p ./photo ");
			System.out.println("ex: java cdac/in/util/PhotoNameExt -p ./signature ");
			System.exit(0);
		}	

		File  folder = new File( filePath );
		printFiles( folder );
	}

} 
