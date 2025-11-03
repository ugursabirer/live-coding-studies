package beginnerLevel.problem01ProductFilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {
    public static void main(String[] args) {
        String[] input = {"Ayakkabı", "Bilgisayar", "Ayakkabı", "Atkı", "Canta", "Armut"};
        List<String> result = filterAndSortProducts(input, "A");
        result.forEach(System.out::println);
    }

    public static List<String> filterAndSortProducts(String[] products, String prefix) {
        return Arrays.stream(products)
                .distinct()
                .sorted()
                .filter(s -> s.startsWith(prefix))
                .collect(Collectors.toList());
    }
}