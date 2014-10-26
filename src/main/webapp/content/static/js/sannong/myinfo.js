;(function($) {

     $(function() {
         var emailRegEx = /^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/i,
             mobileRegEx = /^0?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/,
             userNameRegEx = /^[a-zA-Z0-9]\w+$/,
             passRegEx = /^.{5,25}$/,
             numRegEx = /^[_\d]+$/;
         var errorSpan = $('#error-row');
         var username_hash = {};
         var store = {};
         //解决firefox下刷新不了用户名的问题
         //$('#userName').val('');
         var username_valid = function(ev) {
             var showError = function($obj, txt) {
                 var $td = $obj.parent();
                 $td.find(".errorDiv").html(txt);
                 $td.find("i").css("display", "inline-block").removeClass("icon_yes");
             };
             var showTrue = function($obj) {
                 var $td = $obj.closest("td");
                 $td.find(".errorDiv").html("");
                 $td.find("i").css("display", "inline-block").removeClass("icon_no").addClass("icon_yes")
             };

            $user = $('#userName');

            var userName = $.trim($user.val());

            var sep = ',', isErr = 0, msg = '';
             if(!userName || userName.length < 4 || userName.length > 16 || !userNameRegEx.test(userName)) {
                 msg = '请输入4-16个字符，支持英文或英文与数字组合';
                 showError($user, msg);
                 isErr = 1;
            } else if(numRegEx.test(userName)){
                 msg = '不能只有数字或下划线';
                 showError($user, msg);
                 isErr = 1;
            } else if ( userName in username_hash ) {
                 msg = '该用户名已被使用';
                 showError($user, msg);
                 isErr = 1;
            }
            //console.log('eee', firstp2p_username_hash.indexOf( (sep + userName + sep) ));
            if (isErr) {
                $user.get(0).setAttribute("iserr", isErr);
                $user.get(0).setAttribute("msg", msg);
                return false;
            }
 
            $user.attr("iserr", isErr);
            $user.attr("msg", msg);
         };
         var valid = function(ev) {
             var hasError = false,
             $user = $('#userName');
             $('#register-form').find(".errorDiv").html("");
             $('#register-form').find("i").removeClass();
             var showError = function($obj, txt) {
                 var $td = $obj.closest("td");
                 $td.find(".errorDiv").html(txt);
                 $td.find("i").css("display", "inline-block").removeClass("icon_yes");
             };
             var showTrue = function($obj) {
                 var $td = $obj.closest("td");
                 $td.find(".errorDiv").html("");
                 $td.find("i").css("display", "inline-block").removeClass("icon_no").addClass("icon_yes")
             };
             if ($user.attr("iserr")) {
                 if ( $user.attr("iserr") == '1') {
                         showError($user, $user.attr("msg") || '');
                         hasError = true;
                         return false;
                 } else  {
                         showTrue($user);
                         hasError = false;
                 } 
             } else {
                         var msg = '请输入4-16个字符，支持英文或英文与数字组合';
                         showError($user, msg);
                         hasError = true;
                         return false;
             }
              /*
              var userName = $.trim($user.val());
                 if(!userName || userName.length < 4 || userName.length > 16 || !userNameRegEx.test(userName)){
                     showError($user, '请输入4-16个字符，支持英文或英文与数字组合');
                     hasError = true;
                     return false;
                 } else if(numRegEx.test(userName)){
                     showError($user, '不能只有数字或下划线');
                     hasError = true;
                     return false;
                 } else {
                     showTrue($user);
                     hasError = false;
                 }
                 
                */
                 if (!$('#input-password').val() || !passRegEx.test($('#input-password').val())) {
                     showError($('#input-password'), '5-25个英文、数字、字符，区分大小写');
                     hasError = true;
                     return false;
                 } else {
                     showTrue($('#input-password'));
                     hasError = false;
                 }

                 if (!$('#input-retype').val()) {
                     showError($('#input-retype'), '确认密码不能为空');
                     hasError = true;
                     return false;
                 }else if ($('#input-password').val() != $('#input-retype').val()) {
                     showError($('#input-retype'), '两次填写的密码不一致');
                     hasError = true;
                     return false;
                 } else {
                     showTrue($('#input-retype'));
                     hasError = false;
                 }
                 
                 if (!$.trim($('#input-email').val())) {
                     showError($('#input-email'), '请填写有效的邮箱地址');
                     hasError = true;
                     return false;
                 }else if (!emailRegEx.test($.trim($('#input-email').val()))) {
                     showError($('#input-email'), '请填写有效的邮箱地址');
                     hasError = true;
                     return false;
                 } else {
                     showTrue($('#input-email'));
                     hasError = false;
                 }
                 if (!$.trim($('#cellphone').val())) {
                     showError($('#cellphone'), '请填写有效的手机号');
                     hasError = true;
                     return false;
                 }else if (!mobileRegEx.test($.trim($('#input-mobile').val()))) {
                     showError($('#cellphone'), '请填写有效的手机号');
                     hasError = true;
                     return false;
                 } else {
                     showTrue($('#input-mobile'));
                     hasError = false;
                 }
                 

                  if (!$.trim($('#input-code').val())) {
                     showError($('#input-code'), '请填写6位数字验证码');
                     hasError = true;
                     return false;
                  }else if(!/^\d{6}$/.test($('#input-code').val())){
                     showError($('#input-code'), '请填写6位数字验证码');
                     hasError = true;
                     return false;
                  }else {
                     showTrue($('#input-code'));
                     hasError = false;
                  }

                 if (!$('#agree').is(':checked')) {
                     
                     var $td = $('#agree').closest("td");
                     $td.find(".errorDiv").html('不同意注册协议无法完成注册');
                     
                     hasError = true;
                     return false;
                 } else {
                    var $td = $('#agree').closest("td");
                     $td.find(".errorDiv").html('');
                     hasError = false;
                 }
            
             if ( !! hasError ) {
                 ev.preventDefault();
                 return false;
             }
             return true;
         };
         $('#register-btn').click(function(ev) {
//         $('#register-form').submit(function(ev) {
             if (!valid(ev)) {
                 return false;
             }
             else
             {
                 var url = '/user/CheckInvitecode';
                 var invite_code = $('#input-invite').val();
                 if(invite_code && invite_code != '')
                 {
                        var returnStatus = false;
                        $('.register-btn').addClass("gray");
                        $('.register-btn').attr('disabled',true);
                        $.ajax({
                            type: "get",
                            data: {code:invite_code},
                            url: url,
                            success: function(data) {
                                if(data == '1')
                                {
                                     returnStatus = true;
                                    $('#register-form').submit();
                                }
                                else
                                {
                                     var t = $.weeboxs.open('您的邀请码无效，注册成功后无法获得邀请返利，是否要继续？', 
                                            {
                                                boxid:'fanwe_error_box',
                                                contentType:'text',
                                                showButton:true, 
                                                showCancel:true, 
                                                showOk:true,
                                                title:'提示',
                                                okBtnName:'继续注册',
                                                cancelBtnName:'返回修改',
                                                width:340,
                                                type:'wee',
                                                onok:function(){
                                                    //防止重复点击
                                                    $('.register-btn').addClass("gray").attr('disabled',true);
                                                    $('#register-form').submit();
                                                },
                                                oncancel:function(){$.weeboxs.close();}                                         
                                            });
                                     if(returnStatus === false)
                                     {
                                        $('.register-btn').removeClass("gray");
                                        $('.register-btn').attr('disabled',false);                                         
                                     }
                                }
                         }
                   });
                 }
                 else
                 {
                    //防止重复点击
                    $('.register-btn').addClass("gray").attr('disabled',true);
                    $('#register-form').submit();
                 }
                 return returnStatus;
             }
         });

         $('#userName').on("blur", function(ev) {
            username_valid(ev);
         });

         $('#register-form').on("blur focus", ".text", function(ev) {
             valid(ev);
         });

         $('#register-form').on("click", "#agree", function(ev) {
             valid(ev);
         });
         var input_code = $('#input-invite');
         if(input_code)
         {
             input_code.focus(function(){
                 $('.invite-code').show();
             });
             input_code.blur(function(){
                 $('.invite-code').hide();
             });
         }
         //发送短信请求ajax
        
         var sendNum = 1;  //判断是否第一次点击发送
         
         $('#action-send-code').removeAttr('disabled').click(function(ev) {
             var phone = $("#cellphone").val();
             var mobileRegEx = /^0?(13[0-9]|15[0-9]|18[0-9]|14[57]|17[0-9])[0-9]{8}$/;
             var errorSpan = $(this).parent().find(".errorDiv");
             var button = $(this);
             var token_id = $("#token_id").val();
             var token = $("#token").val();
             var btGray = function(){
                    button.addClass("gray");
                    button.val("正在获取中...");
                    button.attr('disabled', 'disabled');
                   // updateTimeLabel(180);
             };

             
             errorSpan.html('');
             if (!mobileRegEx.test(phone)) {
                 errorSpan.html('手机号格式不正确');
                 return false;
             }
             btGray();
             function updateTimeLabel(duration) {
                 var timeRemained = duration;
                 var timer = setInterval(function() {
                     button.val(timeRemained + '秒后重新发送');
                     timeRemained -= 1;
                     if (timeRemained == -1) {
                         clearInterval(timer);
                         button.val('重新发送').removeAttr('disabled').removeClass("gray");
                     }
                 }, 1000);
             }
             
             var sendMsg = function(url,smstype){
                   sendNum = 2;
                   $.ajax({
                       type: "get",
                       data: {
                          type : '1',
                          smstype : smstype,
                          t : new Date().getTime(),
                          mobile : phone,
                          token : token,
                          token_id : token_id
                       },
                       url: url,
                       //async: false,
                       dataType: "json",
                       success: function(data) {
                           
                           if (data==true) {
                               updateTimeLabel(60, 'action-send-code');
                               return;
                           } else {
                               $.showErr(data.message, function() {}, "提示");
                           }
                           button.val('重新发送').removeAttr('disabled').removeClass("gray");
                       }
                   });
             };
             //updateTimeLabel(180, 'action-send-code');
             if(sendNum == 1){
                   sendMsg(button.data("url"),button.data("type"));
             }else{
                   $("#cellphone").parent().find(".errorDiv").text('*如未收到验证码，再次发送');
                   sendMsg(button.data("url2"),button.data("type")); 
             }
             
         });
     })
 })(jQuery);
 //用于未来扩展的提示正确错误的JS
$.showErr = function(str,func,title)
{
    $.weeboxs.open(str, {boxid:'fanwe_error_box',contenpe:'text',showButton:true, showCancel:false, showOk:true,title:title,width:250,type:'wee',onclose:func});
}; 