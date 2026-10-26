package bookinventoryfinal_ddl;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class EditBookController {

    @FXML private TextField txtID;
    @FXML private TextField txtTitle;
    @FXML private TextField txtAuthor;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtCategory;
    @FXML private TextField txtISBN;
    @FXML private TextField txtPublisher;
    @FXML private TextField txtYear;
    @FXML private TextField txtCoverPath;

    @FXML private javafx.scene.control.Button btnChooseCoverEdit;

    private Book book;
    private Stage dialogStage;
    private boolean saved = false;

    private static final String COVERS_DIR = "covers";

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setBook(Book book) {
        this.book = book;
        if (book != null) {
            txtID.setText(String.valueOf(book.getId()));
            txtTitle.setText(book.getTitle());
            txtAuthor.setText(book.getAuthor());
            txtQuantity.setText(String.valueOf(book.getQuantity()));
            txtCategory.setText(book.getCategory());
            txtISBN.setText(book.getIsbn());
            txtPublisher.setText(book.getPublisher());
            txtYear.setText(book.getYear());
            txtCoverPath.setText(book.getCoverPath());
        }
    }

    @FXML
    private void chooseCoverInDialog() {
        try {
            Window owner = (dialogStage != null) ? dialogStage : null;
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Cover Image");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png","*.jpg","*.jpeg","*.gif"));
            File selected = (owner == null) ? chooser.showOpenDialog(null) : chooser.showOpenDialog(owner);
            if (selected == null) return;

            File covers = new File(COVERS_DIR);
            if (!covers.exists()) covers.mkdirs();

            String safeName = System.currentTimeMillis() + "_" + selected.getName();
            File dest = new File(covers, safeName);
            Files.copy(selected.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

            String relativePath = COVERS_DIR + "/" + safeName;
            txtCoverPath.setText(relativePath);
        } catch (Exception ex) {
            System.err.println("Choose cover failed: " + ex.getMessage());
        }
    }

    @FXML
    private void onSave() {
        try {
            if (book == null) return;
            book.setTitle(txtTitle.getText().trim());
            book.setAuthor(txtAuthor.getText().trim());
            book.setCategory(txtCategory.getText().trim());
            book.setIsbn(txtISBN.getText().trim());
            book.setPublisher(txtPublisher.getText().trim());
            book.setYear(txtYear.getText().trim());
            book.setCoverPath(txtCoverPath.getText().trim());
            int qty = Integer.parseInt(txtQuantity.getText().trim());
            book.setQuantity(qty);
            saved = true;
            if (dialogStage != null) dialogStage.close();
        } catch (NumberFormatException ex) {
            txtQuantity.requestFocus();
        } catch (Exception ex) {
            System.err.println("Save in edit dialog failed: " + ex.getMessage());
            if (dialogStage != null) dialogStage.close();
        }
    }

    @FXML
    private void onCancel() {
        if (dialogStage != null) dialogStage.close();
    }

    public boolean isSaved() { return saved; }
}
