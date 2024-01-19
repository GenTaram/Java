package idusw.javaweb.blogapi.controller;

import idusw.javaweb.blogapi.model.MemberDTO;
import idusw.javaweb.blogapi.model.ProjectDTO;
import idusw.javaweb.blogapi.util.ConnectionManager;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import javax.print.attribute.standard.MediaName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

// 데이터 처리 기본 연산 : C.R.U.D (Create, Read, Update, Delete)
// HTTP Method(Rest API 관련 높음) : post, get, (update, delete) - jsp에서는 문제가 있음
// Http Method - view vs. process
@WebServlet(name="memberController", urlPatterns = {
        "/members/post-form", "/members/register", "/members/get-list",
        "/members/detail", "/members/update", "/members/delete",
        "/members/login-form", "/members/login", "/members/logout"}) //rest 방식 X
public class MemberController extends HttpServlet {
    // 다형성을 활용
    // Generics를 사용, 컴파일 시점에 유형 문제를 처리할 수 있다.
    List<MemberDTO> memberDTOList = null;
    // JDBC(Java DataBase Connectivity) : 자바 기반 데이터베이스 활용을 위한 API
    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    ConnectionManager connectionManager = ConnectionManager.getInstance();
    int cnt = 0;

    public void process (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        conn = connectionManager.getConnection();
        int index = request.getRequestURI().lastIndexOf("/"); // lastIndexOf : 마지막으로 나온 '/'의 위치 인덱스, post랑 login을 잘라옴
        String command = request.getRequestURI().substring(index + 1);

        HttpSession session = request.getSession();
        // HttpSession 객체를 가져와 생성
        // session : 페이지에 구애받지 않고 일정 시간 동안 유지
        // request : 페이지가 변경되는 경우(forward가 아닌 경우) 다시 생성됨

        //substring 지정한 위치 다음부터 잘라옴
        if (command.equals("register")) {
            String sql = "insert into t_mb202012055(full_name, email, pw) values (?, ?, ?)";
            MemberDTO member = new MemberDTO();
            member.setFullName(request.getParameter("full-name"));
            member.setEmail(request.getParameter("email"));
            String pw1 = request.getParameter("pw1");
            String pw2 = request.getParameter("pw2");
            if (!pw1.equals(pw2)) {
                System.out.println("암호 불 일치로 작업 중단");
                return;
            }
            member.setPw(pw1);
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setString(1, member.getFullName());
                pstmt.setString(2, member.getEmail());
                pstmt.setString(3, member.getPw());
                cnt = pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (cnt >= 1) {
                    request.getRequestDispatcher("../main/index.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "회원가입 실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if (command.equals("post-form")) {
            request.getRequestDispatcher("../members/register.jsp").forward(request, response);
        }
        else if (command.equals("get-list")) {
            String sql = "select * from t_mb202012055";
            try (Connection conn = connectionManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                memberDTOList = new ArrayList<>();
                while(rs.next()) {
                    MemberDTO m = new MemberDTO();
                    m.setMid(rs.getLong(1));
                    m.setFullName(rs.getString("full_name"));
                    m.setEmail(rs.getString(3));
                    m.setPw(rs.getString("pw"));
                    m.setZipcode(rs.getString(5));
                    memberDTOList.add(m);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (memberDTOList != null) {
                    request.setAttribute("dtolist", memberDTOList);
                    request.getRequestDispatcher("list.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if (command.equals("login-form")) {
                request.getRequestDispatcher("../members/login.jsp").forward(request, response);
            }
        else if (command.equals("login")) {
            String sql = "select * from t_mb202012055 where email = ? and pw = ?";
            MemberDTO m = null;
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, request.getParameter("email"));
                pstmt.setString(2, request.getParameter("pw1"));
                try (ResultSet rs = pstmt.executeQuery()) {
                    m = null;
                    if (rs.next()) {
                        m = new MemberDTO();
                        m.setMid(rs.getLong(1));
                        m.setFullName(rs.getString("full_name"));
                        m.setEmail(rs.getString(3));
                        m.setPw(rs.getString("pw"));
                        m.setZipcode(rs.getString(5));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (m != null) {
                    session.setAttribute("dto", m);
                    request.getRequestDispatcher("../main/index.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if (command.equals("detail")) {
            String sql = "select * from t_mb202012055 where mid = ?";
            MemberDTO m = null;
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setLong(1, Long.parseLong(request.getParameter("seq")));
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        m = new MemberDTO();
                        m.setMid(rs.getLong("mid"));
                        m.setFullName(rs.getString("full_name")); // rs로 가져와진 레코드의 필드값을 m 객체의 필드에 set 함
                        m.setEmail(rs.getString(3));
                        m.setPw(rs.getString("pw"));
                        m.setZipcode(rs.getString("zipcode"));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                String url = m != null ? "../members/detail.jsp" : "../errors/fail.jsp";
                if (m != null) {
                    request.setAttribute("dto", m);
                }
                request.getRequestDispatcher(url).forward(request, response);
            }
        }
        else if (command.equals("update")) {
            MemberDTO member = new MemberDTO();
            member.setMid(Long.valueOf(request.getParameter("mid")));
            member.setFullName(request.getParameter("full-name"));
            member.setEmail(request.getParameter("email"));
            member.setPw(request.getParameter("pw1"));
            member.setZipcode(request.getParameter("zipcode"));

            String sql = "update t_mb202012055 set full_name = ?, pw = ?, zipcode = ? where mid = ?";
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, member.getFullName());
                pstmt.setString(2, member.getPw());
                pstmt.setString(3, member.getZipcode());
                pstmt.setLong(4, member.getMid());
                cnt = pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (cnt > 0) {
                    response.sendRedirect("../members/detail.jsp");
                } else {
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if (command.equals("logout")){
            session.invalidate();
            request.getRequestDispatcher("../main/index.jsp").forward(request, response);
        }
        else if (command.equals("delete")) {
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMid(Long.valueOf(request.getParameter("mid")));
            String sql = "delete from t_mb202012055 where mid = ?";
            int cnt = 0;
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, memberDTO.getMid());
                cnt = pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                session.invalidate();
                if (cnt > 0) {
                    request.getRequestDispatcher("../main/index.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
    }
    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response); //process로 호출하여 Post와 get 처리를 한번에 모아놓고 URI에 따라 선택
    }
}

