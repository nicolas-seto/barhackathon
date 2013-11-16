package com.barracuda.contest2013;

public class RemainingCardStats {
    private int[] cards_remaining;
    private double low, high;
    private double percentage, lowpercentage;
    private final double MAX_LOW = 48;
    private final double MAX_HIGH = 56;
    
    public RemainingCardStats() {
        cards_remaining = new int[]{8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};
        low = 48;
        high = 56;
    }
    
    public void decrement(int cardValue) {
        cards_remaining[cardValue - 1]--;
        if (cardValue > 6) {
            high--;
        } 
        else {
            low--;
        }
        calculateHighPercentage();
    }
    
    public void reshuffle() {
        cards_remaining = new int[]{8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};
        low = 48;
        high = 56;
    }
    
    private void calculateHighPercentage() {
        percentage =  high / MAX_HIGH;
    }
    
    private void calculateLowPercentage() {
        lowpercentage = low / MAX_LOW;
    }
    
    public double getHighPercentage(){
            return percentage;
    }
    
    public double getLowPercentage() {
        return lowpercentage;
    }
}
