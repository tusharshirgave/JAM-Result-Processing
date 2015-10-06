package cdac.in.util;

import java.io.*;

class Print4Zone{

	public static void main(String args[]){

		try{

			BufferedReader br = new BufferedReader( new InputStreamReader(System.in) );
			String line = null;
			boolean first = true;
			while( (line = br.readLine() ) != null ){

				if( first ){
					System.out.println(line);
					first = false;
					continue;
				}

				String tk[] = line.split(",");

				for(int i = 0; i < 10; i++  )
					System.out.print(tk[i]+", ");

				Double d = new Double( tk[10] );
				System.out.print( String.format("%.2f", d.doubleValue() )+", ");
				
				if( tk[11].indexOf( "Not Applicable" ) >= 0 )
					System.out.print( tk[11]+", ");
				else{
					d = new Double( tk[11] );
					System.out.print( String.format("%.2f", d.doubleValue() )+", ");
				}
				for(int i = 12; i < 20; i++  )
					System.out.print(tk[i]+", ");

				d = new Double( tk[20] );
				System.out.print( String.format("%.2f", d.doubleValue() )+", ");
				d = new Double( tk[21] );
				System.out.print( String.format("%.2f", d.doubleValue() )+", ");
				d = new Double( tk[22] );
				System.out.println( String.format("%.2f", d.doubleValue() )+", ");
			}

		}catch(Exception e){
			e.printStackTrace();
		}


	}

}
