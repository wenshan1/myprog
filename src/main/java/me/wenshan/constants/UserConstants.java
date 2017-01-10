package me.wenshan.constants;

public class UserConstants{
  private UserConstants(){
  }

  public static final String USER_STATUS_NORMAL = "N";
  public static final String USER_STATUS_CLOSE = "C";
  public static final String USER_STATUS_FROZEN = "F";
  public static final String USER_STATUS_DELETE = "D";
  
  public static final int USER_ROLE_ADMIN = 0; //"admin";  
  public static final int USER_ROLE_EDITOR = 1; //"editor";
  /* 投稿者 */
  public static final int USER_ROLE_CONTRIBUTOR= 2; //"contributor";

}
