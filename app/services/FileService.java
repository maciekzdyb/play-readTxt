package services;

import play.libs.Files;
import play.mvc.Http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileService {

    public Map<String,Long> getFrequencyList(Files.TemporaryFile temporaryFile)  {

        File file = new File("test.txt");
        temporaryFile.copyTo(file.toPath(), true);

        String content = readFile(file);
        return createSortedMap(content);
    }

    private String readFile(File file){
        StringBuilder data = new StringBuilder();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                data.append(scanner.nextLine());
            }
            scanner.close();;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data.toString();
    }

    private Map<String,Long> createSortedMap(String content){
        String[] wordsTable = content.split("\\P{L}+");
        Map<String, Long> counted = Arrays.stream(wordsTable)
                .filter(word -> word.length()>0)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return counted.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(w1,w2) -> w1, LinkedHashMap::new));
    }
}
