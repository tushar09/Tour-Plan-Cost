package club.tushar.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class Generator {
    public static void main(String [] args){
        Schema schema = new Schema(2, "club.tushar.tourplancost.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"../app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(Schema schema) {
        addTourEntities(schema);
        addTourEventCostEntities(schema);
    }

    private static Entity addTourEntities(Schema schema) {
        Entity tour = schema.addEntity("Tour");
        tour.addIdProperty().autoincrement();
        tour.addStringProperty("name");
        tour.addLongProperty("startDate");
        tour.addLongProperty("endDate");
        tour.addStringProperty("description");
        return tour;
    }

    private static Entity addTourEventCostEntities(Schema schema) {
        Entity tour = schema.addEntity("TourEventCost");
        tour.addIdProperty().autoincrement();
        tour.addLongProperty("tourId");
        tour.addStringProperty("name");
        tour.addLongProperty("Date");
        tour.addIntProperty("cost");
        return tour;
    }
}
