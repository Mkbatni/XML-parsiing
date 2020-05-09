import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SAXParseMoviesM extends DefaultHandler {
    // all the records from XML
   static List<Movies> myMovie;
    public static int counter = 24;
    private String tempVal;
    boolean fid = false, dir = false, ct = false, yr = false ,title = false, movieTag = false;
    //to maintain context    to hold info about each record
    private Movies tempMovie;

    public SAXParseMoviesM() {

        myMovie = new ArrayList<Movies>();
    }

    public void runExample() {
        parseDocument();
        printData();
    }
    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private void printData() {

        System.out.println("No of Movies '" + myMovie.size() + "'.");
       /* Iterator<Movies> it = myMovie.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }*/
    }


    //Event Handlers  **key
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("directorfilms")) {
            //create a new instance of movie
            tempMovie = new Movies();
            tempMovie.setType(attributes.getValue("type"));

        }
        if (qName.equalsIgnoreCase("film")) {
            movieTag = true;
        }
        else if (qName.equalsIgnoreCase("fid")) {
             fid = true;
        }
        else if (qName.equalsIgnoreCase("dirname"))
        {
            dir = true;
        }
        else if (qName.equalsIgnoreCase("t")) {
            title = true;
        }
        else if (qName.equalsIgnoreCase("year")) {
             yr = true ;
        }
        else if (qName.equalsIgnoreCase("cat")) {
            ct = true;
        }
    }
    String fid_ = "NA", dir_ = "NA",ct_ = "NA", title_ = "NA";
    int year_ = 0;
    public void characters(char[] ch, int start, int length) throws SAXException {
      try
      {
          tempMovie = new Movies();
        if (fid) {
            //System.out.println("id " + new String(ch, start, length));

            fid_ = new String(ch, start, length);
            fid = false;
        }
        else if (dir)
        {
            //System.out.println("dr " + new String(ch, start, length));
            dir_ = new String(ch, start, length);
            dir = false;

        }
        else if (title) {
            //System.out.println("title " + new String(ch, start, length));

            title_ = new String(ch, start, length);
            title = false;
            //tempMovie.setName(title_);
        }  else if (yr) {
            String yrTemp = new String(ch, start, length);
            year_ = Integer.parseInt(yrTemp);
            //System.out.println(year_);
           /* if (Pattern.matches("[a-zA-Z]+", yrTemp) == true && yrTemp.length() == 4) {
                year_ = Integer.parseInt(yrTemp);

            }*/
            yr = false ;
        }
        else if (ct) {
            ct_ = new String(ch, start, length);
            ct = false;

        }
        else if(movieTag)
        {
            //System.out.println(fid_ + "-->" + title_ + "-->" + year_ + "-->" + dir_  + "-->" + ct_);
            tempMovie = new Movies(title_, fid_, year_, "null", ct_, dir_);
            myMovie.add(tempMovie);
            movieTag = false;
        }
        /*  Movies e = new Movies(title_, fid_, year_, "null", ct_);
          myMovie.add(e);*/

      }
        catch(NumberFormatException e)
        {

        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("director")) {
            //add it to the list

            /*tempMovie = new Movies(title_, fid_, year_, "null", ct_);
           myMovie.add(tempMovie);*/

        }


    }
    public void endDocument(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("directorfilms")) {
            //add it to the list
            //  myMovie.add(tempMovie);

        }
    }
    public static void handleJDB() throws InstantiationException, IllegalAccessException, ClassNotFoundException, Exception{
        String dbtype = "mysql";
        String dbname = "moviedbtest";
        String username = "mytestuser";
        String password = "mypassword";
        Hashtable<String, String> oldMovie = new Hashtable<String, String>();
        Hashtable<String, Integer> oldGenre = new Hashtable<String, Integer>();
        Hashtable<String, String> dupMovie = new Hashtable<String, String>();
        Hashtable<String, String> notDupMovie = new Hashtable<String, String>();
        //List <String> oldGenre = new ArrayList<String>();

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
            String query = "select distinct (movies.title) , movies.id from movies ;";
            String query2 = "select distinct (genres.id), genres.name from genres ;";
            PreparedStatement select = connection.prepareStatement(query);
            ResultSet result = select.executeQuery(query);

            // print table's contents, field by field
            while (result.next()) {
                String title = result.getString("movies.title");
                String id = result.getString("movies.id");
                oldMovie.put(title, id);

            }

           select = connection.prepareStatement(query2);
           result = select.executeQuery(query2);
            while (result.next()) {
                String genName = result.getString("genres.name");
                int genID = result.getInt("genres.id");
                      oldGenre.put(genName, genID);
            }
            result.close();
            select.close();


        /*    System.out.println(oldMovie);
            System.out.println(oldGenre);*/

            int[] iNoRows=null;
            int[] iNoRows2=null;
            int[] iNoRows3=null;
            int[] iNoRows4=null;
            connection.setAutoCommit(false);

           /*
           * INSERT INTO movies VALUES('tt0421974','Sky Fighters',2005,'Gérard Pirès');
           * INSERT INTO ratings VALUES('tt0421974',5.8,3920);  @ rating 9.9 0
           *
           * INSERT INTO genres VALUES(1,'Action');
           * INSERT INTO genres_in_movies VALUES(1,'tt0421974');
            */
            String sqlInsertToMovie = " INSERT INTO movies VALUES(?,?,?,?);";
            String sqlInsertToRating = " INSERT INTO ratings VALUES(?,9.9,0);";
            String sqlInsertToGenre = " INSERT INTO genres VALUES(?,?);";
            String sqlInsertToGenre_in_movie = " INSERT INTO genres_in_movies VALUES(?,?);";


            PreparedStatement psInsertRecord= null;
            PreparedStatement psInsertRating=null;
            PreparedStatement psGenre=null;
            PreparedStatement psGemreInMovie=null;


            psInsertRecord = connection.prepareStatement(sqlInsertToMovie);
            psInsertRating = connection.prepareStatement(sqlInsertToRating);
            psGenre  = connection.prepareStatement(sqlInsertToGenre);
            psGemreInMovie = connection.prepareStatement(sqlInsertToGenre_in_movie);


        // ------------------- Insertion

            int limitPerBatch = 50;
            int iter = 0 ;
            for (int i = 0; i < myMovie.size() ; i++) {
                Movies mv = myMovie.get(i);
                String newMovie = mv.getName();
                // if it is not a duplicate


                if(!oldMovie.containsKey(newMovie) && !oldMovie.containsValue(mv.getId()))
                {
                     if (!notDupMovie.containsKey(mv.getId()) && !notDupMovie.contains(mv.getName()))
                    {
                        notDupMovie.put(mv.getId(),mv.getName());
                    }
                    else
                    {
                        dupMovie.put(mv.getId(),mv.getName());
                        continue;
                    }

                    psInsertRecord.setString(1,myMovie.get(i).getId());
                    psInsertRecord.setString(2,myMovie.get(i).getName());
                    psInsertRecord.setInt(3,myMovie.get(i).getAge());
                    psInsertRecord.setString(4,myMovie.get(i).getDirector());
                    psInsertRecord.addBatch();
                    // insert rating
                     psInsertRating.setString(1,myMovie.get(i).getId());
                     psInsertRating.addBatch();
                    // validate the genre than add it to the branch

                    String newMvGen = mv.getGenre();
                    if (newMvGen == "Comd")
                    {
                        newMvGen = "Comedy";
                    }
                    else if (newMvGen.startsWith("H**") )
                        newMvGen = "NA";
                    else
                    {
                        boolean genreValidation = false;
                        Set<String> keys = oldGenre.keySet();
                        for(String key: keys){
                            boolean validateP = key.matches("^"+ newMvGen + ".*");
                            if( validateP) {
                                newMvGen = key;
                                genreValidation = true;
                                break;
                            }
                        }   // it matches
                        if (!genreValidation)
                        {
                            // insert it
                            psGenre.setInt(1,counter);
                            psGenre.setString(2,newMvGen);
                            psGenre.addBatch();
                            oldGenre.put(newMvGen, counter++);
                        }
                    }
                    //Insert into genre_in_movie
                    int getGenID = 0;
                    getGenID = oldGenre.get(newMvGen);
                    psGemreInMovie.setInt(1, getGenID);
                    String MovieID =  myMovie.get(i).getId();
                    psGemreInMovie.setString(2, MovieID);
                    psGemreInMovie.addBatch();
                }// End of IF

                if ( limitPerBatch == iter)
                {
                    iNoRows=psInsertRecord.executeBatch();
                    connection.commit();
                    iNoRows2 = psInsertRating.executeBatch();
                    connection.commit();
                    if(psGenre != null){
                        iNoRows3 = psGenre.executeBatch();connection.commit();
                    }
                    psGemreInMovie.executeBatch();
                    connection.commit();

                iter = 0;
                }  iter++;
            }
            System.out.println("size of Inconsistant Data from Movies: " + dupMovie.size());
            System.out.println("size of Data from XML to DB: " + notDupMovie.size());
            System.out.println("size of original DB before insertion: " + oldMovie.size());
            System.out.println("Inconsistant Data from Movies: " + dupMovie);
            String sqlIndex = " CREATE INDEX id_index ON ratings (rating);";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sqlIndex);
            connection.commit();
            try {
                if(psInsertRecord!=null)
                {
                    psInsertRecord.close();
                    psInsertRating.close();
                    psGenre.close();
                    psGemreInMovie.close();
                    stmt.close();
                }
                if(connection!=null) connection.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());

        } catch(Exception e)
        {
            System.out.println("Error MSG: " + e.getMessage() );
        }

    }


    public static void main(String[] args) throws Exception {
        SAXParseMoviesM spe = new SAXParseMoviesM();
        spe.runExample();
        //System.out.printf("each Element counts: " +  counter);

        //Database Name needs to be changed
        handleJDB();


        //Genre will have counter # 24 as NA
}
}
