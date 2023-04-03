package vote;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Admin extends db {

    static db worker = new db();
    public static void run() throws IOException, SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    Выберите пункт :
                    1.Добавить кондидата
                    2.Посмотреть список и рейтинг кондидатов
                    3.Выход
                    """);
            int com = sc.nextInt();
            if (com <= 0 || com > 4) {
                System.out.println("try again, no existing command(((");
            } else if (com == 1) {
                addCondidate();
            } else if (com == 2) {
                ratingAndList();
            } else if (com == 3)  {
                System.out.println("bye bye");
                break;
            }
        }
    }

    public static void addCondidate() throws SQLException {
        String insert = "insert into peoples ( " +
                "name, lastname, login, password,TypeAccount ) values (?, ?, ?, ?,'cond');";
        PreparedStatement ps = worker.getConnection().prepareStatement(insert);
        Scanner regisUser = new Scanner(System.in);
        System.out.println("Что бы зарегистрировать пользователя заполните данные");
        System.out.println("Введите имя : ");
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
        ps.close();
        System.out.println("Вы успешно зарегистировали!");
    }
    public static void ratingAndList() throws IOException, SQLException {
        Statement st = worker.getConnection().createStatement();

        ResultSet rs = st.executeQuery("SELECT * FROM peoples  where TypeAccount = 'cond' ORDER BY votes DESC");
        int rating = 0;
        while (rs.next()){
            rating++;
            System.out.println(rating+" | Имя кондидата : "+rs.getString("name")+"  |  Общее количество голосов : " + rs.getString("votes"));

        }
        st.close();
    }
}
