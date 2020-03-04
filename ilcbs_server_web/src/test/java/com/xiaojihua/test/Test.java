package com.xiaojihua.test;

import org.apache.commons.collections.map.HashedMap;

import java.util.*;

public class Test {

    public static int[] twoSum(int[] nums, int target) {
        int diff = 0;
        Map<Integer,Integer> map = new HashMap<>();
        for(int i=0;i<nums.length;i++){
            diff = target - nums[i];
            if(map.get(nums[i]) != null){
                return new int[]{map.get(nums[i]),i};
            }
            map.put(diff,i);
        }

        return null;
    }




    public static void main(String[] args){
        int[] nums = {3,2,4};
        int[] result = twoSum(nums,6);
        System.out.println(result[0] + "," + result[1]);
    }


}
