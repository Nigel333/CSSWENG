import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main{
    ArrayList<Part> parts = new ArrayList<>();
    ArrayList<Part> filteredParts = parts;


    public static void main(String[] args) throws IOException {
        Main model = new Main();
        FileInputStream file = new FileInputStream("database.xlsx");
        String password="password";

        XSSFWorkbook workbook=(XSSFWorkbook)WorkbookFactory.create(file,password);
        Iterator<Sheet> sheets = workbook.sheetIterator();

        while(sheets.hasNext())
        {
            Sheet sheet = sheets.next();
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

        Collections.sort(model.parts, new Comparator<Part>() {
            @Override
            public int compare(Part o1, Part o2) {
                return o1.name.toLowerCase().compareTo(o2.name.toLowerCase());
            }
        });

        Frame view = new Frame(model);
        Controller controller = new Controller(view);
    }
}