<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title> c:sql </title>
</head>
<body>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<sql:setDataSource driver="com.mysql.cj.jdbc.Driver"
                   url="jdbc:mysql://localhost:3306/db_b202012055"
                   user="u_b202012055"
                   password="cometrue" />

<sql:query var="users" startRow="0" >
    <!-- zipcode 값 변경, ?로 param의 value값을 받아와 사용
    update는 값을 반환 X-->
    update db_b202012055.t_mb202012055 SET db_b202012055.t_mb202012055.zipcode = ? where db_b202012055.t_mb202012055.full_name = 'ckj';
    <!--select * from user where user like ?; -->
    <sql:param value="00100" />
</sql:query>

<c:forEach var="row" items="${users.rows}">
    ${row.mid} :: ${row.full_name} :: ${row.email} :: ${row.zipcode} <br>
    ${row.Host} - ${row.User} <br>
</c:forEach>
</body>
</html>

