function validate_empty(domId,domName){
	var domValue=$('#'+domId).val().trim();
	if(domValue==''){
		alert(domName+"不能为空");
		$('#'+domId)[0].focus();
		return false;
	}
	return true;
}
function validate_phone(phoneId){
	var phone =$('#'+phoneId).val().trim();
	if(phone==''){
		alert('手机号不能为空');
		$('#'+phoneId)[0].focus();
		return false;
	}
	if(!(/^1[34578]\d{9}$/.test(phone))){ 
        alert("不是合法的手机号码"); 
        return false; 
    } 
	return true;
}

function set_time(time){
	return new Date(parseInt(time)).toLocaleString().replace(/:\d{1,2}$/,'');
}