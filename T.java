import java.util.*;

class T{

	public static void main(String[] args){ 
		String [] tokens = args[0].split(";");
		List<String> list = new ArrayList<String>();
		int i = 0;
		for(String token: tokens){
			System.out.println(++i+", "+token);
			list.add( token );
		}
		System.out.println(args[1]+", "+list.contains( args[1] ));

	}	
}
