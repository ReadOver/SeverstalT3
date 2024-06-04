import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

<<<<<<< HEAD
    // Заголовки столбцов
//    public static Object[] columnsHeader = new String[] {"ID сотрудника","Имя", "Фамилия",
//            "Должность","Заработная плата","ID автосалона"};


=======
>>>>>>> 7141239 (Initial commit)
    private Connection connection;
    private Statement statement;
    private final String DB_URL = "jdbc:postgresql://localhost:5432/severstal";
    private final String DB_USER = "postgres";
    private final String DB_PASSWORD = "1";

    public DataBase() {
        try {
            //Регистрируем драйвер
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            statement = connection.createStatement();

            try {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS Supplier ( " +
                        "id SERIAL PRIMARY KEY, " +
                        "name VARCHAR(50) NOT NULL);");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS Product ( " +
                        "id SERIAL PRIMARY KEY, " +
                        "name VARCHAR(100) NOT NULL);");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS PricePeriod ( " +
                        "id SERIAL PRIMARY KEY, " +
                        "supplier_id INTEGER NOT NULL REFERENCES Supplier(id), " +
                        " start_date DATE NOT NULL,"+
                        " end_date DATE NOT NULL);");

<<<<<<< HEAD
                //SELECT p.start_date|| ' - ' || p.end_date as period, pr.name,pp.price FROM PricePeriod p INNER JOIN
                //PriceProduct pp ON p.id = pp.price_period_id INNER JOIN product pr ON
                //pp.product_id = pr.id WHERE p.supplier_id=1;
=======

>>>>>>> 7141239 (Initial commit)
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS PriceProduct ( " +
                        "id SERIAL PRIMARY KEY, " +
                        "price_period_id INTEGER NOT NULL REFERENCES PricePeriod(id), " +
                       " product_id INTEGER NOT NULL REFERENCES product(id),"+
                       " price DECIMAL(10, 2) NOT NULL);");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS Transaction ( " +
                        "id SERIAL PRIMARY KEY, " +
                        "date DATE NOT NULL,"+
                        "price_period_id INTEGER REFERENCES Supplier(id), " +
                        " product_id INTEGER NOT NULL REFERENCES product(id),"+
                        "weight DECIMAL(10, 2) NOT NULL);");
<<<<<<< HEAD
//
//                statement.executeUpdate("CREATE TABLE IF NOT EXISTS TransactionList ( " +
//                        "id SERIAL PRIMARY KEY, " +
//                        "transaction_id INTEGER REFERENCES Transaction(id), " +
//                        "fruit_id INTEGER REFERENCES Fruit(id), " +
//                        "quantity INTEGER, " +
//                        "price DECIMAL(10, 2));");
=======

>>>>>>> 7141239 (Initial commit)

            }catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to create tables");}

            System.out.println("Successful launch");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load driver");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
    }

    public void closeConnection()
    {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertPricePeriod(int supplierId, Object startDate, Object endDate) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PricePeriod" +
                " (supplier_id, start_date, end_date) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, supplierId);
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 0) {
                System.out.println("Ошибка при вставке данных");
            } else {
                System.out.printf("Данные успешно добавлены\n");
            }

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();


<<<<<<< HEAD
//            //Выполняем запрос к БД
//            preparedStatement.execute();
//
//            //Закрываем соединение
//            preparedStatement.close();

=======
>>>>>>> 7141239 (Initial commit)
        }
    }

    public void insertTransaction( Object date,int pricePeriodID,int productID, double weight) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Transaction" +
                " (date, price_period_id,product_id, weight) VALUES (?, ?,?, ?)")) {
            preparedStatement.setObject(1, date);
            preparedStatement.setInt(2, pricePeriodID);
            preparedStatement.setInt(3, productID);
            preparedStatement.setDouble(4, weight);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 0) {
                System.out.println("Ошибка при вставке данных");
            } else {
                System.out.printf("Данные успешно добавлены\n");
            }

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    //проверяем есть ли у этого поставщика уже поставка на эту дату
    public boolean checkDateTransaction(Object date,int supplierID)throws SQLException{
        String query = "SELECT EXISTS (SELECT * FROM Transaction\n" +
                "INNER JOIN PricePeriod ON Transaction.price_period_id = PricePeriod.id\n" +
                "WHERE Transaction.date = ? AND PricePeriod.supplier_id = ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, date);
        statement.setInt(2, supplierID);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            boolean exists = resultSet.getBoolean(1); // 1 - это индекс столбца с результатом EXISTS
            return exists;
        } else {
            System.err.println("Ошибка при выполнении запроса.");
            return false;
        }
    }

    //получаем Дату, название, вес, цена для таблицы поставок поставщиков
    public List<DBHelper> getDateNameWeightPrice(int supplierId){
        List<DBHelper> products = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT Transaction.date," +
                "Product.name,Transaction.weight,PriceProduct.price\n" +
                "FROM Transaction\n" +
                "INNER JOIN PricePeriod ON Transaction.price_period_id = PricePeriod.id\n" +
                "INNER JOIN PriceProduct ON Transaction.price_period_id= PriceProduct.id\n" +
                "INNER JOIN Product on Transaction.product_id=Product.id\n" +
                "WHERE PricePeriod.supplier_id = ?;")) {

            preparedStatement.setInt(1, supplierId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getDate("date").toString();
                String name = resultSet.getString("name");
                double weight = resultSet.getDouble("weight");
                double price = resultSet.getDouble("price");

                DBHelper product = new DBHelper(date,name,weight,price);
                products.add(product);
            }

            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

<<<<<<< HEAD

=======
    /**
     *  Тестовые методы( checkTestSupplier,setTestProduct,setTestSupplier) для загрузочных данных
     */

    public int checkTestSupplier()throws SQLException {

        // Создание запроса на проверку наличия данных
        PreparedStatement statement1 = connection.prepareStatement("SELECT COUNT(*) FROM Supplier");
        ResultSet resultSet = statement1.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        } else {
            throw new SQLException("Не удалось получить количество строк в таблице Supplier.");
        }
    }

    public void setTestProduct()throws SQLException {

        // Создание запроса на проверку наличия данных
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Product (name) VALUES " +
                "('Груша Белая'),('Груша Зелёная'), ('Яблоко Красное'), ('Яблоко Жёлтое');");
        statement.executeUpdate();

    }

    public void setTestSupplier()throws SQLException {

        // Создание запроса на проверку наличия данных
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Supplier (name) VALUES " +
                "('Поставщик А'),('Поставщик Б'), ('Поставщик В');");
        statement.executeUpdate();
    }

    public String[] getProduct()throws SQLException {
        String[] supplierNames=new String[4];

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Product ");
        ResultSet resultSet = statement.executeQuery();
        int i=0;
        while (resultSet.next()) {
            supplierNames[i]=resultSet.getString("name");
            i++;
        }

        return supplierNames;
    }

    public String[] getSupplier()throws SQLException {
        String[] supplierNames=new String[3];

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Supplier ");
        ResultSet resultSet = statement.executeQuery();
        int i=0;
        while (resultSet.next()) {
            supplierNames[i]=resultSet.getString("name");
            i++;
        }

        return supplierNames;
    }

