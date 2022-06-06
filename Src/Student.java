import javax.swing.*;

/**
 * This class extends the class {@link Person}. It adds an extra field of data -
 * student ID. In addition, it overrides the {@link Person#match},
 * {@link Person#commit}, {@link Person#validateData} and
 * {@link Person#rollBack} methods to accompany the new data field.
 */
public class Student extends Person {
    // adding the student ID required fields (string, JTextField, JLabel) to the
    // class.
    private String studentIdstString;
    private JTextField studentIdTextField;
    private JLabel studentAsterisk;

    /**
     * This constructor receives 5 (all) parameters required to create a new
     * student. It invokes the parent class' constructor with ID, name, surname and
     * phone number. In addition, it updates the new field, student ID, accordingly,
     * by utilizing {@link ClubAbstractEntity#createAsterisk},
     * {@link ClubAbstractEntity#createRow} and
     * {@link ClubAbstractEntity#createTextField}.
     * 
     * @param id        The student's ID.
     * @param name      The student's name.
     * @param surname   The student's surname.
     * @param tel       The student's phone number.
     * @param studentID The student's student ID.
     */

    public Student(String id, String name, String surname, String tel, String studentID) {
        // call parent constructor
        super(id, name, surname, tel);

        // set student id
        this.studentIdstString = studentID;

        // initialize the new field(s) and add to the panel
        studentIdTextField = createTextField(studentID);
        studentAsterisk = createAsterisk();
        addToCenter(createRow("Student ID", studentIdTextField, studentAsterisk));

        // adjust window settings
        setSize(450, 250);
        setTitle("Student");
    }

    /**
     * Empty constructor. It invokes the 5-parameters constructor with empty strings
     * to create an empty student.
     */
    public Student() {
        this("", "", "", "", "");
    }

    /**
     * This method overrides {@link Person#match} to accompany the new field,
     * student ID.
     */
    @Override
    public boolean match(String key) {
        return super.match(key) || key.equals(studentIdstString.substring(4));
    }

    /**
     * This method overrides {@link Person#validateData} to accompany the new data
     * field, student ID.
     */
    @Override
    protected boolean validateData() {
        // set asterisk as blank space by default
        studentAsterisk.setText(" ");

        // validate parent variables
        if (!super.validateData()) {
            return false;
        }

        // if the student id matches the regex return whether the id already exists in
        // the DB or not
        if (studentIdTextField.getText().matches("[A-Z]{3}/[1-9]\\d{4}")) {
            return !(NightClubMgmtApp.isExist("Student ID", studentIdTextField.getText().substring(4), this));
        }

        // if student id does not match regex place an "*" and return false 
        studentAsterisk.setText("*");
        return false;
    }

    /**
     * This method overrides {@link Person#commit} to accompany the new data field,
     * student ID.
     */
    @Override
    protected void commit() {
        super.commit();
        studentIdstString = studentIdTextField.getText();

    }

    /**
     * This method overrides {@link Person#rollBack} to accompany the new data
     * field, student ID.
     */
    @Override
    protected void rollBack() {
        super.rollBack();
        studentIdTextField.setText(studentIdstString);
        studentAsterisk.setText(" ");
    }
}
