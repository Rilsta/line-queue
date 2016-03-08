import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class User {
  private int id;
  private String user_name;
  private String password;
  private String permission;
  private int score = 0;

  //CONSTRUCTOR//
  public User(String user_name) {
    this.user_name = user_name;
    this.password = password;
    this.permission = permission;
  }

  //GETTERS//
  public String getUserName() {
    return user_name;
  }

  public int getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  public String getPermission() {
    return permission;
  }

  @Override
  public boolean equals(Object otherUser){
    if (!(otherUser instanceof User)) {
      return false;
    } else {
      User newUser = (User) otherUser;
      return this.getUserName().equals(newUser.getUserName());
    }
  }

  //CREATE//
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO users(user_name, password, permission) VALUES (:user_name, :password, :permission)";
      this.id = (int) con.createQuery(sql,true)
      .addParameter("user_name", this.user_name)
      .addParameter("password", this.password)
      addParameter("permission", this.permission)
      .executeUpdate()
      .getKey();
    }
  }

  //READ//
  public static List<User> all() {
    String sql = "SELECT id, user_name FROM users";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .executeAndFetch(User.class);
    }
  }

  public static User find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM Users where id=:id";
      User user = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(User.class);
      return user;
    }
  }

  public List<Restaurant> getRestaurants() {
    String sql = "SELECT restaurants.* FROM users JOIN check_ins ON (users.id = check_ins.user_id) JOIN restaurants ON (check_ins.restaurant_id = restaurants.id) WHERE users.id = :user_id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .addParameter("user_id", id)
      .executeAndFetch(Restaurant.class);
    }
  }

  //UPDATE//
  public void update(String newVenueName) {
    this.user_name = newVenueName;
    String sql = "UPDATE users SET user_name = :newVenueName WHERE id=:id";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
      .addParameter("newVenueName", newVenueName)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

  public void addRestaurant(Restaurant restaurant) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO check_ins (restaurant_id, user_id) VALUES (:restaurant_id, :user_id)";
      con.createQuery(sql)
      .addParameter("restaurant_id", restaurant.getId())
      .addParameter("user_id", this.getId())
      .executeUpdate();
    }
  }

  public void assignRestaurant(int restaurant_id) {
    restaurant_id = restaurant_id;
    String sql = "UPDATE check_ins SET restaurant_id = :restaurant_id WHERE user_id=:user_id";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
         .addParameter("restaurant_id", restaurant_id)
         .addParameter("id", this.id)
         .executeUpdate();
    }
  }

  //DESTROY//
  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM users WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();

      String check_insQuery = "DELETE FROM check_ins WHERE user_id = :userId";
      con.createQuery(check_insQuery)
      .addParameter("userId", this.getId())
      .executeUpdate();
    }
  }
}
