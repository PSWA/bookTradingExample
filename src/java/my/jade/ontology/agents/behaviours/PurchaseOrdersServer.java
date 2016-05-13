package my.jade.ontology.agents.behaviours;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import my.jade.ontology.ontology.Book;
import my.jade.ontology.ontology.Costs;
import my.jade.ontology.ontology.Sell;

public class PurchaseOrdersServer extends CyclicBehaviour {
    private MessageTemplate messageTemplate;
    private ACLMessage message;
    private Book book;
    private ACLMessage reply;
    private Done done;

    public void action() {
        this.createMessageTemplate();
        this.receiveMessage();
        if (this.hasReceivedMessage()) {
            this.extractContent();
            this.createReply();
            if (contentCondition())
                this.handleProposal();
            else
                this.handleFailure();
            this.sendReply();
        } else
            this.block();
    }

    private boolean hasReceivedMessage() {
        return this.message != null;
    }

    private void sendReply() {
        this.myAgent.send(this.reply);
    }

    private boolean contentCondition() {
        return this.book != null;
    }

    private void extractContent() {
        try {
            ContentElement contentElement = myAgent.getContentManager().extractContent(this.message);
            if (contentElement instanceof Costs)
                this.book = ((Costs) contentElement).getItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleProposal() {
        this.setPerformative(ACLMessage.INFORM);
        this.createDone();
        this.fillContent();
    }

    private void fillContent() {
        try {
            this.myAgent.getContentManager().fillContent(this.reply, this.done);
        } catch (Codec.CodecException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        }
    }

    private void createDone() {
        Sell sell = new Sell();
        sell.setItem(this.book);
        Action action = new Action(myAgent.getAID(), sell);
        this.done = new Done(action);
    }

    private void handleFailure() {
        this.setPerformative(ACLMessage.FAILURE);
    }

    private void setPerformative(int performative) {
        this.reply.setPerformative(performative);
    }

    private void createReply() {
        this.reply = this.message.createReply();
    }

    private void receiveMessage() {
        this.message = myAgent.receive(this.messageTemplate);
    }

    private void createMessageTemplate() {
        this.messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
    }
}
