package my.jade.ontology.agents.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import my.jade.ontology.agents.BookBuyerAgent;

import java.util.Set;
import java.util.Vector;

/**
 * Created by Michal on 2016-05-05.
 */
public class SellerFindingBehaviour extends TickerBehaviour {
    private Set sellers;

    public SellerFindingBehaviour(Agent agent, Set<AID> sellerAgents) {
        super(agent, 1000);
        this.sellers = sellerAgents;
    }

    protected void onTick() {
        this.searchService();
        this.updateTicker();
    }

    private void updateTicker() {
        this.reset(this.getPeriod()+100);
    }

    private void searchService() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Book-selling");
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            this.sellers.clear();
            for (int i = 0; i < result.length; ++i) {
                this.sellers.add(result[i].getName());
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
