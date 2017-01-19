$(function(){
	$('#getCode_btn').click(function(){
		var _this=$(this);
		var phone =$('#phone').val().trim();
		if(!validate_phone('phone')){
			return false;
		}
		$.ajax({
			url:userpath+'/weixin/sendMsg/'+phone,
			type:'post',
			async:false,
			success:function(data){
				console.log(data);
				if(data.errcode==0){
					alert("验证码发送成功，请注意查收！");
					getCode(_this);
				}else{
					alert(data.errmsg);
				}
			},
			error:function(data){
				alert("系统错误，验证码发送失败");
			}
		}) 
	})
	$('#register_btn').click(function(){
		if(!validate_phone('phone')){
			return false;
		}
		if(!validate_empty('password',"密码")){
			return false;
		}
		if(!validate_empty('msgCode',"验证码")){
			return false;
		}
		//序列化表单数据
		var data=$('#register_form_btn').serializeJson();
		console.log(data);
		$.ajax({
			url : userpath+'/user/register',
			type : 'post',
			async:false,
			contentType : 'application/json',
			data : JSON.stringify(data),
			success:function(data){
				console.log(data);
				if(data.errcode==0){
					alert("注册成功");
					location.href="index.html";
				}else{
					alert("注册失败");
				}
			}
		})
	})
})