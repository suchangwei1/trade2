var _const = {
    SMS_CAPTCHA_KEY : "_s_",
    VOICE_CAPTCHA_KEY : "_v_",
    EMAIL_CAPTCHA_KEY : "_e_",
    IMAGE_CAPTCHA_KEY : "_i_",
    CAPTCHA_SECONDS : 60,
    SUBMIT_FLAG : "val"
}

var _handler = {
    showHandlerTips : function(btn, secs){
        var _this = $("#" + btn);
        if(secs > 0){
            _this.text(secs + "s");
        }else{
            var name = _this.data('name');
            if(name){
                _this.text(name);
            }else{
                _this.text("发送验证码");
            }
            _this.data(_const.SUBMIT_FLAG, true);
        }
    },
    updateTipsSeconds : function(btn, secs){
        if(secs <= 0){
            return;
        }
        var _this = $("#" + btn);
        _this.data(_const.SUBMIT_FLAG, false);
        secs = secs*1;
        for(var i=0; i<=secs; i++){
            setTimeout("_handler.showHandlerTips('" + btn + "', " + (secs - i) + ")", i * 1000);
        }
    },
    cleanTipsHandler : function(key){
        var strs = $.cookie(key);
        if(!strs || {} == strs){
            return;
        }
        var time = 0;
        var vals = strs.split("\|");
        for(var i=0; i<vals.length; i++){
            var val = vals[i];
            if(val){
                var btnVals = val.split("_");
                var _t = btnVals[1] * 1;
                var secs = _const.CAPTCHA_SECONDS - ((new Date().getTime() - (btnVals[1] * 1)) / 1000).toFixed(0);
                if(secs <= 0){
                    strs.replace("\|" + val, '');
                }else if(_t > time){
                    time = _t + (_const.CAPTCHA_SECONDS * 1000);
                }
            }
        }
        if("\|" == strs){
            $.removeCookie(key);
        }else{
            var date = new Date(time);
            $.cookie(key, strs, {expires : date, path : '/'});
        }
        return strs;
    },
    addTipsHandler : function(key, btn){
        _handler.cleanTipsHandler(key);
        var vals = $.cookie(key);
        if(!vals || {} == vals){
            vals = '|';
        }
        var date = new Date();
        var minsecs = date.getTime();
        vals += btn + "_" + minsecs + "|";
        date.setSeconds(date.getSeconds() + _const.CAPTCHA_SECONDS);
        $.cookie(key, vals, {expires : date, path : '/'});
    },
    recoverHandler : function(key){
        var strs = _handler.cleanTipsHandler(key);
        if(!strs){
            return;
        }

        var vals = strs.split("\|");
        for(var i=0; i<vals.length; i++){
            var val = vals[i];
            if(val){
                var btnVals = val.split("_");
                var secs = _const.CAPTCHA_SECONDS - ((new Date().getTime() - (btnVals[1] * 1)) / 1000).toFixed(0);
                _handler.updateTipsSeconds(btnVals[0], secs);
            }
        }
    }
}

$(function() {
    // 发送验证码读秒
    _handler.recoverHandler(_const.SMS_CAPTCHA_KEY);
    _handler.recoverHandler(_const.VOICE_CAPTCHA_KEY);
    _handler.recoverHandler(_const.EMAIL_CAPTCHA_KEY);

})