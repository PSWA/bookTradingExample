package my.jade.ontology.agents.behaviours;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import my.jade.ontology.ontology.Book;
import my.jade.ontology.ontology.Costs;
import my.jade.ontology.ontology.Sell;

import java.util.Map;

/**
 * Created by Michal on 2016-05-06.
 */
public class OfferRequestsServer extends CyclicBehaviour {
    private final Map<Book, PriceManager> catalogue;
    private ACLMessage message;
    private Book book;
    private ACLMessage reply;
    private PriceManager priceManager;
    private Costs costs;

    public OfferRequestsServer(Agent a, Map catalogue) {
        super(a);
        this.catalogue = catalogue;
    }

    public void action() {
        this.receiveMessage();
        if (this.hasReceivedMessage()) {
            this.extractBook();
            this.PrintBeginningInformation();
            this.getPriceManager();
            if (this.sendingProposeCondition())
                this.handlePropose();
            else
                this.handleRefuse();
            this.printFinalInformation();
        } else
            this.block();
        }

    private boolean hasReceivedMessage() {
        return this.message != null;
    }

    private void printFinalInformation() {
        System.out.println(reply.getPerformative() == ACLMessage.PROPOSE ? "Sent Proposal to sell at " + this.costs.getPrice() : "Refused Proposal as the book is not for sale");
    }

    private void handlePropose() {
        this.createReply();
        this.setPerformative(ACLMessage.PROPOSE);
        this.createCostsPredicate();
        this.fillContent();
        this.sendReply();
    }

    private void createCostsPredicate() {
        this.costs = new Costs();
        costs.setItem(this.book);
        float price = this.priceManager.getCurrentPrice();
        costs.setPrice(price);
    }

    private void fillContent() {
        try {
            this.myAgent.getContentManager().fillContent(this.reply, this.costs);
        } catch (Codec.CodecException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        }
    }

    private void handleRefuse() {
        this.createReply();
        this.setPerformative(ACLMessage.REFUSE);
        this.sendReply();
    }

    private void setPerformative(int performative) {
        this.reply.setPerformative(performative);
    }

    private boolean sendingProposeCondition() {
        return this.priceManager != null;
    }

    private void PrintBeginningInformation() {
        System.out.println("Received Proposal to buy " + this.book);
    }

    private void extractBook() {
        try {
            ContentManager cm = super.myAgent.getContentManager();
            Action act = (Action) cm.extractContent(message);
            Sell sellAction = (Sell) act.getAction();
            this.book = sellAction.getItem();
        } catch (Exception oe) {
            this.handleException();
        }
    }

    private void handleException() {
        this.createReply();
        this.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        this.sendReply();
    }

    private void sendReply() {
        super.myAgent.send(this.reply);
    }

    private void createReply() {
        this.reply = this.message.createReply();
    }

    private void receiveMessage() {
        this.message = super.myAgent.receive();
    }

    public void getPriceManager() {
        this.priceManager = this.catalogue.get(this.book.toString());
    }
}
