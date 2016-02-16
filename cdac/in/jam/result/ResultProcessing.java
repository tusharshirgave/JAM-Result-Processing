package cdac.in.jam.result;

import java.text.DecimalFormat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.math.BigInteger;
import java.security.MessageDigest;

import cdac.in.jam.result.StdStats;
import cdac.in.jam.util.QRCodeGenerator;

/**
 * 
 * This class capture the Qualifilying Marks for each category
 * @author Chandra Shekhar 
 * @version 0.2
 * @date 10/02/2016
 *
 **/

class QualifyingMarks{

	int total;
	int gen;
	int obc;
	int sc;
	int st;

	QualifyingMarks( int total, int gen, int obc, int sc, int st){

		this.total =  total;
		this.gen =  gen;      
		this.obc =  obc;
		this.sc =  sc;
		this.st =  st;
	}
}


/**
 *  
 * This class contains the configuration parameter for the 
 * Overall result processing program. 
 *
 * @author Chandra Shekhar
 * @version 0.1 	
 * @date 27/03/2014
 *
 **/

class Config {

	public static String negative = "3";
	public static String cancelled = "zero";
	public static String unattempted = "zero";	
	public static String invalidResponse = "zero";

}


/**
 *  
 * This class contains the Print configuration parameter for the 
 * Overall result processing program. 
 *
 * @author Chandra Shekhar
 * @version 0.1 	
 * @date 27/03/2014
 *
 **/


class Print{

	static boolean actual = false;
	static boolean detail = false;
	static boolean analysis = false;
	static boolean info = false;
	static boolean multiSession = false;
	static boolean resultView = false;
	static boolean scoreView = false;
}


/**
 *  
 *  CodeMapping class just create the mapping of the Paper Codes
 *  and Section code into the hash map, which can be use for final printing
 *	
 * @author Chandra Shekhar
 * @version 0.1 	
 * @date 27/03/2014
 *
 **/


class CodeMapping{

	static HashMap<String, String> paperCodeMap = new HashMap<String, String>();
	static HashMap<String, String> sectionCodeMap = new HashMap<String, String>();

	static{

		/*  Paper Code Mapping */

		paperCodeMap.put("GG", "Geology");			 	
		paperCodeMap.put("MA", "Mathematics");	 			
		paperCodeMap.put("BT", "Biotechnology");	 		
		paperCodeMap.put("PH", "Physics");			 			
		paperCodeMap.put("CY", "Chemistry");						
		paperCodeMap.put("MS", "Mathematical Statistics");
		paperCodeMap.put("BL", "Biological Sciences");


		/*  Section Code Mapping */

		sectionCodeMap.put("1", "Geology");
		sectionCodeMap.put("2", "Geophysics");
	}	
}

/**
 *  
 * DigitalSignature is a private class use for creating Digital Signature for candidate
 * using the candidate's field, this use for verifying the candidate information from intantioanl 
 * tempering of the data.
 *
 * @author Chandra Shekhar
 * @version 0.1 	
 * @date 27/04/2014
 *
 *
 **/

class DigitalSignature{

	static MessageDigest md = null;

