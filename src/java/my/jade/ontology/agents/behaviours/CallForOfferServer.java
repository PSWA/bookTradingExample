package my.jade.ontology.agents.behaviours;

import jade.content.ContentElementList;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import my.jade.ontology.agents.BookSellerAgent;
import my.jade.ontology.ontology.Book;
import my.jade.ontology.ontology.BookTradingOntology;
import my.jade.ontology.ontology.Costs;
import my.jade.ontology.ontology.Sell;

import java.util.Map;

/**
 * Created by Michal on 2016-05-05.
 */
public class CallForOfferServer extends ContractNetResponder {

    float price;
    private Map catalogue;

    public CallForOfferServer(Agent agent, MessageTemplate messageTemplate, Map catalogue) {
        super(agent, messageTemplate);
        this.catalogue = catalogue;
    }

    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        ACLMessage reply = cfp.createReply();

        try {
            ContentManager cm = myAgent.getContentManager();
            Action act = (Action) cm.extractContent(cfp);
            Sell sellAction = (Sell) act.getAction();
            Book book = sellAction.getItem();
            System.out.println("Received Proposal to buy " + book.getTitle());
            PriceManager pm = (PriceManager) catalogue.get(book.getTitle());
            if (pm != null) {
                reply.setPerformative(ACLMessage.PROPOSE);
                ContentElementList cel = new ContentElementList();
                cel.add(act);
                Costs costs = new Costs();
                costs.setItem(book);
                price = pm.getCurrentPrice();
                costs.setPrice(price);
                cel.add(costs);
                cm.fillContent(reply, cel);
            } else {
                reply.setPerformative(ACLMessage.REFUSE);
            }
        } catch (OntologyException oe) {
            oe.printStackTrace();
            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        } catch (Codec.CodecException ce) {
            ce.printStackTrace();
            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        } catch (Exception e) {
            e.printStackTrace();
            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        }

        System.out.println(reply.getPerformative() == ACLMessage.PROPOSE ? "Sent Proposal to sell at " + price : "Refused Proposal as the book is not for sale");
        return reply;
    }

    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        ACLMessage inform = accept.createReply();
        inform.setPerformative(ACLMessage.INFORM);
        inform.setContent(Float.toString(price));
        System.out.println("Sent Inform at price " + price);
        return inform;
    }

}
