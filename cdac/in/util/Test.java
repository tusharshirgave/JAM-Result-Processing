import java.text.DecimalFormat;

class Test{

		public static void main(String[] args){

				double mean = 5.9;
				double stdDev = 3.2; 

               // double [] factor = {0.1, 0.2, 0.3, 0.4, 0.5, 0, -0.1, -0.2, -0.3, -0.4, -0.5, -0.6, -0.7, -0.8, -0.9};
                double [] factor = {0.5, 0.4, 0.3, 0.2, 0.1, 0, -0.1, -0.2, -0.3, -0.4, -0.5, -0.6, -0.7, -0.8, -0.9};

                for(int i = 0; i < factor.length; i++){

                        double Gx = Double.parseDouble( new DecimalFormat("#0.0#").format( mean + factor[i] * stdDev ) );
                        double Ox = Double.parseDouble( new DecimalFormat("#0.0#").format( Gx * 0.9 ) );
                        double SSPx  = Double.parseDouble( new DecimalFormat("#0.0#").format( Gx * 0.5) );
						System.out.println( Gx+", "+Ox+", "+SSPx);
				}
	 } 

}
