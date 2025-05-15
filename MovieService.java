package Class6_3;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
///接口与多态 知识>>>>>>>>>>>>
//MovieOperation 接口定义了电影操作的方法
interface MovieOperation {
    String getDirectorWithMostMovies();
    List<String> getHighestRatedMovie();
    String getMostVotedMovie();
    String getActorWithMostMovies();
    String getScreenwriterWithMostMovies();
    List<Movie> getMoviesByCriteria(String country, String director, String language, String mainActor,
                                    String type, String year, Integer minlength, Integer maxlength);
}
// 电影服务类，负责各个功能集中处理
public class MovieService implements MovieOperation{
    private DatabaseManager dbManager;
    private ExcelReader excelReader;

    public MovieService() {
        dbManager = new DatabaseManager();
        excelReader = new ExcelReader();
    }

    // 处理 Excel 文件，将数据存入数据库（使用多线程）
    public void processExcelFile(String filePath) {
    	List<Movie> movies = excelReader.readExcel(filePath);
        //数据库里面建个表
        dbManager.createTable();
        // 创建一个固定大小为 10 的线程池	
        ExecutorService executor = Executors.newFixedThreadPool(10); 
        
        //遍历每一部电影，
        for (Movie movie : movies) {
        	// 提交一个任务给线程池，插入电影数据到数据库
            executor.submit(() -> dbManager.insertMovie(movie));
        }
     
        //关闭线程池，表示不再接受新的任务，但会继续执行已经提交的任务，直至完成，。
        executor.shutdown();
        //关闭线程池并等待任务完成：（超时处理和中断处理机制）
        try {
            // 等待所有任务完成，设置最大等待时间为1小时
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                // 如果超时未完成，强制关闭线程池
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            // 在等待过程中，如果当前线程被中断，
        	//立即重新中断当前线程，并强制关闭线程池。
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // 获取电影数量最多的导演
    public String getDirectorWithMostMovies() {
        return dbManager.getDirectorWithMostMovies();
    }

    // 获取评分最高的电影
    public List<String> getHighestRatedMovie() {
    	return dbManager.getHighestRatedMovies();
    }

    // 获取评价人数最多的电影
    public String getMostVotedMovie() {
        return dbManager.getMostVotedMovie();
    }

    // 获取出演电影数目最多的主演
    public String getActorWithMostMovies() {
        return dbManager.getActorWithMostMovies();
    }

    // 获取参与电影数目最多的编剧
    public String getScreenwriterWithMostMovies() {
        return dbManager.getScreenwriterWithMostMovies();
    }

    // 获取满足特定条件的电影
    public List<Movie> getMoviesByCriteria(String country, String director, String language, String mainActor,String Type, String Year,Integer minlength,Integer maxlength) {
        return dbManager.getMoviesByCriteria(country, director, language, mainActor,Type,Year, minlength,maxlength);
    }
}
