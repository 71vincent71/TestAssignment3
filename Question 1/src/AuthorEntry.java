import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthorEntry extends JFrame
{
    private Connection connection;
    private PreparedStatement statement;

    public AuthorEntry(String driver, String url, String user, String password)
    {
        super("Add a New Author");
        super.setLocation(960, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        try {
            Class.forName(driver);

            connection = DriverManager.getConnection(url, user, password);

            JLabel firstNameLabel = new JLabel("First Name: ");
            JLabel lastNameLabel = new JLabel("Last Name: ");

            JTextField firstNameField = new JTextField(20);
            JTextField lastNameField = new JTextField(20);

            JButton submitButton = new JButton("Submit");

            Box firstNameEntry = Box.createHorizontalBox();
            firstNameEntry.add(firstNameLabel);
            firstNameEntry.add(firstNameField);

            Box lastNameEntry = Box.createHorizontalBox();
            lastNameEntry.add(lastNameLabel);
            lastNameEntry.add(lastNameField);

            Container frameContent = getContentPane();
            frameContent.add(firstNameEntry, BorderLayout.NORTH);
            frameContent.add(lastNameEntry, BorderLayout.CENTER);
            frameContent.add(submitButton);

            setSize(360, 200);
            setVisible(true);

            submitButton.addActionListener(e ->
                    {
                        try
                        {
                            String first = firstNameField.getText();
                            String last = lastNameField.getText();

                            statement =
                                    connection.prepareStatement(
                                            "INSERT INTO authors values (NULL,'" + first + "','" + last + "');"
                                    );
                            statement.executeUpdate();
                        }
                        catch (SQLException sqlException)
                        {
                            JOptionPane.showMessageDialog(null, sqlException.toString(),
                                    "Database error", JOptionPane.ERROR_MESSAGE);
                        }
                        finally
                        {
                            dispose();
                        }

                    }
            );
        }
        catch (ClassNotFoundException classNotFound)
        {
            JOptionPane.showMessageDialog(null,
                    "Cloudscape driver not found", "Driver not found",
                    JOptionPane.ERROR_MESSAGE);
        }

        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(null,
                    sqlException.toString(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
