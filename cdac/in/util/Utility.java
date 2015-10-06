package cdac.in.util;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class Utility{

	public static void main(String[] args){

		String filename =  null;
		String sourcePath =  null;

		try{
			int  i = 0 ;
			while( i < args.length ){
				if( args[i].equals("-f") )
					filename = args[i+1].trim();
				if( args[i].equals("-s") )
					sourcePath = args[i+1].trim();
			i++;	
			}
			if( filename == null || sourcePath == null){
				System.out.println("uses java cdac.in.util.Utility -f <filename>  -s <souce-photo-signature-directory>");
				System.exit(0);
			}

			File theDir = new File("photo");
			if (!theDir.exists()) {
				boolean result = theDir.mkdir(); 
				if( result ){
					System.out.println(theDir+" directory created");
					for( i = 1; i < 10; i++){
						theDir = new File("photo/S"+i);
						result = theDir.mkdir(); 
						if( result )
							System.out.println(theDir+" created");
					} 
				}
			}	

			theDir = new File("signature");
			if (!theDir.exists()) {
				boolean result = theDir.mkdir(); 
				if( result ){
					System.out.println(theDir+" directory created");
					for( i = 1; i < 10; i++){
						theDir = new File("signature/S"+i);
						result = theDir.mkdir(); 
						if( result )
							System.out.println(theDir+" created");
					} 
				}
			}	


			BufferedReader br = new BufferedReader(new FileReader(new File( filename ) ) );
			String line = null;
			i = 0;

			while( ( line = br.readLine() ) != null ){
				String[] token = line.split(",");
				String enrollment = token[1].trim() + token[0].trim();
				String session =  enrollment.substring(7,8); 
				String photoPath = "photo/S"+session+"/"+enrollment;
				String sigPath = "signature/S"+session+"/"+enrollment;

				System.out.println("cp -vu "+sourcePath+""+photoPath+" "+photoPath);
				System.out.println("cp -vu "+sourcePath+""+sigPath+" "+sigPath);

			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
} 
