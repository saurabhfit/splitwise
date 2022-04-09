package com.splitwise.model.split;

import com.splitwise.model.User;


public class PercentSplit extends Split{

    double percent;

    public PercentSplit(String userId, double percent) {
        super(userId);
        this.percent = percent;
    }

    public double getPercent(){
        return percent;
    }

    public void setPercent(double percent){
        this.percent = percent;
    }
}
