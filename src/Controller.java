import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class Controller {
    Main model;
    Frame view;

    public Controller(Frame view)
    {
        this.view = view;
        this.model = this.view.model;

        this.view.searchField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {filter();}
            @Override
            public void removeUpdate(DocumentEvent e) {filter();}
            @Override
            public void changedUpdate(DocumentEvent e) {filter();}
        });

        this.view.sortBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {filter();}
        });

        this.view.order.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {filter();}
        });

        this.view.brandFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {filter();}
        });

        this.view.modelFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {filter();}
        });

        this.view.fromYear.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                if(!view.fromYear.getText().isEmpty()) {
                    try {
                        int num = Integer.parseInt(view.fromYear.getText());
                        if (num >= 9999) {
                            view.fromYear.setBackground(Color.decode("#FFCCCC"));
                        } else {
                            view.fromYear.setBackground(Color.WHITE);
                        }
                    } catch(Exception exception) {
                        view.fromYear.setBackground(Color.decode("#FFCCCC"));
                    }
                } else {
                    view.fromYear.setBackground(Color.WHITE);
                }
                filter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        this.view.toYear.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                if(!view.toYear.getText().isEmpty()) {
                    try {
                        int num = Integer.parseInt(view.toYear.getText());
                        if (num >= 9999) {
                            view.toYear.setBackground(Color.decode("#FFCCCC"));
                        } else {
                            view.toYear.setBackground(Color.WHITE);
                        }
                    } catch(Exception exception) {
                        view.toYear.setBackground(Color.decode("#FFCCCC"));
                    }
                } else {
                    view.toYear.setBackground(Color.WHITE);
                }
                filter();
            }
            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        this.view.fromPrice.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                if(!view.fromPrice.getText().isEmpty()) {
                    String input = view.fromPrice.getText();
                    try
                    {
                        Double.parseDouble(input);
                        view.fromPrice.setBackground(Color.WHITE);
                    } catch (Exception exception)
                    {
                        view.fromPrice.setBackground(Color.decode("#FFCCCC"));
                    }
                } else {
                    view.fromPrice.setBackground(Color.WHITE);
                }
                filter();
            }
            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        this.view.toPrice.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                if(!view.toPrice.getText().isEmpty()) {
                    String input = view.toPrice.getText();
                    try
                    {
                        Double.parseDouble(input);
                        view.toPrice.setBackground(Color.WHITE);
                    } catch (Exception exception)
                    {
                        view.toPrice.setBackground(Color.decode("#FFCCCC"));
                    }
                } else {
                    view.toPrice.setBackground(Color.WHITE);
                }
                filter();
            }
            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        this.view.newFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });

        this.view.authenticityFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });

        this.view.account.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.account.setEnabled(false);
                if (!model.isManager)
                {
                    String password = "";
                    Object[] option = {"Enter"};
                    JPasswordField passwordField = new JPasswordField();
                    if(JOptionPane.showOptionDialog(null, passwordField, "Please Enter the Password", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]) == 0)
                        password = passwordField.getText();
                    if(password.equals("password"))
                    {
                        view.account.setIcon(new ImageIcon("resources/manager.png"));
                        model.isManager = true;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Wrong Password Inputted. Please Try Again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    int choice = JOptionPane.showConfirmDialog(
                            null,
                            "Would you like to logout?",
                            "Logout",
                            JOptionPane.YES_NO_OPTION);
                    if(choice == JOptionPane.YES_OPTION)
                    {
                        view.account.setIcon(new ImageIcon("resources/regular.png"));
                        model.isManager = false;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Did Not Logout.", "", JOptionPane.PLAIN_MESSAGE);
                }
                view.account.setEnabled(true);
            }
        });


        for (CartButton cartButton :  view.cartButtons)
        {
            cartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    view.cartButtons.get(model.currCart - 1).setEnabled(true);
                    model.currCart = cartButton.num;
                    cartButton.setEnabled(false);

                    view.rightDisplay.remove(view.displayScreen);
                    view.displayScreen = view.cartPanel;
                    view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                    view.rightDisplay.repaint();
                    view.rightDisplay.revalidate();
                }
            });
        }

        view.cart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /* for future use
                view.rightDisplay.remove(view.displayScreen);
                view.displayScreen = view.cartPanel;
                view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                view.rightDisplay.repaint();
                view.rightDisplay.revalidate();
                */

                view.checkout.setEnabled(false);
                view.payment.setEnabled(false);
                view.receipt.setEnabled(false);
            }
        });
        view.checkout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view.cart.setEnabled(false);
                view.payment.setEnabled(false);
                view.receipt.setEnabled(false);
            }
        });
        view.payment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view.cart.setEnabled(false);
                view.checkout.setEnabled(false);
                view.receipt.setEnabled(false);
            }
        });
        view.receipt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view.cart.setEnabled(false);
                view.checkout.setEnabled(false);
                view.payment.setEnabled(false);
            }
        });
    }
    public void filter() {
        try {
            ArrayList<Part> filtered = new ArrayList<>();
            String text = this.view.searchField.getText().toLowerCase();

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

            filtered.sort(new Comparator<Part>() {
                @Override
                public int compare(Part o1, Part o2) {
                    int order;
                    if (Objects.requireNonNull(view.order.getSelectedItem()).toString().equals("Ascending"))
                        order = 1;
                    else
                        order = -1;
                    String sortBy = Objects.requireNonNull(view.sortBy.getSelectedItem()).toString();
                    if ("Car Brand".equals(sortBy)) {
                        return o1.carBrand.toLowerCase().compareTo(o2.carBrand.toLowerCase()) * order;
                    } else if ("Car Model".equals(sortBy)) {
                        return o1.carModel.toLowerCase().compareTo(o2.carModel.toLowerCase()) * order;
                    } else if ("Name".equals(sortBy)) {
                        return o1.name.toLowerCase().compareTo(o2.name.toLowerCase()) * order;
                    } else if ("Year".equals(sortBy)) {
                        return Integer.compare(o1.year, o2.year) * order;
                    } else if ("Quantity".equals(sortBy)) {
                        return Integer.compare(o1.quantity, o2.quantity) * order;
                    } else if ("Price".equals(sortBy)) {
                        return Double.compare(o1.price, o2.price) * order;
                    } else if ("isNew".equals(sortBy)) {
                        return Boolean.compare(o1.isNew, o2.isNew) * order;
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

            String brand = Objects.requireNonNull(this.view.brandFilter.getSelectedItem()).toString();
            if(!brand.equals("All"))
            {
                ArrayList<Part> filter = new ArrayList<>();
                for (Part part: filtered)
                {
                    if (part.carBrand.equals(brand))
                        filter.add(part);
                }
                filtered = filter;
            }

            String carModel = Objects.requireNonNull(this.view.modelFilter.getSelectedItem()).toString();
            if(!carModel.equals("All"))
            {
                ArrayList<Part> filterOut = new ArrayList<>();
                for (Part part: filtered)
                {
                    if (!part.carModel.equals(carModel))
                        filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }

            if ((!this.view.fromYear.getText().isEmpty()) && this.view.fromYear.getBackground().equals(Color.WHITE))
            {
                int year = Integer.parseInt(this.view.fromYear.getText());
                ArrayList<Part> filterOut = new ArrayList<>();
                for (Part part : filtered)
                {
                    if (part.year < year)
                        filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }
            if ((!this.view.toYear.getText().isEmpty()) && this.view.toYear.getBackground().equals(Color.WHITE))
            {
                int year = Integer.parseInt(this.view.toYear.getText());
                ArrayList<Part> filterOut = new ArrayList();
                for (Part part : filtered)
                {
                    if (part.year > year)
                        filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }

            if ((!this.view.fromPrice.getText().isEmpty()) && this.view.fromPrice.getBackground().equals(Color.WHITE))
            {
                    double price = Double.parseDouble(this.view.fromPrice.getText());
                    ArrayList<Part> filterOut = new ArrayList();
                    for (Part part : filtered)
                    {
                        if (part.price < price)
                            filterOut.add(part);
                    }
                    filtered.removeAll(filterOut);
            }

            if ((!this.view.toPrice.getText().isEmpty()) && this.view.toPrice.getBackground().equals(Color.WHITE))
            {
                double price = Double.parseDouble(this.view.toPrice.getText());
                ArrayList<Part> filterOut = new ArrayList();
                for (Part part : filtered)
                {
                    if (part.price > price)
                        filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }

            String condition = this.view.newFilter.getSelectedItem().toString();
            if(!condition.equals("All"))
            {
                ArrayList<Part> filterOut = new ArrayList<>();
                for (Part part: filtered)
                {
                    if(condition.equals("NEW"))
                    {
                        if(part.isNew != true)
                            filterOut.add(part);
                    }
                    else
                        if(part.isNew != false)
                            filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }

            String authenticity = this.view.authenticityFilter.getSelectedItem().toString();
            if(!authenticity.equals("All"))
            {
                ArrayList<Part> filterOut = new ArrayList<>();
                for (Part part: filtered)
                {
                    if(authenticity.equals("OTHERS"))
                    {
                        if(part.authenticity.equals("ORIGINAL") || part.authenticity.equals("CLASS A"))
                            filterOut.add(part);
                    }
                    else if (authenticity.equals("ORIGINAL"))
                    {
                        if (!part.authenticity.equals("ORIGINAL"))
                            filterOut.add(part);
                    }
                    else
                    {
                        if (!part.authenticity.equals("CLASS A"))
                            filterOut.add(part);
                    }
                }
                filtered.removeAll(filterOut);
            }

            model.tableModel.changeParts(filtered);
            if (model.tableModel.getRowCount() != 0)
                model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
            view.partTable.revalidate();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
