package bookinventoryfinal_ddl;

import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

    public static void writeBooksToCsv(String fileName, ObservableList<Book> books) throws IOException {

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            writer.println("ID,Title,Author,Quantity,Category,ISBN,Publisher,Year,CoverPath");

            for (Book book : books) {

                writer.printf("%d,%s,%s,%d,%s,%s,%s,%s,%s%n",

                        book.getId(),

                        escape(book.getTitle()),

                        escape(book.getAuthor()),

                        book.getQuantity(),

                        escape(book.getCategory()),

                        escape(book.getIsbn()),

                        escape(book.getPublisher()),

                        escape(book.getYear()),

                        escape(book.getCoverPath()));

            }

        }

    }

    public static List<Book> readBooksFromCsv(String fileName) throws IOException {

        List<Book> books = new ArrayList<>();

        File file = new File(fileName);

        if (!file.exists()) {
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",", -1);

                if (data.length < 9)
                    continue;

                Book book = new Book(

                        Integer.parseInt(data[0]),

                        data[1],

                        data[2],

                        Integer.parseInt(data[3]),

                        data[4],

                        data[5],

                        data[6],

                        data[7],

                        data[8]

                );

                books.add(book);

            }

        }

        return books;

    }

    private static String escape(String text) {

        if (text == null)
            return "";

        return text.replace(",", " ");

    }

}