package com.yan.analysispageinfo;

import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since 2021/3/17
 */
public class test {
    @Test
    public void test() {
//        [3,2,1,5,6,4]
//        3
        ArrayList<Integer> arr = new ArrayList<>();
        ArrayList<Integer> minArr = new ArrayList<>();
        arr.add(3);
        arr.add(2);
        arr.add(1);
        arr.add(5);
        arr.add(6);
        arr.add(4);
        for (int i = 0; i < arr.size(); i++) {
            putNum2Arr(i, 3, minArr);
        }
        addStrings("0","0");
    }

    public void putNum2Arr(int num, int k, ArrayList<Integer> arr) {
        int l = 0;
        int r = arr.size();
        while (l < r) {
            int mid = l+(r - l) / 2;
            int midNum = arr.get(mid);
            if (num >= midNum) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        arr.add(l, num);

        if (arr.size() > k) {
            arr.remove(k);
        }
    }


    public String addStrings(String num1, String num2) {
        if (num1.length() <= 0 && num2.length() <= 0) return "";

        String max = num1;
        String min = num2;

        if (num1.length() > num2.length()) {
            max = num1;
            min = num2;
        } else if (num1.length() < num2.length()) {
            max = num2;
            min = num1;
        }

        int offset = max.length() - min.length();
        for (int i = 0; i < offset; i++) {
            min = "0" + min;
        }


        int index = max.length() - 1;
        StringBuilder res = new StringBuilder();
        char more = '0';
        while (index >= 0) {
            char c1 = num1.charAt(index);
            char c2 = num2.charAt(index);

            char cSum = (char) (c1 + c2 + more);
            if (cSum > '9') {
                res.append((char)(cSum - '9'));
                more = '1';
            } else {
                res.append(cSum);
            }
            index--;
        }
        if (more > '0') res.append(more);
        return res.reverse().toString();
    }

}
