package cdac.in.jam.util;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;

public class PhotoManager{

	public static void main(String[] args){

		try{
			BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
			String line = null;
			boolean first = true;			
			int count  = 0;
			long otime = (new java.util.Date()).getTime();

			String registrationNo = null;
			String photoPath = null;
			String session = null;
			String zone = null;
			
			while((line = br.readLine()) != null ){
				try{
					String[] token = line.split(",");
					registrationNo = token[0].trim();
					photoPath = token[1].trim();
					session = registrationNo.substring(7,8);

					String dir = "signature/S"+session.trim();
					File theDir = new File(dir);
					if (!theDir.exists()) {
						boolean result = theDir.mkdirs();  
						if( result ){
							System.out.println("DIR created "+dir);  
						}else{
							System.out.println(dir+" not created!");
						}
					}
					String sourcePath = photoPath.substring(9);
					String descPath = dir+"/"+registrationNo;
					System.out.println("cp -uv "+sourcePath+" "+descPath);
					count++;
					if( count % 100 == 0){
						long ctime = (new java.util.Date()).getTime();
						System.out.println("echo \""+count+" done! \""); 
					}
				}catch(Exception e){
					System.out.println("Source "+ photoPath+" does not existSource ");
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
