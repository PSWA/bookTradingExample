package my.jade.ontology.agents;

import jade.core.*;

import java.util.*;

import jade.content.lang.sl.SLCodec;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import my.jade.ontology.agents.behaviours.PurchaseManager;
import my.jade.ontology.agents.behaviours.SellerFindingBehaviour;
import my.jade.ontology.ontology.Book;
import my.jade.ontology.ontology.BookTradingOntology;

public class BookBuyerAgent extends BookAgent {
    private Set<AID> sellerAgents = new HashSet<>();

    protected void setup() {
        System.out.println("Buyer-agent " + getAID().getName() + " is ready.");

        this.setupContentManager();

        addBehaviour(new SellerFindingBehaviour(this, this.sellerAgents));

        Book book = new Book();
        book.setTitle("LoTR");
        this.purchase(book, 50, super.getTime(0, 0, 30, 0));
    }



    protected void purchase(Book book, int maxPrice, Date deadline) {
        addBehaviour(new PurchaseManager(this, this.sellerAgents, book, maxPrice, deadline));
    }

    private void setupContentManager() {
        super.getContentManager().registerLanguage(new SLCodec());
        super.getContentManager().registerOntology(BookTradingOntology.getInstance());
    }

    protected void takeDown() {
        System.out.println("Buyer-agent " + getAID().getName() + "terminated.");
    }

}


