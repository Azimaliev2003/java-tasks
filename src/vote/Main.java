package vote;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 Система электронного голосования

 Сначала система нас спрашивает есть ли у нас аккаунт
    "У вас есть акккаунт?   Да-1 / Нет-2"

 если есть то нажимаем на 1 если нет то 2
 после этого выберем тип аккаунта :
    "Выберите тип аккаунта : "
        "1 - Админ"
        "2 - Избератель"

 У нас в базе есть только один аккаунт "Админа"
 и никто не может создать аккаунт админа

 Если выберем 1 или 2 то система вызывает метот login();

 if (type == 1){
    accType = "admin";
    Login();
 } else if(type == 2){
    accType = "user";
    Login();
 }

 Если у нас нет аккаунта мы можем создать новый
 Но только аккаунт избирателя то есть
 тип аккаунта по умолчанию будет как Избератель


 ------Аккаунт админа-------


    Приветствую !!! 'Нурик' (Имя админа мы получаем из базы данных по его логину)
    Выберите пункт :
    1.Добавить кондидата
        (Добавим кондидата изначально у него будет 0 голос)
    2.Посмотреть список и рейтинг кондидатов
        1 | Имя кондидата : con1     |  Общее количество голосов : 8
        2 | Имя кондидата : con2     |  Общее количество голосов : 2
        3 | Имя кондидата : Алексей  |  Общее количество голосов : 2
        4 | Имя кондидата : con3     |  Общее количество голосов : 0
    3.Выход

 ------Аккаунт изберателя------

        Приветствую !!! 'Нурик' (Имя изберателя мы получаем из базы данных по его логину)
        Выберите пункт :
        1.Посмотреть список и рейтинг кондидатов
            1 | Имя кондидата : con1     |  Общее количество голосов : 8
            2 | Имя кондидата : con2     |  Общее количество голосов : 2
            3 | Имя кондидата : Алексей  |  Общее количество голосов : 2
            4 | Имя кондидата : con3     |  Общее количество голосов : 0
        2.Голосовать
            (Выводится список кондидатов)
            и система просит нас вести имя кондидата
            если мы уже провели голосование
            в базе сохраняется и
            мы не можем повторно голосовать
 3.Выход


 **/

public class Main extends db {
    static db worker = new db(); //подключение к базе данных идет через класс "db.java"
    static Scanner log = new Scanner(System.in);
    static String accType; // это для того чтобы определить тип аккаунта "Избератель" или "Админ"
    public static void main(String[] args) throws SQLException, IOException {
        String insert = "insert into peoples ( " +
                "name, lastname, login, password ) values (?, ?, ?, ?);";  //  SQL запрос для добавление данных
        PreparedStatement ps = worker.getConnection().prepareStatement(insert); // PreparedStatement используется для выполнения SQL-запросов
        Scanner input = new Scanner(System.in);
        Scanner regisUser = new Scanner(System.in);
        int choice;
        do {
                System.out.println("У вас есть акккаунт?   Да-1 / Нет-2");
                choice = input.nextInt();

                if (choice == 1){
                    System.out.println("Выберите тип аккаунта : ");
                    System.out.println("1 - Админ \n" +
                                        "2 - Избератель");
                    int type = input.nextInt();

                    if (type == 1){
                        accType = "admin";
                        Login(); // Вызов метода логин
                    } else if(type == 2){
                        accType = "user";
                        Login(); // Вызов метода логин
                    }
                }
                else if (choice == 2){
                    System.out.println("Что бы зарегистрироватся заполните данные");
                    System.out.println("Введите свое имя : ");
                    String name = regisUser.nextLine();
                    System.out.println("Введите фамилию : ");
                    String lastname = regisUser.nextLine();
                    System.out.println("Введите логин : ");
                    String login = regisUser.nextLine();
                    System.out.println("Введите пароль : ");
                    String pass = regisUser.nextLine();
                    ps.setString(1,name);
                    ps.setString(2,lastname);
                    ps.setString(3,login);
                    ps.setString(4,pass);
                    ps.execute();
                    System.out.println("Вы успешно зарегистировались!!!");
                }
            }while (choice !=3);

        }


    public static void Login() throws SQLException, IOException {
        PreparedStatement ps = worker.getConnection().prepareStatement("select * from peoples where login = ? and password = ? and TypeAccount =  '" + accType + "'");
        boolean isUserExists = false;
        System.out.println("Введите логин : ");
        String login = log.nextLine();
        System.out.println("Введите пароль : ");
        String password = log.nextLine();
        ps.setString(1, login);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {   // проверка на наличие записи в БД
            isUserExists = true;
        }
        if (isUserExists && rs.getString(6).trim().equals("user")) {
            // Здесь проверяем какой тип аккаунта пользователя
            System.out.println("Приветствую !!! " + rs.getString(2));
            // если избератель то вызываем класс "User" c методом "run"
            User.run(login,password);
        }
        else if (isUserExists && rs.getString(6).trim().equals("admin")) {
            // Аналогичго
            System.out.println("Приветствую !!! " + rs.getString(2));
            Admin.run();
        }
    }

}

