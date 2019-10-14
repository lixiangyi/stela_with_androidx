package com.stela.comics_unlimited.stela_new_app;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
        int oldCapacity = 5;
        // 扩展为原来的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        System.out.print("newCapacity" + newCapacity);
    }

    @Test
    public void doProxy() {
        MaotaiJiu maotaijiu = new MaotaiJiu();
        Wuliangye wu = new Wuliangye();
        Furongwang fu = new Furongwang();

        InvocationHandler jingxiao1 = new GuitaiA(maotaijiu);
        InvocationHandler jingxiao2 = new GuitaiA(wu);
        InvocationHandler jingxiao3 = new GuitaiA(fu);

        SellWine dynamicProxy = (SellWine) Proxy.newProxyInstance(MaotaiJiu.class.getClassLoader(),
                MaotaiJiu.class.getInterfaces(), jingxiao1);
        SellWine dynamicProxy1 = (SellWine) Proxy.newProxyInstance(Wuliangye.class.getClassLoader(),
                Wuliangye.class.getInterfaces(), jingxiao2);
        dynamicProxy.mainJiu();
        dynamicProxy1.mainJiu();

        SellCigarette dynamicProxy3 = (SellCigarette) Proxy.newProxyInstance(Furongwang.class.getClassLoader(),
                Furongwang.class.getInterfaces(), jingxiao3);
        dynamicProxy3.sell();

        System.out.println("dynamicProxy class name:" + dynamicProxy.getClass().getName());
        System.out.println("dynamicProxy1 class name:" + dynamicProxy1.getClass().getName());
        System.out.println("dynamicProxy3 class name:" + dynamicProxy3.getClass().getName());
    }

    int[] nums = {2, 7, 11, 15};

    //    @Test
//    public int[] twoSum(int[] nums, int target) {
////        int[] numsnew = new int[nums.length];
////        for (int i = 0; i < nums.length; i++) {
////            if (nums[i]<target) {
////
////            numsnew[i]= nums[i];
////            }else {
////                numsnew[i]= 0;
////            }
////        }
//        ArrayList<Integer> numList = new ArrayList<>();
//        for (int i = 0; i < nums.length; i++) {
//            if (nums[i]<target) {
//                numList.add(nums[i]);
//            }
//        }
//        for(int i=0;i<numList.size()-1;i++) {
//            for (int j = i; j < numList.size() - 1 - i; j++) {
//                if (nums[i] + nums[j] == target) {
//
//                }
//            }
//        }
//    }
    class out {
        public out() {
            System.out.println("out");
        }

        {
            System.out.println("out 代码块");
        }

        public Inner getInnerClass() {
            return new Inner() {
                private int num;

                @Override
                public void run() {
//                    super.run();
//                    System.out.println( ": run");
                    System.out.println("step =" + num);
                }

                public Inner accept(int s) {
                    num = s;
                    return this;
                }

                {
//                    System.out.println("我是匿名内部类，我是： " );
                    System.out.println("我是匿名内部类，我是： " + this.getClass().toString());
                }
            }.accept( step(10));
        }
    }

    static {
        System.out.println("外部类 静态 代码块");
    }

    {
        System.out.println("外部类 非静态 代码块");
    }

    class Inner extends out {
        public Inner() {
            System.out.println("Inner");
        }

        public void run() {
            System.out.println("Inner run");
        }

        {
            System.out.println("Inner 代码块");
        }

    }

    @Test
    public void testInnerFun() {
//        System.out.println("我是： " + this.getClass().toString());
//        new Inner().getInnerClass().run();
        int num = 0;
        for(int i = 0; i < 5; ++i)
        {
            num+=i;
        }
        System.out.println("num ++i="+num);
        num = 0;
        for(int i = 0; i < 5; i++)
        {
            num+=i;
        }
        System.out.println("num i++="+num);

    }

    public static void main(String[] args) {
        System.out.println("外部类 main");
        System.out.println( step(3));

    }

    public static int step(int num) {
        if(num==1)
            return  1;
        if (num==2)
            return 2;
//        if (num==3)
//            return  4;
        if (num>2)
            return  step(num)+ step(num-1);
        return 0;
    }
//)+step(num-2)

}