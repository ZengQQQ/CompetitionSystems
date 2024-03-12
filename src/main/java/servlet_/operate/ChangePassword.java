package servlet_.operate;

import Utils.HashPassword;
import druid_JDBC_utils.Druid_Utils;

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

@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("changePassword.html").forward(req,resp);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 返回结果
        // todo 修改结构
        String result = "false";


        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String id = (String) req.getSession().getAttribute("Student_ID");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");



        oldPassword = HashPassword.hashPassword(oldPassword);
        newPassword = HashPassword.hashPassword(newPassword);
        try {
            String sql = "select * from student_info where  Student_ID=? and password=?";
            connection = Druid_Utils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, oldPassword);
            ResultSet rs = preparedStatement.executeQuery();
            // 旧密码正确
            if (rs.next()) {
                // 更新密码
                sql = "update student_info set password=? where Student_ID=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, id);
                // 返回更新的行数
                int r = preparedStatement.executeUpdate();
                if (r == 0) {
                    throw new RuntimeException("更新密码失败");
                }else {
                    result= "true";
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Druid_Utils.close(resultSet, preparedStatement, connection);
        }
        resp.getWriter().print("<script>alert('"+result+"');window.location.href='ChangePassword'</script>");
    }

}
