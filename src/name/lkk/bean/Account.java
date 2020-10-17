package name.lkk.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author: linkirk
 * @date: 2020/9/29 10:40
 * @description:
 */
public abstract class Account {
    /**
     * 账户帐号
     */
    private BigInteger accountId;
    /**
     * 账户姓名
     */
    private String accountName;
    /**
     * 账户密码
     */
    private Integer accountPassword;
    /**
     * 账户余额
     */
    private BigDecimal accountBalance;
    /**
     * 账号锁定状态
     */
    private boolean lock = false;
    private boolean online = false;

    public Account() {
    }

    public Account(BigInteger accountId, String accountName, Integer accountPassword, BigDecimal accountBalance) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountPassword = accountPassword;
        this.accountBalance = accountBalance;
    }

    public BigInteger getAccountId() {
        return accountId;
    }

    public void setAccountId(BigInteger accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(Integer accountPassword) {
        this.accountPassword = accountPassword;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    @Override
    public String toString() {
        return "Account{" +
                "账号ID=" + accountId +
                ", 账号名称='" + accountName + '\'' +
                ", 账号密码=" + accountPassword +
                ", 账号余额=" + accountBalance +
                '}';
    }

    /**
     * 存款
     *
     * @param money 存款金额
     * @return 返回账户余额
     */
    public abstract boolean depoist(BigDecimal money);

    /**
     * 取款
     *
     * @param money 取款金额
     * @return 返回账户余额
     */
    public abstract boolean withdraw(BigDecimal money);

    /**
     * 登录
     *
     * @param accountId
     * @param accountPassword
     * @return
     */
    public boolean login(BigInteger accountId, Integer accountPassword) {
        return this.accountId.compareTo(accountId) == 0
                && this.accountPassword.intValue() == accountPassword.intValue();
    }

    /**
     * 账号在线状态
     * @return
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * 设置账号在线状态
     * @param online
     */
    public void setOnline(boolean online) {
        this.online = online;
    }
}