>>>>>>> 7141239 (Initial commit)

    //получаем Дату, название, вес, цена для таблицы поставок поставщиков за определенный срок
    public List<DBHelper> getSortDateNameWeightPrice(int supplierId,Object dateStart,Object dateEnd){
        List<DBHelper> products = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT Transaction.date," +
                "Product.name,Transaction.weight,PriceProduct.price\n" +
                "FROM Transaction\n" +
                "INNER JOIN PricePeriod ON Transaction.price_period_id = PricePeriod.id\n" +
                "INNER JOIN PriceProduct ON Transaction.price_period_id= PriceProduct.id\n" +
                "INNER JOIN Product on Transaction.product_id=Product.id\n" +
                "WHERE PricePeriod.supplier_id = ? AND ?<=Transaction.date AND ?>=Transaction.date")) {

            preparedStatement.setInt(1, supplierId);
            preparedStatement.setObject(2, dateStart);
            preparedStatement.setObject(3, dateEnd);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getDate("date").toString();
                String name = resultSet.getString("name");
                double weight = resultSet.getDouble("weight");
                double price = resultSet.getDouble("price");

                DBHelper product = new DBHelper(date,name,weight,price);
                products.add(product);
            }

            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    // Метод для получения идентификатора записи, соответствующей переданной дате
    public int getIdByDate(Object date,int supplierId) throws SQLException {
        String query = "SELECT id FROM PricePeriod WHERE start_date <= ? AND end_date >= ? AND supplier_id= ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, date);
        statement.setObject(2, date);
        statement.setInt(3, supplierId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        } else {
            return 0; //Запись не найдена.
           // throw new SQLException("Запись не найдена.");
        }
    }

    //получаем ID последнего добавленного периода с ценами
    public int getPricePeriodID() {
        int id = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PricePeriod " +
                "ORDER BY id DESC LIMIT 1")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    //получаем ID and Price товаров, цены которых известны на выбранную дату
    public List<DBHelper> getProductIDPrice(int pricePeriodId){
        List<DBHelper> products = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("select product_id,price from " +
                "priceproduct where price_period_id= ?")) {

            preparedStatement.setInt(1, pricePeriodId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int productID = resultSet.getInt("product_id");
                double price = resultSet.getDouble("price");

                DBHelper product = new DBHelper(productID, price);
                products.add(product);
            }

            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    public void insertPriceProduct(int pricePeriodId, int productId, double price) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PriceProduct" +
                " (price_period_id, product_id, price) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, pricePeriodId);
            preparedStatement.setInt(2, productId);;
            preparedStatement.setDouble(3, price);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 0) {
                System.out.println("Ошибка при вставке данных");
            } else {
                System.out.printf("Данные успешно добавлены\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();


        }
    }

    public List<DBHelper> getPeriodNamePrice(int id) {
        List<DBHelper> products = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT p.start_date || ' - ' ||" +
                " p.end_date AS period, pr.name, pp.price FROM PricePeriod p INNER JOIN PriceProduct pp ON " +
                "p.id = pp.price_period_id INNER JOIN product pr ON pp.product_id = pr.id WHERE p.supplier_id="+id+";"))
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String period = resultSet.getString("period");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");

                DBHelper product = new DBHelper(period, name, price);
                products.add(product);
            }

            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }



}
