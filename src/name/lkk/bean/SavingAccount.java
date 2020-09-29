package name.lkk.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author: linkirk
 * @date: 2020/9/29 11:31
 * @description:
 */
public class SavingAccount extends Account {

    public SavingAccount(BigInteger accountId, String accountName, Integer accountPassword, BigDecimal accountBalance) {
        super(accountId, accountName, accountPassword, accountBalance);
    }

    @Override
    public boolean depoist(BigDecimal money) {
        if (money.compareTo(new BigDecimal(0.00)) == 1) {
            setAccountBalance(getAccountBalance().add(money));
            return true;
        }
        return false;
    }

    /**
     * 储蓄卡取款
     *
     * @param money 取款金额
     * @return
     */
    @Override
    public boolean withdraw(BigDecimal money) {
        if (money.compareTo(getAccountBalance()) == -1) {
            setAccountBalance(getAccountBalance().subtract(money));
            System.out.println("取款成功");
            return true;
        } else {
            System.out.println("余额不足，取款失败");
        }
        return false;

    }
}
