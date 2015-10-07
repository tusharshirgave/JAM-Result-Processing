package cdac.in.jam.util;

/**
 * @author  shekhar@cdac.in
 * @date 08/03/2014
 **/

import java.util.ArrayList;
import java.util.Random;
import java.util.List;


public class QRCodeGenerator{

	private static List<String> listOfCode = new ArrayList<String>();
	private static final char[] symbols = new char[36];
	private static int length =  5;	

	private static final Random random = new Random();
	private static final char[] buf;

	static {
		for(int i = 0; i < 10; i++)
			symbols[i] = (char) ('0' + i );
		for(int i = 10; i < 36; i++)
			symbols[i] = (char) ('A' + (i - 10) );
		buf = new char[length];
	}

	static boolean ignoreCode( String code ){
		if(code.charAt(0) == '0' || code.indexOf("XX") >= 0 || code.length() != 5 )
			return true;
		for(int i = 0;  i < code.length(); i++){
			if( !("0123456789".indexOf(code.charAt(i) ) >= 0 ) )
				return false;
		}
	return true;	
	}

	public static String getCode() {
		boolean flag =  true;
		while( true ){
			for(int i = 0; i < buf.length; i++)
				buf[i] = symbols[ random.nextInt( symbols.length) ];
			String code =  new String(buf);
			if( listOfCode.contains(code) || ignoreCode( code.trim() ) )
				continue;
			listOfCode.add(code);
			return code;
		}
	}
	
	public static void main(String[] args){
		try{
			for( int i = 0; i < 300000; i++){
				System.out.println((i+1)+" "+getCode() );
			}

		}catch(Exception e){
			e.printStackTrace();

		}	
	}
	

}
