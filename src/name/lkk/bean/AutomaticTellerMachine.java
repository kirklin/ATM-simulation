package name.lkk.bean;

import name.lkk.exception.AccountException;
import name.lkk.exception.PasswordException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author: linkirk
 * @date: 2020/9/29 10:56
 * @description:
 */
public class AutomaticTellerMachine {
    private String atmName;
    private int atmId;
    private Map<Integer,Account> accountMap;

    public AutomaticTellerMachine(String atmName, int atmId, Map<Integer, Account> accountMap) {
        this.atmName = atmName;
        this.atmId = atmId;
        this.accountMap = accountMap;
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


    public Map<Integer, Account> getAccountMap() {
        return accountMap;
    }

    public void setAccountMap(Map<Integer, Account> accountMap) {
        this.accountMap = accountMap;
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
    public BigDecimal atmDepoist(BigDecimal money, Account account) {
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
    public BigDecimal atmwithdraw(BigDecimal money, Account account) {
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

    public boolean isLogin(Account account) {
        if (account.isOnline() && !account.isLock()) {
            return true;
        } else if (account.isOnline() && account.isLock()) {
            System.out.println("您的账号已锁定，请联系人工客服");
            return false;
        } else {
            Scanner in = new Scanner(System.in);
            BigInteger id = account.getAccountId();
            for (int i = 0; i < 3; i++) {
                System.out.println("请输入密码：");
                int pwd = in.nextInt();
                if (account.login(id, pwd)) {
                    System.out.println("登陆成功");
                    account.setOnline(true);
                    break;
                } else {
                    try {
                        throw new PasswordException("登录失败");
                    } catch (PasswordException e) {
                        System.out.println("您还有" + (2 - i) + "次机会");
                    }
                    if ((2 - i) == 0) {
                        try {
                            throw new AccountException("您的账号已锁定", account);
                        } catch (AccountException e) {
                            return false;
                        }
                    }
                }
            }
        }
        return account.isOnline();
    }

    /**
     * 判断账号锁定状态
     *
     * @return
     */
    public boolean isLock(Account account) {
        return account.isLock();
    }

    /**
     * 锁定账号
     */
    public void lockAccount(Account account) {
        account.setLock(true);
    }
}
