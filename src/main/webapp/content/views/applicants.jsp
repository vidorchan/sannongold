<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 10/14/14
  Time: 10:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="content/static/css/bootstrap-3.2.0/bootstrap.css" rel="stylesheet">
    <title></title>
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <jsp:include page='navbar.jsp'/>
        </div>
    </div>
    <div class="row clearfix">
        <div class="col-md-2 column">
            <jsp:include page='sidebar.jsp'/>
        </div>
        <div class="col-md-10 column">
            <div class="row">

                <div class="col-lg-6">

                    <div class="input-group">
                       <span class="input-group-addon"><label type="text" value="">查询条件</label></span>
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"  id="searchKey">用户名<span class="caret"></span></button>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="javascript:void(0)" onclick="changeContent()" id="dropdown1">手机号</a></li>
                            </ul>
                        </div><!-- /btn-group -->
                        <input type="text" class="form-control" id="searchValue">
                    </div><!-- /input-group -->
                </div><!-- /.col-lg-6 -->
                <button type="submit" class="btn btn-success" id="retrieve">查询</button>
                <button type="submit" class="btn btn-sm btn-primary">导出问卷调查结果</button>
            </div><!-- /.row -->

            <br/>

            <table class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th>
                        #
                    </th>
                    <th>
                        用户名
                    </th>
                    <th>
                        注册日期
                    </th>
                    <th>
                        手机号码
                    </th>
                    <th>
                        工作单位
                    </th>
                    <th>
                        职位
                    </th>
                    <th>

                    </th>
                </tr>
                </thead>
                <tbody>
                
                <c:forEach items="${applicants}" var="user" varStatus="stauts">
                <tr>
                    <td>
                        ${stauts.count}
                    </td>
                    <td>
                        ${user.realName}
                    </td>
                    <td>
                        ${user.createTime}
                    </td>
                    <td id="cell${stauts.count}">
                        ${user.cellphone}
                    </td>
                    <td>
                        ${user.company}
                    </td>
                    <td>
                        ${user.jobTitle}
                    </td>
                    <td>
                        <a class="btn btn-sm btn-success" href="javascript:void(0)" onclick="showQuestionnaire(${stauts.count})">问卷调查</a>
                    </td>
                </tr>
                </c:forEach>
                </tbody>
            </table>

            <ul class="pagination">
                <li><a href="#">&laquo;</a></li>
                <li><a href="#">1</a></li>
                <li><a href="#">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">5</a></li>
                <li><a href="#">&raquo;</a></li>
            </ul>
        </div>
    </div>
</div>
<div class="row clearfix">
    <div class="col-md-12 column">
        <jsp:include page='footer.jsp'/>
    </div>
</div>
</body>

<script type="text/javascript">
	function changeContent(){
		var searchKey = $("#searchKey").text();
		var dropDown1 = $("#dropdown1").text();
		
		$("#dropdown1").text(searchKey);
		$("#searchKey").html(dropDown1 + '<span class="caret">');
	}

	$("#retrieve").click(function(){
		var searchKey = $("#searchKey").text().trim();
		var searchValue = $("#searchValue").val();
		
		var url;
		if (searchKey == "手机号")
		{
			url = "applicants?cellphone=" + searchValue;
		}
		else if (searchKey == "用户名")
		{
			url = "applicants?realName=" + searchValue;
		}
		location.href = url;
	})
	
	function showQuestionnaire(id){
		var id = "cell" + id;
		var cellphone = $("#" + id).text().trim();

		location.href = "questionnaireanswer?cellphone=" + cellphone;
	}
	
</script>
</html>