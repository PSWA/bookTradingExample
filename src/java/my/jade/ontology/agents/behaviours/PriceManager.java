package my.jade.ontology.agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import my.jade.ontology.ontology.Book;

import java.util.Date;
import java.util.Map;

/**
 * Created by Michal on 2016-05-05.
 */
public class PriceManager extends TickerBehaviour {
    private Book book;
    private float currentPrice, initPrice, deltaP;
    private long initTime, deadline, deltaT;
    private Map catalogue;

    public PriceManager(Agent a, Map catalogue, Book book, float initialPrice, float minPrice, Date d) {
        super(a, 5000);
        this.catalogue = catalogue;
        this.book = book;
        initPrice = initialPrice;
        currentPrice = initPrice;
        deltaP = initPrice - minPrice;
        deadline = d.getTime();
        initTime = System.currentTimeMillis();
        deltaT = ((deadline - initTime) > 0 ? (deadline - initTime) : 60000);
    }

    public void onStart() {
        catalogue.put(book.toString(), this);
        super.onStart();
    }

    public void onTick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime > deadline) {
            System.out.println("Cannot sell book "+ book);
            this.catalogue.remove(book.toString());
            stop();
        } else {
            long elapsedTime = currentTime - initTime;
            currentPrice = (int) Math.round(initPrice - 1.0 * deltaP * (1.0 * elapsedTime / deltaT));
        }
    }

    public float getCurrentPrice() {
        return currentPrice;
    }
}
