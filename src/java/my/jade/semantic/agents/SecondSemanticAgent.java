package my.jade.semantic.agents;

import jade.semantics.interpreter.SemanticAgent;
import my.jade.semantic.capabilities.SecondCapabilities;

/**
 * Created by Michal on 2016-04-15.
 */
public class SecondSemanticAgent extends SemanticAgent {
    public SecondSemanticAgent() {
        super(new SecondCapabilities());
    }

    @Override
    public void setup() {
        super.setup();

        super.getSemanticCapabilities().interpret("(for_sale \"Fafik\" ??myself)");
    }
}
