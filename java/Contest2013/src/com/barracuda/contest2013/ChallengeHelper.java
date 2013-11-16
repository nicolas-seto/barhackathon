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
            arrayLength++;
        }
        switch(ourScore)
        {
        /* All 5 cards */
        case 0:
            /* 5 cards in play */
            if(theirScore ==0)
            {
                /* If no tie or 1 tie */
                if(arrayLength == 5 || arrayLength == 4)
                {
                    if((king+queen+jack) >= 3)
                    {
                        result = 2;
                    }
                /* If 2 ties */
                } else if(arrayLength == 3)
                {
                    if((king+queen+jack) >= 2)
                    {
                        result = 2;
                    }
                /* If 3 ties */
                } else if (arrayLength == 2) {
                    if ((king + queen + jack) >= 1) {
                        result = 2;
                    }
                /* If 4 ties */
                }else if(arrayLength ==1)
                {
                    if(king+queen ==1)
                    {
                        result = 3;
                    }
                }
            /* 4 cards left */
            }else if (theirScore ==1)
            {
                /* If 1 tie */
                if(arrayLength ==3)
                {
                    if(king+queen >= 2)
                    {
                        result = 3;
                    }else if(king+queen+jack >=2)
                    {
                        result = 2;
                    }
                /* If 2 ties */
                } else if (arrayLength == 2 ){
                    if (king + queen == 2) {
                       result = 3;
                    }
                    if (king + queen + jack == 2) {
                        result = 2;
                    }
                /* If 3 ties */
                }else if (arrayLength ==1)
                {
                    if(king+queen ==1)
                    {
                        result = 3;
                    }
                /* If 0 ties */
                }else
                {
                    if((king+queen+jack) >=3)
                    {
                        result = 2;
                    }else if ((king+queen+jack) >= 2)
                    {
                        result = 1;
                    }
                }
            /* 3 cards left */
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
        case 1: /* At least 4 cards in hand still */
            if(theirScore ==0) 
            {   /* 1 tie */
                if(arrayLength ==3)
                {
                    if(king+queen>=2)
                    {
                        result =3;
                    }else if(king + queen + jack >=2)
                    {
                        result =2;
                    }
                /* 2 ties */
                }else if(arrayLength ==2)
                {
                    if(king+queen >=1)
                    {
                        result = 3;
                    }else if(king+queen+jack >=1)
                    {
                        result = 2;
                    }
                /* 3 ties */
                }else if (arrayLength ==1)
                {
                    if(card ==13)
                    {
                        result = 3;
                    }else if(king+queen ==1 && card ==0)
                    {
                        result = 3;
                    }else if(king+queen + jack ==1 && card ==0)
                    {
                        result =2;
                    }
                }else{ /* 0 ties */
                    if((king+queen+jack) >=3 || (king+queen) >=2)
                    {
                        result = 2;
                    }else if(king+queen+jack >=2)
                    {
                        result = 1;
                    }
                }
                /* At least 3 cards left in hands */
            }else if (theirScore ==1)
            {   /* There's been 1 tie */
                if(arrayLength ==2)
                {
                    if(king+queen>=2)
                    {
                        result =3;
                    }else if (king + queen >=1)
                    {
                        result = 2;
                    }else if(king + queen >=1)
                    {
                        result =1;
                    }
                /* There's been 2 ties */
                }else if(arrayLength ==1)
                {
                    if(card ==13)
                    {
                        result = 3;
                    }else if(king+queen ==1 && card ==0)
                    {
                        result = 3;
                    }else if(king+queen + jack ==1 && card ==0)
                    {
                        result =2;
                    }
                }else
                    if((king+queen+jack) ==3 || (king+queen) >=2)
                    {
                        result = 2;
                    }else if(king+queen+jack ==2)
                    {
                        result =1;
                    }
            }else if (theirScore == 2)
            {
                if(arrayLength == 1 && king ==1)
                {
                    result = 3;
                }else if((king+queen) ==2)
                {
                    result = 2;
                }else if(king+queen+jack ==2)
                {
                    result =1;
                }
            }
            break;
        case 2: 
            if(theirScore ==0)
            {
                if(arrayLength ==2)
                {
                    result =3;
                }else if(arrayLength ==1)
                {
                    result =3;
                }else {
                    if((king+queen)>=1)
                    {
                        result = 2;
                    }else if(king+queen+jack >=1)
                    {
                        result =1;
                    }
                }
            }else if (theirScore ==1)
            {
                if(arrayLength ==1)
                {
                    result = 3;
                }else
                {if((king+queen)>=1)
                {
                    result = 2;
                }else if(king+queen+jack >=1)
                {
                    result =1;
                }
                }
            }else if (theirScore == 2)
            {
                if(king ==1)
                {
                    result = 3;  
                }else{
                    if((queen)>=1 && card ==0)
                    {
                        result = 2;
                    }else if(king+queen+jack >=1 && card==0)
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
        }else if (ourPoint == 8 )
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
    
    public static boolean issueChallenge (int[] sort,int ourScore,int theirScore,int ourPoint, int theirPoint,int card)
    {
        boolean result = isChallengeAccepted(sort,ourScore,theirScore,ourPoint,theirPoint,0);
        if(ourScore == theirScore && sort.length ==1 )
        {
            if(card == 13)
            {
                result = false;
            } else if (sort[0] == 13) {
                result = true;
            }
        }       
        
        if(theirPoint == 9)
        {
            result = true;
        }
        
        if(ourScore ==2 && sort[sort.length-1] - card >0)
        {
            result = true;
        }
                
        return result;
    }
}