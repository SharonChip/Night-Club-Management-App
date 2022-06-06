import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.Serializable;

/**
 * Base abstract class that extends {@link JFrame} responsible to contain and
 * handle the GUI and base behavior of its inheritors.
 * 
 * Defines a number of abstract methods that allow generic handling for the Ok
 * and Cancel buttons.
 */
public abstract class ClubAbstractEntity extends JFrame {
    // buttons
    private JButton okButton, cancelButton;
    // panel
    private JPanel centerPanel;
    // hidden handler class
    private ButtonsHandler handler;

    /**
     * Parameterless constructor. Creates and initializes instance variables and all
     * GUI elements for this class.
     * 
     * Creates and initializes {@link ButtonsHandler} to handle okButton and
     * cancelButton.
     * 
     * Prevents resizing the window and disables the effect of the X button.
     */
    public ClubAbstractEntity() {
        // create the entity's frame
        // JFrame theFrame = new JFrame();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // create panels
        centerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel buttonsPanel = new JPanel();

        // create handler - NEEDED?
        handler = new ButtonsHandler();

        // create and set buttons
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        cancelButton.setEnabled(false);

        // add listeners to handler
        okButton.addActionListener(handler);
        cancelButton.addActionListener(handler);

        // add buttons to panel
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        // add panel to frame
        add(buttonsPanel, BorderLayout.SOUTH);
        add(centerPanel);
        setResizable(false);
    }

    /**
     * Adds a component to the center of the panel. Used as an interface that allows
     * inheritors to communicate with the centerPanel of this class.
     * 
     * @param guiComponent component to be added to centerPanel
     */
    protected void addToCenter(Component guiComponent) {
        centerPanel.add(guiComponent);
    }

    /**
     * Creates a row of elements that'll hold the information of a given field.
     * 
     * @param name      name of the information written in the row
     * @param textfield contains the correlating information
     * @param asterisk  error character, replaced with blankspace by default
     * @return a row of elements
     */
    protected JPanel createRow(String name, JTextField textfield, JLabel asterisk) {
        // panel that contains the row
        JPanel row = new JPanel();

        // elements of the row
        row.add(new JLabel(name));
        row.add(textfield);
        row.add(asterisk);

        return row;
    }

    /**
     * Creates a textfield with a defined size
     * 
     * @param str string within the textfield
     * @return new textfield with the given string
     */
    protected JTextField createTextField(String str) {
        return new JTextField(str, 30);
    }

    /**
     * This method creates a JLabel with the text " " and dyes it red. When needed
     * the text will be changed to "*". The blank space is used to "reserve" a place
     * for the "*"
     * 
     * 
     * @return the created JLabel.
     */
    protected JLabel createAsterisk() {
        JLabel asterisk = new JLabel(" ");
        asterisk.setForeground(Color.red);
        return asterisk;
    }

    /**
     * An abstract method to define a "matching" interface.
     * 
     * @param key The key to search for in the database.
     * @return true or false, depending if the key was found in the database.
     */
    public abstract boolean match(String key);

    /**
     * An abstract method to define a validation interface.
     * 
     * @return true if all the text fields are valid, false otherwise.
     */

    protected abstract boolean validateData();

    /**
     * An abstract method that saves the JTextfields' data of the inheritors into
     * their matching strings, respectively.
     */
    protected abstract void commit();

    /**
     * An abstract method that copies the "saved" data from the inheritors to their
     * matching JTextfields, respectively.
     */
    protected abstract void rollBack();

    /**
     * Tends to events generated from {@link javax.swing.JButton} okButton and
     * cancelButton of outer class.
     */
    class ButtonsHandler implements ActionListener, Serializable {

        /**
         * Realizes the buttons actions, polymorphically, according to the caller's
         * class using {@link #commit} and {@link #rollBack}.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // if ok button was pressed, validate data, and if data is valid commit
            if ((e.getSource() == okButton) && validateData()) {
                commit();
                cancelButton.setEnabled(true);
                setVisible(false);
            }

            // if cancel button was pressed, rollback
            if (e.getSource() == cancelButton) {
                rollBack();
                setVisible(false);
            }
        }
    }
}
