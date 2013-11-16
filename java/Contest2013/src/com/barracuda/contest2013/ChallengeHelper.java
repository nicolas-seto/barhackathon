package com.barracuda.contest2013;

public class ChallengeHelper {



	private static int checkCard(int[] currentHand,int ourScore,int theirScore,int card)
	{
		int result = 0;//result =0 is bad, = 1 is good, =2 is best,=3 you have to accept;
		int king=0,queen=0,jack=0;
		int arrayLength = currentHand.length;
		for(int i = 0;i < arrayLength; i++) 
		{
			switch (currentHand[i])
			{
			case 13:
				king++;
				break;
			case 12:
				queen++;
				break;
			case 11:
				jack++;
				break;
			default:
			    break;
			}
		}
		if (card !=0)
		{
			switch (card)
			{
			case 13:
				king++;
				break;
			case 12:
				queen++;
				break;
			case 11:
				jack++;
				break;
			default:
			    break;
			} 
		}

		switch(ourScore)
		{
		case 0:
			if(theirScore ==0)
			{
				if((king+queen+jack) >= 3)
				{
					result = 2;
				}
			}else if (theirScore ==1)
			{
				if((king+queen+jack) >3)
				{
					result = 2;
				}else if ((king+queen+jack) ==3)
				{
					result = 1;
				}
			}else if (theirScore == 2)
			{
				if((king+queen) ==3)
				{
					result =2;
				}else if((king+queen+jack) ==3)
				{
					result =1;
				}
			}

			break;
		case 1: //currently all the if else are similar but we can tweek it later if needed
			if(theirScore ==0) 
			{
				if((king+queen+jack) ==3 || (king+queen) ==2)
				{
					result = 2;
				}else if(king+queen+jack ==2)
				{
					result =1;
				}
			}else if (theirScore ==1)
			{
				if((king+queen+jack) ==3 || (king+queen) ==2)
				{
					result = 2;
				}else if(king+queen+jack ==2)
				{
					result =1;
				}
			}else if (theirScore == 2)
			{
				if((king+queen) ==2)
				{
					result = 2;
				}else if(king+queen+jack ==2)
				{
					result =1;
				}
			}
			break;
		case 2: //similar for now 
			if(theirScore ==0)
			{
				if((king+queen)>=1)
				{
					result = 2;
				}else if(king+queen+jack >=1)
				{
					result =1;
				}
			}else if (theirScore ==1)
			{
				if((king+queen)>=1)
				{
					result = 2;
				}else if(king+queen+jack >=1)
				{
					result =1;
				}
			}else if (theirScore == 2)
			{
				if(king ==1)
				{
				  result =3;  
				}else{
			    if((king+queen)>=1)
				{
					result = 2;
				}else if(king+queen+jack >=1)
				{
					result =1;
				}}
			}
			break;

		}
		return result;
	}



	public static boolean isChallengeAccepted(int[] sort,int ourScore,int theirScore,int ourPoint, int theirPoint,int card)
	{
		boolean result = false;	
		int handAssessment = checkCard(sort,ourScore,theirScore,card);

		if(theirPoint ==9)
		{
			result = true;
		}else if(ourPoint == 9)
		{
			if(handAssessment ==2){result = true;}

		}else if (ourPoint == 8)
		{
			if(theirPoint >2 && theirPoint <7)
			{
				if(handAssessment >=1 ){result = true;}
			}
			else if(theirPoint >= 7)// && theirPoint !=9)
			{
				if(handAssessment ==2){result = true;}
			}
		}else if (ourPoint <= 7)
		{
			if(theirPoint >= 8 && theirPoint-ourPoint >2)
			{
				if(handAssessment >=1 ){result = true;}
			}else if(theirPoint <7)
			{
				if(handAssessment >=1 ){result = true;}
			}
		}
		
		if(handAssessment ==3)
		{
		    result = true;
		}
		return result;
	}


}