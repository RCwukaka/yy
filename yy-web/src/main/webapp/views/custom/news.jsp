<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/system-web/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="/system-web/js/wangEditor-1.3.12.min.js"></script>
<link rel="stylesheet" href="/system-web/css/wangEditor-1.3.12.css"
	type="text/css">
<link rel="stylesheet" href="/system-web/css/news.css" type="text/css">
<link rel="stylesheet" href="/system-web/css/bootstrap.min.css">
<link rel="stylesheet" href="/system-web/css/basepage.css">
<title>新闻</title>
</head>
<body>
	<jsp:include page="navbar.jsp"></jsp:include>
	<div class="content">
		<div class="row">
			<div class="col-md-9">
				<h2>文章标题</h2>
				<p style="display: none">新闻id</p>
				<div>
					<p style='display: inline-block;'>作者:</p>
					<p style='display: inline-block; margin-left: 20px;'>更新时间:</p>
				</div>
				<div>文章插入图片</div>
				<div>文章内容</div>
				<textarea id='commentContent' style='height: 200px; width: 100%;'></textarea>
				<button class="btn btn-warning" style="margin-top:10px" id="send">发表评论</button>
			</div>
			<div class="col-md-3">
				<div id="article_side">
					<div class="row">
						<div class="col-md-6">
							<img class="img-circle" width="60px" height="60px"
								style="display: block; margin: 0 auto;" src="/system-web/img/test.png">
						</div>
						<div class="col-md-6">
							<p>作者</p>
							<p>简介</p>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							文章数
						</div>
						<div class="col-md-6">
							阅读量
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/html/common/foot.html"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$('#commentContent')
					.wangEditor(
							{
								'menuConfig' : [
										[ 'bold', 'underline', 'italic' ],
										[ 'blockquote', 'fontFamily', 'list',
												'justify' ],
										[ 'createLink', 'unLink', 'insertTable' ],
										[ 'undo', 'redo' ] ]
							});
		});
	</script>
	<script src="/system-web/js/bootstrap.min.js"></script>
</body>
</html>