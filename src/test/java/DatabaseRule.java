import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/line_queue_test", null, null);
   }

  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String userDeleteQuery = "DELETE FROM users *;";
      String restaurantDeleteQuery = "DELETE FROM restaurants *;";
      String check_insDeleteQuery = "DELETE FROM check_ins *;";
      con.createQuery(userDeleteQuery).executeUpdate();
      con.createQuery(restaurantDeleteQuery).executeUpdate();
      con.createQuery(check_insDeleteQuery).executeUpdate();
    }
  }
}
