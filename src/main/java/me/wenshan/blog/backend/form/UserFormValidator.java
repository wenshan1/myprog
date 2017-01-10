package me.wenshan.blog.backend.form;

import me.wenshan.userinfo.domain.User;
import me.wenshan.util.CommRegular;
import me.wenshan.util.MapContainer;
import me.wenshan.util.StringUtils;

public class UserFormValidator{

  public static MapContainer validateInsert(User user, String repass){
    MapContainer form = validateUser(user);
    if(StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(repass)){
      form.put("password", "需填写用户密码");
    }
    if(!user.getPassword().equals(repass) || !user.getPassword().matches(CommRegular.PASSWD)){
      form.put("password", "两次密码不一致或者密码格式不对");
    }

    return form;
  }

  public static MapContainer validateUpdate(User user, String repass){
    MapContainer form = validateUser(user);
    if(!StringUtils.isBlank(user.getPassword())
        && (!user.getPassword().equals(repass) || !user.getPassword().matches(CommRegular.PASSWD))){
      form.put("password", "两次密码不一致或者密码格式不对");
    }else if(StringUtils.isBlank(user.getId())){
      form.put("msg", "ID不合法");
    }

    return form;
  }

  private static MapContainer validateUser(User user){
    MapContainer form = new MapContainer();
    if(StringUtils.isBlank(user.getNickName())){
      form.put("nickName", "需填写用户名称");
    }
    if(StringUtils.isBlank(user.getEmail()) || !user.getEmail().matches(CommRegular.EMAIL)){
      form.put("email", "邮箱格式不正确");
    }
    if(StringUtils.isBlank(user.getRealName())){
      form.put("realName", "需填写用户真实名称");
    }

    return form;
  }
  
  public static MapContainer validateMy(User user, String repass){
    MapContainer form = new MapContainer();
    if(StringUtils.isBlank(user.getEmail()) || !user.getEmail().matches(CommRegular.EMAIL)){
      form.put("email", "邮箱格式不正确");
    }
    if(StringUtils.isBlank(user.getRealName())){
      form.put("realName", "需填写用户真实名称");
    }
    
    if(!StringUtils.isBlank(user.getPassword())
        && (!user.getPassword().equals(repass) || !user.getPassword().matches(CommRegular.PASSWD))){
      form.put("password", "两次密码不一致或者密码格式不对");
    }else if(StringUtils.isBlank(user.getId())){
      form.put("msg", "ID不合法");
    }

    return form;
  }

}
