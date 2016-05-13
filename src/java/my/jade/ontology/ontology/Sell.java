package my.jade.ontology.ontology;
/** Class associated to the SELL schema
 **/

import jade.content.AgentAction;

public class Sell implements AgentAction {
    private Book item;

    public Book getItem() {
        return item;
    }

    public void setItem(Book item) {
        this.item = item;
    }

}
