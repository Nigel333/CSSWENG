import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Controller {
    Main model;
    Frame view;

    public Controller(Frame view)
    {
        this.view = view;
        this.model = this.view.model;

        this.view.searchField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }


        });

        this.view.sortBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });

        this.view.order.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });

    }
    public void filter() {
        try {
            ArrayList<Part> filtered = new ArrayList<>();
            String text = this.view.searchField.getText();

            for(Part part : model.parts)
            {
                boolean has = true;
                for (String input: text.split(" "))
                {
                    if (!part.toString().toLowerCase().contains(input))
                    {
                        has = false;
                        break;
                    }
                }
                if (has)
                {
                    filtered.add(part);
                }
            }

            Collections.sort(filtered, new Comparator<Part>() {
                @Override
                public int compare(Part o1, Part o2) {
                    int order;
                    if(view.order.getSelectedItem().toString().equals("Asc"))
                        order = 1;
                    else
                        order = -1;
                    String sortBy = view.sortBy.getSelectedItem().toString();
                    if ("Car Brand".equals(sortBy)) {
                        return o1.carBrand.toLowerCase().compareTo(o2.carBrand.toLowerCase()) * order;
                    } else if ("Car Model".equals(sortBy)) {
                        return o1.carModel.toLowerCase().compareTo(o2.carModel.toLowerCase()) * order;
                    } else if ("Name".equals(sortBy)){
                        return o1.name.toLowerCase().compareTo(o2.name.toLowerCase()) * order;
                    } else if ("Year".equals(sortBy)){
                        return Integer.valueOf(o1.year).compareTo(o2.year) * order;
                    } else if ("Quantity".equals(sortBy)){
                        return Integer.valueOf(o1.quantity).compareTo(o2.quantity) * order;
                    } else if ("Price".equals(sortBy)){
                        return Double.valueOf(o1.price).compareTo(o2.price) * order;
                    } else if ("isNew".equals(sortBy)){
                        return Boolean.valueOf(o1.isNew).compareTo(o2.isNew) * order;
                    } else {
                        String a = o1.authenticity;
                        String b = o2.authenticity;
                        int result;

                        if (a.equals("ORIGINAL")) {
                            if (b.equals("ORIGINAL")) {
                                result = 0;
                            } else if (b.equals("CLASS A")) {
                                result = 1;
                            } else {
                                result = 1;
                            }
                        } else if (a.equals("CLASS A")) {
                            if (b.equals("ORIGINAL")) {
                                result = -1;
                            } else if (b.equals("CLASS A")) {
                                result = 0;
                            } else {
                                result = 1;
                            }
                        } else {
                            if (b.equals("ORIGINAL") || b.equals("CLASS A")) {
                                result = -1;
                            } else {
                                result = a.toLowerCase().compareTo(b.toLowerCase());
                            }
                        }
                        return result * order;
                    }
                }
            });

            model.tableModel.changeParts(filtered);
            if (model.tableModel.getRowCount() != 0)
                model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
            view.partTable.revalidate();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
