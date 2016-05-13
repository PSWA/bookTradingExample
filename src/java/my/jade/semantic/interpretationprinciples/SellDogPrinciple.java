package my.jade.semantic.interpretationprinciples;

import jade.semantics.interpreter.SemanticCapabilities;
import jade.semantics.interpreter.SemanticRepresentation;
import jade.semantics.interpreter.sips.adapters.ApplicationSpecificSIPAdapter;
import jade.semantics.lang.sl.tools.MatchResult;
import jade.util.leap.ArrayList;

import static my.jade.semantic.agents.FirstSemanticAgent.I_DONE_SELL_ACTION;

/**
 * Created by Michal on 2016-04-15.
 */
public class SellDogPrinciple extends ApplicationSpecificSIPAdapter {
    public SellDogPrinciple(SemanticCapabilities capabilities) {
        super(capabilities, "(selling_price ??what ??price ??seller)");
    }

    @Override
    protected ArrayList doApply(MatchResult matchResult, ArrayList results, SemanticRepresentation semanticRepresentation) {
        System.out.println("SellDogPrinciple -> matchResult: " + matchResult + ", results: " + results + ", SR: " + semanticRepresentation);

        results.add(new SemanticRepresentation(I_DONE_SELL_ACTION.instantiate("actor", matchResult.term("seller"))
                .instantiate("object", matchResult.term("what"))
                .instantiate("price", matchResult.term("price"))
                .instantiate("buyer", this.myCapabilities.getAgentName())));
        this.myCapabilities.getMySemanticInterpretationTable().removeSemanticInterpretationPrinciple(this);

        return results;
    }
}
