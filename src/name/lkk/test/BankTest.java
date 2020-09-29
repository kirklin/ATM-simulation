package name.lkk.test;

import name.lkk.bean.Bank;

/**
 * @author: linkirk
 * @date: 2020/9/29 15:35
 * @description:
 */
public class BankTest {
    public static void main(String[] args) {
        Bank bank = Bank.getInstance();
        bank.start();
    }
}
