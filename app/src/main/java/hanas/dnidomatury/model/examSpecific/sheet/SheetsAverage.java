package hanas.dnidomatury.model.examSpecific.sheet;

import java.io.Serializable;
import java.util.Observable;

public class SheetsAverage extends Observable implements Serializable {
    private double percentageSum;
    private double sheetsCounter;

    public SheetsAverage() {
        this.percentageSum = 0;
        this.sheetsCounter = 0;
    }

    public double getAverage() {
        return percentageSum / sheetsCounter;
    }

    void resetAverage() {
        percentageSum = 0;
        sheetsCounter = 0;
        setChanged();
        notifyObservers(percentageSum / sheetsCounter);
    }

    public void add(double points, double maxPoints) {
        percentageSum += points / maxPoints * 100;
        sheetsCounter++;
        setChanged();
        notifyObservers(percentageSum / sheetsCounter);
    }

    void subtract(double points, double maxPoints) {
        percentageSum -= points / maxPoints * 100;
        sheetsCounter--;
        setChanged();
        notifyObservers(percentageSum / sheetsCounter);
    }

    void updateAverage(double[] percentages) {
        double oldPercentage = percentages[0];
        double newPercentage = percentages[1];
        this.percentageSum -= oldPercentage;
        this.percentageSum += newPercentage;
        setChanged();
        notifyObservers(percentageSum / sheetsCounter);
    }
}
