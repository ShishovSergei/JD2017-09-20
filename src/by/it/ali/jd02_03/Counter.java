package by.it.ali.jd02_03;

import java.util.Deque;
import java.util.LinkedList;

public class Counter {
    static final Integer planBuyer=10;
    volatile static int countBuyer=0;
    static final Deque<Buyer> buyerQueue=new LinkedList<>();
}
