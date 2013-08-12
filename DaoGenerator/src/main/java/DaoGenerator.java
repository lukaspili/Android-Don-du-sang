import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by lukas on 6/18/13.
 */
public class DaoGenerator {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(4, "com.siu.android.dondusang.dao.model");
        schema.enableKeepSectionsByDefault();

        Entity entry = schema.addEntity("Center");
        entry.implementsSerializable();

        entry.setTableName("centers");

        entry.addIdProperty();

        /* Common attributes */
        entry.addStringProperty("city");
        entry.addStringProperty("description");
        entry.addDoubleProperty("latitude");
        entry.addDoubleProperty("longitude");

        /* Permanenent center */
        entry.addStringProperty("phone");
        entry.addStringProperty("email");
        entry.addStringProperty("region");

        /* Temporal center */
        entry.addDateProperty("date");
        entry.addDateProperty("start");
        entry.addDateProperty("end");
        entry.addStringProperty("type");

        /* News */
        Entity news = schema.addEntity("News");
        news.implementsSerializable();

        news.addIdProperty();
        news.addStringProperty("title");
        news.addStringProperty("content");
        news.addDateProperty("date");

        /* Don */
        Entity bloodEvent = schema.addEntity("BloodEvent");
        bloodEvent.implementsSerializable();

        bloodEvent.addIdProperty();
        bloodEvent.addIntProperty("type");
        bloodEvent.addDateProperty("date");

        new de.greenrobot.daogenerator.DaoGenerator().generateAll(schema, "../DonDonSuang/src/main/java");
    }
}
