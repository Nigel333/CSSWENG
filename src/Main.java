import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.table.AbstractTableModel;

public class Main{
    ArrayList<Part> parts = new ArrayList<>();
    ArrayList<String> carBrands;
    PartTableModel tableModel;
    boolean isManager = false;
    int currCart = 1;
    ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Main model = new Main();
        FileInputStream file = new FileInputStream("database.xlsx");
        String password="password";

        XSSFWorkbook workbook=(XSSFWorkbook)WorkbookFactory.create(file,password);
        Iterator<Sheet> sheets = workbook.sheetIterator();

        model.carBrands = new ArrayList<>();
        while(sheets.hasNext())
        {
            Sheet sheet = sheets.next();
            model.carBrands.add(sheet.getSheetName());
            Iterator<Row> iterator=sheet.iterator();

            while(iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator=  nextRow.cellIterator();

                String partBrand = cellIterator.next().getStringCellValue();
                String name = cellIterator.next().getStringCellValue();
                int year = (int) cellIterator.next().getNumericCellValue();
                int quantity = (int) cellIterator.next().getNumericCellValue();
                double price = cellIterator.next().getNumericCellValue();
                boolean isNew = "YES".equals(cellIterator.next().getStringCellValue());
                String authenticity = cellIterator.next().getStringCellValue();

                model.parts.add(new Part(sheet.getSheetName(), partBrand, name, year, quantity, price, isNew, authenticity));
            }
        }

        for(int i = 1; i <= 5; i++)
            model.shoppingCarts.add(new ShoppingCart(i));


        Collections.sort(model.parts, new Comparator<Part>() {
            @Override
            public int compare(Part o1, Part o2) {
                return o1.name.toLowerCase().compareTo(o2.name.toLowerCase());
            }
        });

        model.tableModel = new PartTableModel(model.parts);

        Frame view = new Frame(model);
        Controller controller = new Controller(view);
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

    public void changeParts(ArrayList<Part> parts)
    {
        this.parts = parts;
    }
}