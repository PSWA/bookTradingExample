package my.jade.ontology.ontology;
/**
 * Section 5.1.3.2 Page 85
 * Java class representing a Book
 **/

// Class associated to the BOOK schema
import jade.content.Concept;
import jade.util.leap.List;

public class Book implements Concept {
    private String title;
    private List authors;
    private String editor;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List getAuthors() {
        return authors;
    }

    public void setAuthors(List authors) {
        this.authors = authors;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Override
    public String toString() {
        String string = "[ " +title + "; ";
        for (int i = 0; i < 0; i++)
            string += authors.get(i) + ", ";
        return string += this.editor + "]";
    }
}

