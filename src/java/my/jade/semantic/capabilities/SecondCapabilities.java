package my.jade.semantic.capabilities;

import jade.semantics.actions.SemanticActionTable;
import jade.semantics.interpreter.DefaultCapabilities;
import my.jade.semantic.actions.SellDogSemanticAction;

/**
 * Created by Michal on 2016-04-15.
 */
public class SecondCapabilities extends DefaultCapabilities {

    @Override
    protected SemanticActionTable setupSemanticActions() {
        SemanticActionTable table = super.setupSemanticActions();

        table.addSemanticAction(new SellDogSemanticAction(this));

        return table;
    }
}
