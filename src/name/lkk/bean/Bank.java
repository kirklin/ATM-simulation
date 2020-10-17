package name.lkk.bean;

import name.lkk.exception.AccountException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author: linkirk
 * @date: 2020/9/29 10:40
 * @description:银行类
 */

public class Bank {
    private String bankName;
    private List<AutomaticTellerMachine> atms;
    static Scanner in = new Scanner(System.in);
    static double TotalBalence;
    static final double MAX_OPERATION = 5000;
    static final int VERIFICATION_SAME_PWD = 111111;
    static final Integer PASSWORD_LENGTH = 6;
    static int count = 0;

    private Bank() {
    }

    private Bank(String bankName, List<AutomaticTellerMachine> atms) {
        this.bankName = bankName;
        this.atms = atms;
    }

    private static Bank instance;

    /**
     * 使用单例模式创建银行
     *
     * @return：返回银行对象
     */
    public static synchronized Bank getInstance() {
        if (instance == null) {
            Map<Integer,Account> accountMap = new HashMap<>();
            accountMap.put(123456,new SavingAccount(new BigInteger("123456"), "KK",
                    123456, new BigDecimal(10000)));
            List<AutomaticTellerMachine> automaticTellerMachineList = new ArrayList<>();
            automaticTellerMachineList.add(new AutomaticTellerMachine("一号取款机", 1, accountMap));
            automaticTellerMachineList.add(new AutomaticTellerMachine("二号取款机", 2, accountMap));
            automaticTellerMachineList.add(new AutomaticTellerMachine("三号取款机", 3, accountMap));
            instance = new Bank("kk银行", automaticTellerMachineList);
        }
        return instance;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<AutomaticTellerMachine> getAtms() {
        return atms;
    }

    public void setAtms(List<AutomaticTellerMachine> atms) {
        this.atms = atms;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bankName='" + bankName + '\'' +
                ", atms=" + atms +
                '}';
    }

    public void start() {
        TotalBalence = 0.0;
        System.out.println("------------------欢迎来到" + bankName + "---------------------");
        System.out.println("请按ID选择提款机:");
        AutomaticTellerMachine ATM = atms.get(chooseATM());
        Map<Integer, Account> accountMap = ATM.getAccountMap();
        while (true){
            System.out.println("请登陆您的账号");
            int accountId = in.nextInt();
            //判断账号是否存在
            if (accountMap.containsKey(accountId)){

                if (accountMap.get(accountId).isLock()){
                    System.out.println("您的账号已锁定，请联系人工客服");
                }else{
                    //登陆账号
                    ATM.isLogin(accountMap.get(accountId));
                    while (accountMap.get(accountId).isOnline()) {
                        System.out.println();
                        System.out.println("-----------------------------------------------------");
                        System.out.println("|                                                   |");
                        System.out.println("|                    请选择功能:                      |");
                        System.out.println("|                                                   |");
                        System.out.println("|       1.取款 2.存款 3.查询余额 4.修改密码 5.注销       |");
                        System.out.println("|                                                   |");
                        System.out.println("-----------------------------------------------------");
                        System.out.println();
                        menu(in.nextInt(),ATM,accountMap.get(accountId));
                    }
                }
            }else{
                System.out.println("账号不存在");
            }
        }
    }

    private void menu(int operation, AutomaticTellerMachine atm,Account account) {
        switch (operation) {
            case 1:
                //1.取款
                takeMoney(atm,account);
                break;
            case 2:
                //2.存款
                saveMoney(atm,account);
                break;
            case 3:
                //3.查询余额
                System.out.println("余额为：" + account.getAccountBalance());
                break;
            case 4:
                //4.修改密码
                updatePassword(atm,account);
                break;
            case 5:
                //5.注销
                System.out.println("感谢您的本次使用，欢迎下次访问该系统！");
                account.setOnline(false);
                break;
            default:
                System.out.println("你输入有误！请重新输入！！！");
                break;
        }

    }

    /**
     * 选择取款机
     *
     * @return
     */
    public int chooseATM() {
        for (AutomaticTellerMachine atm : atms) {
            System.out.println(atm);
        }
        int atmId;
        while (true) {
            atmId = in.nextInt();
            if (atmId > atms.size()) {
                System.out.println("没有该台取款机");
            } else {
                break;
            }
        }
        System.out.println("您选择了" + atmId + "号ATM机");
        return atmId - 1;
    }

    public void takeMoney(AutomaticTellerMachine atm,Account account) {
        System.out.println("请输入取款金额");
        BigDecimal operation = in.nextBigDecimal();
        if (TotalBalence <= MAX_OPERATION && TotalBalence >= 0) {
            TotalBalence += operation.doubleValue();
            System.out.println(atm.atmwithdraw(operation,account));
        } else {
            System.out.println("您今日取款已达上限5000元");
        }
    }

    public void saveMoney(AutomaticTellerMachine atm,Account account) {
        System.out.println("请输入存款金额");
        BigDecimal operation = in.nextBigDecimal();
        System.out.println(atm.atmDepoist(operation,account));
    }

    public void updatePassword(AutomaticTellerMachine atm,Account account) {
        if (account.isOnline()&&!account.isLock()){
            count++;
            //获取原密码
            System.out.println("请输入原密码：");
            Integer password = in.nextInt();
            //原密码正确，才可以继续修改密码
            if (password.equals(account.getAccountPassword())) {
                count = 0;
                System.out.println("请输入新密码：");
                Integer newPassword1 = in.nextInt();

                //判断是否是六位数密码
                if (newPassword1.toString().length()==PASSWORD_LENGTH) {
                    //判断六位密码是否相同
                    if (Integer.parseInt(newPassword1.toString())%VERIFICATION_SAME_PWD!=0) {
                        System.out.println("请再次输入新密码：");
                        Integer newPassword2 = in.nextInt();
                        //两次新密码匹配成功
                        if (newPassword1.equals(newPassword2)) {
                            System.out.println("密码修改成功，即将重新登录！！！");
                            account.setAccountPassword(newPassword1);
                            account.setOnline(false);
                            //重新登录
                        } else {//两次新密码输入不相同
                            System.out.println("您两次输入的新密码不相同,请重新操作！！！");
                            updatePassword(atm, account);
                        }
                    }else{
                        System.out.println("密码不能为相同的六位数");
                    }
                } else {
                    System.out.println("新密码必须为六位数");
                    updatePassword(atm,account);
                }

            } else {//原密码输入错误,或者账号被锁定

                if (count >= 3) {
                    try {
                        account.setOnline(false);
                        throw new AccountException("您三次输入的密码都不正确！！！账号已被锁定", account);
                    } catch (AccountException e) {
                        account.setOnline(false);
                        return;
                    }
                }
                System.out.println("您所输入的密码与原密码不相同，"+"剩余"+(3-count)+"次机会"+"，请重新输入：");
                //继续输入原密码
                updatePassword(atm,account);
            }
        }else {
            return;
        }


    }
}

