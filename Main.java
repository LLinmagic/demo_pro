package Class6_3;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    	//用 MovieOperation 接口引用 MovieService 对象
    	MovieOperation movieService = new MovieService();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        String filePath = "D:/豆瓣电影数据(1).xlsx";
        //强制类型转换movieService,调用 MovieService 特有方法:
        //但静态类型仍然是接口类型。
        ((MovieService) movieService).processExcelFile(filePath);

        while (!exit) {
            System.out.println("请选择一个功能:");
            System.out.println("1. 查询电影数量最多的导演");
            System.out.println("2. 查询评分最高的电影");
            System.out.println("3. 查询评价人数最多的电影");
            System.out.println("4. 查询出演电影数目最多的主演");
            System.out.println("5. 查询参与电影最多的编剧");
            System.out.println("6. 根据条件查询电影");
            System.out.println("0. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    System.out.println("电影数量最多的导演: " + movieService.getDirectorWithMostMovies());
                    break;
                case 2:
                    System.out.println("评分最高的电影: " );
                    for (String s:movieService.getHighestRatedMovie()) {
                    	System.out.println(s);
                    }
                    break;
                case 3:
                    System.out.println("评价人数最多的电影: " + movieService.getMostVotedMovie());
                    break;
                case 4:
                    System.out.println("出演电影数目最多的主演: " + movieService.getActorWithMostMovies());
                    break;
                case 5:
                    System.out.println("参与电影最多的编剧: " + movieService.getScreenwriterWithMostMovies());
                    break;
                case 6:
                    System.out.println("请输入查询条件（留空表示忽略该条件）:");
                    System.out.print("类型: ");
                    String type = scanner.nextLine();
                    System.out.print("国家: ");
                    String country = scanner.nextLine();
                    System.out.print("导演: ");
                    String director = scanner.nextLine();
                    System.out.print("语种: ");
                    String language = scanner.nextLine();
                    System.out.print("主演: ");
                    String mainActor = scanner.nextLine();
                    System.out.print("片长(区间): ");
                    String lengthStr = scanner.nextLine();
                    Integer minlength = null;
                    Integer maxlength = null;

                    // 处理片长区间输入
                    if (!lengthStr.trim().isEmpty()) {
                        String[] lengthStrs = lengthStr.split("\\s+");//将输入的字符串按空格分割，以处理多个空格的情况。
                        if (lengthStrs.length >= 1) {
                            minlength = lengthStrs[0].isEmpty() ? null : Integer.parseInt(lengthStrs[0]);
                        }
                        if (lengthStrs.length >= 2) {
                            maxlength = lengthStrs[1].isEmpty() ? null : Integer.parseInt(lengthStrs[1]);
                        }
                    }

                    
                    System.out.print("年份: ");
                    String year = scanner.nextLine();
                    List<Movie> moviesByCriteria = movieService.getMoviesByCriteria(country, director, language, mainActor,type,year,minlength,maxlength);
                    System.out.println("满足条件的电影: ");
                    if(!moviesByCriteria.isEmpty()) {
	                    for (Movie movie : moviesByCriteria) {
	                        System.out.println(movie.getTitle());
	                    }
                    }
                    else System.out.println("无");
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("无效选择，请重新输入。");
            }
        }

        scanner.close();
    }
}
