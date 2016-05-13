package my.jade.semantic.capabilities;

import jade.semantics.actions.SemanticActionTable;
import jade.semantics.interpreter.DefaultCapabilities;
import jade.semantics.interpreter.SemanticInterpretationPrincipleTable;
import jade.semantics.kbase.KBase;
import jade.semantics.kbase.filters.FilterKBase;
import my.jade.semantic.kbase.assertfilters.adapters.DogNamePredicateBeliefFilterAdapter;
import my.jade.semantic.actions.SellDogSemanticAction;
import my.jade.semantic.interpretationprinciples.SellDogDone;
import my.jade.semantic.interpretationprinciples.SellDogPrinciple;

/**
 * Created by Michal on 2016-04-14.
 */
public class FirstCapabilities extends DefaultCapabilities {
    @Override
    protected KBase setupKbase() {
        FilterKBase kb = (FilterKBase)super.setupKbase();

        kb.addKBAssertFilter(new DogNamePredicateBeliefFilterAdapter());

        return kb;
    }

    @Override
    protected SemanticInterpretationPrincipleTable setupSemanticInterpretationPrinciples() {
        SemanticInterpretationPrincipleTable table = super.setupSemanticInterpretationPrinciples();

        table.addSemanticInterpretationPrinciple(new SellDogDone(this));
        table.addSemanticInterpretationPrinciple(new SellDogPrinciple(this));

        return table;
    }


}
