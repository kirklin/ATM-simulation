package name.lkk.bean;

import name.lkk.exception.AccountException;
import name.lkk.exception.PasswordException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * @author: linkirk
 * @date: 2020/9/29 10:56
 * @description:
 */
public class AutomaticTellerMachine {
    private String atmName;
    private int atmId;
    private Account account;
    private boolean isLogin;

    public AutomaticTellerMachine(String atmName, int atmId, Account account) {
        this.atmName = atmName;
        this.atmId = atmId;
        this.account = account;
    }

    public String getAtmName() {
        return atmName;
    }

    public void setAtmName(String atmName) {
        this.atmName = atmName;
    }

    public int getAtmId() {
        return atmId;
    }

    public void setAtmId(int atmId) {
        this.atmId = atmId;
    }

    public Account getAccount() {
        return account;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "{取款机名字= '" + atmName + '\'' +
                ", 取款机ID= " + atmId +
                '}';
    }

    /**
     * ATM存款操作
     *
     * @param money
     * @return
     */
    public BigDecimal atmDepoist(BigDecimal money) {
        if (money.remainder(new BigDecimal(100.00)).compareTo(new BigDecimal(0.00)) == 0) {
            if (account.depoist(money)) {
                System.out.println("存款成功");
            } else {
                System.out.println("存款失败");
            }
        } else {
            System.out.println("存款失败，每次存款金额为100的倍数");
        }
        return account.getAccountBalance();
    }

    /**
     * ATM取款操作
     *
     * @param money
     * @return
     */
    public BigDecimal atmwithdraw(BigDecimal money) {
        if (money.remainder(new BigDecimal(100.00)).compareTo(new BigDecimal(0.00)) == 0) {
            account.withdraw(money);
        } else {
            System.out.println("取款失败,每次取款金额为100的倍数");
        }
        return account.getAccountBalance();
    }

    /**
     * 判断账号登陆状态
     *
     * @return
     */
    public boolean isLogin(Account loginAccount) {
        if (isLogin) {
            return true;
        } else {
            System.out.println("您还未登陆账号，请进行登陆操作");
            System.out.println("请输入账号：");
            Scanner in = new Scanner(System.in);
            BigInteger id = in.nextBigInteger();
            account.setAccountId(id);
            if (!loginAccount.isLock()){
                for (int i = 0; i < 3; i++) {
                    System.out.println("请输入密码：");
                    Integer pwd = in.nextInt();
                    if (isLogin = account.login(id, pwd)) {
                        System.out.println("登陆成功");
                        isLogin = true;
                        break;
                    } else {
                        isLogin = false;
                        try {
                            throw new PasswordException("登录失败");
                        } catch (PasswordException e) {
                            System.out.println("您还有" + (2 - i) + "次机会");
                        }
                        if ((2-i)==0){
                            try {
                                throw new AccountException("您的账号已锁定",account);
                            } catch (AccountException e) {
                            }
                        }
                    }
                }
            }else{
                System.out.println("您的账号已锁定，请联系人工客服");
                return false;
            }
        }
        return isLogin;
    }

    /**
     * 判断账号锁定状态
     * @return
     */
    public boolean isLock(){
        return account.isLock();
    }

    /**
     * 锁定账号
     */
    public void lockAccount(){
        account.setLock(true);
    }
}
