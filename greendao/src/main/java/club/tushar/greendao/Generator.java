package club.tushar.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

public class Generator {
    public static void main(String [] args){
        Schema schema = new Schema(6, "club.tushar.tourplancost.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"../app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(Schema schema) {
        Entity tour = addTourEntities(schema);
        addTourEventCostEntities(schema, tour);
    }

    private static Entity addTourEntities(Schema schema) {
        Entity tour = schema.addEntity("Tour");
        tour.addIdProperty().autoincrement();
        tour.addStringProperty("name").unique();
        tour.addLongProperty("total");
        tour.addLongProperty("startDate");
        tour.addLongProperty("endDate");
        tour.addStringProperty("description");
        return tour;
    }

    private static Entity addTourEventCostEntities(Schema schema, Entity tour) {
        Entity tourCost = schema.addEntity("TourEventCost");
        tourCost.addIdProperty().autoincrement();
        Property tourId = tourCost.addLongProperty("tourId").getProperty();
        tourCost.addStringProperty("eventName");
        Property date = tourCost.addLongProperty("date").getProperty();
        tourCost.addIntProperty("cost");

        ToMany tourToEvents = tour.addToMany(tourCost, tourId);
        tourToEvents.setName("TourTotalCost");
        tourToEvents.orderDesc(date);
        return tour;
    }
}
