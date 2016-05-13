package my.jade.ontology.agents.behaviours;

import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import my.jade.ontology.agents.BookBuyerAgent;
import my.jade.ontology.ontology.Book;
import my.jade.ontology.ontology.BookTradingOntology;
import my.jade.ontology.ontology.Costs;
import my.jade.ontology.ontology.Sell;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Michal on 2016-05-06.
 */
public class BookNegotiatorBehaviour extends Behaviour {
    private float bestPrice;
    private AID bestSeller;
    private MessageTemplate messageTemplate;
    private int step;
    private Set<AID> sellerAgents;
    private Action actionToNegotiate;
    private ACLMessage message;
    private BookBuyerAgent myAgent;
    private String codecName;
    private String ontologyName;
    private ACLMessage replyMessage;
    private Set<AID> repliesSenders;
    private Book bookForSale;

    public BookNegotiatorBehaviour(BookBuyerAgent bookSellerAgent, Set sellers, Book book, float maxPrice) {
        super(bookSellerAgent);
        this.myAgent = bookSellerAgent;
        this.sellerAgents = sellers;
        this.bestPrice = maxPrice;
        this.bookForSale = book;
        this.actionToNegotiate = this.createAction(book);
        this.codecName = new SLCodec().getName();
        this.ontologyName = BookTradingOntology.getInstance().getName();
        this.repliesSenders = new HashSet<>();
        this.step = 0;
    }

    private Action createAction(Book book) {
        Sell sellAction = new Sell();
        sellAction.setItem(book);
        return new Action(myAgent.getAID(), sellAction);
    }

    public void action() {
        switch (step) {
            case 0:
                this.handleCfp();
                break;
            case 1:
                this.handleProposalReply();
                break;
            case 2:
                this.handleProposals();
                break;
            case 3:
                this.handleInformReply();
                break;
        }
    }

    private void handleCfp() {
        this.prepareCfpMessage();
        this.sendMessage();
        this.createMessageTemplate();
        this.nextStep();
    }

    private void handleInformReply() {
        this.receiveReply();
        if (this.hasReceivedReply()) {
            if (this.isReplayAInform()) {
                this.handleInform();
                myAgent.doDelete();
            } else {
                System.out.println("Attempt failed: requested book already sold.");
            }
            this.nextStep();
        } else {
            block();
        }
    }

    private void handleInform() {
        try {
            ContentElement contentElement = myAgent.getContentManager().extractContent(this.replyMessage);
            if (contentElement instanceof Done) {
                Book book = ((Sell)((Action) (((Done)contentElement)).getAction()).getAction()).getItem();
                System.out.print(book + " successfully purchased from agent " + replyMessage.getSender().getName());
                System.out.println(" by price = " + bestPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isReplayAInform() {
        return replyMessage.getPerformative() == ACLMessage.INFORM;
    }

    private void handleProposals() {
        this.createAcceptProposalMessage();
        this.rewriteReplyContent();
        this.addMessageReceiver();
        this.setConversationId();
        this.setReplyWith();
        this.sendMessage();
        this.createMessageTemplate();
        this.prepareRejectProposalMessage();
        this.setConversationId();
        this.setReplyWith();
        this.sendMessage();
        this.nextStep();
    }

    private void prepareRejectProposalMessage() {
        this.message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
        this.repliesSenders.remove(this.bestSeller);
        this.repliesSenders.forEach(this.message::addReceiver);
    }

    private void rewriteReplyContent() {
        this.message.setContent(this.replyMessage.getContent());
        this.message.setLanguage(this.replyMessage.getLanguage());
        this.message.setOntology(this.replyMessage.getOntology());
    }

    private void addMessageReceiver() {
        this.message.addReceiver(this.bestSeller);
    }

    private void createAcceptProposalMessage() {
        this.message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
    }

    private void handleProposalReply() {
        this.receiveReply();

        if (this.hasReceivedReply()) {
            this.checkSender();
            if (this.isReplyAProposal()) {
                this.handlePropose();
            }
            if (this.hasReceivedAllReplies()) {
                this.nextStep();
            }
        } else {
            super.block();
        }
    }

    private void checkSender() {
        this.repliesSenders.add(this.replyMessage.getSender());
    }

    private boolean hasReceivedAllReplies() {
        return this.repliesSenders.equals(this.sellerAgents); // TODO: Danger! Sellers list can change!
    }

    private void handlePropose() {
        try {
            ContentElement contentElement = super.myAgent.getContentManager().extractContent(this.replyMessage);
            if (contentElement instanceof Costs) {
                float price = ((Costs) contentElement).getPrice();
                System.out.println("Received Proposal at " + price + " when maximum acceptable price was " + this.bestPrice);
                if (this.bestSeller == null || price < this.bestPrice) {
                    this.bestSeller = this.replyMessage.getSender();
                    this.bestPrice = price;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }           
    }

    private void prepareCfpMessage() {
        this.createCfpMessage();
        this.addReceivers();
        this.setLanguage();
        this.setOntology();
        this.fillContent();
        this.setConversationId();
        this.setReplyWith();
    }

    private boolean isReplyAProposal() {
        return replyMessage.getPerformative() == ACLMessage.PROPOSE;
    }

    private boolean hasReceivedReply() {
        return replyMessage != null;
    }

    private void receiveReply() {
        this.replyMessage = myAgent.receive(messageTemplate);
    }

    private void nextStep() {
        step++;
    }

    private void createMessageTemplate() {
        messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                MessageTemplate.MatchInReplyTo(this.message.getReplyWith()));
    }

    private void sendMessage() {
        super.myAgent.send(message);
    }

    private void setReplyWith() {
        message.setReplyWith("message" + System.currentTimeMillis());
    }

    private void setConversationId() {
        this.message.setConversationId("book-trade");
    }

    private void fillContent() {
        try {
            myAgent.getContentManager().fillContent(this.message, this.actionToNegotiate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOntology() {
        this.message.setOntology(this.ontologyName);
    }

    private void setLanguage() {
        this.message.setLanguage(this.codecName);
    }

    private void addReceivers() {
        this.sellerAgents.forEach(message::addReceiver);
    }

    private void createCfpMessage() {
        this.message = new ACLMessage(ACLMessage.CFP);
    }

    public boolean done() {
        if (this.failureCondition()) {
            System.out.println("Attempt failed: " + this.bookForSale + " not available for sale");
        }
        return (this.failureCondition() || this.successCondition());
    }

    private boolean successCondition() {
        return step == 4;
    }

    private boolean failureCondition() {
        return step == 2 && bestSeller == null;
    }
}
