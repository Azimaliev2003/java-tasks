package vote;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



public class User extends db{
    static db worker = new db();
    static Scanner num = new Scanner(System.in);
    static Scanner nameCond = new Scanner(System.in);
    static String info;
    public static void run(String login, String password) throws IOException, SQLException {
        Scanner sc = new Scanner(System.in);
        info=login;
        while (true) {
            System.out.println("""
                    Выберите пункт :
                    1.Посмотреть список и рейтинг кондидатов
                    2.Голосовать
                    3.Выход
                    """);
            int com = sc.nextInt();
            if (com <= 0 || com > 4) {
                System.out.println("try again, no existing command(((");
            } else if (com == 1) {
                ratingAndList();
            } else if (com == 2) {
                poll();
            } else if (com == 3)  {
                System.out.println("bye bye");
                break;
            }
        }


    }

    public static void ratingAndList() throws IOException, SQLException {

        // Этот метод показывает список всех кондидатов
        // по их голосов будет изменятся расположение в списке
        Statement st = worker.getConnection().createStatement();

        ResultSet rs = st.executeQuery("SELECT * FROM peoples  where TypeAccount = 'cond' ORDER BY votes DESC");
        int rating = 0;
        while (rs.next()){
            rating++;
            System.out.println(rating+" | Имя кондидата : "+rs.getString("name")+"  |  Общее количество голосов : " + rs.getString("votes"));

        }
        st.close();
    }

    public static void poll() throws IOException, SQLException {
        // Голосование
        PreparedStatement poll = worker.getConnection().prepareStatement("update peoples set votes = votes +1 where name = ?;");
        // SQL запрос для добавление голоса
        PreparedStatement pollus = worker.getConnection().prepareStatement("update peoples set votes = votes +1 where login = ?;");
        // SQL запрос для проверки уже голосовал ли избератель
        Statement st = worker.getConnection().createStatement();
        PreparedStatement PSCOND = worker.getConnection().prepareStatement("SELECT * FROM peoples  where name = ? and TypeAccount='cond'");
        PreparedStatement PSUSER = worker.getConnection().prepareStatement("SELECT * FROM peoples  where login = ? and TypeAccount='user'");
        ResultSet rs = st.executeQuery("SELECT * FROM peoples  where TypeAccount = 'cond' ORDER BY votes DESC");

        int rating = 0;
        while (rs.next()){
            rating++;
            System.out.println(rating+" | Имя кондидата : "+rs.getString("name")+"  |  Общее количество голосов : " + rs.getString("votes"));

        }
        System.out.println("Введите имя кондидата : ");
        String name = nameCond.nextLine();
        PSCOND.setString(1,name);
        PSUSER.setString(1,info);
        ResultSet condRS = PSCOND.executeQuery();
        ResultSet userRS = PSUSER.executeQuery();
//        System.out.println(condRS.getString(2));
        boolean bool = false;
        if (userRS.next() && condRS.next()){
            bool = true;
        }

        if (bool && userRS.getInt(7)==0){
            if (condRS.getString(2).trim().equals(name)){
                System.out.println("Вы можете голосовать : "+userRS.getString(2));
                poll.setString(1,name);
                pollus.setString(1,info);
                poll.executeUpdate();
                pollus.executeUpdate();
            }
            else {
                System.out.println("Error1");
            }
        } else if (bool && userRS.getInt(7)==1) {
            System.out.println("Вы уже голосовали!!!");
        } else {
            System.out.println("Error2");
        }


        st.close();
    }
}