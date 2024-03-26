import java.io.*;
import java.util.*;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.table.AbstractTableModel;

import java.io.File;
import java.io.IOException;
public class Main{
    ArrayList<Part> parts = new ArrayList<>();
    ArrayList<String> carBrands;
    PartTableModel tableModel;
    boolean isManager = false;
    int currCart = 0;
    ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();

    public void overwriteFile()
    {
        Workbook workbook = new XSSFWorkbook();
        for (String brand: carBrands)
        {
            Sheet sheet = workbook.createSheet();
            workbook.setSheetName(workbook.getSheetIndex(sheet), brand);
            ArrayList<Part> partsOfBrand = new ArrayList<Part>();
            for (Part part: parts)
                if (part.carBrand.equals(brand))
                    partsOfBrand.add(part);

            int rowNum = 0;
            for(Part part: partsOfBrand)
            {
                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(part.carModel);
                row.createCell(1).setCellValue(part.name);
                row.createCell(2).setCellValue(part.year);
                row.createCell(3).setCellValue(part.quantity);
                row.createCell(4).setCellValue(part.price);
                if (part.isNew)
                    row.createCell(5).setCellValue("YES");
                else
                    row.createCell(5).setCellValue("NO");
                row.createCell(6).setCellValue(part.authenticity);
                rowNum++;
            }
        }

        try
        {
            POIFSFileSystem fs = new POIFSFileSystem();
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            Encryptor enc = info.getEncryptor();
            enc.confirmPassword("password");
            OutputStream os = enc.getDataStream(fs);
            workbook.write(os);
            os.close();
            FileOutputStream fos = new FileOutputStream("database.xlsx");
            fs.writeFilesystem(fos);
            fos.close();
            workbook.close();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }

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
        model.overwriteFile();
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