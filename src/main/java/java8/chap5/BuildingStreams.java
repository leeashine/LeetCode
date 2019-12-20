package java8.chap5;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BuildingStreams {

    public static void main(String...args) throws Exception{
        
        // Stream.of
        Stream<String> stream = Stream.of("Java 8", "Lambdas", "In", "Action");
        stream.map(String::toUpperCase).forEach(System.out::println);

        // Stream.empty
        Stream<String> emptyStream = Stream.empty();

        // Arrays.stream
        int[] numbers = {2, 3, 5, 7, 11, 13};
        System.out.println(Arrays.stream(numbers).sum());

        // Stream.iterate
        Stream.iterate(0, n -> n + 2)
              .limit(10)
              .forEach(System.out::println);

        // fibonnaci with iterate
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1],t[0] + t[1]})
              .limit(10)
              .forEach(t -> System.out.println("(" + t[0] + ", " + t[1] + ")"));
        
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1],t[0] + t[1]})
              .limit(10)
              . map(t -> t[0])  //使用Map提取每个元组中的第一个元素
              .forEach(System.out::println);

        // random stream of doubles with Stream.generate
        Stream.generate(Math::random)
              .limit(10)
              .forEach(System.out::println);
 
        // stream of 1s with Stream.generate
        IntStream.generate(() -> 1)
                 .limit(5)
                 .forEach(System.out::println);

        IntStream.generate(new IntSupplier(){
            public int getAsInt(){
                return 2;
            }
        }).limit(5)
          .forEach(System.out::println);
   

        IntSupplier fib = new IntSupplier(){
                  private int previous = 0;
                  private int current = 1;
                  public int getAsInt(){
                      int nextValue = this.previous + this.current;
                      this.previous = this.current;
                      this.current = nextValue;
                      return this.previous;
                  }
              };
         IntStream.generate(fib).limit(10).forEach(System.out::println);

//         Files.lines，它会返回一个由指定文件中的各行构成的字符串流。
//         流会自动关闭
//你可以使用Files.lines得到一个流，其中的每个元素都是给定文件中的一行。然后，你
//可以对line调用split方法将行拆分成单词。应该注意的是，你该如何使用flatMap产生一个扁
//平的单词流，而不是给每一行生成一个单词流。最后，把distinct和count方法链接起来，数
//数流中有多少各不相同的单词。
//        flatmap方法让你把一个流中的每个值都换成另一个流，然后把所有的流连接起来成为一个流。
         long uniqueWords = 0;
         try(Stream<String> lines =Files.lines(Paths.get("D:\\workspace\\aleetcode\\src\\java8\\data.txt"),Charset.defaultCharset())) {
             uniqueWords=lines.flatMap(line->Arrays.stream(line.split(" ")))
                     .distinct()
                     .count();
         }catch (IOException e){
             e.printStackTrace();
         }

         System.out.println("There are " + uniqueWords + " unique words in data.txt");


    }
}
