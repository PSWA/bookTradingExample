package my.jade.ontology.agents;

import jade.domain.*;
import jade.domain.FIPAAgentManagement.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.*;
import my.jade.ontology.agents.behaviours.OfferRequestsServer;
import my.jade.ontology.agents.behaviours.PriceManager;
import my.jade.ontology.agents.behaviours.PurchaseOrdersServer;
import my.jade.ontology.ontology.Book;
import my.jade.ontology.ontology.BookTradingOntology;

import java.util.*;

public class BookSellerAgent extends BookAgent {
    private Map catalogue = new HashMap();

    private Codec codec = new SLCodec();
    private Ontology ontology = BookTradingOntology.getInstance();

    protected void setup() {
        System.out.println("Seller-agent "+getAID().getName()+" is ready.");

        this.registerService();

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);


        Book book = new Book();
        book.setTitle("LoTR");
        this.putForSale(book, 40.5f, 23.4f, super.getTime(0,5,0,0));

        addBehaviour(new OfferRequestsServer(this, this.catalogue));
        addBehaviour(new PurchaseOrdersServer());
    }

    private void registerService() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Book-selling");
        sd.setName(getLocalName()+"-Book-selling");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    protected void takeDown() {
        System.out.println("Seller-agent "+getAID().getName()+"terminating.");

        this.deregisterService();
    }

    private void deregisterService() {
        try {
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    public void putForSale(Book book, float initPrice, float minPrice, Date deadline) {
        addBehaviour(new PriceManager(this, this.catalogue, book, initPrice, minPrice, deadline));
    }

}

