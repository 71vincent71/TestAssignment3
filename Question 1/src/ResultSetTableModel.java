import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class ResultSetTableModel extends AbstractTableModel
{
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numberOfRows;

    // initialize resultSet and obtain its meta data object;
    // determine number of rows
    public ResultSetTableModel(String driver, String url, String user, String password, String query)
            throws SQLException, ClassNotFoundException {
        // load database driver class
        Class.forName(driver);

        // connect to database
        connection = DriverManager.getConnection(url, user, password);

        // create Statement to query database
        statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        // set query and execute it
        setQuery(query);
    }

    // get class that represents column type
    public Class getColumnClass(int column)
    {
        // determine Java class of column
        try
        {
            String className = metaData.getColumnClassName(column + 1);
            return Class.forName(className);
        }

        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return Object.class;
    }

    // get number of columns in ResultSet
    public int getColumnCount()
    {
        // determine number of columns
        try
        {
            return metaData.getColumnCount();
        }
        // catch SQLExceptions and print error message
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

        return 0;
    }

    // get name of a particular column in ResultSet
    public String getColumnName(int column)
    {
        try
        {
            return metaData.getColumnName(column + 1);
        }
        // catch SQLExceptions and print error message
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return "";
    }

    public int getRowCount() {
        return numberOfRows;
    }


    public Object getValueAt(int row, int column)
    {
        try
        {
            resultSet.absolute(row + 1);

            return resultSet.getObject(column + 1);
        }

        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return "";
    }


    protected void finalize() {

        try {
            statement.close();
            connection.close();
        }


        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    public void setQuery(String query) throws SQLException {

        resultSet = statement.executeQuery(query);

        metaData = resultSet.getMetaData();


        resultSet.last();
        numberOfRows = resultSet.getRow();


        fireTableStructureChanged();
    }
}

