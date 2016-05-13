package my.jade.ontology.agents;

import jade.core.Agent;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Michal on 2016-05-06.
 */
public abstract class BookAgent extends Agent {
    protected Date getTime(int hours, int minutes, int seconds, int millis) {
        seconds += millis / 100;
        seconds %= 100;
        minutes += seconds / 60;
        seconds %= 60;
        hours += minutes / 60;
        minutes %= 60;
        int days = hours / 24;
        hours %= 24;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + days);
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + hours);
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minutes);
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + seconds);
        cal.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND) + millis);
        return cal.getTime();
    }
}
