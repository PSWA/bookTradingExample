package my.jade.ontology.agents.behaviours;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import my.jade.ontology.agents.BookBuyerAgent;
import my.jade.ontology.ontology.Book;

import java.util.Date;
import java.util.Set;

public class PurchaseManager extends TickerBehaviour {
    private Book book;
    private float maxPrice;
    private long deadline, initTime, deltaT;
    private Set<AID> sellers;
    protected BookBuyerAgent myAgent;

    public PurchaseManager(BookBuyerAgent a, Set<AID> sellerAgents, Book book, float maxPrice, Date d) {
        super(a, 6000);
        this.myAgent = a;
        this.sellers = sellerAgents;
        this.book = book;
        this.maxPrice = maxPrice;
        deadline = d.getTime();
        initTime = System.currentTimeMillis();
        deltaT = deadline - initTime;
        this.negotiate(0);
    }

    public void onTick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime > deadline) {
            System.out.println(myAgent.getLocalName() + " cannot buy book " + book);
            stop();
        } else {
            long elapsedTime = currentTime - initTime;
            float acceptablePrice = (float) (maxPrice * (1.0 * elapsedTime / deltaT));
            this.negotiate(acceptablePrice);
        }
    }

    private void negotiate(float acceptablePrice) {
        myAgent.addBehaviour(new BookNegotiatorBehaviour(this.myAgent, this.sellers, this.book, acceptablePrice));
    }
}
