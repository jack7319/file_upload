<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>webuploader组件上传</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<!-- 1.准备好webuploader的资源 -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/webuploader.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/webuploader.js"></script>
	<style type="text/css">
		#dndArea{
			width:200px;
			height:100px;
			border-color:red;
			border-style: dashed;
		}
	</style>
	
  </head>
  
  <body>
          <!-- 2.设计一个页面元素 -->
          
          <div id="uploader">
          		<!-- 该元素用于拖拽文件 -->
          		<div id="dndArea"></div>
          
          		<!-- 用于显示文件列表 -->
          		<div id="fileList"></div>
          		
          		<!-- 用于选择文件 -->
               <div id="filePicker">点击选择文件</div>
          </div>
          
          
          
	       <!-- 3.使用webuploader进行渲染 -->
          <script type="text/javascript">
          		//获取到了文件的标记
          		var fileMd5;
          		//监听分块上传过程中的三个时间点
          		WebUploader.Uploader.register({
						"before-send-file":"beforeSendFile",
						"before-send":"beforeSend",
						"after-send-file":"afterSendFile"
					},{
						//时间点1：:所有分块进行上传之前调用此函数
						beforeSendFile:function(file){
							var deferred = WebUploader.Deferred();
						
							//1.计算文件的唯一标记，用于断点续传和秒传
							(new WebUploader.Uploader()).md5File(file,0,5*1024*1024)
									.progress(function(percentage){
										$("#"+file.id).find("div.state").text("正在获取文件信息...");
									})
									.then(function(val){
										fileMd5 = val;
										
										$("#"+file.id).find("div.state").text("成功获取文件信息");
										//只有文件信息获取成功，才进行下一步操作
										deferred.resolve();
									});
							
							//2.请求后台是否保存过该文件，如果存在，则跳过该文件，实现秒传功能
							return deferred.promise();
						},
						//时间点2：如果有分块上传，则 每个分块上传之前调用此函数
						beforeSend:function(block){
							var deferred = WebUploader.Deferred();
							
							//alert(fileMd5);
							$.ajax(
								{
								type:"POST",
								url:"${pageContext.request.contextPath}/UploadActionServlet?action=checkChunk",
								data:{
									//文件唯一标记
									fileMd5:fileMd5,
									//当前分块下标
									chunk:block.chunk,
									//当前分块大小
									chunkSize:block.end-block.start
								},
								dataType:"json",
								success:function(response){
									if(response.ifExist){
										//分块存在，跳过该分块
										deferred.reject();
									}else{
										//分块不存在或者不完整，重新发送该分块内容
										deferred.resolve();
									}
								}
								}
							);
							
							this.owner.options.formData.fileMd5 = fileMd5;
							
							return deferred.promise();
							//1.请求后台是否保存过当前分块，如果存在，则跳过该分块文件，实现断点续传功能
						},
						//时间点3：所有分块上传成功之后调用此函数
						afterSendFile:function(){
							//1.如果分块上传，则通过后台合并所有分块文件
							
							$.ajax(
							{
							type:"POST",
							url:"${pageContext.request.contextPath}/UploadActionServlet?action=mergeChunks",
							data:{
								fileMd5:fileMd5
							},
							success:function(response){
								
							}
							}
							);
							
							
						}
					});
          		
          
          		var uploader = 	WebUploader.create(
          				{
          				//flash的地址	
          				swf:"${pageContext.request.contextPath}/js/Uploader.swf",
          				//设置提交的服务地址
          				server:"${pageContext.request.contextPath}/UploadServlet",
          				//渲染文件上传元素
          				pick:"#filePicker",
          				//自动上传
          				auto:true,
          				//开启拖拽
          				dnd:"#dndArea",
          				//屏蔽拖拽区域外的功能
          				disableGlobalDnd:true,
          				//开启黏贴功能
          				paste:"#uploader",
          				//开启分块上传
          				chunked:true,
						//每块文件大小（默认5M）
						chunkSize:5*1024*1024
          				}
          			);
          			
          			
          		//4.实现选择文件，并且提示文件的功能
          		//file:代表选择到的哪个文件
          		uploader.on("fileQueued",function(file){
          			//把文件信息追加到fileList的div中
          			$("#fileList").append("<div id="+file.id+"><img/><span>"+file.name+"</span><div><span class='percentage'></span></div><span class='state'></span></div>");
          			
          			//制作缩略图
          			//file
          			//error:不是图片，则有error
          			//src:代表生成后的缩略图的地址
          			uploader.makeThumb(file,function(error,src){
          				//判断是否已经成功生成缩略图
          				if(error){
          					$("#"+file.id).find("img").replaceWith("无法预览");
          				}
          				
          				//成功
          				$("#"+file.id).find("img").attr("src",src);
          			
          			});
          			
          			
          		});
          		
          		//5.在上传的过程中实现文件上传监控
          		//percentage：代表的时候文件的百分比： 0.15   1
          		uploader.on("uploadProgress",function(file,percentage){
          			$("#"+file.id).find("span.percentage").text(Math.round(percentage*100)+"%");
          		});
          			
          			
          </script>
          
          
          
          
          
  </body>
</html>
