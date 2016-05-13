package my.jade.semantic.agents;

import jade.core.AID;
import jade.core.behaviours.WakerBehaviour;
import jade.semantics.interpreter.SemanticAgent;
import jade.semantics.interpreter.Tools;
import jade.semantics.lang.sl.grammar.*;
import jade.semantics.lang.sl.tools.SL;
import my.jade.semantic.capabilities.FirstCapabilities;

/**
 * Created by Michal on 2016-04-14.
 */
public class FirstSemanticAgent extends SemanticAgent {
    public static final Term SELL_ACTION_TERM
            = SL.term("(SELL :buyer ??buyer :object ??object :price ??price)");

    public static final ActionExpression SELL_ACTION_EXPRESSION
            = new ActionExpressionNode(new MetaTermReferenceNode("actor"), SELL_ACTION_TERM);

    public static final Formula I_DONE_SELL_ACTION
            = SL.formula("(I ??buyer (done "+SELL_ACTION_EXPRESSION+"))");

    public FirstSemanticAgent() {
        super(new FirstCapabilities());
    }

    @Override
    public void setup() {
        super.setup();
        Term seller = Tools.AID2Term(new AID("sas", AID.ISLOCALNAME));
        super.getSemanticCapabilities().interpret("(selling_price Fafik 60.2 " + seller + ")");
    }
}
