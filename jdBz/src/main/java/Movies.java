public class Movies {



    private String titleId;
    private int year;



    private String director;
    private String title;

    private String genre;

    //dont know yet
    private String type;

    public Movies(){

    }

    public Movies(String name, String id, int age, String type, String gen, String dir) {
        this.title = name;
        this.year = age;
        this.titleId  = id;
        this.type = type;
        this.genre = gen;
        this.director = dir;
    }
    public int getAge() {
        return year;
    }

    public void setAge(int age) {
        this.year = age;
    }

    public String getId() {
        return titleId;
    }

    public void setId(String id) {
        this.titleId = id;
    }

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String gen) {
        this.genre = gen;
    }
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Movies Details - ");
        sb.append(", ");
        sb.append(" Id: " + getId());
        sb.append(" - Title: " + getName());
        sb.append(", ");

        sb.append("Year: | " + getAge());
        sb.append(", ");
        sb.append(" | Genre:" + getGenre());
        sb.append(" --> dir: " + getDirector());
        sb.append(".");

        return sb.toString();
    }
}
