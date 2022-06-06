import javax.swing.*;

/**
 * This class extends the abstract class {@link ClubAbstractEntity}. It holds
 * all the required identifiers for each customer as a {@link JFrame}.
 */
public class Person extends ClubAbstractEntity {
    // the required identifiers:
    // identifiersStrings[0] - id, identifiersStrings[1] - name
    // identifiersStrings[2] - surname, identifiersStrings[3] - phone number.
    protected String identifierStrings[];

    // each identifier has a matching textfield
    // ordered the same as the identifier strings
    protected JTextField identifiersTextFields[];

    // each identifier has a JLabel to hold the asterisk for an error.
    protected JLabel[] asterisks;

    /**
     * This constructor receives 4 (all) parameters required to create a new person.
     * In addition, it adds the fields to their respected locations in the frame by
     * using the {@link ClubAbstractEntity#addToCenter} method. By using the
     * following methods: {@link ClubAbstractEntity#createAsterisk},
     * {@link ClubAbstractEntity#createRow},
     * {@link ClubAbstractEntity#createTextField} we get a generic implementation
     * for a person.
     * 
     * @param id      The person's ID.
     * @param name    The person's name.
     * @param surname The person's surname.
     * @param tel     The person's phone number.
     */
    public Person(String id, String name, String surname, String tel) {
        // window settings
        this.setTitle("Person");
        this.setSize(450, 220);
        setLocationRelativeTo(null);

        // fields initialization
        identifierStrings = new String[] { id, name, surname, tel };
        identifiersTextFields = new JTextField[identifierStrings.length];
        String[] labels = { "ID", "Name", "Surname", "Tel" };
        asterisks = new JLabel[identifierStrings.length];

        for (int i = 0; i < identifierStrings.length; i++) {

            identifiersTextFields[i] = createTextField(identifierStrings[i]);
            asterisks[i] = createAsterisk();

            addToCenter(createRow(labels[i], identifiersTextFields[i], asterisks[i]));
        }
    }

    /**
     * Empty constructor. Invokes the 4-parameter constructor with empty strings.
     * Used to create a new and empty entity.
     */
    public Person() {
        this("", "", "", "");
    }

    /**
     * Checks if given key matches the person ID.
     * 
     * @param key ID String to be matched
     * @return True if key matches idString, otherwise false.
     */
    @Override
    public boolean match(String key) {
        return key.equals(identifierStrings[0]);
    }

    /**
     * This method validates the ID, name, surname and phone number by utilizing
     * regular expressions.
     * 
     * @return true/false, according to validation.
     */
    @Override
    protected boolean validateData() {
        // all regex to be checked
        String[] patterns = { "^\\d-\\d{7}\\|[1-9]$", "[A-Z][a-z]+", "([A-Z][a-z]*('|-)?)+",
                "^\\+\\([1-9]\\d{0,2}\\)[1-9]\\d{0,2}-[1-9]\\d{6}$" };

        Boolean found = true;

        // check if strings match the regex
        for (int i = 0; i < identifiersTextFields.length; i++) {
            // hide the asterisk by default
            asterisks[i].setText(" ");
            // if there's no match, show asterisk and return
            if (found && !(identifiersTextFields[i].getText().matches(patterns[i]))) {
                found = false;
                asterisks[i].setText("*");
            }
        }

        // check if ID exists within the DB
        String insertedID = identifiersTextFields[0].getText();
        return found && !(NightClubMgmtApp.isExist("ID", insertedID, this));
    }

    /**
     * After a successful validation, write the information into the database.
     */
    @Override
    protected void commit() {
        for (int i = 0; i < identifierStrings.length; i++) {
            identifierStrings[i] = identifiersTextFields[i].getText();
        }
    }

    /**
     * Rolls back the previous valid values into the textfields.
     */
    @Override
    protected void rollBack() {
        for (int i = 0; i < identifiersTextFields.length; i++) {
            identifiersTextFields[i].setText(identifierStrings[i]);
            asterisks[i].setText(" ");
        }
    }
}
