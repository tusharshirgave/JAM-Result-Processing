package cdac.in.result;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
*@author Chandra Shekhar
*@email shekhar@cdac.in
*@date 18/02/2014
*
*/

class QuestionReport{

	Question question;
	String sessionId;
	int notAttempt;	
	int attempt;
	int correct;
	int wrong;
	boolean flag;

	List<String> answers; 
	
	QuestionReport(Question question){

		this.question = question;
		this.sessionId = null;
		answers = new ArrayList<String>();
		attempt = 0;
		notAttempt = 0;
		correct = 0;
		wrong = 0;
	}	

	void print(){

		Map<String, Integer> answersMap = new HashMap<String, Integer>();

		for(int i = 0; i < answers.size(); i++ ){
			Integer n = (Integer) answersMap.get( answers.get(i) );
			if( n != null){
				n = new Integer( n.intValue() + 1 );
				answersMap.put( answers.get(i), n);
			}
			else{
				answersMap.put( answers.get(i), new Integer("1"));
			}
		}
		
		if( wrong >  correct){
			Iterator it = answersMap.entrySet().iterator();
			String option = null;
			Integer count = 0 ;
			System.out.print(question.Id+",AT:"+attempt+",CR:"+correct+",WO:"+wrong+",AN:"+question.getAnswer()+", ");
                	while ( it.hasNext() ) {
                        	Map.Entry pairs = (Map.Entry)it.next();
				String o = (String) pairs.getKey();
				int c = ((Integer) pairs.getValue()).intValue();
				System.out.print("("+o+"|"+c+"),");
			}		
			System.out.println();
		}
	}
}

public class Analysis{	
	
	HashMap<String,QuestionReport> qReports = new HashMap<String,QuestionReport>();

	void PaperAnalyis(Session session){

		Collections.sort( session.listOfCandidate, new RawMarksComp() );

		System.out.println();

		System.out.println("[ Session "+session.id+" Question Analysis ]");

		//for(int i = 0; i <  session.zeroPointOnePercent; i++){

		for(int i = 0; i < 100; i++){

			Candidate can = session.listOfCandidate.get(i);

			for(int j = 0; j < can.responses.size(); j++){
				
				Response response =  can.responses.get(j);	
				Question question = session.listOfQuestions.get(j);
				QuestionReport report = qReports.get( session.id+"_"+question.Id );

				
				if( report == null){
					report = new QuestionReport( question );
					report.sessionId = session.id;
				}

				if( response.answer.equals("--") ){
					report.notAttempt++;
				}else{
					report.attempt++;
					if( question.eval(response) > 0){
						report.correct++;
					}else{
						report.wrong++;
					}

					if( question.type().equals("NAT") )	
						report.answers.add( response.options );
					else
						report.answers.add( response.answer );
				}
				qReports.put( session.id+"_"+question.Id, report );
			}
		}
		print( session.id );
	}		

	void print( String sessionId ){
		Iterator it = qReports.entrySet().iterator();
                while ( it.hasNext() ) {
                        Map.Entry pairs = (Map.Entry)it.next();
                        QuestionReport qr = (QuestionReport) pairs.getValue();
		        if( qr.sessionId.equals( sessionId ) )			
                        	qr.print();
                }      
	}
}
