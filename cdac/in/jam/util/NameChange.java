package cdac.in.jam.util;


import java.util.*;
import java.io.*;


class NameChange{

	static HashMap<String, String> names = new HashMap<String, String>();

	public static void main(String args[]){
		try{
			String candidateFile =  null;
			String changeFile = null;
			int i = 0 ;
			while ( i < args.length ){
				if( args[i].equals("-can") ){
					candidateFile =  args[i+1];
					i++;	
				}else if( args[i].equals("-ch") ){
					changeFile =  args[i+1];
					i++;	
				}
			i++;
			}

			if( candidateFile == null || changeFile == null){
				System.out.println("Uses java cdac.in.util.NameChange -can <candidatefile> -ch <change-request-file>");
				System.exit(0);
			}
		
			BufferedReader br = new BufferedReader( new FileReader(changeFile) );
			String line = null;
			while( ( line = br.readLine() ) != null ){
				String[] tk = line.split(",");
				names.put( tk[1].trim(), tk[2].trim() );
			}
			br = new BufferedReader( new FileReader(candidateFile) );
			line = null;
			while( ( line = br.readLine() ) != null ){
				String[] tk = line.split("\"?(,|$)(?=(([^\"]*\"){2})*[^\"]*$) *\"?");
				String regNo = tk[2].substring(2);
				String name = names.get( regNo.trim() );
				if( name != null ){
					System.out.print( tk[0]+","+tk[1]+","+tk[2]+","+tk[3]+","+tk[4]+","+name);
					for( i = 6; i < tk.length; i++){
						if( tk[i].indexOf(",") >=0 ){
							System.out.print(",\""+tk[i].replaceAll("\"","")+"\"");
						}else if ( tk[i].trim().length() == 0 ) {
							System.out.print(",\"\"");
						}else{
							System.out.print(","+tk[i]);
						}
					}
					System.out.println();
					
				}else{
					System.out.println(line);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
