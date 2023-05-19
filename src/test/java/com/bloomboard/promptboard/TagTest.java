package com.bloomboard.promptboard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class TagTest {

    @Test
    public void tagParsing_test() {
        //String tags = "  Tag1, Tag2,Tag3  ,  Tag4,,Tag5, Tag 6,  ";
        String tags = "  Tag1, Tag2,Tag3  ,  Tag4,,Tag5, Tag 6, ,   ,  ";
        System.out.println(tags);
        //remove whitespaces around commas
        Pattern whitespaceComma = Pattern.compile("\\s*,\\s*(?=\\S|$)");
        Matcher matcher = whitespaceComma.matcher(tags);
        String cleanTags = matcher.replaceAll(",");

        System.out.println(String.format("Matcher 1, remove whitespace around commas:   [%s]",cleanTags));
        assertNotEquals(cleanTags, tags);

        //remove whitespaces at ends of string
        Pattern whitespaceEnds = Pattern.compile("^\\s+|\\s+$");
        Matcher matcher2 = whitespaceEnds.matcher(cleanTags);
        String cleanTags2 = matcher2.replaceAll("");

        System.out.println(String.format("Matcher 2, remove whitespace at ends:   [%s]",cleanTags2));
        assertNotEquals(cleanTags2, cleanTags);

        //replace potential double comma and add comma to end if none exists
        Pattern endCommas = Pattern.compile(",{2,}|,+$");
        Matcher matcher3 = endCommas.matcher(cleanTags2);
        String cleanTags3 = matcher3.find() ? matcher3.replaceAll(",") : cleanTags2 + ",";

        System.out.println(String.format("Matcher 3, remove double comma and add to end:    [%s]", cleanTags3));
        assertNotEquals(cleanTags3, cleanTags2);
    }
}
