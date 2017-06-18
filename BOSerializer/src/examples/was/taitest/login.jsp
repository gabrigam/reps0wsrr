<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<HTML>
<HEAD>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>login.jsp</TITLE>
</HEAD>
<BODY>
<jsp:useBean id="errorMessage" scope="request" class="java.lang.String"></jsp:useBean><BR>
<%=errorMessage%>
</P><HR>
<FORM method="post" action="/TAITest/PrintUserInfo">User Name: <INPUT type="text" name="userid" size="20"><BR>
<BR>Password: <INPUT type="password" name="password" size="20"><BR>
<BR>
<INPUT type="checkbox" name="AdminPriv" value="Y">I want to login in with administrative privileges
<BR>
<BR>
<INPUT type="hidden" name="from_login_jsp" value="Y">
<INPUT type="submit" name="submit" value="Submit"></FORM>
<P></P>
</BODY>
</HTML>
