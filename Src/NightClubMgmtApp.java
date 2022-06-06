
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * This class extends {@link JFrame}. It acts as the main class for the program.
 * Managing the GUI and writing to and from the database using
 * {@link #writeClubbersDBtoFile()} and {@link #loadClubbersDBFromFile()}.
 * 
 * It has a field of {@link ArrayList} containing all saved
 * {@link ClubAbstractEntity}, polymorphically.
 */
public class NightClubMgmtApp extends JFrame {
    private static ArrayList<ClubAbstractEntity> clubbers;

    /**
     * This empty constructor serves as the main function of the program, it handles
     * all events and initializations of itself
     */
    public NightClubMgmtApp() {
        // initialize clubbers using DB (if it exists)
        clubbers = new ArrayList<>();
        loadClubbersDBFromFile();

        // create panel and buttons for the app
        JButton searchButton = new JButton("Search");
        JButton addButton = new JButton("Add");
        String[] types = { "Person", "Student", "Soldier" };
        JComboBox<String> choice = new JComboBox<>(types);

        // add listeners and anonymous inner listener
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClubAbstractEntity ent = createEntity(choice.getSelectedIndex());
                if (ent != null) {
                    clubbers.add(ent);
                    ent.setVisible(true);
                }
            }
        });
        // search
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manipulateDB();
            }
        });
        // window closing
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                writeClubbersDBtoFile();
                System.exit(0);
            }
        });

        // create buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(choice);
        buttonsPanel.add(addButton);
        buttonsPanel.add(searchButton);

        // app's window 'settings'
        add(buttonsPanel);
        setSize(450, 200);
        setTitle("BK Club Management");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /**
     * check if the entity already exists using {@link ClubAbstractEntity#match} of
     * each inheritor individually {@link Person#match}, {@link Student#match},
     * {@link Soldier#match}.
     * 
     * @param name      The name of the entity.
     * @param key       The key to match by.
     * @param newEntity The entity we want to check.
     * 
     * @return true if entity exist, false otherwise.
     */
    public static boolean isExist(String name, String key, ClubAbstractEntity newEntity) {
        for (ClubAbstractEntity clubber : clubbers) {

            // check if newEntity's key matches another entity key within the array.
            if (clubber != newEntity && clubber.match(key)) {
                String str = name + " " + key + " is in the database";
                JOptionPane.showMessageDialog(newEntity, str, "Clubber Already In Data Base",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }

    /**
     * Create a new entity.
     * 
     * @param idx The index of the combobox, selected by the user. 0 - Person, 1 -
     *            Student, 2 - Soldier
     * @return a new instance of the chosen entity
     */
    private ClubAbstractEntity createEntity(int idx) {
        switch (idx) {
            case 0:
                return new Person();
            case 1:
                return new Student();
            case 2:
                return new Soldier();
            default:
                return null;

        }
    }

    /**
     * Searches an entity using {@link ClubAbstractEntity#match(String)}.
     * 
     * @param key a string to search for
     * @return the entity itself, if it's found
     */
    private ClubAbstractEntity find(String key) {
        for (ClubAbstractEntity clubber : clubbers) {
            if (clubber.match(key))
                return clubber;
        }
        return null;
    }

    /**
     * Function to show the requested clubber's info. It asks the user for a
     * clubber's key and searches it in the database by utilizing the {@link #find}
     * method. If the requested clubber was found, the function displays their info.
     * If the requested clubber was not found, the function displays a message to
     * notify the user.
     */
    private void manipulateDB() {
        String input;

        // lets input dialog open again in case the user entered a nonexistent key
        while (true) {
            // input key to dialog
            input = JOptionPane.showInputDialog(this, "Enter Clubber's Key ");

            // cancel was pressed
            if (input == null) {
                break;
            }

            // look for a clubber with given input
            ClubAbstractEntity clubber = find(input);
            if (clubber != null) {
                clubber.setVisible(true);
                break;
            }
            // clubber not found
            else {
                JOptionPane.showMessageDialog(this, "Clubber with key " + input + " does not exist",
                        "Clubber Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Function to read clubbers information from a file named "BKCustomers.dat", to
     * an ArrayList of {@link ClubAbstractEntity}, by using the
     * {@link ObjectInputStream} and {@link FileInputStream} classes. In addition,
     * 
     * 
     * the function removes any empty entities which were created by closing the
     * main window, after pressing the "Add" button and before saving anything.
     */
    @SuppressWarnings("unchecked")
    private void loadClubbersDBFromFile() {

        try {
            // create a new object input stream
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("BKCustomers.dat"));

            // read info from DB file to the array
            clubbers = (ArrayList<ClubAbstractEntity>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Database file not found.\nA new file will be created.",
                    "Database file not found", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // removal of empty entities
        ClubAbstractEntity toRemove;
        while (true) {
            toRemove = find("");
            if (toRemove != null)
                clubbers.remove(toRemove);
            else
                break;
        }
    }

    /**
     * Function to write the clubbers information ,from the ArrayList, to a file
     * named "BKCustomers.dat", by utilizing {@link java.io.ObjectOutputStream} and
     * {@link java.io.FileOutputStream} classes.
     */
    private void writeClubbersDBtoFile() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(("BKCustomers.dat")));
            oos.writeObject(clubbers);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main function. Creates an instance of NightClubMgmtApp to initialize and run
     * the program.
     * 
     * @param args .
     */
    public static void main(String[] args) {
        NightClubMgmtApp application = new NightClubMgmtApp();
    }
}
