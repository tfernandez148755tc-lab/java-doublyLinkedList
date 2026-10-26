package bookinventoryfinal_ddl;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class MainController {

    /* ==============================
       Navigation
       ============================== */

    @FXML private VBox leftNav;

    @FXML private Button btnNavDashboard;
    @FXML private Button btnNavAdd;
    @FXML private Button btnNavSearch;
    @FXML private Button btnNavDelete;
    @FXML private Button btnNavViewAll;
    @FXML private Button btnNavAbout;

    /* ==============================
       Pages
       ============================== */

    @FXML private StackPane centerStack;

    @FXML private VBox dashboardPane;
    @FXML private ScrollPane addPane;
    @FXML private VBox searchPane;
    @FXML private VBox deletePane;
    @FXML private VBox viewAllPane;
    @FXML private VBox aboutPane;

    /* ==============================
       Dashboard
       ============================== */

    @FXML private ImageView imgCover;

    @FXML private Label displayTitle;
    @FXML private Label displayAuthor;
    @FXML private Label displayGenre;
    @FXML private Label displayISBN;
    @FXML private Label displayQuantity;
    @FXML private Label displayPublisher;
    @FXML private Label displayYear;

    @FXML private HBox thumbContainer;

    @FXML private Button btnNext;
    @FXML private Button btnPrev;

    @FXML private Label lblPosition;

    @FXML private Label lblStatus;
    @FXML private Label lblTotalBooks;

    /* ==============================
       Add Book
       ============================== */

    @FXML private TextField txtID;
    @FXML private TextField txtTitle;
    @FXML private TextField txtAuthor;
    @FXML private TextField txtQuantity;

    @FXML private ComboBox<String> comboCategory;

    @FXML private TextField txtISBN;
    @FXML private TextField txtPublisher;
    @FXML private TextField txtYear;

    @FXML private TextField txtCoverPath;

    @FXML private Button btnChooseCover;

    @FXML private ImageView imgAddCover;

    @FXML private Label addStatus;

    /* ==============================
       Search
       ============================== */

    @FXML private TextField txtSearchMain;
    @FXML private Label searchStatus;

    /* ==============================
       Delete
       ============================== */

    @FXML private TextField txtDeleteMain;
    @FXML private Label deleteStatus;

    /* ==============================
       Table
       ============================== */

    @FXML private TableView<Book> tableAllBooks;

    @FXML private TableColumn<Book,Integer> colAllID;
    @FXML private TableColumn<Book,String> colAllTitle;
    @FXML private TableColumn<Book,String> colAllAuthor;
    @FXML private TableColumn<Book,Integer> colAllQty;
    @FXML private TableColumn<Book,String> colAllCategory;

    /* ==============================
       Date and Time
       ============================== */

    @FXML private Label lblDate;
    @FXML private Label lblTime;

    /* ==============================
       Data
       ============================== */

    private DoublyLinkedList list = new DoublyLinkedList();

    private ObservableList<Book> tableData =
            FXCollections.observableArrayList();

    private int currentIndex = -1;

    private static final String CSV_FILE = "books.csv";

    private static final String COVER_FOLDER = "covers";

    /* ==============================
       Initialize
       ============================== */

    @FXML
    public void initialize() {

        new File(COVER_FOLDER).mkdirs();

        comboCategory.setItems(
                FXCollections.observableArrayList(
                        "Programming",
                        "Computer Science",
                        "Database",
                        "Networking",
                        "Fiction",
                        "Classic",
                        "Self Help",
                        "General"
                )
        );

        colAllID.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        colAllTitle.setCellValueFactory(
                new PropertyValueFactory<>("title"));

        colAllAuthor.setCellValueFactory(
                new PropertyValueFactory<>("author"));

        colAllQty.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        colAllCategory.setCellValueFactory(
                new PropertyValueFactory<>("category"));

        tableAllBooks.setItems(tableData);

        loadFromCsv();

        updateTotalBooks();

        renderThumbnails();

        if(list.size()>0){

            currentIndex=0;

            displayDashboardPreview();

        }

        Timeline clock=new Timeline(

                new KeyFrame(Duration.ZERO,e->{

                    LocalDateTime now=LocalDateTime.now();

                    lblDate.setText(
                            now.format(
                                    DateTimeFormatter.ofPattern(
                                            "MMMM dd, yyyy"
                                    )
                            )
                    );

                    lblTime.setText(
                            now.format(
                                    DateTimeFormatter.ofPattern(
                                            "hh:mm:ss a"
                                    )
                            )
                    );

                }),

                new KeyFrame(Duration.seconds(1))

        );

        clock.setCycleCount(Animation.INDEFINITE);

        clock.play();

        Platform.runLater(()->{

            Scene scene=centerStack.getScene();

            if(scene!=null){

                attachResponsiveListeners(scene);

                attachMaximizeListener(scene);

            }

        });

    }/* ==========================================
   NAVIGATION
   ========================================== */

@FXML
private void onNavDashboard() {
    setActiveNav(btnNavDashboard);
    showPane("dashboard");
}

@FXML
private void onNavAdd() {
    setActiveNav(btnNavAdd);
    showPane("add");
}

@FXML
private void onNavSearch() {
    setActiveNav(btnNavSearch);
    showPane("search");
}

@FXML
private void onNavDelete() {
    setActiveNav(btnNavDelete);
    showPane("delete");
}

@FXML
private void onNavViewAll() {
    setActiveNav(btnNavViewAll);
    showPane("view");
    refreshAllTable();
}

@FXML
private void onNavAbout() {
    setActiveNav(btnNavAbout);
    showPane("about");
}

/* ==========================================
   ACTIVE MENU
   ========================================== */

private void setActiveNav(Button active){

    btnNavDashboard.getStyleClass().remove("menu-active");
    btnNavAdd.getStyleClass().remove("menu-active");
    btnNavSearch.getStyleClass().remove("menu-active");
    btnNavDelete.getStyleClass().remove("menu-active");
    btnNavViewAll.getStyleClass().remove("menu-active");
    btnNavAbout.getStyleClass().remove("menu-active");

    active.getStyleClass().add("menu-active");
}

/* ==========================================
   SHOW PANES
   ========================================== */

private void showPane(String pane){

    dashboardPane.setVisible(false);
    dashboardPane.setManaged(false);

    addPane.setVisible(false);
    addPane.setManaged(false);

    searchPane.setVisible(false);
    searchPane.setManaged(false);

    deletePane.setVisible(false);
    deletePane.setManaged(false);

    viewAllPane.setVisible(false);
    viewAllPane.setManaged(false);

    aboutPane.setVisible(false);
    aboutPane.setManaged(false);

    switch(pane){

        case "dashboard":

            dashboardPane.setVisible(true);
            dashboardPane.setManaged(true);
            displayDashboardPreview();
            break;

        case "add":

            addPane.setVisible(true);
            addPane.setManaged(true);
            break;

        case "search":

            searchPane.setVisible(true);
            searchPane.setManaged(true);
            break;

        case "delete":

            deletePane.setVisible(true);
            deletePane.setManaged(true);
            break;

        case "view":

            viewAllPane.setVisible(true);
            viewAllPane.setManaged(true);
            break;

        case "about":

            aboutPane.setVisible(true);
            aboutPane.setManaged(true);
            break;
    }
}

/* ==========================================
   CHOOSE COVER
   ========================================== */

@FXML
private void chooseCover(){

    FileChooser chooser = new FileChooser();

    chooser.setTitle("Choose Book Cover");

    chooser.getExtensionFilters().add(

            new FileChooser.ExtensionFilter(

                    "Image Files",

                    "*.png",

                    "*.jpg",

                    "*.jpeg",

                    "*.gif"

            )

    );

    File file = chooser.showOpenDialog(
            btnChooseCover.getScene().getWindow());

    if(file == null)
        return;

    try{

        File folder = new File(COVER_FOLDER);

        if(!folder.exists())
            folder.mkdirs();

        String filename =
                System.currentTimeMillis()
                        + "_"
                        + file.getName();

        File destination =
                new File(folder, filename);

        Files.copy(

                file.toPath(),

                destination.toPath(),

                StandardCopyOption.REPLACE_EXISTING

        );

        txtCoverPath.setText(
                destination.getAbsolutePath());

        Image image =
                new Image(destination.toURI().toString());

        if(imgAddCover != null){
            imgAddCover.setImage(image);
        }

        setAddStatus("Cover selected.");

    }

    catch(Exception e){

        e.printStackTrace();

        setAddStatus("Unable to load cover.");

    }

}

/* ==========================================
   STATUS
   ========================================== */

private void setAddStatus(String text){

    addStatus.setText(text);

}/* ==========================================
   ADD BOOK
   ========================================== */

@FXML
private void addBook() {

    try {

        if (txtID.getText().isBlank()
                || txtTitle.getText().isBlank()
                || txtAuthor.getText().isBlank()
                || txtQuantity.getText().isBlank()) {

            setAddStatus("Please complete all required fields.");
            return;
        }

        int id = Integer.parseInt(txtID.getText());

        if (list.search(id) != null) {
            setAddStatus("Book ID already exists.");
            return;
        }

        int quantity = Integer.parseInt(txtQuantity.getText());

        Book book = new Book(

                id,

                txtTitle.getText(),

                txtAuthor.getText(),

                quantity,

                comboCategory.getValue(),

                txtISBN.getText(),

                txtPublisher.getText(),

                txtYear.getText(),

                txtCoverPath.getText()

        );

        list.add(book);

        tableData.add(book);

        saveToCsv();

        currentIndex = list.size() - 1;

        refreshAllTable();

        renderThumbnails();

        updateTotalBooks();

        displayDashboardPreview();

        clearAddFields();

        setAddStatus("Book added successfully.");

    }

    catch (NumberFormatException e) {

        setAddStatus("ID and Quantity must be numbers.");

    }

    catch (Exception e) {

        e.printStackTrace();

        setAddStatus("Unable to add book.");

    }

}

/* ==========================================
   CLEAR
   ========================================== */

@FXML
private void clearAddFields() {

    txtID.clear();
    txtTitle.clear();
    txtAuthor.clear();
    txtQuantity.clear();

    comboCategory.getSelectionModel().clearSelection();

    txtISBN.clear();
    txtPublisher.clear();
    txtYear.clear();

    txtCoverPath.clear();

    if (imgAddCover != null)
        imgAddCover.setImage(null);

}

/* ==========================================
   SEARCH BOOK
   ========================================== */

@FXML
private void searchBookFromMain() {

    String keyword = txtSearchMain.getText().trim();

    if (keyword.isBlank()) {

        searchStatus.setText("Enter Book ID or Title.");

        return;

    }

    try {

        int id = Integer.parseInt(keyword);

        Node node = list.search(id);

        if (node != null) {

            currentIndex = list.indexOf(id);

            displayDashboardPreview();

            searchStatus.setText("Book found.");

            showPane("dashboard");

            return;

        }

    } catch (Exception ignored) {

    }

    Node current = list.getHead();

    int index = 0;

    while (current != null) {

        if (current.book.getTitle().equalsIgnoreCase(keyword)) {

            currentIndex = index;

            displayDashboardPreview();

            searchStatus.setText("Book found.");

            showPane("dashboard");

            return;

        }

        current = current.next;

        index++;

    }

    searchStatus.setText("Book not found.");

}

/* ==========================================
   DELETE BOOK
   ========================================== */

@FXML
private void deleteBookFromMain() {

    try {

        int id = Integer.parseInt(txtDeleteMain.getText());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setHeaderText("Delete Book");

        alert.setContentText("Delete this book?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK)
            return;

        if (list.delete(id)) {

            tableData.removeIf(book -> book.getId() == id);

            saveToCsv();

            refreshAllTable();

            renderThumbnails();

            updateTotalBooks();

            if (list.size() == 0) {

                currentIndex = -1;

            } else {

                currentIndex = 0;

            }

            displayDashboardPreview();

            deleteStatus.setText("Book deleted.");

        } else {

            deleteStatus.setText("Book not found.");

        }

    }

    catch (Exception e) {

        deleteStatus.setText("Invalid Book ID.");

    }

}/* ==========================================
   NEXT BOOK
   ========================================== */

@FXML
private void onNext() {

    if (list.size() == 0)
        return;

    currentIndex++;

    if (currentIndex >= list.size()) {
        currentIndex = 0;
    }

    displayDashboardPreview();

}

/* ==========================================
   PREVIOUS BOOK
   ========================================== */

@FXML
private void onPrev() {

    if (list.size() == 0)
        return;

    currentIndex--;

    if (currentIndex < 0) {
        currentIndex = list.size() - 1;
    }

    displayDashboardPreview();

}

/* ==========================================
   DISPLAY CURRENT BOOK
   ========================================== */

private void displayDashboardPreview() {

    if (list.size() == 0) {

        displayTitle.setText("No Books");

        displayAuthor.setText("");

        displayGenre.setText("");

        displayISBN.setText("");

        displayQuantity.setText("");

        displayPublisher.setText("");

        displayYear.setText("");

        imgCover.setImage(null);

        lblPosition.setText("0 of 0");

        return;

    }

    Node node = list.getNodeAt(currentIndex);

    if (node == null)
        return;

    Book book = node.book;

    displayTitle.setText(book.getTitle());

    displayAuthor.setText("Author : " + book.getAuthor());

    displayGenre.setText("Category : " + book.getCategory());

    displayISBN.setText("ISBN : " + book.getIsbn());

    displayQuantity.setText("Quantity : " + book.getQuantity());

    displayPublisher.setText("Publisher : " + book.getPublisher());

    displayYear.setText("Year : " + book.getYear());

    imgCover.setImage(loadCoverImage(book.getCoverPath()));

    lblPosition.setText((currentIndex + 1) + " of " + list.size());

}private void renderThumbnails() {

    thumbContainer.getChildren().clear();

    Node current = list.getHead();

    int index = 0;

    while (current != null) {

        Book book = current.book;

        VBox card = new VBox();

        card.setAlignment(javafx.geometry.Pos.CENTER);

        card.setSpacing(8);

        card.getStyleClass().add("preview-card");

        ImageView image = new ImageView();

        image.setFitWidth(100);

        image.setFitHeight(140);

        image.setPreserveRatio(true);

        image.getStyleClass().add("book-cover");

        image.setImage(loadCoverImage(book.getCoverPath()));

        Label title = new Label(book.getTitle());

        title.setWrapText(true);

        title.setMaxWidth(100);

        title.setStyle("-fx-font-weight:bold;");

        Label author = new Label(book.getAuthor());

        card.getChildren().addAll(image, title, author);

        final int selectedIndex = index;

        card.setOnMouseClicked(e -> {

            currentIndex = selectedIndex;

            displayDashboardPreview();

        });

        thumbContainer.getChildren().add(card);

        current = current.next;

        index++;

    }

}private Image loadCoverImage(String path) {

    try {

        if (path == null || path.isBlank()) {

            return new Image(
                    getClass()
                    .getResource("/images/no_cover.png")
                    .toExternalForm());

        }

        File file = new File(path);

        if (!file.isAbsolute()) {

            file = new File(
                    System.getProperty("user.dir"),
                    path);

        }

        if (file.exists()) {

            return new Image(file.toURI().toString());

        }

    } catch (Exception ignored) {
    }

    return new Image(
            getClass()
            .getResource("/images/no_cover.png")
            .toExternalForm());

}/* ==========================================
   REFRESH TABLE
   ========================================== */

private void refreshAllTable() {

    tableData.clear();

    Node current = list.getHead();

    while (current != null) {

        tableData.add(current.book);

        current = current.next;
    }

}

/* ==========================================
   LOAD CSV
   ========================================== */

private void loadFromCsv() {

    try {

        List<Book> books = CSVUtil.readBooksFromCsv(CSV_FILE);

        list = new DoublyLinkedList();

        tableData.clear();

        for (Book book : books) {

            list.add(book);

            tableData.add(book);

        }

    } catch (Exception e) {

        System.out.println("No CSV file found.");

    }

}

/* ==========================================
   SAVE CSV
   ========================================== */

public void saveToCsv() {

    try {

        CSVUtil.writeBooksToCsv(CSV_FILE, tableData);

    } catch (Exception e) {

        e.printStackTrace();

    }

}

/* ==========================================
   UPDATE TOTAL BOOKS
   ========================================== */

private void updateTotalBooks() {

    lblTotalBooks.setText("TOTAL BOOKS : " + list.size());

}

/* ==========================================
   RESPONSIVE LAYOUT
   ========================================== */

private void attachResponsiveListeners(Scene scene) {

    scene.widthProperty().addListener((obs, oldValue, newValue) -> {

        if (newValue.doubleValue() < 900) {

            imgCover.setFitWidth(170);
            imgCover.setFitHeight(240);

        } else {

            imgCover.setFitWidth(220);
            imgCover.setFitHeight(320);

        }

    });

}

/* ==========================================
   MAXIMIZE
   ========================================== */

private void attachMaximizeListener(Scene scene) {

    if (!(scene.getWindow() instanceof Stage))
        return;

    Stage stage = (Stage) scene.getWindow();

    stage.maximizedProperty().addListener((obs, oldValue, maximized) -> {

        if (maximized) {

            imgCover.setFitWidth(280);
            imgCover.setFitHeight(400);

        } else {

            imgCover.setFitWidth(220);
            imgCover.setFitHeight(320);

        }

    });

}

/* ==========================================
   SAFE TEXT
   ========================================== */

private String safeGetText(TextField field) {

    if (field == null)
        return "";

    return field.getText() == null ? "" : field.getText().trim();

}

/* ==========================================
   STATUS
   ========================================== */

private void setStatus(String message) {

    lblStatus.setText(message);

}}