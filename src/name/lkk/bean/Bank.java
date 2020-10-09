package name.lkk.bean;

import name.lkk.exception.AccountException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    static final Integer PASSWORD_LENGTH = 100000;
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
            SavingAccount account = new SavingAccount(new BigInteger("123456"), "KK",
                    new Integer(123456), new BigDecimal(10000));
            AutomaticTellerMachine automaticTellerMachine1 = new AutomaticTellerMachine("一号取款机", 1, account);
            AutomaticTellerMachine automaticTellerMachine2 = new AutomaticTellerMachine("二号取款机", 2, account);
            AutomaticTellerMachine automaticTellerMachine3 = new AutomaticTellerMachine("三号取款机", 3, account);
            List<AutomaticTellerMachine> automaticTellerMachineList = new ArrayList<>();
            automaticTellerMachineList.add(automaticTellerMachine1);
            automaticTellerMachineList.add(automaticTellerMachine2);
            automaticTellerMachineList.add(automaticTellerMachine3);
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
        System.out.println("请登陆您的账号");
        while (true){
            while (ATM.isLogin(ATM.getAccount())) {
                System.out.println();
                System.out.println("-----------------------------------------------------");
                System.out.println("|                                                   |");
                System.out.println("|                    请选择功能:                      |");
                System.out.println("|                                                   |");
                System.out.println("|       1.取款 2.存款 3.查询余额 4.修改密码 5.退出       |");
                System.out.println("|                                                   |");
                System.out.println("-----------------------------------------------------");
                System.out.println();
                menu(in.nextInt(), ATM);
            }
        }
    }

    private void menu(int operation, AutomaticTellerMachine atm) {
        switch (operation) {
            case 1:
                //1.取款
                takeMoney(atm);
                break;
            case 2:
                //2.存款
                saveMoney(atm);
                break;
            case 3:
                //3.查询余额
                System.out.println("余额为：" + atm.getAccount().getAccountBalance());
                break;
            case 4:
                //4.修改密码
                updatePassword(atm);
                break;
            case 5:
                //5.退出
                System.out.println("感谢您的本次使用，欢迎下次访问该系统！");
                System.exit(0);
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

    public void takeMoney(AutomaticTellerMachine atm) {
        System.out.println("请输入取款金额");
        BigDecimal operation = in.nextBigDecimal();
        if (TotalBalence <= MAX_OPERATION && TotalBalence >= 0) {
            TotalBalence += operation.doubleValue();
            System.out.println(atm.atmwithdraw(operation));
        } else {
            System.out.println("您今日取款已达上限5000元");
        }
    }

    public void saveMoney(AutomaticTellerMachine atm) {
        System.out.println("请输入存款金额");
        BigDecimal operation = in.nextBigDecimal();
        System.out.println(atm.atmDepoist(operation));
    }

    public void updatePassword(AutomaticTellerMachine atm) {

        System.out.println("请输入原密码：");
        count++;
        //获取原密码
        Integer password = in.nextInt();
        //原密码正确，才可以继续修改密码
        if (password.equals(atm.getAccount().getAccountPassword())) {
            count = 0;
            System.out.println("请输入新密码：");
            Integer newpassword1 = in.nextInt();
            System.out.println("请再次输入新密码：");
            Integer newpassword2 = in.nextInt();
            //判断是否是六位数密码
            if ((newpassword1 / PASSWORD_LENGTH) >= 1) {
                //两次新密码匹配成功
                if (newpassword1.equals(newpassword2)) {

                    System.out.println("密码修改成功，即将重新登录！！！");
                    atm.getAccount().setAccountPassword(newpassword1);
                    atm.setLogin(false);
                    //重新登录
                    atm.isLogin(atm.getAccount());

                } else {//两次新密码输入不相同
                    System.out.println("您两次输入的新密码不相同，请重新操作！！！");
                    updatePassword(atm);
                }
            } else {
                System.out.println("新密码必须大于六位数");
                updatePassword(atm);
            }

        } else {//原密码输入错误
            if (count >= 3) {
                try {
                    atm.setLogin(false);
                    throw new AccountException("您三次输入的密码都不正确！！！账号已被锁定", atm.getAccount());
                } catch (AccountException e) {
                }
            }
            System.out.println("您所输入的密码与原密码不相同，请重新输入：");
            //继续输入原密码
            updatePassword(atm);

        }
    }


}

