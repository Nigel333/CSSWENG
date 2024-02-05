import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Frame extends JFrame {
    JTable partTable;
    JPanel leftPanel, rightPanel, middlePanel;
    JPanel filterPanel;
    JTextField searchField;

    public Frame(Main model) {
        super("Item Database");

        PartTableModel tableModel = new PartTableModel(model.filteredParts);
        partTable = new JTable(tableModel);

        // Set the layout manager for the frame
        setLayout(new BorderLayout());

        middlePanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        middlePanel.add(searchField, BorderLayout.NORTH);
        middlePanel.add(new JScrollPane(partTable), BorderLayout.CENTER);
        add(middlePanel, BorderLayout.CENTER);
        initializeLeft();
        add(leftPanel, BorderLayout.WEST);
        initializeRight();
        add(rightPanel, BorderLayout.EAST);


        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    protected void initializeLeft()
    {
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Logo and Time"), BorderLayout.NORTH);
        filterPanel = new JPanel();
        leftPanel.add(filterPanel, BorderLayout.CENTER);
        leftPanel.add(new JLabel("Account Details"), BorderLayout.SOUTH);
    }
    protected void initializeRight()
    {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Shopping Cart Side"),BorderLayout.CENTER);
    }
}

class PartTableModel extends AbstractTableModel {
    private ArrayList<Part> parts;
    private String[] columnNames = {"Car Brand", "Car Model", "Name", "Year", "Quantity", "Price", "Is New", "Authenticity"};

    public PartTableModel(ArrayList<Part> parts) {
        this.parts = parts;
    }

    @Override
    public int getRowCount() {
        return parts.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Part part = parts.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return part.carBrand;
            case 1:
                return part.carModel;
            case 2:
                return part.name;
            case 3:
                return part.year;
            case 4:
                return part.quantity;
            case 5:
                return part.price;
            case 6:
                return part.isNew;
            case 7:
                return part.authenticity;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // Return the appropriate class for each column
        if (columnIndex == 6) {
            return Boolean.class; // "Is New" column is of boolean type
        } else {
            return super.getColumnClass(columnIndex);
        }
    }
}
