
import javax.swing.*;

/**
 * This class extends the class {@link Person}. It adds an extra field of data -
 * personal number. In addition, it overrides the {@link Person#match},
 * {@link Person#commit}, {@link Person#validateData} and
 * {@link Person#rollBack} methods to accompany the new data field.
 */
public class Soldier extends Person {
    // adding the personal number required fields (string, JTextField, JLabel)
    // to the class.
    private String personalNumString;
    private JTextField personalNumTextField;
    private JLabel soldierAsterisk;

    /**
     * This constructor receives 5 (all) parameters required to create a new
     * soldier. It invokes the parent class' constructor with ID, name, surname and
     * phone number. In addition, it updates the new field, personal number,
     * accordingly, by utilizing {@link ClubAbstractEntity#createAsterisk},
     * {@link ClubAbstractEntity#createRow} and
     * {@link ClubAbstractEntity#createTextField}.
     * 
     * @param id          The soldier's ID.
     * @param name        The soldier's name.
     * @param surname     The soldier's surname.
     * @param tel         The soldier's phone number.
     * @param personalNum The soldier's personal number.
     */
    public Soldier(String id, String name, String surname, String tel, String personalNum) {
        // call parent constructor
        super(id, name, surname, tel);

        // set student id
        personalNumString = personalNum;

        // initialize the new field(s) and add to the panel
        personalNumTextField = createTextField(personalNum);
        soldierAsterisk = createAsterisk();
        addToCenter(createRow("Personal No.", personalNumTextField, soldierAsterisk));

        // adjust window settings
        setSize(450, 250);
        setTitle("Soldier");
    }

    /**
     * Empty constructor. It invokes the 5-parameters constructor with empty strings
     * to create an empty soldier.
     */
    public Soldier() {
        this("", "", "", "", "");
    }

    /**
     * This method overrides {@link Person#match} to accompany the new data field,
     * personal number.
     */
    @Override
    public boolean match(String key) {
        return super.match(key) || personalNumString.equals(key);
    }

    /**
     * This method overrides {@link Person#validateData} to accompany the new data
     * field, personal number.
     */
    @Override
    protected boolean validateData() {
        // set asterisk as blank space by default
        soldierAsterisk.setText(" ");

        // validate parent variables
        if (!super.validateData()) {
            return false;
        }

        // if the personal number matches the regex return whether the id already exists
        // in
        // the DB or not
        if (personalNumTextField.getText().matches("[ROC]/[1-9]\\d{6}")) {
            return !(NightClubMgmtApp.isExist("Personal Number", personalNumString, this));
        }

        // if personal number does not match regex place an "*" and return false
        soldierAsterisk.setText("*");
        return false;
    }

    /**
     * This method overrides {@link Person#commit} to accompany the new data field,
     * personal number.
     */
    @Override
    protected void commit() {
        super.commit();
        personalNumString = personalNumTextField.getText();

    }

    /**
     * This method overrides {@link Person#rollBack} to accompany the new data
     * field, personal number.
     */
    @Override
    protected void rollBack() {
        super.rollBack();
        personalNumTextField.setText(personalNumString);
        soldierAsterisk.setText(" ");
    }
}