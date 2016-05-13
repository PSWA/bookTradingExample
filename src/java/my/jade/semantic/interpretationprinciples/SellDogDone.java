package my.jade.semantic.interpretationprinciples;

import jade.semantics.interpreter.SemanticCapabilities;
import jade.semantics.interpreter.SemanticRepresentation;
import jade.semantics.interpreter.sips.adapters.ActionDoneSIPAdapter;
import jade.semantics.lang.sl.tools.MatchResult;
import jade.util.leap.ArrayList;

import static my.jade.semantic.agents.FirstSemanticAgent.SELL_ACTION_EXPRESSION;

/**
 * Created by Michal on 2016-04-15.
 */
public class SellDogDone extends ActionDoneSIPAdapter {
    public SellDogDone(SemanticCapabilities capabilities) {
        super(capabilities, SELL_ACTION_EXPRESSION);
    }

    @Override
    protected ArrayList doApply(MatchResult matchResult, ArrayList results, SemanticRepresentation semanticRepresentation) {
        System.out.println("SellDogDone -> matchResult: " + matchResult + ", results: " + results + ", SR: " + semanticRepresentation);
        return results;
    }
}
