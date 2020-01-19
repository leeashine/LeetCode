package java8.chap5;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> pairs =
                numbers1.stream()
                        .flatMap(i -> numbers2.stream()
                                .filter(j -> (i + j) % 3 == 0)
                                .map(j -> new int[]{i, j})
                        )
                        .collect(toList());
        wordCount();

    }

    //使用jdk1.8流的特性统计文本中单词出现的次数
    public static void wordCount() throws IOException {
        // 使用try-resource 关闭资源
        try (Stream<String> lines = Files.lines(Paths.get("D:\\workspace\\aleetcode\\src\\main\\java\\java8\\chap5\\data.txt"), Charset.defaultCharset())) {

            Map<String, Long> counts = lines
                    // trim前后空格(使用方法引用)
                    .map(String::trim)
                    // 过滤掉空串
                    .filter(s -> !s.isEmpty())
                    // 把空格隔开的转为数组
                    .map(s -> s.split(" "))
                    // 数组转成流
                    .map(array -> Stream.of(array))
                    // 拉平
                    .flatMap(stream -> stream)
                    // 分组
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

            System.out.println("单词书限次数字典:" + counts);

            // 统计信息
            LongSummaryStatistics summaryStatistics = counts.entrySet().stream()
                    // 得到次数
                    .mapToLong(entry -> entry.getValue())
                    // 统计
                    .summaryStatistics();

            System.out.println("统计信息:" + summaryStatistics);
        }
    }
}