	static {	
		try{
			md = MessageDigest.getInstance("MD5");
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	/** 
	 * This static method will return the fingerprint(MD5 hashed of the message) string of the message 
	 * composed of the following data belong to candidate.  
	 * paperCode, rollNumber, name, category, gender, rawMark, JAMScore, rank
	 * This method also add a salt as '2014gAtE' in the end
	 *
	 * @param message   candidate information into a single string(paperCode, rollNumber, name, category, gender, rawMark, JAMScore, rank). 
	 * @return          MD5 hashed of message + '2014gAtE'  
	 * @since           1.0
	 */

	public static String getDigitalSignature(String message){

		String salt = "2016jAm";
		message = message+""+salt;
		md.reset();
		md.update( message.getBytes() );
		byte[] theDigest =  md.digest();
		BigInteger bigInt = new BigInteger( 1, theDigest );
		String hashtext = bigInt.toString(16);

		return hashtext;
	}
}

/**
 *  
 * This class is for Candidate response which store the candidate answer in 'answer' for Multipal Choice Question
 * and 'Options' in case of  for Range Type Question.
 *
 * @author Chandra Shekhar
 * @version 0.1 	
 * @date 27/04/2014
 *
 *
 **/

class Response{

	String answer;
	String options;

	/** 
	 * Response Class constructor.
	 * @param answer candidate answer
	 * @param obtions option id for MCQ type & answer in case of Range Type
	 */

	Response(String answer, String options){
		this.answer = answer.trim();
		this.options = options.trim();
	}

	String getAnswer(){
		return answer;
	}

	String getOptions(){
		return options;
	}
}


/**
 *  
 * This is a abstract class Question which can be exteded by the actual 
 * this provide the abstract method as eval() & print() which can be actully 
 * implemented by the class who will be extending this class. 
 * Using this more question type can be supported by the system. Just need to extedns the 
 * new question type in the system.	 
 *
 * @author Chandra Shekhar
 * @version 0.1 	
 * @date 27/03/2014
 *
 **/

abstract class Question{

	String Id;
	double mark;
	boolean isCancelled;	
	String section;
	String DL;
	double negative;
	double unattempted;
	double invalidResponse;

	int R;
	int NA;
	int W;
	int AT;

	double perR;
	double perW;
	double perNA;
	double perRAT;

	abstract double eval(Response response, Candidate candidate);
	abstract void printLog( boolean flag );
	abstract void print();
	abstract String getAnswers();
	abstract String type();
	abstract String getDL();	
}


/**
 *  
 * This is MultipalChocie Question class Excending the 'Question' and proving the implemention of the 
 * Eval() and Print() method 
 *
 * @author Chandra Shekhar
 * @version 0.1 	
 * @date 27/03/2014
 *
 **/

class MultipalChocie extends Question{

	List<String> answers;
	private boolean marksToAll;
	static String options = "ABCD";


	/** 
	 * 
	 * MultipalChocie Class constructor which initialised the question and assign the marks for
	 * correct answer, Wrong answer, invalid answer based on the configuration on 'Config' Object.
	 * 
	 * @param Id Question Id 
	 * @param section section of the question
	 * @param answer answer of the question 
	 * @param marks marks of the question if the answer is correct 
	 *
	 */


	MultipalChocie(String Id, String section, String answer, String mark){

		this.Id = Id;
		this.section = section;
		this.isCancelled = false;
		this.R = 0;
		this.NA = 0;
		this.W = 0;	
		this.AT = 0;
		this.perR = 0.0d;
		this.perW = 0.0d;
		this.perNA = 0.0d;
		this.perRAT = 0.0d;
		this.DL = null;
		this.marksToAll = false;
		this.answers = new ArrayList<String>();

		if( answer.equalsIgnoreCase("can") ){
			this.isCancelled = true;
		}else{

			if( answer.equals("MTA") ){
				marksToAll = true;
			}else{	
				String[] tokens =  answer.split(";");
				if( tokens.length > 0){
					for(String token: tokens){
						answers.add( token.trim() );	
					}	
				}else{
					System.err.println("Master key has error (MCQ) "+answer+" Question ID: "+Id);
					System.exit(0);
				}
			}

			this.mark = Double.parseDouble( mark );

			if( Config.negative.equals("zero") ){
				this.negative = 0.0d;
			}else{ 

				this.negative = -1 * ( this.mark / Integer.parseInt( Config.negative ) );
			}

			if( Config.unattempted.equals("zero") ){
				this.unattempted = 0.0d;
			}else{
				this.unattempted = ( this.mark / Integer.parseInt( Config.unattempted ) );
			}

			if( Config.invalidResponse.equals("zero") ){
				this.invalidResponse = 0.0d;
			}else{
				this.invalidResponse = ( this.mark / Integer.parseInt( Config.invalidResponse ) );
			}

		}
	}      					

	/** 
	 * 
	 * This is eval method of the multipal choice question against a candidate response 
	 * This method evalulate the correctness of the candidate response against question 
	 * cottect answer and return the marks depending on the correct answer, wrong answer and invalid Response.
	 * 
	 * @param Id Question Id 
	 * @param section section of the question
	 * @param answer answer of the question 
	 * @param marks marks of the question if the answer is correct 
	 **/

	double eval( Response response, Candidate candidate ){
		try{	
			if( response == null ){
				System.err.println("1. Error in response (MCQ) Question ("+Id+") "+response.getOptions()+" "+response.getAnswer()+" for :"+candidate.rollNumber);
				System.exit(0);
			}

			if( isCancelled ){

				if( Config.cancelled.equals("zero") ){
					return 0.0d;
				}else{
					return ( this.mark / Integer.parseInt( Config.cancelled ) );
				}

			}else if( this.marksToAll ) {

				this.AT++;
				this.R++;
				return mark;

			}else if( response.getAnswer().equals("--") ){   
				this.NA++;
				return unattempted;

			}else if( options.indexOf( response.getAnswer() ) < 0 ){
				System.err.println("2. Error in response (MCQ) Question ("+Id+") "+response.getOptions()+" "+response.getAnswer()+" for :"+candidate.rollNumber);
				System.exit(0);

			}else if( this.answers.contains( response.getAnswer() ) ){
				this.AT++;
				this.R++;
				return mark;
			}else{
				this.AT++;
				this.W++;
				return negative;
			}

		}catch(Exception e){
			System.err.println("3. Error in response (MCQ) Question ("+Id+") "+response.getOptions()+" "+response.getAnswer()+" for :"+candidate.rollNumber);
			System.exit(0);
		}

		this.AT++;
		return this.invalidResponse;
	}

	void print(){

		System.out.print("[MCQ"+Id+", "+getAnswers()+", ");
		FP.printD(mark);
		System.out.print(", ");
		FP.printD(negative);
		System.out.print(", ");
		FP.printD(unattempted);
		System.out.print("]");
	}

	void printLog(boolean flag){
		System.out.println("R:"+this.R+"# TY:"+type()+" # NA:"+this.NA+" # W:"+W+" # %R:"+perR+" # %W:"+perW+" # %NA:"+perNA+" # %R/A:"+perRAT+" # DL:"+DL);
	}

	String getDL(){
		return DL;
	}

	String getAnswers(){

		if( marksToAll )
			return "MTA";

		String tanswer = "";
		boolean flag = true;

		for(String ans: answers){
			if( flag ){
				flag = false;
				tanswer = ans;
			}else{
				tanswer += ";"+ans;
			}
		}
		return tanswer;
	}

	String type(){
		return "MCQ";
	}
}

/**
 *  
 * This is MultipalAnswer Question class Excending the 'Question' and proving the implemention of the 
 * Eval() and Print() method 
 *
 * @author Chandra Shekhar
 * @version 0.1 	
 * @date 09/02/2015
 *
 **/

class MultipalAnswer extends Question {

	private List<String> answers;
	private boolean marksToAll;
	private static String options = "A;B;C;D;A;B;D;A;C;D;A;D;B;D";

	/** 
	 * 
	 * MultipalChocie Class constructor which initialised the question and assign the marks for
	 * correct answer, Wrong answer, invalid answer based on the configuration on 'Config' Object.
	 * 
	 * @param Id Question Id 
	 * @param section section of the question
	 * @param answer answer of the question 
	 * @param marks marks of the question if the answer is correct 
	 *
	 **/


	MultipalAnswer(String Id, String section, String answer, String marks){

		this.Id = Id;
		this.section = section;
		this.isCancelled = false;
		this.R = 0;
		this.NA = 0;
		this.W = 0;	
		this.AT = 0;
		this.perR = 0.0d;
		this.perW = 0.0d;
		this.perNA = 0.0d;
		this.perRAT = 0.0d;
		this.DL = null;
		this.marksToAll = false;

		this.answers = new ArrayList<String>();
		this.mark = Double.parseDouble( marks );
		this.negative = 0.0d;

		if( answer.equalsIgnoreCase("can") ){
			this.isCancelled = true;
		}else{
			if( answer.equals("MTA") ){
				this.marksToAll = true;
			}else{	
				String[] tokens = answer.split("#");
				for(String token: tokens){
					this.answers.add( token.trim() );
				}

				if(answers.size() <= 0){
					System.err.println("Error in master key creation: "+answer+" Question: "+Id);
					System.exit(0);
				}
			}

			if( Config.unattempted.equals("zero") ){
				this.unattempted = 0.0d;
			}else{
				this.unattempted = ( this.mark / Integer.parseInt( Config.unattempted ) );
			}

			if( Config.invalidResponse.equals("zero") ){
				this.invalidResponse = 0.0d;
			}else{
				this.invalidResponse = ( this.mark / Integer.parseInt( Config.invalidResponse ) );
			}

		}
	}      					

	/** 
	 * 
	 * This is eval method of the multipal choice question against a candidate response 
	 * This method evalulate the correctness of the candidate response against question 
	 * cottect answer and return the marks depending on the correct answer, wrong answer and invalid Response.
	 * 
	 * @param Id Question Id 
	 * @param section section of the question
	 * @param answer answer of the question 
	 * @param marks marks of the question if the answer is correct 
	 **/

	double eval( Response response, Candidate candidate ){
		try{	
			if( response == null ){
				System.err.println("1. Error in response (MSQ) Question ("+Id+") options: <"+response.getOptions()+">  answer: <"+response.getAnswer()+"> for :"+candidate.rollNumber);
				System.exit(0);
			}

			if( isCancelled ){

				if( Config.cancelled.equals("zero") ){
					return 0.0d;
				}else{
					return ( this.mark / Integer.parseInt( Config.cancelled ) );
				}

			}else if( marksToAll ){

				this.AT++;
				this.R++;
				return mark;

			}else if( response.getAnswer().equals("--") ){   
				this.NA++;
				return unattempted;

			}else if( options.indexOf( response.getAnswer() ) < 0 ){       
				System.err.println("2. Error in response (MSQ) Question ("+Id+") "+response.getOptions()+" "+response.getAnswer()+" for :"+candidate.rollNumber);
				System.exit(0);

			}else if( this.answers.contains( response.getAnswer() ) ) {  
				this.AT++;
				this.R++;
				return mark;
			}else{
				this.AT++;
				this.W++;
				return negative;
			}

		}catch(Exception e){
			System.err.println("3. Error in response (MSQ) Question ("+Id+") "+response.getOptions()+" "+response.getAnswer()+" for :"+candidate.rollNumber);
			System.exit(0);
		}

		this.AT++;
		return this.invalidResponse;
	}

	void print(){

		System.out.print("[MSQ "+this.Id+", "+this.getAnswers()+", ");
		FP.printD(mark);
		System.out.print(", ");
		FP.printD(negative);
		System.out.print(", ");
		FP.printD(unattempted);
		System.out.print("]");
	}

	void printLog(boolean flag){
		System.out.println("R:"+this.R+"# TY:"+type()+" # NA:"+this.NA+" # W:"+W+" # %R:"+perR+" # %W:"+perW+" # %NA:"+perNA+" # %R/A:"+perRAT+" # DL:"+DL);
	}

	String getDL(){
		return DL;
	}

	String getAnswers(){

		if( marksToAll )
		return "MTA";

		String tanswer = "";
		boolean flag = true;
		for(String ans: answers){
			if( flag ){
				flag = false;
				tanswer = ans;	
			}else{
				tanswer += "#"+ans;
			}	
		}
		return tanswer.trim();
	}

	String type(){
		return "MSQ";
	}
}

/**
 *  
 * This is Range Answer Question class Excending the 'Question' and proving the implemention of the 
 * Eval() and Print() method 
 *
 * @author Chandra Shekhar
 * @version 0.1 	
 * @date 09/02/2015
 **/


class Rang{
	
	private double lower;
	private double upper;

	Rang(double lower, double upper){
		this.lower = lower;
		this.upper = upper;
	}

	boolean eval(double value){
		if( value >= lower && value <= upper )
			return true;
		return false;
	}

	String getRang(){
		return lower+":"+upper;
	}
}

class RangeQuestion extends Question {

	private List<Rang> answers;
	private boolean marksToAll;

	/** 
	 * 
	 * RangeQuestion Class constructor which initialised the question and assign the marks for
	 * correct answer, Wrong answer, invalid answer etc.
	 * 
	 * @param Id Question Id 
	 * @param section section of the question
	 * @param answer answer of the question 
	 * @param marks marks of the question if the answer is correct 
	 *
	 */

	RangeQuestion( String Id, String section, String answer, String mark){

		this.Id = Id;
		this.section = section;
		this.isCancelled = false;
		this.R = 0;
		this.NA = 0;
		this.W = 0;
		this.AT = 0;
		this.perR = 0.0d;
		this.perW = 0.0d;
		this.perNA = 0.0d;
		this.perRAT = 0.0d;
		this.DL = null;

		this.marksToAll = false;
		this.answers = new ArrayList<Rang>();

		this.mark = Double.parseDouble( mark );
		this.negative = 0.0d;

		if( answer.equalsIgnoreCase("can") ){

			this.isCancelled = true;
		}else{

			if( "MTA".equals( answer.trim() ) ) {
				this.marksToAll = true;
			}else{	
				String[] tokens = answer.split(";");
				for(String token: tokens){

					String []rang = token.split(":");
					if( rang.length != 2) {                                          
						System.err.println("Error in master key creation: "+answer+" Question: "+Id);
						System.exit(0);
					}

					double lower = Double.parseDouble( rang[0] );
					double upper= Double.parseDouble( rang[1] );	

					if( lower <= upper ){
						answers.add( new Rang(lower, upper) );
					}else{
						answers.add( new Rang(upper, lower) );
					}
				}

				if(answers.size() <= 0){
					System.err.println("Error in master key creation: "+answer+" Question: "+Id);
					System.exit(0);
				}
			}	

			if( Config.unattempted.equals("zero") ){
				this.unattempted = 0.0d;
			}else{
				this.unattempted = ( this.mark / Integer.parseInt( Config.unattempted ) );
			}

			if( Config.invalidResponse.equals("zero") ){
				this.invalidResponse = 0.0d;
			}else{
				this.invalidResponse = ( this.mark / Integer.parseInt( Config.invalidResponse ) );
			}
		}
	}

	double eval(Response response, Candidate candidate){

		if( response == null ){
			System.err.println("1. Error in response (NAT) Question ("+Id+") "+response.getOptions()+" "+response.getAnswer()+" for :"+candidate.rollNumber);
			System.exit(0);
		}

		if( isCancelled ){

			if( Config.cancelled.equals("zero") ){
				return 0.0d;
			}else{
				return ( this.mark / Integer.parseInt( Config.cancelled ) );
			}

		}else if( this.marksToAll ){

                        this.AT++;
                        this.R++;
                        return mark;

		}else if ( response.getAnswer().equals("--") && !response.getOptions().equals("--")){  

			System.err.println("2. Error in response (NAT) Question ("+Id+") "+response.getOptions()+" "+response.getAnswer()+" for :"+candidate.rollNumber);
			System.exit(0);

		}else if( response.getAnswer().equals("--") ){
			this.NA++;
			return this.unattempted;
		}

		try{
			Double.parseDouble( response.getOptions() );
		}catch(Exception e){
			System.err.println( "Exception in NAT( "+Id+" ) for "+candidate.rollNumber+": <"+response.getOptions()+"> Preprocessed Response: <"+sanitizeNATResponse( response.getOptions() )+">");
		}

		try{
			double resp =  Double.parseDouble( sanitizeNATResponse( response.getOptions() ) ); 

			for(Rang rang: this.answers){
				if( rang.eval( resp ) ){
					this.R++;
					this.AT++;
					return this.mark;
				}
			}

			this.AT++;
			this.W++;
			return this.negative;                 

		}catch(Exception e){

			this.AT++;
			return this.invalidResponse;
		}	
	}

	String sanitizeNATResponse(String response)
	{
		response = response.trim();

		if(response.contains("http"))
		{
			response = response.replaceAll("http.*", "");
		}
		if(response.endsWith(".") && response.length() > 1)
		{
			response = response.substring(0, response.lastIndexOf("."));
		}
		if(response.endsWith("-") && response.length() > 1)
		{
			response = response.substring(0, response.lastIndexOf("-"));
		}
		if(".".equals(response))
		{
			response = "";
		}
		if("-".equals(response))
		{
			response = "";
		}

		return response;
	}


	void print(){

		System.out.print("[NAT"+Id+", ("+getAnswers()+"), ");
		FP.printD(mark);
		System.out.print(", ");
		FP.printD(negative);
		System.out.print(", ");
		FP.printD(unattempted);
		System.out.print("] ");
	}

	void printLog(boolean flag){
		System.out.println("R:"+this.R+" # TY:"+type()+" # NA:"+this.NA+" # W:"+W+" # %R:"+perR+" # %W:"+perW+" # %NA:"+perNA+" # %R/A:"+perRAT+" # DL:"+DL);
	}

	String getAnswers(){
		if( marksToAll )
			return "MTA";

		String tanswer = "";
		boolean flag = true;
		for(Rang rang: answers){
			if( flag ){
				flag = false;
				tanswer = rang.getRang();	
			}else{
				tanswer += ";"+rang.getRang();
			}	
		}
		return tanswer.trim();
	}

	String getDL(){
		return DL;
	}

	String type(){
		return "NAT";
	}
}

class FP{

	public static void printD(double value){
		System.out.printf("%.2f",value);
	}

	public static String prints(double value){
		String output = String.format("%.2f", value);
		return output.trim();
	}


}

class Session{

	ArrayList <Double> listOfObtainedMarks;
	ArrayList <Double> listOfActualMarks;
	ArrayList <Double> listOfActualMarksGreterZero;

	ArrayList <Question> listOfQuestions;
	ArrayList <Candidate> listOfCandidate;

	String id;
	double mTBar;
	double mQ;
	double mean;
	double stdDev;
	double maxRawScore;
	double minRawScore;

	int DL1;
	int DL2;
	int DL3;
	int DL4;
	int DL5;

	int zeroPointOnePercent;

	Session(String id){

		this.id = id;
		this.listOfQuestions = new ArrayList<Question>();
		this.listOfObtainedMarks = new ArrayList<Double>();
		this.listOfActualMarks = new ArrayList<Double>();
		this.listOfActualMarksGreterZero = new ArrayList<Double>();
		this.listOfCandidate = new ArrayList<Candidate>();

		this.mTBar = 0.0d;
		this.mQ = 0.0d;
		this.mean = 0.0d;
		this.stdDev = 0.0d;
		this.zeroPointOnePercent = 0;
		this.maxRawScore = 0.0d;
		this.minRawScore = 0.0d;

		this.DL1 = 0;
		this.DL2 = 0;
		this.DL3 = 0;
		this.DL4 = 0;
		this.DL5 = 0;
	}	

	void calStats(){

		/*
		   Collections.sort( listOfObtainedMarks, Collections.reverseOrder() );
		   double [] marks = StdStats.toArray( listOfObtainedMarks );
		   this.zeroPointOnePercent = ( int ) Math.ceil( ( (double) marks.length) / 1000 );
		   this.mean = StdStats.mean( marks );
		   this.stdDev = StdStats.stddev( marks );
		   this.mTBar = StdStats.mean( marks, 0, zeroPointOnePercent - 1 );
		   this.mQ = this.mean + this.stdDev;	
		   double [] amarks = StdStats.toArray( listOfActualMarks );
		   this.maxRawScore = StdStats.max( amarks );
		   this.minRawScore = StdStats.min( amarks );
		 */

		Collections.sort( listOfActualMarksGreterZero, Collections.reverseOrder() );
		double [] marks = StdStats.toArray( listOfActualMarksGreterZero );

		this.zeroPointOnePercent = ( int ) Math.ceil( ( (double) marks.length) / 1000 );
		this.mean = StdStats.mean( marks );
		this.stdDev = StdStats.stddev( marks );

		this.mTBar = StdStats.mean( marks, 0, zeroPointOnePercent - 1 );
		this.mQ = this.mean + this.stdDev;	
		double [] amarks = StdStats.toArray( listOfActualMarks );
		this.maxRawScore = StdStats.max( amarks );
	}

	void print(){

		System.out.format(" ________________________________________%n");
		System.out.format("| Session ID             | %-13s |%n", id);
		System.out.format("|________________________|_______________|%n");
		System.out.format("| Total Candidates       | %-13d |%n", listOfObtainedMarks.size() );
		System.out.format("| 0.1 %% Candidate(s)     | %-13d |%n", zeroPointOnePercent );
		System.out.format("| Avg Of 0.1%%(s)         | %-13f |%n",mTBar );
		System.out.format("| Mean(s) + stdDev(s)    | %-13f |%n",mQ );
		System.out.format("| Postive)               | %-13d |%n", listOfActualMarksGreterZero.size() );
		System.out.format("| Mean(Postive)          | %-13f |%n",mean );
		System.out.format("| stdDev(Positive)       | %-13f |%n",stdDev );
		System.out.format("| max                    | %-13f |%n",maxRawScore );
		System.out.format("| min                    | %-13f |%n",minRawScore );
		System.out.format("|________________________|_______________|%n%n");

	}

	void printLog(){

		for(int i = 0;  i < this.listOfQuestions.size(); i++){

			Question question = this.listOfQuestions.get(i);

			question.perR =  Double.parseDouble( new DecimalFormat("#0.0#").format( (double) (100 / (double) this.listOfCandidate.size() ) * (double) question.R ) );
			question.perW =  Double.parseDouble( new DecimalFormat("#0.0#").format( (double) (100 / (double) this.listOfCandidate.size() ) * (double) question.W ) );
			question.perNA = Double.parseDouble( new DecimalFormat("#0.0#").format( (double) (100 / (double) this.listOfCandidate.size() ) * (double) question.NA ) );
			question.perRAT = Double.parseDouble( new DecimalFormat("#0.0#").format( (double) (100 / (double) question.AT ) * (double) question.R ) );	

			if( question.perR > 80.00d ){
				question.DL = "DL1";
				DL1++;
			}else if( question.perR >= 60.00d && question.perR <= 80.00d ){	 
				question.DL = "DL2";
				DL2++;
			}else if( question.perR >= 40.00d && question.perR <= 60.00d ){ 
				question.DL = "DL3";
				DL3++;
			}else if( question.perR >= 20.00d && question.perR <= 40.00d ){	 
				question.DL = "DL4";
				DL4++;
			}else if( question.perR < 20.00d ){	 
				question.DL = "DL5";
				DL5++;
			}
			System.out.print("Qn:"+(i+1)+" # ");
			if( i == this.listOfQuestions.size() -1)
				question.printLog( false );
			else
				question.printLog( true );
		}

		System.out.println();
		System.out.format(" ________________________________________%n");
		System.out.format("| D-Level Session        | %-13s |%n", id);
		System.out.format("|________________________|_______________|%n");
		System.out.format("| Total Candidates       | %-13d |%n", listOfObtainedMarks.size() );
		System.out.format("|________________________|_______________|%n");
		System.out.format("| DL1                    | %-13d |%n",DL1 );
		System.out.format("| DL2                    | %-13d |%n",DL2 );
		System.out.format("| DL3                    | %-13d |%n",DL3 );
		System.out.format("| DL4                    | %-13d |%n",DL4 );
		System.out.format("| DL5                    | %-13d |%n",DL5 );
		System.out.format("|________________________|_______________|%n%n");
	}
}

class Paper{

	String paperCode;
	Map<String, Session> sessionMap;
	ArrayList<Double> listOfObtainedMarks;
	ArrayList<Double> listOfNormalisedMarks;	
	ArrayList<Double> listOfActualNormalisedMarks;	
	ArrayList<Double> listOfActualMarks;	
	ArrayList<Double> listOfActualMarksGreterZero;	
	ArrayList<Candidate> listOfCandidate;
	static Map<String, QualifyingMarks> qulifyMark;

	double mTgBar;
	double mQg;
	double mQ;
	double mTBar;
	double mean;
	double stdDev;

	double maxRawMarks;
	double minRawMarks;
	double maxNorMarks;
	double minNorMarks;

	double genCutOff;
	double obcCutOff;
	double sTsCPwDCutOff;

	int maxOf10OR01per;
	int zeroPointOnePercent;
	boolean multiSession;

	int QGen;
	int QObc;
	int QSc;
	int QSt;
	int QPwD;
	int QGenX;
	int QObcX;
	int QPwDX;
	int QObcG;
	int QScG;
	int QStG;
	int QPwDG;

	int QGenObcB;

	int QSCObcB;
	int QSTObcB;
	int QPwDObcB;

	int QScX;
	int QStX;
	int notQ;
	int Q;


	double perQualified;
	double avgMarksQualified;
	double stdMarksQualified;

	int maleQ;
	int femaleQ;
	int otherQ;

	double perMaleQ;
	double perFemaleQ;
	double perOtherQ;

	double maxMarksMaleQ;
	double minMarksMaleQ;

	double maxMarksFemaleQ;
	double minMarksFemaleQ;

	double maxMarksOtherQ;
	double minMarksOtherQ;

	double maxMarksQualified;
	double minMarksQualified;

	int pwdG;
	int pwdO;
	int pwdSC;
	int pwdST;

	static int SQ = 350;
	static int ST = 900;


	static{

		qulifyMark = new HashMap<String, QualifyingMarks>();
		qulifyMark.put( "BL", new QualifyingMarks( 20, 10, 5, 3, 2 ) );
		qulifyMark.put( "BT", new QualifyingMarks( 89, 43, 24, 14, 8 ) );
		qulifyMark.put( "CY", new QualifyingMarks( 441, 219,118, 68, 36) );
		qulifyMark.put( "GG", new QualifyingMarks( 127, 65, 33, 19, 10 ) );
		qulifyMark.put( "MA", new QualifyingMarks( 412, 207, 111, 63, 31 ) );
		qulifyMark.put( "MS", new QualifyingMarks( 105, 53, 28, 15, 9 ) );
		qulifyMark.put( "PH", new QualifyingMarks( 406, 227, 123, 71, 39) );
	}


	Paper(String paperCode){

		this.sessionMap = new HashMap<String, Session>();
		this.listOfObtainedMarks = new ArrayList<Double>();
		this.listOfNormalisedMarks = new ArrayList<Double>();
		this.listOfActualNormalisedMarks = new ArrayList<Double>();	
		this.listOfCandidate = new ArrayList<Candidate>();
		this.listOfActualMarks = new ArrayList<Double>();
		this.listOfActualMarksGreterZero = new ArrayList<Double>();

		this.paperCode = paperCode;

		this.mTgBar = 0.0d;
		this.mQg = 0.0d;
		this.mQ = 0.0d;
		this.mTBar = 0.0d;
		this.mean = 0.0d;
		this.stdDev = 0.0d;

		this.genCutOff = 0.0d;
		this.obcCutOff = 0.0d;
		this.sTsCPwDCutOff = 0.0d;

		this.zeroPointOnePercent = 0;
		int maxOf10OR01per = 0;
		this.multiSession = false;

		this.maxRawMarks = 0.0d;
		this.minRawMarks = 0.0d;
		this.maxNorMarks = 0.0d;
		this.minNorMarks = 0.0d;

		this.QGen = 0;
		this.QObc = 0;
		this.QSc = 0;
		this.QSt = 0;
		this.QPwD = 0;

		this.QGenX = 0;
		this.QObcX = 0;
	
		this.pwdG = 0;
		this.pwdO = 0;
		this.pwdSC = 0;
		this.pwdST = 0;

		this.QObcG = 0;
		this.QScG = 0;
		this.QStG = 0;
		this.QPwDG = 0;
		this.QGenObcB = 0;
		this.QScX = 0;
		this.QStX = 0;
		this.QPwDX = 0;
		this.notQ = 0;

		this.QSCObcB = 0;
		this.QSTObcB = 0; 
		this.QPwDObcB = 0;
		this.Q = 0;

		this.perQualified = 0.0d;
		this.minMarksQualified = 0.0d;
		this.avgMarksQualified = 0.0d;
		this.stdMarksQualified = 0.0d;

		this.maleQ = 0;
		this.femaleQ = 0;
		this.otherQ = 0;

		this.perMaleQ = 0.0d;
		this.perFemaleQ = 0.0d;

		this.maxMarksMaleQ = 0.0d;
		this.minMarksMaleQ = 0.0d;

		this.maxMarksFemaleQ = 0.0d;
		this.minMarksFemaleQ = 0.0d;

		this.maxMarksOtherQ = 0.0d;
		this.minMarksOtherQ = 0.0d;

		this.maxMarksQualified = 0.0d;
		this.minMarksQualified = 0.0d;

	}

	void ranking(){

		Collections.sort( listOfCandidate, new RawMarksComp() );

		if( multiSession ){
			Print.multiSession = true;
			Collections.sort( listOfCandidate, new NorMarksComp() );
		}

		double oldMarks = 99999.0d;  
		double oldRatio = 0000.0d;  

		this.maxMarksMaleQ = -999.0d;
		this.minMarksMaleQ = 9999.0d;

		this.maxMarksFemaleQ = -999.0d;
		this.minMarksFemaleQ = 999.0d;

		this.maxMarksOtherQ = -999.0d;
		this.minMarksOtherQ = 999.0d;

		this.maxMarksQualified = -999.0d;
		this.minMarksQualified = 999.0d;

		ArrayList<Double> listOfQualifedMarks = new ArrayList<Double>();

		for(int i = 0, count = 1, rank = 1; i < listOfCandidate.size(); i++, count++){

			Candidate candidate = listOfCandidate.get(i);
			double marks = candidate.getRawMarks();
			double ratio = ( candidate.noP / candidate.noN ) ;	

			if( candidate.noN != 0)
				ratio = Double.parseDouble( new DecimalFormat("#0.0#").format( ratio ));

			if( multiSession ){
				marks = candidate.getNRMark();
			}

			if( oldMarks > marks ){   
				rank = count;
			}else if ( oldMarks == marks && oldRatio > ratio ){
				rank = count;
			}

			oldMarks = marks;
			oldRatio = ratio;
			candidate.rank = rank;

			if( candidate.info != null ){

				double score = candidate.getRawMarks();

				if( multiSession )	
					score = candidate.getNRMark();

				if( score >= obcCutOff ){

			
					if( candidate.info.category.equals("1") ){

						 if( score >= genCutOff ){
								candidate.isQualified = true;
						 }else{
								candidate.isQualified = false;
						 }
								
					}else{

						candidate.isQualified = true;
					}

					if( candidate.info.category.equals("1")  && score >= genCutOff ){
						QGenX++;
					}else if( candidate.info.category.equals("2")  && score >= genCutOff ){
						QObcG++;
					}else if( candidate.info.category.equals("3")  && score >= genCutOff ){
						QScG++;
					}else if( candidate.info.category.equals("4")  && score >= genCutOff ){
						QStG++;
					}

					if( candidate.info.isPd  && score >= genCutOff ){
						QPwDG++;
					}

					if( score >= obcCutOff && score < genCutOff ){

						if( candidate.info.category.equals("2") ){
							QObcX++;
						}else if( candidate.info.category.equals("3") ){
							QSCObcB++;	
						}else if( candidate.info.category.equals("4") ){
							QSTObcB++;	
						}

						if( candidate.info.isPd ){
							QPwDObcB++;	
						}
					}
				}

				if( ( candidate.info.category.equals("3") || candidate.info.category.equals("4") || candidate.info.isPd ) && score >= sTsCPwDCutOff ){

					candidate.isQualified = true;

					if( candidate.info.category.equals("3")  && score < obcCutOff )
						QScX++;
					else if( candidate.info.category.equals("4") && score < obcCutOff )
						QStX++;
					else if( candidate.info.isPd && score < obcCutOff )
						QPwDX++;
				}

				if( candidate.isQualified  ){

					Q++;

					if( candidate.info.isPd ){

						if( candidate.info.category.equals("1") )
							pwdG++; 
						else if( candidate.info.category.equals("2") )
							pwdO++; 
						else if( candidate.info.category.equals("3") )
							pwdSC++; 
						else if( candidate.info.category.equals("4") )
							pwdST++; 
					}

					listOfQualifedMarks.add( score );

					if( candidate.info.gender.equals("Male") )
						maleQ++;
					else if( candidate.info.gender.equals("Female") )
						femaleQ++;
					else
						otherQ++;

					if( candidate.info.gender.equals("Male") ){

						if( score > maxMarksMaleQ )
							maxMarksMaleQ = score;
						if( score < minMarksMaleQ )
							minMarksMaleQ = score;

					}else if( candidate.info.gender.equals("Female") ){

						if( score > maxMarksFemaleQ )
							maxMarksFemaleQ = score;
						if( score < minMarksFemaleQ )
							minMarksFemaleQ = score;
					}else{
						if( score > maxMarksOtherQ )
							maxMarksOtherQ = score;
						if( score < minMarksOtherQ )
							minMarksOtherQ = score;
					}

					if( score > maxMarksQualified )
						maxMarksQualified = score;
					if( score < minMarksQualified )
						minMarksQualified =  score;

					if( candidate.info.category.equals("2") && !candidate.info.isPd)
						QObc++;
					else if( candidate.info.category.equals("3") && !candidate.info.isPd)
						QSc++;
					else if( candidate.info.category.equals("4") && !candidate.info.isPd)
						QSt++;	
					if( candidate.info.isPd )
						QPwD++;
				}else{
					notQ++;
				}
			}

		}

		if( listOfQualifedMarks.size() > 0 ){

			double [] qmarks = StdStats.toArray( listOfQualifedMarks );

			this.avgMarksQualified = Double.parseDouble( new DecimalFormat("#0.0#").format( StdStats.mean( qmarks ) ) );
			this.stdMarksQualified = Double.parseDouble( new DecimalFormat("#0.0#").format( StdStats.stddev( qmarks ) ) );
			this.perQualified = Double.parseDouble( new DecimalFormat("#0.0#").format( ((double) Q / ( double) (Q + notQ )) * 100 ) ) ;
			this.perMaleQ = Double.parseDouble( new DecimalFormat("#0.0#").format( ((double) maleQ / (double) (Q + notQ )) * 100 ) );
			this.perFemaleQ = Double.parseDouble( new DecimalFormat("#0.0#").format( ((double) femaleQ / (double) (Q + notQ )) * 100 ) );
			this.perOtherQ = Double.parseDouble( new DecimalFormat("#0.0#").format( ((double) otherQ / (double) (Q + notQ )) * 100 ) );
		}
	}

	void process(){

		calStats();

		Iterator it = sessionMap.entrySet().iterator();

		while( it.hasNext() ){

			Map.Entry pairs = (Map.Entry)it.next();

			Session sn = (Session) pairs.getValue();
			sn.calStats();

			for(int i = 0; i < sn.listOfCandidate.size(); i++){

				Candidate candidate = sn.listOfCandidate.get(i);

				if( multiSession ){

					Double actualMark =   Double.parseDouble( new DecimalFormat("#0.0#").format( candidate.actualMark ));
					candidate.normalisedMark = ( (mTgBar - mQg) / (sn.mTBar - sn.mQ ) ) * ( actualMark - sn.mQ ) + mQg;
					candidate.actualNormalisedMark = candidate.normalisedMark;
					candidate.normalisedMark = Double.parseDouble( new DecimalFormat("#0.0#").format( candidate.normalisedMark ));
					listOfActualNormalisedMarks.add( candidate.normalisedMark );

					if( candidate.normalisedMark >= 0){
						listOfNormalisedMarks.add( candidate.normalisedMark );
					}else{
						listOfNormalisedMarks.add( 0.0d );
					}
				}else{
					/*
					   candidate.actualJAMScore = ( SQ + ( ST - SQ ) * ( ( candidate.rawMark  - mQ ) / ( mTBar - mQ  ) ) );
					   candidate.JAMScore = (int) Math.round( candidate.actualJAMScore );
					   if( candidate.JAMScore > 1000 )
					   candidate.JAMScore = 1000;
					 */

					candidate.actualJAMScore = 0;
					candidate.JAMScore = 0;
				}
			}
		}

		if( multiSession ){

			calStatsAfterNormalisation();

			it = sessionMap.entrySet().iterator();

			while( it.hasNext() ){

				Map.Entry pairs = (Map.Entry)it.next();
				Session sn = (Session) pairs.getValue();

				for(int i = 0; i < sn.listOfCandidate.size(); i++){

					Candidate candidate = sn.listOfCandidate.get(i);

					/*

					   candidate.actualJAMScore = ( SQ + ( ST - SQ ) * ( ( candidate.normalisedMark  - mQ ) / ( mTBar - mQ  ) ) );
					   candidate.JAMScore = (int) Math.round( candidate.actualJAMScore );

					   if( candidate.JAMScore > 1000 ){
					   candidate.JAMScore = 1000;
					   }

					 */
					candidate.actualJAMScore = 0;
					candidate.JAMScore = 0;	
				}
			}
		}

	}

	void addQuestion(int qn, Question question, String sessionNo){

		Session session = sessionMap.get( sessionNo );

		if( session == null ){

			session = new Session( sessionNo );
			sessionMap.put( sessionNo, session );
		}

		session.listOfQuestions.add( qn, question );
	}

	void calStatsAfterNormalisation(){

		Collections.sort( this.listOfNormalisedMarks, Collections.reverseOrder() );
		double [] marks = StdStats.toArray( this.listOfNormalisedMarks );

		zeroPointOnePercent = ( int ) Math.ceil( ( (double) marks.length) / 1000 );
		maxOf10OR01per = (int) Math.max( zeroPointOnePercent, 10 );
		mTgBar = StdStats.mean( marks, 0, this.zeroPointOnePercent - 1 );

		mTBar = StdStats.mean( marks, 0, this.maxOf10OR01per - 1 );; 
		mean = StdStats.mean( marks );
		stdDev = StdStats.stddev( marks );

		mQg = this.mean + this.stdDev;
		mQ = Math.max( this.mQg , 25 );

		genCutOff = Double.parseDouble( new DecimalFormat("#0.0#").format( mQ ) );
		obcCutOff = Double.parseDouble( new DecimalFormat("#0.0#").format( mQ * 0.9 ) );
		sTsCPwDCutOff = Double.parseDouble( new DecimalFormat("#0.0#").format( mQ * (2.0/3.0) ) );

		maxNorMarks = StdStats.max( marks );

		double []amarks = StdStats.toArray( this.listOfActualNormalisedMarks );

		minNorMarks = StdStats.min( amarks );
	}

	boolean verify(double Gx, double Ox, double SSPx){

		QualifyingMarks qms = qulifyMark.get( paperCode );

		double countGen = 0;
		double countObc = 0;
		double countSC = 0;
		double countST = 0;

		double totalGenBC = qms.gen;
		double totalOBC = qms.obc;
		double totalSC = qms.sc;
		double totalST = qms.st;

		for(int i = 0;  i <  listOfCandidate.size(); i++ ){

			Candidate candidate = listOfCandidate.get(i);

			if( candidate.info.category.equals("1") && candidate.actualMark >= Gx )
				countGen++;
			if( candidate.info.category.equals("2") && candidate.actualMark >= Ox )
				countObc++;
			if(candidate.info.category.equals("3") && candidate.actualMark >=  SSPx ) 
				countSC++;
			if(candidate.info.category.equals("4") && candidate.actualMark >=  SSPx ) 
				countST++;

			if(  countGen >= (1.25 * totalGenBC )  &&  countObc >= (1.25 * totalOBC)  && countSC >= (1.25 * totalSC)  && countST >= (1.25 * totalST) ){
				return true;
			}
		}

		//System.out.println((totalGenBC) +", "+( totalOBC )+", "+ ( totalSC ) +", "+ ( totalST));

		return false;
	}

	void calStats(){

		Collections.sort( listOfActualMarksGreterZero, Collections.reverseOrder());
		double [] marks = StdStats.toArray( listOfActualMarksGreterZero );

		zeroPointOnePercent = ( int ) Math.ceil( ( (double) marks.length) / 1000 );
		maxOf10OR01per = (int) Math.max( zeroPointOnePercent, 10 );
		mTgBar = StdStats.mean( marks, 0, zeroPointOnePercent - 1 );
		mTBar = StdStats.mean( marks, 0, maxOf10OR01per - 1 ); 
		
		mean = StdStats.mean( marks );

		//System.out.println( marks.length+" "+StdStats.sum( marks)+" "+mean);

		stdDev = StdStats.stddev( marks );

		double [] factor = {0.5, 0.4, 0.3, 0.2, 0.1, 0, -0.1, -0.2, -0.3, -0.4, -0.5, -0.6, -0.7, -0.8, -0.9, -1.0};

		for(int i = 0; i < factor.length; i++){

			double Gx = Double.parseDouble( new DecimalFormat("#0.0#").format( mean + factor[i] * stdDev ) );
			double Ox = Double.parseDouble( new DecimalFormat("#0.0#").format( Gx * 0.9 ) );
			double SSPx  = Double.parseDouble( new DecimalFormat("#0.0#").format( Gx * 0.5) );

			if( verify ( Gx, Ox, SSPx ) ){

				genCutOff =  Gx;
				obcCutOff =  Ox;
				sTsCPwDCutOff = SSPx;
				break;		

			}
		}

		if( genCutOff <= 0 ){

			System.out.println("Cut Off Less Than Zero");
			Collections.sort( listOfActualMarksGreterZero );

			double min = 9999;

			for(int i = 0;  i <  listOfCandidate.size(); i++ ){

				Candidate candidate = listOfCandidate.get(i);

				if( candidate.info.category.equals("1") && candidate.actualMark > 0 ){
					if( min > candidate.rawMark )
						min = candidate.rawMark; 
				}
			}

			genCutOff = min;
			obcCutOff = genCutOff * 0.9;
			sTsCPwDCutOff = genCutOff * 0.5;
		}	

		mQg = mean + stdDev;
		mQ = Math.max( mQg , 25 );

		genCutOff = Double.parseDouble( new DecimalFormat("#0.0#").format( genCutOff ) );
		obcCutOff = Double.parseDouble( new DecimalFormat("#0.0#").format( obcCutOff ) );
		sTsCPwDCutOff = Double.parseDouble( new DecimalFormat("#0.0#").format( sTsCPwDCutOff ) );

		if( sessionMap.size() > 1)
			multiSession = true;

		Iterator it = sessionMap.entrySet().iterator();

		while( it.hasNext() ){

			Map.Entry pairs = (Map.Entry)it.next();
			Session session = (Session) pairs.getValue();
			session.calStats();
		}

		double [] amarks = StdStats.toArray( listOfActualMarks );
		maxRawMarks = StdStats.max( amarks ) ;
		minRawMarks = StdStats.min( amarks ) ;
	}	

	void header2(){
		System.out.println(" _____________________________________________________________________________________________________________________________________________");
		System.out.println("|        |             |          |                                                                                                           |");
		System.out.println("| Rank   | Reg-Number  | Raw-Mark | Question wise marks                                                                                       |");
		System.out.println("|________|_____________|__________|___________________________________________________________________________________________________________|");
	}
	void footer2(){
		System.out.println("|________|_____________|__________|___________________________________________________________________________________________________________|");
	}

	void header1( ){
		if( Print.info ){
			System.out.println(" ______________________________________________________________________________________________________________________________________________________________________________________________________________________________");
			System.out.println("|        |              |                                     |         |          |                 |           |       |       |             |          |          |             |                                           |");
			System.out.println("| Rank   | Reg-Number   | Name                                | Session | Raw-Mark | NormalisedMarks | JAMScore  | CatID | PwD   | Qualified   | Positive | Negative | Ratio(P/N)  | Section                                   |");
			System.out.println("|________|______________|_____________________________________|_________|__________|_________________|___________|_______|_______|_____________|__________|__________|_____________|___________________________________________|");
		}else{
			System.out.println(" _______________________________________________________________________________________");
			System.out.println("|        |              |         |          |                 |           |            |");
			System.out.println("| Rank   | Reg-Number   |Session  | Raw-Mark | NormalisedMarks | JAMScore  | Section    |");
			System.out.println("|________|______________|_________|__________|_________________|___________|____________|");
		}
	}

	void footer1(){
		if( Print.info ){
			System.out.println("|________|______________|_____________________________________|_________|__________|_________________|___________|_______|_______|_____________|__________|__________|_____________|___________________________________________|");
		}else{
			System.out.println("|________|______________|_________|__________|_________________|___________|____________|");
		}
	}


	void header0( ){
		if( Print.info ){
			System.out.println(" ________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");
			System.out.println("|        |              |                                     |         |          |             |                 |           |                  |       |       |           |           |          |        |                                          |");
			System.out.println("| Rank   | Reg-Number   | Name                                | Session | Raw-Mark | Actual-Mark | NormalisedMarks | JAMScore  | Actual JAMScore  | CatID | Pwd   | Qualified | Positive  | Negative | Ratio  | Section                                  |");
			System.out.println("|________|______________|_____________________________________|_________|__________|_____________|_________________|___________|__________________|_______|_______|___________|___________|__________|________|__________________________________________|");
		}else{
			System.out.println(" ______________________________________________________________________________________________________________________");
			System.out.println("|        |             |         |          |             |                 |           |                  |           |");
			System.out.println("| Rank   | Reg-Number  | Session | Raw-Mark | Actual-Mark | NormalisedMarks | JAMScore  | Actual JAMScore  | Section   |");
			System.out.println("|________|_____________|_________|__________|_____________|_________________|___________|__________________|___________|");
		}
	}

	void footer0( ){
		if( Print.info ){		
			System.out.println("|________|______________|_____________________________________|_________|__________|_____________|_________________|___________|__________________|_______|_______|___________|___________|__________|________|__________|");
		}else{
			System.out.println("|________|_____________|_________|__________|_____________|_________________|___________|__________________|___________|");
		}
	}

	void print(boolean log){

		ranking();
		print();

		Iterator it = sessionMap.entrySet().iterator();
		if( log ){
			System.out.format(" ________________________________________%n");
			System.out.format("|                                        |%n");
			System.out.format("|              Log Print                 |%n");
			System.out.format("|________________________________________|%n");
		}
		while( it.hasNext() ){
			Map.Entry pairs = (Map.Entry)it.next();
			Session session = (Session) pairs.getValue();
			session.print();
			if( log ){
				session.printLog( );
			}
		}

		if( Print.resultView ){

			/*
			   CHANGED 16032014
			   In header paper-name, opt-1-Sec-name, and opt-2-sec-name is added 

			 */

			System.out.println("Registration_id, Enrollment_id, applicant_name, category_id, is_pd, Paper-Code, Paper-Name, RawMarks, Normalized-Marks, AIR, JAM-Score, is_qualified, genCutOff, obcCutOff, sTsCPwDCutOff");
			printResultView();
			return;

		}else if( Print.scoreView ){

			/*
			   CHANGED 16032014
			   In header paper-name, opt-1-Sec-name, and opt-2-sec-name is added 

			 */

			System.out.println("Registration_id, isQualified, Enrollment_id, JAM-Year,JAM-PaperCode, Paper-Name, Candidate-Name, Number-of-Candidate-appeared, RawMarks, NorMarks, JAMScore,AIR, category, PwD, Scribe, Nationality, Gender, dob (dd/mm/yy), Name of the Parent, Email, Gen-cutoff, OBC-cutoff, SCSTPwD_Cutoff, DigitalFingerPrint ");

			printScoreView();
			return;

		}else if( Print.detail )
			header2();
		else if( Print.actual )
			header0();
		else
			header1();

		for(int i = 0; i < listOfCandidate.size(); i++){
			Candidate candidate = listOfCandidate.get(i);
			candidate.print();
		}

		if( Print.detail )
			footer2();
		else if( Print.actual )
			footer0();
		else
			footer1();

	}

	void print(){

		System.out.format(" ________________________________________%n");
		System.out.format("| Paper Code             | %-13s |%n", paperCode);
		System.out.format("|________________________|_______________|%n");
		System.out.format("| Total Candidates       | %-13d |%n", listOfObtainedMarks.size() );
		System.out.format("| Avg 0.1%% Candidate(G)  | %-13f |%n", mTgBar);
		System.out.format("| 0.1 %% Candidate(G)     | %-13d |%n", zeroPointOnePercent );
		System.out.format("| Max(10,0.1%%) (G)       | %-13d |%n", maxOf10OR01per );
		System.out.format("| Max of means(G)        | %-13f |%n", mTBar);
		System.out.format("| Max of (mu+sigma,25)   | %-13f |%n", mQ );
		System.out.format("| No Of Positive Marks   | %-13d |%n", listOfActualMarksGreterZero.size()  );
		System.out.format("| Mean(Positive)         | %-13f |%n", mean );
		System.out.format("| stdDev(Positive)       | %-13f |%n", stdDev );
		System.out.format("| Max(Raw)               | %-13f |%n", maxRawMarks );
		System.out.format("| Min(Raw)               | %-13f |%n", minRawMarks );
		System.out.format("| Max(Nor)               | %-13.2f |%n", maxNorMarks );
		System.out.format("| Min(Nor)               | %-13.2f |%n", minNorMarks );
		System.out.format("| CutOff(GEN)            | %-13.2f |%n", genCutOff );
		System.out.format("| CutOff(OBC)            | %-13.2f |%n", obcCutOff );
		System.out.format("| CutOff(ST/SC/PwD)      | %-13.2f |%n", sTsCPwDCutOff );
		System.out.format("|________________________|_______________|%n");
		System.out.format("| (X-GEN)                | %-13d |%n", QGenX );
		System.out.format("| (OBC-as-GEN)           | %-13d |%n", QObcG);
		System.out.format("| (SC-as-GEN)            | %-13d |%n", QScG);
		System.out.format("| (ST-as-GEN)            | %-13d |%n", QStG);
		System.out.format("| (PwD-as-GEN) *         | %-13d |%n", QPwDG);
		System.out.format("|________________________|_______________|%n");
		System.out.format("| (X-OBC)                | %-13d |%n", QObcX );
		System.out.format("| (GEN-in-OBC-BAND)      | %-13d |%n", QGenObcB);
		System.out.format("| (SC-in-OBC-BAND)       | %-13d |%n", QSCObcB);
		System.out.format("| (ST-in-OBC-BAND)       | %-13d |%n", QSTObcB);
		System.out.format("| (PwD-in-OBC-BAND) *    | %-13d |%n", QPwDObcB);
		System.out.format("| (X-SC)                 | %-13d |%n", QScX );
		System.out.format("| (X-ST)                 | %-13d |%n", QStX );
		System.out.format("| (X-PD)                 | %-13d |%n", QPwDX );
		System.out.format("|________________________|_______________|%n");
		System.out.format("| (OBC-Qualified-Ex-PD)  | %-13d |%n", QObc);
		System.out.format("| (SC-Qualified-EX-PD)   | %-13d |%n", QSc);
		System.out.format("| (ST-Qualified-EX-PD)   | %-13d |%n", QSt);
		System.out.format("| (PwD-Qualified)        | %-13d |%n", QPwD);
		System.out.format("|________________________|_______________|%n");
		System.out.format("| (Score-Card Issued)    | %-13d |%n", Q);
		System.out.format("| (No-Score-Card)        | %-13d |%n", notQ);
		System.out.format("|________________________|_______________|%n");
		System.out.format("| Total GEN              | %-13d |%n", QGenX );
		System.out.format("| Total OBC              | %-13d |%n", ( QObcG + QObcX ) );
		System.out.format("| Total SC               | %-13d |%n", ( QScG + QSCObcB + QScX ) );
		System.out.format("| Total ST               | %-13d |%n", ( QStG + QSTObcB + QStX ) );
		System.out.format("| Total PwD              | %-13d |%n", ( QPwDObcB + QPwDX + QPwDG ) );
		System.out.format("| Total PwD-Gen          | %-13d |%n", pwdG );
		System.out.format("| Total PwD-OBC          | %-13d |%n", pwdO );
		System.out.format("| Total PwD-SC           | %-13d |%n", pwdSC );
		System.out.format("| Total PwD-ST           | %-13d |%n", pwdST );
		System.out.format("| Total SC/ST/PwD        | %-13d |%n", (QScG + QSCObcB + QScX ) + (QStG + QSTObcB + QStX) + ( QPwDObcB + QPwDX + QPwDG ) );
		System.out.format("|________________________|_______________|%n");

		System.out.format("| %% Total GEN            | %-13.2f |%n", ( ( (double) QGenX / (double) (Q + notQ ) )  * 100 ) );
		System.out.format("| %% Total OBC            | %-13.2f |%n", ( ( (double) ( QObcG + QObcX ) / (double) ( Q + notQ ) ) * 100)  );
		System.out.format("| %% Total SC             | %-13.2f |%n", ( ( (double) ( QScG + QSCObcB + QScX ) / (double) ( Q + notQ ) ) * 100 ) );
		System.out.format("| %% Total ST             | %-13.2f |%n", ( ( (double) ( QStG + QSTObcB + QStX ) / (double) ( Q + notQ ) ) * 100 ) );
		System.out.format("| %% Total PwD            | %-13.2f |%n", ( ( (double) ( QPwDObcB + QPwDX + QPwDG ) / (double) ( Q + notQ ) ) * 100 ) );
		System.out.format("| %% Total SC/ST/PwD      | %-13.2f |%n", ( ( (double) ( ( QScG + QSCObcB + QScX ) + ( QStG + QSTObcB + QStX ) + ( QPwDObcB + QPwDX + QPwDG ) ) / (double) ( Q + notQ ))*100) );

		System.out.format("|________________________|_______________|%n");
		System.out.format("| %% Qualified            | %-13.2f |%n", perQualified);
		System.out.format("| Max Marks Qualified    | %-13.2f |%n", maxMarksQualified);
		System.out.format("| Min Marks Qualified    | %-13.2f |%n", minMarksQualified);
		System.out.format("| Avg Marks Qualified    | %-13.2f |%n", avgMarksQualified);
		System.out.format("| StdD Marks Qualified   | %-13.2f |%n", stdMarksQualified);
		System.out.format("|________________________|_______________|%n");
		System.out.format("| Male Qualified         | %-13d |%n", maleQ);
		System.out.format("| Female Qualified       | %-13d |%n", femaleQ);
		System.out.format("| Other Qualified        | %-13d |%n", otherQ);
		System.out.format("| %% Male                 | %-13.2f |%n", perMaleQ);
		System.out.format("| %% Female               | %-13.2f |%n", perFemaleQ);
		System.out.format("| %% Other                | %-13.2f |%n", perOtherQ);
		System.out.format("|________________________|_______________|%n");
		System.out.format("| Max Male Qualified     | %-13.2f |%n", maxMarksMaleQ);
		System.out.format("| Min Male Qualified     | %-13.2f |%n", minMarksMaleQ);
		System.out.format("| Max FeMale Qualified   | %-13.2f |%n", maxMarksFemaleQ);
		System.out.format("| Min FeMale Qualified   | %-13.2f |%n", minMarksFemaleQ);
		System.out.format("| Max Other Qualified    | %-13.2f |%n", maxMarksOtherQ);
		System.out.format("| Min Other Qualified    | %-13.2f |%n", minMarksOtherQ);
		System.out.format("|________________________|_______________|%n");
		System.out.format("| -999.0/+999.0 No Candidate found       |%n");
		System.out.format("|________________________|_______________|%n");
	}



	void printResultView(){

		for(int i = 0; i < listOfCandidate.size(); i++){

			Candidate c = listOfCandidate.get(i);

			if( c.info == null ){
				System.out.println("Candidate information is msssing");
				return;
			}

			String NRMark = Double.parseDouble( new DecimalFormat("#0.0#").format( c.actualNormalisedMark ) ) + "";

			if( !multiSession )
				NRMark = "Not Applicable";
			System.out.print(c.rollNumber+","+c.info.applicationId+","+c.info.name+","+c.info.category+","+c.info.isPd+","+c.paperCode+","+CodeMapping.paperCodeMap.get( c.paperCode.trim() )+",");

			/*

			if( c.sections.size() > 0){

				Iterator<String> itr = c.sections.iterator();

				while( itr.hasNext() ){
					String section = itr.next();
					System.out.print(section+","+CodeMapping.sectionCodeMap.get( section.trim() )+", ");
				}	
				if( c.sections.size() == 1)	
					System.out.print(", , ");
			}
			else{
				System.out.print(", , , , ");
			}

			*/

			double rMark = Double.parseDouble( new DecimalFormat("#0.0#").format( c.actualMark ) );

			System.out.println( rMark+", "+NRMark+","+c.rank+","+c.JAMScore+","+c.isQualified+","+genCutOff+","+obcCutOff+","+sTsCPwDCutOff);
		}

	}

	void printScoreView(){

		for(int i = 0; i < listOfCandidate.size(); i++){

			Candidate c = listOfCandidate.get(i);

			//if( !c.isQualified )
			//	continue;

			if( c.info == null ){
				System.out.println("Candidate information is msssing");
				return;
			}

			c.photoPath="photo/S"+c.sessionId+"/"+c.paperCode+""+c.rollNumber;
			c.signaturePath="signature/S"+c.sessionId+"/"+c.paperCode+""+c.rollNumber;
			c.digitalFP = DigitalSignature.getDigitalSignature( c.paperCode+""+c.rollNumber+""+c.name+""+c.info.category+""+c.info.gender+""+c.rawMark+""+c.JAMScore+""+c.rank);

			c.qrCode = QRCodeGenerator.getCode(); 	

			String NRMark = Double.parseDouble( new DecimalFormat("#0.0#").format( c.actualNormalisedMark ) ) + "";

			if( !multiSession )
				NRMark = "Not Applicable";

			System.out.print(c.rollNumber+", "+c.isQualified+", "+c.info.applicationId+", 2016, "+c.paperCode+", "+CodeMapping.paperCodeMap.get(c.paperCode.trim())+", ");

            /*
			if( c.sections.size() > 0){

				Iterator<String> itr = c.sections.iterator();
				while( itr.hasNext() ){
					String section = itr.next();
					System.out.print(section+","+CodeMapping.sectionCodeMap.get( section.trim() )+",");
				}
				if( c.sections.size() == 1)	
					System.out.print(", , ");

			}
			else{
				System.out.print(", , , , ");
			}
            */

			double rMark = Double.parseDouble( new DecimalFormat("#0.0#").format( c.actualMark ) );
			System.out.println(c.info.name+", "+listOfCandidate.size()+", "+rMark+", "+NRMark+", "+c.JAMScore+", "+c.rank+", "+c.info.category+", "+c.info.isPd+", "+c.info.scribe+", "+c.info.nationality+", "+c.info.gender+", "+c.info.dob+", "+c.info.parentName+", "+genCutOff+", "+obcCutOff+", "+sTsCPwDCutOff+", "+c.digitalFP);

		}
	}

	void printLog(){
		Iterator it = sessionMap.entrySet().iterator();
		while( it.hasNext() ){
			Map.Entry pairs = (Map.Entry)it.next();
			Session session = (Session) pairs.getValue();
			session.printLog();
		}
	}
}

class CandidateInfo{

	String applicationId;
	String name;
	String parentName;
	String category;
	String city;
	String nationality;
	String gender;
	String dob;

	boolean scribe;	
	boolean isPd;

	CandidateInfo( String applicationId, String name, String parentName, String dob, String gender, String nationality, String category, String isPd, String scribe) {

		this.applicationId = applicationId;                 
		this.name = name;
		this.parentName = parentName;
		this.category = category;
		this.nationality = nationality;
		this.gender = gender;
		this.dob = dob;

		if( scribe.trim().equals("t"))
			this.scribe = true;	

		if( isPd.trim().equals("t"))
			this.isPd = true;
	}

	String print(){
		String out = applicationId+","+name+", "+parentName+", "+dob+", "+gender+", "+nationality+", "+scribe;
		return out.trim();
	}
}

class PNMarks{
	
	String section;
	double positive;
	double negative;
	
	PNMarks(String section, double positive, double negative){
		this.section =  section;
		this.negative = negative;
		this.positive = positive;
	}
	public String toString(){
		return String.format("[%s %-4.2f/%-4.2f]", section,positive,negative);
	}
}


class Candidate {

	String rollNumber;
	String name;	
	String paperCode;
	String sessionId;
	boolean isQualified;
	CandidateInfo info;

	int rank;	
	int marks2Negative;
	int marks1Negative;

	double noN;
	double noP;

	double actualMark;	
	double rawMark;
	double normalisedMark;
	double actualNormalisedMark;
	int JAMScore;
	double actualJAMScore;

	String digitalFP;
	String photoPath;
	String signaturePath;
	String qrCode;	

	List<Double> marks;
	List<Response> responses;
	SortedMap<String, PNMarks> sectionWisePNMarks;
	SortedSet<String> sections;

	Candidate(String rollNumber, String name, String sessionId, String paperCode){

		this.rollNumber = rollNumber;
		this.name = name;
		this.sessionId = sessionId;
		this.paperCode = paperCode;
		this.noN = 0.0; 
		this.noP = 0.0; 
		this.marks2Negative = 0;
		this.marks1Negative = 0;

		this.rawMark = 0.0d;
		this.actualMark = 0.0d;
		this.actualJAMScore =  0.0d;
		this.JAMScore =  0;
		this.normalisedMark = 0.0d;
		this.rank = -1;

		this.isQualified = false;
		this.info = null;
		this.digitalFP = null;
		this.qrCode = null;

		marks = new ArrayList<Double>();
		responses = new ArrayList<Response>();
		sections = new TreeSet<String>();
		sectionWisePNMarks = new TreeMap<String, PNMarks>();	
	}

	double getRawMarks(){
		return this.rawMark;
	}

	double getJAMScore(){
		return this.JAMScore;
	}

	double getNRMark(){
		return this.normalisedMark;
	}

	void print(){

		if( Print.detail ){

			System.out.format("| %5d  | %-11s | %-8.2f |", rank, paperCode+""+rollNumber, rawMark, actualMark);
			String output = "";
			boolean first = true;
			for(int i = 0; i < marks.size(); i++ ){
				String qw = "";
					
				if( responses.get(i).answer.equals("--") ){
					//qw = "NA";
					qw += ""+FP.prints( marks.get(i) )+"|"+responses.get(i).answer+"|"+responses.get(i).options;
				}else{
					qw += ""+FP.prints( marks.get(i) )+"|"+responses.get(i).answer+"|"+responses.get(i).options;
				}				

				if( i == marks.size() - 1)
					output += ""+qw;
				else	
					output += ""+qw+",";

				if( output.length() > 100){
					if( first ){
						System.out.format("%-106s |\n",output);
					}else{
						System.out.print("|        |             |          |");
						System.out.format("%-106s |\n",output);
					}
					output = "";
					first = false;
				}
			}
			if( output.trim().length() > 1){
				System.out.print("|        |             |          |");
				System.out.format("%-106s |\n",output);

			}
			System.out.println("|        |             |          |                                                                                                           |");

		}else{

			String NrScore = normalisedMark+"";

			if( !Print.multiSession ){
				NrScore = "Not Applicable";
			}

			if( info != null){

				if( Print.actual ){
					System.out.format("| %5d  | %-12s | %-35s | %-7s | %-8.2f | %-11f | %-15s | %-9d | %-16f | %-5s | %-5b | %-9b | %-9.2f | %-8.2f | %-6.2f |", rank, rollNumber, info.name, sessionId, rawMark, actualMark, NrScore , JAMScore, actualJAMScore, info.category, info.isPd, isQualified, noP, noN, (noP/noN) );
				}else{
					System.out.format("| %5d  | %-12s | %-35s | %-7s | %-8.2f | %-15s | %-9d |  %-4s | %-5b | %-11b | %-9.2f | %-8.2f | %-6.2f |", rank, rollNumber, info.name, sessionId, rawMark, NrScore, JAMScore, info.category, info.isPd, isQualified, noP, noN, ( noP/noN ) );
				}
			}else{

				if( Print.actual ){
					System.out.format("| %5d  | %-12s | %-7s | %-8.2f | %-11f | %-15.2f | %-9d | %-16f | ", rank, rollNumber, sessionId, rawMark, actualMark, normalisedMark, JAMScore, actualJAMScore);
				}else{
					System.out.format("| %5d  | %-12s | %-7s | %-8.2f | %-15.2f | %-9d | ", rank, rollNumber, sessionId, rawMark, normalisedMark, JAMScore );
				}

			}

			Iterator<String> itr = sections.iterator();

			String section = "";
			boolean first = true;

			while( itr.hasNext() ){

				String tsection = itr.next().trim();	
				PNMarks pnm = sectionWisePNMarks.get( tsection );	
				tsection = pnm.toString();

				if( first ){
					section = tsection;	
					first = false;
				}else if( "GA".equals( tsection ) ) { 	
					section = tsection+":"+section;	
				}else{
					section  = section+":"+tsection;
				}	
			}

			System.out.format("%-9s |%n",section);
		}

	}
	void printInfo(){
		String output =  rank+", "+rollNumber+" ,"+sessionId+", "+rawMark+", "+actualMark+", "+normalisedMark+", "+JAMScore+", "+actualJAMScore+", "+info.category+", "+info.isPd+", "+isQualified;
		output += ", "+info.print()+", "+qrCode+", "+digitalFP;
		System.out.println( output.trim() );
	}
}

class NorMarksComp implements Comparator<Candidate>{
	@Override
		public int compare(Candidate candidate1, Candidate candidate2) {
			if( candidate1.getNRMark() < candidate2.getNRMark() )
				return 1;
			if( candidate1.getNRMark() > candidate2.getNRMark() )
				return -1;

			long d1 = Double.doubleToLongBits(candidate1.getNRMark());
			long d2 = Double.doubleToLongBits(candidate2.getNRMark());

			return (d1 == d2 ?  0 : // Values are equal
					(d1 < d2 ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
					 1));                          // (0.0, -0.0) or (NaN, !NaN)
		}
}

class RawMarksComp implements Comparator<Candidate>{
	@Override
		public int compare(Candidate candidate1, Candidate candidate2) {

			if( candidate1.getRawMarks() < candidate2.getRawMarks() )
				return 1;
			if( candidate1.getRawMarks() > candidate2.getRawMarks() )
				return -1;
			
			if( candidate1.getRawMarks() == candidate2.getRawMarks() ){

				double ratio1 = (candidate1.noP / candidate1.noN);

				if( candidate1.noN > 0)
					ratio1 = Double.parseDouble( new DecimalFormat("#0.0#").format( ratio1 ));

				double ratio2 = (candidate2.noP / candidate2.noN);

				if( candidate2.noN > 0)
					ratio2 = Double.parseDouble( new DecimalFormat("#0.0#").format( ratio2 ));

				if( ratio1 < ratio2 )
					return 1;
				if( ratio1 > ratio2)
					return -1;

				long d1 = Double.doubleToLongBits( ratio1 );
                        	long d2 = Double.doubleToLongBits( ratio2 );
				
				return (d1 == d2 ?  0 : // Values are equal
					(d1 < d2 ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
					 1));                          // (0.0, -0.0) or (NaN, !NaN)

			}

			long d1 = Double.doubleToLongBits(candidate1.getRawMarks());
			long d2 = Double.doubleToLongBits(candidate2.getRawMarks());

			return (d1 == d2 ?  0 : // Values are equal
					(d1 < d2 ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
					 1));                          // (0.0, -0.0) or (NaN, !NaN)
		}
}


class JAMScore implements Comparator<Candidate>{
	@Override
		public int compare(Candidate candidate1, Candidate candidate2) {
			if( candidate1.getJAMScore() < candidate2.getJAMScore() )
				return 1;
			if( candidate1.getJAMScore() > candidate2.getJAMScore() )
				return -1;

			long d1 = Double.doubleToLongBits(candidate1.getJAMScore());
			long d2 = Double.doubleToLongBits(candidate2.getJAMScore());

			return (d1 == d2 ?  0 : // Values are equal
					(d1 < d2 ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
					 1));                          // (0.0, -0.0) or (NaN, !NaN)
		}
}

public class ResultProcessing{

	Map<String, Paper> paperMap;
	Map<String, CandidateInfo> candidateInfoMap;
	ResultProcessing(){

		paperMap = new HashMap<String, Paper>();
		candidateInfoMap = new HashMap<String, CandidateInfo>();
	}

	void readKey(String file){

		try{
			BufferedReader br = new BufferedReader(new FileReader( new File(file)) );
			String questionType = null;
			String sectionName = null;
			String questionKey = null;
			String questionMarks = null;
			String negativeMarks = null;

			boolean firstline = true;
			int count = 0;

			while( ( questionType = br.readLine() ) != null ){

				if( firstline ){
					firstline = false;
					continue;
				}
				sectionName = br.readLine();
				questionKey = br.readLine();
				questionMarks = br.readLine();

				//System.out.println("QT=> "+ questionType );
				//System.out.println("SN=> "+ sectionName );
				//System.out.println("KY=> "+ questionKey );
				//System.out.println("mK=> "+ questionMarks );

				String[] typeToken = questionType.split(",");
				String[] sectionToken = sectionName.split(",");
				String[] keyToken =  questionKey.split(",");
				String[] marksToken =  questionMarks.split(",");

				String paperCode = typeToken[1].trim();
				String sessionId = typeToken[2].trim();

				Paper paper = paperMap.get( paperCode );

				if( paper == null )
					paper = new Paper( paperCode );

				for(int i = 3, qn = 0; i < typeToken.length; i++, qn++ ){

					Question question  =  null;					
					String questionId = "Q"+(i-2);
					if( (i-2) < 10)
						questionId = "Q0"+(i-2);

					if( typeToken[i].equals("MCQ")  ){
						question = new MultipalChocie(questionId, sectionToken[i].trim(), keyToken[i].trim(), marksToken[i].trim() );
					}else if( typeToken[i].equals("NAT")  ){
						question = new RangeQuestion( questionId , sectionToken[i].trim(), keyToken[i].trim(), marksToken[i].trim() );
					}else if ( typeToken[i].equals("MSQ") ){
						question = new MultipalAnswer( questionId, sectionToken[i].trim(), keyToken[i].trim(), marksToken[i].trim() );
					}

					if( question == null ){
						System.out.println( typeToken[i] );
						System.out.println("Question is not proper "+ questionKey);
						System.exit(0);
					}

					paper.addQuestion( qn, question, sessionId );
				}

				if( paper == null){

					System.out.println("Paper is not proper "+ questionKey);
					System.exit(0);
				}

				paperMap.put( paperCode, paper );
				count++;
			}

		}catch(Exception e){
			e.printStackTrace();
		}	
	}

	boolean isAttempted(Response response){

		if( response != null ){
			if( !response.answer.trim().equals("--") ){
				return true;
			}
		}
		return false;
	}

	void readCandidateInfo(String filename){
		try{

			BufferedReader br = new BufferedReader(new FileReader( new File(filename) ) );
			String line = null;

			while( ( line = br.readLine() ) != null ){
				String[] tk = line.split("\"?(,|$)(?=(([^\"]*\"){2})*[^\"]*$) *\"?");

				String appId = tk[0].trim();
				String reg1 = tk[1].trim();
				String reg2 = tk[2].trim();
				String name = tk[3].trim();
				String pname = tk[4].trim();
				String dob = tk[5].trim();
				String gender = tk[6].trim();
				String category = tk[7].trim();
				String nationality = tk[8].trim();
				String pwd = tk[9].trim();
				String scribe = tk[10].trim();

				CandidateInfo cinfo = new CandidateInfo( appId, name, pname, dob, gender, nationality, category, pwd, scribe );

				candidateInfoMap.put( reg1, cinfo );

				if( reg2 != null && reg2.trim().length() > 0  )
					candidateInfoMap.put( reg2, cinfo );
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	void readCandidateAndRawMarkCalulation( String file, String sessionId  ){

		try{

			BufferedReader br = new BufferedReader(new FileReader( new File(file) ) );

			/* false reading */
			br.readLine();
			br.readLine();
			br.readLine();
			br.readLine();
			br.readLine();
			/* false reading end */

			String line = null;

			while( (line = br.readLine()) != null ){

				if( line.split(",").length <= 2 )
					continue;

				String options = br.readLine();

				String rtoken[] = line.split(",");
				String otoken[] = options.split(",");

				//System.out.println(  line );
				//System.out.println( options );

				String rollNumber = rtoken[1].trim();
				String name = rtoken[2].trim();

				String paperCode = rtoken[1].trim().substring(0,2);
				Paper paper = paperMap.get( paperCode );

				Candidate candidate = null;

				if( paper != null ){

					Session session = paper.sessionMap.get( sessionId );

					if( session != null ){

						candidate = new Candidate( rollNumber, name, sessionId, paperCode );
						CandidateInfo ci = candidateInfoMap.get( rollNumber );

						if( ci != null ){

							candidate.info = ci;
						}

						for(int i = 0, r = 12; i < session.listOfQuestions.size(); i++, r++){

							//System.out.println( rtoken[r] +", "+otoken[r] );

							Response response = new Response( rtoken[r], otoken[r] );

							Question question = session.listOfQuestions.get(i);

							double mark = question.eval( response, candidate );

							if( mark > 0.0){
								candidate.noP += mark;
							}else if( mark < 0.0){
								if( question.mark == 1.0d )
									candidate.marks1Negative++;
								else
									candidate.marks2Negative++;
							}

							candidate.responses.add(i, response );
							candidate.marks.add( mark );

							if( Math.abs( mark ) != 0 || ( isAttempted( response ) &&  question.section.trim().length() > 0 ) ){

								candidate.sections.add( question.section.trim() );
								PNMarks pnm = candidate.sectionWisePNMarks.get( question.section.trim() );

								if( pnm == null ){
									pnm = new PNMarks( question.section, 0 , 0);
								}

								if( mark > 0){
									pnm.positive += Math.abs( mark );
								}else if (mark < 0) {
									pnm.negative += Math.abs( mark );
								}

								candidate.sectionWisePNMarks.put( question.section.trim(), pnm );
							}

							candidate.rawMark += mark;
						}

						candidate.noN = ( candidate.marks1Negative*1.0d + 2.0d*candidate.marks2Negative) / 3.0d ;

						//System.out.println(candidate.rollNumber+", "+candidate.marks1Negative+"-1s "+ candidate.marks2Negative+"-2s "+candidate.noN);

						candidate.noN = Double.parseDouble( new DecimalFormat("#0.0#").format( candidate.noN ));

						candidate.actualMark = candidate.rawMark;

						candidate.rawMark = Double.parseDouble( new DecimalFormat("#0.0#").format( candidate.rawMark ));

						//if( candidate.rawMark <= 0.0d){
						//	candidate.rawMark = 0.0d;
						//}

						if( candidate.rawMark > 0.00 ){

							paper.listOfActualMarksGreterZero.add(  candidate.rawMark );
							session.listOfActualMarksGreterZero.add( candidate.rawMark );
						}

						paper.listOfObtainedMarks.add(  candidate.rawMark );

						paper.listOfCandidate.add(  candidate );

						session.listOfObtainedMarks.add( candidate.rawMark );
						session.listOfCandidate.add( candidate );

						session.listOfActualMarks.add ( candidate.actualMark );
						paper.listOfActualMarks.add ( candidate.actualMark );
					}else{
						System.out.println("Seesion is null");	
						System.exit(0);
					}
				}else{
					System.out.println("Paper not found! "+line);
					System.exit(0);
				}
			}
		}catch(Exception e){
			System.out.println("Problem is reading file :"+file);
			e.printStackTrace();
		}

	}	

	void readConfig(String configFile){

		try{

			if( configFile != null ){
				BufferedReader br = new BufferedReader(new FileReader( new File(configFile) ) );
				String line = null;
				while( (line = br.readLine() ) != null ){
					String token[] = line.split(":");
					if( token[0].trim().equalsIgnoreCase("negative") ){
						Config.negative = token[1].trim();
					}else if( token[0].trim().equalsIgnoreCase("cancelled") ){
						Config.cancelled = token[1].trim();
					}else if( token[0].trim().equalsIgnoreCase("unattempted") ){
						Config.unattempted = token[1].trim();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	void print( boolean log ){

		Iterator it = paperMap.entrySet().iterator();

		if( candidateInfoMap.size() > 0)
			Print.info = true;

		while ( it.hasNext() ) {
			Map.Entry pairs = (Map.Entry)it.next();
			Paper paper = (Paper) pairs.getValue();
			paper.print( log );
		}
	}

	void read(String keyFile, String resFile, String configFile, String applicantFile, String sessionId){

		readConfig( configFile );
		readKey( keyFile );

		if( applicantFile != null ){
			readCandidateInfo( applicantFile );
		}	

		readCandidateAndRawMarkCalulation( resFile, sessionId );
	}

	void process(){

		Iterator it = paperMap.entrySet().iterator();

		while ( it.hasNext() ) {
			Map.Entry pairs = (Map.Entry)it.next();
			Paper paper = (Paper) pairs.getValue();
			paper.process();
		}
	}

	void analysis(){

		Iterator it = paperMap.entrySet().iterator();
		Analysis analysis = new Analysis(); 
		while ( it.hasNext() ) {
			Map.Entry pairs = (Map.Entry)it.next();
			Paper paper = (Paper) pairs.getValue();

			Iterator itr = paper.sessionMap.entrySet().iterator();
			while( itr.hasNext() ){
				Map.Entry tpairs = (Map.Entry) itr.next();
				Session session = ( Session ) tpairs.getValue();
				analysis.PaperAnalyis( session );	
			}
		}
	}

	public static void main(String[] args){
		try{
			boolean log = false;
			String resFile =  null;
			String keyFile = null;
			String applicantFile = null;
			String configFile = null;
			String sessionId = null;	
			int i = 0;

			while( i < args.length ){

				if( args[i].equals("-k") ){

					keyFile =  args[i+1].trim();
					i++;

				}else if( args[i].equals("-an") ){

					Print.analysis = true;

				}else if( args[i].equals("-ap")  ){

					applicantFile = args[i+1];
					i++;

				}else if( args[i].equals("-r") ){

					resFile = args[i+1].trim();
					i++;

				}else if( args[i].equals("-c") ){

					configFile =  args[i+1].trim();
					i++;	

				}else if( args[i].equals("-l") ){
					log = true;

				}else if( args[i].equals("-a") ){

					Print.actual = true;

				}else if( args[i].equals("-d") ){

					Print.detail = true;

				}else if(  args[i].equals("-rv") ){

					Print.resultView = true;

				}else if( args[i].equals("-sv" ) ){

					Print.scoreView = true;
				}
				else if ( args[i].equals("-s") ){
					sessionId = args[i+1];
				}
				i++;
			}

			if( resFile == null || keyFile == null || sessionId == null ){

				System.out.println("Uses: -k <key-file> -r <response-file> -s <session-id> -c [optional] <config-file> [ -ap <applicant-file> ]  -l -a ");

				System.out.println("-l Printing of Question Deatils (Difficulty Level) ");
				System.out.println("-a Printing Actual Marks (Without Floor or Ceiling)");
				System.out.println("-d Detail Question wise marks for each applicant");
				System.out.println("-s sessionId");
				System.out.println("-an Top 100 Analysis of question");
				System.out.println("-sv Candidate Score View");
				System.out.println("-rv Candidate result View");
				System.out.println("[optional attribute ] ");
				System.exit(0);
			}

			ResultProcessing rp = new ResultProcessing();

			rp.read( keyFile, resFile, configFile, applicantFile, sessionId );

			rp.process();
			rp.print( log  );

			if( Print.analysis ){
				rp.analysis();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
