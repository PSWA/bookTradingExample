package my.jade.semantic.actions;

import jade.semantics.actions.OntologicalAction;
import jade.semantics.behaviours.OntoActionBehaviour;
import jade.semantics.behaviours.SemanticBehaviour;
import jade.semantics.interpreter.SemanticCapabilities;
import jade.semantics.lang.sl.tools.SL;
import my.jade.semantic.agents.FirstSemanticAgent;

/**
 * Created by Michal on 2016-04-15.
 */
public class SellDogSemanticAction extends OntologicalAction {
    public SellDogSemanticAction(SemanticCapabilities capabilities) {
        super(capabilities, FirstSemanticAgent.SELL_ACTION_TERM,
                SL.formula("(not (for_sale ??what ??actor))"),
                SL.formula("(for_sale ??what ??actor)"));
    }

    @Override
    public void perform(OntoActionBehaviour behaviour) {

        behaviour.setState(SemanticBehaviour.SUCCESS);
    }
}
