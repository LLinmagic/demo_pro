package Class6_3;

//电影类，电影的数据模型
public class Movie {
    private String title;   // 片名0
    private String director;   // 导演11
    private double score;   // 评分1
    private int votes;   // 评价人数2
    private String mainActor;   // 主演13
    private String screenwriter;   // 编剧12
    private String country;   // 制片国家/地区14
    private String language;   // 语言15
    private int length;   // 片长17
    private String type;   // 类型10
    private String year;//年份16

    public Movie(String title, String director, double score, int votes, String mainActor, String screenwriter, String country, String language,String type,String year, int length) {
        this.title = title;
        this.director = director;
        this.score = score;
        this.votes = votes;
        this.mainActor = mainActor;
        this.screenwriter = screenwriter;
        this.country = country;
        this.language = language;
        this.length = length;
        this.type = type;
        this.year = year;
    }
    //getter.
    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public double getScore() {
        return score;
    }

    public int getVotes() {
        return votes;
    }

    public String getMainActor() {
        return mainActor;
    }

    public String getScreenwriter() {
        return screenwriter;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }

    public String getType() {
        return type;
    }
    public String getYear() {
    	return year;
    }

    public int getLength() {
        return length;
    }

}


