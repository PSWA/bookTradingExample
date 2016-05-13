package my.jade.ontology.agents.behaviours;

import jade.content.ContentElementList;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import my.jade.ontology.agents.BookBuyerAgent;
import my.jade.ontology.ontology.Book;
import my.jade.ontology.ontology.BookTradingOntology;
import my.jade.ontology.ontology.Costs;
import my.jade.ontology.ontology.Sell;

import java.util.Vector;

public class BookNegotiator extends ContractNetInitiator {
    private String title;
    private float maxPrice;
    private PurchaseManager manager;

    public BookNegotiator(PurchaseManager manager, ACLMessage cfp, String title, int price) {
        super(manager.getAgent(), cfp);
        this.title = title;
        this.manager = manager;
        maxPrice = price;
        Book book = new Book();
        book.setTitle(this.title);
        Sell sellAction = new Sell();
        sellAction.setItem(book);
        Action act = new Action(myAgent.getAID(), sellAction);
        try {
            cfp.setLanguage(new SLCodec().getName());
            cfp.setOntology(BookTradingOntology.getInstance().getName());
            myAgent.getContentManager().fillContent(cfp, act);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleAllResponses(Vector responses, Vector acceptances) {
        ACLMessage bestOffer = null;
        float bestPrice = -1;
        for (int i = 0; i < responses.size(); i++) {
            ACLMessage rsp = (ACLMessage) responses.get(i);
            if (rsp.getPerformative() == ACLMessage.PROPOSE) {
                try {
                    ContentElementList cel = (ContentElementList) myAgent.getContentManager().extractContent(rsp);
                    float price = ((Costs) cel.get(1)).getPrice();
                    System.out.println("Received Proposal at " + price + " when maximum acceptable price was " + maxPrice);
                    if (bestOffer == null || price < bestPrice) {
                        bestOffer = rsp;
                        bestPrice = price;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for (int i = 0; i < responses.size(); i++) {
            ACLMessage rsp = (ACLMessage) responses.get(i);
            ACLMessage accept = rsp.createReply();
            if (rsp == bestOffer) {
                boolean acceptedProposal = (bestPrice <= maxPrice);
                accept.setPerformative(acceptedProposal ? ACLMessage.ACCEPT_PROPOSAL : ACLMessage.REJECT_PROPOSAL);
                accept.setContent(title);
                System.out.println(acceptedProposal ? "sent Accept Proposal" : "sent Reject Proposal");
            } else {
                accept.setPerformative(ACLMessage.REJECT_PROPOSAL);
            }
            acceptances.add(accept);
        }
    }

    protected void handleInform(ACLMessage inform) {
        int price = Integer.parseInt(inform.getContent());
        System.out.println("Book " + title + " successfully purchased. Price =" + price);
        manager.stop();
    }
}
