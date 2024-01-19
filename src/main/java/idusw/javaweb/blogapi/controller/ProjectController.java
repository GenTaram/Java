package idusw.javaweb.blogapi.controller;

import idusw.javaweb.blogapi.model.ProjectDTO;
import idusw.javaweb.blogapi.util.ConnectionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


// 데이터 처리 기본 연산 : C.R.U.D (Create, Read, Update, Delete)
// HTTP Method(Rest API 관련 높음) : post, get, (update, delete) - jsp에서는 문제가 있음
// Http Method - view vs. process
@WebServlet(name="projectController", urlPatterns = {
        "/projects/add-form", "/projects/add",
        "/projects/list", "/projects/edit",
        "/projects/update-form", "/projects/update",
        "/projects/delete-form", "/projects/delete",
        "/projects/detail", "/projects/search"}) //rest 방식 X

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 30,
        maxRequestSize = 1024 * 1024 * 50)

public class ProjectController extends HttpServlet {
    ConnectionManager connectionManager = ConnectionManager.getInstance();
    ProjectDTO projectDTO = null;
    List<ProjectDTO> projectDTOList = null;
    int cnt = 0;

    public void process (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int index = request.getRequestURI().lastIndexOf("/"); // lastIndexOf : 마지막으로 나온 '/'의 위치 인덱스, post랑 login을 잘라옴
        String command = request.getRequestURI().substring(index + 1);
        HttpSession session = request.getSession(); // HttpSession 객체를 가져옴, 없는 경우 생성

        String sql = null;
        String tableName = "t_prjb202012055";

        if (command.equals("detail")){
            projectDTO = new ProjectDTO();
            projectDTO.setPid(Long.valueOf(request.getParameter("pid")));
            sql = " select * from " + tableName + " where pid = " + projectDTO.getPid();

            try(Connection conn = connectionManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);)
            {
                if(rs.next()){
                    projectDTO = setRsToDTO(rs);
                }
            }catch (SQLException e){
                throw new RuntimeException(e);
            } finally {
                if(projectDTO != null){
                    request.setAttribute("pDto", projectDTO);
                    request.getRequestDispatcher("../projects/detail.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 상세 보기 실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        if (command.equals("add")) {
            projectDTO = new ProjectDTO();
            projectDTO.setProjectName(request.getParameter("project-name"));
            projectDTO.setStatus(request.getParameter("status"));
            projectDTO.setProjectDescription(request.getParameter("project-description"));
            projectDTO.setProjectLeader(request.getParameter("project-leader"));

            // 이미지 파일 업로드 처리
            fileUpload(request, response);
            projectDTO.setProjectImage((String) request.getAttribute("project-image"));

            sql = "insert into t_prjb202012055(project_name, project_description, status, project_leader, project_image) values (?, ?, ?, ?, ?)";

            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);)
            {
                pstmt.setString(1, projectDTO.getProjectName());
                pstmt.setString(2, projectDTO.getProjectDescription());
                pstmt.setString(3, projectDTO.getStatus());
                pstmt.setString(4, projectDTO.getProjectLeader());
                pstmt.setString(5, projectDTO.getProjectImage());
                cnt = pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            finally {
                if(cnt >= 1) {
                    response.sendRedirect("../projects/list");
                }
                else{
                    request.setAttribute("message", "프로젝트 등록 실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if (command.equals("add-form")) {
            request.getRequestDispatcher("../projects/add.jsp").forward(request, response);
        }
        else if (command.equals("list")) {
            String orderBy = request.getParameter("orderby");
            String direction = request.getParameter("direction");
            String condition = "";
            if(orderBy != null && direction != null){
                condition = " order by " + orderBy + " " + direction;
            }
            sql = " select * from " + tableName + condition;
            //sql = "select * from t_prjb202012055";
            try (Connection conn = connectionManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql);)
            {
                projectDTOList = new ArrayList<>();
                while(rs.next()) { // ResultSet : 질의 결과를 다루는 클래스
                    ProjectDTO project = setRsToDTO(rs);
                    projectDTOList.add(project);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(projectDTOList != null) {
                    request.setAttribute("pDtoList", projectDTOList); // Attribute Key - Value
                    request.getRequestDispatcher("../projects/list.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 등록 실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if (command.equals("edit")) {
            request.getRequestDispatcher("../projects/edit.jsp").forward(request, response);
        }
        else if (command.equals("update-form")){
            projectDTO = new ProjectDTO();
            projectDTO.setPid(Long.valueOf(request.getParameter("pid")));
            sql = "select * from t_prjb202012055 where pid= " + projectDTO.getPid();

            try(Connection conn = connectionManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);)
            {
                if(rs.next()){
                    projectDTO = setRsToDTO(rs);
                }
            }catch (SQLException e){
                throw new RuntimeException(e);
            } finally {
                if(projectDTO != null){
                    request.setAttribute("pDto", projectDTO);
                    request.getRequestDispatcher("../projects/edit.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 업데이트 폼 로드 실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if (command.equals("update")){
            projectDTO = new ProjectDTO();
            projectDTO.setPid(Long.valueOf(request.getParameter("pid")));
            projectDTO.setProjectName(request.getParameter("project-name"));
            projectDTO.setStatus(request.getParameter("status"));
            projectDTO.setProjectDescription(request.getParameter("project-description"));
            projectDTO.setProjectLeader(request.getParameter("project-leader"));

            sql = "update t_prjb202012055 set project_name = ?, project_description = ?, status = ?, project_leader = ? where pid = ? ";
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);)
            {
                pstmt.setString(1, projectDTO.getProjectName());
                pstmt.setString(2, projectDTO.getProjectDescription());
                pstmt.setString(3, projectDTO.getStatus());
                pstmt.setString(4, projectDTO.getProjectLeader());
                pstmt.setLong(5, projectDTO.getPid());
                cnt = pstmt.executeUpdate();
            } catch(SQLException e){
                throw new RuntimeException(e);
            } finally {
                if(cnt >= 1){
                    response.sendRedirect("../projects/list");
                }
                else {
                    request.setAttribute("message", "프로젝트 업데이트 실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                    System.out.println(cnt);
                }
            }
        }
        else if (command.equals("delete")){
            projectDTO = new ProjectDTO();
            projectDTO.setPid(Long.valueOf(request.getParameter("pid")));

            sql = "delete from " + tableName + " where pid=?";
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);){
                pstmt.setLong(1, projectDTO.getPid());
                cnt = pstmt.executeUpdate();
            } catch (SQLException e){
                throw new RuntimeException(e);
            } finally {
                if(cnt >= 1){
                    response.sendRedirect("../projects/list");
                }
                else{
                    request.setAttribute("message", "프로젝트 등록 실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if (command.equals("search")) {
            String fieldName = "project_name";
            String by = request.getParameter("by");
            String keyword = request.getParameter("keyword");
            if (by.equals("Leader"))
                fieldName = "project_leader";

            sql = "select * from " + tableName + " where " + fieldName + " like '%" + keyword + "%'";
            try (Connection conn = connectionManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql);) {
                projectDTOList = new ArrayList<>();
                while (rs.next()) {
                    ProjectDTO project = setRsToDTO(rs);
                    projectDTOList.add(project);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                request.setAttribute("pDtoList", projectDTOList);
                request.getRequestDispatcher("../projects/list.jsp").forward(request, response);
            }
        }
    }
    private static final String SAVE_DIR = "files";
    private String partName = null;
    private String partValue = null;

    public void fileUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appPath = request.getServletContext().getRealPath("");
        String savePath = appPath + File.separator + SAVE_DIR;
        File fileSaveDir = new File(savePath);
        if( !fileSaveDir.exists() ) {
            fileSaveDir.mkdir();
        }
        Collection<Part> parts = request.getParts();
        for (Part part : parts) {
            partName = part.getName();
            if(part.getContentType() != null) {
                partValue = getFileName(part);
                if(partValue != null && !partValue.isEmpty()) {
                    part.write(savePath + File.separator + partValue);
                }
            }
            else {
                partValue = request.getParameter(partName);
            }
            request.setAttribute(partName, partValue);
        }
    }
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items){
            if (s.trim().startsWith("filename")){
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }

    private ProjectDTO setRsToDTO(ResultSet rs) throws SQLException {
        projectDTO = new ProjectDTO();
        projectDTO.setPid(rs.getLong("pid"));
        projectDTO.setProjectName(rs.getString("project_name"));
        projectDTO.setProjectDescription(rs.getString("project_description"));
        projectDTO.setStatus(rs.getString("status"));
        projectDTO.setProjectLeader(rs.getString("project_leader"));
        projectDTO.setRegTimeStamp(rs.getTimestamp("reg_timestamp"));
        projectDTO.setRevTimeStamp(rs.getTimestamp("rev_timestamp"));
        projectDTO.setProjectImage(rs.getString("project_image"));
        return projectDTO;
    }
    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response); //process로 호출하여 Post와 get 처리를 한번에 모아놓고 URI에 따라 선택
        fileUpload(request, response);
    }
}