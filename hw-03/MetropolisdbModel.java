import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MetropolisdbModel is a table model that interacts with a MySQL database
 * to store and retrieve information about metropolises.
 *
 * It extends AbstractTableModel and provides data to a JTable.
 */
public class MetropolisdbModel extends AbstractTableModel {
    public static final String MYSQL_USERNAME = "root";
    public static final String MYSQL_PASSWORD = "Apple123!";
    public static final String MYSQL_DATABASE_SERVER = "jdbc:mysql://localhost:3306/cs103_hw_db";
    public static final String MYSQL_DATABASE_NAME = "cs103_hw_db";
    private static final String POPU_SMALL = "Population Smaller Then";
    private static final String POPU_LARGE  = "Population Larger Then";
    private static final String [] cols = { "Metropolis", "Continent", "Population" };
    private List<String[]> data = new ArrayList<>();
    private Connection connection;
    /**
     * Constructor: Establishes the connection to the database.
     * If connection fails, it prints an error message.
     */
    public MetropolisdbModel(){
        try {
            connection = DriverManager.getConnection(MYSQL_DATABASE_SERVER, MYSQL_USERNAME, MYSQL_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
    /**
     * Returns the number of rows in the current dataset.
     */
    @Override
    public int getRowCount() {
        return data.size();
    }

    /**
     * Returns the number of columns (fixed by the cols array).
     */
    @Override
    public int getColumnCount() {
        return cols.length;
    }

    /**
     * Returns the name of the column at the specified index.
     */
    @Override
    public String getColumnName(int index) {
        return cols[index];
    }

    /**
     * Returns the value at a specified row and column in the data list.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }
    /**
     * Adds a new metropolis record to the database if all input fields are non-empty.
     * After inserting, it refreshes the table data with a default query.
     *
     * @param metro Name of the metropolis
     * @param cont Continent of the metropolis
     * @param pop Population as a string
     */
    public void addMetropolis(String metro, String cont, String pop) {
        String query = "insert into metropolises (metropolis, continent, population) values (?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(query)){

            boolean b = !cont.isEmpty() && !pop.isEmpty()&& !metro.isEmpty();
            if(b){
                stmt.setString(1, metro);
                stmt.setString(2, cont);
                stmt.setString(3, pop);
                stmt.executeUpdate();
            }



        } catch (SQLException e){
            e.printStackTrace();
        }
        searchMetropolises("", "", "", POPU_LARGE, "Partial Match");
    }

/**
 * Searches the database for matching metropolises based on user input and filters.
 * Builds the SQL query dynamically depending on which fields are provided.
 * Updates the internal data list and notifies the JTable of changes.
 *
 */
 public void searchMetropolises(String metro, String cont, String pop, String popFilter, String matchType) {
        StringBuilder query = new StringBuilder("SELECT * FROM metropolises  ");
        boolean whereAdded = false;

        List<String> params = new ArrayList<>();

        if (!metro.isEmpty()) {
            query.append(whereAdded ? " AND " : " WHERE ");
            query.append("metropolis ").append(matchType.equals("Exact Match") ? "= ?" : "LIKE ?");
            params.add(matchType.equals("Exact Match") ? metro : "%" + metro + "%");
            whereAdded = true;
        }
        if (!cont.isEmpty()) {
            query.append(whereAdded ? " AND " : " WHERE ");
            query.append("continent ").append(matchType.equals("Exact Match") ? "= ?" : "LIKE ?");
            params.add(matchType.equals("Exact Match") ? cont : "%" + cont + "%");
            whereAdded = true;
        }
        if (!pop.isEmpty()) {
            query.append(whereAdded ? " AND " : " WHERE ");
            query.append("population ").append(popFilter.equals(POPU_SMALL) ? "< ?" : "> ?");
            params.add(pop);
            whereAdded = true;
        }
        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            data.clear(); // clear existing rows
            while (rs.next()) {
                String m = rs.getString("metropolis");
                String c = rs.getString("continent");
                String p = String.valueOf(rs.getInt("population"));
                data.add(new String[]{m, c, p});
            }
            fireTableDataChanged();
        } catch (SQLException e) {
            System.err.println("Search failed: " + e.getMessage());
        }

    }
}
