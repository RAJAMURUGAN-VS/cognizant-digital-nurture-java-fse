package library;

public class LibraryTest {

    public static void main(String[] args) {

        Book[] books = {
            new Book(1, "Clean Code", "Robert Martin"),
            new Book(2, "The Pragmatic Programmer", "Andrew Hunt"),
            new Book(3, "Java Programming", "Herbert Schildt"),
            new Book(4, "Design Patterns", "Gang of Four"),
            new Book(5, "Head First Java", "Kathy Sierra")
        };

        System.out.println("-- Linear Search --");
        Book found = LibrarySearch.linearSearchByTitle(books, "Java Programming");
        System.out.println(found != null ? "Found: " + found : "Not found");

        found = LibrarySearch.linearSearchByTitle(books, "Data Structures");
        System.out.println(found != null ? "Found: " + found : "Not found: Data Structures");

        Book[] sortedBooks = {
            new Book(1, "Clean Code", "Robert Martin"),
            new Book(4, "Design Patterns", "Gang of Four"),
            new Book(5, "Head First Java", "Kathy Sierra"),
            new Book(3, "Java Programming", "Herbert Schildt"),
            new Book(2, "The Pragmatic Programmer", "Andrew Hunt")
        };

        System.out.println("\n-- Binary Search (sorted by title) --");
        found = LibrarySearch.binarySearchByTitle(sortedBooks, "Head First Java");
        System.out.println(found != null ? "Found: " + found : "Not found");

        found = LibrarySearch.binarySearchByTitle(sortedBooks, "Data Structures");
        System.out.println(found != null ? "Found: " + found : "Not found: Data Structures");
    }
}
