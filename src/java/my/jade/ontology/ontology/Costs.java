package my.jade.ontology.ontology;

/**
 * Section 5.1.3.2 Page 85
 * Java class representing a Costs
 **/

// Class associated to the COSTS schema

import jade.content.Predicate;

public class Costs implements Predicate {
    private Book item;
    private float price;

    public Book getItem() {
        return item;
    }

    public void setItem(Book item) {
        this.item = item;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

