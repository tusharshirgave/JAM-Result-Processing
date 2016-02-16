cdac/in/jam/result/ResultProcessing.java:698: error: cannot find symbol
				this.answer = answer.trim();
				    ^
  symbol: variable answer
cdac/in/jam/result/ResultProcessing.java:699: error: no suitable method found for add(double,double)
				this.answers.add(0.0d, 0.0d);
				            ^
    method List.add(int,Rang) is not applicable
      (actual argument double cannot be converted to int by method invocation conversion)
    method List.add(Rang) is not applicable
      (actual and formal argument lists differ in length)
    method Collection.add(Rang) is not applicable
      (actual and formal argument lists differ in length)
cdac/in/jam/result/ResultProcessing.java:715: error: cannot find symbol
						answers.add( new rang(lower, upper) );
						                 ^
  symbol:   class rang
  location: class RangeQuestion
cdac/in/jam/result/ResultProcessing.java:717: error: cannot find symbol
						answers.add( new rang(upper, lower) );
						                 ^
  symbol:   class rang
  location: class RangeQuestion
cdac/in/jam/result/ResultProcessing.java:759: error: cannot find symbol
		}else if( "MTA".equals( this.answer ) ){
		                            ^
  symbol: variable answer
cdac/in/jam/result/ResultProcessing.java:834: error: cannot find symbol
		System.out.print("[NAT"+Id+", ("+upper+":"+lower+"), ");
		                                 ^
  symbol:   variable upper
  location: class RangeQuestion
cdac/in/jam/result/ResultProcessing.java:834: error: cannot find symbol
		System.out.print("[NAT"+Id+", ("+upper+":"+lower+"), ");
		                                           ^
  symbol:   variable lower
  location: class RangeQuestion
7 errors
