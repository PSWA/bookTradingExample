package my.jade.semantic.kbase.assertfilters.adapters;

import jade.semantics.kbase.filters.KBAssertFilterAdapter;
import jade.semantics.lang.sl.grammar.Formula;
import jade.semantics.lang.sl.tools.MatchResult;
import jade.semantics.lang.sl.tools.SL;

/**
 * Created by Michal on 2016-04-15.
 */
public class DogNamePredicateBeliefFilterAdapter extends KBAssertFilterAdapter {
    public DogNamePredicateBeliefFilterAdapter() {
        super(SL.formula("(B ??agent (name Dog ??name))")); // (B ??agent (name Dog ??name))
    }

    @Override
    public Formula doApply(Formula formula, MatchResult match) {
        System.out.println("formula: " + formula + ", match: " + match);
        return super.doApply(formula, match);
    }
}
