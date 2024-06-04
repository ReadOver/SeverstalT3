
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Interface extends JFrame {

    private JFrame frame = new JFrame("Приложение для учёта поставок");
    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();

    private  DataBase dataBase=new DataBase();

    private int idPeriod; //id таблицы PricePeriod

    public Interface() { ///!!!!заменить потом на Interface
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //Делаем frame по центру экрана
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
        int windowY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(windowX, windowY);


        frame.add(panel1);

        // Окно 1
        activity1();

        // Окно 2
       // activity2();

//        // Создание вкладок
//        JTabbedPane mainTabbedPane = new JTabbedPane();
//        mainTabbedPane.addTab("Поставка", panel1);
//        mainTabbedPane.addTab("Отчёт", panel2);
//
//        // Добавление элементов на основное окно
//        frame.add(mainTabbedPane);
//
//        // Обработчик для переключения между вкладками
//        mainTabbedPane.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                if (mainTabbedPane.getSelectedIndex() == 1) {
//                    // Код для переключения на второе окно
//                }
//            }
//        });

        frame.setVisible(true);
    }

    private void activity1(){

        panel1.setLayout(null); // Установка layout на null для возможности управления местоположением компонентов


        JButton button1 = new JButton("Цены на товары");
        button1.setBounds(10, 10, 200, 25);
        panel1.add(button1);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supplierPrices();
            }
        });

        /**
         *  Реализации части добавления поставки
         */

        String[] suppliers = {"Supplier 1", "Supplier 2", "Supplier 3"};
        JComboBox<String> comboBox = new JComboBox<>(suppliers);
        comboBox.setBounds(10,50,100,25);
        panel1.add(comboBox);

        JDateChooser dateChooser = new JDateChooser();
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        editor.setEditable(false);
        dateChooser.setBounds(140, 50, 110, 30);
        panel1.add(dateChooser);

        Label labelProduct1 = new Label("Груша Спелая зелеёная");
        labelProduct1.setBounds(50, 100, 150, 15);
        panel1.add(labelProduct1);

        Label labelProduct2 = new Label("Груша 2");
        labelProduct2.setBounds(250, 100, 150, 15);
        panel1.add(labelProduct2);

        Label labelProduct3 = new Label("Яблоко азазза");
        labelProduct3.setBounds(450, 100, 150, 15);
        panel1.add(labelProduct3);

        Label labelProduct4 = new Label("Яблоко ббб");
        labelProduct4.setBounds(650, 100, 150, 15);
        panel1.add(labelProduct4);

        PriceField textField1 = new PriceField();
        textField1.setBounds(50, 120, 80, 20);
        textField1.setEditable(false);
        panel1.add(textField1);


        PriceField textField2 = new PriceField();
        textField2.setBounds(250, 120, 80, 20);
        textField2.setEditable(false);
        panel1.add(textField2);

        PriceField textField3 = new PriceField();
        textField3.setBounds(450, 120, 80, 20);
        textField3.setEditable(false);
        panel1.add(textField3);

        PriceField textField4 = new PriceField();
        textField4.setBounds(650, 120, 80, 20);
        textField4.setEditable(false);
        panel1.add(textField4);


        Button button = new Button("Добавить поставку");
        button.setVisible(false);
        button.setBounds(350, 160, 110, 30);
        panel1.add(button);

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.setEditable(false);
                textField1.setText("");
                labelProduct1.setText("временно");
                textField2.setEditable(false);
                textField2.setText("");
                labelProduct2.setText("временно");
                textField3.setEditable(false);
                textField3.setText("");
                labelProduct3.setText("временно");
                textField4.setEditable(false);
                textField4.setText("");
                labelProduct4.setText("временно");
                button.setVisible(false);
            }
        });

        //находим есть ли на эту эту дату продукты
        dateChooser.addPropertyChangeListener( new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                textField1.setEditable(false);
                textField1.setText("");
                labelProduct1.setText("временно");
                textField2.setEditable(false);
                textField2.setText("");
                labelProduct2.setText("временно");
                textField3.setEditable(false);
                textField3.setText("");
                labelProduct3.setText("временно");
                textField4.setEditable(false);
                textField4.setText("");
                labelProduct4.setText("временно");
                button.setVisible(false);



                Date date = dateChooser.getDate();
                if (date!=null){
                    Instant instant1=date.toInstant();
                    LocalDate firstDate = instant1.atZone(ZoneId.systemDefault()).toLocalDate();

                    try {
                        int supplierID=comboBox.getSelectedIndex() + 1;
                        idPeriod = dataBase.getIdByDate(firstDate,supplierID);
                        System.out.printf(String.valueOf(idPeriod));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    if (idPeriod!=0){ //дата входит в период цены
                        button.setVisible(true);
                        List<DBHelper> products= dataBase.getProductIDPrice(idPeriod);
                        for (DBHelper product : products) {
                              if (product.getProductId()==1){
                                  textField1.setEditable(true);
                                  labelProduct1.setText(String.valueOf(product.getPrice()));
                              }
                              else if (product.getProductId()==2){
                                  textField2.setEditable(true);
                                  labelProduct2.setText(String.valueOf(product.getPrice()));
                              }
                              else if (product.getProductId()==3){
                                  textField3.setEditable(true);
                                  labelProduct3.setText(String.valueOf(product.getPrice()));
                              }
                              else if (product.getProductId()==4){
                                  textField4.setEditable(true);
                                  labelProduct4.setText(String.valueOf(product.getPrice()));
                              }
                            }
                        }
                    }
                }
        });


        //отправляем поставку в БД
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Instant instant = dateChooser.getDate().toInstant();
                LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

                //для отправки поставки все доступные товары должны иметь вес
                if ((textField1.isEditable()&&textField1.getText().isEmpty()) ||
                    (textField2.isEditable()&&textField2.getText().isEmpty()) ||
                    (textField3.isEditable()&&textField3.getText().isEmpty()) ||
                    (textField4.isEditable()&&textField4.getText().isEmpty())) {

                    JOptionPane.showMessageDialog(frame,
                            "Укажите веса для доступных товаров",
                            "Ошибка",
                            JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        int supplierID=comboBox.getSelectedIndex() + 1;
                        if(dataBase.checkDateTransaction(date,supplierID)){ //Проверяем есть ли поставка на эту дату
                            JOptionPane.showMessageDialog(frame,
                                    "На данную дату уже имеется поставка",
                                    "Ошибка",
                                    JOptionPane.WARNING_MESSAGE);


                        } else {

                            //Пусть товар не добавляется в поставку если на него не указали цену (т.е. недоступен)
                           if (textField1.isEditable()){
                               dataBase.insertTransaction(date,idPeriod,1,Double.parseDouble(textField1.getText()));
                           }
                            if (textField2.isEditable()){
                                dataBase.insertTransaction(date,idPeriod,2,Double.parseDouble(textField2.getText()));
                            }
                            if (textField3.isEditable()){
                                dataBase.insertTransaction(date,idPeriod,3,Double.parseDouble(textField3.getText()));
                            }
                            if (textField4.isEditable()){
                                dataBase.insertTransaction(date,idPeriod,4,Double.parseDouble(textField4.getText()));
                            }
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }


            }
        });

        /**
         *  Реализации части выбора периода для отчёта
         */

        JCheckBox periodCheckBox = new JCheckBox("За всё время");
        periodCheckBox.setBounds(300,235,120,20);
        periodCheckBox.setSelected(true);
        panel1.add(periodCheckBox);

        JDateChooser dateChooserStart = new JDateChooser();
        dateChooserStart.setToolTipText("до");
        JTextFieldDateEditor editor1 = (JTextFieldDateEditor) dateChooserStart.getDateEditor();
        editor1.setEditable(false);
        dateChooserStart.setBounds(450, 235, 110, 20);
        panel1.add(dateChooserStart);

        JDateChooser dateChooserEnd = new JDateChooser();
        JTextFieldDateEditor editor2 = (JTextFieldDateEditor) dateChooserEnd.getDateEditor();
        editor2.setEditable(false);
        dateChooserEnd.setBounds(600, 235, 110, 20);
        panel1.add(dateChooserEnd);

        /**
         * Исключаем ситацию, когда date1>date2
         */
        dateChooserEnd.addPropertyChangeListener( new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Date firstDate = dateChooserStart.getDate();
                Date secondDate = dateChooserEnd.getDate();

                if (secondDate!=null) {
                    if ( dateChooserEnd.getCalendar().getTime().before(firstDate)) {
                        // Запрещаем выбор даты
                        dateChooserEnd.setDate(null);
                    }
                }
            }
        });

        Label labelDate1 = new Label("с:");
        labelDate1.setBounds(435, 235, 110, 20);
        panel1.add(labelDate1);

        Label labelDate2 = new Label("по:");
        labelDate2.setBounds(580, 235, 110, 20);
        panel1.add(labelDate2);

        /**
         *  Реализации части таблицы для вывода отчёта
         */

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);




        String[] columns = { "Дата", "Продукт ","Вес (кг)","Цена за 1 кг"};
        DefaultTableModel tableModel1 = new DefaultTableModel(columns, 0);
        JTable table1 = new JTable(tableModel1);
        table1.setRowHeight(0, 40); // Устанавливаем высоту первой строки


        JScrollPane scrollPane1 = new JScrollPane(table1);


        scrollPane1.setBounds(0,235,980,300); //достаточно указать размеры для 1-й таблицы
        tabbedPane.addTab("Поставщик 1", scrollPane1);

        DefaultTableModel tableModel2 = new DefaultTableModel(columns, 0);
        JTable table2 = new JTable(tableModel2);
        JScrollPane scrollPane2 = new JScrollPane(table2);
        tabbedPane.addTab("Поставщик 2", scrollPane2);

        DefaultTableModel tableModel3 = new DefaultTableModel(columns, 0);
        JTable table3 = new JTable(tableModel3);
        JScrollPane scrollPane3 = new JScrollPane(table3);
        tabbedPane.addTab("Поставщик 3", scrollPane3);

        tabbedPane.setBounds(0, 235, 980, 300);

        // Обработчик для переключения между вкладками таблицы
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                List<DBHelper> products;



                if (periodCheckBox.isSelected()){ //если выбран весь период
                    products= dataBase.getDateNameWeightPrice(tabbedPane.getSelectedIndex()+1);
                } else{
                    Instant instant1 = dateChooserStart.getDate().toInstant();
                    LocalDate dateStart = instant1.atZone(ZoneId.systemDefault()).toLocalDate();

                    Instant instant2 = dateChooserEnd.getDate().toInstant();
                    LocalDate dateEnd = instant2.atZone(ZoneId.systemDefault()).toLocalDate();

                    products=dataBase.getSortDateNameWeightPrice(tabbedPane.getSelectedIndex()+1,
                            dateStart,dateEnd);
                };

                if (tabbedPane.getSelectedIndex() == 0) {
                    tableModel1.setRowCount(0); // clear table rows

                    for (DBHelper product : products) {
                        tableModel1.addRow(new Object[]{
                                product.getPeriod(),
                                product.getName(),
                                product.getWeight(),
                                product.getPrice()
                        });

                    }
                }
                if (tabbedPane.getSelectedIndex() == 1) {
                    tableModel2.setRowCount(0); // clear table rows

                    for (DBHelper product : products) {
                        tableModel2.addRow(new Object[]{
                                product.getPeriod(),
                                product.getName(),
                                product.getWeight(),
                                product.getPrice()
                        });

                    }
                }
                if (tabbedPane.getSelectedIndex() == 2) {
                    tableModel3.setRowCount(0); // clear table rows

                    for (DBHelper product : products) {
                        tableModel3.addRow(new Object[]{
                                product.getPeriod(),
                                product.getName(),
                                product.getWeight(),
                                product.getPrice()
                        });

                    }
                }
            }
        });
        panel1.add(tabbedPane);



    }

    private void supplierPrices(){
        JDialog dialog = new JDialog(frame, "Цены поставщиков", true);
        JPanel panel3 = new JPanel();
        panel3.setBackground(Color.lightGray);
        panel3.setLayout(null);
        dialog.add(panel3);
        dialog.setSize(800, 500);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowX = (int) ((screenSize.getWidth() - dialog.getWidth()) / 2);
        int windowY = (int) ((screenSize.getHeight() - dialog.getHeight()) / 2);
        dialog.setLocation(windowX, windowY);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);



        String[] suppliers = {"Supplier 1", "Supplier 2", "Supplier 3"};
        JComboBox<String> comboBox = new JComboBox<>(suppliers);
        int supplierID=comboBox.getSelectedIndex() + 1;
        comboBox.setBounds(50,15,100,25);
        panel3.add(comboBox);


        String[] columns = {"Период поставок","Наименование товара", "Цена (Руб)"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane1 = new JScrollPane(table);
        scrollPane1.setBounds(50,50,700,200); //достаточно указать размеры для 1-й таблицы

        List<DBHelper> products= dataBase.getPeriodNamePrice(supplierID);
        for (DBHelper product : products) {
            tableModel.addRow(new Object[]{
                    product.getPeriod(),
                    product.getName(),
                    product.getPrice()
            });
        }
        panel3.add(scrollPane1);

        /**
         *  обновление данных в таблице исходя из выбора поставщика
         */
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    tableModel.setRowCount(0); // clear table rows
                    int supplierID=comboBox.getSelectedIndex() + 1;

                     List<DBHelper> products= dataBase.getPeriodNamePrice(supplierID);
                        for (DBHelper product : products) {
                            tableModel.addRow(new Object[]{
                                    product.getPeriod(),
                                    product.getName(),
                                    product.getPrice()
                            });

                        }
                }
            }
        });

        Label textDateStart = new Label("Начало поставки:");
        textDateStart.setBounds(50, 265, 150, 15);
        panel3.add(textDateStart);

        JDateChooser dateChooser1 = new JDateChooser();
        JTextFieldDateEditor editor1 = (JTextFieldDateEditor) dateChooser1.getDateEditor();
        editor1.setEditable(false);
        dateChooser1.setBounds(50, 295, 110, 30);
        panel3.add(dateChooser1);


        Label textDateEnd = new Label("Конец поставки:");
        textDateEnd.setBounds(250, 265, 150, 15);
        panel3.add(textDateEnd);

        JDateChooser dateChooser2 = new JDateChooser();
        JTextFieldDateEditor editor2 = (JTextFieldDateEditor) dateChooser2.getDateEditor();
        editor2.setEditable(false);
        dateChooser2.setBounds(250, 295, 110, 30);
        dateChooser2.setEnabled(false);
        panel3.add(dateChooser2);

        /**
         * Исключаем ситацию, когда date1>date2
         */
        dateChooser2.addPropertyChangeListener( new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        Date firstDate = dateChooser1.getDate();
                        Date secondDate = dateChooser2.getDate();

                        if (secondDate!=null) {
                            if ( dateChooser2.getCalendar().getTime().before(firstDate)) {
                                // Запрещаем выбор даты
                                dateChooser2.setDate(null);
                            }
                        }
                    }
        });

        /**
         * Исключаем ситуацию, когда поставку начинают указывать с "даты конца" (т.к. date2 >= date1, date1!=null )
         */
        dateChooser1.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                Date firstDate = dateChooser1.getDate();
                dateChooser2.setEnabled(firstDate != null);

            }
        });

        Label labelProduct1 = new Label("Груша Спелая зелеёная");
        labelProduct1.setBounds(50, 350, 150, 15);
        panel3.add(labelProduct1);

        Label labelProduct2 = new Label("Груша 2");
        labelProduct2.setBounds(250, 350, 150, 15);
        panel3.add(labelProduct2);

        Label labelProduct3 = new Label("Яблоко азазза");
        labelProduct3.setBounds(450, 350, 150, 15);
        panel3.add(labelProduct3);

        Label labelProduct4 = new Label("Яблоко ббб");
        labelProduct4.setBounds(650, 350, 150, 15);
        panel3.add(labelProduct4);

        PriceField textField1 = new PriceField();
        textField1.setBounds(50, 380, 80, 20);
        panel3.add(textField1);


        TextField textField2 = new TextField();
        textField2.setBounds(250, 380, 80, 20);
        panel3.add(textField2);

        TextField textField3 = new TextField();
        textField3.setBounds(450, 380, 80, 20);
        panel3.add(textField3);

        TextField textField4 = new TextField();
        textField4.setBounds(650, 380, 80, 20);
        panel3.add(textField4);


        Button button = new Button("Добавить");
        button.setBounds(350, 420, 110, 30);
        panel3.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dateChooser1.getDate()==null ||dateChooser2.getDate()==null){
                    JOptionPane.showMessageDialog(frame,
                            "Выберите период поставок",
                            "Ошибка",
                            JOptionPane.WARNING_MESSAGE);


                } else if(textField1.getText().isEmpty() && textField2.getText().isEmpty() &&
                        textField3.getText().isEmpty() && textField4.getText().isEmpty() ){
                    JOptionPane.showMessageDialog(frame,
                            "Необходимо указать цену минимум для одного товара",
                            "Ошибка",
                            JOptionPane.WARNING_MESSAGE);

                }else{

                    /**
                     * Заносим данные в БД
                     */
                    int supplierID=comboBox.getSelectedIndex() + 1;

                    Instant instant1 = dateChooser1.getDate().toInstant();
                    LocalDate firstDate = instant1.atZone(ZoneId.systemDefault()).toLocalDate();
                    Instant instant2 = dateChooser2.getDate().toInstant();
                    LocalDate secondDate = instant2.atZone(ZoneId.systemDefault()).toLocalDate();


                    dataBase.insertPricePeriod(supplierID,firstDate,secondDate);
                    int pricePeriodId=dataBase.getPricePeriodID();

                    //Пусть товар не доавляется в список если на него не указали цену
                    if (!textField1.getText().isEmpty()) {
                        double price1=Double.parseDouble(textField1.getText());
                        dataBase.insertPriceProduct(pricePeriodId,1, price1);
                    };
                    if (!textField2.getText().isEmpty()) {
                        double price2=Double.parseDouble(textField2.getText());
                        dataBase.insertPriceProduct(pricePeriodId,2, price2);
                    };
                    if (!textField3.getText().isEmpty()) {
                        double price3=Double.parseDouble(textField3.getText());
                        dataBase.insertPriceProduct(pricePeriodId,3, price3);
                    };
                    if (!textField4.getText().isEmpty()) {
                        double price4=Double.parseDouble(textField4.getText());
                        dataBase.insertPriceProduct(pricePeriodId,4, price4);
                    };

                    //labelProduct1.setText(date.toString());
                }


            }
        });



        dialog.setVisible(true);
    }

//    private void activity2(){
//        JTextField inputField = new JTextField(20);
//        panel2.add(inputField);
//
//        JButton button = new JButton("Кнопка");
//        panel2.add(button);
//    }






}


