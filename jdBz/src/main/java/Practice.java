import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class Practice {


    public static void main(String[] args) throws Exception  {
        String dbtype = "mysql";
        String dbname = "moviedbtest";
        String username = "mytestuser";
        String password = "mypassword";
        Hashtable<String, String> oldMovie = new Hashtable<String, String>();
        Hashtable<String, String> oldGenre = new Hashtable<String, String>();
        // Incorporate mySQL driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Connect to the test database
            Connection connection = DriverManager.getConnection("jdbc:" + dbtype + ":///"
                            + dbname + "?autoReconnect=true&useSSL=false",
                    username, password);

            if (connection != null) {
                System.out.println("Connection established!!");
                System.out.println();
            }

            // Create an execute an SQL statement to select all of table"Stars" records
            String query = "select movies.title , movies.id, genres.name , genres.id from movies, genres,genres_in_movies " +
                    "where genres.id =genres_in_movies.genreId and " +
                    "movies.id = genres_in_movies.movieId; ";

            PreparedStatement select = connection.prepareStatement(query);
            ResultSet result = select.executeQuery(query);

            // Get metatdata from stars; print # of attributes in table
            System.out.println("The results of the query");

            // print table's contents, field by field
            while (result.next()) {
                String title = result.getString("movies.title");
                String id = result.getString("movies.id");
                String genId = result.getString("genres.id");
                String genName =  result.getString("genres.name");
                oldMovie.put(title, id);
                oldGenre.put(genName,genId);
            }

            System.out.println(oldMovie);
            System.out.println(oldGenre);

        }
        catch(Exception e)
        {
            System.out.println("Error MSG: " + e.getMessage());
        }

    }





}
