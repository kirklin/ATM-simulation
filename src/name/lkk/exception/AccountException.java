package name.lkk.exception;

import name.lkk.bean.Account;

/**
 * @author: linkirk
 * @date: 2020/10/9 16:44
 * @description:
 */
public class AccountException extends RuntimeException{
    static final long serialVersionUID = 8786311841541922913L;
    public AccountException(){

    }

    public AccountException(String msg, Account account){
        super(msg);
        account.setLock(true);
        account.setOnline(false);
        System.out.println("您的账号已锁定，请联系人工客服");
    }

}

