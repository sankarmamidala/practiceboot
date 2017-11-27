
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head><title>JavaScript Captcha Example</title></head>
<body onload="alphanumeric_unique()">
 
<script>
var captcha;
 
function alphanumeric_unique() {
	captcha=  Math.random().toString(36).split('').filter( function(value, index, self) { 
        return self.indexOf(value) === index;
    }).join('').substr(3,7);
    
    document.getElementById("captcha").value = captcha;
}
 
function check(){
    var input=document.getElementById("inputText").value;
 
    if(input==captcha){
        alert("**Valid Captcha**");
        
        document.getElementById("capid").submit();
    }
    else{
        alert("**Invalid Captcha**");
        alphanumeric_unique();
        document.getElementById("inputText").value="";
    }
}
</script>
 <form action="captchasubmit.jsp" method="POST" id="capid"> 
<input type="text" id="captcha" disabled/><br/><br/>
<input type="text" id="inputText"/><br/><br/>
<input type="button" onclick="alphanumeric_unique()" value="refresh"/>
<input type="button" onclick="check()" value="submit"/>
 
 
 </form>
</body>
</html>