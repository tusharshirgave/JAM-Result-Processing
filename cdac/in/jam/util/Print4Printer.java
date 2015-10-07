package cdac.in.jam.util;

import java.io.*;

import cdac.in.util.QRCodeGenerator;

class Print4Printer{

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
				String tk[] = line.split("\"?(,|$)(?=(([^\"]*\"){2})*[^\"]*$) *\"?");

				System.out.print( tk[0]+", ");
				System.out.print( QRCodeGenerator.getCode()+", ");
				System.out.print( tk[2]+", "+tk[3]+", "+tk[4]+", "+tk[5]+", "+tk[6]+", "+tk[7]+", "+tk[8]+", "+tk[9]+",");
				Double d = new Double( tk[10] );
				System.out.print( String.format("%.2f", d.doubleValue() ) +", ");

				if( tk[11].indexOf( "Not Applicable" ) >= 0 )
					System.out.print( tk[11]+", ");
				else{
					d = new Double( tk[11] );
					System.out.print( String.format("%.2f", d.doubleValue() )+", ");
				}

				System.out.print( tk[12]+", "+tk[13]+", ");

				if( tk[14].indexOf(",") >=0 )
					System.out.print("\""+tk[14]+"\", ");
				else
					System.out.print(tk[14]+", ");

				if( tk[15].indexOf(",") >=0 )
					System.out.print("\""+tk[15]+"\", ");
				else
					System.out.print(tk[15]+", ");

				if( tk[16].indexOf(",") >=0 )
					System.out.print("\""+tk[16]+"\", ");
				else
					System.out.print(tk[16]+", ");

				System.out.print(tk[17]+", "+tk[18]+", "+tk[19]+", "+tk[20]+", "+tk[21]+", "+tk[22]+", ");

				d = new Double( tk[23] );
				System.out.print( String.format("%.2f", d.doubleValue() )+", ");
				d = new Double( tk[24] );
				System.out.print( String.format("%.2f", d.doubleValue() )+", ");
				d = new Double( tk[25] );
				System.out.println( String.format("%.2f", d.doubleValue() ));
			}

		}catch(Exception e){
			e.printStackTrace();
		}


	}

}
