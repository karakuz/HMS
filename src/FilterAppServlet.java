import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FilterAppServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String department = request.getParameter("departments");

        String patientEmail = "";
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("email"))
                patientEmail = cookie.getValue();
        }

        String pEmail = patientEmail;
        String depName = department;

        ArrayList<String> times = new ArrayList<>();
        ArrayList<String> dNames = new ArrayList<>();
        ArrayList<String> depNames = new ArrayList<>();

        DB_Handler handler = new DB_Handler();

        try {
            String sql = "SELECT datetime, users.name, departments.name  FROM appointments inner join userdeprel inner join departments inner join users " +
                    " on appointments.d_id = userdeprel.u_id and userdeprel.dept_id = departments.dept_id and appointments.d_id = users.u_id " +
                    " where p_id = (select u_id from users where email = ? ) and departments.name =  ?";
            handler.init();
            PreparedStatement pstmt = handler.getConn().prepareStatement(sql);
            pstmt.setString(1,pEmail);
            pstmt.setString(2,depName);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                times.add(rs.getString(1));
                dNames.add(rs.getString(2));
                depNames.add(rs.getString(3));
            }

            handler.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(times);
        System.out.println(dNames);
        System.out.println(depNames);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
