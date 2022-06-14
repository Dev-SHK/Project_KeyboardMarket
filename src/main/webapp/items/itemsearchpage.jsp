<%@page import="model.item.itemDTO"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Arrays"%>
<%@page import="model.order.OrderDTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
	crossorigin="anonymous">
<%@page import="model.member.memberDTO"%>
<%	
	int memberNo;
	String name = "비회원";
	HttpSession user = request.getSession();
	String userStatus = null;
	memberDTO dto = (memberDTO) user.getAttribute("inUser");
	if (dto != null) {
		userStatus = dto.getStatus();
		memberNo = dto.getMemberNo();
		name = dto.getName();
	} else {
		memberNo = 0;
	}
	pageContext.setAttribute("memberNo",memberNo);
%>
<title>관리자 페이지</title>
</head>
<body>
	<div class="jumbotron">
		<div class="container">
			<h3 class="display-3">${itemname }로 검색한 상품</h3>
		</div>
	</div>
	
	<div class="container">
		<table table-striped class="table table-striped">
				<c:forEach var="name" items="${itemList }" varStatus="i">
					<c:if test="${name.item_name != null}">
						<tr>
							<th><a href="./itemdetail?id=${name.itemNo}"><img
								src="<c:url value="/source/${name.item_img1 }"/>" alt=""
								width=150 height=150 /></a></th>
							<td>
								<p>[상품번호:${name.itemNo }]</p>
								<p><a href="./itemdetail?id=${name.itemNo}">[${name.manufacture}] ${name.item_name}</a></p>
							</td>
							<td>
								<p>${name.price}원</p> <c:choose>
									<c:when test="${name.stock == 0 }">
										<p>재고없음</p>
									</c:when>
									<c:otherwise>
										<p>[재고:${name.stock }]</p>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:if>
				</c:forEach>
				</tbody>
			</table table-striped>
	</div>

</body>
</html>