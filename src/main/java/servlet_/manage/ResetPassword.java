package servlet_.manage;

import druid_JDBC_utils.Druid_Utils;
import Utils.HashPassword;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// todo 重置密码的后端url
@WebServlet("/ResetPassword")
public class ResetPassword extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String id = req.getParameter("id");
        String pwd = id;
        System.out.println("reset id: " + id);
        pwd = HashPassword.hashPassword(pwd);
        try {
            String sql = "update student_info set password=? where Student_ID=?";
            connection = Druid_Utils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, pwd);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Druid_Utils.close(resultSet, preparedStatement, connection);
        }
        String js = "<script>alert('重置密码成功！'); window.location.href='http://localhost:8080/_war_exploded/showStudentByPage';</script>";
        resp.getWriter().print(js);

    }
}
