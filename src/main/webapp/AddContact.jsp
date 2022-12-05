<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form method="post" action="AddContactServlet">
		<table>
			<tr>
				<th><h2>Ajout d'un contact</h2></th>
				<tr>
					<td><i>first name: <input type="text" name="fname" size="25"></i></td>
				</tr>
				<tr>
					<td><i>last name: <input type="text" name="lname" size="25"></i></td>
				</tr>
				<tr>
					<td><i>email: <input type="text" name="email" size="25"></i></td>
				</tr>
				
				<tr>
					<td><input class="button" type="submit" value="Submit" /><input class="button" type="reset" value="Reset"></td>
				</tr>
			
		</table>
	</form>
</body>
</html>