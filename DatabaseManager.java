package Class6_3;

import java.sql.*;   //JDBC知识>>>>>>
import java.util.ArrayList;
import java.util.List;
import java.util.*;

// 数据库管理类，负责数据库连接和操作
public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/moviedb";
    private static final String USER = "root";
    private static final String PASSWORD = "Lvlin049211#";

    // 连接到数据库
    public Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 创建 Movies 表
    public void createTable() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            String sqlDrop = "DROP TABLE IF EXISTS Movies";
            String sql = "CREATE TABLE Movies (" +
                    "MovieID INT AUTO_INCREMENT PRIMARY KEY," +
                    "Title VARCHAR(1000)," +
                    "Director VARCHAR(255)," +
                    "Score DOUBLE," +
                    "Votes INT," +
                    "MainActor VARCHAR(10000)," +
                    "Screenwriter VARCHAR(1000)," +
                    "Country VARCHAR(1000)," +
                    "Language VARCHAR(1000)," +
                    "Type VARCHAR(255),"+
                    "Year VARCHAR(255)," +
                    "Length INT"+
                    ")";
            stmt.execute(sqlDrop);
            stmt.executeUpdate(sql);
            System.out.println("Table Movies created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 插入电影数据
    public void insertMovie(Movie movie) {
        String sql = "INSERT INTO Movies (Title, Director, Score, Votes, MainActor, Screenwriter, Country, Language,Type,Year, Length) VALUES (?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getDirector());
            pstmt.setDouble(3, movie.getScore());
            pstmt.setInt(4, movie.getVotes());
            pstmt.setString(5, movie.getMainActor());
            pstmt.setString(6, movie.getScreenwriter());
            pstmt.setString(7, movie.getCountry());
            pstmt.setString(8, movie.getLanguage());
            pstmt.setString(9, movie.getType());
            pstmt.setString(10,movie.getYear());
            pstmt.setInt(11, movie.getLength());
            
            pstmt.executeUpdate(); // 执行插入数据的 SQL 语句
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    
    // 导演电影数量最多的导演
    public String getDirectorWithMostMovies() {
        String sql = "SELECT Director, COUNT(*) AS MovieCount FROM Movies WHERE Director !='null'  GROUP BY Director ORDER BY MovieCount DESC LIMIT 1";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("Director") + " - " + rs.getInt("MovieCount") + " movies";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    

    // 获取评分最高的电影
    public List<String> getHighestRatedMovies() {
        String sql = "SELECT Title, Score FROM Movies WHERE Score = (SELECT MAX(Score) FROM Movies) ORDER BY Score DESC";
        List<String> movies = new ArrayList<>();
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String movieInfo = rs.getString("Title") + " - " + rs.getDouble("Score") + " 分数";
                movies.add(movieInfo);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return movies;
    }

    // 获取评价人数最多的电影
    public String getMostVotedMovie() {
        String sql = "SELECT Title, Votes FROM Movies ORDER BY Votes DESC LIMIT 1";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("Title") + " - " + rs.getInt("Votes") + " votes";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取出演电影数目最多的主演
    public String getActorWithMostMovies() {
        String sql = "SELECT MainActor FROM Movies WHERE MainActor !='null'";
        Map<String,Integer> actorCountMap = new HashMap<>();
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	String mainActors = rs.getString("MainActor"); 
            	if (mainActors != null && !mainActors.isEmpty()) {
                    String[] actors = mainActors.split("/");
                    for (String actor : actors) {
                        actor = actor.trim();
                        actorCountMap.put(actor, actorCountMap.getOrDefault(actor, 0) + 1);//有这个key则value+1,无则加进去value=0
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        String mostFrequentActor = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : actorCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostFrequentActor = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return mostFrequentActor != null ? mostFrequentActor + " - " + maxCount + " movies" : null;
    }
  
    // 获取参与电影数目最多的编剧
    public String getScreenwriterWithMostMovies() {
    	 String sql = "SELECT Screenwriter FROM Movies WHERE Screenwriter !='null' ";
         Map<String,Integer> ScreenwriterCountMap = new HashMap<>();
         try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
             while (rs.next()) {
             	String Screenwriter = rs.getString("Screenwriter"); 
             	if (Screenwriter != null && !Screenwriter.isEmpty()) {
                     String[] Screenwriters = Screenwriter.split("/");
                     for (String S: Screenwriters) {
                    	 S = S.trim();
                         ScreenwriterCountMap.put(S, ScreenwriterCountMap.getOrDefault(S, 0) + 1);
                     }
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         String mostFrequenter = null;
         int maxCount = 0;
         for (Map.Entry<String, Integer> entry : ScreenwriterCountMap.entrySet()) {
             if (entry.getValue() > maxCount) {
                 mostFrequenter = entry.getKey();
                 maxCount = entry.getValue();
             }
         }

         return mostFrequenter != null ? mostFrequenter + " - " + maxCount + " movies" : null;
    }

    // 查询满足条件的电影getMoviesByCriteria
    public List<Movie> getMoviesByCriteria(String country, String director, String language, String mainActor,String Type, String Year,Integer minLength, Integer maxLength) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Movies WHERE 1=1");
        //可以存储任何类型的list
        List<Object> params = new ArrayList<>();
        
        //
        if (country != null && !country.isEmpty()) {
            // 对国家进行模糊匹配处理     //美国 / 英国   当输入条件是包含英国或美国时都符合条件
            String[] countries = country.split("/");
            sql.append(" AND (");
            for (int i = 0; i < countries.length; i++) {
                sql.append("Country LIKE ?");
                if (i < countries.length - 1) {
                    sql.append(" OR ");
                }
                params.add("%" + countries[i].trim() + "%");
            }
            sql.append(")");
            //////  >>>>    .....AND (Country LIKE ? OR Country LIKE ?.......)
        }
        
        if (language != null && !language.isEmpty()) {
            // 对语言进行模糊匹配处理
            String[] languages = language.split("/");
            sql.append(" AND (");
            for (int i = 0; i < languages.length; i++) {
                sql.append("Language LIKE ?");
                if (i < languages.length - 1) {
                    sql.append(" OR ");
                }
                params.add("%" + languages[i].trim() + "%");
            }
            sql.append(")");
        }
        
        if (mainActor != null && !mainActor.isEmpty()) {
            // 对主演进行模糊匹配处理
            String[] mainActors = mainActor.split("/");
            sql.append(" AND (");
            for (int i = 0; i < mainActors.length; i++) {
                sql.append("MainActor LIKE ?");
                if (i < mainActors.length - 1) {
                    sql.append(" OR ");
                }
                params.add("%" + mainActors[i].trim() + "%");
            }
            sql.append(")");
        }
        if (Type != null && !Type.isEmpty()) {
            // 对类型进行模糊匹配处理     //剧情/科幻/悬疑/冒险   当输入条件是剧情、科幻、悬疑或冒险时都符合条件
            String[] countries = Type.split("/");
            sql.append(" AND (");
            for (int i = 0; i < countries.length; i++) {
                sql.append("Type LIKE ?");
                if (i < countries.length - 1) {
                    sql.append(" OR ");
                }
                params.add("%" + countries[i].trim() + "%");
            }
            sql.append(")");
        }
        
        if (Year != null && !Year.isEmpty()) {
            // 对年份进行模糊匹配处理，例如 "2012-11-22(中国大陆)/2012-09-28(纽约电影节)/2011-11-21(美国)"
            String[] years = Year.split("/");
            sql.append(" AND (");
            for (int i = 0; i < years.length; i++) {
                sql.append("Year LIKE ?");
                if (i < years.length - 1) {
                    sql.append(" OR ");
                }
                params.add("%" + years[i].trim() + "%");
            }
            sql.append(")");
        }
        
        
        if (director != null && !director.isEmpty()) {
            sql.append(" AND Director = ?");
            params.add(director);
        }
        if (minLength != null) {
            sql.append(" AND Length >= ?");
            params.add(minLength);
        }
        if (maxLength != null) {
            sql.append(" AND Length <= ?");
            params.add(maxLength);
        }

        List<Movie> movies = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
        
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof String) {
                    pstmt.setString(i + 1, (String) params.get(i));//设置预编译语句的参数值。
                } else if (params.get(i) instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) params.get(i));
                }
            }
        	//pstmt.executeQuery()：用于执行返回结果集的 SQL 查询（如 SELECT 语句）。
        	//pstmt.executeUpdate()：用于执行更新操作（如 INSERT、UPDATE、DELETE 语句），返回更新的行数。
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString("Title");
                String movieDirector = rs.getString("Director");
                double score = rs.getDouble("Score");
                int votes = rs.getInt("Votes");
                String movieMainActor = rs.getString("MainActor");
                String screenwriter = rs.getString("Screenwriter");
                String movieCountry = rs.getString("Country");
                String movieLanguage = rs.getString("Language");
                String type = rs.getString("Type");
                String year = rs.getString("Year");
                int movieLength = rs.getInt("Length");
                
                movies.add(new Movie(title, movieDirector, score, votes, movieMainActor, screenwriter, movieCountry, type,year,movieLanguage, movieLength));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
    
}
